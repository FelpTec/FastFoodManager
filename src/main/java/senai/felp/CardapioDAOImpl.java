package senai.felp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardapioDAOImpl implements CardapioDAO {

    @Override
    public void adicionar(Produto produto) {
        if (produto instanceof Lanche) {
            String sql = "INSERT INTO cardapio (nome, preco, tipo, vegano) VALUES (?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, produto.nome);
                stmt.setDouble(2, produto.preco);
                stmt.setString(3, "Lanche");
                stmt.setBoolean(4, ((Lanche) produto).isVegano());
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Erro ao adicionar produto: " + e.getMessage());
            }
        } else if (produto instanceof Bebida) {
            String sql = "INSERT INTO cardapio (nome, preco, tipo, alcoolica) VALUES (?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, produto.nome);
                stmt.setDouble(2, produto.preco);
                stmt.setString(3, "Bebida");
                stmt.setBoolean(4, ((Bebida) produto).isAlcoolica());
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Erro ao adicionar produto: " + e.getMessage());
            }
        }
    }

    @Override
    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM cardapio WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                String tipo = rs.getString("tipo");

                if ("Lanche".equals(tipo)) {
                    boolean vegano = rs.getBoolean("vegano");
                    return new Lanche(id, nome, preco, vegano);
                } else if ("Bebida".equals(tipo)) {
                    boolean alcoolica = rs.getBoolean("alcoolica");
                    return new Bebida(id, nome, preco, alcoolica);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void atualizar(Produto produto) {
        if (produto instanceof Lanche) {
            String sql = "UPDATE cardapio SET nome = ?, preco = ?, tipo = ?, vegano = ? WHERE id = ?";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, produto.nome);
                stmt.setDouble(2, produto.preco);
                stmt.setString(3, "Lanche");
                stmt.setBoolean(4, ((Lanche) produto).isVegano());
                stmt.setInt(5, produto.id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Erro ao atualizar produto: " + e.getMessage());
            }
        } else if (produto instanceof Bebida) {
            String sql = "UPDATE cardapio SET nome = ?, preco = ?, tipo = ?, alcoolica = ? WHERE id = ?";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, produto.nome);
                stmt.setDouble(2, produto.preco);
                stmt.setString(3, "Bebida");
                stmt.setBoolean(4, ((Bebida) produto).isAlcoolica());
                stmt.setInt(5, produto.id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Erro ao atualizar produto: " + e.getMessage());
            }
        }
    }

    @Override
    public void remover(int id) {
        String sql = "DELETE FROM cardapio WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao Bando de Dados: " + e.getMessage());
        }
    }

    @Override
    public List<Produto> listarPorTipo(String tipo) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM cardapio WHERE tipo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");

                if ("Lanche".equals(tipo)) {
                    boolean vegano = rs.getBoolean("vegano");
                    produtos.add(new Lanche(id, nome, preco, vegano));
                } else if ("Bebida".equals(tipo)) {
                    boolean alcoolica = rs.getBoolean("alcoolica");
                    produtos.add(new Bebida(id, nome, preco, alcoolica));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos por tipo: " + e.getMessage());
        }
        return produtos;
    }

    @Override
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM cardapio";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                String tipo = rs.getString("tipo");

                if ("Lanche".equals(tipo)) {
                    boolean vegano = rs.getBoolean("vegano");
                    produtos.add(new Lanche(id, nome, preco, vegano));
                } else if ("Bebida".equals(tipo)) {
                    boolean alcoolica = rs.getBoolean("alcoolica");
                    produtos.add(new Bebida(id, nome, preco, alcoolica));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos por tipo: " + e.getMessage());
        }
        return produtos;
    }

    public void adicionarHistoricoPedido(Pedido pedido, String formaPagamento) {
        String produtos = pedido.listarProdutos(); // Obtém a lista de produtos do pedido
        double total = pedido.calcularTotal(); // Calcula o total do pedido
        String sql = "INSERT INTO historico_pedidos (produtos, total, forma_pagamento) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, produtos);
            pstmt.setDouble(2, total);
            pstmt.setString(3, formaPagamento);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar pedido ao histórico: " + e.getMessage());
        }
    }

    public List<String> listarHistoricoPedidos() {
        List<String> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM historico_pedidos ORDER BY data DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String produtos = rs.getString("produtos");
                String formaPagamento = rs.getString("forma_pagamento");
                String data = rs.getString("data");
                pedidos.add("=====================================================================================" + produtos + "Pagamento: " + formaPagamento + " \nData: " + data);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar Histórico de Pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    @Override
    public List<Produto> buscarPorNome(String predicado) {
        List<Produto> produtosEncontrados = new ArrayList<>();
        String sql = "SELECT * FROM cardapio WHERE nome LIKE ? OR tipo LIKE ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Usando '%' para permitir busca parcial
            String busca = "%" + predicado + "%";
            stmt.setString(1, busca);
            stmt.setString(2, busca);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                String tipo = rs.getString("tipo");

                if ("Lanche".equals(tipo)) {
                    boolean vegano = rs.getBoolean("vegano");
                    produtosEncontrados.add(new Lanche(id, nome, preco, vegano));
                } else if ("Bebida".equals(tipo)) {
                    boolean alcoolica = rs.getBoolean("alcoolica");
                    produtosEncontrados.add(new Bebida(id, nome, preco, alcoolica));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produtos: " + e.getMessage());
        }
        return produtosEncontrados;
    }
}
