package br.com.iamandu.disparador.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.iamandu.disparador.dto.RegistroJobDTO;
import br.com.iamandu.disparador.mediator.RegistroJobSchedulerMediator;
import br.com.iamandu.barramento.comum.config.ConfiguracaoSwagger;
import br.com.iamandu.barramento.comum.excecao.BarramentoException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/registro")
@Api(tags={"Serviços de Registro de Tarefas"}, produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
public class RegistroJobService {

	@Autowired
	private RegistroJobSchedulerMediator mediator;
	
	/** 
	 * Metodo que salva um registro de tarefas.
	 * @param entidade
	 * @return {@link String}
	 * @throws BarramentoException
	 */
	@ApiOperation(value = "Salva um novo registro de tarefa", response = String.class, authorizations = {
			@Authorization(value = ConfiguracaoSwagger.SECURITY_SCHEMA_OAUTH2, scopes = {
			@AuthorizationScope(scope = ConfiguracaoSwagger.AUTHORIZATION_SCOPE_GLOBAL, description = ConfiguracaoSwagger.AUTHORIZATION_SCOPE_GLOBAL_DESCRICAO) }) })
	@ApiResponses({ 
			@ApiResponse(code = 200, message = "Registro salvo com sucesso."),
			@ApiResponse(code = 422, message = "Houve um erro ao realizar operação de salvar."), })
	@RequestMapping(method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value=HttpStatus.OK)
	public ResponseEntity<RegistroJobDTO> salvar(@ApiParam(name = "RegistroJobDTO", value = "Representação do registro de tarefa") 
												 @Validated @RequestBody RegistroJobDTO entidade) throws BarramentoException, Exception { 
		log.info("Salvando nova tarefa");
		mediator.salvar(entidade);
		return new ResponseEntity<RegistroJobDTO>(entidade, HttpStatus.OK);
	}
	
	/** 
	 * Metodo que remove um registro de tarefas.
	 * @param nome
	 * @return {@link String}
	 * @throws BarramentoException
	 */
	@ApiOperation(value = "Remove um registro de tarefa", response = String.class, authorizations = {
			@Authorization(value = ConfiguracaoSwagger.SECURITY_SCHEMA_OAUTH2, scopes = {
			@AuthorizationScope(scope = ConfiguracaoSwagger.AUTHORIZATION_SCOPE_GLOBAL, description = ConfiguracaoSwagger.AUTHORIZATION_SCOPE_GLOBAL_DESCRICAO) }) })
	@ApiResponses({ 
			@ApiResponse(code = 200, message = "Registro salvo com sucesso."),
			@ApiResponse(code = 422, message = "Houve um erro ao realizar operação de salvar."), })
	@RequestMapping(value = "/{nome}", method = RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value=HttpStatus.OK)
	public ResponseEntity<String> remover(@ApiParam(name = "Nome", value = "Nome do registro de tarefa") 
												 @PathVariable("nome") String nome) throws BarramentoException, Exception { 
		log.info("Removendo a tarefa " + nome);
		mediator.remover(nome);
		return new ResponseEntity<String>("Tarefa removida com sucesso.", HttpStatus.OK);
	}

	/**
	 * 
	 * @param pageable
	 * @return
	 * @throws BarramentoException
	 */
	@ApiOperation(value="Busca todas as tarefas agendadas", response=RegistroJobDTO.class,
			  authorizations={@Authorization(value = ConfiguracaoSwagger.SECURITY_SCHEMA_OAUTH2, 
							  				 scopes = {@AuthorizationScope(scope = ConfiguracaoSwagger.AUTHORIZATION_SCOPE_GLOBAL, 
							  				 							   description = ConfiguracaoSwagger.AUTHORIZATION_SCOPE_GLOBAL_DESCRICAO
											  )})}
	)	
	@ApiResponses({
		@ApiResponse(code = 200, message = "Lista de tarefas agendadas recuperada com sucesso."),
		@ApiResponse(code = 422, message = "Não foi possí­vel recuperar as tarefas agendadas."),
	})		
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<Page<RegistroJobDTO>> buscarTodos(Pageable pageable) throws BarramentoException {
		Page<RegistroJobDTO> lista = mediator.buscarTodos(pageable);
		return new ResponseEntity<Page<RegistroJobDTO>>(lista, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param pageable
	 * @return
	 * @throws BarramentoException
	 */
	@ApiOperation(value="Busca tarefa agendada por nome", response=RegistroJobDTO.class,
			  authorizations={@Authorization(value = ConfiguracaoSwagger.SECURITY_SCHEMA_OAUTH2, 
							  				 scopes = {@AuthorizationScope(scope = ConfiguracaoSwagger.AUTHORIZATION_SCOPE_GLOBAL, 
							  				 							   description = ConfiguracaoSwagger.AUTHORIZATION_SCOPE_GLOBAL_DESCRICAO
											  )})}
	)	
	@ApiResponses({
		@ApiResponse(code = 200, message = "Tarefa agendada recuperada com sucesso."),
		@ApiResponse(code = 422, message = "Não foi possí­vel recuperar a tarefa agendada pelo nome informado."),
	})		
	@RequestMapping(value = "/{nome}", method = RequestMethod.GET)
	public ResponseEntity<RegistroJobDTO> buscarPorNome(@PathVariable("nome") String nome) throws BarramentoException {
		RegistroJobDTO registroJob = mediator.buscarPorNome(nome);
		return new ResponseEntity<RegistroJobDTO>(registroJob, HttpStatus.OK);
	}
	
}
