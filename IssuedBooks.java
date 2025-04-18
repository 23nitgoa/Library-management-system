package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class IssuedBooks extends JFrame {

    public IssuedBooks() {
        setTitle("Issued Books");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        setContentPane(new JLabel(new ImageIcon("src/image/image1.jpg")));
        setLayout(new BorderLayout());

        Font font = new Font("Segoe UI", Font.BOLD, 14);

        
        JLabel topLabel = new JLabel("Issued Books List", SwingConstants.CENTER);
        topLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topLabel.setForeground(Color.WHITE);
        topLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(topLabel, BorderLayout.NORTH);

        
        String[] columns = { "Book ID", "Title", "Username", "Issue Date", "Return Date" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db", "root", "soumya04@4567");

            String query = """
                SELECT i.book_id, b.title, i.username, i.issue_date, i.return_date
                FROM issued_books i
                JOIN books b ON i.book_id = b.book_id
            """;
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("book_id"),
                    rs.getString("title"),
                    rs.getString("username"),
                    rs.getDate("issue_date"),
                    rs.getDate("return_date") != null ? rs.getDate("return_date").toString() : "Not Returned"
                });
            }

            conn.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }

        setVisible(true);
    }
}
