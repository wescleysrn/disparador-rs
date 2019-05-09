package br.com.iamandu.disparador.mediator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iamandu.disparador.dao.RegistroJobDao;
import br.com.iamandu.disparador.entidade.RegistroJob;
import br.com.iamandu.barramento.comum.excecao.BarramentoException;
import lombok.extern.slf4j.Slf4j;

/**
 * Mediator da entidade Registro Job
 * @author wescley.sousa
 * @since 14/12/2018
 * 
 */
@Service
@Slf4j
public class RegistroJobMediator {

	@Autowired
	private RegistroJobDao dao;

	/**
	 * Metodo responsável por recuperar todos os registros de job não processados no banco de dados
	 * @return
	 * @throws BarramentoException
	 */
	public List<RegistroJob> buscarJobs() throws BarramentoException {
		log.info("Buscando registros de job para incluir no Quartz");
		return dao.findAll();
	}

}
