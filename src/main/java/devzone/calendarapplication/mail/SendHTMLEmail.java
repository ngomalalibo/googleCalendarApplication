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
public class SendHTMLEmail
{
    
    @Value("${google.mail.username}")
    String username;
    @Value("${google.mail.password}")
    String password;
    
    String host = "smtp.gmail.com";
    String port = "465"; //d_port  = "465";
    
    String to = username;
    
    // Sender's email ID needs to be mentioned
    String from = username;
    
    public void loginMail()
    {
        try
        {
            sendPlainTextEmail(host, port, username, password, to, from);
        }
        catch (Exception e)
        {
            System.out.println("Error sending Message: " + e.getMessage() + " " + e.getCause());
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
                d_port = "465";
        
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
                return new PasswordAuthentication(username, password);
            }
        });
        
        try
        {
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
        }
        catch (MessagingException mex)
        {
            mex.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
    
    
    }
    
    public void sendPlainTextEmail(String host, String port,
                                   final String userName, final String password, String toAddress, String from) throws AddressException,
            MessagingException
    {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.user", userName);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        // creates a new session with an authenticator
        
        Session session = Session.getInstance(properties, new javax.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(userName, password);
            }
        });
        
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
        
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("ngomalalibo@yahoo.com"));
        msg.setFrom(new InternetAddress("ngomalalibo@gmail.com"));
        msg.setSentDate(new Date());
        // set plain text message
        msg.setText("Login alert: Message! ");
        
        msg.setSubject("Login alert. Successful login to calendar App!");
        msg.setContent("<h1>This is actual message</h1>", "text/html");
        
        // sends the e-mail
        Transport.send(msg);
        
        System.out.println("Login Successful....");
    }
    
    public void sendMailUntested()
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp-relay.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(username, password);
                    }
                });
        
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(username));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");
            
            Transport.send(message);
            
            System.out.println("Mail Sent");
            
        }
        catch (MessagingException e)
        {
            System.out.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}