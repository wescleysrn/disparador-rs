package br.com.iamandu.disparador.mediator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.iamandu.barramento.comum.dto.renderizador.RenderizadorDTO;
import br.com.iamandu.barramento.comum.dto.validacao.MensagensDTO;
import br.com.iamandu.barramento.comum.excecao.BarramentoException;
import br.com.iamandu.barramento.comum.excecao.BarramentoNaoTratadoException;
import br.com.iamandu.barramento.comum.propriedade.BarramentoPropriedadeBean;

/**
 * 
 * @author wescley.sousa
 * @since 02/01/2018
 */
@Controller
@ComponentScan({"br.com.iamandu.barramento.comum.propriedade"})
public class BarramentoRenderizadorJob {
	
	@Autowired
	@Qualifier(value = "BarramentoPropriedadeBean")
	private BarramentoPropriedadeBean propriedadeServidor;

	@Autowired
	@Qualifier(value = "ClienteAutenticacaoBeanJob")
	private OAuth2RestOperations restTemplate;

	public String renderizarDocumento(final RenderizadorDTO parametros) throws BarramentoException {
		try {
			final HttpEntity<?> entity = new HttpEntity<RenderizadorDTO>(parametros, obterCabecalho());
			final RestTemplate restTemplate = new RestTemplate();			
			final HttpEntity<?> documento = restTemplate.exchange(this.propriedadeServidor.SERVICO_RENDERIZADOR, HttpMethod.POST, entity, RenderizadorDTO.class);
			final RenderizadorDTO modelo = (RenderizadorDTO) documento.getBody();
			return modelo.getModeloRenderizado();				
		} catch (HttpClientErrorException ex) {
			throw new BarramentoException(new MensagensDTO(ex.getMessage()), ex);
		} catch (final BarramentoNaoTratadoException ex) {
			throw ex;
		} catch (final RuntimeException ex) {
			throw new BarramentoNaoTratadoException(ex.getMessage(), ex);
		} catch (final Exception ex) {
			throw new BarramentoNaoTratadoException(ex.getMessage(), ex);
		}
	}

	private HttpHeaders obterCabecalho() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + this.restTemplate.getAccessToken());
		return headers;
	}

}
