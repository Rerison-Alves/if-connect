package com.if_connect.utils;

import com.google.gson.*;
import com.if_connect.enums.Turno;

import java.lang.reflect.Type;

public class TurnoAdapter implements JsonSerializer<Turno>, JsonDeserializer<Turno> {

    @Override
    public JsonElement serialize(Turno turno, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(turno.name());
    }

    @Override
    public Turno deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Turno.valueOf(jsonElement.getAsString());
    }
}
