package ru.yandex.practicum.kanban.manager.web.server.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.kanban.gson.adapter.TaskJsonAdapter;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class AbstractHandler implements HttpHandler {

    protected TaskManager manager;
    protected Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskJsonAdapter())
            .registerTypeAdapter(SubTask.class, new TaskJsonAdapter())
            .registerTypeAdapter(Epic.class, new TaskJsonAdapter())
            .create();
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected AbstractHandler(TaskManager manager) {
        this.manager = manager;
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] bytes = text.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    protected String toJson(Object o) {
        return gson.toJson(o);
    }
}
