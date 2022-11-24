import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Epic epic1 = new Epic();
        SubTask subTask1epic1 = new SubTask(epic1);
        SubTask subTask2epic1 = new SubTask(epic1);

        Epic epic2 = new Epic();
        SubTask subTask1epic2 = new SubTask(epic2);

        taskManager.add(epic1);
        taskManager.add(subTask1epic1);
        taskManager.add(subTask2epic1);
        taskManager.add(epic2);
        taskManager.add(subTask1epic2);

        taskManager.getEpic(3);
        taskManager.getEpic(0);

        System.out.println("History data: " + taskManager.getHistory());
        System.out.println("History size: " + taskManager.getHistory().size());
        System.out.println();

        taskManager.getEpic(0);
        taskManager.getSubTask(4);
        taskManager.getEpic(3);
        taskManager.getEpic(0);
        taskManager.getSubTask(2);
        taskManager.getEpic(0);
        taskManager.getEpic(3);
        taskManager.getSubTask(1);
        taskManager.getEpic(0);
        taskManager.getEpic(3);
        taskManager.getEpic(3);
        taskManager.getSubTask(2);
        taskManager.getEpic(0);
        taskManager.getEpic(3);
        taskManager.getSubTask(1);
        taskManager.getEpic(0);

        System.out.println("History data: " + taskManager.getHistory());
        System.out.println("History size: " + taskManager.getHistory().size());
    }
}
