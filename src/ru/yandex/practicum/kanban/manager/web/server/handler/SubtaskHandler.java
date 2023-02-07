package ru.yandex.practicum.kanban.manager.web.server.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.IOException;
import java.util.Optional;

public class SubtaskHandler extends AbstractTaskHandler {

    public SubtaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    protected void unknownHandler(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
    }

    @Override
    protected void delByIdHandler(HttpExchange exchange) throws IOException {
        manager.removeSubTask(getAttributeId(exchange));
        exchange.sendResponseHeaders(200, 0);
    }

    @Override
    protected void delAllHandler(HttpExchange exchange) throws IOException {
        manager.clearSubTasks();
        exchange.sendResponseHeaders(200, 0);
    }

    @Override
    protected void getByIdHandler(HttpExchange exchange) throws IOException {
        sendText(exchange, toJson(manager.getSubTask(getAttributeId(exchange))));
    }

    @Override
    protected void getAllHandler(HttpExchange exchange) throws IOException {
        sendText(exchange, toJson(manager.getAllSubTasks()));
    }

    @Override
    protected void postHandler(HttpExchange exchange) throws IOException {
        var inputStream = exchange.getRequestBody();
        var body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        Optional<Task> task;
        try {
            task = Optional.of(gson.fromJson(body, SubTask.class));
            if (task.isEmpty()) {
                exchange.sendResponseHeaders(404, 0);
            } else {
                manager.add(task.get());
                exchange.sendResponseHeaders(200, 0);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(404, 0);
        }
    }

}
