package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MessageServlet extends HttpServlet {
    private class Message {
        String user;
        String text;

        Message(String user, String text) {
            this.user = user;
            this.text = text;
        }
    }

    private List<Message> chat = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        HttpSession session = request.getSession();
        String username;
        response.setContentType("application/json");
        if (uri.endsWith("auth")) {
            username = request.getParameter("user");
            String json;
            if (username != null) {
                session.setAttribute("user", username);
                json = new Gson().toJson(username);
            }
            else {
                session.setAttribute("user", "");
                json = new Gson().toJson("");
            }
            response.getWriter().print(json);
            response.getWriter().flush();
        }
        if (uri.endsWith("findAll")) {
            String json = new Gson().toJson(chat);
            response.getWriter().print(json);
            response.getWriter().flush();
        }
        if (uri.endsWith("add")) {
            username = session.getAttribute("user").toString();
            String text = request.getParameter("text");
            chat.add(new Message(username, text));
        }
    }
}