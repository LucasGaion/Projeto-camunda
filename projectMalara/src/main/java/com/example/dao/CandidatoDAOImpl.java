package com.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.model.CandidatoTO;

public class CandidatoDAOImpl implements CandidatoDAO {
    private Connection connection;

    public CandidatoDAOImpl() {
        try {
            // Configurar a conexão com o banco de dados MySQL
            String url = "jdbc:mysql://localhost:3306/candidatos";
            String username = "root";
            String password = "oeste123";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserirCandidato(CandidatoTO candidato) {
        try {
            // Preparar a consulta SQL para inserção do candidato
            String sql = "INSERT INTO candidatos (nome, cpf, telefone, email, endereco, cidade, estado, cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Preencher os parâmetros da consulta com os dados do candidato
            statement.setString(1, candidato.getNome());
            statement.setString(2, candidato.getCpf());
            statement.setString(3, candidato.getTelefone());
            statement.setString(4, candidato.getEmail());
            statement.setString(5, candidato.getEndereco());
            statement.setString(6, candidato.getCidade());
            statement.setString(7, candidato.getEstado());
            statement.setString(8, candidato.getCep());



            // Executar a consulta de inserção
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
