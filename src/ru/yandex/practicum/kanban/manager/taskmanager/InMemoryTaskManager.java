package ru.yandex.practicum.kanban.manager.taskmanager;

import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final int CURRENT_YEAR = LocalDateTime.now().getYear();
    private final int TIME_INTERVAL = 15;
    private Map<Long, Task> tasks = new HashMap<>();
    private Map<Long, SubTask> subTasks = new HashMap<>();
    private Map<Long, Epic> epicTasks = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();

    private final Comparator<Task> priority = (t1, t2) -> t1.getStartTime().isPresent() && t2.getStartTime().isPresent() ?
            (t1.getStartTime().get().isAfter(t2.getStartTime().get()) ? 1 : -1) : 1;

    private Set<Task> prioritizedTasks = new TreeSet<>(priority);

    private Map<LocalDateTime, Boolean> timeGrid = new HashMap<>();


    public InMemoryTaskManager() {
        initTimeGrid();
    }

    private void initTimeGrid() {

        var timeInterval = LocalDateTime.of(CURRENT_YEAR, 1, 1, 0, 0);

        while (timeInterval.isBefore(LocalDateTime.of(CURRENT_YEAR + 1, 1, 1, 0, 0))) {

            timeGrid.put(timeInterval, false);

            timeInterval = timeInterval.plusMinutes(TIME_INTERVAL);

        }
    }

    public boolean isNotCrossOnTimeGrid(Task task) throws IllegalArgumentException {

        var taskStartTime = task.getStartTime();
        var taskDuration = task.getDuration();
        var isNotCross = true;

        if (taskStartTime.isPresent() && taskDuration.isPresent()) {

            var startTime = taskStartTime.get();
            var duration = taskDuration.get();

            isNotCross = tryFillTimeGrid(startTime, duration);

        }

        return isNotCross;
    }

    private boolean tryFillTimeGrid(LocalDateTime startTime, Duration duration) throws IllegalArgumentException {

        if (startTime.getMinute() % TIME_INTERVAL != 0) {
            throw new IllegalArgumentException("Начальное время задачи должно быть кратно " + TIME_INTERVAL + " мин.");
        }

        if (duration.toMinutes() % TIME_INTERVAL != 0) {
            throw new IllegalArgumentException("Длительность задачи должна быть кратна " + TIME_INTERVAL + " мин.");
        }

        var checkTime = startTime;
        var lastTime = startTime.plusMinutes(duration.toMinutes());
        var backupForRollback = new ArrayList<LocalDateTime>();

        while (checkTime.isBefore(lastTime) || checkTime.isEqual(lastTime)) {

            var isFreeTime = !timeGrid.get(checkTime);

            if (isFreeTime) {

                timeGrid.put(checkTime, true);
                backupForRollback.add(checkTime);

            } else {

                for (LocalDateTime time : backupForRollback) {
                    timeGrid.put(time, false);
                }

                return false;

            }

            checkTime = checkTime.plusMinutes(TIME_INTERVAL);

        }

        return true;
    }

    private void removeTaskOnTimeGrid(Task task) {

        var taskStartTime = task.getStartTime();
        var taskDuration = task.getDuration();

        if (taskStartTime.isPresent() && taskDuration.isPresent()) {

            var time = taskStartTime.get();
            var duration = taskDuration.get();
            var lastTime = time.plusMinutes(duration.toMinutes());

            while (time.isBefore(lastTime) || time.isEqual(lastTime)) {

                timeGrid.put(time, false);

                time = time.plusMinutes(TIME_INTERVAL);

            }
        }
    }

    protected Map<Long, Task> getInMemoryTasks() {
        return tasks;
    }

    protected Map<Long, SubTask> getInMemorySubTasks() {
        return subTasks;
    }

    protected Map<Long, Epic> getInMemoryEpicTasks() {
        return epicTasks;
    }

    protected HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void clearTasks() {
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.clear();
    }

    @Override
    public void clearSubTasks() {
        subTasks.values().forEach(prioritizedTasks::remove);
        subTasks.clear();
        for (Epic e : epicTasks.values()) {
            e.clear();
        }
    }

    @Override
    public void clearEpicTasks() {
        epicTasks.values().forEach(prioritizedTasks::remove);
        clearSubTasks();
        epicTasks.clear();
    }

    @Override
    public void removeTask(Long id) {

        var removableTask = tasks.get(id);

        prioritizedTasks.remove(removableTask);

        removeTaskOnTimeGrid(removableTask);

        tasks.remove(id);
        historyManager.remove(id);

    }

    @Override
    public void removeSubTask(Long id) {

        var removableTask = subTasks.get(id);

        prioritizedTasks.remove(removableTask);

        removeTaskOnTimeGrid(removableTask);

        subTasks.get(id).getSuperEpic().remove(id);
        subTasks.remove(id);
        historyManager.remove(id);

    }

    @Override
    public void removeEpic(Long id) {

        var removableEpic = epicTasks.get(id);

        prioritizedTasks.remove(removableEpic);

        removeTaskOnTimeGrid(removableEpic);

        for (SubTask inEpicSubTask : removableEpic.getAllSubTasks()) {

            prioritizedTasks.remove(inEpicSubTask);

            removeTaskOnTimeGrid(inEpicSubTask);

            removeSubTask(inEpicSubTask.getId());

        }

        epicTasks.remove(id);
        historyManager.remove(id);

    }

    @Override
    public Task getTask(Long id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public SubTask getSubTask(Long id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(Long id) {
        historyManager.add(epicTasks.get(id));
        return epicTasks.get(id);
    }

    @Override
    public List<SubTask> getEpicSubTasks(Epic epic) {
        return epic.getAllSubTasks();
    }

    @Override
    public void add(Task task) throws IllegalArgumentException {

        if (isNotCrossOnTimeGrid(task)) {

            tasks.put(task.getId(), task);

            prioritizedTasks.add(task);

        }

    }

    @Override
    public void add(SubTask subTask) throws IllegalArgumentException {

        if (isNotCrossOnTimeGrid(subTask)) {

            subTasks.put(subTask.getId(), subTask);

            subTask.getSuperEpic().addSubTask(subTask);

            prioritizedTasks.add(subTask);

        }

    }

    @Override
    public void add(Epic epic) {

        epicTasks.put(epic.getId(), epic);

        prioritizedTasks.add(epic);
    }

    @Override
    public void update(Task task) throws IllegalArgumentException {

        if (tasks.containsKey(task.getId())) {

            var oldTask = tasks.get(task.getId());

            removeTaskOnTimeGrid(oldTask);

            if (isNotCrossOnTimeGrid(task)) {

                tasks.put(task.getId(), task);

                prioritizedTasks.add(task);

            }
        }
    }

    @Override
    public void update(SubTask subTask) throws IllegalArgumentException {

        if (subTasks.containsKey(subTask.getId())) {

            var oldSubTask = subTasks.get(subTask.getId());

            removeTaskOnTimeGrid(oldSubTask);

            if (isNotCrossOnTimeGrid(subTask)) {

                subTasks.put(subTask.getId(), subTask);

                subTask.getSuperEpic().addSubTask(subTask);

                prioritizedTasks.add(subTask);

            }
        }
    }

    @Override
    public void update(Epic epic) throws IllegalArgumentException {

        if (epicTasks.containsKey(epic.getId())) {

            var oldEpic = epicTasks.get(epic.getId());

            removeTaskOnTimeGrid(oldEpic);

            epicTasks.put(epic.getId(), epic);

            prioritizedTasks.add(epic);

        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getAllEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "taskMap=" + tasks +
                ", subTaskMap=" + subTasks +
                ", epicTaskMap=" + epicTasks +
                '}';
    }
}
