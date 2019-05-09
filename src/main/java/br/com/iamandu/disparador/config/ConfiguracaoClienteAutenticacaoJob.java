/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software é confidencial e propriedade da Conab.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem
 * expressa autorização da Conab.
 * Este arquivo contém informações proprietárias.
 */
package br.com.iamandu.disparador.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import br.com.iamandu.barramento.comum.propriedade.BarramentoPropriedadeBean;
import br.com.iamandu.barramento.consumidor.cripto.TokenUtil;

/**
 * @author wescley.sousa
 * @since 23 de mar de 2017
 */
@EnableOAuth2Client
@Configuration
@ComponentScan({"br.com.iamandu.barramento.comum.propriedade"})
@PropertySource("classpath:usuario-barramento.properties")
public class ConfiguracaoClienteAutenticacaoJob {

	@Value("${barramento.usuario}")
	private String SERVICO_OAUTH_ID_CLIENTE;

	@Value("${barramento.senha}")
	private String SERVICO_OAUTH_SEGREDO_CLIENTE;
	
	@Autowired
	private Environment env;

	@Autowired
	@Qualifier(value = "BarramentoPropriedadeBean")
	private BarramentoPropriedadeBean propriedadeServidor;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
		final PropertySourcesPlaceholderConfigurer propriedade = new PropertySourcesPlaceholderConfigurer();
		propriedade.setIgnoreUnresolvablePlaceholders(Boolean.TRUE);
		return propriedade;
	}

	@Bean(name = "ClienteAutenticacaoBeanJob")
	public OAuth2RestOperations restTemplate() throws Exception {
		final AccessTokenRequest atr = new DefaultAccessTokenRequest();
		return new OAuth2RestTemplate(resourceJob(), new DefaultOAuth2ClientContext(atr));
	}

	@Bean
	protected OAuth2ProtectedResourceDetails resourceJob() throws Exception {
		final ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
		final List<String> scopes = new ArrayList<>(2);
		scopes.add("write");
		scopes.add("read");
		resource.setAccessTokenUri(this.propriedadeServidor.SERVICO_OAUTH_TOKEN);
		resource.setClientId(SERVICO_OAUTH_ID_CLIENTE);
		resource.setClientSecret(SERVICO_OAUTH_SEGREDO_CLIENTE);
		resource.setGrantType("password");
		resource.setScope(scopes);
		resource.setUsername(this.env.getProperty("barramento.usuario"));
		resource.setPassword(new TokenUtil().decrypt(this.env.getProperty("barramento.senha")));
		resource.setAuthenticationScheme(AuthenticationScheme.header);
		return resource;
	}

}
