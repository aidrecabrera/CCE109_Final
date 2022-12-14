package open.api.aidre.UMCoV;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Test {
    static String line;

    public static void main(String[] args) throws IOException, InterruptedException {
//        Map<String, String> map = new HashMap<>();
//        URL url = new URL("https://vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com/api/npm-covid-data/");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//        connection.setRequestProperty("X-RapidAPI-Host", "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com");
//        connection.setRequestProperty("X-RapidAPI-Key", "4654f7147bmsha14fefd6aba622ep12dcb9jsne9b3877e70a2");
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//        String parsedJSON = response.toString();
//        Gson gson = new Gson();
//        JsonArray array = gson.fromJson(parsedJSON, JsonArray.class); // General
//        JsonObject object = null;
//        for (int i = 0; i < array.size(); i++) {
//            object = array.get(i).getAsJsonObject();
//            if (object.has("Country") && object.get("Country").getAsString().equals("Philippines")) {
//                System.out.println(object);
//            }
//        }
//        // Iterate over the JsonObject and put each element and value into the HashMap
//        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue().getAsString();
//            map.put(key, value);
//        }
//        System.out.println(map);

        // Print the text "Hello"
        System.out.print("Hello");
// Delete the text on that line
        System.out.print("\u001b[2K");
// Move the cursor up one line
        System.out.print("\u001b[1F");

// Delete the text on that line
        System.out.print("\u001b[2K");

        // Delete the text on that line
        System.out.print("\u001b[2K");
        // Delete the text on that line
        System.out.print("\u001b[2K");
        // Move the cursor up one line
        System.out.print("\u001b[1F");

// Delete the text on that line
        System.out.print("\u001b[2K");

        // Move the cursor up one line
        System.out.print("\u001b[1F");

// Delete the text on that line
        System.out.print("\u001b[2K");

// Print the text "World" on the same line
        System.out.print("World");

    }

    public static String getApiKey() {
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

    class CountryData {
        public String id;
        public int rank;
        public String Country;
        public String Continent;
        public String TwoLetterSymbol;
        public String ThreeLetterSymbol;
        public double Infection_Risk;
        public double Case_Fatality_Rate;
        public double Test_Percentage;
        public double Recovery_Proporation;
        public long TotalCases;
        public long NewCases;
        public long TotalDeaths;
        public long NewDeaths;
        public String TotalRecovered;
        public long NewRecovered;
        public long ActiveCases;
        public String TotalTests;
        public String Population;
        public int one_Caseevery_X_ppl;
        public int one_Deathevery_X_ppl;
        public int one_Testevery_X_ppl;
        public double Deaths_1M_pop;
        public int Serious_Critical;
        public double Tests_1M_Pop;
        public double TotCases_1M_Pop;
    }
}
