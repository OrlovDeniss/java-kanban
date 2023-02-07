package ru.yandex.practicum.kanban.test.kv;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.web.kv.KVServer;
import ru.yandex.practicum.kanban.manager.web.kv.KVTaskClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KVClientServerTest {

    KVServer server;
    KVTaskClient client;
    String url = "http://localhost:8078";

    @BeforeEach
    void beforeEach() throws IOException {
        server = new KVServer();
        server.start();
        client = new KVTaskClient(url);
    }

    @AfterEach
    void afterEach() {
        server.stop();
    }

    @Test
    void saveLoadTest() {
        var key = "key";
        var value = "value";
        var newValue = "newValue";
        client.put(key, value);
        assertEquals(value, client.load(key));
        client.put(key, newValue);
        assertEquals(newValue, client.load(key));
    }

}
