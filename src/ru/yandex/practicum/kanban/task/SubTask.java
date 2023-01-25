package ru.yandex.practicum.kanban.task;

import ru.yandex.practicum.kanban.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private Epic superEpic;

    public SubTask(Epic superEpic) {
        this.superEpic = superEpic;
        superEpic.addSubTask(this);
    }

    public SubTask(Long id, Epic superEpic) {
        this.superEpic = superEpic;
        setId(id);
        superEpic.addSubTask(this);
    }

    public Epic getSuperEpic() {
        return superEpic;
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
        superEpic.addSubTask(this);
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        superEpic.addSubTask(this);
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
        superEpic.addSubTask(this);
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
        superEpic.addSubTask(this);
    }

    @Override
    public String toString() {
        return "Sub" + super.toString();
    }

}