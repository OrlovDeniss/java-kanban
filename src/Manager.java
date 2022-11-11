import java.util.HashMap;
import java.util.Map;

public class Manager {
    public static int freeIdNumber = 0;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epicTasks = new HashMap<>();

    public void clearTasks() {
        tasks.clear();
    }

    public void clearSubTasks() {
        subTasks.clear();
        for (Epic e : epicTasks.values()) {
            e.clear();
        }
    }

    public void clearEpicTasks() {
        epicTasks.clear();
    }

    public void remove(int id) {
        try {
            subTasks.get(id).getSuperEpic().remove(id);
        } catch (NullPointerException e) {

        }
        tasks.remove(id);
        subTasks.remove(id);
        epicTasks.remove(id);
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public Epic getEpic(int id) {
        return epicTasks.get(id);
    }

    public SubTask[] getEpicSubTasks(Epic epic) {
        return epic.getAllSubTasks();
    }

    public void add(Task task) {
        tasks.put(task.getId(), task);
    }

    public void add(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }

    public void add(Epic epic) {
        epicTasks.put(epic.getId(), epic);
    }

    public void upd(Task task) {
        tasks.put(task.getId(), task);
    }

    public void upd(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        subTask.getSuperEpic().addSubTask(subTask);
    }

    public void upd(Epic epic) {
        epicTasks.put(epic.getId(), epic);
    }

    public Task[] getAllTasks() {
        return tasks.values().toArray(new Task[0]);
    }

    public SubTask[] getAllSubTasks() {
        return subTasks.values().toArray(new SubTask[0]);
    }

    public Epic[] getAllEpicTasks() {
        return epicTasks.values().toArray(new Epic[0]);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "taskMap=" + tasks +
                ", subTaskMap=" + subTasks +
                ", epicTaskMap=" + epicTasks +
                '}';
    }
}
