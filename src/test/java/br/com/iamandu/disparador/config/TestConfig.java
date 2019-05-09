/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software é confidencial e propriedade da Conab.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem
 * expressa autorização da Conab.
 * Este arquivo contém informações proprietárias.
 */

package br.com.iamandu.disparador.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author nome.sobrenome
 * @since data
 */
@EnableTransactionManagement
@PropertySource({"classpath:application.properties"})
@ComponentScan({"br.com.iamandu.barramento.consumidor.config", 
				"br.com.iamandu.barramento.consumidor.mediator", 
				"br.com.iamandu.disparador.dao", 
				"br.com.iamandu.disparador.mediator"})
@EnableJpaRepositories(basePackages = "br.com.iamandu.disparador.dao")
public class TestConfig {

	@Autowired
	private Environment env;

	@Bean
	public AnnotationMBeanExporter annotationMBeanExporter() {
		final AnnotationMBeanExporter annotationMBeanExporter = new AnnotationMBeanExporter();
		annotationMBeanExporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
		return annotationMBeanExporter;
	}
	
	@Bean
	public DataSource dataSourceTest() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(this.env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(this.env.getProperty("jdbc.url"));
		dataSource.setUsername(this.env.getProperty("jdbc.usuario"));
		dataSource.setPassword(this.env.getProperty("jdbc.senha"));
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSourceTest());
		em.setPackagesToScan(new String[] {"br.com.iamandu.disparador.entidade"});
		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());
		return em;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	final Properties additionalProperties() {
		final Properties hibernateProperties = new Properties();
//		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", this.env.getProperty("hibernate.hbm2ddl.auto"));
		hibernateProperties.setProperty("hibernate.dialect", this.env.getProperty("hibernate.dialect"));
		hibernateProperties.setProperty("hibernate.show_sql", this.env.getProperty("hibernate.show_sql"));
		hibernateProperties.setProperty("hibernate.format_sql", this.env.getProperty("hibernate.format_sql"));
		hibernateProperties.setProperty("javax.persistence.validation.mode", this.env.getProperty("javax.persistence.validation.mode"));
		return hibernateProperties;
	}
			
}
