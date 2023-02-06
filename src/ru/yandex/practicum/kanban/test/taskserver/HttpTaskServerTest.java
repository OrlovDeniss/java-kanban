package ru.yandex.practicum.kanban.test.taskserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.manager.web.kv.KVServer;
import ru.yandex.practicum.kanban.manager.web.server.HttpTaskServer;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class HttpTaskServerTest<T extends Task> {

    private KVServer kvServer;
    private HttpTaskServer taskServer;
    private TaskManager taskManager;
    private Gson gson;
    private Task task;
    private Epic epic;
    private Task referenceTask;
    private SubTask subTask;
    private Class<T> t;
    private Type listType;
    private Type taskType;

    HttpTaskServerTest(Class<T> t, Type listType, Type taskType) {

        this.t = t;
        this.listType = listType;
        this.taskType = taskType;

    }

    @BeforeEach
    void beforeEach() throws IOException, InterruptedException {

        gson = Managers.getGson();
        kvServer = new KVServer();
        kvServer.start();
        taskServer = new HttpTaskServer();

        taskManager = taskServer.getManager();

        task = new Task();
        epic = new Epic();
        subTask = new SubTask(epic);

        task.setStartTime(LocalDateTime.of(2023, 1, 1, 13, 0));
        task.setDuration(Duration.ofMinutes(60));

        subTask.setStartTime(LocalDateTime.of(2023,1,1,18,0));
        subTask.setDuration(Duration.ofMinutes(60));

        taskManager.add(task);
        taskManager.add(epic);
        taskManager.add(subTask);

        taskManager.getSubTask(subTask.getId());
        taskManager.getEpic(epic.getId());

        switch (t.getSimpleName()) {
            case "Task":
                referenceTask = task;
                break;
            case "Epic":
                referenceTask = epic;
                break;
            case "SubTask":
                referenceTask = subTask;
                break;
        }

    }

    @AfterEach
    void afterEach() {
        taskServer.stop();
        kvServer.stop();
    }

    @Test
    public void getAllTasks() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + t.getSimpleName().toLowerCase());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<T> tasks = gson.fromJson(response.body(), listType);

        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        assertEquals(referenceTask, tasks.get(0));

    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + t.getSimpleName().toLowerCase() + "/?id=" + referenceTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        T taskById = gson.fromJson(response.body(), taskType);

        assertNotNull(taskById);
        assertEquals(referenceTask, taskById);

    }

    @Test
    public void postTask() throws IOException, InterruptedException {

        var json = gson.toJson(referenceTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());


        url = URI.create("http://localhost:8080/tasks/" + t.getSimpleName().toLowerCase() + "/?id=" + referenceTask.getId());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        T taskFromServer = gson.fromJson(response.body(), taskType);

        assertNotNull(taskFromServer);
        assertEquals(referenceTask, taskFromServer);

    }

    @Test
    public void deleteTaskById() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + t.getSimpleName().toLowerCase() + "/?id=" + referenceTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/" + t.getSimpleName().toLowerCase());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<T> tasks = gson.fromJson(response.body(), listType);

        assertNotNull(tasks);
        assertEquals(0, tasks.size());

    }

    @Test
    public void deleteAll() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + t.getSimpleName().toLowerCase());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/" + t.getSimpleName().toLowerCase());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<T> tasks = gson.fromJson(response.body(), listType);

        assertNotNull(tasks);
        assertEquals(0, tasks.size());

    }

    @Test
    public void getEpicSubTasks() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type lstType = new TypeToken<List<SubTask>>() {
        }.getType();

        List<SubTask> tasksFromServer = gson.fromJson(response.body(), lstType);

        assertNotNull(tasksFromServer);
        assertEquals(1, tasksFromServer.size());

        assertEquals(subTask, tasksFromServer.get(0));

    }

    @Test
    public void getHistory() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type lstType = new TypeToken<List<Task>>() {
        }.getType();

        List<Task> tasksFromServer = gson.fromJson(response.body(), lstType);

        assertNotNull(tasksFromServer);
        assertEquals(2, tasksFromServer.size());
        assertEquals(subTask, tasksFromServer.get(0));
        assertEquals(epic, tasksFromServer.get(1));

    }

    @Test
    public void getPrioritizedTasks() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type lstType = new TypeToken<List<Task>>() {
        }.getType();

        List<Task> tasksFromServer = gson.fromJson(response.body(), lstType);

        assertNotNull(tasksFromServer);
        assertEquals(2, tasksFromServer.size());
        assertEquals(task, tasksFromServer.get(0));
        assertEquals(subTask, tasksFromServer.get(1));

    }

}
