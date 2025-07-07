package dev.srivatsan.spring_boot_kafka;

import dev.srivatsan.spring_boot_kafka.service.ProducerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SpringBootKafkaApplication implements CommandLineRunner {

	private ProducerService producerService;

	public SpringBootKafkaApplication(ProducerService producerService) {
		this.producerService = producerService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootKafkaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		producerService.sendMessageInfinitely();
	}
}
