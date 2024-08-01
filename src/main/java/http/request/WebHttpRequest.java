package http.request;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WebHttpRequest {
    private String serverPath;
    private String requesterAddress;
    private String contentType;
    private String method;
    private Map<String, String> parameters;

    public WebHttpRequest(byte[] stream) throws IOException {
        List<String> httpRequestLines = WebHttpRequestHandler.getHttpRequestLines(stream);
        Map<String, String> httpRequestMap = WebHttpRequestHandler.getHttpRequestKeyValues(httpRequestLines);
        initRequest(httpRequestMap);
    }

    private void initRequest(Map<String, String> httpRequestMap){
        setServerPath(httpRequestMap);
        setRequesterAddress(httpRequestMap);
        setContentType(httpRequestMap);
        setMethod(httpRequestMap);
        setParameters(httpRequestMap);
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

    public Map<String, String> getParameters(){
        return parameters;
    }

    private void setServerPath(Map<String, String> httpRequestMap){
        if(httpRequestMap.containsKey("Url")){
            String url = httpRequestMap.get("Url");
            int queryIndex = url.indexOf('?');
            if(queryIndex == -1)
                serverPath = url;
            else
                serverPath = url.substring(0, queryIndex);
        }
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

    private void setParameters(Map<String, String> httpRequest){
        parameters = new HashMap<>();
        String url = httpRequest.get("Url");
        if(url.contains("?")){
            String urlQuery = url.substring(url.indexOf("?") + 1);
            if(!urlQuery.isEmpty()){
                List<String> queries = getUrlQueries(urlQuery);
                for(String query: queries){
                    if(query.contains("=")){
                        parameters.put(query.substring(0, query.indexOf("=")), query.substring(query.indexOf("=")+1));
                    }
                }
            }
        }
    }

    private List<String> getUrlQueries(String query){
        List<String> queries = new LinkedList<>();
        String tempQuery;
        while(query.contains("&")){
             tempQuery = query.substring(0, query.indexOf('&'));
             query = query.substring(query.indexOf('&') + 1);
             queries.add(tempQuery);
        }
        if(!query.isEmpty())
            queries.add(query);
        return queries;
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
