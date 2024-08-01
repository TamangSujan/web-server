package page;

public class WebPageHandler {
    public static String getWebPage(String pageName){
        return """
                <html>
                    <head>
                        <title>Test Form</title>
                    </head>
                    <body>
                        <form method="POST" action="/test/form">
                            <input type="text" name="username"/>
                            <input type="password" name="password"/>
                            <input type="submit" value="Login"/>
                        </form>
                    </body>
                </html>
                """;
    }
}
