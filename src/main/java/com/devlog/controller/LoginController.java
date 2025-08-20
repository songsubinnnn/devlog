package com.devlog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/login")
public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @GetMapping
    public String loginView(Model model) {
        return "login"; // templates/login.html
    }

}
