package ru.yandex.practicum.kanban.manager.history;

import ru.yandex.practicum.kanban.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node> history = new HashMap<>();
    private CustomLinkedList customLinkedList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        Node oldNode = history.put(task.getId(), customLinkedList.linkLast(task));
        if (oldNode != null) {
            customLinkedList.removeNode(oldNode);
        }
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            customLinkedList.removeNode(history.get(id));
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

}
