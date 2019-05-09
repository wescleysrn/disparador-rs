package br.com.iamandu.disparador.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade onde é armazenado os job's do tipo chamada de serviço RS
 * @author wescley.sousa
 * @since 14/12/2018
 * 
 */
@Entity
@EqualsAndHashCode(of={"id"})
@Table(name="tb_registro_job")
@ApiModel(value="Entidade Registro Job", description="Representa um registro de tarefa agendada.")
@AllArgsConstructor
@NoArgsConstructor
public class RegistroJob implements Serializable {

	private static final long serialVersionUID = -7412703801572662410L;

	@Id
	@SequenceGenerator(name = "REGISTRO_JOB_ID_GENERATOR", sequenceName = "REGISTRO_JOB_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REGISTRO_JOB_ID_GENERATOR")
	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	@Column(name = "nome", length=100)
	private String nome;
	
	@Getter
	@Setter
	@Column(name = "descricao", length=300)
	private String descricao;
	
	@Getter
	@Setter
	@Column(name="id_resource_server", nullable=false)
	private Long idRS; // TODO Verificar necessidade 
	
	@Getter
	@Setter
	@Column(name = "url_servico", length=600)
	private String urlServico;

	@Getter
	@Setter
	@Column(name = "expressao", length=60)
	private String expressao;
	
	@Getter
	@Setter
	@Column(name = "email", length=300)
	private String email;
	
	@Getter
	@Setter
	@Column(name = "sigla_documento", length=9)
	private String siglaDocumento;
	
}
