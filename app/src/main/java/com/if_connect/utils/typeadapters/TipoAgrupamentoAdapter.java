package com.if_connect.utils.typeadapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.if_connect.models.enums.TipoAgrupamento;

import java.lang.reflect.Type;

public class TipoAgrupamentoAdapter implements JsonSerializer<TipoAgrupamento>, JsonDeserializer<TipoAgrupamento> {

    @Override
    public JsonElement serialize(TipoAgrupamento tipoAgrupamento, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(tipoAgrupamento.name());
    }

    @Override
    public TipoAgrupamento deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return TipoAgrupamento.valueOf(jsonElement.getAsString());
    }
}
