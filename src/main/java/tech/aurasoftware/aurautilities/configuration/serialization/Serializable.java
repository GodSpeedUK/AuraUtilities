package tech.aurasoftware.aurautilities.configuration.serialization;

import java.util.Map;

public interface Serializable {

    default Map<String, Object> serialize(){
        return Serialization.serialize(this);
    }

}
