package tech.aurasoftware.aurautilities.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Placeholder {

    private final String key, value;

    public String apply(String message){
        return message.replace(key, value);
    }

    public static String apply(String message, Placeholder... placeholders){
        for(Placeholder placeholder : placeholders) {
            message = placeholder.apply(message);
        }

        return message;
    }

}
