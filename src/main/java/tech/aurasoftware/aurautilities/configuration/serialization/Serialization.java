package tech.aurasoftware.aurautilities.configuration.serialization;

import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.aurasoftware.aurautilities.configuration.serialization.annotation.Ignored;
import tech.aurasoftware.aurautilities.file.YamlFile;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;


public class Serialization {


    private static final List<String> PRIMATIVE_TYPES = Arrays.asList("int", "double", "float", "long", "boolean", "String");

    public static final List<Class<? extends Serializable>> CONFIG_MAP = new ArrayList<>();

    public static void register(Class<? extends Serializable> clazz) {
        if (CONFIG_MAP.contains(clazz)) {
            return;
        }
        CONFIG_MAP.add(clazz);
    }

    public static Class<? extends Serializable> getSerializableClass(Serializable configItem) {
        for (Class<? extends Serializable> configItems : CONFIG_MAP) {
            if (configItems.equals(configItem.getClass())) {
                return configItems;
            }
        }
        return null;
    }

    public static Object deserialize(Class<? extends Serializable> clazz, YamlFile yamlFile, String path) {
        Field field;
        Method method;
        try {
            method = clazz.getMethod("deserialize", YamlFile.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        if (!Modifier.isStatic(method.getModifiers())) {
            return null;
        }

        try {
            return method.invoke(clazz, yamlFile, path);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }

    @SneakyThrows
    public static Map<String, Object> serialize(Object obj) {
        Map<String, Object> map = new HashMap<>();

        if(!(obj instanceof Serializable)){
            return null;
        }



        for(Field field: obj.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if(field.isAnnotationPresent(Ignored.class)){
                continue;
            }

            // Check if field is serializable
            if(field.getType().isAssignableFrom(Serializable.class)){

                Map<String, Object> serialized = Serialization.serialize(field.get(obj));

                for(String key: serialized.keySet()){
                    map.put(field.getName() + "." + key, serialized.get(key));
                }
                continue;
            }

            // Check if field is a list
            if(field.getType().isAssignableFrom(List.class)){
                // Check if List type is serializable
                List<Object> list = (List<Object>) field.get(obj);


                if(list.isEmpty()){
                    continue;
                }

                if(list.get(0) instanceof Serializable){
                    for(int i = 0; i < list.size(); i++){
                        Map<String, Object> serialized = Serialization.serialize(list.get(i));

                        for(String key: serialized.keySet()){
                            map.put(field.getName() + "." + i + "." + key, serialized.get(key));
                        }
                    }
                    continue;
                }

                String type = list.get(0).getClass().getName();

                if(PRIMATIVE_TYPES.contains(type)){
                    map.put(field.getName(), list);
                }

            }

            if(isInitialized(field, obj)){
                map.put(field.getName(), field.get(obj));
            }

        }

        return map;
    }

    @SneakyThrows
    public static <T> T deserialize(Class<T> clazz, YamlConfiguration config, String key){
        T obj = clazz.newInstance();
        for(Field field: clazz.getDeclaredFields()){
            field.setAccessible(true);
            if(field.isAnnotationPresent(Ignored.class)){
                continue;
            }

            if(field.getType().isAssignableFrom(Serializable.class)){
                field.set(obj, Serialization.deserialize((Class<? extends Serializable>) field.getType(), config, key + "." + field.getName()));
                continue;
            }

            if(field.getType().isAssignableFrom(List.class)){
                String type = field.getGenericType().getTypeName().split("<")[1].split(">")[0];

                if(PRIMATIVE_TYPES.contains(type)){

                    List<?> list = config.getList(key + "." + field.getName());
                    field.set(obj, list);
                    continue;
                }

                if(CONFIG_MAP.contains(Class.forName(type))){
                    List<Object> list = new ArrayList<>();
                    for(String listKey: config.getConfigurationSection(key + "." + field.getName()).getKeys(false)){
                        list.add(Serialization.deserialize(Class.forName(type), config, key + "." + field.getName() + "." + listKey));
                    }
                    field.set(obj, list);
                    continue;
                }
            }
            if(config.contains(key + "." + field.getName())){
                field.set(obj, config.get(key + "." + field.getName()));
            }
        }
        return obj;
    }

    private static boolean isInitialized(Field field, Object obj) {
        try {
            field.setAccessible(true);
            switch (field.getType().getName()) {
                case "int":
                    return field.getInt(obj) != 0;
                case "double":
                    return field.getDouble(obj) != 0;
                case "float":
                    return field.getFloat(obj) != 0;
                case "long":
                    return field.getLong(obj) != 0;
                case "boolean":
                    return field.getBoolean(obj);
                default:
                    return field.get(obj) != null;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

}
