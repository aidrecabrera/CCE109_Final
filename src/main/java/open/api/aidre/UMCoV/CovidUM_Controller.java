package open.api.aidre.UMCoV;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.neovisionaries.i18n.CountryCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;


public class CovidUM_Controller {
    String apiKey = "219b425ab4msh2bb05f0b18c4f30p1057fdjsnc65dc852e2d7";
    String apiHost = "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com";
    URL url = new URL("https://" + apiHost + "/api/npm-covid-data/countries-name-ordered");
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
    static List<String> countries = new ArrayList<>();

    public static HashMap<String, String> covContainer = new HashMap<>();
    public static String countryName = "Philippines", countryIso3 = "phl";
    NumberFormat formatter = NumberFormat.getInstance();
    String[] headerParsed = {"Country", "TotalCases", "TotalDeaths", "TotalRecovered", "ActiveCases"};
    Gson gson = new Gson();

    public CovidUM_Controller() throws MalformedURLException {
    }

    public void initialize() throws IOException {
        requestApi();
        valueRefresher();
        getAvailableCountries();
        country_choices.getItems().addAll(countries);
    }
    void getAvailableCountries() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-RapidAPI-Key", apiKey);
        conn.setRequestProperty("X-RapidAPI-Host", apiHost);

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
            System.out.println(response);
        } else {
            System.out.println("Request failed with response code: " + responseCode);
        }

        String json = response.toString();
        List<Map<String, String>> list = gson.fromJson(json, new TypeToken<List<Map<String, String>>>() {}.getType());
        // Loop through the list of maps and add the country values to the ArrayList
        for (Map<String, String> map : list) {
            countries.add(map.get("Country"));
        }
    }
    @FXML
    void country_input(ActionEvent event) throws IOException {
        try {
            countryName = country_choices.getValue();
            String iso2 =  CountryCode.findByName(countryName).get(0).name();
            CountryCode code = CountryCode.getByCode(iso2);
            countryIso3 = code.getAlpha3();
            System.out.println(countryIso3);
            requestApi();
            valueRefresher();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There was an error with the API or there is no data available. It's okie, it is not your fault anyways :)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
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
        for (int index = 0; index < headerParsed.length; index++) {
            covContainer.put(headerParsed[index], object.get(headerParsed[index]).getAsString());
        }
        System.out.println(response);
    }
    void requestGeneralApi() {
        try {
            URL url = new URL("https://vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com/api/npm-covid-data/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-RapidAPI-Key", "219b425ab4msh2bb05f0b18c4f30p1057fdjsnc65dc852e2d7");
            connection.setRequestProperty("X-RapidAPI-Host", "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com");

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void valueRefresher() {
        String country = covContainer.get(headerParsed[0]);
        int cases = Integer.parseInt(covContainer.get(headerParsed[1]));
        int deaths = Integer.parseInt(covContainer.get(headerParsed[2]));
        int recovered = Integer.parseInt(covContainer.get(headerParsed[3]));
        int active = Integer.parseInt(covContainer.get(headerParsed[4]));

        value_country.setText(country);
        value_cases.setText(formatter.format(cases));
        value_deaths.setText(formatter.format(deaths));
        value_recovered.setText(formatter.format(recovered));
        value_active.setText(formatter.format(active));

        System.out.println(value_country);
        System.out.println(value_cases);
        System.out.println(value_deaths);
        System.out.println(value_recovered);
        System.out.println(value_active);
    }
}
