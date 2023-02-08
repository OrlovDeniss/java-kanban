package ru.yandex.practicum.kanban.test.kv;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.web.kv.KVServer;
import ru.yandex.practicum.kanban.manager.web.kv.KVTaskClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KVClientServerTest {

    private static KVServer KvServer;
    private static KVTaskClient KvClient;
    private static String url = "http://localhost:8078";

    @BeforeAll
    static void beforeAll() throws IOException {
        KvServer = new KVServer();
        KvServer.start();
        KvClient = new KVTaskClient(url);
    }

    @AfterAll
    static void afterAll() {
        KvServer.stop();
    }

    @Test
    void saveLoadTest() throws IOException {
        var key = "key";
        var value = "value";
        var newValue = "newValue";
        KvClient.put(key, value);
        assertEquals(value, KvClient.load(key));
        KvClient.put(key, newValue);
        assertEquals(newValue, KvClient.load(key));
    }

}
