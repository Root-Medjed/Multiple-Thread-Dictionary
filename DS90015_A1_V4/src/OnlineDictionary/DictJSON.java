package OnlineDictionary;

import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class DictJSON {

    @SuppressWarnings("unchecked")
    public static void buildJsonDictionary() {

        // three words in dictionary
        JSONObject obj = new JSONObject();
        obj.put("json", "Meaning: (noun) is an open standard file format, and data interchange format, that uses " +
                "human-readable text to store and transmit data objects.");
        obj.put("xml", "Meaning: (noun) a metalanguage which allows users to define their own customized markup languages, " +
                "especially in order to display documents on the internet.");
        obj.put("tcp", "Meaning: (noun) Transmission Control Protocol.");

        try (FileWriter file = new FileWriter("dictionary.json")) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

