package br.com.lucimarsb.ecommerce;

import br.com.lucimarsb.ecommerce.consumer.ConsumerService;
import br.com.lucimarsb.ecommerce.consumer.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ReadinReportService implements ConsumerService<User>{

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) {

        new ServiceRunner(ReadinReportService::new).start(5);
    }


    public void parse(ConsumerRecord<String, Message<User>> record) throws IOException {
        System.out.println("________________________________________");
        System.out.println("Processando relatório para " + record.value());

        var message = record.value();
        var user = message.getPayload();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Criado para " + user.getUuid());
        System.out.println("Arquivo Criado " + target.getAbsolutePath());

    }


    @Override
    public String getTopic() {
        return "ECOMMERCE_USER_GENERATE_READING_REPORT";
    }

    @Override
    public String getConsumerGroup() {
        return ReadinReportService.class.getSimpleName();
    }
}
