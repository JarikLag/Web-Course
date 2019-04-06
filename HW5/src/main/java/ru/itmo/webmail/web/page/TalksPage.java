package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.Talk;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class TalksPage extends Page {
    private void action(HttpServletRequest request, Map<String, Object> view) {
    }

    private void add(HttpServletRequest request, Map<String, Object> view) {
        User sourceUser = getUser();
        User targetUser;
        String text;
        try {
            targetUser = getUserService().findByLogin(request.getParameter("targetUser"));
            if (targetUser == null) {
                throw new ValidationException("Incorrect target username");
            }
            if (targetUser.getId() == sourceUser.getId()) {
                throw new ValidationException("You can't write to yourself");
            }
            text = request.getParameter("text");
            if (text == null || text.equals("")) {
                throw new ValidationException("Please enter the message");
            }
        } catch (ValidationException e) {
            view.put("user", sourceUser);
            view.put("error", e.getMessage());
            return;
        }
        getTalkService().addTalk(sourceUser.getId(), targetUser.getId(), text);
        throw new RedirectException("/talks");
    }

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        if (getUser() == null) {
            throw new RedirectException("/index", "accessDenied");
        }
        List<Talk> inTalks = getTalkService().findByTargetUser(getUser());
        List<Talk> outTalks = getTalkService().findBySourceUser(getUser());
        String incomingTalks[][] = new String[inTalks.size()][2];
        String outgoingTalks[][] = new String[outTalks.size()][2];
        for (int i = 0; i < inTalks.size(); ++i) {
            incomingTalks[i][0] = getUserService().find(inTalks.get(i).getSourceUserId()).getLogin();
            incomingTalks[i][1] = inTalks.get(i).getText();
        }
        for (int i = 0; i < outTalks.size(); ++i) {
            outgoingTalks[i][0] = getUserService().find(outTalks.get(i).getTargetUserId()).getLogin();
            outgoingTalks[i][1] = outTalks.get(i).getText();
        }
        view.put("incomingTalks", incomingTalks);
        view.put("outgoingTalks", outgoingTalks);
    }
}