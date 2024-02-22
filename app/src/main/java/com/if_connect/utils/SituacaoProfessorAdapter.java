package com.if_connect.utils;

import com.google.gson.*;
import com.if_connect.enums.SituacaoProfessor;

import java.lang.reflect.Type;

public class SituacaoProfessorAdapter implements JsonSerializer<SituacaoProfessor>, JsonDeserializer<SituacaoProfessor> {

    @Override
    public JsonElement serialize(SituacaoProfessor situacao, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(situacao.name());
    }

    @Override
    public SituacaoProfessor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return SituacaoProfessor.valueOf(jsonElement.getAsString());
    }
}
