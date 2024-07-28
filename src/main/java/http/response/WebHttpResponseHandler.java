package http.response;

import context.ApiContext;
import function.ResponseFunction;
import http.request.WebHttpRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class WebHttpResponseHandler {
    private WebHttpResponseHandler(){}
    public static WebHttpResponse getHttpResponse(WebHttpRequest webHttpRequest) throws IOException {
        ResponseEntity response = ApiContext.context().getResponse(webHttpRequest);
        return new WebHttpResponse(response.getStatus(), response.getMessage(), response.getData());
    }

    public static void writeResponseTo(Socket requester, WebHttpResponse response) throws IOException{
        try(PrintWriter responseWriter = new PrintWriter(requester.getOutputStream())){
            responseWriter.write("HTTP/1.1 "+response.getStatus()+" "+response.getMessage()+"\r\n");
            responseWriter.write("\r\n");
            responseWriter.write(response.getData());
            responseWriter.flush();
        }
    }
}
