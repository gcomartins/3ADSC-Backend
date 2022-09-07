import javax.xml.crypto.Data;
import java.util.Date;

public class Usuario {

    private int idUsuario;
    private String nome;
    private String email;
    private String cpf;
    private double saldo;
    private String senha;
    private Date dataNascimento;

    public Usuario(int idUsuario, String nome, String email, String cpf,
                   double saldo, String senha, Date dataNascimento) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.saldo = saldo;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return String.format
                ("\n---------- Usuario ----------" +
                        "\nidUsuario: %d" +
                        "\nNome: %s " +
                        "\nEmail: %s " +
                        "\nCPF: %s " +
                        "\nSaldo: %.2f " +
                        "\nSenha: *** " +
                        "\nData de Nascimento: %s", idUsuario, nome, email, cpf, saldo, dataNascimento);
    }
}
