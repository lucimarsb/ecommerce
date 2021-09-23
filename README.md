# ecommerce
Projeto ecommerce usando Kafka, ZooKeeper e Microserviços

Siga os passos abaixo para configurar o servidor Kafka:

1 - Baixa a ultima versão do Kafka.

2 - Descompacta e coloca em qualquer lugar do computador, desde que nenhum nome de pasta tenha espaços entre os nomes.

3 - Atraves da linha de comando CMD/Git Bash/PowerShell execute os seguintes comandos:

    3.1 - Navegue até a pasta do Kafka
    3.2 - Execute bin/zookeeper-server-start.sh config/zookeeper.properties
        3.2.1 - Verifica se o ZooKeeper está rodando na porta 0.0.0.0:2101 
    3.3 - Execute bin/kafka-server-start.sh config/server.properties
        3.3.1 - Verifica se o kafka informa "started", isso significa que o kafka se conectou com o zookeeper, verifica
                verifica também se o kafka iniciou na porta 9092, pois é o que está configurado nas classes do projeto
    3.4 - Cria os topics necessário usando o comando 
          bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ECOMMERCE_SEND_EMAIL
          bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ECOMMERCE_NEW_ORDER
    3.5 - Verifica se os topics foram criados
        3.5.1 - bin/kafka-topics.sh --list --bootstrap-server localhost:9092

4 - Comando para enviar atraves do CMD mensagens para fila usando o producer:
    
    4.1 bin/kafka-console-producer.sh --broker-list localhost:9092 --topic NOME_DO_TOPIC

5 - Comando para consumir mensagens de uma fila atraves do CMD:
    
    5.1 - bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic NOME_DO_TOPIC
    5.1 - bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic NOME_DO_TOPIC --from-beginning


    
