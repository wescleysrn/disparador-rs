package br.com.iamandu.disparador.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.iamandu.disparador.dto.RegistroJobDTO;
import br.com.iamandu.disparador.mediator.BarramentoEmailJob;
import br.com.iamandu.disparador.mediator.BarramentoRenderizadorJob;
import br.com.iamandu.disparador.mediator.ChamadaRSMediator;
import br.com.iamandu.barramento.comum.dto.disparador.JobDTO;
import br.com.iamandu.barramento.comum.dto.renderizador.RenderizadorDTO;
import br.com.iamandu.barramento.comum.dto.email.EmailDTO;
import br.com.iamandu.barramento.comum.dto.email.TextTypeEnum;
import br.com.iamandu.barramento.comum.excecao.BarramentoException;
import br.com.iamandu.barramento.comum.util.DataUtil;
import lombok.extern.log4j.Log4j2;

/**
 * Job generico que é criado diversas vezes de acordo com os registros de job do tipo chamada a RS
 * @author wescley.sousa
 * @since 14/12/2018
 * 
 */
@Log4j2
public class ChamadaRSJob implements Job, Serializable {

	private static final long serialVersionUID = 8838957460240124975L;

	@Autowired
	private ChamadaRSMediator mediator;
	
	@Autowired
	private BarramentoEmailJob emailMediator;
	
	@Autowired
	private BarramentoRenderizadorJob renderizadorMediator;
	
	/**
	 * Metodo de execução de acordo com a cron expression informada
	 */
	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		RegistroJobDTO dto = new RegistroJobDTO();
		JobDataMap jobDataMap = job.getMergedJobDataMap();
		dto.setUrlServico(jobDataMap.getString("urlServico"));
		dto.setNome(jobDataMap.getString("nome"));
		dto.setDescricao(jobDataMap.getString("descricao"));
		dto.setEmail(jobDataMap.getString("email"));
		dto.setSiglaDocumento(jobDataMap.getString("siglaDocumento"));
		
		// Chamar serviço
		try {
				
			JobDTO jobDTO = mediator.executarJob(dto);
			log.info("Informação de processamento a ser enviada por e-mail .......:" + jobDTO.getMensagemEmail());
			
			// Enviar e-mail
			if(dto.getEmail() != null && !"".equals(dto.getEmail())) {
				List<String> destinatarios = new ArrayList<>();
				destinatarios.add(dto.getEmail());
				
				RenderizadorDTO renderizador = new RenderizadorDTO();
				if(dto.getSiglaDocumento() != null && !"".equals(dto.getSiglaDocumento())) {
					renderizador.setSigla(dto.getSiglaDocumento());
					renderizador.setParametros(jobDTO.getParametros()); 
				} else {
					Map<String, Object> parametros = new HashMap<String, Object>();
					parametros.put("mensagem", jobDTO.getMensagemEmail());
					parametros.put("servico", dto.getNome());
					parametros.put("inicio", jobDTO.getDataHoraInicio());
					parametros.put("termino", jobDTO.getDataHoraTermino());
					parametros.put("tempoTotal", DataUtil.getDuracao(DataUtil.formatarStringToDataHora(jobDTO.getDataHoraInicio()), DataUtil.formatarStringToDataHora(jobDTO.getDataHoraTermino())));
					
					renderizador.setSigla("DISP-PADR");
					renderizador.setParametros(parametros);				
				}
				
				String mensagem = renderizadorMediator.renderizarDocumento(renderizador);
				
				EmailDTO email = new EmailDTO();
				email.setMensagem(mensagem);
				email.setDestinatarios(destinatarios);
				email.setAssunto("Execução de Job - " + dto.getNome());
				email.setFormato(TextTypeEnum.HTML); 
				email.setRegistrarEnvio(Boolean.FALSE);
				email.setRemetente("Edux Concursos <postmaster@eduxconcurso.com.br>");
				
				emailMediator.enviarEmail(email);			
			}
		
		} catch (BarramentoException ex) {
			System.out.println(ex);
		}
		
		// Armazenar histórico de execução
		
	}
	
}
