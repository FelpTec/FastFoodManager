package senai.felp;

public class Bebida extends Produto {
    private final boolean isAlcoolica;

    public Bebida(int id, String nome, double preco, boolean isAlcoolica) {
        super(id, nome, preco);
        this.isAlcoolica = isAlcoolica;
    }

    public boolean isAlcoolica() {
        return isAlcoolica;
    }

    @Override
    public String toString() {
        return "ID: "+ id + " | Bebida: " + nome + " | Preço: R$" + preco + " | Alcoólica: " + (isAlcoolica ? "Sim" : "Não");
    }
}
