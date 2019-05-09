# Disparador-RS

<p align="justify">Projeto de chamada de tarefas agendadas no ecossistema de micro serviços distribuídos.</p>

## Descrição Geral

<p align="justify">O projeto é uma abstração do framework Quart'z que é um serviço de agendamento de tarefas que pode ser integrado, ou utilizado virtualmente, em qualquer aplicação Java SE ou Java EE. A ferramenta pode ser utilizada para criar agendas que executam milhares de tarefas, que são definidas utilizando componentes padrão da plataforma Java, que são codificados para suprir as necessidades da aplicação. O Quartz Scheduler fornece diversos recursos corporativos, como suporte a transações JTA ou clusterização.</p>
<p align="justify">A ideia desse resource server é se tornar um centralizador de informações e disparo de tarefas agendadas, podendo fornecer estatísticas dos serviços, tomar ações de registro e notificação em caso de falhas das tarefas e possibilitar uma forma de acoplar tarefas no ecosistema de micro serviços corporativos.</p>

## Modelo de Dados

<p align="justify">O Scheduler é o componente principal do Quartz e é o responsável por gerenciar a execução de jobs. A partir dele, o desenvolvedor pode agendar, iniciar e parar as execuções. O trabalho realizado nesse resource server é uma abstração do funcionamento do framework assim como foi feito no Autorization Server. Foi extraido e adaptado sua composição de entidades formando um modelo de dados que é exibido a seguir:</p>

<p align="center">
  <img src="https://github.com/wescleysrn/mestradounb/blob/master/imagens/disparador/bancoModelo.png">
</p>

## Componente de Ligação

<p align="justify">Um novo rest controller abstrato foi introduzido nos projetos comuns para gerar um ponto de conexão para chamada pelo resource disparador. Para isso basta criar um rest controller que estenda JobController e sobrescrever o método executarTarefaAgendada conforme exemplo abaixo:</p>

<p align="center">
  <img src="https://github.com/wescleysrn/mestradounb/blob/master/imagens/disparador/jobController.png">
</p>

<p align="justify">Neste controller pode-se injetar um mediator que realiza a tarefa de fato e chama-lo.</p>

<p align="justify">A seguir é descrito os dados do JobDTO, que é basicamente a forma de comunicação de retorno para o resource disparador.</p>

<p align="center">
  <img src="https://github.com/wescleysrn/mestradounb/blob/master/imagens/disparador/jobDTO.png">
</p>

<p align="justify">A data inicio e termino assim como a mensagem é auto explicativo, a observação é somente para o atributo parâmetros que diz respeito a uma relação do resource server disparador com o resource renderizador de documentos. Trata-se de uma forma de criar modelo de documento ou template que será enviado por e-mail no momento da execução da tarefa, o disparador possui um modelo padrão em formato HTML que fornece os dados básicos da tarefa, mas se houver a necessidade de customizar a mensagem de execução da tarefa podemos incluir no registro da tarefa um código de modelo a ser renderizado, caso este possua parâmetros para serem preenchidos estes devem ser incluídos no atributo parametros do JobDTO.</p>

## Documentação da API

<p align="justify">Uma vez preparado o resource server que irá executar a tarefa agendada, deve-se fornecer ao disparador as informações de disparo. Isso futuramente será realizado por uma aplicação web que irá contemplar outras funcionalidades, como nosso novo controle de permissões e controle do Autorizador Oauth2. Por hora podemos consumir a API do Disparador de Tarefas, acessível pelo contexto falado no inicio:</p>

<p align="center">
  <img src="https://github.com/wescleysrn/mestradounb/blob/master/imagens/disparador/swagger001.png">
</p>

<p align="justify">Temos serviços da api disparador para incluir e remover tarefas agendadas para execução, mas o objetivo deste projeto é ser uma central de informações de execução de tarefas, podendo fornecer informações desde tempo médio de execução, até relatórios periódicos de tarefas. Como o projeto possui uma base de dados customizada do framework Quartz as possibilidades são infinitas, pois essa base já armazena informações históricas podendo ser customizadas.</p>

<p align="justify">A seguir é mostrado um exemplo de inclusão de tarefa:</p>

<p align="center">
  <img src="https://github.com/wescleysrn/mestradounb/blob/master/imagens/disparador/swagger002.png">
</p>

## Script de Inclusão de Tarefa

<p align="justify">A demonstração de consumo de API mostrada anteriormente é uma forma de inserir tarefas, mas é possível também inserir no banco de dados pelo script abaixo:</p>

<p align="center">
  <img src="https://github.com/wescleysrn/mestradounb/blob/master/imagens/disparador/tbRegistro.png">
</p>

<p align="justify">Como observação aqui, vale lembrar que a URL do serviço que irá executar a tarefa deve ser complementada com "/job/tarefa" que é o identificador da classe abstrata.</p>

<p align="justify">E por fim e mais importante a expressão cron que determina a periodicidade de execução, pode-se ser criada por meio de serviços web que facilitam sua criação como por exemplo o site abaixo que fornece uma interface amigável para determinar quando será executado a tarefa:</p>

http://www.cronmaker.com/

<p align="justify">Com essas informações as equipes podem facilmente acoplar um serviço rest dos projetos resource server para ser consumido pelo disparador na periodicidade determinada.</p>

## Trabalhos futuros

<p align="justify">Esse projeto torna possível acoplar informações de tarefas agendadas e pode ser utilizado como fonte para dashboard e relatórios de um sistema web.</p>
