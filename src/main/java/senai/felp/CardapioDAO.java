package senai.felp;

import java.util.List;

public interface CardapioDAO {
    void adicionar(Produto produto);
    Produto buscar(int id);
    void atualizar(Produto produto);
    void remover(int id);
    List<Produto> listarTodos();
    List<String>listarHistoricoPedidos();
    void adicionarHistoricoPedido(Pedido pedido, String pagamentoSelecionado);
}
