package OnlineDictionary;

import org.json.simple.JSONObject;

import java.io.*;

public class DictJSON {

    @SuppressWarnings("unchecked")
    public static void buildJsonDictionary() throws IOException {

        BufferedWriter writer = null;
        String path = "dictionary.json";
        File file = new File(path);

        if(!file.exists()){
            try {
                file.createNewFile();
                System.out.println("File created！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("File already exists");
        }

        //String jsonData= JSON.toJSONString(**some**data**);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("json", "Meaning: (noun) is an open standard file format, and data interchange format, that uses " +
                "human-readable text to store and transmit data objects.");
        jsonObject.put("xml", "Meaning: (noun) a metalanguage which allows users to " +
                "define their own customized markup languages, " +
                "especially in order to display documents on the internet.");
        jsonObject.put("python", "1. a large heavy-bodied non-venomous; 2. a high-level " +
                "general-purpose programming language.");
        jsonObject.put("java", "coffee");


        FileWriter fileWriter = new FileWriter(path, false);
        try {
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
        } catch (FileNotFoundException e) {
            System.out.println("Oops! file could not found!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load file");
        }finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /**
        //write JSONString to json dictionary file
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), "UTF-8"));
            writer.write(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }**/

    }

}

