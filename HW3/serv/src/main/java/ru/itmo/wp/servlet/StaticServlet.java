package ru.itmo.wp.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class StaticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mainUri = request.getRequestURI();
        String[] parts = mainUri.split("\\+");
        List<File> files = new ArrayList<>();
        for (String uri : parts) {
            File file = new File("src/main/webapp/static/" + uri);
            if (!file.isFile()) {
                file = new File(getServletContext().getRealPath("/static/" + uri));
            }
            if (file.isFile()) {
                files.add(file);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        response.setContentType(getContentTypeFromName(parts[0]));
        OutputStream outputStream = response.getOutputStream();
        for (File curFile : files) {
            Files.copy(curFile.toPath(), outputStream);
        }
        outputStream.flush();
    }

    private String getContentTypeFromName(String name) {
        name = name.toLowerCase();

        if (name.endsWith(".png")) {
            return "image/png";
        }

        if (name.endsWith(".jpg")) {
            return "image/jpeg";
        }

        if (name.endsWith(".html")) {
            return "text/html";
        }

        if (name.endsWith(".css")) {
            return "text/css";
        }

        if (name.endsWith(".js")) {
            return "application/javascript";
        }

        throw new IllegalArgumentException("Can't find content type for '" + name + "'.");
    }
}