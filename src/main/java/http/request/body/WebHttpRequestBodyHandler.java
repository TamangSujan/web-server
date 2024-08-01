package http.request.body;

import http.request.WebHttpRequest;

import java.util.Map;

public interface WebHttpRequestBodyHandler {
    Map<String, String> getBody(WebHttpRequest request);
}
