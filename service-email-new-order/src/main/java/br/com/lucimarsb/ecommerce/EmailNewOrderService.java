package br.com.lucimarsb.ecommerce;
import br.com.lucimarsb.ecommerce.consumer.KafkaService;
import br.com.lucimarsb.ecommerce.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EmailNewOrderService {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var emailService = new EmailNewOrderService();
        try (var service = new KafkaService<>(EmailNewOrderService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                emailService::parse,
                Map.of())) {
            service.run();
        }
    }

    private  final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("________________________________________");
        System.out.println("Processando nova Order, preparando e-mail");
        System.out.println(record.value());

        var message = record.value();
        var order = message.getPayload();
        var id = message.getId().continueWith(EmailNewOrderService.class.getSimpleName());
        var emailCode = "Obrigado pelo seu pedido! Já Estamos processando!";
        emailDispatcher.send("ECOMMERCE_SEND_EMAIL", order.getEmail(),  id, emailCode);
    }
}
