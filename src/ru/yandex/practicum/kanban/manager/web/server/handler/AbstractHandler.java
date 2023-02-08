package ru.yandex.practicum.kanban.manager.web.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class AbstractHandler implements HttpHandler {

    protected TaskManager manager;
    protected Gson gson;
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected AbstractHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    protected void sendObjectAsJson(HttpExchange exchange, Object object) throws IOException {
        var json = gson.toJson(object);
        byte[] bytes = json.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    protected void responseNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
    }

    protected void responseOk(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, 0);
    }
}
