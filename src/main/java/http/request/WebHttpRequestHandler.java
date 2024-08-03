package http.request;

import http.constant.HttpContentType;
import http.request.line.RequestLineHandlerContext;

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

    protected static List<String> getHttpRequestLines(byte[] stream) {
        String meta = WebHttpRequestReader.getRequestStream(stream);
        int contentTypeIndex = meta.indexOf("content-type: ");
        String contentType = HttpContentType.X_WWW_FORM_URLENCODED;
        if(contentTypeIndex != -1) {
            contentType = meta.substring(contentTypeIndex);
            contentType = contentType.substring(0, contentType.indexOf("\r\n"));
            if (contentType.contains(";")) {
                contentType = contentType.substring(0, contentType.indexOf(";"));
            }
        }
        List<String> requestLines = RequestLineHandlerContext.context().getLineHandler(contentType).getRequestLines(stream);
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
