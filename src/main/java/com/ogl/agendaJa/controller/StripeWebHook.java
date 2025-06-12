package com.ogl.agendaJa.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.ogl.agendaJa.services.UsuarioService;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebHook {

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecret;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(400).body("Assinatura inválida");
        } catch (com.stripe.exception.SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

        switch (event.getType()) {
            case "checkout.session.completed":
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();
                String costumerId = session.getCustomer();
                String email = session.getCustomerDetails() != null ? session.getCustomerDetails().getEmail() : null;
                if (email != null) {
                    usuarioService.ativarPlano(email, costumerId);
                }
                break;

            case "invoice.payment_failed":
                // avisar via email
                System.out.println("Pagamento da fatura falhou.");
                break;

            case "customer.subscription.deleted":
                String customerId = event.getDataObjectDeserializer().getObject().map(obj -> ((com.stripe.model.Subscription) obj).getCustomer()).orElse(null);
                if (customerId != null) {
                    usuarioService.desativarPlano(customerId);
                    System.out.println("Assinatura cancelada para: " + customerId);
                }
                break;

            default:
                System.out.println("Evento ignorado: " + event.getType());
        }
        return ResponseEntity.ok("");
    }

}
