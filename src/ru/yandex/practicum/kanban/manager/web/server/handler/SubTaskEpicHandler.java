package ru.yandex.practicum.kanban.manager.web.server.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;

import java.io.IOException;

public class SubTaskEpicHandler extends AbstractTaskHandler {

    public SubTaskEpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    protected void unknownHandler(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
    }

    @Override
    protected void delByIdHandler(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
    }

    @Override
    protected void delAllHandler(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
    }

    @Override
    public void getByIdHandler(HttpExchange exchange) throws IOException {
        sendText(exchange, toJson(manager.getEpic(getAttributeId(exchange)).getAllSubTasks()));
    }

    @Override
    protected void getAllHandler(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
    }

    @Override
    protected void postHandler(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
    }

}
