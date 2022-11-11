import java.util.Arrays;

//Привет, Дмитрий! Прошу проверить мою работу строго.

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Epic epic1 = new Epic();
        SubTask subTask1epic1 = new SubTask(epic1);
        SubTask subTask2epic1 = new SubTask(epic1);

        Epic epic2 = new Epic();
        SubTask subTask1epic2 = new SubTask(epic2);

        manager.add(epic1);
        manager.add(subTask1epic1);
        manager.add(subTask2epic1);
        manager.add(epic2);
        manager.add(subTask1epic2);

        System.out.println(Arrays.toString(manager.getAllEpicTasks()));
        System.out.println(Arrays.toString(manager.getAllTasks()));
        System.out.println(Arrays.toString(manager.getAllSubTasks()));
        System.out.println();

        subTask1epic1.setStatus(Status.NEW);
        subTask2epic1.setStatus(Status.DONE);
        subTask1epic2.setStatus(Status.DONE);

        manager.upd(subTask1epic1);
        manager.upd(subTask2epic1);
        manager.upd(subTask1epic2);

        System.out.println(Arrays.toString(manager.getAllEpicTasks()));
        System.out.println(Arrays.toString(manager.getAllTasks()));
        System.out.println(Arrays.toString(manager.getAllSubTasks()));
        System.out.println();

        manager.remove(1);
        manager.remove(3);

        System.out.println(Arrays.toString(manager.getAllEpicTasks()));
        System.out.println(Arrays.toString(manager.getAllTasks()));
        System.out.println(Arrays.toString(manager.getAllSubTasks()));
        System.out.println();

    }
}
