package ru.yandex.practicum.kanban.test.taskserver;

import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.kanban.task.Task;

import java.util.List;

public class TaskClassServerTest extends HttpTaskServerTest<Task> {

    TaskClassServerTest() {
        super(Task.class, new TypeToken<List<Task>>() {
        }.getType(), new TypeToken<Task>() {
        }.getType());
    }

}
