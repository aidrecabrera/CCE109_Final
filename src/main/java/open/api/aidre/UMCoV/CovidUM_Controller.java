package open.api.aidre.UMCoV;


// Importing the necessary libraries for the program to run.

import animatefx.animation.FadeIn;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author Aidre Love S. Cabrera
 * @Course BS Computer Science Student from the University of Mindanao
 * @Description This is a final project in partial fulfillment for the requirement of the CCE109 - Fundamentals
 *         of Computing
 * @Professor Emmanuel Christian Valderrama Galang
 */


/**
 * @Abstract The CovidUM_Controller class is used to retrieve data from an API and display it in a JavaFX application.
 *
 * @Synopsis The CovidUM_Controller class is a Java class that is used to retrieve data from an API and display it in
 * a JavaFX application. The class contains various methods that are used to make an API request, get a list of available
 * countries from the API, refresh the values displayed in the application, and add the available countries to a dropdown menu.
 */
public class CovidUM_Controller extends CovidUm_Application {
    public static HashMap<String, String> covContainer = new HashMap<>();
    public static String countryName = "Philippines", countryIso3 = "phl";
    static List<String> countries = new ArrayList<>();

    @FXML
    Label value_active;
    @FXML
    Label value_cases;
    @FXML
    Label value_country;
    @FXML
    Label value_deaths;
    @FXML
    Label value_recovered;
    @FXML
    JFXButton button_country;
    @FXML
    JFXComboBox<String> country_choices = new JFXComboBox<>();
    String referenceUserInput = countryName;
    NumberFormat formatter = NumberFormat.getInstance();
    String[] headerParsed = {"Country", "TotalCases", "TotalDeaths", "TotalRecovered", "ActiveCases"};
    Gson gson = new Gson();
    JsonArray jsonArrayContainCovidGeneral;
    JsonObject object = null;
    JsonObject covidFinalParsed;
    StringBuffer response = new StringBuffer();
    // Set up the progress indicator
    String anim = "This program is purely written by Aidre (fueled by Mental Torment)";
    int i = 0;

    public CovidUM_Controller() throws MalformedURLException {
    }

