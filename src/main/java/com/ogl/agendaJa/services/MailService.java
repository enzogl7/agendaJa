package com.ogl.agendaJa.services;

import com.ogl.agendaJa.model.Agendamento;
import com.ogl.agendaJa.model.PaginaNegocio;
import com.ogl.agendaJa.model.Usuario;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Year;
import java.util.Locale;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private PaginaNegocioService paginaNegocioService;
    @Value("${spring.mail.username}")
    private String remetente;

    public String enviarEmailConfirmacaoAgendamento(Agendamento agendamento) {
        try {
            PaginaNegocio negocioPrestador = paginaNegocioService.findByUsuario(agendamento.getPrestador());

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(agendamento.getCliente().getEmail());
            helper.setSubject("✅ Agendamento Confirmado em " + negocioPrestador.getNome());

            Context context = new Context();
            context.setVariable("nomeCliente", agendamento.getCliente().getNome());
            context.setVariable("nomeNegocioPrestador", negocioPrestador.getNome());
            context.setVariable("nomeServico", agendamento.getServico().getNome());
            context.setVariable("dataAgendamento", agendamento.getData());
            context.setVariable("horaAgendamento", agendamento.getHorario());
            context.setVariable("anoAtual", String.valueOf(Year.now().getValue()));

            String htmlContent = templateEngine.process("email/confirmacao_agendamento", context);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            System.out.println("Email de confirmação de agendamento enviado para: " + agendamento.getCliente().getEmail());
            return "Email de confirmação de agendamento enviado";

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro geral ao enviar email: " + e.getMessage());
            return "Erro ao tentar enviar email de confirmação do agendamento";
        }
    }
}
