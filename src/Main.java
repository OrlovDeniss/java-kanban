import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task();
        Task task2 = new Task();

        Epic epic1 = new Epic();
        SubTask subTask1 = new SubTask(epic1);
        SubTask subTask2 = new SubTask(epic1);
        SubTask subTask3 = new SubTask(epic1);

        Epic epic2 = new Epic();

        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(epic1);
        taskManager.add(subTask1);
        taskManager.add(subTask2);
        taskManager.add(subTask3);
        taskManager.add(epic2);

        taskManager.getTask(0);
        printHistory(taskManager);

        taskManager.getEpic(2);
        printHistory(taskManager);

        taskManager.getSubTask(3);
        printHistory(taskManager);

        taskManager.getTask(1);
        printHistory(taskManager);

        taskManager.getSubTask(4);
        printHistory(taskManager);

        taskManager.getEpic(6);
        printHistory(taskManager);

        taskManager.getSubTask(3);
        printHistory(taskManager);

        taskManager.getSubTask(5);
        printHistory(taskManager);

        taskManager.getSubTask(3);
        printHistory(taskManager);

        taskManager.removeTask(0);
        printHistory(taskManager);

        taskManager.removeEpic(2);
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