package dev.srivatsan.spring_boot_kafka.controller;

import dev.srivatsan.spring_boot_kafka.dto.Employee;
import dev.srivatsan.spring_boot_kafka.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class KafkaController {

    private final ProducerService kafkaProducerService;

    public KafkaController(ProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/produce")
    public void save(@RequestBody Map<String, Object> message) {
        log.info("Received message: {}", message);
        Employee employee = new Employee();
        employee.setFirstName((String) message.get("firstName"));
        employee.setLastName((String) message.get("lastName"));
        employee.setEmpId((Integer) message.get("empId"));
        kafkaProducerService.sendMessage(employee);
    }

}
