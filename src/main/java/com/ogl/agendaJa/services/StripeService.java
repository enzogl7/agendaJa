package com.ogl.agendaJa.services;

import com.ogl.agendaJa.model.ProductRequestStripe;
import com.ogl.agendaJa.model.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;
    @Value("${app.base-url}")
    private String baseUrl;

    public StripeResponse checkoutProduct(ProductRequestStripe request) {
        Stripe.apiKey = secretKey;

        SessionCreateParams.LineItem lineItem = new SessionCreateParams.LineItem.Builder()
                .setQuantity(request.getQuantity())
                .setPrice(request.getPriceId()).build();

        SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(baseUrl + "/login")
                .setCancelUrl(baseUrl + "/login")
                .setCustomerEmail(request.getEmail())
                .addLineItem(lineItem).build();

        Session session = null;
        try {
           session = Session.create(params);
            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Sessão de pagamento criada com sucesso!")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        } catch (StripeException e) {
            System.out.println(e.getMessage());
            return StripeResponse.builder()
                    .status("ERROR")
                    .message("Erro ao criar sessão: " + e.getMessage())
                    .build();
        }

    }
}
