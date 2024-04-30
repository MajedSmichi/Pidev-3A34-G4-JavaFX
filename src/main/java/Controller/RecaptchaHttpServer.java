package Controller;

import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.net.InetSocketAddress;

public class RecaptchaHttpServer {
    public static void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/recaptcha", httpExchange -> {
           String response = "<html>\n" +
    "<head>\n" +
    "    <title>reCAPTCHA demo</title>\n" +
    "    <script src=\"https://www.google.com/recaptcha/api.js\" async defer></script>\n" +
    "</head>\n" +
    "<body>\n" +
    "    <form action=\"?\" method=\"POST\">\n" +
    "        <div class=\"g-recaptcha\" data-sitekey=\"6Ld1QIEpAAAAAFXkv9jOb2mdwYyz5OzLwnR4NysB\"></div>\n" +
    "        <br/>\n" +
    "        <input type=\"submit\" value=\"Submit\">\n" +
    "    </form>\n" +
    "</body>\n" +
    "</html>";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
