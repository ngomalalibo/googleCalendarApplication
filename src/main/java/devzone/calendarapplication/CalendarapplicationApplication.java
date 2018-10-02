package devzone.calendarapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Configuration
//@PropertySource(value = {"classpath:application.properties"})
@EnableOAuth2Sso
@SpringBootApplication
public class CalendarapplicationApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(CalendarapplicationApplication.class, args);
    }
}
