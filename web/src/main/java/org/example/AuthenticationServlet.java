package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class AuthenticationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login=req.getParameter("login");
        String password=req.getParameter("password");

        boolean userExists=checkUserInDatabase(login,password);

        if(userExists){
            //  Создание сессии и сохранение состояния авторизации
            HttpSession session = req.getSession();
            session.setAttribute("user",login);

            //  Перенаправление на страницу меню
            resp.sendRedirect(req.getContextPath() + "/menu.html");
        }
        else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().write("<h3>Ошибка авторизации: неверный логин или пароль</h3>");
            resp.getWriter().write("<a href='index.html'>Вернуться к форме входа</a>");
        }
    }

    private boolean checkUserInDatabase(String login, String password) {
        // Здесь вызывается бизнес-логика (например, UserService.getInstance().isValid(login, password))
        return "admin".equals(login) && "1234".equals(password);
    }
}
