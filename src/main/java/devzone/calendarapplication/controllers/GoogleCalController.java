package devzone.calendarapplication.controllers;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.json.jackson.JacksonFactory;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import devzone.calendarapplication.CalendarapplicationApplication;
import devzone.calendarapplication.calendar.CalendarQuickstart;
import devzone.calendarapplication.mail.SendHTMLEmail;
import devzone.calendarapplication.model.EventEntity;
import devzone.calendarapplication.model.User;
import devzone.calendarapplication.repositories.UserRepository;
import devzone.calendarapplication.repositories.UserRepositoryImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("")
public class GoogleCalController
{
    
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private UserRepository userRepository;
    private UserRepositoryImpl userRepositoryImpl;
    private SendHTMLEmail sendMail;
    
    
    public GoogleCalController(UserRepository userRepository, UserRepositoryImpl userRepositoryImpl, SendHTMLEmail sendMail)
    {
        this.userRepository = userRepository;
        this.userRepositoryImpl = userRepositoryImpl;
        this.sendMail = sendMail;
    }
    
    private final static Log logger = LogFactory.getLog(GoogleCalController.class);
    private static final String APPLICATION_NAME = "Google Calendar App";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static com.google.api.services.calendar.Calendar client;
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    
    
    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    //EmbeddedLdapProperties.Credential credential;
    Credential credential;
    
    @Value("${security.oauth2.client.client-id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;
    @Value("${security.oauth2.client.registered-redirect-uri}")
    private String redirectURI;
    
    private Set<Event> events = new HashSet<>();
    List<Event> googleEvents = new ArrayList<>();
    
    String message;
    
    final DateTime date1 = new DateTime("2017-05-05T16:30:00.000+05:30");
    final DateTime date2 = new DateTime(new Date());
    
    public void setEvents(Set<Event> events)
    {
        this.events = events;
    }
    
    @RequestMapping(value = {"/", "/index"})
    public ModelAndView welcomePage()
    {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }
    
    @RequestMapping("/welcome")
    public ModelAndView welcome()
    {
        return new ModelAndView("welcome");
    }
    
    @RequestMapping(value = "/login/google", method = RequestMethod.GET)
    public ModelAndView googleConnectionStatus(HttpServletRequest request) throws Exception
    {
        System.out.println("Inside googleConnectionStatus----------");
        //return new RedirectView(authorize());
        return authorize();
    }
    
    @RequestMapping(value = "/login/google", method = RequestMethod.GET, params = "code")
    public ModelAndView oauth2Callback(@RequestParam(value = "code") String code) {
        System.out.println("Inside oauth2Callback-----------");
    
        System.out.println("Code: "+code);
        
        ModelAndView mv = new ModelAndView();
        com.google.api.services.calendar.model.Events eventList;
        String message;
        try {
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
            credential = flow.createAndStoreCredential(response, "userID");
            client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            Events events = client.events();
            eventList = events.list("primary").setTimeMin(date1).setTimeMax(date2).execute();
            getCalendarEvents(eventList.getItems());
            message = eventList.getItems().toString();
            System.out.println("My:" + eventList.getItems());
            mv.addObject("events", eventList.getItems());
            mv.setViewName("welcome");
        } catch (Exception e) {
            logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.");
            message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.";
        }
        
        System.out.println("cal message:" + message);
        return mv;
    }
    
    private ModelAndView authorize() throws Exception
    {
        System.out.println("Inside authorize----------");
        
        com.google.api.services.calendar.model.Events eventList;
        String message;
        ModelAndView mv = new ModelAndView();
    
        String authUrl = "";
        
        AuthorizationCodeRequestUrl authorizationUrl;
        
        try
        {
            if (flow == null)
            {
                Details web = new Details();
                web.setClientId(clientId);
                web.setClientSecret(clientSecret);
                clientSecrets = new GoogleClientSecrets().setWeb(web);
                httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//                flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
//                        Collections.singleton(CalendarScopes.CALENDAR)).build();
    
                /*flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                        .setAccessType("offline")
                        .build();*/
    
                
    
                // Load client secrets.
                InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
                GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
    
                // Build flow and trigger user authorization request.
                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                        .build();
                credential =  new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    
                authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
                //authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI).setAccessType("offline").setApprovalPrompt("force");
                System.out.println("cal authorizationUrl->" + authorizationUrl);
    
                authUrl = authorizationUrl.build();
    
                System.out.println("after authorizationUrl.build()");
            }
            
            List<Event> events = CalendarQuickstart.getEvents(credential);
    
            System.out.println("CalendarQuickstart.getEvents(credential)---------->");
    
            List<EventEntity> ee = getCalendarEvents(events);
            System.out.println("getCalendarEvents(events)---------->");
            
            mv.addObject("events", ee);
            mv.setViewName("welcome");
        }
        catch (Exception e)
        {
            logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.");
            message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.";
        }
        
        return mv;
    }
    
    /*@RequestMapping(value = "/login/google", method = RequestMethod.GET)
    public ResponseEntity<String> oauth2Callback(@RequestParam(value = "code") String code)
    {
        //mv.setViewName("welcome");
        
        System.out.println("cal message: " + message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }*/
    
    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session)
    {
        //ModelAndView modelAndView = new ModelAndView("logout");
        session.removeAttribute("username");
        session.removeAttribute("user");
        
        ModelAndView modelAndView = new ModelAndView("redirect:index");
        modelAndView.addObject("user", new User());
        sendMail.logoutMail();
        return modelAndView;
    }
    
    private List<EventEntity> getCalendarEvents(List<Event> events)
    {
    
        System.out.println("Inside getCalendarEvents--------->");
        List<EventEntity> ee = new ArrayList<>();
        
        try
        {
            EventEntity eventPersist = new EventEntity();
            
            for (Event event : events)
            {
                eventPersist.setColorId(event.getColorId());
                eventPersist.setCreated(event.getCreated());
                eventPersist.setCreator(event.getCreator());
                eventPersist.setDescription(event.getDescription());
                eventPersist.setLocation(event.getLocation());
                eventPersist.setSummary(event.getSummary());
    
                System.out.println("ColorId: "+eventPersist.getColorId());
                System.out.println("Description: "+eventPersist.getDescription());
                System.out.println("Summary: "+eventPersist.getSummary());
                
                ee.add(eventPersist);
                
                eventPersist.persist(eventPersist);
            }
        }
        catch (Exception e)
        {
            logger.warn("Exception while handling OAuth2 callback (" + e.getClass() + ")."
                    + " Redirecting to google connection status page.");
            message = "Exception while handling OAuth2 callback (" + e.getClass()+" "+ e.getCause()+ ")."
                    + " Redirecting to google connection status page.";
        }
        
        System.out.println("cal message: " + message);
        
        return ee;
    }
    
    public Set<Event> getEvents() throws IOException
    {
        return this.events;
    }
    
}
