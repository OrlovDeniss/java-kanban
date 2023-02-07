package ru.yandex.practicum.kanban.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.gson.adapter.TaskJsonAdapter;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTypeAdapterTest {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskJsonAdapter())
            .registerTypeAdapter(SubTask.class, new TaskJsonAdapter())
            .registerTypeAdapter(Epic.class, new TaskJsonAdapter())
            .create();

    @Test
    public void taskJsonAdapterTasksTest() {
        var task = new Task();
        var epic = new Epic();
        var subTask = new SubTask(epic);
        var taskJson = gson.toJson(task);
        var epicJson = gson.toJson(epic);
        var subTaskJson = gson.toJson(subTask);
        assertEquals(task, gson.fromJson(taskJson, Task.class));
        assertEquals(epic, gson.fromJson(epicJson, Epic.class));
        assertEquals(subTask, gson.fromJson(subTaskJson, SubTask.class));
    }

    @Test
    void taskJsonAdapterArrayTasksTest() {
        var task = new Task();
        var epic = new Epic();
        var subTask = new SubTask(epic);
        List<Task> tasks = Arrays.asList(task, epic, subTask);
        Type listType = new TypeToken<List<Task>>() {}.getType();
        String json = gson.toJson(tasks);
        List<Task> fromJson = gson.fromJson(json, listType);
        assertEquals(task, fromJson.get(0));
        assertEquals(epic, fromJson.get(1));
        assertEquals(subTask, fromJson.get(2));
    }

}
