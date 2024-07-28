import assertions.Assert;
import context.AppConfigContext;

import java.io.IOException;

public class AppConfigContextTest {
    public static void main(String[] args) throws IOException {
        AppConfigContext context = AppConfigContext.context();
        Assert.print(context.getValue("port"));
    }
}
