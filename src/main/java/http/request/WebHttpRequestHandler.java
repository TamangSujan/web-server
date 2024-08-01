package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;

public class WebHttpRequestHandler {
    private WebHttpRequestHandler(){}
    public static WebHttpRequest getHttpRequest(byte[] stream) throws IOException {
        return new WebHttpRequest(stream);
    }

    protected static List<String> getHttpRequestLines(byte[] stream) throws IOException {
        String[] meta = WebHttpRequestReader.getRequestStream(stream).split("\r\n\r\n");
        String headers;
        String body = "Body: ";
        if(meta.length == 2){
            headers = meta[0];
            body = "Body: "+meta[1];
        }else{
            headers = meta[0];
        }
        List<String> requestLines = new LinkedList<>(Arrays.asList(headers.split("\r\n")));
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
        if(separatorIndex>2){
            requestMap.put(
                    requestLine.substring(0, separatorIndex),
                    requestLine.substring(separatorIndex + 2)
            );
        }
    }
}
