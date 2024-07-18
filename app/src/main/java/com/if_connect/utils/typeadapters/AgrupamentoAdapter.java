package com.if_connect.utils.typeadapters;

import com.google.gson.*;
import com.if_connect.models.Agrupamento;
import com.if_connect.models.Grupo;
import com.if_connect.models.Turma;
import com.if_connect.models.enums.TipoAgrupamento;

import java.lang.reflect.Type;

public class AgrupamentoAdapter implements JsonDeserializer<Agrupamento> {

    @Override
    public Agrupamento deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement tipoElement = jsonObject.get("tipoAgrupamento");

        if (tipoElement != null) {
            TipoAgrupamento tipoAgrupamento = TipoAgrupamento.valueOf(tipoElement.getAsString());
            switch (tipoAgrupamento) {
                case GRUPO:
                    return context.deserialize(jsonObject, Grupo.class);
                case TURMA:
                    return context.deserialize(jsonObject, Turma.class);
                default:
                    throw new JsonParseException("Tipo desconhecido de Agrupamento: " + tipoAgrupamento);
            }
        }

        throw new JsonParseException("Campo tipoAgrupamento não encontrado: " + json);
    }
}