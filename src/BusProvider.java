import java.util.List;

public interface BusProvider {

    String request() throws Exception;

    List<StopEta> parseResponse(String response);

    String getHost();

    String getLineNumber();

}
