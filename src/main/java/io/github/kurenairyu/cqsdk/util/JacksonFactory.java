package io.github.kurenairyu.cqsdk.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.github.kurenairyu.cqsdk.config.NumericBooleanDeserializer;
import io.github.kurenairyu.cqsdk.config.NumericBooleanSerializer;

/**
 * @author Kurenai
 * @since 2021-04-08 14:46
 */
public class JacksonFactory {

    private static ObjectMapper instance;

    public static ObjectMapper getInstance() {
        if (instance != null) return instance;

        SimpleModule myModule = new SimpleModule();
        myModule.addDeserializer(Boolean.class, new NumericBooleanDeserializer());
        myModule.addDeserializer(Boolean.TYPE, new NumericBooleanDeserializer());
        myModule.addSerializer(Boolean.class, new NumericBooleanSerializer());
        myModule.addSerializer(Boolean.TYPE, new NumericBooleanSerializer());
        instance = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .addModules(new Jdk8Module(), new JavaTimeModule(), new ParameterNamesModule(), myModule)
                .build();
        return instance;
    }

}
