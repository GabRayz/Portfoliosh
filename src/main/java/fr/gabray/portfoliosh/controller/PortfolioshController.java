package fr.gabray.portfoliosh.controller;

import fr.gabray.portfoliosh.model.SendInputModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortfolioshController {

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
    public void send(final SendInputModel model)
    {
        System.out.println(model.getInput());
        simpMessagingTemplate.convertAndSend("/sock/receive", new SendInputModel("Hello world!"));
    }
}
