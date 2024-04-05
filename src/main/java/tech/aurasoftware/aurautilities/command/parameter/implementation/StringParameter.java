package tech.aurasoftware.aurautilities.command.parameter.implementation;

import tech.aurasoftware.aurautilities.command.parameter.Parameter;

import java.util.ArrayList;
import java.util.List;


public class StringParameter extends Parameter<String> {
    public StringParameter() {
        super(String.class);
    }

    @Override
    public String parse(String input) {
        return input;
    }

    @Override
    public List<String> tabComplete(){
        return new ArrayList<>();
    }
}
