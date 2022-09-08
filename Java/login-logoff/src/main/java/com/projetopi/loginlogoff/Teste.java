package com.projetopi.loginlogoff;
import java.sql.Date;


public class Teste {
    public static void main(String[] args) {
        Date dataDespesa = new Date(2002, 06, 03);
        Despesa despesa = new Despesa(10, "CPFL", "Conta de Luz", 70.0, dataDespesa, "Contas Fixas", true, 1);
        Receita receita = new Receita(20, "Salário Agosto", "Salário mensal", 2000.0, dataDespesa, "Receitas fixas", true, 12);
        Objetivo objetivo = new Objetivo(30, "Viagem", "Viajar para Bahia", 5000.0, dataDespesa, "Objetivo", 3000.0, dataDespesa);

        Usuario u1 = new Usuario( "Marcos", "marcos@gmail.com", "12345", 7000.0, "teste123",dataDespesa);

        despesa.modificaSaldo(u1);
        u1.pegueSaldo();
        System.out.println(u1);
    }
}