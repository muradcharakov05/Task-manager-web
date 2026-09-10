package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String name=req.getParameter("name");
        String ageStr=req.getParameter("age");
        String email=req.getParameter("email");
        String login=req.getParameter("login");
        String password=req.getParameter("password");

        if(isLoginExists(login)){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().write("<h3>Ошибка регистрации: пользователь с логином '" + login + "' уже существует!</h3>");
            resp.getWriter().write("<a href='registration.html'>Попробовать снова</a>");
            return;
        }

        saveUserToDatabase(name, ageStr, email, login, password);

        HttpSession session=req.getSession();
        session.setAttribute("user",login);

        resp.sendRedirect(req.getContextPath() + "/menu.html");


    }
    private boolean isLoginExists(String login) {
        // В реальном проекте здесь будет запрос к БД: userService.existsByLogin(login)
        return "admin".equalsIgnoreCase(login) || "user".equalsIgnoreCase(login);
    }

    // Вспомогательный метод записи в БД
    private void saveUserToDatabase(String name, String age, String email, String login, String password) {
        // В реальном проекте здесь будет вызов DAO/Service: userDao.save(new User(...))
        System.out.println("Пользователь " + login + " сохранен в БД.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Перенаправляем пользователя на HTML-файл формы
        req.getRequestDispatcher("/registration.html").forward(req, resp);
    }
}
