package com.projetopi.loginlogoff;
import com.projetopi.loginlogoff.financas.objetivo.ObjetivoSubject;
import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.mensageria.MensageriaObserver;
import com.projetopi.loginlogoff.usuario.Usuario;

import java.sql.Date;


public class Teste {
    public static void main(String[] args) {
//        Date dataDespesa = new Date(2002, 06, 03);
//        Despesa despesa = new Despesa(10, "CPFL", "Conta de Luz", 70.0, dataDespesa, "Contas Fixas", true, 1);
//        Receita receita1 = new Receita(20, "Salário Agosto", "Salário mensal", 7000.0, dataDespesa, "Receitas fixas", true, 12);
//        Receita receita2 = new Receita(21, "Salário Setembro", "Salário mensal", 5000.0, dataDespesa, "Receitas fixas", true, 2);
//        Receita receita3 = new Receita(22, "Salário out", "Salário mensal", 450.0, dataDespesa, "Receitas fixas", true, 2);
//        Receita receita4 = new Receita(23, "Salário nov", "Salário mensal", 8000.0, dataDespesa, "Receitas fixas", true, 2);
//        Objetivo objetivo = new Objetivo(30, "Viagem", "Viajar para Bahia", 5000.0, dataDespesa, "Objetivo", 3000.0, dataDespesa);
//
//        ListaObj<Receita> listaObj = new ListaObj<>(5);
//        listaObj.adiciona(receita1);
//        listaObj.adiciona(receita2);
//        listaObj.adiciona(receita3);
//        listaObj.adiciona(receita4);
//
//        listaObj.selectionSortOtimizado(listaObj);
//        listaObj.exibe();
//
//        Usuario u1 = new Usuario( "Marcos", "marcos@gmail.com", "12345", 7000.0, "teste123",dataDespesa);
//
//        despesa.modificaSaldo(u1);
//        u1.pegueSaldo();
//        System.out.println(u1);

        ObjetivoSubject objetivoSubject = new ObjetivoSubject(100.0, 200.0);
        objetivoSubject.setValorInicialObjetivo(230.0);
        objetivoSubject.notificaObservadores();
    }
}