package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AllBooks extends JFrame {

    public AllBooks() {
        setTitle("All Books");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        setContentPane(new JLabel(new ImageIcon("src/image/image1.jpg")));
        setLayout(new BorderLayout());

        Font font = new Font("Segoe UI", Font.BOLD, 14);

        
        JLabel topLabel = new JLabel("List of All Books", SwingConstants.CENTER);
        topLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topLabel.setForeground(Color.WHITE);
        topLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(topLabel, BorderLayout.NORTH);

       
        String[] columns = { "Book ID", "Title", "Author", "Publisher", "Year", "Genre", "Quantity" };
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

            String query = "SELECT * FROM books";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("publisher"),
                    rs.getString("year"),
                    rs.getString("genre"),
                    rs.getInt("quantity")
                });
            }

            conn.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }

        setVisible(true);
    }
}
