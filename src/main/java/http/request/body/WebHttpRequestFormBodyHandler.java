package http.request.body;

import http.request.WebHttpRequest;

import java.util.HashMap;
import java.util.Map;

public class WebHttpRequestFormBodyHandler implements WebHttpRequestBodyHandler{
    @Override
    public Map<String, String> getBody(String body) {
        Map<String, String> formKeyValue = new HashMap<>();
        String tempKeyValue;
        while (body.contains("&")) {
            tempKeyValue = body.substring(0, body.indexOf('&'));
            if (tempKeyValue.contains("=")) {
                formKeyValue.put(tempKeyValue.substring(0, tempKeyValue.indexOf("=")), tempKeyValue.substring(tempKeyValue.indexOf("=") + 1));
            }
            body = body.substring(body.indexOf('&') + 1);
        }
        if (!body.isEmpty())
            if (body.contains("=")) {
                formKeyValue.put(body.substring(0, body.indexOf("=")), body.substring(body.indexOf("=") + 1));
            }
        return formKeyValue;
    }
}
