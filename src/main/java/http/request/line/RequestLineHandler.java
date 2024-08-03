package http.request.line;

import java.util.List;

public interface RequestLineHandler {
    List<String> getRequestLines(byte[] request);
}
