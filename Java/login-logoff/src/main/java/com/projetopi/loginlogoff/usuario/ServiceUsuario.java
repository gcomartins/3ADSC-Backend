package com.projetopi.loginlogoff.usuario;

import com.projetopi.loginlogoff.ListaObj;
import com.projetopi.loginlogoff.financas.despesa.Despesa;
import com.projetopi.loginlogoff.financas.despesa.DespesaController;
import com.projetopi.loginlogoff.financas.despesa.DespesaRepository;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import com.projetopi.loginlogoff.financas.objetivo.ObjetivoController;
import com.projetopi.loginlogoff.financas.objetivo.ObjetivoRepository;
import com.projetopi.loginlogoff.financas.RelatorioMensal;
import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.financas.receita.ReceitaController;
import com.projetopi.loginlogoff.financas.receita.ReceitaRepository;
import net.bytebuddy.asm.Advice;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcOperationsExtensionsKt;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class ServiceUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ObjetivoRepository objetivoRepository;
    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private DespesaRepository despesaRepository;
    @Autowired
    private ObjetivoController objetivoController;
    @Autowired
    private ReceitaController receitaController;
    @Autowired
    private DespesaController despesaController;


    public Boolean atualizarUsuario(String emailAntigo, String senhaAntiga,
                                    Usuario usuarioAtualizado) {
        Boolean isAtualizado = false;
        Usuario usuario = usuarioRepository.findByEmailAndSenha(emailAntigo, senhaAntiga);
        if (usuario == null) return isAtualizado;
        if (usuarioAtualizado.getSenha().equals(senhaAntiga) &&
                usuarioAtualizado.getEmail().equals(emailAntigo) &&
                usuarioAtualizado.getNome().equals(usuario.getNome())) {
            return isAtualizado;
        }
        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setSenha(usuarioAtualizado.getSenha());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setDataNascimento(usuarioAtualizado.getDataNascimento());
        usuarioRepository.save(usuario);
        isAtualizado = true;
        return isAtualizado;
    }

    public String gerarCsv(int idUsuario, String nomeArquivo) {
        List<Objetivo> objetivos = objetivoRepository.findByUsuarioIdOrderByData(idUsuario);
        List<Receita> receitas = receitaRepository.findByUsuarioIdOrderByData(idUsuario);
        List<Despesa> despesas = despesaRepository.findByUsuarioIdOrderByData(idUsuario);
        // aqui pegando o tamanho dos itens para passar como parametro quando transforma-los em vetor
        // criando objetos do tipo ListObj
        ListaObj listaReceitas = new ListaObj<>(receitaRepository.countByUsuarioId(idUsuario));
        ListaObj listaObjetos = new ListaObj<>(objetivoRepository.countByUsuarioId(idUsuario));
        ListaObj listaDespesa = new ListaObj<>(despesaRepository.countByUsuarioId(idUsuario));
        for (Receita receitaAtual : receitas) {
            listaReceitas.adiciona(receitaAtual);
        }
        for (Objetivo objetivoAtual : objetivos) {
            listaObjetos.adiciona(objetivoAtual);
        }

        for (Despesa despesaAtual : despesas) {
            listaDespesa.adiciona(despesaAtual);
        }
        // gravando as informações nos arquivos
        gravaArquivoCsvObjetivo(listaObjetos, listaReceitas, listaDespesa, nomeArquivo);
        return gravaArquivoCsvObjetivo(listaObjetos, listaReceitas, listaDespesa, nomeArquivo);
    }

    public String gerarTxt(int idUsuario, String nomeArquivo) {
        List<Objetivo> objetivos = objetivoRepository.findByUsuarioIdOrderByData(idUsuario);
        List<Receita> receitas = receitaRepository.findByUsuarioIdOrderByData(idUsuario);
        List<Despesa> despesas = despesaRepository.findByUsuarioIdOrderByData(idUsuario);

        ListaObj listaObjetivos = new ListaObj<>(objetivoRepository.countByUsuarioId(idUsuario));
        for (Objetivo objetivoAtual : objetivos) {
            listaObjetivos.adiciona(objetivoAtual);
        }
        ListaObj listaReceitas = new ListaObj<>(receitaRepository.countByUsuarioId(idUsuario));
        for (Receita receitaAtual : receitas) {
            listaReceitas.adiciona(receitaAtual);
        }
        ListaObj listaDespesas = new ListaObj<>(despesaRepository.countByUsuarioId(idUsuario));
        for (Despesa despesaAtual : despesas) {
            listaDespesas.adiciona(despesaAtual);
        }

        return gravaArquivoTxt(listaObjetivos, listaReceitas, listaDespesas, nomeArquivo);


    }

    public String gravaArquivoCsvObjetivo(ListaObj<Objetivo> listaObjetivo,
                                          ListaObj<Receita> listaReceita,
                                          ListaObj<Despesa> listaDespesa,
                                          String nomeArq) {
        if (listaDespesa.getTamanho() <= 0 && listaReceita.getTamanho() <= 0 && listaObjetivo.getTamanho() <= 0)
            return "Nenhuma informacao a colocar no arquivo";
        FileWriter arq = null; // objeto que representa o arquivo de gravação
        Formatter saida = null; // objeto usado para escrever no arquivo
        Boolean deuRuim = false;
        nomeArq += ".csv";
        Boolean gravouAlgo = false;
        // Bloco que abre o arquivo ;
        try {
            arq = new FileWriter(nomeArq); // abrindo arquivo
            saida = new Formatter(arq); // cria objeto saida associando ao arquivo
        } catch (IOException e) {
            return "Erro ao abrir o arquivo";
        }

        // bloco que grava o arquivo
        try {
            // aqui nesse saida .format colocar o nome dos campos do objetivo
            if (listaObjetivo.getTamanho() > 0) {
                saida.format("%s\n%s;%s;%s;%s;%s;%s;%s;%s\n", "Objetivos", "codigo", "nome", "categoria", "descricao", "valor", "valor atual", "data criacao", "data final");
                for (int i = 0; i < listaObjetivo.getTamanho(); i++) {
                    Objetivo o = listaObjetivo.getElemento(i);
                    //aqui colocar o que do objeto vai ser exibido
                    saida.format("%d;%s;%s;%s;%.2f;%.2f;%s;%s\n", o.getCodigo(), o.getNome(), o.getCategoria(),
                            o.getDescricao(), o.getValor(), o.getValorAtual(), o.getData(), o.getDataFinal());
                }
                gravouAlgo = true;
            }
            // aqui nesse saida .format colocar o nome dos campos do receita
            if (listaReceita.getTamanho() > 0) {
                saida.format("%s\n%s;%s;%s;%s;%s;%s;%s;%s\n", "Receitas", "codigo", "nome", "categoria", "descricao",
                        "valor", "recorrente", "frequencia", "data criacao");
                for (int i = 0; i < listaReceita.getTamanho(); i++) {
                    Receita r = listaReceita.getElemento(i);
                    //aqui colocar o que da receita  vai ser exibido
                    saida.format("%d;%s;%s;%s;%.2f;%b;%d;%s\n", r.getCodigo(), r.getNome(), r.getCategoria(),
                            r.getDescricao(), r.getValor(), r.isRecorrente(), r.getFrequencia(), r.getData());
                }
                gravouAlgo = true;
            }
            if (listaDespesa.getTamanho() > 0) {
                saida.format("Despesas\n%s;%s;%s;%s;%s;%s;%s;%s\n", "Codigo", "nome", "Categoria", "Descricao", "Valor", "Esta Pago?",
                        "Quantidade de parcelas", "Data");
                for (int i = 0; i < listaDespesa.getTamanho(); i++) {
                    Despesa d = listaDespesa.getElemento(i);
                    saida.format("%d;%s;%s;%s;%.2f;%b;%d;%s\n", d.getCodigo(), d.getNome(), d.getCategoria(),
                            d.getDescricao(), d.getValor(), d.isPago(), d.getQtdParcelas(), d.getData());
                }
                gravouAlgo = true;
            }

        } catch (FormatterClosedException e) {
            return "erro ao gravar arquivo";

        } finally {
            saida.close();
            try {
                arq.close();
                if (!gravouAlgo) return "arquivo sem informacoes";
                return "arquivo gerado com sucesso";
            } catch (IOException e) {
                return "Erro ao fechar o arquivo";

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


    public String gravaRegistro(String registro, String nomeArq, Boolean isUpload) {
        BufferedWriter saida = null;

        // try-catch para abrir o arquivo
        try {
            saida = new BufferedWriter(new FileWriter(nomeArq, true));
        } catch (IOException erro) {
            erro.printStackTrace();
        }

        // try-catch para gravar e fechar o arquivo
        try {
            saida.append(registro + "\n");
            saida.close();
        } catch (IOException erro) {
            erro.printStackTrace();
            return "Erro ao gravar o arquivo";

        }
        return "arquivo gravado com sucesso";
    }


    public String gravaArquivoTxt(ListaObj<Objetivo> listaObjetivo,
                                  ListaObj<Receita> listaReceita,
                                  ListaObj<Despesa> listaDespesa,
                                  String nomeArq) {
        int contaRegDados = 0;
        if (listaDespesa.getTamanho() <= 0 && listaReceita.getTamanho() <= 0 && listaObjetivo.getTamanho() <= 0)
            return "Nenhuma informacao a colocar no arquivo";

        // Monta o registro de header
        String header = "00FINANCAS";
        header += LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        header += "01";
        // Grava o registro de header
        gravaRegistro(header, nomeArq,false);

        // Monta e grava os registros de corpo
        String corpo;

        for (int i = 0; i < listaObjetivo.getTamanho(); i++) {
            Objetivo objetivoAtual = listaObjetivo.getElemento(i);
            corpo = "02";
            corpo += String.format("%-50.50s", objetivoAtual.getNome()); //nome 50 espacos
            corpo += String.format("%-100.100s", objetivoAtual.getDescricao());// descricao 100 espacos
            corpo += String.format("%011.2f", objetivoAtual.getValor()); //valor 11 espacos
            corpo += String.format("%-10.10s", objetivoAtual.getData()); // data 10 espacos
            corpo += String.format("%-20.20s", objetivoAtual.getCategoria()); // categoria  20 espacos
            // valor numérico não se coloca o -, para alinhar para a direita
            // e é recomendável preencher o valor com zeros à esquerda
            corpo += String.format("%011.2f", objetivoAtual.getValorAtual()); //valor atual 11 espacos
            corpo += String.format("%-10.10s", objetivoAtual.getDataFinal()); //data final 10 espacos
            contaRegDados++;
            gravaRegistro(corpo, nomeArq, false);
        }
        for (int i = 0; i < listaReceita.getTamanho(); i++) {
            Receita receitaAtual = listaReceita.getElemento(i);
            corpo = "03";
            corpo += String.format("%-50.50s", receitaAtual.getNome()); // nome 50
            corpo += String.format("%-100.100s", receitaAtual.getDescricao());// descricao 100
            corpo += String.format("%011.2f", receitaAtual.getValor());// valor 11
            corpo += String.format("%-10.10s", receitaAtual.getData()); //data 10
            corpo += String.format("%-20.20s", receitaAtual.getCategoria()); // categoria 20
            corpo += String.format("%-5.5s", receitaAtual.isRecorrente()); //is recorrente 5
            corpo += String.format("%02d", receitaAtual.getFrequencia()); // frequencia 2
            contaRegDados++;
            gravaRegistro(corpo, nomeArq,false);
        }

        for (int i = 0; i < listaDespesa.getTamanho(); i++) {
            Despesa despesaAtual = listaDespesa.getElemento(i);
            corpo = "04";
            corpo += String.format("%-50.50s", despesaAtual.getNome()); // nome 50
            corpo += String.format("%-100.100s", despesaAtual.getDescricao()); // descricao 100
            corpo += String.format("%011.2f", despesaAtual.getValor()); //valor 11
            corpo += String.format("%-10.10s", despesaAtual.getData()); //data 10
            corpo += String.format("%-20.20s", despesaAtual.getCategoria()); //categoria 20
            corpo += String.format("%-5.5s", despesaAtual.isPago()); //is pago 5
            corpo += String.format("%03d", despesaAtual.getQtdParcelas());
            gravaRegistro(corpo, nomeArq,false);
        }

        // Monta e grava o registro de trailer
        String trailer = "01";
        trailer += String.format("%010d", contaRegDados);
        return gravaRegistro(trailer, nomeArq,false);
    }

    public String leArquivoTxt(String nomeArq, int idUsuario) {
        BufferedReader entrada = null;
        String registro, tipoRegistro;
        //dados Objetivo
        String nomeObjetivo, descricaoObjetivo, categoriaObjetivo;
        Double valorObjetivo, valorAtualObjetivo;
        String dataObjetivo, dataFinalObjetivo;
        Integer dataObjetivoAno, dataObjetivoMes, dataObjetivoDia,
                dataFinalObjetivoDia, dataFinalObjetivoMes, dataFinalObjeitoAno;
        Integer contaRegDadoLido = 0;
        Integer qtdRegDadoGravadoTrailer;
        List<Objetivo> listaLidaObjetivos = new ArrayList<Objetivo>();
        // Cria uma lista com os dados lidos do arquivo

        // try-catch para abrir o arquivo
        try {
            entrada = new BufferedReader(new FileReader(nomeArq));
        } catch (IOException erro) {
            erro.printStackTrace();
            return "Erro ao abrir o arquivo!";

        }

        // try-catch para ler e fechar o arquivo
        try {

            registro = entrada.readLine();       // Lê o 1o registro
           while (registro != null){
                   tipoRegistro = registro.substring(0, 2);


                  if (tipoRegistro.equals("02")) {
                    nomeObjetivo = registro.substring(2, 52).trim();
                    descricaoObjetivo = registro.substring(52, 152).trim();
                    valorObjetivo = Double.valueOf(registro.substring(152, 163).replace(',', '.'));
                    dataObjetivoAno = Integer.valueOf(registro.substring(163, 167));
                    dataObjetivoMes = Integer.valueOf(registro.substring(168, 170));
                    dataObjetivoDia = Integer.valueOf(registro.substring(171, 173));
                    categoriaObjetivo = registro.substring(174, 192).trim();
                    valorAtualObjetivo = Double.valueOf(registro.substring(193, 204).replace(',', '.'));
                    dataFinalObjeitoAno = Integer.valueOf(registro.substring(204, 208));
                    dataFinalObjetivoMes = Integer.valueOf(registro.substring(209, 211));
                    dataFinalObjetivoDia = Integer.valueOf(registro.substring(212, 214));
                    LocalDate dataObjetivoOficial = LocalDate.of(dataObjetivoAno, dataObjetivoMes, dataObjetivoDia);
                    LocalDate dataFinalObjetivoOficial = LocalDate.of(dataFinalObjeitoAno, dataFinalObjetivoMes, dataFinalObjetivoDia);
                    Objetivo objetivo = new Objetivo(nomeObjetivo, descricaoObjetivo, valorObjetivo, dataObjetivoOficial
                            , categoriaObjetivo, valorAtualObjetivo, dataFinalObjetivoOficial);
                    objetivoController.criarObjetivo(idUsuario, objetivo);
                } else if (tipoRegistro.equals("03")) {
                    String nomeReceita, descricaoReceita, categoriaReceita, dataReceita, recorrenciaReceita;
                    Double valorReceita;
                    Integer dataReceitaAno, dataReceitaMes, dataReceitaDia, frequenciaReceita;
                    nomeReceita = registro.substring(2, 52).trim();
                    descricaoReceita = registro.substring(52, 152).trim();
                    valorReceita = Double.valueOf(registro.substring(152, 163).replace(',', '.'));
                    dataReceitaAno = Integer.valueOf(registro.substring(163, 167));
                    dataReceitaMes = Integer.valueOf(registro.substring(168, 170));
                    dataReceitaDia = Integer.valueOf(registro.substring(171, 173));
                    categoriaReceita = registro.substring(173, 192).trim();
                    recorrenciaReceita = registro.substring(193, 198);
                    frequenciaReceita = Integer.valueOf(registro.substring(198, 200));
                    LocalDate dataReceitaFinal = LocalDate.of(dataReceitaAno, dataReceitaMes, dataReceitaDia);
                    Boolean isRecorrente = false;
                    if (recorrenciaReceita.contains("true")) isRecorrente = true;
                    Receita receita = new Receita(nomeReceita, descricaoReceita, valorReceita, dataReceitaFinal,
                            categoriaReceita, isRecorrente, frequenciaReceita);
                    receitaController.criarReceita(idUsuario, receita);
                    contaRegDadoLido++;
                } else if (tipoRegistro.equals("04")) {
                    String nomeDespesa, descricaoDespesa, categoriaDespesa, dataDespesa, pago;
                    Double valorDespesa;
                    Integer dataDespesaAno, dataDespesaMes, dataDespesaDia, parcelas;

                    nomeDespesa = registro.substring(2, 52).trim();
                    descricaoDespesa = registro.substring(52, 152).trim();
                    valorDespesa = Double.valueOf(registro.substring(152, 163).replace(',', '.'));
                    dataDespesaAno = Integer.valueOf(registro.substring(163, 167));
                    dataDespesaMes = Integer.valueOf(registro.substring(168, 170));
                    dataDespesaDia = Integer.valueOf(registro.substring(171, 173));
                    categoriaDespesa = registro.substring(173, 192).trim();
                    pago = registro.substring(193, 198);
                    parcelas = Integer.valueOf(registro.substring(198, 201));
                    Boolean isPago = false;
                    LocalDate dataFinalDespesa = LocalDate.of(dataDespesaAno, dataDespesaMes, dataDespesaDia);
                    if (pago.contains("true")) isPago = true;
                    Despesa despesa = new Despesa(nomeDespesa, descricaoDespesa, valorDespesa, dataFinalDespesa, categoriaDespesa
                            , isPago, parcelas);
                    despesaController.criarDespesa(idUsuario, despesa);
                }

                registro = entrada.readLine();
            }
            entrada.close();
        } catch (IOException erro) {
            erro.printStackTrace();
            return "Erro ao ler o arquivo";
        }
        return "Arquivo lido com sucesso";
    }

    public List<Receita> getHistoricoReceita(Integer idUsuario, Integer mes, Integer ano) {
        LocalDate dataAtual = LocalDate.now();
        if (ano != null && mes == null) {
            List<Receita> receitas = new ArrayList<>();
            return receitas;
        } else if (ano != null && mes != null) {
            dataAtual = dataAtual.withYear(ano);
        }
        if (mes != null) {
            dataAtual = dataAtual.withMonth(mes).with(TemporalAdjusters.lastDayOfMonth());
        }
        LocalDate aPartirDe = LocalDate.of(dataAtual.getYear(), dataAtual.getMonth(), 01);
        List<Receita> receitas = receitaRepository.findByUsuarioIdAndDataBetween(idUsuario, aPartirDe, dataAtual);
        return receitas;
    }

    public List<Despesa> getHistoricoDespesa(Integer idUsuario, Integer mes, Integer ano) {
        LocalDate dataAtual = LocalDate.now();
        if (ano != null && mes == null) {
            List<Despesa> despesas = new ArrayList<>();
            return despesas;
        } else if (ano != null && mes != null) {
            dataAtual = dataAtual.withYear(ano);
        }
        if (mes != null) {
            dataAtual = dataAtual.withMonth(mes).with(TemporalAdjusters.lastDayOfMonth());
        }
        LocalDate aPartirDe = LocalDate.of(dataAtual.getYear(), dataAtual.getMonth(), 01);
        List<Despesa> despesas = despesaRepository.findByUsuarioIdAndDataBetween(idUsuario, aPartirDe, dataAtual);
        return despesas;
    }


    public List<RelatorioMensal> getValorGroupByDateReceita(int idUsuario) {
        List<RelatorioMensal> valoresReceita = receitaRepository.getValorAgrupadoPorData(idUsuario);
        return valoresReceita;

    }

    public List<RelatorioMensal> getValorGroupByDateDespesa(int idUsuario) {
        List<RelatorioMensal> valoresDespesa = despesaRepository.getValorAgrupadoPorData(idUsuario);
        return valoresDespesa;

    }
    public List<RelatorioMensal> getRelatorioGeralByData(int idUsuario){
        List<RelatorioMensal> receitas = getValorGroupByDateReceita(idUsuario);

        List<RelatorioMensal> despesas = getValorGroupByDateDespesa(idUsuario);
        despesas.forEach(e -> e.setValor(e.getValor() * -1) );
        List<RelatorioMensal> geral = new ArrayList<>();
        for (RelatorioMensal r : receitas){
            geral.add(r);
        }
        for (RelatorioMensal d: despesas){
            geral.add(d);
        }

        for (int i = 0; i < geral.size(); i++){
            RelatorioMensal relatorio1 = geral.get(i);
            int mes1 = relatorio1.getMes();
            int ano1 = relatorio1.getAno();
            Double valor1 = relatorio1.getValor();
            for (int a = i+1; a < geral.size();a++){
                RelatorioMensal relatorio = geral.get(a);
                int mes = relatorio.getMes();
                int ano = relatorio.getAno();
                Double valor = relatorio.getValor();

                if (mes1 == mes && ano1 == ano){
                    Double valorFinal = valor1 + valor;
                    relatorio1.setValor(valorFinal);
                    geral.remove(a);


                }
            }
        }
        List<RelatorioMensal> relatorio = this.organizarVetor(geral);
        return  relatorio;
    }
    public List<RelatorioMensal> organizarVetor( List<RelatorioMensal> array){
//
        int indMenor;
        LocalDate menorData;
        for (int i = 0; i < array.size() - 1; i++) {
            RelatorioMensal lista1 = array.get(i);
            int mes1 = lista1.getMes();
            int ano1 = lista1.getAno();
            LocalDate data1= LocalDate.of(ano1,mes1,01);
            menorData = data1;
            indMenor = i;
            for (int j = i + 1; j < array.size(); j++) {
                RelatorioMensal lista =array.get(j);
                int mes = lista.getMes();
                int ano = lista.getAno();
                LocalDate data= LocalDate.of(ano,mes,01);
                if (data.isBefore(menorData)) {
                    menorData = data;
                    indMenor = j;
                }
            }
            RelatorioMensal aux;
            aux = array.get(i);
            array.set(i, array.get(indMenor));
            array.set(indMenor, aux);
        }
    return array;
    }

    public  Double getSaldoMensal(int idUsuario){
        LocalDate dataAtual = LocalDate.now();
        Month dataAtualMes = dataAtual.getMonth();
        Integer dataAtualAno = dataAtual.getYear();
        List<RelatorioMensal> relatorio = getRelatorioGeralByData(idUsuario);
        for (RelatorioMensal r: relatorio){
            if (r.getMes().equals(11) && dataAtualAno.equals(r.getAno())){
                return (r.getValor());
            }
        }
        return 0.0;

    }

    public void recebendoArquivoEGrandoInfoBanco( Integer idUsuario, String nomeArquivo, byte[] arquivoTxt) throws IOException {
        nomeArquivo += ".txt";
        String registro = new String(arquivoTxt);
        final CountDownLatch latch = new CountDownLatch(1);
        gravaRegistro(registro,nomeArquivo,true);
        leArquivoTxt(nomeArquivo,idUsuario);
        File file = new File(nomeArquivo);
        file.delete();
        gerandoArquivoCompletoEGravandoBanco(idUsuario,nomeArquivo);
    }

    public void gerandoArquivoCompletoEGravandoBanco(Integer idUsuario,String nomeArquivo)
            throws IOException{
        gerarTxt(idUsuario,"nomeArquivo");
        File file = new File("nomeArquivo");
        byte[] arr = Files.readAllBytes(file.toPath());
        file.delete();

        usuarioRepository.setArquivoTxt(idUsuario,arr);

    }

    public Usuario logoff(String email, String senha){
        // se o usuario existir ele verifica se o mesmo está logado, desloga ele e retorna o usuario para a controller
        Usuario usuario = usuarioRepository.findByEmailAndSenha(email, senha);
        if (usuario == null) return null;
        if(usuario.getAutenticado()) {
            usuario.setAutenticado(false);
            usuarioRepository.save(usuario);
            return usuario;
        } else  {
            return null;
        }
    }
};

