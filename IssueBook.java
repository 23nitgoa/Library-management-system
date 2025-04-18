package library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class IssueBook extends JFrame {


	public IssueBook() {
        setTitle("Issue Book");
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

        JButton issueBtn = new JButton("Issue Book");
        issueBtn.setFont(font);
        issueBtn.setBackground(new Color(70, 130, 180));
        issueBtn.setForeground(Color.WHITE);
        issueBtn.setFocusPainted(false);

        
        issueBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                issueBtn.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(MouseEvent evt) {
                issueBtn.setBackground(new Color(70, 130, 180));
            }
        });

        panel.add(new JLabel("Username:", JLabel.RIGHT));
        panel.add(userField);
        panel.add(new JLabel("Book ID:", JLabel.RIGHT));
        panel.add(bookIdField);
        panel.add(new JLabel(""));
        panel.add(issueBtn);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panel, gbc);

        
        issueBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String bookId = bookIdField.getText().trim();
            LocalDate today = LocalDate.now();
            LocalDate dueDate = today.plusDays(1); // Return date is 1 day after issue


            if (username.isEmpty() || bookId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields.");
                return;
            }

            try {
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "soumya04@4567");

                
                String checkQuery = "SELECT quantity FROM books WHERE book_id=?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, bookId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt("quantity") > 0) {
                    
                    String insertQuery = "INSERT INTO issued_books (username, book_id, issue_date, return_date) VALUES (?, ?, ?, ?)";
                    PreparedStatement issueStmt = conn.prepareStatement(insertQuery);
                    issueStmt.setString(1, username);
                    issueStmt.setString(2, bookId);
                    issueStmt.setDate(3, Date.valueOf(today));
                    issueStmt.setDate(4, Date.valueOf(dueDate));
                    issueStmt.executeUpdate();

                    
                    String updateQuery = "UPDATE books SET quantity = quantity - 1 WHERE book_id=?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setString(1, bookId);
                    updateStmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Book issued successfully!\nReturn Date: " + dueDate);

                } else {
                    JOptionPane.showMessageDialog(null, "Book not available.");
                }

                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}
