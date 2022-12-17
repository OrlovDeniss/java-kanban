package ru.yandex.practicum.kanban;

public enum Status {

    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
