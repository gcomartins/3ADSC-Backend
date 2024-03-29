import java.util.Date;

public class Objetivo extends Financas {

    private double valorAtual;
    private Date dataFinal;

    public Objetivo(int codigo, String nome, String descricao, double valor,
                    Date data, String categoria, double valorAtual, Date dataFinal) {
        super(codigo, nome, descricao, valor, data, categoria);
        this.valorAtual = valorAtual;
        this.dataFinal = dataFinal;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(double valorAtual) {
        this.valorAtual = valorAtual;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    @Override
    public void modificaSaldo(Usuario usuario) {
        usuario.setSaldo(usuario.getSaldo() + valorAtual);
    }

    @Override
    public String toString() {
        return String.format
                ("\n---------- Objetivo ----------" +
                "\nValor Atual: %.2f" +
                "\nData Final: %s", valorAtual, dataFinal);
    }
}
