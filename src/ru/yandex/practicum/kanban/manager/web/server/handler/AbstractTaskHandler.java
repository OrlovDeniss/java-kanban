package ru.yandex.practicum.kanban.manager.web.server.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;

import java.io.IOException;
import java.util.Objects;

public abstract class AbstractTaskHandler extends AbstractHandler {

    AbstractTaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        EndPoint endPoint = getEndPoint(exchange);
        try {
            switch (endPoint) {
                case GET_ALL:
                    getAllHandler(exchange);
                    break;
                case GET_BY_ID:
                    getByIdHandler(exchange);
                    break;
                case POST:
                    postHandler(exchange);
                    break;
                case DEL_ALL:
                    delAllHandler(exchange);
                    break;
                case DEL_BY_ID:
                    delByIdHandler(exchange);
                    break;
                default:
                    unknownHandler(exchange);
            }
        } catch (IOException  e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    protected abstract void unknownHandler(HttpExchange exchange) throws IOException;

    protected abstract void delByIdHandler(HttpExchange exchange) throws IOException;

    protected abstract void delAllHandler(HttpExchange exchange) throws IOException;

    protected abstract void getByIdHandler(HttpExchange exchange) throws IOException;

    protected abstract void getAllHandler(HttpExchange exchange) throws IOException;

    protected abstract void postHandler(HttpExchange exchange) throws IOException;

    protected EndPoint getEndPoint(HttpExchange exchange) {
        var request = Request.valueOf(exchange.getRequestMethod());
        var query = exchange.getRequestURI().getQuery();
        var isQuery = Objects.nonNull(query);
        boolean isCorrectQuery = isQuery && checkQuery(exchange, query);

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

        return isCorrectQuerySize && isCorrectTeg && checkAndSetBufferId(exchange, stringId);
    }

    private boolean checkAndSetBufferId(HttpExchange exchange, String query) {
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

}
