package com.art.jeanyvesart_resourceserver.adapter;

import com.art.jeanyvesart_resourceserver.model.MyOrder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class MyOrderAdapter extends TypeAdapter<MyOrder> {
    @Override
    public void write(JsonWriter out, MyOrder myOrder) throws IOException {
        out.beginObject();
        out.name("id").value(myOrder.getId());

        // Serialize other Author fields
        // Avoid serializing the 'books' field here
        out.endObject();
    }


    @Override
    public MyOrder read(JsonReader in) throws IOException {
        // Implement deserialization if needed
        return null;
    }
}
