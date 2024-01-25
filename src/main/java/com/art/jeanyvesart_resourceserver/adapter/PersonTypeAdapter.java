//package com.art.jeanyvesart_resourceserver.adapter;
//
//
//import com.google.gson.TypeAdapter;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonToken;
//import com.google.gson.stream.JsonWriter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class PersonTypeAdapter extends TypeAdapter<Person> {
//    //@Override
////    public void write(JsonWriter out, Person person) throws IOException {
////        out.beginObject();
////        out.name("name").value(person.getName());
////
////        // Serialize other Author fields
////        // Avoid serializing the 'books' field here
////
////        out.endObject();
////    }
//
//
//    @Override
//    public Person read(JsonReader reader) throws IOException {
//        if (reader.peek() == JsonToken.NULL) {
//            reader.nextNull();
//            return null;
//        }
//        String person = reader.nextString();
//        String[] parts = person.split(",");
//        String firstname = parts[0];
//        String lastname = parts[1];
//        return new Person(firstname, lastname,new Artwork(), new ArrayList<>() );
//    }
//    public void write(JsonWriter writer, Person value) throws IOException {
//        if (value == null) {
//            writer.nullValue();
//            return;
//        }
//        String person = value.getName() + "," + value.getTelephone();
//        writer.value(person);
//    }
////    public Person read(JsonReader in) throws IOException {
////        // Implement deserialization if needed
////        return null;
////    }
//}
