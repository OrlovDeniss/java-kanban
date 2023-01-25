package ru.yandex.practicum.kanban.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.Status;
import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.manager.history.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    private static final String TASK_NOT_FOUND = "Задача не найдена.";
    private static final String WRONG_NUMBER_OF_TASKS = "Неверное количество задач.";
    private static final String TASKS_DO_NOT_MATCH = "Задачи не совпадают.";
    private static final String TASKS_ARE_NOT_RETURNED = "Задачи не возвращаются.";

    private final T taskManager;
    private final HistoryManager historyManager;

    private Task task;
    private Epic epic;
    private SubTask subTask;

    TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
        this.historyManager = new InMemoryHistoryManager();
    }

    @BeforeEach
    public void beforeEach() {
        task = new Task();
        epic = new Epic();
        subTask = new SubTask(epic);
    }

    @Test
    public void clearTasks() {

        taskManager.add(task);

        assertEquals(task, taskManager.getTask(task.getId()), TASK_NOT_FOUND);

        taskManager.clearTasks();

        assertEquals(0, taskManager.getAllTasks().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void clearSubTasks() {

        taskManager.add(subTask);

        assertEquals(subTask, taskManager.getSubTask(subTask.getId()), TASK_NOT_FOUND);

        taskManager.clearSubTasks();

        assertEquals(0, taskManager.getAllSubTasks().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void clearEpicTasks() {

        taskManager.add(epic);
        taskManager.add(subTask);

        assertEquals(epic, taskManager.getEpic(epic.getId()), TASK_NOT_FOUND);
        assertEquals(subTask, taskManager.getSubTask(subTask.getId()), TASK_NOT_FOUND);

        taskManager.clearEpicTasks();

        assertEquals(0, taskManager.getAllEpicTasks().size(), WRONG_NUMBER_OF_TASKS);
        assertEquals(0, taskManager.getAllSubTasks().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void removeTask() {

        taskManager.add(task);

        assertEquals(task, taskManager.getTask(task.getId()), TASK_NOT_FOUND);
        assertEquals(1, taskManager.getAllTasks().size(), WRONG_NUMBER_OF_TASKS);

        taskManager.removeTask(task.getId());

        assertEquals(0, taskManager.getAllTasks().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void removeTaskWithNotExistId() {

        assertEquals(0, taskManager.getAllTasks().size(), WRONG_NUMBER_OF_TASKS);

        assertThrows(NullPointerException.class,
                () -> taskManager.removeTask(1L));

    }

    @Test
    public void removeSubTask() {

        taskManager.add(epic);
        taskManager.add(subTask);

        assertEquals(epic, taskManager.getEpic(epic.getId()), TASK_NOT_FOUND);
        assertEquals(subTask, taskManager.getSubTask(subTask.getId()), TASK_NOT_FOUND);

        assertEquals(epic, taskManager.getSubTask(subTask.getId()).getSuperEpic(), TASKS_DO_NOT_MATCH);

        assertEquals(1, taskManager.getAllEpicTasks().size(), WRONG_NUMBER_OF_TASKS);
        assertEquals(1, taskManager.getAllSubTasks().size(), WRONG_NUMBER_OF_TASKS);

        taskManager.removeSubTask(subTask.getId());

        assertEquals(1, taskManager.getAllEpicTasks().size(), WRONG_NUMBER_OF_TASKS);
        assertEquals(0, taskManager.getAllSubTasks().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void removeSubTaskWithNotExistId() {

        assertThrows(NullPointerException.class,
                () -> taskManager.removeSubTask(5L));

    }

    @Test
    public void removeEpic() {

        taskManager.add(epic);
        taskManager.add(subTask);

        assertEquals(epic, taskManager.getEpic(epic.getId()), TASK_NOT_FOUND);
        assertEquals(subTask, taskManager.getSubTask(subTask.getId()), TASK_NOT_FOUND);

        assertEquals(epic, taskManager.getSubTask(subTask.getId()).getSuperEpic(), TASKS_DO_NOT_MATCH);

        assertEquals(1, taskManager.getAllEpicTasks().size(), WRONG_NUMBER_OF_TASKS);
        assertEquals(1, taskManager.getAllSubTasks().size(), WRONG_NUMBER_OF_TASKS);

        taskManager.removeEpic(epic.getId());

        assertEquals(0, taskManager.getAllEpicTasks().size(), WRONG_NUMBER_OF_TASKS);
        assertEquals(0, taskManager.getAllSubTasks().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void removeEpicWithNotExistId() {

        assertThrows(NullPointerException.class,
                () -> taskManager.removeEpic(0L));

    }

    @Test
    public void getTask() {

        taskManager.add(task);

        assertEquals(task, taskManager.getTask(task.getId()), TASKS_DO_NOT_MATCH);

    }

    @Test
    public void getNotExistTask() {

        assertThrows(NullPointerException.class,
                () -> taskManager.getTask(0L));

    }

    @Test
    public void getSubTask() {

        taskManager.add(epic);
        taskManager.add(subTask);

        assertEquals(subTask, taskManager.getEpic(epic.getId()).getAllSubTasks().get(0), TASKS_DO_NOT_MATCH);

        assertEquals(epic, taskManager.getSubTask(subTask.getId()).getSuperEpic(), TASKS_DO_NOT_MATCH);

        assertEquals(subTask, taskManager.getSubTask(subTask.getId()), TASKS_DO_NOT_MATCH);

    }

    @Test
    public void getNotExistSubTask() {

        assertThrows(NullPointerException.class,
                () -> taskManager.getSubTask(0L));

    }

    @Test
    public void getEpic() {

        taskManager.add(epic);

        assertEquals(epic, taskManager.getEpic(epic.getId()), TASKS_DO_NOT_MATCH);

    }

    @Test
    public void getNotExistEpic() {

        assertThrows(NullPointerException.class,
                () -> taskManager.getEpic(0L));

    }

    @Test
    public void getEpicSubTasks() {

        taskManager.add(epic);
        taskManager.add(subTask);

        final SubTask[] subTasksTestArray = {subTask};

        assertArrayEquals(subTasksTestArray, taskManager.getEpicSubTasks(epic).toArray(), TASKS_DO_NOT_MATCH);

    }

    @Test
    public void getNotExistEpicSubtasks() {

        var emptyEpic = new Epic();

        taskManager.add(emptyEpic);

        assertEquals(0, taskManager.getEpicSubTasks(emptyEpic).size());

    }

    @Test
    public void addNewTask() {

        taskManager.add(task);

        assertNotNull(taskManager.getTask(task.getId()), TASK_NOT_FOUND);

        assertEquals(task, taskManager.getTask(task.getId()), TASKS_DO_NOT_MATCH);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, TASKS_ARE_NOT_RETURNED);

        assertEquals(1, tasks.size(), WRONG_NUMBER_OF_TASKS);

        assertEquals(task, tasks.get(0), TASKS_DO_NOT_MATCH);

    }

    @Test
    public void addNewSubTask() {

        taskManager.add(epic);
        taskManager.add(subTask);

        assertNotNull(taskManager.getSubTask(subTask.getId()), TASK_NOT_FOUND);

        assertEquals(subTask, taskManager.getSubTask(subTask.getId()), TASKS_DO_NOT_MATCH);

        final List<SubTask> subTasks = taskManager.getAllSubTasks();

        assertNotNull(subTasks, TASKS_ARE_NOT_RETURNED);

        assertEquals(1, subTasks.size(), WRONG_NUMBER_OF_TASKS);

        assertEquals(subTask, subTasks.get(0), TASKS_DO_NOT_MATCH);
    }

    @Test
    public void addNewEpicTask() {

        taskManager.add(epic);

        assertNotNull(taskManager.getEpic(epic.getId()), TASK_NOT_FOUND);

        assertEquals(epic, taskManager.getEpic(epic.getId()), TASKS_DO_NOT_MATCH);

        final List<Epic> epicTasks = taskManager.getAllEpicTasks();

        assertNotNull(epicTasks, TASKS_ARE_NOT_RETURNED);

        assertEquals(1, epicTasks.size(), WRONG_NUMBER_OF_TASKS);

        assertEquals(epic, epicTasks.get(0), TASKS_DO_NOT_MATCH);

    }

    @Test
    public void updateTask() {

        taskManager.add(task);

        assertEquals(task, taskManager.getTask(task.getId()), TASKS_DO_NOT_MATCH);

        var taskForUpdate = new Task();

        taskForUpdate.setId(task.getId());

        taskManager.update(taskForUpdate);

        assertEquals(taskForUpdate, taskManager.getTask(task.getId()), TASKS_DO_NOT_MATCH);

        assertEquals(1, taskManager.getAllTasks().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void shouldIgnoreWhenUpdateTaskWithWrongId() {

        taskManager.add(task);

        assertEquals(1, taskManager.getAllTasks().size(), TASKS_DO_NOT_MATCH);
        assertEquals(task, taskManager.getAllTasks().get(0));

        var taskWithWrongId = new Task();

        taskWithWrongId.setId(task.getId()+1);

        taskManager.update(taskWithWrongId);

        assertEquals(1, taskManager.getAllTasks().size(), TASKS_DO_NOT_MATCH);
        assertEquals(task, taskManager.getAllTasks().get(0));

    }

    @Test
    public void updateSubTaskAndCheckEpicStatus() {

        taskManager.add(epic);
        taskManager.add(subTask);

        assertEquals(Status.NEW, taskManager.getEpic(epic.getId()).getStatus());

        subTask.setStatus(Status.IN_PROGRESS);

        taskManager.update(subTask);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic.getId()).getStatus());

        subTask.setStatus(Status.DONE);

        taskManager.update(subTask);

        assertEquals(Status.DONE, taskManager.getEpic(epic.getId()).getStatus());

    }

    @Test
    public void shouldIgnoreWhenUpdateSubTaskWithWrongId() {

        taskManager.add(subTask);

        assertEquals(1, taskManager.getAllSubTasks().size(), TASKS_DO_NOT_MATCH);
        assertEquals(subTask, taskManager.getAllSubTasks().get(0));

        var subTaskWithWrongId = new SubTask(epic);

        subTaskWithWrongId.setId(subTask.getId()+1);

        taskManager.update(subTaskWithWrongId);

        assertEquals(1, taskManager.getAllSubTasks().size(), TASKS_DO_NOT_MATCH);
        assertEquals(subTask, taskManager.getAllSubTasks().get(0));

    }

    @Test
    public void updateEpic() {

        taskManager.add(epic);

        assertEquals(epic, taskManager.getEpic(epic.getId()), TASKS_DO_NOT_MATCH);

        var epicForUpdate = new Epic();
        epicForUpdate.setId(epic.getId());

        taskManager.update(epicForUpdate);

        assertEquals(epicForUpdate, taskManager.getEpic(epicForUpdate.getId()), TASKS_DO_NOT_MATCH);
        assertEquals(1, taskManager.getAllEpicTasks().size());

    }

    @Test
    public void shouldIgnoreWhenUpdateEpicWithWrongId() {

        taskManager.add(epic);

        assertEquals(1, taskManager.getAllEpicTasks().size(), TASKS_DO_NOT_MATCH);
        assertEquals(epic, taskManager.getAllEpicTasks().get(0));

        var epicWithWrongId = new SubTask(epic);

        epicWithWrongId.setId(epic.getId()+1);

        taskManager.update(epicWithWrongId);

        assertEquals(1, taskManager.getAllEpicTasks().size(), TASKS_DO_NOT_MATCH);
        assertEquals(epic, taskManager.getAllEpicTasks().get(0));

    }

    @Test
    public void getAllTasks() {

        taskManager.add(task);

        final Task[] tasks = {task};

        assertArrayEquals(tasks, taskManager.getAllTasks().toArray(), TASKS_DO_NOT_MATCH);

        var task2 = new Task();

        taskManager.add(task2);

        final Task[] tasks2 = {task, task2};

        assertArrayEquals(tasks2, taskManager.getAllTasks().toArray(), TASKS_DO_NOT_MATCH);

    }

    @Test
    public void getAllSubTasks() {

        var epic1 = new Epic();
        var subTask1 = new SubTask(epic1);

        taskManager.add(epic1);
        taskManager.add(subTask1);

        final Task[] tasks = {subTask1};

        assertArrayEquals(tasks, taskManager.getAllSubTasks().toArray(), TASKS_DO_NOT_MATCH);

        var subTask2 = new SubTask(epic1);

        taskManager.add(subTask2);

        final Task[] tasks2 = {subTask1, subTask2};

        assertArrayEquals(tasks2, taskManager.getAllSubTasks().toArray(), TASKS_DO_NOT_MATCH);

    }

    @Test
    public void getAllEpicTasks() {

        taskManager.add(epic);

        final Task[] tasks = {epic};

        assertArrayEquals(tasks, taskManager.getAllEpicTasks().toArray(), TASKS_DO_NOT_MATCH);

    }

    @Test
    public void getHistory() {

        taskManager.add(task);
        taskManager.add(epic);
        taskManager.add(subTask);

        taskManager.getTask(task.getId());
        taskManager.getSubTask(subTask.getId());
        taskManager.getEpic(epic.getId());

        final Task[] trueHistoryArray = {task, subTask, epic};

        assertArrayEquals(trueHistoryArray, taskManager.getHistory().toArray(), TASKS_DO_NOT_MATCH);

    }

    @Test
    public void getPrioritizedTasks() {

        var task3 = new Task();
        task3.setStartTime(LocalDateTime.of(2023, 2, 1, 22, 0));
        taskManager.add(task3);

        task.setStartTime(LocalDateTime.of(2023, 2, 1, 10, 0));
        taskManager.add(task);

        var task4 = new Task();
        taskManager.add(task4);

        var task2 = new Task();
        task2.setStartTime(LocalDateTime.of(2023, 2, 1, 11, 0));
        taskManager.add(task2);

        var task5 = new Task();
        taskManager.add(task5);

        Task[] trueTimePriority = {task, task2, task3, task4, task5};

        assertArrayEquals(trueTimePriority, taskManager.getPrioritizedTasks().toArray());

    }

    @Test
    public void shouldNotBeAddedToManagerIfFoundCrossTime() {

        task.setStartTime(LocalDateTime.of(2023, 1, 5, 13, 30));
        task.setDuration(Duration.ofMinutes(60));

        taskManager.add(task);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(task, taskManager.getTask(task.getId()));

        var crossTask = new Task();
        crossTask.setStartTime(LocalDateTime.of(2023, 1, 5, 14, 0));
        crossTask.setDuration(Duration.ofMinutes(60));

        taskManager.add(crossTask);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(task, taskManager.getTask(task.getId()));

    }

    @Test
    public void shouldBeFreeTimeGridAfterRemoveTaskInManager() {

        task.setStartTime(LocalDateTime.of(2023, 1, 5, 13, 30));
        task.setDuration(Duration.ofMinutes(60));

        taskManager.add(task);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(task, taskManager.getTask(task.getId()));

        taskManager.removeTask(task.getId());

        var crossTask = new Task();
        crossTask.setStartTime(LocalDateTime.of(2023, 1, 5, 14, 0));
        crossTask.setDuration(Duration.ofMinutes(60));

        taskManager.add(crossTask);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(crossTask, taskManager.getTask(crossTask.getId()));

    }

    @Test
    public void shouldBeThrowWhenIncorrectStartTime() {

        task.setStartTime(LocalDateTime.of(2023, 1,1,15,33));
        task.setDuration(Duration.ofMinutes(15));

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.add(task));

        assertEquals("Начальное время задачи должно быть кратно 15 мин.", exception.getMessage());

    }

    @Test
    public void shouldBeThrowWhenIncorrectDuration() {

        task.setStartTime(LocalDateTime.of(2023, 1,1,15,30));
        task.setDuration(Duration.ofMinutes(17));

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.add(task));

        assertEquals("Длительность задачи должна быть кратна 15 мин.", exception.getMessage());

    }

}
