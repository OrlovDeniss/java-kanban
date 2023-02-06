package ru.yandex.practicum.kanban.manager.web.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;

import java.io.IOException;

public class SubTaskEpicHandler extends EpicHandler implements HttpHandler {
    public SubTaskEpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {

        bufferId = -1L;

        EndPoint endPoint = getEndPoint(exchange);

        try {

            if (endPoint == EndPoint.GET_BY_ID) {
                getByIdHandler(exchange);
            } else {
                unknownHandler(exchange);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }

    }

    @Override
    public void getByIdHandler(HttpExchange exchange) throws IOException, InterruptedException {

        sendText(exchange, gson.toJson(manager.getEpic(bufferId).getAllSubTasks()));

    }

}
