package tech.aurasoftware.aurautilities.command.parameter.implementation;


import tech.aurasoftware.aurautilities.command.parameter.Parameter;

public class IntegerParameter extends Parameter<Integer> {
    public IntegerParameter() {
        super(Integer.class);
    }

    @Override
    public Integer parse(String input) {
        try{
            int x = Integer.parseInt(input);

            if(x < 0){
                return null;
            }
            return x;
        }catch (NumberFormatException e) {
            return null;
        }

    }

}
