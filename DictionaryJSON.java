package com.DictionaryClientServer;

import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;


public class DictionaryJSON {

    @SuppressWarnings(value = "unchecked")

    public static void buildJsonDictionary() {

        //saved initial words in dictionary
        JSONObject obj = new JSONObject();
        obj.put("JSON", "Meaning: (noun) is an open standard file format, and data interchange format, that uses " +
                "human-readable text to store and transmit data objects.");
        obj.put("XML", "Meaning: (noun) a metalanguage which allows users to define their own customized markup languages, " +
                "especially in order to display documents on the internet.");
        obj.put("TCP", "Meaning: (noun) Transmission Control Protocol.");

        try (FileWriter file = new FileWriter("dictionary.json")) {

            file.write(obj.toJSONString());
            file.flush();
            //file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
