package http.request.line;

import http.constant.HttpContentType;

import java.util.HashMap;
import java.util.Map;

public class RequestLineHandlerContext {
    private static RequestLineHandlerContext requestLineHandlerContext;
    private final Map<String, RequestLineHandler> map;

    private RequestLineHandlerContext(){
        map = new HashMap<>();
        map.put(HttpContentType.X_WWW_FORM_URLENCODED, new RequestFormLineHandler());
        map.put(HttpContentType.MULTIPART_FORM_DATA, new RequestMultipartFormLineHandler());
        map.put(HttpContentType.JSON, new RequestJsonLineHandler());
    }

    public static RequestLineHandlerContext context(){
        if(requestLineHandlerContext==null)
            requestLineHandlerContext = new RequestLineHandlerContext();
        return requestLineHandlerContext;
    }

    public RequestLineHandler getLineHandler(String contentType){
        return map.get(contentType);
    }
}
