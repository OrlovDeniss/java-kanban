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
        this.manager = manager;
    }

    @Override
    protected void unknownHandler(HttpExchange exchange) throws IOException {

        exchange.sendResponseHeaders(400, 0);

    }

    @Override
    protected void delByIdHandler(HttpExchange exchange) throws IOException, InterruptedException {

        manager.removeSubTask(bufferId);

        exchange.sendResponseHeaders(200, 0);

    }

    @Override
    protected void delAllHandler(HttpExchange exchange) throws IOException, InterruptedException {

        manager.clearSubTasks();

        exchange.sendResponseHeaders(200, 0);

    }

    @Override
    protected void getByIdHandler(HttpExchange exchange) throws IOException, InterruptedException {

        sendText(exchange, gson.toJson(manager.getSubTask(bufferId)));

    }

    @Override
    protected void getAllHandler(HttpExchange exchange) throws IOException {

        sendText(exchange, gson.toJson(manager.getAllSubTasks()));

    }

    @Override
    protected void postHandler(HttpExchange exchange) throws IOException, InterruptedException {

        var inputStream = exchange.getRequestBody();
        var body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

        Optional<Task> task = Optional.empty();

        try {
            task = Optional.of(gson.fromJson(body, SubTask.class));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, 0);
        }

        if (task.isPresent()) {
            manager.add(task.get());
            exchange.sendResponseHeaders(200, 0);
        }
    }
}
