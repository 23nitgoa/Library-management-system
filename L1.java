package library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class L1 extends JFrame {

    public L1() {
        setTitle("Login Page");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       
        setContentPane(new JLabel(new ImageIcon("src/image/image1.jpg")));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        Font font = new Font("Segoe UI", Font.BOLD, 14);

     
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(new Color(255, 255, 255, 180));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(font);
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(font);
        JPasswordField passField = new JPasswordField(15);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(font);

 
        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(70, 130, 180));
            }
        });

      
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel(""));
        panel.add(loginBtn);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panel, gbc);

        
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            try {
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "soumya04@4567");

                String query = "SELECT * FROM users WHERE username=? AND password=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    dispose();
                    new Dashboard(username);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password");
                }

                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new L1();
    }
}
