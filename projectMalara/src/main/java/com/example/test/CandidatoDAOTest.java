package com.example.test;

import com.example.dao.CandidatoDAO;
import com.example.dao.CandidatoDAOImpl;
import com.example.model.CandidatoTO;

public class CandidatoDAOTest {
    public static void main(String[] args) {
        testInserirCandidato();
    }

    public static void testInserirCandidato() {
        CandidatoDAO candidatoDAO = new CandidatoDAOImpl();
        CandidatoTO candidato = new CandidatoTO("João da Silva", "12345678900", "1234567890", "joao@example.com",
                "Rua A, 123", "Cidade A", "Estado A", "12345-678");
        candidatoDAO.inserirCandidato(candidato);
        System.out.println("Candidato inserido com sucesso!");
    }
}
