package com.example.T1.kafka;

import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class KafkaSender {
        private final KafkaProducer<String, String> producer;
        private Logger logger = LoggerFactory.getLogger(KafkaSender.class);

        public KafkaSender(KafkaProducer<String, String> producer){

            this.producer = producer;
        }


        public void sendMessage(String topic, String key, String value, String errorType) throws RuntimeException{
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            record.headers().add("errorType", errorType.getBytes());

            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    logger.error("Ошибка при отправке сообщения в Kafka: {}", exception.getMessage());
                } else {
                    logger.info("Сообщение успешно отправлено в топик {}, партиция {}, offset {}",
                            metadata.topic(), metadata.partition(), metadata.offset());
                }
            });
        }

        @PreDestroy
        public void destroy() {
            producer.close();
        }
}