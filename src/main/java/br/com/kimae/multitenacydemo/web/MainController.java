package br.com.kimae.multitenacydemo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.kimae.multitenacydemo.service.InfoManagerService;
import br.com.kimae.multitenacydemo.web.interceptor.DatabaseContextSensitivy;

@Controller
public class MainController {

	private static final String UNKNOWN_USER_INFO = "UNKNOWN";
	@Autowired
	private InfoManagerService infoManagerService;

	@RequestMapping("/")
	@DatabaseContextSensitivy
	public ModelAndView index() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ModelAndView view = new ModelAndView("index");

		view.addObject("email", auth.getName());
		view.addObject("info", infoManagerService.getSensitivyUserInfo(auth.getName()).orElse(UNKNOWN_USER_INFO));
		return view;
	}

}
