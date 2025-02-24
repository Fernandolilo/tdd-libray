package com.systempro.library.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.systempro.library.service.EmailService;

@Service

public class EmailServiceImpl implements EmailService{
	

    private final JavaMailSender javaMailSender;

    @Value("${default.sender}")
    private String remetente;

    // Construtor padrão com @Autowired para injeção de dependência
    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(String message, List<String> mails) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String[] emailList = mails.toArray(new String[0]);

        mailMessage.setFrom(remetente);
        mailMessage.setSubject("Livro com empréstimo atrasado");
        mailMessage.setText(message);
        mailMessage.setTo(emailList);

        javaMailSender.send(mailMessage);
    }

}
