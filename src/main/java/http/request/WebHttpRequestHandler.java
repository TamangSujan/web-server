package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;

public class WebHttpRequestHandler {
    private WebHttpRequestHandler(){}
    public static WebHttpRequest getHttpRequest(Socket request) throws IOException {
        return new WebHttpRequest(request.getInputStream());
    }

    protected static List<String> getHttpRequestLines(InputStream stream) throws IOException {
        String[] meta = WebHttpRequestReader.getRequestLines(stream);
        String body = "Body: " + WebHttpRequestReader.getRequestStream(stream);
        List<String> requestLines = new LinkedList<>(Arrays.asList(meta));
        requestLines.add(body);
        initializeHttpMethodPathAndVersion(requestLines);
        return requestLines;
    }

    private static void initializeHttpMethodPathAndVersion(List<String> requestLines){
        String[] value = requestLines.getFirst().split(" ");
        requestLines.add("Method: "+value[0]);
        requestLines.add("Url: "+value[1]);
        requestLines.add("Version: "+value[2]);
        requestLines.removeFirst();
    }

    protected static Map<String, String> getHttpRequestKeyValues(List<String> httpRequestLines){
        Map<String, String> requestMap = new HashMap<>();
        for(String requestLine: httpRequestLines){
            addKeyValueInRequestMap(requestMap, requestLine);
        }
        return requestMap;
    }

    protected static void addKeyValueInRequestMap(Map<String, String> requestMap, String requestLine){
        int separatorIndex = requestLine.indexOf(": ");
        requestMap.put(
                requestLine.substring(0, separatorIndex),
                requestLine.substring(separatorIndex + 2)
        );
    }
}
