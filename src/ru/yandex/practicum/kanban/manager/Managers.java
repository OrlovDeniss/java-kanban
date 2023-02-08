package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.manager.history.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.manager.taskmanager.HttpTaskManager;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;

import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() throws IOException {
        return new HttpTaskManager("http://localhost:8078");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
