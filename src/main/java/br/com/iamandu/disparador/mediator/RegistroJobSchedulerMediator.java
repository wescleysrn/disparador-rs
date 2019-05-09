package br.com.iamandu.disparador.mediator;

import java.lang.reflect.Type;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.iamandu.disparador.dao.RegistroJobDao;
import br.com.iamandu.disparador.dto.RegistroJobDTO;
import br.com.iamandu.disparador.entidade.RegistroJob;
import br.com.iamandu.disparador.job.ChamadaRSJob;
import br.com.iamandu.barramento.comum.excecao.BarramentoException;
import lombok.extern.slf4j.Slf4j;

/**
 * Mediator de operações no scheduler em tempo de execução 
 * @author wescley.sousa
 * @since 14/12/2018
 * 
 */
@Service
@Slf4j
public class RegistroJobSchedulerMediator {

	@Autowired
	@Qualifier(value = "SchedulerDisparador")
	private Scheduler scheduler;
	
	@Autowired
	private RegistroJobDao dao;

	/**
	 * Metodo responsável por salvar e registrar um novo registro de tarefas
	 * @param entidadeDTO
	 */
	@Transactional
	public void salvar(RegistroJobDTO entidadeDTO) throws BarramentoException, Exception {
		log.info("Salvando registro de tarefas");
		
		final ModelMapper modelMapper = new ModelMapper();
		RegistroJob entidade = modelMapper.map(entidadeDTO, RegistroJob.class);	
		dao.save(entidade);
		
    	JobDetail jobDetail = criarJobDetail(ChamadaRSJob.class, entidade);
    	CronTrigger trigger = criarCronTrigger(jobDetail, entidade);
    	
    	scheduler.deleteJob(jobDetail.getKey());        	
    	scheduler.scheduleJob(jobDetail, trigger);
    	scheduler.start();		
	}
	
	/**
	 * Metodo responsável por remover um registro de tarefas
	 * @param entidadeDTO
	 */
	@Transactional
	public void remover(String nome) throws BarramentoException, Exception {
		log.info("Removendo registro de tarefas");
		RegistroJob entidade = dao.findByNome(nome);
		dao.delete(entidade);
    	scheduler.deleteJob(new JobKey(nome));        	
	}
	
	/**
	 * Metodo responsável por recuperar todas as tarefas agendadas salvas no disparador
	 * @param pageable
	 * @return
	 */
	public Page<RegistroJobDTO> buscarTodos(Pageable pageable) {
		Page<RegistroJob> registrosJob = dao.findAll(pageable);		
		ModelMapper modelMapper = new ModelMapper();
		Type alvoTipoLista = new TypeToken<List<RegistroJobDTO>>(){}.getType();		
		Page<RegistroJobDTO> registrosJobPage = new PageImpl<RegistroJobDTO>(modelMapper.map(registrosJob.getContent(), alvoTipoLista), pageable, registrosJob.getTotalElements()); 
		return registrosJobPage;
	}

	/**
	 * Metodo responsável por recuperar tarefa agendada por nome
	 * @param nome
	 * @return
	 */
	public RegistroJobDTO buscarPorNome(String nome) {
		RegistroJob registroJob = dao.findByNome(nome);
		if(registroJob == null) {
			return null;			
		}
		ModelMapper modelMapper = new ModelMapper();
		RegistroJobDTO registroJobDTO = modelMapper.map(registroJob, RegistroJobDTO.class);
		return registroJobDTO;
	}
	
    /**
     * 
     * @param jobClass
     * @param job
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private JobDetail criarJobDetail(Class jobClass, RegistroJob job) {
    	JobDataMap jobDataMap = new JobDataMap();
    	jobDataMap.put("nome", job.getNome());
    	jobDataMap.put("descricao", job.getDescricao());
    	jobDataMap.put("urlServico", job.getUrlServico());
    	jobDataMap.put("email", job.getEmail());
    	jobDataMap.put("siglaDocumento", job.getSiglaDocumento());
    	
    	return JobBuilder.newJob(jobClass)
    	    	.withIdentity(job.getNome())
    	    	.withDescription(job.getDescricao())
    	    	.usingJobData(jobDataMap)
    	    	.build();
    }
    
    /**
     * 
     * @param jobDetail
     * @param job
     * @return
     */
    private CronTrigger criarCronTrigger(JobDetail jobDetail, RegistroJob job) {
    	CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(job.getExpressao()).withMisfireHandlingInstructionFireAndProceed();
    	return TriggerBuilder.newTrigger()
    						 .forJob(jobDetail)
    						 .withIdentity(job.getNome())
    						 .withDescription(job.getDescricao())
    						 .withSchedule(schedBuilder)
    						 .build();
    }

}
