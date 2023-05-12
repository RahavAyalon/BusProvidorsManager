import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BusProvider1 implements BusProvider {

    private final String host;
    private final String lineNumber;

    public BusProvider1(String host, String lineNumber) {
        this.host = host;
        this.lineNumber = lineNumber;
    }

    @Override
    public String request() throws Exception {
        try {
            URL url = new URL(this.getHost());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (MalformedURLException e) {
            // Handle malformed URL exception
            throw new Exception("Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            // Handle input/output exception
            throw new Exception("Error reading input stream: " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            throw new Exception("Error occurred: " + e.getMessage());
        }
    }

    @Override
    public List<StopEta> parseResponse(String response) {
        // Create a Gson object
        Gson gson = new Gson();

        // Parse the JSON string into a Map object
        Type mapType = new TypeToken<Map<String, List<Map<String, Object>>>>(){}.getType();
        Map<String, List<Map<String, Object>>> map = gson.fromJson(response, mapType);

        // Get the "arrivals" list from the Map
        List<Map<String, Object>> arrivalsList = map.get("arrivals");

        // Create a list to store the StopEta objects
        List<StopEta> stopEtaList = new ArrayList<>();

        // Loop through the arrivals list and create a StopEta object for each item
        for (Map<String, Object> arrival : arrivalsList) {

            int stopId = ((Double) arrival.get("stopId")).intValue();
            long eta = ((Double) arrival.get("eta")).longValue();

            StopEta stopEta = new StopEta();

            stopEta.stopId = stopId;
            stopEta.eta = new Date(eta);

            stopEtaList.add(stopEta);
        }

        return stopEtaList;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public String getLineNumber() {
        return this.lineNumber;
    }
}
