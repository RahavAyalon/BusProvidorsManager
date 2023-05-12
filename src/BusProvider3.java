import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class BusProvider3 implements BusProvider{

    private final String host;
    private final String lineNumber;

    public BusProvider3(String host, String lineNumber) {
        this.host = host;
        this.lineNumber = lineNumber;
    }

    private String generateRequestBody(String lineNumber) {
        // Helper Classes
        class LineRequest {
            private String lineId;

            public LineRequest(String lineId) {
                this.lineId = lineId;
            }
        }
        class LineRequestWrapper {
            private LineRequest lineRequest;

            public LineRequestWrapper(LineRequest lineRequest) {
                this.lineRequest = lineRequest;
            }
        }

        // Create a LineRequest object with the given string as the lineId
        LineRequest lineRequest = new LineRequest(lineNumber);

        // Wrap the LineRequest object in a top-level JSON object with key "lineRequest"
        LineRequestWrapper wrapper = new LineRequestWrapper(lineRequest);

        // Convert the wrapper object to JSON using Gson library
        Gson gson = new Gson();
        return gson.toJson(wrapper);
    }
    @Override
    public String request() throws Exception {
        try {
            URL url = new URL(this.getHost());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true); // Enable output

            // generate request body
            String body = generateRequestBody(this.getLineNumber());
            // Write request body to output stream
            OutputStream os = connection.getOutputStream();
            os.write(body.getBytes());
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

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
            throw new Exception("Error reading/writing to connection: " + e.getMessage());
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
