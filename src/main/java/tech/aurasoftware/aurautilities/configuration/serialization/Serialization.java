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


    private static final List<String> PRIMATIVE_TYPES = Arrays.asList("int", "double", "float", "long", "boolean", "java.lang.String", "java.lang.Integer", "java.lang.Boolean", "java.lang.Double", "java.lang.Float", "java.lang.Long");

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

        for(Field field: obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Ignored.class)) {
                continue;
            }

            if(field.get(obj) == null){
                continue;
            }

            String typeString = field.getType().getName();
            if(PRIMATIVE_TYPES.contains(typeString)){
                map.put(field.getName(), field.get(obj));
                continue;
            }

            Class<?> type = Class.forName(typeString);
            if(CONFIG_MAP.contains(type)){
                Map<String, Object> serialized = Serialization.serialize(field.get(obj));
                for(String key: serialized.keySet()){
                    map.put(field.getName() + "." + key, serialized.get(key));
                }
                continue;
            }

            if(field.getType().isAssignableFrom(List.class)){
                List<?> list = (List<?>) field.get(obj);
                if(list == null || list.isEmpty()){
                    continue;
                }

                if(PRIMATIVE_TYPES.contains(list.get(0).getClass().getName())){
                    map.put(field.getName(), list);
                    continue;
                }

                if(CONFIG_MAP.contains(list.get(0).getClass())){
                    for(int i = 0; i < list.size(); i++){
                        Map<String, Object> serialized = Serialization.serialize(list.get(i));
                        for(String key: serialized.keySet()){
                            map.put(field.getName() + "." + i + "." + key, serialized.get(key));
                        }
                    }
                }
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

            String type = field.getType().getName();
            if(PRIMATIVE_TYPES.contains(type)){
                if(config.contains(key + "." + field.getName())){
                    field.set(obj, config.get(key + "." + field.getName()));
                }
                continue;
            }
            Class<?> fieldClass = Class.forName(field.getType().getName());
            // Check if field is serializable
            if(CONFIG_MAP.contains(fieldClass)){
                field.set(obj, Serialization.deserialize(fieldClass, config, key + "." + field.getName()));
                continue;
            }

            if(field.getType().isAssignableFrom(List.class)){
                String listType = field.getGenericType().getTypeName().split("<")[1].split(">")[0];

                if(PRIMATIVE_TYPES.contains(listType)){
                    if(config.contains(key + "." + field.getName())){
                        field.set(obj,  config.get(key + "." + field.getName()));
                    }
                    continue;
                }

                if(CONFIG_MAP.contains(Class.forName(listType))){
                    List<Object> list = new ArrayList<>();
                    for(String listKey: config.getConfigurationSection(key + "." + field.getName()).getKeys(false)){
                        list.add(Serialization.deserialize(Class.forName(listType), config, key + "." + field.getName() + "." + listKey));
                    }
                    field.set(obj, list);
                    continue;
                }

                if(config.contains(key + "." + field.getName())){
                    field.set(obj, config.get(key + "." + field.getName()));
                }

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
                    return false;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

}
