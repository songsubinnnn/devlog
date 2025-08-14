package com.devlog.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author sbsong
 * @package com.devlog.exception
 * @Classname WebExceptionHandler.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-08-14 sbsong : 최초작성
 * </PRE>
 * @since 2025-08-14
 */
@Slf4j
@ControllerAdvice(basePackages = "com.devlog.controller")
public class WebExceptionHandler {
    // 404 :  잘못된 URL
    @ExceptionHandler(NoHandlerFoundException.class)
    public String noHandlerFound(HttpServletRequest req, Exception e, Model model) {
        log.error("404 Not Found Exception", e);
        model.addAttribute("msg", "404 Not Found Exception");
        model.addAttribute("path", req.getRequestURI());
        return "error/404";
    }

    // 500 : 기타 서버 오류
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model, HttpServletRequest request) {
        log.error("500 page error", e);
        model.addAttribute("path", request.getRequestURI());
        return "error/500";
    }
}
