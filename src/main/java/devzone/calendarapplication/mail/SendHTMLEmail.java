package devzone.calendarapplication.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class SendHTMLEmail {
    
    @Value("${google.mail.username}")
    String username;
    @Value("${google.mail.password}")
    String password;
    
    String host = "smtp.gmail.com";
    String port = "587"; //d_port  = "465";
    
    String to = username;
    
    // Sender's email ID needs to be mentioned
    String from = username;
    
    public void loginMail()
    {
        try
        {
            sendPlainTextEmail(host, port, username, password, to, from);
        }
        catch(Exception e)
        {
            System.out.println("Error sending Message: "+e.getMessage());
            e.printStackTrace();
        }
    
        
        // Get system properties
        /*Properties properties = System.getProperties();
    
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
        session.setDebug(true);*/
    
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
    
    public void sendPlainTextEmail(String host, String port,
                                   final String userName, final String password, String toAddress, String from) throws AddressException,
            MessagingException {
        
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        // creates a new session with an authenticator
    
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        
        // creates a new e-mail message
        MimeMessage msg = new MimeMessage(session);
        
        msg.setFrom(new InternetAddress(userName));
        /*InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);*/
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("ngomalalibo@gmail.com"));
        msg.setFrom(new InternetAddress("ngomalalibo@gmail.com"));
        msg.setSentDate(new Date());
        // set plain text message
        msg.setText("Login alert: Message! ");
    
        //msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject("Login alert. Successful login to calendar App!");
        msg.setContent("<h1>This is actual message</h1>", "text/html");
        
        // sends the e-mail
        //Transport.send(msg);
        Transport transport = session.getTransport("smtp");
        transport.connect(host, Integer.valueOf(port), username, password);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
        //Transport.send(message);
        System.out.println("Login Successful....");
    }
}