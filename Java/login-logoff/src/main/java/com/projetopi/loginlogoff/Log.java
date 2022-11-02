package com.projetopi.loginlogoff;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    public   String gravaRegistro(String registro, String nomeArq) {
        BufferedWriter saida = null;

        // try-catch para abrir o arquivo
        try {
            saida = new BufferedWriter(new FileWriter(nomeArq, true));
        }
        catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            erro.printStackTrace();
        }

        // try-catch para gravar e fechar o arquivo
        try {
            saida.append(registro + "\n");
            saida.close();
        }
        catch (IOException erro) {
            erro.printStackTrace();
            return  "Erro ao gravar o arquivo";

        }
        return "arquivo gravado com sucesso";
    }
    public  String gravaLog(String textoLog) {
        int contaRegDados = 0;

        // Monta o registro de log
        String corpoLog = textoLog+" - ";
        corpoLog += LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        // Grava o registro de log
        return gravaRegistro(corpoLog, "saveUpLogApplication");
    }
}
