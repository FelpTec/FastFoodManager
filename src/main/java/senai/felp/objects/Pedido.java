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
        lista.append("=================================================\n");

        for (Produto p : produtos) {
            lista.append(p.toString()).append("\n");
        }

        lista.append("=================================================\n");
        lista.append(String.format("Total: R$%.2f |", calcularTotal())); // Formata o total com duas casas decimais
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