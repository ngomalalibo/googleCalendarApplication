package devzone.calendarapplication.calendar;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.json.jackson.JacksonFactory;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import devzone.calendarapplication.database.MongoDB;
import devzone.calendarapplication.mail.SendHTMLEmail;
import devzone.calendarapplication.model.EventEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
public class GoogleCalController
{
    private final static Log logger = LogFactory.getLog(GoogleCalController.class);
    private static final String APPLICATION_NAME = "Google Calendar App";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static com.google.api.services.calendar.Calendar client;
    
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
    
    private SendHTMLEmail sendMail;
    
    final DateTime date1 = new DateTime("2017-05-05T16:30:00.000+05:30");
    final DateTime date2 = new DateTime(new Date());
    
    public void setEvents(Set<Event> events)
    {
        this.events = events;
    }
    
    @RequestMapping(value = "/newUser")
    public ResponseEntity addNewClient(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        RedirectView redirectView = new RedirectView();
            try {
                System.out.println("Inside NewCLent");
                redirectView = googleConnectionStatus(response);
                logger.info(redirectView.getUrl());
                response.sendRedirect(redirectView.getUrl());
                //return new ResponseEntity(redirectView.getUrl(), HttpStatus.OK);
            } catch (Exception ex) {
            }
        
        return new ResponseEntity(redirectView.getUrl(), HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/google", method = RequestMethod.GET)
    public RedirectView googleConnectionStatus(HttpServletResponse response) throws Exception {
            System.out.println("Inside googleConnectionStatus----------");
        return new RedirectView(authorize());
    }
    
    @RequestMapping(value = "/login/google", method = RequestMethod.GET, params = "code")
    public ResponseEntity<String> oauth2Callback(@RequestParam(value = "code") String code, HttpSession session, Model model) {
        System.out.println("Inside oauth2Callback-----------");
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
        } catch (Exception e) {
            logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.");
            message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.";
        }
        
        System.out.println("cal message:" + message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
    /*@RequestMapping(value = "/login/callback", method = RequestMethod.GET, params = "code")
    public ResponseEntity<List <EventEntity>> oauth2Callback(@RequestParam(value = "code") String code)
    {
        
        ModelAndView modelAndView = new ModelAndView();
        
        List<EventEntity> ee = getCalendarEvents(code);
    
        modelAndView.addObject("responseEntity", new ResponseEntity<>(ee, HttpStatus.OK));
        modelAndView.addObject("events" , ee);
        
        return new ResponseEntity<>(ee, HttpStatus.OK);
    }*/
    
    private List<EventEntity> getCalendarEvents(List<Event> events)
    {
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
            
                ee.add(eventPersist);
            
                eventPersist.persist(eventPersist);
            }
        }
        catch (Exception e)
        {
            logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.");
            message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.";
        }
    
        System.out.println("cal message:" + message);
        
        return ee;
    }
    
    private void refreshDataTable() {
//        googleEvents.clear();
//        googleEvents.addAll(getCalendarEvents());
//        updateOrderCount(allOrders.size());
//        grid.getDataProvider().refreshAll();
    }
    
    public Set<Event> getEvents() throws IOException
    {
        return this.events;
    }
    
    private String authorize() throws Exception
    {
        System.out.println("Inside authorize----------");
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null)
        {
            Details web = new Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR)).build();
        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI).setAccessType("offline").setApprovalPrompt("force");
        System.out.println("cal authorizationUrl->" + authorizationUrl);
        System.out.println("----------***Sending Mail***--------------");
        sendMail.loginMail();
        return authorizationUrl.build();
    }
}
