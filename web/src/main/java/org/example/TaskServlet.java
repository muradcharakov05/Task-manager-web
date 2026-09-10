package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {

    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        taskService = new TaskService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Task> tasks = taskService.getAllTasks();
        renderHtml(req, resp, tasks);  // ← ПЕРЕДАЁМ req
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("create".equals(action)) {
            String title = req.getParameter("title");
            if (title != null && !title.trim().isEmpty()) {
                taskService.createTask(title.trim());
            }
            resp.sendRedirect(req.getContextPath() + "/tasks");

        } else if ("complete".equals(action)) {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                try {
                    Long id = Long.parseLong(idParam);
                    Task task = taskService.findTaskById(id);
                    if (task != null) {
                        taskService.completeTask(task);
                    }
                } catch (NumberFormatException ignored) {}
            }
            resp.sendRedirect(req.getContextPath() + "/tasks");
        }
    }

    // ИСПРАВЛЕНО: добавляем параметр HttpServletRequest req
    private void renderHtml(HttpServletRequest req, HttpServletResponse resp, List<Task> tasks) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Task Manager</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; max-width: 800px; margin: 0 auto; padding: 20px; }");
        out.println(".task { border: 1px solid #ccc; margin: 5px 0; padding: 10px; border-radius: 4px; }");
        out.println(".task.DONE { background: #e8f5e9; border-color: #4caf50; }");
        out.println(".task.TODO { background: #fff3e0; border-color: #ff9800; }");
        out.println(".task.IN_PROGRESS { background: #e3f2fd; border-color: #2196f3; }");
        out.println(".add-form { margin: 20px 0; padding: 15px; background: #f5f5f5; border-radius: 4px; }");
        out.println("button { margin-left: 10px; padding: 8px 16px; cursor: pointer; }");
        out.println("input[type='text'] { padding: 8px; width: 300px; }");
        out.println(".status-badge { display: inline-block; padding: 2px 10px; border-radius: 12px; font-size: 12px; color: white; }");
        out.println(".status-badge.TODO { background: #ff9800; }");
        out.println(".status-badge.IN_PROGRESS { background: #2196f3; }");
        out.println(".status-badge.DONE { background: #4caf50; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>📋 Task Manager</h1>");

        out.println("<div class='add-form'>");
        out.println("<form action='" + req.getContextPath() + "/tasks' method='post'>");
        out.println("<input type='hidden' name='action' value='create'>");
        out.println("<input type='text' name='title' placeholder='Название задачи' required>");
        out.println("<button type='submit'>➕ Создать</button>");
        out.println("</form>");
        out.println("</div>");

        out.println("<h2>Все задачи</h2>");

        if (tasks.isEmpty()) {
            out.println("<p style='color:#888;'>Нет задач. Создайте первую!</p>");
        } else {
            for (Task task : tasks) {
                out.println("<div class='task " + task.getStatus() + "'>");
                out.println("<strong>#" + task.getId() + "</strong> ");
                out.println(task.getTitle());
                out.println("<span style='float:right;'>");
                out.println("<span class='status-badge " + task.getStatus() + "'>" + task.getStatus() + "</span>");
                if (task.getStatus() != Status.DONE) {
                    out.println("<form action='" + req.getContextPath() + "/tasks' method='post' style='display:inline; margin-left: 10px;'>");
                    out.println("<input type='hidden' name='action' value='complete'>");
                    out.println("<input type='hidden' name='id' value='" + task.getId() + "'>");
                    out.println("<button type='submit'>✅ Завершить</button>");
                    out.println("</form>");
                }
                out.println("</span>");
                out.println("</div>");
            }
        }

        out.println("</body>");
        out.println("</html>");
    }
}