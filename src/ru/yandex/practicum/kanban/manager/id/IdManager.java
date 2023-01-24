package ru.yandex.practicum.kanban.manager.id;

public class IdManager {

    private static Long freeIdNumber = 0L;

    public static Long getId() {
        return freeIdNumber++;
    }

    public static void setStartId(Long id) {
        freeIdNumber = id;
    }

}
