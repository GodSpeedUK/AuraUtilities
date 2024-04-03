package tech.aurasoftware.aurautilities.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Parameters {
    Class<?>[] value();
    int[] index();
    boolean[] optional();

}
