package com.devlog.controller;

import com.devlog.dto.user.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

/**
 * @author sbsong
 * @package com.devlog.controller
 * @Classname LoginController.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-08-20 sbsong : 최초작성
 * </PRE>
 * @since 2025-08-20
 */
@Controller
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @GetMapping("/login")
    public String loginView(Model model) {
        return "login"; // templates/login.html
    }

    @GetMapping("/join")
    public String joinView(Model model) {
        model.addAttribute("joinRequest", new UserJoinRequest());
        return "join";
    }

}
