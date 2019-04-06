package ru.itmo.wm4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wm4.domain.Comment;
import ru.itmo.wm4.domain.Role;
import ru.itmo.wm4.security.AnyRole;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class CommentPage extends Page {
    @AnyRole({Role.Name.USER, Role.Name.ADMIN})
    @GetMapping(path = "/notice/{nowId}")
    public String main(@PathVariable final Long nowId, Model model) {
        model.addAttribute("notice", getNoticeService().findById(nowId));
        model.addAttribute("comment", new Comment());
        return "CommentPage";
    }

    @AnyRole({Role.Name.USER, Role.Name.ADMIN})
    @PostMapping(path = "/notice/{nowId}")
    public String noticePost(@PathVariable final Long nowId, @Valid @ModelAttribute("comment") Comment comment,
                             BindingResult bindingResult, Model model, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("notice", getNoticeService().findById(nowId));
            return "CommentPage";
        }

        getCommentService().save(comment, getNoticeService().findById(nowId), getUser(httpSession));
        return "redirect:/notice/" + nowId;
    }
}
