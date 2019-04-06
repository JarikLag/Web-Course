package ru.itmo.wm4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wm4.domain.User;
import ru.itmo.wm4.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UsersPage extends Page {
    private final UserService userService;

    public UsersPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    public String main(Model model) {
        model.addAttribute("users", userService.findAll());
        return "UsersPage";
    }

    @PostMapping(path = "/update")
    public String update(HttpServletRequest request) {
        User curUser = getUser(request.getSession());
        if (curUser == null) {
            return "redirect:/";
        }
        long userId = Long.parseLong(request.getParameter("userId"));
        User user = userService.findById(userId);
        boolean change = !user.isDisabled();
        userService.updateStatus(userId, change);
        if (change && curUser.getId() == userId) {
            unsetUser(request.getSession());
            return "redirect:/";
        }
        return "redirect:/users";
    }
}
