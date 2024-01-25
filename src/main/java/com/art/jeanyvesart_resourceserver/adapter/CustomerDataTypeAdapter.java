package com.art.jeanyvesart_resourceserver.adapter;

import com.art.jeanyvesart_resourceserver.model.CustomerData;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class CustomerDataTypeAdapter<M extends CustomerData<H>, H> extends TypeAdapter<M> {
    @Override
    public void write(JsonWriter out, M author) throws IOException {
        out.beginObject();
        out.name("id").value(author.getId());

        // Serialize other Author fields
        // Avoid serializing the 'books' field here
        out.endObject();
    }


    @Override
    public M read(JsonReader in) throws IOException {
        // Implement deserialization if needed
        return null;
    }
}
