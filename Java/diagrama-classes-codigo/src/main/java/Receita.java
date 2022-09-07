import java.util.Date;

public class Receita extends Financas{

    private boolean isRecorrente;
    private int frequencia;

    public Receita(int codigo, String nome, String descricao, double valor, Date data,
                   String categoria, boolean isRecorrente, int frequencia) {
        super(codigo, nome, descricao, valor, data, categoria);
        this.isRecorrente = isRecorrente;
        this.frequencia = frequencia;
    }

    public boolean isRecorrente() {
        return isRecorrente;
    }

    public void setRecorrente(boolean recorrente) {
        isRecorrente = recorrente;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    @Override
    public void modificaSaldo(Usuario usuario) {
     usuario.setSaldo(usuario.getSaldo() + getValor());
    }

    @Override
    public String toString() {
        return String.format
                ("\n---------- Receita ----------" +
                "\nisRecorrente: %s " +
                "\nFrequência: %d",isRecorrente, frequencia);
    }
}
