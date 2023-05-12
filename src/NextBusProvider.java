import java.util.List;

public class NextBusProvider implements INextBusProvider {

    public static void main(String[] args) throws Exception {
        NextBusProvider obj = new NextBusProvider();
        List<StopEta> result = obj.getLineEta(1, "6");
    }

    public List<StopEta> getLineEta(int agencyId, String lineNumber) throws Exception {
        BusProviderFactory manager = new BusProviderFactory();
        BusProvider provider = manager.createBusProvider(agencyId, lineNumber);

        String lineEtas = provider.request();
        return provider.parseResponse(lineEtas);
    }
}



