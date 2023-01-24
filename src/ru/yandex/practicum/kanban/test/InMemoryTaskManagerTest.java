package ru.yandex.practicum.kanban.test;

import ru.yandex.practicum.kanban.manager.taskmanager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

}
