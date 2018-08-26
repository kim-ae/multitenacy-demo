package br.com.kimae.multitenacydemo.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @RequestMapping("/")
    public ModelAndView index(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView view = new ModelAndView("index");

        view.addObject("email", auth.getName());
        return view;
    }

}
