package http.request.body;

import java.util.Map;

public interface WebHttpRequestBodyHandler {
    Map<String, String> getBody(String body);
}
