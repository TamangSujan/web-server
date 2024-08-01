package http.request;

import java.io.IOException;
import java.io.InputStream;

public class WebHttpRequestReader {
    private WebHttpRequestReader(){}
     public static String getRequestStream(byte[] stream) {
        return getStream(stream).toString();
    }

    private static StringBuilder getStream(byte[] stream) {
        StringBuilder requestStream = new StringBuilder();
        for(byte data: stream){
            requestStream.append((char) data);
        }
        return requestStream;
    }

    public static void consumeStream(InputStream stream) throws IOException{
        int data;
        StringBuilder buffer = new StringBuilder();
        while((data = stream.read())>0){
            buffer.append((char)data);
        }
        System.out.println(buffer);
    }
}
