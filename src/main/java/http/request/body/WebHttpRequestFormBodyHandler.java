package http.request.body;

import http.request.WebHttpRequest;

import java.util.HashMap;
import java.util.Map;

public class WebHttpRequestFormBodyHandler implements WebHttpRequestBodyHandler{
    @Override
    public Map<String, String> getBody(WebHttpRequest request) {
        String requestBody = request.getParameters().get("Body");
        Map<String, String> formKeyValue = new HashMap<>();
        String tempKeyValue;
        while (requestBody.contains("&")) {
            tempKeyValue = requestBody.substring(0, requestBody.indexOf('&'));
            if (tempKeyValue.contains("=")) {
                formKeyValue.put(tempKeyValue.substring(0, tempKeyValue.indexOf("=")), tempKeyValue.substring(tempKeyValue.indexOf("=") + 1));
            }
            requestBody = requestBody.substring(requestBody.indexOf('&') + 1);
        }
        if (!requestBody.isEmpty())
            if (requestBody.contains("=")) {
                formKeyValue.put(requestBody.substring(0, requestBody.indexOf("=")), requestBody.substring(requestBody.indexOf("=") + 1));
            }
        return formKeyValue;
    }
}
