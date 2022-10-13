package com.key.win.common.jsonEnum;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.key.win.common.util.StringUtil;

import java.io.IOException;

/**
 * @package: com.key.win.jsonEnum
 * @program:
 * @Date: 2019/6/24 14:16
 * @Author: Floating
 * @Description:
 */
public class TextureEnumDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {

    private JsonEnum target;

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode jn = node.get("stringValue");
        String enumName = "";
        if (jn != null){
            enumName= jn.asText();
        }
        if (StringUtil.isBlank(enumName)) {
            enumName = node.asText();
        }
        return target.selectEnumByName(enumName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonDeserializer<Object> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        Class<Object> clazz = (Class<Object>) property.getType().getRawClass(); //getContextualType().getRawClass();
        target = (JsonEnum) clazz.getEnumConstants()[0];
        return this;
    }

}
