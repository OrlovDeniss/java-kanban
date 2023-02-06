package ru.yandex.practicum.kanban.gson.adapter;

import com.google.gson.*;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.lang.reflect.Type;

public class TaskJsonAdapter<T extends Task> implements JsonDeserializer<T>, JsonSerializer<T> {

    Gson gson = new Gson();

    @Override
    public JsonElement serialize(T task, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonElement jsonElement = gson.toJsonTree(task);

        jsonElement.getAsJsonObject().addProperty("type", task.getClass().getSimpleName());

        return jsonElement;
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        var typeTask = jsonElement.getAsJsonObject().get("type").getAsString();

        switch (typeTask) {

            case "Task":

                return (T) gson.fromJson(jsonElement, Task.class);

            case "Epic":

                return (T) gson.fromJson(jsonElement, Epic.class);

            case "SubTask":

                return (T) gson.fromJson(jsonElement, SubTask.class);

            default:

                throw new JsonParseException("Несовместивый тип задачи");

        }
    }

}
