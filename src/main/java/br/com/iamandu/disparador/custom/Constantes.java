package br.com.iamandu.disparador.custom;

/**
 * 
 * Customização de org.quartz.impl.jdbcjobstore.Constants
 * 
 * <p>
 * This interface can be implemented by any <code>{@link
 * org.quartz.impl.jdbcjobstore.DriverDelegate}</code>
 * class that needs to use the constants contained herein.
 * </p>
 * 
 * @author <a href="mailto:jeff@binaryfeed.org">Jeffrey Wescott</a>
 * @author James House
 */
public interface Constantes {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    // Table names
    String TABLE_JOB_DETAILS = "JOB_DETALHE";

    String TABLE_TRIGGERS = "GATILHOS";

    String TABLE_SIMPLE_TRIGGERS = "GATILHO_SIMPLES";

    String TABLE_CRON_TRIGGERS = "GATILHO_CRON";

    String TABLE_BLOB_TRIGGERS = "GATILHO_BLOB";

    String TABLE_FIRED_TRIGGERS = "GATILHOS_DISPARADOS";

    String TABLE_CALENDARS = "CALENDARIO";

    String TABLE_PAUSED_TRIGGERS = "GATILHOS_PAUSADOS";

    String TABLE_LOCKS = "TRAVAS";

    String TABLE_SCHEDULER_STATE = "ESTADO_AGENDADOR";

    // TABLE_JOB_DETALHE columns names
    
    String COL_SCHEDULER_NAME = "NOME_AGENDAMENTO";
    
    String COL_JOB_NAME = "NOME_JOB";

    String COL_JOB_GROUP = "GRUPO_JOB";

    String COL_IS_DURABLE = "IS_DURAVEL";

    String COL_IS_VOLATILE = "IS_VOLATILE";

    String COL_IS_NONCONCURRENT = "IS_NAO_CONCORRENTE";

    String COL_IS_UPDATE_DATA = "IS_DADOS_ATUALIZAVEL";

    String COL_REQUESTS_RECOVERY = "SOLICITA_RECUPERACAO";

    String COL_JOB_DATAMAP = "DADOS_JOB";

    String COL_JOB_CLASS = "NOME_CLASSE_JOB";

    String COL_DESCRIPTION = "DESCRICAO";

    // TABLE_GATILHOS columns names
    String COL_TRIGGER_NAME = "NOME_GATILHO";

    String COL_TRIGGER_GROUP = "GRUPO_GATILHO";

    String COL_NEXT_FIRE_TIME = "TEMPO_PROXIMO_DISPARO";

    String COL_PREV_FIRE_TIME = "TEMPO_DISPARO_ANTERIOR";

    String COL_TRIGGER_STATE = "ESTADO_GATILHO";

    String COL_TRIGGER_TYPE = "TIPO_GATILHO";

    String COL_START_TIME = "TEMPO_INICIO";

    String COL_END_TIME = "TEMPO_TERMINO";

    String COL_PRIORITY = "PRIORIDADE";

    String COL_MISFIRE_INSTRUCTION = "INSTRUCAO_FALHA_DISPARO";

    String ALIAS_COL_NEXT_FIRE_TIME = "ALIAS_NXT_FR_TM";

    // TABLE_SIMPLE_GATILHOS columns names
    String COL_REPEAT_COUNT = "CONTADOR_REPETICAO";

    String COL_REPEAT_INTERVAL = "INTERVALO_REPETICAO";

    String COL_TIMES_TRIGGERED = "TEMPOS_ACIONAMENTOS";

    // TABLE_CRON_GATILHOS columns names
    String COL_CRON_EXPRESSION = "EXPRESSAO_CRON";

    // TABLE_BLOB_GATILHOS columns names
    String COL_BLOB = "DADOS_BLOB";

    String COL_TIME_ZONE_ID = "TIME_ZONE_ID";

    // TABLE_FIRED_GATILHOS columns names
    String COL_INSTANCE_NAME = "NOME_INSTANCIA";

    String COL_FIRED_TIME = "TEMPO_DISPARO";

    String COL_SCHED_TIME = "TEMPO_AGENDAMENTO";
    
    String COL_ENTRY_ID = "ID_ENTRADA";

    String COL_ENTRY_STATE = "ESTADO";

    // TABLE_CALENDARIO columns names
    String COL_CALENDAR_NAME = "NOME_CALENDARIO";

    String COL_CALENDAR = "CALENDARIO";

    // TABLE_TRAVAS columns names
    String COL_LOCK_NAME = "NOME_TRAVA";

    // TABLE_TRAVAS columns names
    String COL_LAST_CHECKIN_TIME = "TEMPO_ULTIMO_CHECKIN";

    String COL_CHECKIN_INTERVAL = "INTERVALO_CHECKIN";

    // MISC CONSTANTS
    String DEFAULT_TABLE_PREFIX = "TB_";

    // STATES
    String STATE_WAITING = "WAITING";

    String STATE_ACQUIRED = "ACQUIRED";

    String STATE_EXECUTING = "EXECUTING";

    String STATE_COMPLETE = "COMPLETE";

    String STATE_BLOCKED = "BLOCKED";

    String STATE_ERROR = "ERROR";

    String STATE_PAUSED = "PAUSED";

    String STATE_PAUSED_BLOCKED = "PAUSED_BLOCKED";

    String STATE_DELETED = "DELETED";

    /**
     * @deprecated Whether a trigger has misfired is no longer a state, but 
     * rather now identified dynamically by whether the trigger's next fire 
     * time is more than the misfire threshold time in the past.
     */
    String STATE_MISFIRED = "MISFIRED";

    String ALL_GROUPS_PAUSED = "_$_ALL_GROUPS_PAUSED_$_";

    // TRIGGER TYPES
    /** Simple Trigger type. */
    String TTYPE_SIMPLE = "SIMPLE";

    /** Cron Trigger type. */
    String TTYPE_CRON = "CRON";

    /** Calendar Interval Trigger type. */
    String TTYPE_CAL_INT = "CAL_INT";

    /** Daily Time Interval Trigger type. */
    String TTYPE_DAILY_TIME_INT = "DAILY_I";

    /** A general blob Trigger type. */
    String TTYPE_BLOB = "BLOB";
	
}
