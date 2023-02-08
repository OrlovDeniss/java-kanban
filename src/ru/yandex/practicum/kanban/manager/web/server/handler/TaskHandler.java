package ru.yandex.practicum.kanban.manager.web.server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.task.Task;

import java.io.IOException;
import java.util.Optional;

public class TaskHandler extends AbstractTaskHandler {

    public TaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    protected void deleteById(HttpExchange exchange) throws IOException {
        var task = findTaskById(getAttributeId(exchange));
        if (task.isPresent()) {
            manager.removeTask(task.get().getId());
            responseOk(exchange);
        } else {
            responseNotFound(exchange);
        }
    }

    @Override
    protected void deleteAll(HttpExchange exchange) throws IOException {
        manager.clearTasks();
        responseOk(exchange);
    }

    @Override
    protected void getById(HttpExchange exchange) throws IOException {
        var task = findTaskById(getAttributeId(exchange));
        if (task.isPresent()) {
            sendObjectAsJson(exchange, task.get());
        } else {
            responseNotFound(exchange);
        }
    }

    @Override
    protected void getAllByType(HttpExchange exchange) throws IOException {
        sendObjectAsJson(exchange, manager.getAllTasks());
    }

    @Override
    protected void post(HttpExchange exchange) throws IOException {
        var inputStream = exchange.getRequestBody();
        var body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        try {
            var task = Optional.ofNullable(gson.fromJson(body, Task.class));
            if (task.isEmpty()) {
                responseNotFound(exchange);
            } else {
                manager.add(task.get());
                responseOk(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            responseNotFound(exchange);
        }
    }

}
