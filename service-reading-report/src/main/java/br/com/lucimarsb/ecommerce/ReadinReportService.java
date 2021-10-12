package br.com.lucimarsb.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class ReadinReportService {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) {
        var reportService = new ReadinReportService();
        try (var service = new KafkaService<>(ReadinReportService.class.getSimpleName(),
                "ECOMMERCE_USER_GENERATE_READING_REPORT",
                reportService::parse,
                Map.of())) {
            service.run();
        }
    }


    private void parse(ConsumerRecord<String, Message<User>> record) throws IOException {
        System.out.println("________________________________________");
        System.out.println("Processando relatório para " + record.value());

        var message = record.value();
        var user = message.getPayload();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Criado para " + user.getUuid());
        System.out.println("Arquivo Criado " + target.getAbsolutePath());

    }
}
