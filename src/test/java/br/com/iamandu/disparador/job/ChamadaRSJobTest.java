package br.com.iamandu.disparador.job;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.quartz.CronScheduleBuilder;
//import org.quartz.JobBuilder;
//import org.quartz.JobDataMap;
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.Trigger;
//import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import br.com.iamandu.disparador.config.TestConfig;
import br.com.iamandu.disparador.entidade.RegistroJob;
import br.com.iamandu.disparador.mediator.RegistroJobMediator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class}, loader = AnnotationConfigContextLoader.class)
public class ChamadaRSJobTest {

	@BeforeClass
	public static void setSystemProps() {
		System.setProperty("CONAB_SERVIDOR_PROPRIEDADE_CAMINHO", "/opt/apache-tomcat-8.5.28/lib");
	}
	
//	@Autowired
//	private Scheduler scheduler;

	@Autowired
	private RegistroJobMediator mediator;
	
	@Test
	public void test() throws Exception {
		
        for (RegistroJob job: mediator.buscarJobs()) {
        	
//        	JobDataMap jobDataMap = new JobDataMap();
//        	jobDataMap.put("nome", job.getNome());
//        	jobDataMap.put("descricao", job.getDescricao());
//        	jobDataMap.put("urlServico", job.getUrlServico());
//
//    		
//    	    JobDetail jobDetail = JobBuilder.newJob(ChamadaRSJob.class)
//        	    	.withIdentity(job.getNome())
//        	    	.withDescription(job.getDescricao())
//        	    	.usingJobData(jobDataMap)
//        	    	.build();
//    	
//    	    CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(job.getExpressao()).withMisfireHandlingInstructionFireAndProceed();
//    	    
//    	    Trigger trigger = TriggerBuilder.newTrigger()
//    	    								.forJob(jobDetail)
//    	    								.startNow()
//    	    								.withIdentity(job.getNome())
//    	    								.withDescription(job.getDescricao())
//    	    								.withSchedule(schedBuilder)
//    	    								.build();
//    	    
//    	    		TriggerBuilder.newTrigger()
//    	            .forJob(jobDetail)
//    	            .startNow()
//    	            .build();
//    	
//    	    scheduler.scheduleJob(jobDetail, trigger);
//    	    scheduler.start();
//    	    
//    	    Thread.sleep(5000);
        	
        }
		
	}

}
