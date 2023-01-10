package ru.yandex.practicum.kanban.manager.taskmanager;

public class ManagerSaveException extends RuntimeException {

    ManagerSaveException(final Throwable cause){
        super(cause);
    }

}
