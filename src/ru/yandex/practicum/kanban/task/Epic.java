package ru.yandex.practicum.kanban.task;

import ru.yandex.practicum.kanban.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {

    private transient Map<Long, SubTask> subTasks = new HashMap<>();
    private LocalDateTime endTime;

    public Epic() {
        update();
    }

    public Epic(Long id) {
        setId(id);
        update();
    }

    public void clear() {
        subTasks.clear();
        update();
    }

    public void remove(Long id) {
        subTasks.remove(id);
        update();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        update();
    }

    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    private void update() {
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
            if (isNewTasks && isDoneTasks || !isNewTasks && !isDoneTasks) {
                this.setStatus(Status.IN_PROGRESS);
            } else if (isNewTasks) {
                this.setStatus(Status.NEW);
            } else {
                this.setStatus(Status.DONE);
            }
            calculateDurationAndStartEndTimes();
        } else {
            this.setStatus(Status.NEW);
        }
    }

    private void calculateDurationAndStartEndTimes() {
        var sumDuration = Duration.ZERO;
        var mostStartTime = LocalDateTime.MAX;
        var mostEndTime = LocalDateTime.MIN;
        for (SubTask subTask : subTasks.values()) {
            var subTaskDuration = subTask.getDuration();
            if (subTaskDuration.isPresent()) {
                sumDuration = sumDuration.plus(subTaskDuration.get());
                setDuration(sumDuration);
            }
            var subTaskStartTime = subTask.getStartTime();
            if (subTaskStartTime.isPresent() && subTaskStartTime.get().isBefore(mostStartTime)) {
                mostStartTime = subTaskStartTime.get();
                setStartTime(mostStartTime);
            }
            var subTaskEndTime = subTask.getEndTime();
            if (subTaskEndTime.isPresent() && subTaskEndTime.get().isAfter(mostEndTime)) {
                mostEndTime = subTaskEndTime.get();
                endTime = mostEndTime;
            }
        }
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    @Override
    public String toString() {
        return "Epic" + super.toString();
    }

}
