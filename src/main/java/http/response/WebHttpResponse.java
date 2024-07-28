package http.response;


public class WebHttpResponse {
    private final int status;
    private final String message;
    private final String data;

    public WebHttpResponse(int status, String message, String data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }
    public String getData(){
        return data;
    }
}
