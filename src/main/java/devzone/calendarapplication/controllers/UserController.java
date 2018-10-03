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
@RequestMapping("")
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
    
    @RequestMapping(value = "/user")
    public Principal user(Principal principal)
    {
        return principal;
    }
    
    //@ResponseBody allows you pass pure html as a string to the template/view eg return "<h1>" + Helloworld.getMessage(message)+"<h1>";
    @RequestMapping(value = {"", "/", "/index"})
    public ModelAndView welcomePage()
    {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }
    
    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, HttpSession session, Model model)
    {
        ModelAndView modelAndView = new ModelAndView("index");
        
        if (bindingResult.hasErrors())
        {
            //modelAndView.addObject("user", user);
            modelAndView.addObject("error", "Please enter a valid username and password");
            modelAndView.addObject("user", user);
        }
        if (userRepositoryImpl.login(user.getUsername(), user.getPassword()))
        {
            //model.addAttribute("user", user);
            modelAndView.addObject("user", user);
            model.addAttribute("username", user.getUsername());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("user", user);
            modelAndView.setViewName("welcome");
            modelAndView.addObject("success", "Welcome! You have successfully Logged in " + user.getUsername());
            modelAndView.addObject("user", user);
            sendMail.loginMail();
//            return modelAndView;
        } else
        {
            model.addAttribute("error", "Invalid Details");
            modelAndView.setViewName("redirect:index");
//            return "redirect:index";
        }
        return modelAndView;
    }
    
    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session)
    {
        //ModelAndView modelAndView = new ModelAndView("logout");
        session.removeAttribute("username");
        session.removeAttribute("user");
    
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("user", new User());
        sendMail.logoutMail();
        return modelAndView;
    }
    
    @GetMapping("/all")
    public List<User> getAllUsers()
    {
        List<User> users = userRepository.findAll();
        return users;
    }
    
    @PutMapping
    public void insert(@RequestBody User user)
    {
        this.userRepository.insert(user);
    }
    
    @PostMapping
    public void update(@RequestBody User user)
    {
        this.userRepository.save(user);
    }
    
    @DeleteMapping("/{username}")
    public void delete(@PathVariable("username") String username)
    {
        //this.userRepository.deleteUserByUsername(username);
    }
    
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
    
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@ModelAttribute("user") User user)
    {
        /*BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));*/
        //userRepositoryImpl.signup(user);
        return "redirect:../user.html";
    }
    
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
