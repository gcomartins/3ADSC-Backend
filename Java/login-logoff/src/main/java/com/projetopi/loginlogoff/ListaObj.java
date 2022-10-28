package com.projetopi.loginlogoff;

import ch.qos.logback.core.read.ListAppender;
import com.projetopi.loginlogoff.financas.Financas;
import com.projetopi.loginlogoff.financas.despesa.Despesa;
import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import com.projetopi.loginlogoff.financas.receita.ReceitaRepository;
import net.bytebuddy.asm.Advice;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ListaObj <T> {

    // 01) Declarar vetor de int:
    // É inicializado no construtor
    private T[] vetor;

    // 02) Criar atributo nroElem:
    // Tem dupla função: representa quantos elementos foram adicionado no vetor
    // Também o índice de onde será adicionado o próximo elemento
    private int nroElem;

    // 03) Criar Construtor:
    // Recebe como argumento o tamanho máximo do vetor
    // Cria vetor com tamanho máximo informado
    // Inicializa nroElem
    public ListaObj(int tamanho) {
        vetor = (T[]) new Object[tamanho];
        nroElem = 0;
    }

    // 04) Método adiciona:
    // Recebe o elemento a ser adicionado na lista
    // Se a lista estiver cheia usar IllegalStateException();
    public void adiciona(T elemento) {
        if (nroElem >= vetor.length) {
            throw new IllegalStateException();
        } else {
            vetor[nroElem++] = elemento;
        }
    }

    // 05) Método busca:
    // Recebe o elemento a ser procurado na lista
    // Retorna o índice do elemento, se for encontrado
    // Retorna -1 se não encontrou
    public int busca(T elementoBuscado) {
        for (int i = 0; i < nroElem; i++) {
            if (vetor[i].equals(elementoBuscado)) {
                return i;
            }
        }
        return -1;
    }

    // 06) Método removePeloIndice:
    // Recebe o índice do elemento a ser removido
    // Se o índice for inválido, retorna false
    // Se removeu, retorna true
    public boolean removePeloIndice(int indice) {
        if (indice < 0 || indice >= nroElem) {
            System.out.println("\nÍndice inválido!");
            return false;
        }

        // Loop para "deslocar para a esquerda" os elementos do vetor
        // sobrescrevendo o elemento removido
        for (int i = indice; i < nroElem - 1; i++) {
            vetor[i] = vetor[i + 1];
        }

        nroElem--;
        return true;
    }

    // 07) Método removeElemento
    // Recebe um elemento a ser removido
    // Utiliza os métodos busca e removePeloIndice
    // Retorna false, se não encontrou o elemento
    // Retorna true, se encontrou e removeu o elemento
    public boolean removeElemento(T elementoARemover) {
        return removePeloIndice(busca(elementoARemover));
    }

    // 08) Método getTamanho
    // Retorna o tamanho da lista
    public int getTamanho() {
        return nroElem;
    }

    // 09) Método getElemento
    // Recebe um índice e retorna o elemento desse índice
    // Se o índice for inválido, retorna null
    public T getElemento(int indice) {
        if (indice < 0 || indice >= nroElem) {
            return null;
        } else {
            return vetor[indice];
        }
    }

    // 10) Método limpa
    // Limpa a lista
    public void limpa() {
        nroElem = 0;
    }

    // 11) Método exibe:
    // Exibe os elementos da lista
    public void exibe() {
        if (nroElem == 0) {
            System.out.println("\nA lista está vazia.");
        } else {
            System.out.println("\nElementos da lista:");
            for (int i = 0; i < nroElem; i++) {
                System.out.println(vetor[i]);
            }
        }
    }

    // Get do vetor
    // Não retirar, é usado nos testes
    public T[] getVetor() {
        return vetor;
    }

    public ResponseEntity<String> gravaArquivoCsvObjetivo(ListaObj<Objetivo> listaObjetivo,
                                                          ListaObj<Receita> listaReceita,
                                                          ListaObj<Despesa>listaDespesa,
                                                          String nomeArq) {
        FileWriter arq = null; // objeto que representa o arquivo de gravação
        Formatter saida = null; // objeto usado para escrever no arquivo
        Boolean deuRuim = false;
        nomeArq += ".csv";
        // Bloco que abre o arquivo ;
        try {
            arq = new FileWriter(nomeArq); // abrindo arquivo
            saida = new Formatter(arq); // cria objeto saida associando ao arquivo
        } catch (IOException e) {
            return ResponseEntity.status(400).body("Erro ao abrir o arquivo");
        }

        // bloco que grava o arquivo
        try {
            // aqui nesse saida .format colocar o nome dos campos do objetivo
            saida.format("%s\n%s;%s;%s;%s;%s;%s;%s;%s\n","Objetivos","codigo","nome","categoria","descricao","valor","valor atual","data criacao","data final");
            for (int i = 0; i < listaObjetivo.getTamanho(); i++) {
                Objetivo o = listaObjetivo.getElemento(i);
                //aqui colocar o que do objeto vai ser exibido
                saida.format("%d;%s;%s;%s;%.2f;%.2f;%s;%s\n",o.getCodigo(),o.getNome(),o.getCategoria(),
                        o.getDescricao(),o.getValor(),o.getValorAtual(),o.getData(),o.getDataFinal());
            }
            // aqui nesse saida .format colocar o nome dos campos do receita
            saida.format("%s\n%s;%s;%s;%s;%s;%s;%s;%s\n", "Receitas","codigo","nome","categoria","descricao",
                    "valor","recorrente","frequencia","data criacao");
            for (int i = 0; i < listaReceita.getTamanho(); i++) {
                Receita r = listaReceita.getElemento(i);
                //aqui colocar o que da receita  vai ser exibido
                saida.format("%d;%s;%s;%s;%.2f;%b;%d;%s\n", r.getCodigo(), r.getNome(), r.getCategoria(),
                        r.getDescricao(), r.getValor(), r.isRecorrente(),r.getFrequencia(),r.getData());
            }
            saida.format("Despesas\n%s;%s;%s;%s;%s;%s;%s;%s\n","Codigo","nome","Categoria","Descricao","Valor","Esta Pago?",
                    "Quantidade de parcelas","Data");
            for (int i = 0; i < listaDespesa.getTamanho(); i++){
                Despesa d = listaDespesa.getElemento(i);
             saida.format("%d;%s;%s;%s;%.2f;%b;%d;%s\n", d.getCodigo(), d.getNome(), d.getCategoria(),
                        d.getDescricao(), d.getValor(), d.isPago(),d.getQtdParcelas(),d.getData());
            }

        } catch (FormatterClosedException e) {
            return ResponseEntity.status(400).body("erro ao gravar arquivo");

        } finally {
            saida.close();
            try {
                arq.close();
                return ResponseEntity.status(200).body("arquivo gerado com sucesso");
            } catch (IOException e) {
                return ResponseEntity.status(400).body("Erro ao fechar o arquivo");

            }
        }
    }

    public void selectionSortOtimizado(ListaObj<Receita> listaReceitas) {
        int indMenor;

        for (int i = 0; i < listaReceitas.getTamanho() - 1; i++) {
            indMenor = i;
            for (int j = i + 1; j < listaReceitas.getTamanho(); j++) {
                if (listaReceitas.getElemento(j).getValor() < listaReceitas.getElemento(indMenor).getValor()) {
                    indMenor = j;
                }
            }
            double aux;
            aux = listaReceitas.getElemento(i).getValor();
            listaReceitas.getElemento(i).setValor(listaReceitas.getElemento(indMenor).getValor());
            listaReceitas.getElemento(indMenor).setValor(aux);
        }
    }


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


    public  String gravaArquivoTxt(ListaObj<Objetivo> listaObjetivo,
                                   ListaObj<Receita> listaReceita,
                                   ListaObj<Despesa> listaDespesa,
                                   String nomeArq) {
        int contaRegDados = 0;

        // Monta o registro de header
        String header = "00FINANCAS";
        header += LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        header += "01";
        // Grava o registro de header
        gravaRegistro(header, nomeArq);

        // Monta e grava os registros de corpo
        String corpo;
        for (int i =0; i < listaObjetivo.getTamanho(); i++) {
            Objetivo objetivoAtual = listaObjetivo.getElemento(i);
            corpo = "02";
            corpo += String.format("%-50.50s", objetivoAtual.getNome());
            corpo += String.format("%-100.100s", objetivoAtual.getDescricao());
            corpo += String.format("%011.2f", objetivoAtual.getValor());
            corpo += String.format("%-10.10s", objetivoAtual.getData());
            // valor numérico não se coloca o -, para alinhar para a direita
            // e é recomendável preencher o valor com zeros à esquerda
            corpo += String.format("%05.2f", objetivoAtual.getValorAtual());
            corpo += String.format("%-10.10s", objetivoAtual.getDataFinal());
//            corpo += String.format("%-40.40s", objetivoAtual.getDataFinal());
            contaRegDados++;
            gravaRegistro(corpo, nomeArq);
        }
        for (int i = 0; i < listaReceita.getTamanho(); i++){
            Receita receitaAtual = listaReceita.getElemento(i);
            corpo = "03";
            corpo += String.format("%-50.50s", receitaAtual.getNome());
            corpo += String.format("%-100.100s", receitaAtual.getDescricao());
            corpo += String.format("%011.2f", receitaAtual.getValor());
            corpo += String.format("%-10.10s", receitaAtual.getData());
            corpo += String.format("%-20.20s",receitaAtual.getCategoria());
            corpo += String.format("%-5.5s",receitaAtual.isRecorrente());
            corpo += String.format("%01d",receitaAtual.getFrequencia());
            contaRegDados++;
            gravaRegistro(corpo,nomeArq);
        }

        for (int i =0; i < listaDespesa.getTamanho(); i ++){
            Despesa despesaAtual = listaDespesa.getElemento(i);
            corpo = "04";
            corpo += String.format("%-50.50s", despesaAtual.getNome());
            corpo += String.format("%-100.100s", despesaAtual.getDescricao());
            corpo += String.format("%011.2f", despesaAtual.getValor());
            corpo += String.format("%-10.10s", despesaAtual.getData());
            corpo += String.format("%-20.20s",despesaAtual.getCategoria());
            corpo += String.format("%-5.5s",despesaAtual.isPago());
            corpo += String.format("%05d",despesaAtual.getQtdParcelas());
            gravaRegistro(corpo,nomeArq);
        }

        // Monta e grava o registro de trailer
        String trailer = "01";
        trailer += String.format("%010d", contaRegDados);
        return gravaRegistro(trailer, nomeArq);
    }

}


