package es.ucampus.demo;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import es.ucampus.demo.adapterAMQP.AdapterEspacios;

@EntityScan(basePackageClasses = {
	domainObjects.entity.Espacio.class,
	domainObjects.valueObject.Horario.class
})
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Bean
	AdapterEspacios listenerAdapterEspacios() throws IOException {
		return new AdapterEspacios();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException, Exception {
		System.out.println("[Application] Recibiendo el mensaje...");
		listenerAdapterEspacios().receptorAMQP();
	}

}
