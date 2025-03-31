package senai.felp;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginScreen extends JFrame {
    private static JTextField userField = null;
    private final JPasswordField passField;

    public LoginScreen() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(50, 50, 50));

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBackground(new Color(255, 69, 0));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel userLabel = new JLabel("Usuário:");
        userLabel.setForeground(Color.WHITE);
        userField = new JTextField();

        JLabel passLabel = new JLabel("Senha:");
        passLabel.setForeground(Color.WHITE);
        passField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Registrar");

        formPanel.add(userLabel);
        formPanel.add(userField);
        formPanel.add(passLabel);
        formPanel.add(passField);
        formPanel.add(loginButton);
        formPanel.add(registerButton);

        panel.add(formPanel, BorderLayout.CENTER);
        add(panel);

        loginButton.addActionListener(_ -> autenticarUsuario());
        registerButton.addActionListener(_ -> new RegisterScreen().setVisible(true));
    }

    private void autenticarUsuario() {
        String usuario = userField.getText();
        String senha = new String(passField.getPassword());

        if (validarLogin(usuario, senha)) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
            SwingUtilities.invokeLater(() -> new FastFoodGUI(usuario));
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarLogin(String usuario, String senha) {
        String url = "jdbc:sqlite:fastfood.db";
        String sql = "SELECT * FROM usuarios WHERE nome = ? AND senha = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getNomeUsuario() {
        return userField.getText();
    }
}

class RegisterScreen extends JFrame {
    private final JTextField userField;
    private final JPasswordField passField;

    public RegisterScreen() {
        setTitle("Registro");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(50, 50, 50));

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBackground(new Color(255, 69, 0));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel userLabel = new JLabel("Novo Usuário:");
        userLabel.setForeground(Color.WHITE);
        userField = new JTextField();

        JLabel passLabel = new JLabel("Nova Senha:");
        passLabel.setForeground(Color.WHITE);
        passField = new JPasswordField();

        JButton registerButton = new JButton("Registrar");
        JButton loginButton = new JButton("Entrar");

        formPanel.add(userLabel);
        formPanel.add(userField);
        formPanel.add(passLabel);
        formPanel.add(passField);
        formPanel.add(registerButton);
        formPanel.add(loginButton);

        panel.add(formPanel, BorderLayout.CENTER);
        add(panel);

        registerButton.addActionListener(_ -> registrarUsuario());
        loginButton.addActionListener(_ -> {
            new LoginScreen().setVisible(true);
            dispose();
        });
    }

    private void registrarUsuario() {
        String usuario = userField.getText();
        String senha = new String(passField.getPassword());

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:sqlite:fastfood.db";
        String sql = "INSERT INTO usuarios (nome, senha) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuário registrado com sucesso!");
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao registrar usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
