// Это общий для всех HTTP-обработчиков класс, для каждого из пяти базовых путей будет свой  свой обработчик.
// Этот класс будет содержать общие методы для чтения и отправки данных:
// sendText — для отправки общего ответа в случае успеха;
// sendNotFound — для отправки ответа в случае, если объект не был найден;
// sendHasInteractions — для отправки ответа, если при создании или обновлении задача пересекается с уже существующими.
package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class BaseHttpHandler implements HttpHandler  {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
