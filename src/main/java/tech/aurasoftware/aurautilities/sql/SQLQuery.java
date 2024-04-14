package tech.aurasoftware.aurautilities.sql;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SQLQuery {

    private final String query;
    private final Object[] values;

}
