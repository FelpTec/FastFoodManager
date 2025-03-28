public class Lanche extends Produto {
    private boolean isVegano;

    public Lanche(int id, String nome, double preco, boolean isVegano) {
        super(id, nome, preco);
        this.isVegano = isVegano;
    }

    public boolean isVegano() {
        return isVegano;
    }

    @Override
    public String toString() {
        return "ID: "+ id + " | Lanche: " + nome + " | Preço: R$" + preco + " | Vegano: " + (isVegano ? "Sim" : "Não");
    }
}
