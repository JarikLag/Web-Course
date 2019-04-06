package ru.itmo.wm4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wm4.domain.Notice;
import ru.itmo.wm4.domain.Role;
import ru.itmo.wm4.form.NoticeCredentials;
import ru.itmo.wm4.security.AnyRole;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class NoticePage extends Page {
    @AnyRole({Role.Name.USER, Role.Name.ADMIN})
    @GetMapping(path = "/notice")
    public String noticeGet(Model model) {
        model.addAttribute("noticeForm", new NoticeCredentials());
        return "NoticePage";
    }

    @AnyRole({Role.Name.USER, Role.Name.ADMIN})
    @PostMapping(path = "/notice")
    public String noticePost(@Valid @ModelAttribute("noticeForm") NoticeCredentials noticeForm,
                               BindingResult bindingResult, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "NoticePage";
        }

        Notice notice = new Notice();
        notice.setText(noticeForm.getText());

        String tagNames[] = noticeForm.getTags().trim().split("\\s+");
        getNoticeService().save(notice, getUser(httpSession), getTagsService().save(tagNames));
        return "redirect:/notices";
    }
}
