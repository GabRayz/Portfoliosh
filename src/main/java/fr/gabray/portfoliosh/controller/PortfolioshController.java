package fr.gabray.portfoliosh.controller;

import fr.gabray.portfoliosh.ast.Ast;
import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.exception.CommandRuntimeException;
import fr.gabray.portfoliosh.lexer.Lexer;
import fr.gabray.portfoliosh.model.SendInputModel;
import fr.gabray.portfoliosh.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Controller
public class PortfolioshController {
    private static final Logger logger = LoggerFactory.getLogger(PortfolioshController.class);

    private final SimpMessagingTemplate simpMessagingTemplate;

    public PortfolioshController(final SimpMessagingTemplate simpMessagingTemplate)
    {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/")
    public String home()
    {
        return "portfoliosh";
    }

    @MessageMapping("/send")
    public void send(@Payload final SendInputModel model, SimpMessageHeaderAccessor headerAccessor)
    {
        logger.debug(model.getInput());
        try
        {
            Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
            if (sessionAttributes == null)
                throw new CommandRuntimeException("Failed to initialize environment");
            Environment env = (Environment) sessionAttributes.computeIfAbsent("env", key -> Environment.defaultEnv());
            Parser parser = new Parser(new Lexer(model.getInput()));
            Ast ast = parser.parse();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ast.execute(env, outputStream);
            simpMessagingTemplate.convertAndSend("/sock/receive", new SendInputModel(outputStream.toString()));
        }
        catch (Exception e)
        {
            logger.error("Error while executing input", e);
            simpMessagingTemplate.convertAndSend("/sock/receive", new SendInputModel(e.getMessage()));
        }
    }
}
