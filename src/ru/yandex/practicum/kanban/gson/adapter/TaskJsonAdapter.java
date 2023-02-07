package ru.yandex.practicum.kanban.gson.adapter;

import com.google.gson.*;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;
import ru.yandex.practicum.kanban.task.TypeTask;

import java.lang.reflect.Type;

public class TaskJsonAdapter<T extends Task> implements JsonDeserializer<T>, JsonSerializer<T> {

    Gson gson = new Gson();

    @Override
    public JsonElement serialize(T task, Type type, JsonSerializationContext jSContext) {
        JsonElement je = gson.toJsonTree(task);
        je.getAsJsonObject().addProperty("type", task.getClass().getSimpleName());
        return je;
    }

    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jDContext) throws JsonParseException {
        TypeTask typeTask = TypeTask.valueOf(je.getAsJsonObject().get("type").getAsString().toUpperCase());
        switch (typeTask) {
            case TASK:
                return (T) gson.fromJson(je, Task.class);
            case EPIC:
                return (T) gson.fromJson(je, Epic.class);
            case SUBTASK:
                return (T) gson.fromJson(je, SubTask.class);
            default:
                throw new JsonParseException("Несовместимый тип задачи");
        }
    }

}
