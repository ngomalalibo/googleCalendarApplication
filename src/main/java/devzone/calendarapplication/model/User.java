package devzone.calendarapplication.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@QueryEntity //Needed for QueryDSL to create QUser class
@Data //Needed by project Lombok
@Document(collection = "users")
public class User
{
    //    @NotNull
//    @Size
    @Id
    private String id;
    //    private String id= UUID.randomUUID().toString();
    @NotNull
    private String username;
    @NotNull
    private String password;
    private String role;
    
    public User(String id, String username, String password, String role)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public User()
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
