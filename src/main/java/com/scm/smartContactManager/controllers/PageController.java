package com.scm.smartContactManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.smartContactManager.constants.MessageType;
import com.scm.smartContactManager.helper.Message;
import com.scm.smartContactManager.models.UserFormModel;
import com.scm.smartContactManager.models.UserModel;
import com.scm.smartContactManager.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @RequestMapping("/")
    public String homePage(Model model) {
        model.addAttribute("title", "Smart Contact Manager");
        model.addAttribute("heading", "Smart Contact Manager");
        model.addAttribute("desc",
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorum earum, eos incidunt itaque ratione sed sit tenetur voluptas? Ad animi corporis quo vitae. A adipisci alias aliquid amet dicta dolores ea error explicabo harum impedit libero minima natus necessitatibus officia officiis optio pariatur perferendis quisquam ratione sequi, similique sint velit.");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model) {

        model.addAttribute("isActive", false);
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage() {
        return "services";
    }

    @RequestMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @RequestMapping("/register")
    public String singUpPage(Model model) {

        model.addAttribute("userFormModel", new UserFormModel());

        return "register";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserFormModel userForm, BindingResult result,
            HttpSession session, Model model) {

        if (result.hasErrors()) {

            System.out.println("Error encounter -> : " + result.getObjectName());

            // model.addAttribute("userFormModel", userForm);

            return "register";
        }

        UserModel user = new UserModel();

        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        user.setAddress(userForm.getAddress());
        user.setGender(userForm.getGender());
        user.setAvatar(env.getProperty("default.avatar"));

        UserModel savedUser = userService.saveUser(user);

        System.out.println("User is saved : " + savedUser);

        Message message = Message.builder().message("User registered Successfully.")
                .messageType(MessageType.green).build();

        session.setAttribute("msg", message);

        return "redirect:/register";
    }

}
