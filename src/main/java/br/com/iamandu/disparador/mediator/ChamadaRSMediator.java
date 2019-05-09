package br.com.iamandu.disparador.mediator;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.iamandu.disparador.dto.RegistroJobDTO;
import br.com.iamandu.barramento.comum.dto.disparador.JobDTO;
import br.com.iamandu.barramento.comum.dto.validacao.MensagensDTO;
import br.com.iamandu.barramento.comum.excecao.BarramentoException;
import br.com.iamandu.barramento.comum.propriedade.BarramentoPropriedadeBean;
import br.com.iamandu.barramento.comum.util.DataUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Mediator responsável pela chamada autenticada do serviço RS
 * @author wescley.sousa
 * @since 14/12/2018
 */
@Service
@ComponentScan({"br.com.iamandu.barramento.comum.propriedade"})
@Slf4j
public class ChamadaRSMediator {

	@Autowired
	@Qualifier(value = "BarramentoPropriedadeBean")
	private BarramentoPropriedadeBean propriedadeServidor;

	@Autowired
	@Qualifier(value = "ClienteAutenticacaoBeanJob")
	private OAuth2RestOperations restTemplate;

	/**
	 * Metodo responsável por chamar o serviço de barramento para execução de tarefa agendada
	 * @param dto
	 * @return
	 * @throws BarramentoException
	 */
	public JobDTO executarJob(RegistroJobDTO dto) throws BarramentoException {
		log.info("Executando Job ......: " + dto.getNome());
		String dataHoraInicio = DataUtil.formatarDataHoraString(new Date());
		log.info("Iniciado em .........: " + dataHoraInicio);
		try {
			final HttpEntity<?> entity = new HttpEntity<>(obterCabecalho());
			final RestTemplate restTemplate = new RestTemplate();
			final HttpEntity<JobDTO> retorno = restTemplate.exchange(dto.getUrlServico(), HttpMethod.GET, entity, JobDTO.class);
			final JobDTO jobDTO = retorno.getBody();
			jobDTO.setDataHoraInicio(dataHoraInicio);
			jobDTO.setDataHoraTermino(DataUtil.formatarDataHoraString(new Date()));			
			log.info("Finalizado em .........: " + jobDTO.getDataHoraTermino());
			return jobDTO;
		} catch (final RuntimeException ex) {
			log.info("Finalizado em .........: " + DataUtil.formatarDataHoraString(new Date()));
			throw new BarramentoException(new MensagensDTO(ex.getMessage()), ex);
		} catch (final Exception ex) {
			log.info("Finalizado em .........: " + DataUtil.formatarDataHoraString(new Date()));
			throw new BarramentoException(new MensagensDTO(ex.getMessage()), ex);
		}
	}

	/**
	 * Metodo que recupera o cabeçalho de autorização Oauth2
	 * @return
	 */
	private HttpHeaders obterCabecalho() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + this.restTemplate.getAccessToken());
		return headers;
	}
	
}
