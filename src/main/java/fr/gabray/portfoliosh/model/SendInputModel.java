package fr.gabray.portfoliosh.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendInputModel {

    private final String input;
    private final int width;

    public SendInputModel(@JsonProperty("input") final String input, @JsonProperty("width") final int width)
    {
        this.input = input;
        this.width = width;
    }

    public String getInput()
    {
        return input;
    }

    public int getWidth()
    {
        return width;
    }
}
