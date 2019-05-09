package br.com.iamandu.disparador.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Representação Transfer Object da entidade Registro Job
 * @author wescley.sousa
 * @since 14/12/2018
 */
public class RegistroJobDTO implements Serializable {

	private static final long serialVersionUID = -3714569399328861461L;

	@Getter
	@Setter
	private String nome;
	
	@Getter
	@Setter
	private String descricao;
	
	@Getter
	@Setter
	private Long idRS;
	
	@Getter
	@Setter
	private String urlServico;

	@Getter
	@Setter
	private String expressao;
	
	@Getter
	@Setter
	private String email;
	
	@Getter
	@Setter
	private String siglaDocumento;
	
}
