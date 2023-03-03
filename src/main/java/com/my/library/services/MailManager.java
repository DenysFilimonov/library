package com.my.library.services;

import com.my.library.db.entities.Entity;
import org.apache.commons.compress.utils.IOUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Enumeration;
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
        FileWriter writer = new FileWriter("src/main/resources/text_ua_2.properties", true);
        //File sourceFile = new File("src/main/resources/text_ua.properties");
        //try (Stream<String> lines =
 //                    Files.lines(sourceFile.toPath(), StandardCharsets.UTF_8)) {
//
   //         lines.forEach(line -> {
  //            String convertLine = new String(line.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
      //          System.out.println(convertLine);
   //         });

        InputStream in;
        in = new Entity().getClass().getResourceAsStream("/text_ua_2.properties");
        Properties prop = new Properties();
        prop.load(in);
        Enumeration properties = prop.keys();
        while(properties.hasMoreElements()){
            String key = (String) properties.nextElement();
            String property = (String) prop.get(key);
            System.out.println(property);
            System.out.println(charset(property, new String[] {"ISO-8859-1"}));
            String encodedProperty =new String(property.getBytes(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"));
            System.out.println(key+"="+encodedProperty);
            //writer.write(key+"="+encodedProperty+"\n");
        }



    }

    public static String convert(String value, String fromEncoding, String toEncoding) throws UnsupportedEncodingException {
        return new String(value.getBytes(fromEncoding), toEncoding);
    }

    public static String charset(String value, String charsets[]) throws UnsupportedEncodingException {
        String probe = StandardCharsets.UTF_8.name();
        for(String c : charsets) {
            Charset charset = Charset.forName(c);
            if(charset != null) {
                if(value.equals(convert(convert(value, charset.name(), probe), probe, charset.name()))) {
                    return c;
                }
            }
        }
        return StandardCharsets.UTF_8.name();
    }

       // String subj = new String(subject.getBytes(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"));


}
