package tech.aurasoftware.aurautilities.util;

import lombok.experimental.UtilityClass;
import tech.aurasoftware.aurautilities.AuraUtilities;
import tech.aurasoftware.aurautilities.command.parameter.Parameter;

@UtilityClass
public class Util {


    public <T> T getParameter(Class<T> clazz, String input){

        Parameter<T> parameter = (Parameter<T>) AuraUtilities.getInstance().getParameterManager().get(clazz);

        if(parameter == null){
            return null;
        }

        return parameter.parse(input);

    }

}
