import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class BusProvider2 implements BusProvider{

    private final String host;
    private final String lineNumber;

    public BusProvider2(String host, String lineNumber) {
        this.host = host;
        this.lineNumber = lineNumber;
    }

    private String generateRequestBody(String lineNumber) {
        return "<Query>" +
                "<LineID>" +
                lineNumber +
                "</LineID>" +
                "</Query>";
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
        // TODO
        return new ArrayList<>();
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
