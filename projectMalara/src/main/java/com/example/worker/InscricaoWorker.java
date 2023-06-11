package com.example.worker;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.task.ExternalTaskHandler;

import com.example.dao.CandidatoDAO;
import com.example.dao.CandidatoDAOImpl;
import com.example.model.CandidatoTO;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class InscricaoWorker {
    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://camunda.delivoro.com.br:8882/engine-rest") // URL do servidor Camunda
                .build();

        client.subscribe("armazena-inscricao-no-bancodedados")
                .lockDuration(1000)
                .handler(new InscricaoHandler())
                .open();
    }

    private static class InscricaoHandler implements ExternalTaskHandler {
        public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
            // Obter os dados da inscrição do Camunda
            String nome = externalTask.getVariable("nome");
            String cpf = externalTask.getVariable("cpf");
            String telefone = externalTask.getVariable("telefone");
            String email = externalTask.getVariable("email");
            String endereco = externalTask.getVariable("endereco");
            String cidade = externalTask.getVariable("cidade");
            String estado = externalTask.getVariable("estado");
            String cep = externalTask.getVariable("cep");

            // Chamar o método para salvar os dados no banco de dados
            CandidatoDAO candidatoDAO = new CandidatoDAOImpl();
            CandidatoTO candidato = new CandidatoTO(nome, cpf, telefone, email, endereco, cidade, estado, cep);
            candidatoDAO.inserirCandidato(candidato);

            // Exibir os dados inseridos antes de enviar o e-mail
            System.out.println("--- Dados inseridos com sucesso ---");
            System.out.println("Nome: " + nome);
            System.out.println("CPF: " + cpf);
            System.out.println("Telefone: " + telefone);
            System.out.println("Email: " + email);
            System.out.println("Endereco: " + endereco);
            System.out.println("Cidade: " + cidade);
            System.out.println("Estado: " + estado);
            System.out.println("CEP: " + cep);
            System.out.println("\n");

            // Enviar e-mail para o candidato
            sendEmail(nome, email);

            // Completar a tarefa externa do Camunda
            externalTaskService.complete(externalTask);
        }

        private void sendEmail(String nome, String email) {
            // Configurar as propriedades do servidor de e-mail
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.office365.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // Configurar as credenciais de autenticação
            String username = "universidadelucas@outlook.com";
            String password = "Camunda@123";

            // Criar uma sessão de e-mail autenticada
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                // Criar a mensagem de e-mail
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject("Confirmação de Inscrição");
                message.setText("Olá, tudo bem? " + nome + ",\n\nSua inscrição foi realizada com sucesso!\nBem Vindo á Universidade!");


                // Enviar o e-mail
                Transport.send(message);
                System.out.println("--- Enviado email de confirmação de inscrição ---");
                System.out.println("E-mail enviado para: " + email);
                System.out.println("\n");

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
