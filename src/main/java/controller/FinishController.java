package controller;

import entity.Battle;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import service.BattleService;
import service.FinishService;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/finish")
public class FinishController {
    private final UserService userService;
    private final FinishService finishService;
    private final BattleService battleService;


    @Autowired
    public FinishController(UserService userService, BattleService battleService, FinishService finishService) {
        this.userService = userService;
        this.finishService = finishService;
        this.battleService = battleService;
    }

    @GetMapping
    public ModelAndView finishView(HttpServletRequest req,
                                   HttpServletResponse resp) throws IOException {
        User u = userService.getUserAttributeFromSession(req.getSession());
        if (u != null && u.equals(battleService.getBattleById(battleService.isUserInBattle2(u.getLogin())).getPlayer1())) {
            return new ModelAndView("finish", "b", finishService.calculatePoints(battleService.getBattleById((Integer) req.getSession().getAttribute("battleId"))));
        } else if (u != null && u.equals(battleService.getBattleById(battleService.isUserInBattle2(u.getLogin())).getPlayer2())) {
            return new ModelAndView("finish", "b", finishService.calculatePoints(battleService.inverse(battleService.getBattleById((Integer) req.getSession().getAttribute("battleId")))));
        } else {
            resp.sendRedirect("/fs/");
            return null;
        }

    }

    @PostMapping
    public void battle(HttpServletResponse resp,
                       HttpServletRequest req) throws IOException {
        User u = userService.getUserAttributeFromSession(req.getSession());
        if (u != null) {
            Integer battleId = (Integer) req.getSession().getAttribute("battleId");
            Battle b = battleService.getBattleById(battleId);
            finishService.deleteBattle((Integer) req.getSession().getAttribute("battleId"));
            resp.sendRedirect("/fs/main/");
        } else {
            resp.sendRedirect("/fs/");
        }
    }

}
