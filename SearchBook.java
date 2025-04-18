package library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SearchBook extends JFrame {

    public SearchBook() {
        setTitle("Search Book");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

       
        setContentPane(new JLabel(new ImageIcon("src/image/image1.jpg")));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        Font font = new Font("Segoe UI", Font.BOLD, 14);

        
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(new Color(255, 255, 255, 180));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");

        searchBtn.setFont(font);
        searchBtn.setBackground(new Color(70, 130, 180));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);

        
        searchBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                searchBtn.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(MouseEvent evt) {
                searchBtn.setBackground(new Color(70, 130, 180));
            }
        });

        panel.add(new JLabel("Enter Book Title:", JLabel.RIGHT));
        panel.add(searchField);
        panel.add(new JLabel(""));
        panel.add(searchBtn);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panel, gbc);

        
        JTextArea resultArea = new JTextArea(8, 40);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultArea.setEditable(false);
        resultArea.setOpaque(true);
        resultArea.setBackground(new Color(255, 255, 255, 200));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        add(scrollPane, gbc);

       
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();

            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a book title to search.");
                return;
            }

            try {
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "soumya04@4567");

                String query = "SELECT * FROM books WHERE title LIKE ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "%" + keyword + "%");

                ResultSet rs = stmt.executeQuery();
                StringBuilder results = new StringBuilder();

                while (rs.next()) {
                    results.append("ID: ").append(rs.getString("book_id")).append("\n")
                           .append("Title: ").append(rs.getString("title")).append("\n")
                           .append("Author: ").append(rs.getString("author")).append("\n")
                           .append("Publisher: ").append(rs.getString("publisher")).append("\n")
                           .append("Quantity: ").append(rs.getInt("quantity")).append("\n\n");
                }

                if (results.length() == 0) {
                    resultArea.setText("No books found with title: " + keyword);
                } else {
                    resultArea.setText(results.toString());
                }

                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}
