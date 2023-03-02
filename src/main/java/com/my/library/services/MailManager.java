package com.my.library.services;

import com.my.library.db.entities.Entity;
import org.apache.commons.compress.utils.IOUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Stream;

public class MailManager {
    private static void sendEmail(Session session, String fromEmail, String toEmail, String subject, String body){
        try {
            String subj = new String(subject.getBytes(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"));
            String cont = new String(body.getBytes(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"));
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(fromEmail, "NoReply-Librarian"));
            msg.setReplyTo(InternetAddress.parse( fromEmail, false));
            msg.setSubject(subj, "UTF-8");
            msg.setText(cont, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void send(String toEmail, String subject, String emailBody) {
        InputStream in = Entity.class.getResourceAsStream("/email.properties");
        Properties props = new Properties();
        try {
            props.load(in);
        } catch (IOException | RuntimeException ex) {
            throw new RuntimeException(ex);
        }
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(props.getProperty("email"), props.getProperty("password"));
            }
        };

        Session session = Session.getDefaultInstance(props, auth);
        sendEmail(session, (String) props.get("email"), toEmail,subject, emailBody);
    }

    public static void main(String[] args) throws IOException {
        //InputStream in = new Entity().getClass().getResourceAsStream("/text_ua.properties");
        File targetFile = new File("src/main/resources/text_ua_2.properties");
        File sourceFile = new File("src/main/resources/text_ua.properties");
        try (Stream<String> lines =
                     Files.lines(sourceFile.toPath(), StandardCharsets.UTF_8)) {

            lines.forEach(line -> {
                String convertLine = new String(line.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
                System.out.println(convertLine);
            });

        }
       // String subj = new String(subject.getBytes(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"));
    }


}
