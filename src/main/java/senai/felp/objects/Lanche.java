package senai.felp.objects;

public class Lanche extends Produto {
    private final boolean isVegano;

    public Lanche(int id, String nome, double preco, boolean isVegano) {
        super(id, nome, preco);
        if (preco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        this.isVegano = isVegano;
    }

    public boolean isVegano() {
        return isVegano;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Lanche: %s | Preço: R$%.2f | Vegano: %s |",
                id, nome, preco, (isVegano ? "Sim" : "Não"));
    }
}
