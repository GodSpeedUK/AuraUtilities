package tech.aurasoftware.aurautilities.command.parameter.implementation;


import tech.aurasoftware.aurautilities.command.parameter.Parameter;

public class DoubleParameter extends Parameter<Double> {

    public DoubleParameter() {
        super(Double.class);
    }

    @Override
    public Double parse(String input) {
        try{
            double x = Double.parseDouble(input);

            if(x < 0){
                return null;
            }
            return x;
        }catch (NumberFormatException e) {
            return null;
        }

    }



}
