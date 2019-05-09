package br.com.iamandu.disparador.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import br.com.iamandu.disparador.custom.JobFabrica;
import br.com.iamandu.disparador.entidade.RegistroJob;
import br.com.iamandu.disparador.job.ChamadaRSJob;
import br.com.iamandu.disparador.mediator.RegistroJobMediator;

/**
 * 
 * @author wescley.sousa
 * @since 14/12/2018
 */
@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
@Order(value=100)
public class ConfiguracaoQuartz {

	@Autowired
	private RegistroJobMediator mediator;
	
	/**
	 * 
	 * @param applicationContext
	 * @return
	 */
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext){
        JobFabrica jobFactory = new JobFabrica();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
    
    /**
     * 
     * @param dataSource
     * @param jobFactory
     * @return
     * @throws Exception
     */
    @Bean(name="SchedulerDisparador")
    public Scheduler schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws Exception { 
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setAutoStartup(true);
        factory.setJobFactory(jobFactory);

        factory.setQuartzProperties(quartzProperties());
        factory.afterPropertiesSet();

        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);
    	scheduler.start();
        
        for (RegistroJob job: mediator.buscarJobs()) {
			
        	JobDetail jobDetail = criarJobDetail(ChamadaRSJob.class, job);
        	CronTrigger trigger = criarCronTrigger(jobDetail, job);
        	
        	scheduler.deleteJob(jobDetail.getKey());        	
        	scheduler.scheduleJob(jobDetail, trigger);
		}        
        return scheduler;
    }

    /**
     * 
     * @return
     * @throws IOException
     */
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /**
     * 
     * @param jobClass
     * @param job
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static JobDetail criarJobDetail(Class jobClass, RegistroJob job) {
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
    private static CronTrigger criarCronTrigger(JobDetail jobDetail, RegistroJob job) {
    	CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(job.getExpressao()).withMisfireHandlingInstructionFireAndProceed();
    	return TriggerBuilder.newTrigger()
    						 .forJob(jobDetail)
    						 .withIdentity(job.getNome())
    						 .withDescription(job.getDescricao())
    						 .withSchedule(schedBuilder)
    						 .build();
    }
    
}
