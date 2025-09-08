package lanz.global.authenticationservice;

import lanz.global.authenticationservice.config.ServiceConfig;
import lanz.global.libraryservice.converter.config.ConverterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableConfigurationProperties(ServiceConfig.class)
@EnableDiscoveryClient
@Import(ConverterConfig.class)
@EnableFeignClients
public class AuthenticationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

}
