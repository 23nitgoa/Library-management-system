package library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddBook extends JFrame {

    public AddBook() {
        setTitle("Add New Book");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        setContentPane(new JLabel(new ImageIcon("src/image/image1.jpg")));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        Font font = new Font("Segoe UI", Font.BOLD, 14);

        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBackground(new Color(255, 255, 255, 180));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

       
        JTextField bookID = new JTextField(15);
        JTextField title = new JTextField(15);
        JTextField author = new JTextField(15);
        JTextField publisher = new JTextField(15);
        JTextField quantity = new JTextField(15);

        
        panel.add(new JLabel("Book ID:", JLabel.RIGHT)); panel.add(bookID);
        panel.add(new JLabel("Title:", JLabel.RIGHT)); panel.add(title);
        panel.add(new JLabel("Author:", JLabel.RIGHT)); panel.add(author);
        panel.add(new JLabel("Publisher:", JLabel.RIGHT)); panel.add(publisher);
        panel.add(new JLabel("Quantity:", JLabel.RIGHT)); panel.add(quantity);

        
        JButton addBtn = new JButton("Add Book");
        addBtn.setFont(font);
        addBtn.setFocusPainted(false);
        addBtn.setBackground(new Color(70, 130, 180));
        addBtn.setForeground(Color.WHITE);

        addBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                addBtn.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(MouseEvent evt) {
                addBtn.setBackground(new Color(70, 130, 180));
            }
        });

        
        addBtn.addActionListener(e -> {
            String id = bookID.getText();
            String bTitle = title.getText();
            String bAuthor = author.getText();
            String bPublisher = publisher.getText();
            String bQty = quantity.getText();

            try {
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "soumya04@4567");

                String query = "INSERT INTO books (book_id, title, author, publisher, quantity) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, id);
                stmt.setString(2, bTitle);
                stmt.setString(3, bAuthor);
                stmt.setString(4, bPublisher);
                stmt.setInt(5, Integer.parseInt(bQty));

                int inserted = stmt.executeUpdate();
                if (inserted > 0) {
                    JOptionPane.showMessageDialog(null, "Book Added Successfully!");
                    bookID.setText(""); title.setText(""); author.setText(""); publisher.setText(""); quantity.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add book.");
                }

                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        panel.add(new JLabel(""));
        panel.add(addBtn);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panel, gbc);

        setVisible(true);
    }
}
