package ru.yandex.practicum.kanban.task;

import ru.yandex.practicum.kanban.Status;

import java.util.*;

public class Epic extends Task {

    private Map<Integer, SubTask> subTasks = new HashMap<>();

    public Epic() {
        updateStatus();
    }

    public void clear() {
        subTasks.clear();
        updateStatus();
    }

    public void remove(int id) {
        subTasks.remove(id);
        updateStatus();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateStatus();
    }

    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    private void updateStatus() {
        if (!subTasks.isEmpty()) {
            boolean isNewTasks = false;
            boolean isDoneTasks = false;
            for (SubTask subTask : subTasks.values()) {
                if (subTask.getStatus() == Status.NEW) {
                    isNewTasks = true;
                } else if (subTask.getStatus() == Status.DONE) {
                    isDoneTasks = true;
                }
            }
            if (isNewTasks && isDoneTasks) {
                this.setStatus(Status.IN_PROGRESS);
            } else if (isNewTasks) {
                this.setStatus(Status.NEW);
            } else if (isDoneTasks) {
                this.setStatus(Status.DONE);
            }
        } else {
            this.setStatus(Status.NEW);
        }
    }

    @Override
    public String toString() {
        return "Epic" + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subTasks.equals(epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }
}
