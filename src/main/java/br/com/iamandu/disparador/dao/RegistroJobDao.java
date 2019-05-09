package br.com.iamandu.disparador.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.iamandu.disparador.entidade.RegistroJob;

/**
 * Dao da entidade Registro Job
 * @author wescley.sousa
 * @since 14/12/2018
 * 
 */
public interface RegistroJobDao extends JpaRepository<RegistroJob, Long> {

	RegistroJob findByNome(String nome);
	
}
