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

6 - Para verificar situação dos TOPICs

    6.1 - bin/kafka-topics.sh --describe --bootstrap-server localhost:9092

## **Para executar dois Cluster segue o processo abaixo antes de Startar o Servidor Kafka.**

7 - Configurando um segundo servidor (Replicação de Cluster) - Configuração básica e mais simples, poderia está usando 
docker ou algo do tipo, em servidores diferentes, só que nesse exemplo eu subir na mesma máquina, para ver o
funcionamento, caso tenha outro PC, é só reconfigurar conforme a necessidade, aqui é apenas uma dica de como eu fiz.
    
    7.1 - Duplica o arquivo de propriedades da pastas config, seguindo os passos abaixo.
        7.1.1 - cp config/server.properties config/server2.properties  
        7.1.2 - vi config/server2.properties
            7.1.2.1 - Alterar o "broker.id=0" para "broker.id=2"
            7.1.2.2 - Alterar a porta "listeners=PLAINTEXT://:9092" para "listeners=PLAINTEXT://:9093"
            7.1.2.3 - Alterar o diretório de logs "log.dirs=/Users/.../Kafka" para "log.dirs=/Users/.../Kafka2"
    7.2 - Subir o servidor
        7.2.1 - bin/kafka-server-start.sh config/server2.properties
    7.3 - Mudar replicação dos Topics, para que os dois CLuster consiga enxergar os TOPICs
        7.3.1 - bin/kafka-topics.sh --zookeeper localhost:2181 --alter --topic ECOMMERCE_NEW_ORDER --partitions 3 --replication-factor 2
        7.3.2 - Só que o comando a cima não vai funcionar, isso porque o servidor já está sendo usado e não consegue 
                alterar o fator de replicação, então deveria ser feitono momento que estávamos criando o TOPIC
        7.3.3 - vi config/server.properties
            7.3.3.1 - Adicionar a seguinte linha depois da linha que contém o "broker.id=0"
                    -> default.replication.factor = 2
            7.3.3.2 - fazer o mesmo para o segundo cluster "vi config/server2.properties"
    7.4 -  Mudar o _Consumer_offsets
        7.4.1 - vi config/server.properties
        7.4.2 - "offsets.topic.replication.factor=1" mudar para "offsets.topic.replication.factor=2" 
        7.4.3 - Reiniciar todos os servidores novamente, os dois kafkar e o zookeper

8 - Se existir a necessidade de criar mais Cluster é só executar o processo anterior alterando as configuração conforme 
sua necessidade, indico estudar cada ponto de alteração e seus impactos.

    


    
