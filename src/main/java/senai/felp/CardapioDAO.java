package senai.felp;

import senai.felp.objects.Pedido;
import senai.felp.objects.Produto;

import java.util.List;

public interface CardapioDAO {

    //INTERFACE FASTFOOD MANAGER CRUD

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
