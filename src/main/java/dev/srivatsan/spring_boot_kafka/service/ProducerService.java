package dev.srivatsan.spring_boot_kafka.service;

import dev.srivatsan.spring_boot_kafka.dto.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducerService {

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    private KafkaTemplate<String, Object> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Employee message) {
        log.info("Producer Sending message: {}", message);
        kafkaTemplate.send(topic, message);
    }

}