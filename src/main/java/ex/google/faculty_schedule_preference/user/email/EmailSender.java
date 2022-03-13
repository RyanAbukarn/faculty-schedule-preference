package ex.google.faculty_schedule_preference.user.email;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

public interface EmailSender {

    void send(String to, String email) throws MessagingException, UnsupportedEncodingException;

    void sendReset(String email, String buildResetEmail) throws MessagingException, UnsupportedEncodingException;

}
