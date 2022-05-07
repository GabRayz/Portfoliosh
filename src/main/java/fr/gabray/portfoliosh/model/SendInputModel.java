package fr.gabray.portfoliosh.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendInputModel {

    private final String input;

    public SendInputModel(@JsonProperty("input") final String input)
    {
        this.input = input;
    }

    public String getInput()
    {
        return input;
    }
}
