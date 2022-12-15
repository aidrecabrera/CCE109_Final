package open.api.aidre.svene;

import com.google.gson.*;
import javafx.animation.ScaleTransition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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
        System.out.println("Trisha: Hello, " + NameOfUserSession + "! I am Trisha the Chat Bot.");
        CHAT_LOG.put("Trisha", "Hello, " + NameOfUserSession + "! I am Trisha the Chat Bot.");
        boolean x = true;
        try {
            while (x) {
                System.out.print("Your Turn: ");
                String userPrompt = scanner.nextLine();
                if (userPrompt.equals("End") || userPrompt == "Exit Svene") {
                    x = false;
                    break;
                }
                CHAT_LOG.put(NameOfUserSession, userPrompt);
                StringBuilder retrievedJSON = sveneBackend.apiConfiguration(userPrompt);
                String promptResponse = sveneBackend.parsingResponse(retrievedJSON);
                CHAT_LOG.put("Trisha", promptResponse);
                System.out.println("Trisha: " + promptResponse);
            }
        } catch (Exception e) {
            System.out.println("Trisha: An Error has Occured :( Please try again later.");
            System.out.println("Error: " + e);
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
        sveneBackend.chatLogExport(CHAT_LOG);
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
        int maximumLength = 150;
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

     void chatLogExport(Map<String, String> toFile) {
         // Create the GUI
         JFrame frame = new JFrame("Write HashMap to File");
         JButton button = new JButton("Save File");
         JPanel panel = new JPanel(new BorderLayout());
         panel.add(button, BorderLayout.CENTER);
         frame.add(panel);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.pack();
         frame.setVisible(true);

         // Prompt the user for the location where the file should be saved
         button.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 JFileChooser chooser = new JFileChooser();
                 int result = chooser.showSaveDialog(frame);
                 if (result == JFileChooser.APPROVE_OPTION) {
                     File file = chooser.getSelectedFile();
                     String filePath = file.getAbsolutePath();

                     // Write the contents of the HashMap to the file at the specified location
                     try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                         for (Map.Entry<String, String> entry : toFile.entrySet()) {
                             bw.write(entry.getKey() + "=" + entry.getValue());
                             bw.newLine();
                         }
                         JOptionPane.showMessageDialog(frame, "File saved successfully");
                     } catch (IOException ex) {
                         JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                     }
                 }
             }
         });
    }
}