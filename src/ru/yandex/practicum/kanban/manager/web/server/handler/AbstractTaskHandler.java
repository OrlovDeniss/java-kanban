package ru.yandex.practicum.kanban.manager.web.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractTaskHandler extends AbstractHandler {

    AbstractTaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) {
        EndPoint endPoint = getEndPoint(exchange);
        try {
            switch (endPoint) {
                case GET_ALL:
                    getAllByType(exchange);
                    break;
                case GET_BY_ID:
                    getById(exchange);
                    break;
                case POST:
                    post(exchange);
                    break;
                case DEL_ALL:
                    deleteAll(exchange);
                    break;
                case DEL_BY_ID:
                    deleteById(exchange);
                    break;
                default:
                    unknown(exchange);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    protected void unknown(HttpExchange exchange) throws IOException {
        responseNotFound(exchange);
    }

    protected void deleteById(HttpExchange exchange) throws IOException {
        responseNotFound(exchange);
    }

    protected void deleteAll(HttpExchange exchange) throws IOException {
        responseNotFound(exchange);
    }

    protected void getById(HttpExchange exchange) throws IOException {
        responseNotFound(exchange);
    }

    protected void getAllByType(HttpExchange exchange) throws IOException {
        responseNotFound(exchange);
    }

    protected void post(HttpExchange exchange) throws IOException {
        responseNotFound(exchange);
    }

    protected EndPoint getEndPoint(HttpExchange exchange) {
        var request = Request.valueOf(exchange.getRequestMethod());
        var query = exchange.getRequestURI().getQuery();
        var isQuery = Objects.nonNull(query);
        var isCorrectQuery = isQuery && checkQuery(exchange, query);

        switch (request) {
            case GET:
                if (!isQuery) {
                    return EndPoint.GET_ALL;
                } else if (isCorrectQuery) {
                    return EndPoint.GET_BY_ID;
                } else {
                    return EndPoint.UNKNOWN;
                }
            case POST:
                return EndPoint.POST;
            case DELETE:
                if (!isQuery) {
                    return EndPoint.DEL_ALL;
                } else if (isCorrectQuery) {
                    return EndPoint.DEL_BY_ID;
                } else {
                    return EndPoint.UNKNOWN;
                }
            default:
                return EndPoint.UNKNOWN;
        }
    }

    private boolean checkQuery(HttpExchange exchange, String query) {
        var splitQuery = query.split("=");
        var isCorrectQuerySize = splitQuery.length == 2;
        var tegName = splitQuery[0];
        var isCorrectTeg = tegName.equals("id");
        var stringId = splitQuery[1];

        return isCorrectQuerySize && isCorrectTeg && checkAndSetAttributeId(exchange, stringId);
    }

    private boolean checkAndSetAttributeId(HttpExchange exchange, String query) {
        Long id = parseLong(query);
        if (id >= 0) {
            exchange.setAttribute("id", id);
            return true;
        } else {
            return false;
        }
    }

    private Long parseLong(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    protected Long getAttributeId(HttpExchange exchange) {
        return (Long) exchange.getAttribute("id");
    }

    protected Optional<Task> findTaskById(Long id) {
        return manager.getAllTasks().stream()
                .filter(t -> Objects.equals(t.getId(), id))
                .findFirst();
    }

    protected Optional<SubTask> findSubTaskById(Long id) {
        return manager.getAllSubTasks().stream()
                .filter(t -> Objects.equals(t.getId(), id))
                .findFirst();
    }

    protected Optional<Epic> findEpicById(Long id) {
        return manager.getAllEpicTasks().stream()
                .filter(t -> Objects.equals(t.getId(), id))
                .findFirst();
    }
}
