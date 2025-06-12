package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.model.ProductRequestStripe;
import com.ogl.agendaJa.model.StripeResponse;
import com.ogl.agendaJa.services.StripeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/v1")
public class StripeCheckoutController {

    private StripeService stripeService;

    public StripeCheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkout(@RequestBody ProductRequestStripe productRequest) {
        StripeResponse stripeResponse = stripeService.checkoutProduct(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(stripeResponse);
    }
}
