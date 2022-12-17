package ru.yandex.practicum.kanban.manager.history;

import ru.yandex.practicum.kanban.task.Task;

import java.util.ArrayList;
import java.util.List;

public class CustomLinkedList {

    private Node head;
    private Node tail;

    public Node linkLast(Task task) {
        Node oldLast = tail;
        Node newNode = new Node(oldLast, task, null);
        tail = newNode;
        if (oldLast == null) {
            head = newNode;
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
        if (head.equals(node)) {
            head = nextNode;
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node nodeCursor = head;
        while (nodeCursor != null) {
            tasks.add(nodeCursor.getTask());
            nodeCursor = nodeCursor.getNext();
        }
        return tasks;
    }
}
