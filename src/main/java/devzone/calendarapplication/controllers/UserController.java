package devzone.calendarapplication.controllers;


import devzone.calendarapplication.mail.SendHTMLEmail;
import devzone.calendarapplication.model.User;
import devzone.calendarapplication.repositories.UserRepository;
import devzone.calendarapplication.repositories.UserRepositoryImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


//@RestController

//Resolves to http://localhost:8080/<map>
@Controller

public class UserController
{
    private UserRepository userRepository;
    private UserRepositoryImpl userRepositoryImpl;
    private SendHTMLEmail sendMail;
    
    
    public UserController(UserRepository userRepository, UserRepositoryImpl userRepositoryImpl, SendHTMLEmail sendMail )
    {
        this.userRepository = userRepository;
        this.userRepositoryImpl = userRepositoryImpl;
        this.sendMail = sendMail;
    }
    
    
    
    //@ResponseBody allows you pass pure html as a string to the template/view eg return "<h1>" + Helloworld.getMessage(message)+"<h1>";
    
    
    
    
    
    
    
    
    
    
    /*@GetMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session)
    {
        return null;
    
    }*/
    
    /*@RequestMapping(value="/login", method=RequestMethod.POST)
    public String signup(ModelMap modelMap)
    {
        modelMap.put("user", new User());
        return "login";
    }*/
    
    
    
    /*@RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("user") User user,
                        HttpSession session,
                        ModelMap modelMap)
    {
        User user2 = userDataServiceImpl
                .login(user.getUsername(), user.getPassword());
        if (user2 == null)
        {
            modelMap.put("error", "Invalid User");
            return "index";
        } else
        {
            session.setAttribute("username", user.getUsername());
            return "welcome";
        }
    }*/
    
    /*@RequestMapping(value = "profile", method = RequestMethod.GET)
    public String profile(ModelMap modelMap, HttpSession session)
    {
        modelMap.put("user", userDataServiceImpl.find(session.getAttribute("username").toString()));
        return "profile";
    }*/
}
