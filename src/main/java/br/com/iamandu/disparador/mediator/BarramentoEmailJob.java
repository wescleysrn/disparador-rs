package br.com.iamandu.disparador.mediator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.iamandu.barramento.comum.dto.email.EmailDTO;
import br.com.iamandu.barramento.comum.dto.validacao.MensagensDTO;
import br.com.iamandu.barramento.comum.excecao.BarramentoException;
import br.com.iamandu.barramento.comum.propriedade.BarramentoPropriedadeBean;

/**
 * 
 * @author wescley.sousa
 * @since 18/12/2018
 */
@Component
@ComponentScan({"br.com.iamandu.barramento.comum.propriedade"})
public class BarramentoEmailJob {

	@Autowired
	@Qualifier(value = "BarramentoPropriedadeBean")
	private BarramentoPropriedadeBean propriedadeServidor;

	@Autowired
	@Qualifier(value = "ClienteAutenticacaoBeanJob")
	private OAuth2RestOperations restTemplate;

	public void enviarEmail(final EmailDTO parametros) throws BarramentoException {
		try {
			final HttpEntity<?> entity = new HttpEntity<EmailDTO>(parametros, obterCabecalho());
			final RestTemplate restTemplate = new RestTemplate();
			restTemplate.exchange(this.propriedadeServidor.SERVICO_EMAIL, HttpMethod.POST, entity, EmailDTO.class);
		} catch (final RuntimeException ex) {
			throw new BarramentoException(new MensagensDTO(ex.getMessage()), ex);
		} catch (final Exception ex) {
			throw new BarramentoException(new MensagensDTO(ex.getMessage()), ex);
		}
	}

	private HttpHeaders obterCabecalho() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + this.restTemplate.getAccessToken());
		return headers;
	}
	
}
