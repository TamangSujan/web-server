package http.request.line;

import http.request.WebHttpRequestReader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class RequestFormLineHandler implements RequestLineHandler{
    @Override
    public List<String> getRequestLines(byte[] request) {
        String[] meta = WebHttpRequestReader.getRequestStream(request).split("\r\n\r\n");
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
        return requestLines;
    }
}
