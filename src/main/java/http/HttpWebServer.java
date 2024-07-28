package http;

import http.request.WebHttpRequest;
import http.request.WebHttpRequestHandler;
import http.response.WebHttpResponse;
import http.response.WebHttpResponseHandler;
import utils.converter.PrimitiveConverter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpWebServer {
    private HttpWebServer(){}

    public static void run(String port) throws IOException {
        int intPort = PrimitiveConverter.getIntFrom(port);
        try(ServerSocket serverSocket = new ServerSocket(intPort)){
            System.out.println("Server Running on port: "+port);
            while(true){
                Socket requester = serverSocket.accept();
                new Thread(()->{
                    try {
                        WebHttpRequest webHttpRequest = WebHttpRequestHandler.getHttpRequest(requester);
                        WebHttpResponse webHttpResponse = WebHttpResponseHandler.getHttpResponse(webHttpRequest);
                        WebHttpResponseHandler.writeResponseTo(requester, webHttpResponse);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        }
    }
}
