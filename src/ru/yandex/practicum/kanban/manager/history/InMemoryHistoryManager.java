package ru.yandex.practicum.kanban.manager.history;

import ru.yandex.practicum.kanban.task.Task;

import java.util.ArrayList;
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

    private class CustomLinkedList {
        private Node head;
        private Node tail;

        public Node linkLast(Task task) {
            Node oldLast = tail;
            Node newNode = new Node(oldLast, task, null);
            tail = newNode;
            if (oldLast == null) {
                head = null;
            } else {
                oldLast.setNext(newNode);
            }
            return newNode;
        }

        public void removeNode(Node node) {
            Node prevNode = node.getPrev();
            Node nextNode = node.getNext();
            if (prevNode != null) {
                prevNode.setNext(nextNode);
            }
            if (nextNode != null) {
                nextNode.setPrev(prevNode);
            }
            if (tail.equals(node)) {
                tail = prevNode;
            }
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node nodeCursor = tail;
            while (nodeCursor != null) {
                tasks.add(nodeCursor.getTask());
                nodeCursor = nodeCursor.getPrev();
            }
            return tasks;
        }
    }
}
