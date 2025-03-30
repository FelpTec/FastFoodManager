package senai.felp.objects;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final List<Produto> produtos;

    public Pedido() {
        this.produtos = new ArrayList<>();
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public String listarProdutos() {
        StringBuilder lista = new StringBuilder("Produtos no Pedido:\n");
        for (Produto p : produtos) {
            lista.append(p.toString()).append("\n");
        }
        lista.append("\n======================================\nTotal: R$").append(calcularTotal()).append("\n");
        return lista.toString();
    }

    public double calcularTotal() {
        double total = 0.0;
        for (Produto produto : produtos) {
            total += produto.preco; // Soma o preço de cada produto
        }
        return total;
    }
}