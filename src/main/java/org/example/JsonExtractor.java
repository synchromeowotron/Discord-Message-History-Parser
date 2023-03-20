package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonExtractor {
    public static void main(String[] args) throws IOException {

        String jsonDir = "C:/disc_export/input";
        String outputDir = "C:/disc_export/output";

        List<String> contents = new ArrayList<>();
        Files.list(Paths.get(jsonDir))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".json"))
                .forEach(path -> {
                    try {
                        String content = extractContent(path.toFile());
                        contents.add(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        writeContentsToFile(contents, outputDir);
    }

    private static String extractContent(File file) throws IOException {
        System.out.println("Reading Contents of: " + file.getName().toString() + " . . . ");
        StringBuilder content = new StringBuilder();
        String jsonStr = new String(Files.readAllBytes(file.toPath()));
        JSONObject jsonObj = new JSONObject(jsonStr);
        JSONArray messages = jsonObj.getJSONArray("messages");
        System.out.println("The file has: " + Integer.toString(messages.length()) + " messages.");
        List<String> messagesToAdd = new ArrayList<>();
        MaxSizeList messageWithContext = new MaxSizeList(7);

        for (int i = 0; i < messages.length(); i++) {

            JSONObject message = messages.getJSONObject(i);
            JSONObject author = message.getJSONObject("author");
            if (message.has("content") && !message.get("content").toString().contains("http")) {

                messageWithContext.add(message);
                if (author.get("discriminator").toString().equals("7777")) {
                    messageWithContext.getCurrentList().forEach(x -> {
                        JSONObject listMessage = (JSONObject) x;
                        String addToContent = listMessage.getJSONObject("author").get("discriminator") + "¤ "
                                              + listMessage.get("content") + "\n";

                        messagesToAdd.add(addToContent);
                    });
                    messageWithContext.clear();
                }
            }
        }

        for (String message : messagesToAdd) {
            content.append(message);
        }

        System.out.println("Saved " + Integer.toString(messagesToAdd.size()) + " messages.");
        System.out.println("File successfully parsed");
        return content.toString();
    }

    private static void writeContentsToFile(List<String> contents, String outputDir) throws IOException {
        System.out.println("Writing contents of all the files.");
        File outputFile = new File(outputDir, "contents.txt");
        FileWriter writer = new FileWriter(outputFile);
        for (String content : contents) {
            writer.write(content);
        }
        System.out.println("Contents successfully written! uwu");
        writer.close();
    }
}
