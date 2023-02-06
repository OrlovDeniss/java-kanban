package ru.yandex.practicum.kanban.test.taskserver;

import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.kanban.task.Epic;

import java.util.List;

public class EpicClassServerTest extends HttpTaskServerTest<Epic> {

    EpicClassServerTest() {
        super(Epic.class, new TypeToken<List<Epic>>() {
        }.getType(), new TypeToken<Epic>() {
        }.getType());
    }
}
