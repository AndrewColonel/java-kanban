package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class BaseHttpHandler implements HttpHandler  {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
