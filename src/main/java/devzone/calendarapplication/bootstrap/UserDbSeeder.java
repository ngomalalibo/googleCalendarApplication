package devzone.calendarapplication.bootstrap;

import devzone.calendarapplication.model.User;
import devzone.calendarapplication.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserDbSeeder implements CommandLineRunner
{
    UserRepository userRepository;
    
    public UserDbSeeder(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    
    @Override
    public void run(String... args) throws Exception
    {
        System.out.println("Triggered onApplicationEvent loading Users");
        initData();
        System.out.println("Loaded Bootstrap Data");
    }
    
    public void initData()
    {
        this.userRepository.deleteAll();
        
        List<User> users = new ArrayList<>();
        User user1 = new User("ngomalalibo@yahoo.com", "user1", "user1Pass", "admin");
        
        users.add(user1);
        this.userRepository.save(user1);
        
        User user2 = new User("ngomalalibo@gmail.com", "user2", "user2Pass", "customer");
        
        users.add(user2);
        this.userRepository.save(user2);
        
        users = Arrays.asList(user1, user2);
    }
}
