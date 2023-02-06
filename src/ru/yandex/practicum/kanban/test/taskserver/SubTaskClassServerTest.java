package ru.yandex.practicum.kanban.test.taskserver;

import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.kanban.task.SubTask;

import java.util.List;

public class SubTaskClassServerTest extends HttpTaskServerTest<SubTask> {

    SubTaskClassServerTest() {
        super(SubTask.class, new TypeToken<List<SubTask>>() {
        }.getType(), new TypeToken<SubTask>() {
        }.getType());
    }
}
