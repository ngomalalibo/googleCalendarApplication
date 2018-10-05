package devzone.calendarapplication.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendHTMLEmail {
    
    @Value("${google.mail.username}")
    String username;
    @Value("${google.mail.password}")
    String password;
    
    public void loginMail()
    {
        // Recipient's email ID needs to be mentioned.
        String to = username;
    
        // Sender's email ID needs to be mentioned
        String from = username,
                d_port  = "465";
    
        // Assuming you are sending email from localhost
        String host = "smtp.gmail.com";
    
        // Get system properties
        Properties properties = System.getProperties();
    
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.user", username);
        properties.setProperty("mail.smtp.password", password);
        properties.setProperty("mail.smtp.port", d_port);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    
        // Get the default Session object.
        //Session session = Session.getDefaultInstance(properties);
    
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username,password);
            }
        });
        session.setDebug(true);
    
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
        
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
        
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        
            // Set Subject: header field
            message.setSubject("Login alert. Successful login to calendar App!");
            
            message.setText("Login alert: Message! ");
        
            // Send the actual HTML message, as big as you like
            message.setContent("<h1>This is actual message</h1>", "text/html");
        
            // Send message
            Transport transport = session.getTransport("smtps");
            transport.connect(host, Integer.valueOf(d_port), username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            //Transport.send(message);
            System.out.println("Login Successful....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    
    public void logoutMail()
    {
        // Recipient's email ID needs to be mentioned.
        String to = username;
        
        // Sender's email ID needs to be mentioned
        String from = username,
                d_port  = "465";
    
        // Assuming you are sending email from localhost
        String host = "smtp.gmail.com";
    
        // Get system properties
        Properties properties = System.getProperties();
    
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.user", username);
        properties.setProperty("mail.smtp.password", password);
        //properties.setProperty("mail.smtp.port", d_port);
        properties.setProperty("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
    
        // Get the default Session object.
        //Session session = Session.getDefaultInstance(properties);
    
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username,password);
            }
        });
        
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            
            // Set Subject: header field
            message.setSubject("Logout alert. Successful logout to calendar App!");
            
            // Send the actual HTML message, as big as you like
            message.setContent("<h1>This is actual message</h1>", "text/html");
            
            // Send message
            Transport transport = session.getTransport("smtp");
            transport.connect(host, Integer.valueOf(d_port), username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Logout success....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public static void main(String [] args) {
    
    }
}