package org.cyrilselyanin.vendingsystem.diag;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class DiagApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiagApplication.class, args);
	}

}
