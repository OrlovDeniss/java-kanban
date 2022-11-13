package ru.yandex.practicum.kanban.manager;

public class IdManager {
    private static int freeIdNumber = 0;

    public static int getId() {
        return freeIdNumber++;
    }
}
