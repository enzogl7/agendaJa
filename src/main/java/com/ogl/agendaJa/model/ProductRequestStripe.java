package com.ogl.agendaJa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestStripe {
    private String priceId;
    private Long quantity;
}
