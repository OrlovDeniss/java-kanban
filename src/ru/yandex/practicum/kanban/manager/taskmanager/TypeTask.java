package ru.yandex.practicum.kanban.manager.taskmanager;

public enum TypeTask {

    TASK("TASK"),
    SUBTASK("SUBTASK"),
    EPIC("EPIC");

    private final String name;

    TypeTask(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
