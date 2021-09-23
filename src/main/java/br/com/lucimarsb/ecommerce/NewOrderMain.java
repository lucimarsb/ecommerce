package br.com.lucimarsb.ecommerce;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try (var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<String>()) {
                for (var i = 0; i < 10; i++) {

                    var userd = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var amount = new BigDecimal(Math.random() * 500 + 1);
                    var order = new Order(userd, orderId, amount);
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", userd, order);

                    var email = "Obrigado pelo seu pedido! Já Estamos processando seu pedido!";
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userd, email);
                }
            }
        }
    }
}
