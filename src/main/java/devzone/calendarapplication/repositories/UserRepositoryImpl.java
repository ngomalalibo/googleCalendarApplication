package devzone.calendarapplication.repositories;

import devzone.calendarapplication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional

public class UserRepositoryImpl
{
    //Implements/Overrides all the data service methods declared in UserRepository
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    
    public UserRepositoryImpl(MongoTemplate mongoTemplate)
    {
        //this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }
    
    
    public boolean login(String username, String password)
    {
        boolean loginBool = false;
        try
        {
            Query query = new Query();
            User user = mongoTemplate.findOne(query.addCriteria(Criteria.where("username")
                    .is(username)), User.class);
    
//            System.out.println("Username: "+user.getUsername());
//            System.out.println("Password: "+user.getPassword());
            
            if (user != null)
            {
                if(user.getPassword().equals(password))
                {
                    loginBool = true;
                }
                /*BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                if (bCryptPasswordEncoder.matches(password, user.getPassword()))
                {
                    return user;
                }*/
            }
        }
        catch (Exception e)
        {
        
        }
        return loginBool;
    }
    
}
