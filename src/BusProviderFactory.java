import java.util.HashMap;
import java.util.Map;

public class BusProviderFactory {
    public BusProvider createBusProvider(int agencyId, String lineNumber) {
        Map<Integer, String> hosts = new HashMap<Integer, String>() {{
            put(1, "http://d2fo80vv1pnzuu.cloudfront.net/test/");
            put(2, "https://ag2.org/arrivals");
            put(3, "https://provider2.com/etas");
            put(4, "https://abcdefg.org/arrivals?lineNumber=" + lineNumber);
        }};

        String host = hosts.get(agencyId);
        if (host == null) {
            throw new IllegalArgumentException("Invalid agency ID: " + agencyId);
        }
        switch (agencyId) {
            case 1 -> {
                return new BusProvider1(host, null);
            }
            case 2 -> {
                if (lineNumber == null || lineNumber.isEmpty()) {
                    throw new IllegalArgumentException("Line number is required for agency 2");
                }
                return new BusProvider2(host, lineNumber);
            }
            case 3 -> {
                if (lineNumber == null || lineNumber.isEmpty()) {
                    throw new IllegalArgumentException("Line number is required agency 3");
                }
                return new BusProvider3(host, lineNumber);
            }
            case 4 -> {
                return new BusProvider4(host, null);
            }
            default -> {
                throw new IllegalArgumentException("Invalid agency ID: " + agencyId);
            }
        }
    }

}
