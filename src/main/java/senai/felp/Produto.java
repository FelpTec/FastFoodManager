package senai.felp;

public abstract class Produto {
    protected int id;
    protected String nome;
    protected double preco;

    public Produto(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public abstract String toString();
}
