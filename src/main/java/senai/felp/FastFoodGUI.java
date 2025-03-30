package senai.felp;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FastFoodGUI {
    private final JFrame frame;
    private final CardapioDAO cardapioDAO = new CardapioDAOImpl();
    private Pedido pedido = new Pedido();

    public FastFoodGUI() {
        frame = new JFrame("Fast Food Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.getContentPane().setBackground(Color.WHITE); // Cor de fundo branca

        // Usando JTabbedPane para organizar as abas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Criando a aba de Gerenciamento
        JPanel gerenciamentoPanel = new JPanel();
        gerenciamentoPanel.setLayout(new GridLayout(3, 1, 10, 10)); // Layout para os botões

        JButton btnCadastrar = criarBotao("Cadastrar Produto", new Color(255, 69, 0)); // Vermelho
        JButton btnRemover = criarBotao("Remover Produto", new Color(220, 20, 60)); // Vermelho-escuro
        JButton btnAtualizar = criarBotao("Atualizar Produto", new Color(255, 165, 0)); // Laranja
        JButton btnHistorico = criarBotao("Histórico de Pedidos", new Color(255, 140, 0)); // Laranja

        gerenciamentoPanel.add(btnCadastrar);
        gerenciamentoPanel.add(btnRemover);
        gerenciamentoPanel.add(btnAtualizar);
        gerenciamentoPanel.add(btnHistorico);

        // Adicionando a aba de Gerenciamento ao JTabbedPane
        tabbedPane.addTab("Gerenciamento", gerenciamentoPanel);

        // Criando a aba de Operações
        JPanel operacoesPanel = new JPanel();
        operacoesPanel.setLayout(new GridLayout(4, 1, 10, 10)); // Layout para os botões

        JButton btnListar = criarBotao("Ver Cardápio", new Color(255, 140, 0)); // Laranja
        JButton btnListarPedido = criarBotao("Ver Pedido", new Color(255, 255, 0)); // Amarelo
        JButton btnPesquisar = criarBotao("Pesquisar", new Color(255, 69, 0)); // Vermelho

        operacoesPanel.add(btnListar);
        operacoesPanel.add(btnListarPedido);
        operacoesPanel.add(btnPesquisar);

        // Adicionando a aba de Operações ao JTabbedPane
        tabbedPane.addTab("Operações", operacoesPanel);

        // Adicionando o JTabbedPane ao JFrame
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Adicionando ações aos botões
        btnHistorico.addActionListener(_ -> mostrarHistoricoPedidos());
        btnCadastrar.addActionListener(_ -> abrirCadastro());
        btnRemover.addActionListener(_ -> removerProduto());
        btnAtualizar.addActionListener(_ -> atualizarProduto());
        btnListar.addActionListener(_ -> listarProdutos());
        btnListarPedido.addActionListener(_ -> mostrarPedido());
        btnPesquisar.addActionListener(_ -> buscarProduto()); // Ação para o botão "Pesquisar"

        frame.setVisible(true);
    }


    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Adiciona uma borda
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
        JButton btnSalvar = criarBotao("Salvar", new Color(34, 139, 34)); // Verde

        btnSalvar.addActionListener(_ -> {
            String nome = nomeField.getText().trim();
            String precoStr = precoField.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(cadastroFrame, "O nome do produto não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double preco;
            try {
                preco = Double.parseDouble(precoStr);
                if (preco < 0) {
                    JOptionPane.showMessageDialog(cadastroFrame, "O preço não pode ser negativo.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(cadastroFrame, "Preço inválido. Por favor, insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

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
        // Cria um JFrame para o cardápio
        JFrame cardapioFrame = new JFrame("Cardápio");
        cardapioFrame.setSize(400, 300);
        cardapioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cardapioFrame.setLayout(new BorderLayout()); // Usando BorderLayout

        // Cria um JTabbedPane para as abas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Cria o painel para Lanches
        JPanel lanchesPanel = new JPanel();
        lanchesPanel.setLayout(new BoxLayout(lanchesPanel, BoxLayout.Y_AXIS)); // Layout vertical

        // Obtém a lista de lanches e adiciona ao painel
        List<Produto> lanches = cardapioDAO.listarTodos();
        for (Produto produto : lanches) {
            if (produto instanceof Lanche) {
                lanchesPanel.add(new JLabel(produto.toString())); // Adiciona cada lanche como um JLabel
            }
        }

        // Cria o painel para Bebidas
        JPanel bebidasPanel = new JPanel();
        bebidasPanel.setLayout(new BoxLayout(bebidasPanel, BoxLayout.Y_AXIS)); // Layout vertical

        // Obtém a lista de bebidas e adiciona ao painel
        for (Produto produto : lanches) {
            if (produto instanceof Bebida) {
                bebidasPanel.add(new JLabel(produto.toString())); // Adiciona cada bebida como um JLabel
            }
        }

        //Cria o painel para Pesquisa
        JPanel pesquisaPanel = new JPanel();
        pesquisaPanel.setLayout(new BoxLayout(pesquisaPanel, BoxLayout.Y_AXIS));



        // Adiciona os painéis ao JTabbedPane
        tabbedPane.addTab("Lanches", lanchesPanel);
        tabbedPane.addTab("Bebidas", bebidasPanel);
        tabbedPane.addTab("Pesquisar", pesquisaPanel);

        // Adiciona o JTabbedPane ao JFrame
        cardapioFrame.add(tabbedPane, BorderLayout.CENTER); // Adiciona o JTabbedPane na área central

        // Cria o botão "Adicionar ao Pedido"
        JButton btnAdicionarPedido = criarBotao("Adicionar ao Pedido", new Color(34, 139, 34)); // Verde
        btnAdicionarPedido.addActionListener(_ -> selecionarProdutoParaPedido());

        // Adiciona o botão na parte inferior do JFrame
        cardapioFrame.add(btnAdicionarPedido, BorderLayout.SOUTH); // Adiciona o botão na área sul

        // Torna o JFrame visível
        cardapioFrame.setVisible(true);
    }

    private void selecionarProdutoParaPedido() {
        String[] tipos = {"Lanche", "Bebida"};
        String tipoSelecionado = (String) JOptionPane.showInputDialog(frame, "Selecione o tipo de produto:", "Listar por Tipo", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        List<Produto> produtos = cardapioDAO.listarPorTipo(tipoSelecionado);
        String[] opcoes = produtos.stream().map(Produto::toString).toArray(String[]::new);

        String produtoSelecionado = (String) JOptionPane.showInputDialog(frame, "Selecione um produto:", "Adicionar ao Pedido", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        if (produtoSelecionado != null) {
            for (Produto p : produtos) {
                if (produtoSelecionado.contains(p.getNome())) {
                    pedido.adicionarProduto(p);
                    JOptionPane.showMessageDialog(frame, "Produto adicionado ao pedido!");
                    break;
                }
            }
        }
    }

    private void mostrarPedido() {
        JFrame pedidoFrame = new JFrame("Detalhes do Pedido");
        pedidoFrame.setSize(400, 300);
        pedidoFrame.setLayout(new BorderLayout());

        JTextArea textoPedido = new JTextArea();
        textoPedido.setEditable(false);
        textoPedido.setText(pedido.listarProdutos()); // Mostra os produtos e total do pedido

        JButton btnFinalizarPedido = criarBotao("Finalizar Pedido", new Color(34, 139, 34)); // Verde
        btnFinalizarPedido.addActionListener(_ -> finalizarPedido(pedidoFrame)); // Passa a janela para o método de finalização

        pedidoFrame.add(new JScrollPane(textoPedido), BorderLayout.CENTER);
        pedidoFrame.add(btnFinalizarPedido, BorderLayout.SOUTH);
        pedidoFrame.setVisible(true);
    }

    private void buscarProduto() {
        String predicado = JOptionPane.showInputDialog(frame, "Digite o nome ou tipo do produto:");
        if (predicado != null && !predicado.trim().isEmpty()) {
            List<Produto> produtosEncontrados = cardapioDAO.buscarPorNome(predicado);
            if (!produtosEncontrados.isEmpty()) {
                StringBuilder resultado = new StringBuilder("Produtos encontrados:\n");
                for (Produto produto : produtosEncontrados) {
                    resultado.append(produto.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(frame, resultado.toString());
            } else {
                JOptionPane.showMessageDialog(frame, "Nenhum produto encontrado com o critério: " + predicado);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "O critério de busca não pode estar vazio.");
        }
    }

    private void atualizarProduto() {
        String idStr = JOptionPane.showInputDialog(frame, "Digite o ID do produto a atualizar:");
        if (idStr == null || idStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "ID não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "ID inválido. Por favor, insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Produto produto = cardapioDAO.buscarPorId(id);
        if (produto != null) {
            String novoNome = JOptionPane.showInputDialog(frame, "Novo nome:", produto.nome);
            if (novoNome == null || novoNome.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "O nome não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String novoPrecoStr = JOptionPane.showInputDialog(frame, "Novo preço:", produto.preco);
            if (novoPrecoStr == null || novoPrecoStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "O preço não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double novoPreco;
            try {
                novoPreco = Double.parseDouble(novoPrecoStr);
                if (novoPreco < 0) {
                    JOptionPane.showMessageDialog(frame, "O preço não pode ser negativo.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Preço inválido. Por favor, insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

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
        Produto produto = cardapioDAO.buscarPorId(id);
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

    private void finalizarPedido(JFrame pedidoFrame) {
        if (pedido.getProdutos().isEmpty()) {
            JOptionPane.showMessageDialog(pedidoFrame, "Não há produtos no pedido.");
            return;
        }

        String[] opcoesPagamento = {"Débito", "Crédito", "Pix"};
        String pagamentoSelecionado = (String) JOptionPane.showInputDialog(pedidoFrame, "Selecione a forma de pagamento:", "Finalizar Pedido", JOptionPane.QUESTION_MESSAGE, null, opcoesPagamento, opcoesPagamento[0]);

        if (pagamentoSelecionado != null) {
            JOptionPane.showMessageDialog(pedidoFrame, "Pedido finalizado com sucesso!\nTotal: R$" + pedido.calcularTotal() + "\nForma de pagamento: " + pagamentoSelecionado);

            // Salvar o pedido no histórico
            cardapioDAO.adicionarHistoricoPedido(pedido, pagamentoSelecionado);

            pedido = new Pedido(); // Reseta o pedido após finalização
            pedidoFrame.dispose(); // Fecha a janela do pedido
        }
    }


    private void mostrarHistoricoPedidos() {
        JFrame historicoFrame = new JFrame("Histórico de Pedidos");
        historicoFrame.setSize(400, 300);
        historicoFrame.setLayout(new BorderLayout());

        JTextArea textoHistorico = new JTextArea();
        textoHistorico.setEditable(false);

        // Carregar histórico de pedidos do banco de dados
        List<String> historico = cardapioDAO.listarHistoricoPedidos();
        StringBuilder sb = new StringBuilder("Histórico de Pedidos:\n");
        for (String pedido : historico) {
            sb.append(pedido).append("\n");
        }
        textoHistorico.setText(sb.toString());

        historicoFrame.add(new JScrollPane(textoHistorico), BorderLayout.CENTER);
        historicoFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new FastFoodGUI();
    }
}