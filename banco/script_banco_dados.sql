/* Script's necessários ao projeto */
  

/* Resource context.xml do tomcat8 
<Resource 
	name="jdbc/disparadorDS" 
	auth="Container" 
	type="javax.sql.DataSource" 
	url="jdbc:postgresql://localhost:5432/bd_disparador"
	driverClassName="org.postgresql.Driver" 
	username="user_disparador" 
	password="user_disparador" 
	maxTotal="100" 
	maxIdle="30" 
	maxWaitMillis="-1"
	poolPreparedStatements="true"			
	/>
*/
		

-- Role: user_disparador

-- DROP ROLE user_disparador;

CREATE ROLE user_disparador LOGIN
  ENCRYPTED PASSWORD 'md526e9aef39dfafc1df9cb3b5e0e1220d8'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;

COMMENT ON ROLE user_disparador IS 'Usuário de acesso ao banco db_disparador';


-- Database: bd_disparador

-- DROP DATABASE bd_disparador;

CREATE DATABASE bd_disparador
  WITH OWNER = user_disparador
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'pt_BR.UTF-8'
       LC_CTYPE = 'pt_BR.UTF-8'
       CONNECTION LIMIT = -1;

COMMENT ON DATABASE bd_disparador
  IS 'Banco de dados do sistema disparador.';


  
  
-- Sequence: public.registro_job_seq

-- DROP SEQUENCE public.registro_job_seq;

CREATE SEQUENCE public.registro_job_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.registro_job_seq
  OWNER TO user_disparador;


  
  
-- Table: public.tb_registro_job

-- DROP TABLE public.tb_registro_job;