    public String getApiKey() {
        // Reading the api_key from the api_keys.properties file (Locally Stored for Security Purposes).
        Properties prop = new Properties();
        // Reading the api_keys.properties file and returning the api_key property.
        try (InputStream input = new FileInputStream("src/main/resources/open/api/aidre/UMCoV/api_keys.properties")) {
            // Reading the api_key from the properties file.
            prop.load(input);
            return prop.getProperty("api_key");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Stage stage;
    private Scene scene;
    private Object root;
    @FXML
    public void life() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CovidUm_Application.class.getResource("covid.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    // This is the code that is responsible for the API request.
    String apiKey;
    String apiHost = "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com";
    URL url = new URL("https://" + apiHost + "/api/npm-covid-data/countries-name-ordered");
    boolean time = true;
    // Create two threads
    Thread thread1 = new Thread(() -> {
        try {
            requestingAnimation();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            try {
                throw new IOException(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println();
        }
    });
    Thread thread2 = new Thread(() -> {
        try {
            getAvailableCountries();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println();
        }
    });
    /**
     * It gets the available countries from the API, requests the general API, refreshes the values, and adds the
     * countries to the dropdown menu
     */
    public void initialize() throws InterruptedException, IOException {
        life();
        String retrievedAPI = getApiKey();
        System.out.println("API Key Retrieved!");
        System.out.println("API Key: " + retrievedAPI);
        System.out.println();
        apiKey = retrievedAPI;
        // Start the threads
        thread2.start();
        requestGeneralApi();
        valueRefresher();
        country_choices.getItems().addAll(countries);
        thread1.start();
    }
    void requestingAnimation() throws IOException, InterruptedException {
        boolean colored = true;
        int x = 0;
        while (time) {
            if (colored) {
                System.out.print("\u001b[31m" + anim + "\u001b[0m");
                colored = false;
            } else if (!colored){
                System.out.print("\u001b[37m" + anim + "\u001b[0m");
                colored = true;
            }

            try {
                Thread.sleep(100);
                System.out.print("\r");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            x++;
            if (x == 25) {
                System.out.print("\r");
                System.out.print("\u001b[31m" + anim + "\u001b[0m");
                break;
            }
        }

    }

    /**
     * It gets the list of countries from the API and adds them to an ArrayList
     */
    void getAvailableCountries() throws IOException, InterruptedException {
        // Creating a connection to the URL, setting the request method to GET, setting the request
        // properties, and then reading the response from the connection.
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-RapidAPI-Key", apiKey);
        conn.setRequestProperty("X-RapidAPI-Host", apiHost);

        // Checking if the response code is 200, which means that the request was successful. If it is
        // not 200, then it will print out the response code.
        int responseCode = conn.getResponseCode();
        StringBuilder response = null;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } else {
            System.out.println("Request failed with response code: " + responseCode);
        }
        // Checking if the response is null. If it is null, then it will show a message dialog.
        String json = null;
        try {
            assert response != null;
            json = response.toString();
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "There was an error with the API or there is no data available. It's okie, it is not your fault anyways :)", "Error", JOptionPane.ERROR_MESSAGE);
        }
        // Getting the list of countries from the API and adding them to an ArrayList.
        List<Map<String, String>> list = gson.fromJson(json, new TypeToken<List<Map<String, String>>>() {
        }.getType());
        // Loop through the list of maps and add the country values to the ArrayList
        try {
            assert list != null;
            for (Map<String, String> map : list) {
                countries.add(map.get("Country"));
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "There was an error with the API or there is no data available. It's okie, it is not your fault anyways :)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * It takes the user input from the dropdown menu and stores it in a variable
     */
    @FXML
    void country_input() {
        // Getting the value from the dropdown menu and storing it in a variable.
        try {
            countryName = country_choices.getValue();
            if (countryName != referenceUserInput) {
                valueRefresher();
            } else {
                System.out.println("No new request made :)");
            }
            referenceUserInput = countryName;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There was an error with the API or there is no data available. It's okie, it is not your fault anyways :)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * It creates a connection to the URL, sets the request method to GET, sets the request properties, and then reads
     * the response from the connection
     */
    void requestGeneralApi() {
        // Creating a connection to the URL, setting the request method to GET, setting the request
        // properties, and then reading the response from the connection.
        try {
            URL url = new URL("https://vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com/api/npm-covid-data/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-RapidAPI-Host", "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com");
            connection.setRequestProperty("X-RapidAPI-Key", "4654f7147bmsha14fefd6aba622ep12dcb9jsne9b3877e70a2");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * It takes the data from the API and puts it into a HashMap, then it takes the data from the HashMap and puts it
     * into the TextViews
     */
    void valueRefresher() throws InterruptedException {
        // Parsing the JSON data from the API and putting it into a JsonArray.
        String parsedJSON = response.toString();
        jsonArrayContainCovidGeneral = gson.fromJson(parsedJSON, JsonArray.class); // General
        for (int i = 0; i < jsonArrayContainCovidGeneral.size(); i++) {
            object = jsonArrayContainCovidGeneral.get(i).getAsJsonObject();
            if (object.has("Country") && object.get("Country").getAsString().equals(countryName)) {
                System.out.print("\nUnparsed JSON Object: " + object + "\n");
                System.out.println("\nParsed and Retrieved JSON Object");
                int x = 0;
                for (String key : object.keySet()) {
                    System.out.println(x + " " + key + ": " + object.get(key));
                    x++;
                }
                covidFinalParsed = object;
            }
        }
        // Iterate over the JsonObject and put each element and value into the HashMap
        for (Map.Entry<String, JsonElement> entry : covidFinalParsed.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            covContainer.put(key, value);
        }
        // Getting the data from the HashMap and putting it into the TextViews.
        System.out.println("\nNumber of Retrieved Data Values: " + covContainer.size());
        String country = covContainer.get(headerParsed[0]);
        int cases = Integer.parseInt(covContainer.get(headerParsed[1]));
        int deaths = Integer.parseInt(covContainer.get(headerParsed[2]));
        int recovered = Integer.parseInt(covContainer.get(headerParsed[3]));
        int active = Integer.parseInt(covContainer.get(headerParsed[4]));

        // Setting the text of the TextViews to the values of the variables.
        value_country.setText(country);
        value_cases.setText(formatter.format(cases));
        value_deaths.setText(formatter.format(deaths));
        value_recovered.setText(formatter.format(recovered));
        value_active.setText(formatter.format(active));

        // Used to print the values of the TextViews.
        String console_value_country = value_country.getText();
        String console_value_cases = value_cases.getText();
        String console_value_deaths = value_deaths.getText();
        String console_value_recovered = value_recovered.getText();
        String console_value_active = value_active.getText();
        System.out.println(headerParsed[0] + " " + console_value_country);
        System.out.println(headerParsed[1] + " " + console_value_cases);
        System.out.println(headerParsed[2] + " " + console_value_deaths);
        System.out.println(headerParsed[3] + " " + console_value_recovered);
        System.out.println(headerParsed[4] + " " + console_value_active);
        System.out.println("");
        System.out.print("\u001b[31m" + anim + "\u001b[0m");
    }

    // A method that is used to request the API. It is deprecated because it is inefficient.
    void requestApi() throws IOException {
        URL url = new URL("https://vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com/api/npm-covid-data/country-report-iso-based/" + countryName + "/" + countryIso3);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-RapidAPI-Host", "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com");
        connection.setRequestProperty("X-RapidAPI-Key", "4654f7147bmsha14fefd6aba622ep12dcb9jsne9b3877e70a2");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String parsedJSON = response.toString();
        Gson gson = new Gson();
        JsonArray array = gson.fromJson(parsedJSON, JsonArray.class);
        JsonObject object = array.get(0).getAsJsonObject();
        for (String index : headerParsed) {
            covContainer.put(index, object.get(index).getAsString());
        }
        System.out.println(response);
    }

    // Other features :)

    // A boolean variable to control the while true loop
    private boolean stop = false;

    // The method where the while true loop is running
    public void startLoop() throws InterruptedException {
        // The while true loop
        while (true) {
            if (stop) {
                // If the stop variable is true, break out of the loop
                break;
            }
            // Do something in the loop
            System.out.print(anim.charAt(i++));
            Thread.sleep(10);
            if (i == anim.length()) {
                Thread.sleep(100);
                System.out.print("\r");
                i = 0;
            }
        }
    }

    // The method where the while true loop is stopped
    public void stopLoop() {
        // Set the stop variable to true
        stop = true;
    }

    void resetColor() {
        System.out.println("\u001B[0m");
    }

    void redColor() {
        System.out.println("\u001B[31m\u001B[0m");
    }
}
