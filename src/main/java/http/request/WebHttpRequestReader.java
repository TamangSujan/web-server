package http.request;

import java.io.IOException;
import java.io.InputStream;

public class WebHttpRequestReader {
    private WebHttpRequestReader(){}

    public static String[] getRequestLines(InputStream stream) throws IOException {
        return getStream(stream).toString().split("\n");
    }

    public static String getRequestStream(InputStream stream) throws IOException {
        return getStream(stream).toString();
    }

    private static StringBuilder getStream(InputStream stream) throws IOException {
        StringBuilder requestStream = new StringBuilder();
        int data;
        boolean prevCarriageReturn = false;
        int endOfLineCount = 0;
        while((data = stream.read())>0 && endOfLineCount < 2){
            char c = (char) data;
            if(c == '\r'){
                prevCarriageReturn = true;

            }else{
                if(prevCarriageReturn){
                    if(c=='\n'){
                        endOfLineCount++;
                    }else{
                        endOfLineCount = 0;
                    }
                    prevCarriageReturn = false;
                }
            }
            requestStream.append(c);
        }
        return requestStream;
    }
}
