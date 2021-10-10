package br.com.lucimarsb.ecommerce;

import java.math.BigDecimal;

public class Order {

    private final String orderId, email;
    private final BigDecimal amount;

    public Order(String orderId, BigDecimal amount, String email1) {
        this.orderId = orderId;
        this.amount = amount;
        this.email = email1;
    }

    public String getEmail() {
        return email;
    }
}
