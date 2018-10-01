package devzone.calendarapplication.security;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /*.csrf()
                .disable()
                .cors()
                .disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/index", "/welcome", "/login", "/login/google").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .and()
                .logout()//
                // after successful logout the application will redirect to "/"
                // path
                .logoutSuccessUrl("/");*/
                
                .authorizeRequests()
                .antMatchers("", "/", "/index", "/welcome", "/login", "/login/google", "/newUser").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .oauth2Login()
                .loginPage("/welcome")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
    
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();
        
        return new InMemoryUserDetailsManager(user);
    }
}
