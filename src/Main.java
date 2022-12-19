import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Epic epic1 = new Epic();
        SubTask subTask1 = new SubTask(epic1);
        SubTask subTask2 = new SubTask(epic1);
        SubTask subTask3 = new SubTask(epic1);
        Epic epic2 = new Epic();

        taskManager.add(epic1);
        taskManager.add(subTask1);
        taskManager.add(subTask2);
        taskManager.add(subTask3);
        taskManager.add(epic2);

        taskManager.getEpic(0);
        printHistory(taskManager);

        taskManager.getEpic(4);
        taskManager.getSubTask(3);
        printHistory(taskManager);

        taskManager.getSubTask(2);
        taskManager.getEpic(4);
        taskManager.getSubTask(3);
        taskManager.getSubTask(1);
        printHistory(taskManager);

        taskManager.removeSubTask(2);
        printHistory(taskManager);

        taskManager.removeEpic(0);
        printHistory(taskManager);

    }

    private static void printHistory(TaskManager taskManager) {
        System.out.println();
        System.out.println("History data:");
        for (Task t : taskManager.getHistory()) {
            System.out.println(t);
        }
        System.out.println("History size: " + taskManager.getHistory().size());
        System.out.println();
    }
}
