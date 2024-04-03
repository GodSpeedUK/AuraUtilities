package tech.aurasoftware.aurautilities.command.parameter;

import lombok.Getter;

@Getter
public abstract class Parameter<T> {

    private Class<T> clazz;
    public Parameter(Class<T> clazz){
        this.clazz = clazz;
    }

    public abstract T parse(String input);

    public boolean isParsable(String string){
        if(string == null){
            return false;
        }
        return parse(string) != null;
    }

//    public boolean handleNotParsable(LibCommandSender libCommandSender, LibCommandFrame libCommandFrame, String arg){
//        if(arg == null){
//            CoreMessages.INVALID_USAGE.send(libCommandSender, new Placeholder("{usage}", libCommandFrame.getUsage()));
//            return false;
//        }
//        return true;
//    }

}
