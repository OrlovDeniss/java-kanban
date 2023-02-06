package ru.yandex.practicum.kanban.manager.web.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class AbstractHandler implements HttpHandler {

    protected TaskManager manager;
    protected Gson gson = Managers.getGson();
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {

        byte[] bytes = text.getBytes(DEFAULT_CHARSET);

        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }

    }
}
