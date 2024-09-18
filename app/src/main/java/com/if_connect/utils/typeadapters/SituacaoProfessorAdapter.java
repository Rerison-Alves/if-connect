package com.if_connect.utils.typeadapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.if_connect.models.enums.SituacaoProfessor;

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
