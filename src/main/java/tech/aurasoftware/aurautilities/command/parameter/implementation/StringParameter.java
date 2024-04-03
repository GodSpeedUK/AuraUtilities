package tech.aurasoftware.aurautilities.command.parameter.implementation;

import tech.aurasoftware.aurautilities.command.parameter.Parameter;


public class StringParameter extends Parameter<String> {
    public StringParameter() {
        super(String.class);
    }

    @Override
    public String parse(String input) {
        return input;
    }
}
