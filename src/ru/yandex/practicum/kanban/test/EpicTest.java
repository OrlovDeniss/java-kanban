package ru.yandex.practicum.kanban.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.Status;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private static final String STATUS_ERROR = "Статус не совпадает";
    private Epic epic;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic();
    }

    @Test
    public void shouldBeStatusNewWhenNoSubTasks() {

        assertTrue(epic.getAllSubTasks().isEmpty(), "Неверное количество подзадач");

        assertEquals(Status.NEW, epic.getStatus(), STATUS_ERROR);

    }

    @Test
    public void shouldBeStatusNewWhenAllSubTasksStatusNew() {

        new SubTask(epic);

        assertEquals(Status.NEW, epic.getStatus(), STATUS_ERROR);

    }

    @Test
    public void shouldBeStatusDoneWhenAllSubtaskStatusDone() {

        var doneSubTask = new SubTask(epic);

        doneSubTask.setStatus(Status.DONE);

        epic.addSubTask(doneSubTask);

        assertEquals(Status.DONE, epic.getStatus(), STATUS_ERROR);

    }

    @Test
    public void shouldBeStatusInProgressWhenSubTasksStatusNewAndDone() {

        var newSubTask = new SubTask(epic);
        var doneSubTask = new SubTask(epic);

        newSubTask.setStatus(Status.NEW);
        doneSubTask.setStatus(Status.DONE);

        epic.addSubTask(newSubTask);
        epic.addSubTask(doneSubTask);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), STATUS_ERROR);
    }

    @Test
    public void shouldBeStatusInProgressWhenAllSubTasksInProgress() {

        var inProgressSubTask = new SubTask(epic);

        inProgressSubTask.setStatus(Status.IN_PROGRESS);

        epic.addSubTask(inProgressSubTask);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), STATUS_ERROR);
    }

    @Test
    public void shouldBeStartTimeEqualEarlySubTask() {

        var earlyDateTime = LocalDateTime.of(2023,1,1,1,0);

        var subTask1 = new SubTask(epic);
        var subTask2 = new SubTask(epic);

        subTask1.setStartTime(earlyDateTime);
        subTask2.setStartTime(earlyDateTime.plusDays(1));

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertEquals(earlyDateTime, epic.getStartTime().get(), "Время старта не совпадает.");

    }

    @Test void shouldBeStartTimeNullWhenSubTaskStartTimeNull() {

        var subTask1 = new SubTask(epic);
        var subTask2 = new SubTask(epic);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertTrue(subTask1.getStartTime().isEmpty());
        assertTrue(subTask2.getStartTime().isEmpty());

        assertTrue(epic.getStartTime().isEmpty(), "Время старта должно быть пустым.");

    }

    @Test
    public void shouldBeDurationEqualSumOfSubTasksDuration() {

        var subTask1 = new SubTask(epic);
        var subTask2 = new SubTask(epic);

        subTask1.setDuration(Duration.ofMinutes(15));
        subTask2.setDuration(Duration.ofMinutes(15));

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertEquals(Duration.ofMinutes(30), epic.getDuration().get(), "Время окончания не совпадает.");

    }

    @Test
    public void shouldBeDurationNullWhenSubTaskDurationNull() {

        var subTask1 = new SubTask(epic);
        var subTask2 = new SubTask(epic);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertTrue(subTask1.getDuration().isEmpty());
        assertTrue(subTask2.getDuration().isEmpty());

        assertTrue(epic.getDuration().isEmpty());

    }

    @Test
    public void shouldBeEndTimeEqualLateSubTaskEndTime() {

        var earlyDateTime = LocalDateTime.of(2023,1,1,1,0);

        var subTask1 = new SubTask(epic);
        var subTask2 = new SubTask(epic);

        subTask1.setStartTime(earlyDateTime);
        subTask2.setStartTime(earlyDateTime.plusDays(1));

        subTask1.setDuration(Duration.ofMinutes(15));
        subTask2.setDuration(Duration.ofMinutes(15));

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertEquals(subTask2.getEndTime().get(), epic.getEndTime().get());

    }

    @Test void shouldBeEndTimeNullWhenSubTaskEndTimeNull() {

        var subTask1 = new SubTask(epic);
        var subTask2 = new SubTask(epic);

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertTrue(subTask1.getEndTime().isEmpty());
        assertTrue(subTask2.getEndTime().isEmpty());

        assertTrue(epic.getEndTime().isEmpty());

    }

}