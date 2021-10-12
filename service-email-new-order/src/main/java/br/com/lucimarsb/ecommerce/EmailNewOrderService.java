package br.com.lucimarsb.ecommerce;

import br.com.lucimarsb.ecommerce.consumer.ConsumerService;
import br.com.lucimarsb.ecommerce.consumer.ServiceRunner;
import br.com.lucimarsb.ecommerce.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

public class EmailNewOrderService implements ConsumerService<Order> {

    public static void main(String[] args) {

        new ServiceRunner(EmailNewOrderService::new).start(1);
    }

    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("________________________________________");
        System.out.println("Processando nova Order, preparando e-mail");
        System.out.println(record.value());

        var message = record.value();
        var order = message.getPayload();
        var id = message.getId().continueWith(EmailNewOrderService.class.getSimpleName());
        var emailCode = "Obrigado pelo seu pedido! Já Estamos processando!";
        emailDispatcher.send("ECOMMERCE_SEND_EMAIL", order.getEmail(), id, emailCode);
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getConsumerGroup() {
        return EmailNewOrderService.class.getSimpleName();
    }
}
