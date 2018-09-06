package devzone.calendarapplication.repositories;

import devzone.calendarapplication.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends MongoRepository<User,String>, QuerydslPredicateExecutor<User>
{
    //Declares all the data service methods for user
    
}