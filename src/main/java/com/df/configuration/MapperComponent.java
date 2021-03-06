package com.df.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class MapperComponent extends ObjectMapper {

    public MapperComponent() {
        super.findAndRegisterModules();
        super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        super.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
    }

    @Override
    public String writeValueAsString(Object object) {
        try {
            return super.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Could not convert " + object + " to text JSON string.", e);
            return Strings.EMPTY;
        }
    }

}
