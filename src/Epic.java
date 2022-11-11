import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Epic extends Task {

    private Map<Integer, SubTask> subTasks = new HashMap<>();;

    Epic() {
        this.setStatus(Status.NEW);
    }

    public void clear() {
        subTasks.clear();
    }

    public void remove(int id) {
        subTasks.remove(id);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updStatus();
    }

    public SubTask[] getAllSubTasks() {
        return subTasks.values().toArray(new SubTask[0]);
    }

    private void updStatus() {
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
