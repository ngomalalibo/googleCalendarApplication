package devzone.calendarapplication.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserBootstrap // implements ApplicationListener<ContextRefreshedEvent> (Is called before maven package is executed causing an error)
{
    
    /*@Autowired
    private UserRepository userRepository;
    
    public UserBootstrap(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    
    //@Override
    public void onApplicationEvent( ContextRefreshedEvent event)
    {
        //userRepository.deleteAll();
        
        System.out.println("Triggered onApplicationEvent loading Users");
        initData();
        System.out.println("Loaded Bootstrap Data");
    }
    
    public void initData()
    {
        this.userRepository.deleteAll();
 
        List<User> users = new ArrayList<>();
        User user1 = new User("ngomalalibo@yahoo.com", "user1","user1Pass", "admin");

        users.add(user1);
        this.userRepository.save(user1);

        User user2 = new User("ngomalalibo@gmail.com", "user2","user2Pass", "customer");

        users.add(user2);
        this.userRepository.save(user2);
    
        users = Arrays.asList(user1, user2);
    }*/
}
