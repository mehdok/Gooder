/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by mehdok on 5/27/2016.
 */
public class BaseResultAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {

            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {

                JsonElement jsonElement = elementAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("msg_data") && jsonObject.get("msg_data").isJsonArray()) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("msg_data");
                        //if (jsonArray)
                        jsonElement = jsonObject.get("data");
                    }
                }

                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }

    /*@Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType()!= APIPosts.class) return null;

        TypeAdapter<BaseResponse> defaultAdapter = (TypeAdapter<BaseResponse>) gson.getDelegateAdapter(this, type);
        return (TypeAdapter<T>) new ResultObjectAdapter(defaultAdapter);
    }

    public class ResultObjectAdapter extends TypeAdapter<BaseResponse> {

        protected TypeAdapter<BaseResponse> defaultAdapter;


        public ResultObjectAdapter(TypeAdapter<BaseResponse> defaultAdapter) {
            this.defaultAdapter = defaultAdapter;
        }

        @Override
        public void write(JsonWriter out, BaseResponse value) throws IOException {
            defaultAdapter.write(out, value);
        }

        @Override
        public BaseResponse read(JsonReader in) throws IOException {
            *//*
            This is the critical part. So if the value is a string,
            Skip it (no exception) and return null.
            *//*
            if (in.peek() == JsonToken.BOOLEAN) {
                in.skipValue();
                return null;
            }
            return defaultAdapter.read(in);
        }
    }*/
}
