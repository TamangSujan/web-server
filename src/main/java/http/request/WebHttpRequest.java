package http.request;

import java.io.*;
import java.util.List;
import java.util.Map;

public class WebHttpRequest {
    private String serverPath;
    private String requesterAddress;
    private String contentType;
    private String method;

    public WebHttpRequest(InputStream stream) throws IOException {
        List<String> httpRequestLines = WebHttpRequestHandler.getHttpRequestLines(stream);
        Map<String, String> httpRequestMap = WebHttpRequestHandler.getHttpRequestKeyValues(httpRequestLines);
        initRequest(httpRequestMap);
    }

    private void initRequest(Map<String, String> httpRequestMap){
        setServerPath(httpRequestMap);
        setRequesterAddress(httpRequestMap);
        setContentType(httpRequestMap);
        setMethod(httpRequestMap);
    }

    public String getServerPath(){
        return serverPath;
    }

    public String getRequesterAddress(){
        return requesterAddress;
    }

    public String getContentType(){
        return contentType;
    }

    public String getMethod(){
        return method;
    }

    private void setServerPath(Map<String, String> httpRequestMap){
        if(httpRequestMap.containsKey("Url"))
            serverPath = httpRequestMap.get("Url");
    }

    private void setRequesterAddress(Map<String, String> httpRequestMap){
        if(httpRequestMap.containsKey("Host"))
            requesterAddress = httpRequestMap.get("Host");
    }

    private void setContentType(Map<String, String> httpRequestMap){
        if(httpRequestMap.containsKey("Content-Type"))
            contentType = httpRequestMap.get("Content-Type");
    }
    private void setMethod(Map<String, String> httpRequestMap){
        if(httpRequestMap.containsKey("Method"))
            method = httpRequestMap.get("Method");
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Method: ").append(method).append("\n");
        builder.append("Server Path: ").append(serverPath).append("\n");
        builder.append("Requester Address: ").append(requesterAddress).append("\n");
        builder.append("Content-Type: ").append(contentType).append("\n");
        return builder.toString();
    }
}
