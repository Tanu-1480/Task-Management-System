package task.management.system;


import java.io.*;
import java.util.*;

class Task {
    private int taskId;
    private String description;
    private String employeeName;
    private String deadline;
    private String priority;
    private boolean isCompleted;

    public Task(int taskId, String description, String employeeName, String deadline, String priority) {
        this.taskId = taskId;
        this.description = description;
        this.employeeName = employeeName;
        this.deadline = deadline;
        this.priority = priority;
        this.isCompleted = false;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markCompleted() {
        this.isCompleted = true;
    }

    @Override
    public String toString() {
        return "Task ID: " + taskId +
                ", Description: " + description +
                ", Assigned To: " + employeeName +
                ", Deadline: " + deadline +
                ", Priority: " + priority +
                ", Status: " + (isCompleted ? "Completed" : "Pending");
    }
}

public class TaskManagementSystem {
    private List<Task> tasks = new ArrayList<>();
    private static final String FILE_NAME = "tasks.txt";

    public void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int taskId = Integer.parseInt(parts[0]);
                String description = parts[1];
                String employeeName = parts[2];
                String deadline = parts[3];
                String priority = parts[4];
                boolean isCompleted = Boolean.parseBoolean(parts[5]);

                Task task = new Task(taskId, description, employeeName, deadline, priority);
                if (isCompleted) {
                    task.markCompleted();
                }
                tasks.add(task);
            }
        } catch (IOException e) {
            System.out.println("No previous task records found. Starting fresh.");
        }
    }

    public void saveTasksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.println(task.getTaskId() + "," +
                        task.getDescription() + "," +
                        task.getEmployeeName() + "," +
                        task.getDeadline() + "," +
                        task.getPriority() + "," +
                        task.isCompleted());
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks to file.");
        }
    }

    public void addTask(String description, String employeeName, String deadline, String priority) {
        int taskId = tasks.size() + 1;
        Task newTask = new Task(taskId, description, employeeName, deadline, priority);
        tasks.add(newTask);
        System.out.println("Task added successfully: " + newTask);
    }

    public void markTaskCompleted(int taskId) {
        for (Task task : tasks) {
            if (task.getTaskId() == taskId) {
                task.markCompleted();
                System.out.println("Task marked as completed: " + task);
                return;
            }
        }
        System.out.println("Task with ID " + taskId + " not found.");
    }

    public void displayAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            System.out.println("All Tasks:");
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    public void generatePerformanceReport() {
        System.out.println("Performance Report:");
        long completedCount = tasks.stream().filter(Task::isCompleted).count();
        System.out.println("Total Tasks: " + tasks.size());
        System.out.println("Completed Tasks: " + completedCount);
        System.out.println("Pending Tasks: " + (tasks.size() - completedCount));
    }

    public static void main(String[] args) {
        TaskManagementSystem system = new TaskManagementSystem();
        system.loadTasksFromFile();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nTask Management System");
            System.out.println("1. Add Task");
            System.out.println("2. Mark Task as Completed");
            System.out.println("3. Display All Tasks");
            System.out.println("4. Generate Performance Report");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter employee name: ");
                    String employeeName = scanner.nextLine();
                    System.out.print("Enter deadline (YYYY-MM-DD): ");
                    String deadline = scanner.nextLine();
                    System.out.print("Enter priority (High/Medium/Low): ");
                    String priority = scanner.nextLine();
                    system.addTask(description, employeeName, deadline, priority);
                    break;
                case 2:
                    System.out.print("Enter task ID to mark as completed: ");
                    int taskId = scanner.nextInt();
                    system.markTaskCompleted(taskId);
                    break;
                case 3:
                    system.displayAllTasks();
                    break;
                case 4:
                    system.generatePerformanceReport();
                    break;
                case 5:
                    system.saveTasksToFile();
                    System.out.println("Exiting. Tasks saved.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
      