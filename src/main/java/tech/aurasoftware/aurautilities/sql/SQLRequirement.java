package tech.aurasoftware.aurautilities.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SQLRequirement {

    private final String name;
    private final Object value;

}
