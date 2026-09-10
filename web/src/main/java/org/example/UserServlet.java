package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session=req.getSession(false);

        if(session==null || session.getAttribute("user")==null){
            resp.sendRedirect(req.getContextPath()+"/index.html");
        }

        // Получение данных пользователя из сессии (или из БД)
        String login = (String) session.getAttribute("user");
        String name = (String) session.getAttribute("name");
        String age = (String) session.getAttribute("age");
        String email = (String) session.getAttribute("email");
        String password = (String) session.getAttribute("password");

        // Установка дефолтных значений для наглядности
        if (name == null) name = "Иван";
        if (age == null) age = "25";
        if (email == null) email = "user@example.com";
        if (password == null) password = "123";

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'><title>Личный кабинет</title></head><body>");
        out.println("<h2>Личный кабинет</h2>");

        // Форма с выводом полей и возможностью изменения
        out.println("<form action='/user' method='POST'>");
        out.println("<p><b>Логин:</b> " + login + " (нельзя изменить)</p>");
        out.println("<p><label>Имя: <input type='text' name='name' value='" + name + "'></label></p>");
        out.println("<p><label>Возраст: <input type='number' name='age' value='" + age + "'></label></p>");
        out.println("<p><label>Email: <input type='email' name='email' value='" + email + "'></label></p>");
        out.println("<p><label>Пароль: <input type='text' name='password' value='" + password + "'></label></p>");
        out.println("<button type='submit'>Сохранить изменения</button>");
        out.println("</form>");

        out.println("<br><a href='menu.html'>Вернуться в меню</a>");
        out.println("</body></html>");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }

        // Вычитывание обновленных данных из формы
        String updatedName = req.getParameter("name");
        String updatedAge = req.getParameter("age");
        String updatedEmail = req.getParameter("email");
        String updatedPassword = req.getParameter("password");

        // Сохранение обновленных полей в сессию (или обновление в БД через UserService)
        session.setAttribute("name", updatedName);
        session.setAttribute("age", updatedAge);
        session.setAttribute("email", updatedEmail);
        session.setAttribute("password", updatedPassword);

        // Перенаправление на этот же сервлет для отображения обновленных данных
        resp.sendRedirect(req.getContextPath() + "/user");
    }
}
