package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Random;

public class CaptchaFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (session.getAttribute("captcha") != null && session.getAttribute("captcha").equals("passed")) {
            chain.doFilter(request, response);
        } else {
            if (request.getMethod().equals("POST")) {
                if (request.getParameter("captcha-try") != null &&
                        request.getParameter("captcha-try").equals(session.getAttribute("captcha-answer"))) {
                    session.setAttribute("captcha", "passed");
                }
                response.sendRedirect(request.getRequestURI());
                session.setAttribute("captcha-answer", "");
            } else {
                if (session.getAttribute("captcha-answer") == null ||
                        session.getAttribute("captcha-answer").equals("")) {
                    Random rand = new Random();
                    Integer code = rand.nextInt(899) + 100;
                    session.setAttribute("captcha-answer", code.toString());
                }
                response.setContentType("text/html");
                String image = Base64.getEncoder().encodeToString(ImageUtils.toPng(session.getAttribute("captcha-answer").toString()));
                byte[] captchaFile = Files.readAllBytes(Paths.get(getServletContext().getRealPath("/static/captcha.html")));
                String captcha = String.format(new String(captchaFile, "UTF-8"), image);
                response.getWriter().write(captcha);
                response.getWriter().flush();
            }
        }
    }
}