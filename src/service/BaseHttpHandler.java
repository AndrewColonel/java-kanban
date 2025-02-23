// Это общий для всех HTTP-обработчиков класс, для каждого из пяти базовых путей будет свой  свой обработчик.
// Этот класс будет содержать общие методы для чтения и отправки данных:
// sendText — для отправки общего ответа в случае успеха;
// sendNotFound — для отправки ответа в случае, если объект не был найден;
// sendHasInteractions — для отправки ответа, если при создании или обновлении задача пересекается с уже существующими.
package service;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;


    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

//    protected void writeResponse(HttpExchange exchange,
//                                 String responseString,
//                                 int responseCode) throws IOException {
//        try (OutputStream os = exchange.getResponseBody()) {
//            exchange.sendResponseHeaders(responseCode, 0);
//            os.write(responseString.getBytes(DEFAULT_CHARSET));
//        }
//        exchange.close();
//    }


    protected void sendNotFound(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(404, 0);
            os.write("Такого эндпоинта не существует".getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }


    protected void sendHasInteractions() {

    }
}
