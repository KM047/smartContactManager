package com.scm.smartContactManager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {

    @RequestMapping("/")
    public String homePage(Model model) {
        model.addAttribute("title", "Smart Contact Manager");
        model.addAttribute("heading", "Smart Contact Manager");
        model.addAttribute("desc", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorum earum, eos incidunt itaque ratione sed sit tenetur voluptas? Ad animi corporis quo vitae. A adipisci alias aliquid amet dicta dolores ea error explicabo harum impedit libero minima natus necessitatibus officia officiis optio pariatur perferendis quisquam ratione sequi, similique sint velit.");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model){

        model.addAttribute("isActive", false);
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage(){
        return "services";
    }

}
