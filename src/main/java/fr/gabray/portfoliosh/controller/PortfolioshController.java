package fr.gabray.portfoliosh.controller;

import fr.gabray.portfoliosh.ast.Ast;
import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.exception.CommandRuntimeException;
import fr.gabray.portfoliosh.exception.EnvironmentInitException;
import fr.gabray.portfoliosh.exception.ParsingException;
import fr.gabray.portfoliosh.lexer.Lexer;
import fr.gabray.portfoliosh.model.SendInputModel;
import fr.gabray.portfoliosh.parser.Parser;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class PortfolioshController {
    private static final Logger logger = LoggerFactory.getLogger(PortfolioshController.class);
    public static final String SOCK_RECEIVE = "/sock/receive/";

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

    @MessageMapping("/send/{clientId}")
    public void send(@DestinationVariable("clientId") int clientId, @Payload final SendInputModel model, SimpMessageHeaderAccessor headerAccessor)
    {
        logger.debug(model.getInput());
        String socket = SOCK_RECEIVE + clientId;
        try
        {
            Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
            if (sessionAttributes == null)
                throw new EnvironmentInitException("Failed to initialize environment");
            Environment env = (Environment) sessionAttributes.computeIfAbsent("env", key -> Environment.defaultEnv());
            Parser parser = new Parser(new Lexer(model.getInput()));
            Ast ast = parser.parse();
            if (ast == null)
            {
                simpMessagingTemplate.convertAndSend(socket, new SendInputModel("", 0));
                return;
            }
            AutoWrapOutputStream outputStream = new AutoWrapOutputStream(model.getWidth());
            ast.execute(env, outputStream);
            simpMessagingTemplate.convertAndSend(socket, new SendInputModel(outputStream.toString(), 0));
        }
        catch (ParsingException e)
        {
            simpMessagingTemplate.convertAndSend(socket, new SendInputModel("parsing exception: " + e.getMessage(), 0));
        }
        catch (CommandRuntimeException e)
        {
            simpMessagingTemplate.convertAndSend(socket, new SendInputModel("portfoliosh: " + e.getMessage(), 0));
        }
        catch (Exception e)
        {
            logger.error("Error while executing input", e);
            simpMessagingTemplate.convertAndSend(socket, new SendInputModel("500 unexpected error", 0));
        }
    }
}
