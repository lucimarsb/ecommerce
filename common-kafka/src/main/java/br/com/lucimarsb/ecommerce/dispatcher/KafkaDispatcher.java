package br.com.lucimarsb.ecommerce.dispatcher;

import br.com.lucimarsb.ecommerce.CorrelationId;
import br.com.lucimarsb.ecommerce.Message;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaDispatcher<T> implements Closeable {
    private final KafkaProducer<String, Message<T>> producer;

    public KafkaDispatcher() {
        this.producer = new KafkaProducer<>(properties());
    }


    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        //Configuração para saber se todas minhas replicas recebeu a mensagem
        //Garantimos que se um dos meus topicos cair a mensagem vai ser processado por outro.
        //Pode causar lentidão, porém devemos verificar se faz sentido essa configuração, e fazer testes para saber o daley,
        //Caso não queira essa confirmação configurar o valor 0.
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        return properties;
    }
//ASSINCRONA
    public Future<RecordMetadata> sendAsync(String topic, String key, CorrelationId id, T payload) throws ExecutionException, InterruptedException {
        var value = new Message<>(id.continueWith("_"+ topic), payload);
        var record = new ProducerRecord<>(topic, key, value);
        Callback callback = (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                ;
                return;
            }
            System.out.println("sucesso enviado " + data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/ timestamp " + data.timestamp());
        };
        return producer.send(record, callback);
    }
//    SINCRONO
    public void send(String topic, String key, CorrelationId id, T payload) throws ExecutionException, InterruptedException {
        Future<RecordMetadata> future = sendAsync(topic, key, id, payload);
        future.get();

    }

    @Override
    public void close() {
        producer.close();
    }
}