CREATE TABLE public.tb_registro_job
(
  id bigint NOT NULL,
  nome character varying(100) NOT NULL,
  descricao character varying(300) NOT NULL,
  id_resource_server bigint NOT NULL,
  url_servico character varying(600) NOT NULL,
  expressao character varying(60) NOT NULL,
  email character varying(300),
  sigla_documento character varying(9),
CONSTRAINT tb_registro_job_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_registro_job
  OWNER TO user_disparador;


  
insert into tb_registro_job(id, nome, descricao, id_resource_server, url_servico, expressao, email) 
values(nextval('registro_job_seq'), 
	  'Job RS Teste Agente', 
	  'Um teste de job que chama um serviço resource server agente', 
	  1, 
	  'http://localhost:8080/agente/api/agente/job/tarefa', 
	  '0 0/3 * 1/1 * ? *', 
	  'alguem@gmail.com');


insert into tb_registro_job(id, nome, descricao, id_resource_server, url_servico, expressao, email) 
values(nextval('registro_job_seq'), 'Job RS Teste Armazem', 'Um teste de job que chama um serviço resource server armazem', 1, 'http://localhost:8080/armazem/api/armazem/job/tarefa', '0 0/5 * 1/1 * ? *', 'wescleysrn@gmail.com');

  
  
DROP TABLE IF EXISTS TB_GATILHOS_DISPARADOS;
DROP TABLE IF EXISTS TB_GATILHOS_PAUSADOS;
DROP TABLE IF EXISTS TB_ESTADO_AGENDADOR;
DROP TABLE IF EXISTS TB_TRAVAS;
DROP TABLE IF EXISTS TB_GATILHO_SIMPLES;
DROP TABLE IF EXISTS TB_GATILHO_CRON;
DROP TABLE IF EXISTS TB_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS TB_GATILHO_BLOB;
DROP TABLE IF EXISTS TB_GATILHOS;
DROP TABLE IF EXISTS TB_JOB_DETALHE;
DROP TABLE IF EXISTS TB_CALENDARIO;



-- TABELAS

-- Table: public.tb_calendario

-- DROP TABLE public.tb_calendario;

CREATE TABLE public.tb_calendario
(
  nome_agendamento character varying(120) NOT NULL,
  nome_calendario character varying(200) NOT NULL,
  calendario bytea NOT NULL,
  CONSTRAINT tb_calendario_pkey PRIMARY KEY (nome_agendamento, nome_calendario)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_calendario
  OWNER TO user_disparador;

-- Table: public.tb_estado_agendador

-- DROP TABLE public.tb_estado_agendador;

CREATE TABLE public.tb_estado_agendador
(
  nome_agendamento character varying(120) NOT NULL,
  nome_instancia character varying(200) NOT NULL,
  tempo_ultimo_checkin bigint NOT NULL,
  intervalo_checkin bigint NOT NULL,
  CONSTRAINT tb_estado_agendador_pkey PRIMARY KEY (nome_agendamento, nome_instancia)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_estado_agendador
  OWNER TO user_disparador;

-- Table: public.tb_job_detalhe

-- DROP TABLE public.tb_job_detalhe;

CREATE TABLE public.tb_job_detalhe
(
  nome_agendamento character varying(120) NOT NULL,
  nome_job character varying(200) NOT NULL,
  grupo_job character varying(200) NOT NULL,
  descricao character varying(250),
  nome_classe_job character varying(250) NOT NULL,
  is_duravel boolean NOT NULL,
  is_nao_concorrente boolean NOT NULL,
  is_dados_atualizavel boolean NOT NULL,
  solicita_recuperacao boolean NOT NULL,
  dados_job bytea,
  CONSTRAINT tb_job_detalhe_pkey PRIMARY KEY (nome_agendamento, nome_job, grupo_job)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_job_detalhe
  OWNER TO user_disparador;

-- Table: public.tb_gatilhos_disparados

-- DROP TABLE public.tb_gatilhos_disparados;

CREATE TABLE public.tb_gatilhos_disparados
(
  nome_agendamento character varying(120) NOT NULL,
  id_entrada character varying(95) NOT NULL,
  nome_gatilho character varying(200) NOT NULL,
  grupo_gatilho character varying(200) NOT NULL,
  nome_instancia character varying(200) NOT NULL,
  tempo_disparo bigint NOT NULL,
  tempo_agendamento bigint NOT NULL,
  prioridade integer NOT NULL,
  estado character varying(16) NOT NULL,
  nome_job character varying(200),
  grupo_job character varying(200),
  is_nao_concorrente boolean,
  solicita_recuperacao boolean,
  CONSTRAINT tb_gatilhos_disparados_pkey PRIMARY KEY (nome_agendamento, id_entrada)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_gatilhos_disparados
  OWNER TO user_disparador;
  
-- Table: public.tb_gatilhos

-- DROP TABLE public.tb_gatilhos;

CREATE TABLE public.tb_gatilhos
(
  nome_agendamento character varying(120) NOT NULL,
  nome_gatilho character varying(200) NOT NULL,
  grupo_gatilho character varying(200) NOT NULL,
  nome_job character varying(200) NOT NULL,
  grupo_job character varying(200) NOT NULL,
  descricao character varying(250),
  tempo_proximo_disparo bigint,
  tempo_disparo_anterior bigint,
  prioridade integer,
  estado_gatilho character varying(16) NOT NULL,
  tipo_gatilho character varying(8) NOT NULL,
  tempo_inicio bigint NOT NULL,
  tempo_termino bigint,
  nome_calendario character varying(200),
  instrucao_falha_disparo smallint,
  dados_job bytea,
  CONSTRAINT tb_gatilhos_pkey PRIMARY KEY (nome_agendamento, nome_gatilho, grupo_gatilho),
  CONSTRAINT tb_job_detalhe_fkey FOREIGN KEY (nome_agendamento, nome_job, grupo_job)
      REFERENCES public.tb_job_detalhe (nome_agendamento, nome_job, grupo_job) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_gatilhos
  OWNER TO user_disparador;
  
-- Table: public.tb_gatilho_blob

-- DROP TABLE public.tb_gatilho_blob;

CREATE TABLE public.tb_gatilho_blob
(
  nome_agendamento character varying(120) NOT NULL,
  nome_gatilho character varying(200) NOT NULL,
  grupo_gatilho character varying(200) NOT NULL,
  dados_blob bytea,
  CONSTRAINT tb_gatilho_blob_pkey PRIMARY KEY (nome_agendamento, nome_gatilho, grupo_gatilho),
  CONSTRAINT tb_gatilhos_fkey FOREIGN KEY (nome_agendamento, nome_gatilho, grupo_gatilho)
      REFERENCES public.tb_gatilhos (nome_agendamento, nome_gatilho, grupo_gatilho) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_gatilho_blob
  OWNER TO user_disparador;
  
-- Table: public.tb_gatilho_cron

-- DROP TABLE public.tb_gatilho_cron;

CREATE TABLE public.tb_gatilho_cron
(
  nome_agendamento character varying(120) NOT NULL,
  nome_gatilho character varying(200) NOT NULL,
  grupo_gatilho character varying(200) NOT NULL,
  expressao_cron character varying(120) NOT NULL,
  time_zone_id character varying(80),
  CONSTRAINT tb_gatilho_cron_pkey PRIMARY KEY (nome_agendamento, nome_gatilho, grupo_gatilho),
  CONSTRAINT tb_gatilhos_fkey FOREIGN KEY (nome_agendamento, nome_gatilho, grupo_gatilho)
      REFERENCES public.tb_gatilhos (nome_agendamento, nome_gatilho, grupo_gatilho) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_gatilho_cron
  OWNER TO user_disparador;
  
-- Table: public.tb_gatilho_simples

-- DROP TABLE public.tb_gatilho_simples;

CREATE TABLE public.tb_gatilho_simples
(
  nome_agendamento character varying(120) NOT NULL,
  nome_gatilho character varying(200) NOT NULL,
  grupo_gatilho character varying(200) NOT NULL,
  contador_repeticao bigint NOT NULL,
  intervalo_repeticao bigint NOT NULL,
  tempos_acionamentos bigint NOT NULL,
  CONSTRAINT tb_gatilho_simples_pkey PRIMARY KEY (nome_agendamento, nome_gatilho, grupo_gatilho),
  CONSTRAINT tb_gatilhos_fkey FOREIGN KEY (nome_agendamento, nome_gatilho, grupo_gatilho)
      REFERENCES public.tb_gatilhos (nome_agendamento, nome_gatilho, grupo_gatilho) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_gatilho_simples
  OWNER TO user_disparador;
  
-- Table: public.tb_gatilhos_pausados

-- DROP TABLE public.tb_gatilhos_pausados;

CREATE TABLE public.tb_gatilhos_pausados
(
  nome_agendamento character varying(120) NOT NULL,
  grupo_gatilho character varying(200) NOT NULL,
  CONSTRAINT tb_gatilhos_pausados_pkey PRIMARY KEY (nome_agendamento, grupo_gatilho)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_gatilhos_pausados
  OWNER TO user_disparador;
  
-- Table: public.tb_simprop_triggers

-- DROP TABLE public.tb_simprop_triggers;

CREATE TABLE public.tb_simprop_triggers
(
  nome_agendamento character varying(120) NOT NULL,
  nome_gatilho character varying(200) NOT NULL,
  grupo_gatilho character varying(200) NOT NULL,
  str_prop_1 character varying(512),
  str_prop_2 character varying(512),
  str_prop_3 character varying(512),
  int_prop_1 integer,
  int_prop_2 integer,
  long_prop_1 bigint,
  long_prop_2 bigint,
  dec_prop_1 numeric(13,4),
  dec_prop_2 numeric(13,4),
  bool_prop_1 boolean,
  bool_prop_2 boolean,
  CONSTRAINT tb_simprop_triggers_pkey PRIMARY KEY (nome_agendamento, nome_gatilho, grupo_gatilho),
  CONSTRAINT tb_gatilhos_fkey FOREIGN KEY (nome_agendamento, nome_gatilho, grupo_gatilho)
      REFERENCES public.tb_gatilhos (nome_agendamento, nome_gatilho, grupo_gatilho) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_simprop_triggers
  OWNER TO user_disparador;
  
-- Table: public.tb_travas

-- DROP TABLE public.tb_travas;

CREATE TABLE public.tb_travas
(
  nome_agendamento character varying(120) NOT NULL,
  nome_trava character varying(40) NOT NULL,
  CONSTRAINT tb_travas_pkey PRIMARY KEY (nome_agendamento, nome_trava)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tb_travas
  OWNER TO user_disparador;

  
  
-- INDICES

  
  
-- Index: public.idx_tb_t_c

-- DROP INDEX public.idx_tb_t_c;

CREATE INDEX idx_tb_t_c
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", nome_calendario COLLATE pg_catalog."default");

-- Index: public.idx_tb_t_g

-- DROP INDEX public.idx_tb_t_g;

CREATE INDEX idx_tb_t_g
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", grupo_gatilho COLLATE pg_catalog."default");

-- Index: public.idx_tb_t_j

-- DROP INDEX public.idx_tb_t_j;

CREATE INDEX idx_tb_t_j
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", nome_job COLLATE pg_catalog."default", grupo_job COLLATE pg_catalog."default");

-- Index: public.idx_tb_t_jg

-- DROP INDEX public.idx_tb_t_jg;

CREATE INDEX idx_tb_t_jg
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", grupo_job COLLATE pg_catalog."default");

-- Index: public.idx_tb_t_n_g_state

-- DROP INDEX public.idx_tb_t_n_g_state;

CREATE INDEX idx_tb_t_n_g_state
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", grupo_gatilho COLLATE pg_catalog."default", estado_gatilho COLLATE pg_catalog."default");

-- Index: public.idx_tb_t_n_state

-- DROP INDEX public.idx_tb_t_n_state;

CREATE INDEX idx_tb_t_n_state
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", nome_gatilho COLLATE pg_catalog."default", grupo_gatilho COLLATE pg_catalog."default", estado_gatilho COLLATE pg_catalog."default");

-- Index: public.idx_tb_t_next_fire_time

-- DROP INDEX public.idx_tb_t_next_fire_time;

CREATE INDEX idx_tb_t_next_fire_time
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", tempo_proximo_disparo);

-- Index: public.idx_tb_t_nft_misfire

-- DROP INDEX public.idx_tb_t_nft_misfire;

CREATE INDEX idx_tb_t_nft_misfire
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", instrucao_falha_disparo, tempo_proximo_disparo);

-- Index: public.idx_tb_t_nft_st

-- DROP INDEX public.idx_tb_t_nft_st;

CREATE INDEX idx_tb_t_nft_st
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", estado_gatilho COLLATE pg_catalog."default", tempo_proximo_disparo);

-- Index: public.idx_tb_t_nft_st_misfire

-- DROP INDEX public.idx_tb_t_nft_st_misfire;

CREATE INDEX idx_tb_t_nft_st_misfire
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", instrucao_falha_disparo, tempo_proximo_disparo, estado_gatilho COLLATE pg_catalog."default");

-- Index: public.idx_tb_t_nft_st_misfire_grp

-- DROP INDEX public.idx_tb_t_nft_st_misfire_grp;

CREATE INDEX idx_tb_t_nft_st_misfire_grp
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", instrucao_falha_disparo, tempo_proximo_disparo, grupo_gatilho COLLATE pg_catalog."default", estado_gatilho COLLATE pg_catalog."default");

-- Index: public.idx_tb_t_state

-- DROP INDEX public.idx_tb_t_state;

CREATE INDEX idx_tb_t_state
  ON public.tb_gatilhos
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", estado_gatilho COLLATE pg_catalog."default");
  
-- Index: public.idx_tb_ft_inst_job_req_rcvry

-- DROP INDEX public.idx_tb_ft_inst_job_req_rcvry;

CREATE INDEX idx_tb_ft_inst_job_req_rcvry
  ON public.tb_gatilhos_disparados
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", nome_instancia COLLATE pg_catalog."default", solicita_recuperacao);

-- Index: public.idx_tb_ft_j_g

-- DROP INDEX public.idx_tb_ft_j_g;

CREATE INDEX idx_tb_ft_j_g
  ON public.tb_gatilhos_disparados
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", nome_job COLLATE pg_catalog."default", grupo_job COLLATE pg_catalog."default");

-- Index: public.idx_tb_ft_jg

-- DROP INDEX public.idx_tb_ft_jg;

CREATE INDEX idx_tb_ft_jg
  ON public.tb_gatilhos_disparados
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", grupo_job COLLATE pg_catalog."default");

-- Index: public.idx_tb_ft_t_g

-- DROP INDEX public.idx_tb_ft_t_g;

CREATE INDEX idx_tb_ft_t_g
  ON public.tb_gatilhos_disparados
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", nome_gatilho COLLATE pg_catalog."default", grupo_gatilho COLLATE pg_catalog."default");

-- Index: public.idx_tb_ft_tg

-- DROP INDEX public.idx_tb_ft_tg;

CREATE INDEX idx_tb_ft_tg
  ON public.tb_gatilhos_disparados
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", grupo_gatilho COLLATE pg_catalog."default");

-- Index: public.idx_tb_ft_trig_inst_name

-- DROP INDEX public.idx_tb_ft_trig_inst_name;

CREATE INDEX idx_tb_ft_trig_inst_name
  ON public.tb_gatilhos_disparados
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", nome_instancia COLLATE pg_catalog."default");

-- Index: public.idx_tb_j_grp

-- DROP INDEX public.idx_tb_j_grp;

CREATE INDEX idx_tb_j_grp
  ON public.tb_job_detalhe
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", grupo_job COLLATE pg_catalog."default");

-- Index: public.idx_tb_j_req_recovery

-- DROP INDEX public.idx_tb_j_req_recovery;

CREATE INDEX idx_tb_j_req_recovery
  ON public.tb_job_detalhe
  USING btree
  (nome_agendamento COLLATE pg_catalog."default", solicita_recuperacao);
  
  
  


/* Renderização de documentos */  
  
insert into renderizador.tb_categoria_modelo_documento(id, descricao) values(nextval('RENDERIZADOR.TB_CATEGORIA_MODELO_DOCUMENTO_SEQ'), 'Disparador - Modelos de E-mail de Execução de Tarefas');

insert into renderizador.tb_modelo_documento(id, modelo, nome_modelo, sigla, categoria_modelo_documento_fk) 
values(nextval('RENDERIZADOR.TB_CATEGORIA_MODELO_DOCUMENTO_SEQ'),
'<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Disparador | Resource Server de Disparo de Tarefas</title>
	</head>
	<body style="margin: 0; padding: 0;">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td>
					<!-- Cabeçalho -->
					<table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
						<tr>
							<td>
								<table align="center" bgcolor="#38547d" border="0" cellpadding="0" cellspacing="0" width="580" style="border-collapse: collapse;">
									<tr>
										<td>
											<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse;">
												<tr><td bgcolor="#3697d9" style="font-size: 0; line-height: 0;" height="1">&nbsp;</td></tr>
												<!-- Espaço -->
												<tr><td style="font-size: 0; line-height: 0;" height="30">&nbsp;</td></tr>
											</table>
											<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse;">
												<tr>
													<td>
														<table align="left" border="0" cellpadding="0" cellspacing="0" width="100%" style="border-collapse: collapse;">
															<!-- Espaço -->
															<tr><td style="font-size: 0; line-height: 0;" height="3">&nbsp;</td></tr>
															<tr>
																<td width="100%" align="center" style="font-size: 21px; line-height: 34px; font-family:helvetica, Arial, sans-serif; color:#ffffff;">
																	Disparador - Processamento de Tarefas.
																</td>
															</tr>
														</table>
													</td>
												</tr>
											</table>
											<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse;">
												<!-- Espaço -->
												<tr><td style="font-size: 0; line-height: 0;" height="30">&nbsp;</td></tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<!-- Cabeçalho -->
					<!-- Seção de Mensagem -->
					<table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
						<!-- Espaço -->
						<tr><td style="font-size: 0; line-height: 0;" height="20">&nbsp;</td></tr>
						<tr>
							<td style="font-family: helvetica, Arial, sans-serif; font-size: 14px; color: #777777; line-height: 21px; padding-left: 18px;">
								<p>^mensagem</p>
							</td>
						</tr>
						<tr>
							<td style="font-size: 0; line-height: 0;" bgcolor="#f5f5f5" height="1">&nbsp;</td>
						</tr>
						<!-- Espaço -->
						<tr><td style="font-size: 0; line-height: 0;" height="20">&nbsp;</td></tr>
					</table>
					<!--/ Seção de Mensagem -->
					<!-- Seção de Informações Básicas -->
					<table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
						<tr>
							<td>								
								<table align="center" border="0" cellpadding="0" cellspacing="0" width="540" style="border-collapse: collapse;">
									<tr>
										<td> 
											<table align="left" border="0" cellpadding="0" cellspacing="0" width="180" style="border-collapse: collapse;">
												<tr>
													<td>
														<img src="https://cdn.conab.gov.br/cdn/nuarq/disparador/conab-logo-circulo.png" width="120" alt="Conab" style="display: block;">
													</td>
												</tr>
											</table>
											<table align="left" border="0" cellpadding="0" cellspacing="0" width="360" style="border-collapse: collapse;">
												<tr>
													<td>
														<table align="center" border="0" cellpadding="0" cellspacing="0" width="360" style="border-collapse: collapse;">
															<tr>
																<td> 
																	<table align="left" border="0" cellpadding="0" cellspacing="0" width="60%" style="border-collapse: collapse;">
																		<tr>
																			<td>
																				<h3 style="font-family: helvetica, Arial, sans-serif; font-size: 14px; color: #999999; margin-top: 6px; margin-bottom: 6px;">Serviço</h3>
																				<h4 style="color: #999999; font-size: 14px; line-height: 18px; font-weight: normal; font-family: helvetica, Arial, sans-serif;margin-top: 6px; margin-bottom: 6px;">^servico</h4>
																			</td>
																		</tr>
																		<tr>
																			<td>
																				<h3 style="font-family: helvetica, Arial, sans-serif; font-size: 14px; color: #999999; margin-top: 6px; margin-bottom: 6px;">Tempo Total</h3>
																				<h4 style="color: #999999; font-size: 14px; line-height: 18px; font-weight: normal; font-family: helvetica, Arial, sans-serif;margin-top: 6px; margin-bottom: 6px;">^tempoTotal</h4>
																			</td>
																		</tr>
																	</table>
																	<table align="left" border="0" cellpadding="0" cellspacing="0" width="40%" style="border-collapse: collapse;">
																		<tr>
																			<td>
																				<h3 style="font-family: helvetica, Arial, sans-serif; font-size: 14px; color: #999999; margin-top: 6px; margin-bottom: 6px;">Inicio</h3>
																				<h4 style="color: #999999; font-size: 14px; line-height: 18px; font-weight: normal; font-family: helvetica, Arial, sans-serif;margin-top: 6px; margin-bottom: 6px;">^inicio</h4>
																			</td>
																		</tr>
																		<tr>
																			<td>
																				<h3 style="font-family: helvetica, Arial, sans-serif; font-size: 14px; color: #999999; margin-top: 6px; margin-bottom: 6px;">Termino</h3>
																				<h4 style="color: #999999; font-size: 14px; line-height: 18px; font-weight: normal; font-family: helvetica, Arial, sans-serif;margin-top: 6px; margin-bottom: 6px;">^termino</h4>
																			</td>
																		</tr>
																	</table>
																</td> 
															</tr>
														</table>
													</td>
												</tr>
											</table>
										</td> 
									</tr>
								</table>
							</td>
						</tr>
						<!-- Espaço -->
						<tr><td style="font-size: 0; line-height: 0;" height="20">&nbsp;</td></tr>
					</table>
					<!--/ Seção de Informações Básicas -->
					<!-- Rodapé -->
					<table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
						<tr>
							<td>
								<table bgcolor="#f5f5f5" align="center" border="0" cellpadding="0" cellspacing="0" width="580" style="border-collapse: collapse;">
									<tr>
										<td>
											<!-- Espaço -->
											<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%" style="border-collapse: collapse;">
												<tr><td style="font-size: 0; line-height: 0;" bgcolor="#eaeaea" height="1">&nbsp;</td></tr>
												<tr><td style="font-size: 0; line-height: 0;" height="20">&nbsp;</td></tr>
											</table>
											<table align="center" border="0" cellpadding="0" cellspacing="0" width="540" style="border-collapse: collapse;">
												<tr>
													<td align="center" style="color: #999999; font-size: 14px; line-height: 18px; font-weight: normal; font-family: helvetica, Arial, sans-serif;">
														Conab - Companhia Nacional de Abastecimento
													</td>
												</tr>
											</table>
											<!-- Espaço -->
											<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse;">
												<tr><td style="font-size: 0; line-height: 0;" height="20">&nbsp;</td></tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<!-- Rodapé -->
				</td>
			</tr>
		</table>
	</body>
</html>',
'Documento padrão para envio de informações de execução de tarefas do Disparador',
'DISP-PADR',
(select id from renderizador.tb_categoria_modelo_documento where descricao = 'Disparador - Modelos de E-mail de Execução de Tarefas'));
  



