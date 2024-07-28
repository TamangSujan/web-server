import context.AppConfigContext;
import http.HttpWebServer;

import java.io.IOException;

public class JWebServerApplication {
    public static void main(String[] args) throws IOException {
        String port = AppConfigContext.context().getValue("port");
        //ServerBanner.printBanner();
        HttpWebServer.run(port);
    }
}
