package ru.yandex.practicum.kanban.task;

import java.util.Objects;

public class SubTask extends Task {
    private Epic superEpic;

    public SubTask(Epic superEpic) {
        this.superEpic = superEpic;
        superEpic.addSubTask(this);
    }

    public Epic getSuperEpic() {
        return superEpic;
    }

    @Override
    public String toString() {
        return "Sub" + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(superEpic, subTask.superEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), superEpic);
    }
}