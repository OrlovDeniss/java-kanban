import ru.yandex.practicum.kanban.Status;
import ru.yandex.practicum.kanban.manager.TaskManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;

//Привет, Дмитрий! Прошу проверить мою работу строго.
public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

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

        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println();

        subTask1epic1.setStatus(Status.NEW);
        subTask2epic1.setStatus(Status.DONE);
        subTask1epic2.setStatus(Status.DONE);

        taskManager.update(subTask1epic1);
        taskManager.update(subTask2epic1);
        taskManager.update(subTask1epic2);

        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println();

        taskManager.remove(1);
        taskManager.remove(3);

        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println();

    }
}
