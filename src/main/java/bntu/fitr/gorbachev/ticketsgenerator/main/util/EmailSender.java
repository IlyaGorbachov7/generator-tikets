package bntu.fitr.gorbachev.ticketsgenerator.main.util;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    // Метод для отправки электронной почты
    public static void sendEmail(String to, String subject, String body) throws MessagingException {
        // Настройка свойств SMTP-сервера
        Properties props = new Properties();
        props.putAll(TicketGeneratorUtil.getConfig().getAppProp().getProperties());

        // Создание сессии
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(TicketGeneratorUtil.getConfig().getUsername().get(), TicketGeneratorUtil.getConfig().getPassword().get());
            }
        });

        // Создание письма
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(TicketGeneratorUtil.getConfig().getUsername().get()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        // Отправка письма
        Transport.send(message);
    }
}

