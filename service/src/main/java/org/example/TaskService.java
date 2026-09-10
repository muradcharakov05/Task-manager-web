package org.example;

import org.example.Task;
import org.example.Status;

import java.util.ArrayList;
import java.util.List;

public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1;

    public Task createTask(String title) {
        Task task = new Task();
        task.setId(nextId++);
        task.setTitle(title);
        task.setStatus(Status.TODO);
        tasks.add(task);
        return task;
    }

    public void completeTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task не может быть null");
        }
        task.setStatus(Status.DONE);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Task findTaskById(Long id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}