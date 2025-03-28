package senai.felp;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FastFoodGUI {
    private final JFrame frame;
    private final CardapioDAO cardapioDAO = new CardapioDAOImpl();

    public FastFoodGUI() {
        frame = new JFrame("Fast Food Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new GridLayout(3, 2, 10, 10));
        frame.getContentPane().setBackground(new Color(255, 204, 102));

        JButton btnCadastrar = criarBotao("Cadastrar Produto", new Color(255, 69, 0));
        JButton btnListar = criarBotao("Listar Produtos", new Color(255, 140, 0));
        JButton btnBuscar = criarBotao("Buscar Produto", new Color(255, 165, 0));
        JButton btnAtualizar = criarBotao("Atualizar Produto", new Color(255, 215, 0));
        JButton btnRemover = criarBotao("Remover Produto", new Color(220, 20, 60));
        JButton btnFechar = criarBotao("Fechar", Color.DARK_GRAY);
        btnFechar.setForeground(Color.WHITE);

        btnCadastrar.addActionListener(_ -> abrirCadastro());
        btnListar.addActionListener(_ -> listarProdutos());
        btnBuscar.addActionListener(_ -> buscarProduto());
        btnAtualizar.addActionListener(_ -> atualizarProduto());
        btnRemover.addActionListener(_ -> removerProduto());
        btnFechar.addActionListener(_ -> frame.dispose());

        frame.add(btnCadastrar);
        frame.add(btnListar);
        frame.add(btnBuscar);
        frame.add(btnAtualizar);
        frame.add(btnRemover);
        frame.add(btnFechar);
        frame.setVisible(true);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setFocusPainted(false);
        return botao;
    }

    private void abrirCadastro() {
        JFrame cadastroFrame = new JFrame("Cadastrar Produto");
        cadastroFrame.setSize(300, 200);
        cadastroFrame.setLayout(new GridLayout(5, 2));

        JTextField nomeField = new JTextField();
        JTextField precoField = new JTextField();
        String[] tipos = {"Lanche", "Bebida"};
        JComboBox<String> tipoBox = new JComboBox<>(tipos);
        JCheckBox extraCheck = new JCheckBox("Vegano / Alcoólico");
        JButton btnSalvar = criarBotao("Salvar", new Color(34, 139, 34));

        btnSalvar.addActionListener(_ -> {
            String nome = nomeField.getText();
            double preco = Double.parseDouble(precoField.getText());
            String tipo = (String) tipoBox.getSelectedItem();

            if ("Lanche".equals(tipo)) {
                cardapioDAO.adicionar(new Lanche(0, nome, preco, extraCheck.isSelected()));
            } else {
                cardapioDAO.adicionar(new Bebida(0, nome, preco, extraCheck.isSelected()));
            }
            cadastroFrame.dispose();
        });

        cadastroFrame.add(new JLabel("Nome:"));
        cadastroFrame.add(nomeField);
        cadastroFrame.add(new JLabel("Preço:"));
        cadastroFrame.add(precoField);
        cadastroFrame.add(new JLabel("Tipo:"));
        cadastroFrame.add(tipoBox);
        cadastroFrame.add(extraCheck);
        cadastroFrame.add(btnSalvar);

        cadastroFrame.setVisible(true);
    }

    private void listarProdutos() {
        List<Produto> produtos = cardapioDAO.listarTodos();
        StringBuilder lista = new StringBuilder("Produtos:\n");
        for (Produto p : produtos) {
            lista.append(p.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(frame, lista.toString());
    }

    private void buscarProduto() {
        String idStr = JOptionPane.showInputDialog(frame, "Digite o ID do produto:");
        int id = Integer.parseInt(idStr);
        Produto produto = cardapioDAO.buscar(id);
        if (produto != null) {
            JOptionPane.showMessageDialog(frame, produto.toString());
        } else {
            JOptionPane.showMessageDialog(frame, "Produto não encontrado.");
        }
    }

    private void atualizarProduto() {
        String idStr = JOptionPane.showInputDialog(frame, "Digite o ID do produto a atualizar:");
        int id = Integer.parseInt(idStr);
        Produto produto = cardapioDAO.buscar(id);
        if (produto != null) {
            String novoNome = JOptionPane.showInputDialog(frame, "Novo nome:", produto.nome);
            String novoPrecoStr = JOptionPane.showInputDialog(frame, "Novo preço:", produto.preco);
            double novoPreco = Double.parseDouble(novoPrecoStr);

            if (produto instanceof Lanche) {
                boolean isVegano = JOptionPane.showConfirmDialog(frame, "É vegano?", "Atualizar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                cardapioDAO.atualizar(new Lanche(id, novoNome, novoPreco, isVegano));
            } else if (produto instanceof Bebida) {
                boolean isAlcoolica = JOptionPane.showConfirmDialog(frame, "É alcoólica?", "Atualizar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                cardapioDAO.atualizar(new Bebida(id, novoNome, novoPreco, isAlcoolica));
            }
            JOptionPane.showMessageDialog(frame, "Produto atualizado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(frame, "Produto não encontrado.");
        }
    }

    private void removerProduto() {
        String idStr = JOptionPane.showInputDialog(frame, "Digite o ID do produto a remover:");
        int id = Integer.parseInt(idStr);
        Produto produto = cardapioDAO.buscar(id);
        if (produto != null) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Tem certeza que deseja remover este produto?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cardapioDAO.remover(id);
                JOptionPane.showMessageDialog(frame, "Produto removido com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Produto não encontrado.");
        }
    }

    public static void main(String[] args) {
        new FastFoodGUI();
    }
}