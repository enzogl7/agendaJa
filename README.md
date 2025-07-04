# AgendaJá

<h3>*O AgendaJá surgiu como uma ideia de SaaS e está com sua produção PAUSADA porém está muito completo. Estou deixando o repositório público apenas por PORTFÓLIO.</h3>

---
<h3 align="center">Visão geral</h3>

<p align="center">
   <img src="src/main/resources/static/assets/gifs/agendaja-gif.gif">
</p>
<h3 align="center">Stripe - como funciona no projeto</h3>

<p align="center">
   <img src="src/main/resources/static/assets/gifs/stripe-payment.gif">
</p>

## Tecnologias Utilizadas

### Frontend
* HTML5
* CSS3
* JavaScript (ES6+)
* Tailwind CSS
* Bootstrap 5
* Thymeleaf

### Backend
* Java 17+
* Spring Boot 3.x
    * Spring MVC
    * Spring Data JPA
    * Spring Security

### Banco de Dados
* PostgreSQL

### Outras Ferramentas e Bibliotecas
* **Stripe:** Integrei a Stripe para o cliente poder realizar pagamento da assinatura recorrente. A aplicação também possui um webhook do Stripe para poder monitorar se a assinatura foi paga ou não mensalmente, cortando ou reativando o plano do usuário.
* **Thymeleaf:** Engine de templates para renderização de páginas no servidor.
* **Spring Mail:** Para envio de e-mails transacionais (confirmações, notificações) via SMTP.
* **Lombok:** Para redução de código boilerplate em classes Java.
* **SweetAlert2 (Swal):** Para exibição de alertas e modais customizados no frontend.
* **jQuery:** Utilizado para manipulação do DOM e requisições AJAX.
* **JSON Web Tokens (JWT):** Para autenticação e autorização segura baseada em tokens.

---

## Para Quem é o AgendaJá?

* **Prestadores de Serviço Autônomos e Pequenos Negócios:** Profissionais que desejam uma ferramenta eficiente para gerenciar agendamentos, pagamentos, clientes e ter uma vitrine online profissional.
* **Clientes Finais:** Pessoas que buscam uma forma prática e moderna de agendar serviços com seus prestadores favoritos.

## Principais Funcionalidades

### Para Prestadores de Serviço:

* **Página de Negócios Personalizada:** Crie uma vitrine online para seu negócio com uma URL exclusiva (ex: `agendaja.com/negocio/seu-negocio`) para divulgar seus serviços. (Plano Premium)
* **Gestão de Agendamentos Simplificada:** Visualize, confirme ou cancele agendamentos com facilidade.
* **Gestão de Clientes:** Mantenha um cadastro organizado dos seus clientes.
* **Catálogo de Serviços:** Adicione, edite e remova os serviços que você oferece, definindo preços e durações.
* **Configuração de Horários:** Defina seus horários de trabalho, pausas e dias de folga para que apenas horários realmente disponíveis apareçam para os clientes.
* **Recebimento via PIX Facilitado:** Configure qualquer tipo de chave PIX (CPF/CNPJ, e-mail ou telefone). Para cada agendamento pago, a plataforma automaticamente gera um QR Code e um código PIX "Copia e Cola", simplificando o processo de pagamento para seus clientes diretamente na página do seu negócio.
* **Dashboard Completo:**
    * Visão geral dos últimos agendamentos.
    * Acompanhamento da receita mensal gerada.
    * Lista de todos os clientes.
* **Notificações:** Seja avisado sobre novos agendamentos realizados.

### Para Clientes Finais:

* **Agendamento Online Facilitado:** Agende serviços de forma rápida e intuitiva diretamente na página do prestador.
* **Pagamento Seguro via PIX:** Realize o pagamento do serviço no momento do agendamento utilizando QR Code ou código "Copia e Cola".
* **Dashboard Pessoal:** Acesse seu histórico de agendamentos realizados na plataforma.
* **Notificações por E-mail:** Receba confirmações e notificações de cancelamento dos seus agendamentos.

### Geral:

* **Dois Perfis de Acesso:** Sistema de cadastro e login diferenciado para "Prestador" e "Usuário (Cliente)".

## Como Funciona (Fluxo Básico)

1.  **Prestador:** Cadastra-se na plataforma, configura seu perfil, serviços, horários, chave PIX e página do negócio.
2.  **Divulgação:** O prestador compartilha a URL da sua página AgendaJá com seus clientes ou em suas redes sociais.
3.  **Cliente:** Acessa a página do prestador, escolhe o serviço, seleciona um horário disponível e realiza o agendamento, efetuando o pagamento via PIX (QR Code ou Copia e Cola).
4.  **Notificações:** O prestador é notificado do novo agendamento. O cliente recebe a confirmação por e-mail.
5.  **Gestão:** O prestador gerencia o agendamento (confirmando ou, se necessário, cancelando e notificando o cliente). O cliente pode consultar seus agendamentos em seu próprio dashboard.

---
