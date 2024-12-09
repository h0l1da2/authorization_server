package me.holiday.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RedisSerializable {

    private final ObjectMapper mapper;

    public String serialize(Map<Object, Object> map) throws JsonProcessingException {
        return mapper.writeValueAsString(map);
    }

}
