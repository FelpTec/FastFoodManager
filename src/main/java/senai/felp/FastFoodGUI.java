package senai.felp;


import senai.felp.objects.Bebida;
import senai.felp.objects.Lanche;
import senai.felp.objects.Pedido;
import senai.felp.objects.Produto;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FastFoodGUI {
    private final JFrame frame;
    private final CardapioDAO cardapioDAO = new CardapioDAOImpl();
    private Pedido pedido = new Pedido(LoginScreen.getNomeUsuario());

    //Janela Principal
    public FastFoodGUI(String usuarioLogado) {
        frame = new JFrame("Fast Food Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.getContentPane().setBackground(Color.WHITE); // Cor de fundo branca

        // Usando JTabbedPane para organizar as abas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Criando a aba de Gerenciamento
        JPanel gerenciamentoPanel = new JPanel();
        gerenciamentoPanel.setLayout(new GridLayout(3, 1, 10, 10)); // Layout para os botões


        //Criando botões da aba de Gerenciamento
        JButton btnCadastrar = criarBotao("Cadastrar Produto", new Color(255, 69, 0)); // Vermelho
        JButton btnRemover = criarBotao("Remover Produto", new Color(220, 20, 60)); // Vermelho-escuro
        JButton btnAtualizar = criarBotao("Atualizar Produto", new Color(255, 165, 0)); // Laranja
        JButton btnHistorico = criarBotao("Histórico de Pedidos", new Color(255, 140, 0)); // Laranja

        //Adicionando botões ao Panel de gerenciamento
        gerenciamentoPanel.add(btnCadastrar);
        gerenciamentoPanel.add(btnRemover);
        gerenciamentoPanel.add(btnAtualizar);
        gerenciamentoPanel.add(btnHistorico);

        // Adicionando a aba de Gerenciamento ao JTabbedPane
        tabbedPane.addTab("Gerenciamento", gerenciamentoPanel);

        // Criando a aba de Operações
        JPanel operacoesPanel = new JPanel();
        operacoesPanel.setLayout(new GridLayout(4, 1, 10, 10)); // Layout para os botões

        //Criando botões da aba de Operações
        JButton btnListar = criarBotao("Ver Cardápio", new Color(255, 140, 0)); // Laranja
        JButton btnListarPedido = criarBotao("Ver Pedido", new Color(255, 69, 0)); // Vermelho

        //Adicionando botões ao Panel de Operações
        operacoesPanel.add(btnListar);
        operacoesPanel.add(btnListarPedido);

        // Adicionando a aba de Operações ao JTabbedPane
        tabbedPane.addTab("Operações", operacoesPanel);

        JPanel userPanel = new JPanel();

        userPanel.add(new JLabel("Usuário Logado: " + usuarioLogado));

        tabbedPane.addTab("Usuário", userPanel);

        // Adicionando o JTabbedPane ao JFrame
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Adicionando ações aos botões
        btnHistorico.addActionListener(_ -> mostrarHistoricoPedidos());
        btnCadastrar.addActionListener(_ -> abrirCadastro());
        btnRemover.addActionListener(_ -> removerProduto());
        btnAtualizar.addActionListener(_ -> atualizarProduto());
        btnListar.addActionListener(_ -> listarProdutos());
        btnListarPedido.addActionListener(_ -> mostrarPedido());

        //Botando no "ar"
        frame.setVisible(true);
    }

    //Função para criação padrão de Botões
    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Adiciona uma borda
        return botao;
    }

    //JFrame para Cadastro de Produtos + Tratamento de Exceções
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

    //JFrame para saída do Cardápio
    private void listarProdutos() {
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

        // Cria o painel para Pesquisar
        JPanel pesquisaPanel = new JPanel();
        pesquisaPanel.setLayout(new BorderLayout());

        // Cria um campo de texto para a pesquisa
        JTextField pesquisaField = new JTextField();
        pesquisaField.setFont(new Font("Arial", Font.BOLD, 14));

        // Cria um botão para a pesquisa
        JButton btnPesquisar = criarBotao("Pesquisar", new Color(255, 69, 0)); // Vermelho
        btnPesquisar.addActionListener(_ -> {
            String predicado = pesquisaField.getText().trim();
            if (!predicado.isEmpty()) {
                List<Produto> produtosEncontrados = cardapioDAO.buscarPorNome(predicado);
                if (!produtosEncontrados.isEmpty()) {
                    StringBuilder resultado = new StringBuilder("Produtos encontrados:\n");
                    for (Produto produto : produtosEncontrados) {
                        resultado.append(produto.toString()).append("\n");
                    }
                    JOptionPane.showMessageDialog(cardapioFrame, resultado.toString());
                } else {
                    JOptionPane.showMessageDialog(cardapioFrame, "Nenhum produto encontrado com o critério: " + predicado);
                }
            } else {
                JOptionPane.showMessageDialog(cardapioFrame, "O critério de busca não pode estar vazio.");
            }
        });

        // Adiciona o campo de pesquisa e botão de pesquisar
        pesquisaPanel.add(pesquisaField, BorderLayout.CENTER);
        pesquisaPanel.add(btnPesquisar, BorderLayout.EAST);

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

    //Função para Adicionar produto ao Pedido
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

    //JFrame para mostrar o Pedido
    private void mostrarPedido() {
        JFrame pedidoFrame = new JFrame("Detalhes do Pedido");
        pedidoFrame.setSize(400, 300);
        pedidoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pedidoFrame.setLayout(new BorderLayout());

        // Cria um JTabbedPane para as abas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Cria o painel para mostrar os produtos do pedido
        JPanel produtosPanel = new JPanel();
        produtosPanel.setLayout(new BoxLayout(produtosPanel, BoxLayout.Y_AXIS)); // Layout vertical

        // Adiciona os produtos do pedido ao painel
        if (pedido.getProdutos().isEmpty()) {
            produtosPanel.add(new JLabel("Nenhum produto no pedido."));
        } else {
            for (Produto produto : pedido.getProdutos()) {
                produtosPanel.add(new JLabel(produto.toString())); // Adiciona cada produto como um JLabel
            }
        }

        // Adiciona o painel de produtos ao JTabbedPane
        tabbedPane.addTab("Produtos no Pedido", produtosPanel);

        //Adicional o total no Panel de Produtos
        JLabel totalLabel = new JLabel("\nTotal: R$" + pedido.calcularTotal());
        produtosPanel.add(totalLabel, BorderLayout.CENTER);

        // Cria o botão "Finalizar Pedido"
        JButton btnFinalizarPedido = criarBotao("Finalizar Pedido", new Color(34, 139, 34));// Verde
        btnFinalizarPedido.addActionListener(_ -> finalizarPedido(pedidoFrame));

        // Cria o botão "Remover Produto"
        JButton btnRemoverProduto = criarBotao("Remover Produto", Color.RED); // Vermelho
        btnRemoverProduto.addActionListener(_ -> removerProdutoDoPedido());

        // Adiciona o JTabbedPane e o botão ao JFrame
        pedidoFrame.add(tabbedPane, BorderLayout.CENTER);
        pedidoFrame.add(btnFinalizarPedido, BorderLayout.SOUTH);
        pedidoFrame.add(btnRemoverProduto, BorderLayout.EAST);



        // Torna o JFrame visível
        pedidoFrame.setVisible(true);
    }

    //Função para atualizar Produtos através de ID
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
            String novoNome = JOptionPane.showInputDialog(frame, "Novo nome:", produto.getNome());
            if (novoNome == null || novoNome.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "O nome não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String novoPrecoStr = JOptionPane.showInputDialog(frame, "Novo preço:", produto.getPreco());
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

    //Função para deletar Produtos através de ID
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

    //Função para remover Produto do Pedido
    private void removerProdutoDoPedido() {
        if (pedido.getProdutos().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Não há produtos no pedido para remover.");
            return;
        }

        // Cria uma lista de nomes de produtos para exibir na caixa de diálogo
        String[] opcoes = pedido.getProdutos().stream()
                .map(Produto::toString)
                .toArray(String[]::new);

        // Permite ao usuário selecionar um produto para remover
        String produtoSelecionado = (String) JOptionPane.showInputDialog(frame, "Selecione um produto para remover:", "Remover Produto", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        if (produtoSelecionado != null) {
            // Encontra o produto correspondente e o remove
            for (Produto p : pedido.getProdutos()) {
                if (produtoSelecionado.contains(p.getNome())) {
                    pedido.getProdutos().remove(p);
                    JOptionPane.showMessageDialog(frame, "Produto removido do pedido!");
                    break;
                }
            }
        }
    }

    //Função para Finalizar Pedidos
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

            pedido = new Pedido(LoginScreen.getNomeUsuario()); // Reseta o pedido após finalização
            pedidoFrame.dispose(); // Fecha a janela do pedido
        }
    }

    //Função para saída de Histórico de Pedidos
    private void mostrarHistoricoPedidos() {
        JFrame historicoFrame = new JFrame("Histórico de Pedidos");
        historicoFrame.setSize(400, 300);
        historicoFrame.setLayout(new BorderLayout());

        JTextArea textoHistorico = new JTextArea();
        textoHistorico.setEditable(false);
        textoHistorico.setFont(new Font("Arial", Font.PLAIN, 12)); // Define a fonte do texto
        textoHistorico.setLineWrap(true);
        textoHistorico.setWrapStyleWord(true);

        // Carregar histórico de pedidos do banco de dados
        List<String> historico = cardapioDAO.listarHistoricoPedidos();
        StringBuilder sb = new StringBuilder();
        for (String pedido : historico) {
            sb.append(pedido).append("\n");
        }
        textoHistorico.setText(sb.toString());

        // Adiciona um JScrollPane para permitir rolagem
        JScrollPane scrollPane = new JScrollPane(textoHistorico);
        historicoFrame.add(scrollPane, BorderLayout.CENTER);

        // Botão para Gerar PDF
        JButton btnGerarPDF = new JButton("Gerar PDF");
        btnGerarPDF.addActionListener(_ -> gerarRelatorioPDF(historico));
        historicoFrame.add(btnGerarPDF, BorderLayout.EAST);

        // Botão para fechar a janela
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(_ -> historicoFrame.dispose());
        historicoFrame.add(btnFechar, BorderLayout.SOUTH);

        historicoFrame.setVisible(true);
    }

    private void gerarRelatorioPDF(List<String> historico) {
        String caminhoArquivo = "historico_pedidos.pdf"; // Caminho do arquivo PDF
        try {
            PdfWriter writer = new PdfWriter(caminhoArquivo);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Histórico de Pedidos")
                    .setFontSize(18));
            document.add(new LineSeparator(new SolidLine())); // Usando SolidLine para a linha separadora

            for (String pedido : historico) {
                document.add(new Paragraph(pedido));
                document.add(new LineSeparator(new SolidLine())); // Usando SolidLine para a linha separadora
            }

            document.close();
            JOptionPane.showMessageDialog(null, "Relatório PDF gerado com sucesso: " + caminhoArquivo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar relatório PDF: " + e.getMessage());
        }
    }
}