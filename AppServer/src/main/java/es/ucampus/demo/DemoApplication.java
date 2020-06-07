package es.ucampus.demo;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import es.ucampus.demo.adapter.AdapterEspacios;
import es.ucampus.demo.adapter.AdapterReservas;

@EntityScan(basePackageClasses = { domainObjects.entity.Espacio.class, domainObjects.valueObject.Horario.class })
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Bean
	AdapterEspacios listenerAdapterEspacios() throws IOException {
		return new AdapterEspacios("SpringAWebEspacios","WebASpringEspacios");
	}

	@Bean
	AdapterReservas listenerAdapterReservas() throws IOException {
		return new AdapterReservas("SpringAWebReservas","WebASpringReservas");
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException, Exception {
		
		Thread threadEspacios = new Thread("New threadEspacios") {
			public void run() {
				try {
					listenerAdapterEspacios().receptorAMQP();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		Thread threadReservas = new Thread("New threadReservas") {
			public void run() {
				try {
					listenerAdapterReservas().receptorAMQP();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	  
		threadEspacios.start();
		threadReservas.start();
		
		System.out.println("[Application] Recibiendo el mensaje...");

	}

}
