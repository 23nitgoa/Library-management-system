package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReadingHistory extends JFrame {

    public ReadingHistory() {
        setTitle("Reading History");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        setContentPane(new JLabel(new ImageIcon("src/image/image1.jpg")));
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        inputPanel.setOpaque(false);
        Font font = new Font("Segoe UI", Font.BOLD, 14);

        JLabel userLabel = new JLabel("Enter Username:");
        userLabel.setFont(font);
        userLabel.setForeground(Color.WHITE); 

        JTextField userField = new JTextField(15);
        JButton fetchBtn = new JButton("Fetch History");

        fetchBtn.setFont(font);
        fetchBtn.setBackground(new Color(70, 130, 180));
        fetchBtn.setForeground(Color.WHITE);
        fetchBtn.setFocusPainted(false);

       
        fetchBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fetchBtn.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                fetchBtn.setBackground(new Color(70, 130, 180));
            }
        });

        inputPanel.add(userLabel);
        inputPanel.add(userField);
        inputPanel.add(fetchBtn);

        
        String[] columns = { "Book ID", "Title", "Issue Date", "Return Date" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

       
        fetchBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter username!");
                return;
            }

            model.setRowCount(0); 

            try {
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "soumya04@4567");

                String query = """
                    SELECT i.book_id, b.title, i.issue_date, i.return_date
                    FROM issued_books i
                    JOIN books b ON i.book_id = b.book_id
                    WHERE i.username = ?
                    ORDER BY i.issue_date DESC
                """;

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String bookId = rs.getString("book_id");
                    String title = rs.getString("title");
                    String issueDate = rs.getString("issue_date");
                    String returnDate = rs.getString("return_date");

                    model.addRow(new Object[] { bookId, title, issueDate, returnDate != null ? returnDate : "Not Returned" });
                }

                conn.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}
