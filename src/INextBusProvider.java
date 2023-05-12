import java.util.List;

interface INextBusProvider {
    List<StopEta> getLineEta(int agencyId, String lineNumber) throws Exception;
};