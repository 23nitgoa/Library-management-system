package library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class ReturnBook extends JFrame {

    public ReturnBook() {
        setTitle("Return Book");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        setContentPane(new JLabel(new ImageIcon("src/image/image1.jpg")));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        Font font = new Font("Segoe UI", Font.BOLD, 14);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(new Color(255, 255, 255, 180));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JTextField userField = new JTextField(15);
        JTextField bookIdField = new JTextField(15);

        JButton returnBtn = new JButton("Return Book");
        returnBtn.setFont(font);
        returnBtn.setBackground(new Color(70, 130, 180));
        returnBtn.setForeground(Color.WHITE);
        returnBtn.setFocusPainted(false);

        
        returnBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                returnBtn.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(MouseEvent evt) {
                returnBtn.setBackground(new Color(70, 130, 180));
            }
        });

        panel.add(new JLabel("Username:", JLabel.RIGHT));
        panel.add(userField);
        panel.add(new JLabel("Book ID:", JLabel.RIGHT));
        panel.add(bookIdField);
        panel.add(new JLabel(""));
        panel.add(returnBtn);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panel, gbc);

        
        returnBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String bookId = bookIdField.getText().trim();
            LocalDate returnDate = LocalDate.now();

            if (username.isEmpty() || bookId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields.");
                return;
            }

            try {
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "soumya04@4567");

                
                String checkQuery = "SELECT * FROM issued_books WHERE username=? AND book_id=? AND return_date";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, username);
                checkStmt.setString(2, bookId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    
                    String updateIssue = "UPDATE issued_books SET return_date=? WHERE username=? AND book_id=?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateIssue);
                    updateStmt.setDate(1, Date.valueOf(returnDate));
                    updateStmt.setString(2, username);
                    updateStmt.setString(3, bookId);
                    updateStmt.executeUpdate();

                    
                    String updateQty = "UPDATE books SET quantity = quantity + 1 WHERE book_id=?";
                    PreparedStatement qtyStmt = conn.prepareStatement(updateQty);
                    qtyStmt.setString(1, bookId);
                    qtyStmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Book returned successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "No such issued book found!");
                }

                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}
