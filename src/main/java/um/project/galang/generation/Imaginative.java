package um.project.galang.generation;
// Import the necessary libraries

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class Imaginative {
    static HttpResponse<String> response;
    static libraryFunction lib = new libraryFunction();
    static String extractedURL;
    public static void main(String[] args) {
        // Set the API endpoint and your API key
        String endpoint = "https://api.openai.com/v1/images/generations";
        String apiKey = "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";

        // Set the prompt for the image you want to generate
        String prompt = "Juan Karlos singing";

        // Set the number of images to generate and the size of the image
        int n = 1;
        String size = "1024x1024";
        // Create the request body
        String body = "{\"model\":\"image-alpha-001\",\"prompt\":\"" + prompt + "\",\"num_images\":" + n + ",\"size\":\"" + size + "\"}";
        try {
            // Create the request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endpoint)).header("Content-Type", "application/json").header("Authorization", "Bearer " + apiKey).POST(HttpRequest.BodyPublishers.ofString(body)).build();
            // Send the request and get the response
            response = client.send(request, BodyHandlers.ofString());
            // Print the response
            System.out.println(response.body());
            extractedURL = lib.parseUrlJSON(response.body());
            lib.saveRetrievedImage(extractedURL);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class libraryFunction {
    String parseUrlJSON(String JSONResponse) {
        // Create a JsonParser instance
        JsonParser parser = new JsonParser();
        // Parse the JSON data
        JsonObject root = parser.parse(JSONResponse).getAsJsonObject();
        String url = root.getAsJsonArray("data").get(0).getAsJsonObject().get("url").getAsString();
        // Print the url value
        return url;
    }

    void saveRetrievedImage(String extractedURL) throws IOException {
        // Load the image from a URL
        BufferedImage image = ImageIO.read(new URL(extractedURL));

        // Create a file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save File");
        fileChooser.setFileFilter(new FileNameExtensionFilter(".png", ".png"));

        // Prompt the user to select a file
        int userSelection = fileChooser.showSaveDialog(null);

        // If the user selects a file, save the image to it
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                ImageIO.write(image, ".png", fileToSave);
                // Extract the file's name and parent directory
                String fileName = fileToSave.getName();
                String parentDir = fileToSave.getParent();
                // Append the string to the filename
                String modifiedFileName = fileName + ".png";
                // Create a new File object using the modified filename and the original parent directory
            } catch (IOException e) {
                System.out.println("Error has occured while saving the image.");
            }
        }
    }
}

