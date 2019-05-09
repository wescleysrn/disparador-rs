/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software é confidencial e propriedade da Conab.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem
 * expressa autorização da Conab.
 * Este arquivo contém informações proprietárias.
 */
package br.com.iamandu.disparador.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.request.RequestContextListener;

/**
 * @author wescley.sousa
 * @since 14/12/2018
 */
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan({"br.com.iamandu.barramento.comum.config", "br.com.iamandu.barramento.consumidor"})
public class Aplicacao extends SpringBootServletInitializer {

	public static void main(final String[] args) {
		SpringApplication.run(Aplicacao.class, args);
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
	
	@Bean(name="validator")
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}
	
}
