package http.request;

import java.io.IOException;
import java.io.InputStream;

public class WebHttpRequestReader {
    private WebHttpRequestReader(){}
     public static String getRequestStream(byte[] stream) {
        return new String(stream);
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
