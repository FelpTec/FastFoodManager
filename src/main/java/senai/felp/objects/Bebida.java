package senai.felp.objects;

public class Bebida extends Produto {
    private final boolean isAlcoolica;

    public Bebida(int id, String nome, double preco, boolean isAlcoolica) {
        super(id, nome, preco);
        if (preco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        this.isAlcoolica = isAlcoolica;
    }

    public boolean isAlcoolica() {
        return isAlcoolica;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Bebida: %s | Preço: R$%.2f | Alcoólica: %s |",
                id, nome, preco, (isAlcoolica ? "Sim" : "Não"));
    }
}
