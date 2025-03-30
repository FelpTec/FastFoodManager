package senai.felp;

import java.util.List;

public interface CardapioDAO {



    //CRUD

    //CREATE
    void adicionar(Produto produto);
    void adicionarHistoricoPedido(Pedido pedido, String pagamentoSelecionado);

    //READ
    List<String>listarHistoricoPedidos();
    List<Produto> listarPorTipo(String tipo);
    List<Produto> listarTodos();
    List<Produto> buscarPorNome(String predicado);
    Produto buscarPorId(int id);

    //UPDATE
    void atualizar(Produto produto);

    //REMOVE
    void remover(int id);

}
