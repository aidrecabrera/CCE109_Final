package open.api.aidre.svene;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Svene {
    static Svene_Backend sveneBackend = new Svene_Backend();
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, String> CHAT_LOG = new HashMap<>();
        System.out.print("What is your name? ");
        String NameOfUserSession = scanner.nextLine();
        CHAT_LOG.put(NameOfUserSession, NameOfUserSession);
        System.out.println("Svene: Hello, " + NameOfUserSession + "! I am Svene the Chat Bot.");
        CHAT_LOG.put("Svene", "Hello, " + NameOfUserSession + "! I am Svene the Chat Bot.");
        int x = 0;
        while (x<2) {
            System.out.print("Your Turn: ");
            String userPrompt = scanner.nextLine();
            CHAT_LOG.put(NameOfUserSession, userPrompt);
            StringBuilder retrievedJSON = sveneBackend.apiConfiguration(userPrompt);
            String promptResponse = sveneBackend.parsingResponse(retrievedJSON);
            CHAT_LOG.put("Svene", promptResponse);
            System.out.println("Svene: " + promptResponse);
            x++;
        }
        System.out.println("Open Chat Log?");
        System.out.println("[1] - Yes or [0] - No: ");
        int chatLog = scanner.nextInt();
        if (chatLog == 1) {
            for(Map.Entry<String, String> entry: CHAT_LOG.entrySet()) {
                System.out.print(entry.getKey() + ": ");
                System.out.println(entry.getValue());
            }
        }
    }
}

class Svene_Backend {
    StringBuilder apiConfiguration(String prompt) throws IOException {
        String apiKey = "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";
        String endpoint = "https://api.openai.com/v1/completions";
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        String model = "text-davinci-003";
        int maximumLength = 70;
        String requestBody = "{\"prompt\": \"" + prompt + "\", \"model\": \"" + model + "\", \"max_tokens\": " + maximumLength + "}";
        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(requestBody);
        writer.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response;
    }
    String parsingResponse(StringBuilder response) {
        JsonParser parser = new JsonParser();
        JsonElement responseJson = parser.parse(response.toString());
        JsonObject responseObject = responseJson.getAsJsonObject();
        JsonArray choicesArray = responseObject.getAsJsonArray("choices");
        JsonObject firstChoice = choicesArray.get(0).getAsJsonObject();
        String text = firstChoice.get("text").getAsString();
        String newText = text.replaceAll("\n\n", "");
        return newText;
    }
}