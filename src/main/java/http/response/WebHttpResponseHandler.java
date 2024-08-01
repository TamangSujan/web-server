package http.response;

import context.ApiContext;
import function.ResponseFunction;
import http.constant.HttpContentType;
import http.request.WebHttpRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WebHttpResponseHandler {
    private WebHttpResponseHandler(){}
    public static WebHttpResponse getHttpResponse(WebHttpRequest webHttpRequest) throws InvocationTargetException, IllegalAccessException {
        ResponseEntity response = ApiContext.context().getResponse(webHttpRequest);
        return new WebHttpResponse(response.getStatus(), response.getMessage(), response.getData());
    }

    public static void writeResponseTo(SocketChannel requester, WebHttpResponse response) throws IOException{
        StringBuilder bufferBuilder = new StringBuilder();
        bufferBuilder.append("HTTP/1.1 ").append(response.getStatus()).append(" ").append(response.getMessage()).append("\r\n");
        bufferBuilder.append("\r\n");
        bufferBuilder.append(response.getData());
        ByteBuffer buffer = ByteBuffer.wrap(bufferBuilder.toString().getBytes());
        while (buffer.hasRemaining()){
            requester.write(buffer);
        }
        buffer.compact();
        requester.close();
    }
}
