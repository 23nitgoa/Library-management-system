package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculateFine extends JFrame {

    public CalculateFine() {
        setTitle("Calculate Fine");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        setContentPane(new JLabel(new ImageIcon("src/image/image1.jpg")));
        setLayout(new BorderLayout());

        Font font = new Font("Segoe UI", Font.BOLD, 14);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        topPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Enter Username:");
        userLabel.setFont(font);
        userLabel.setForeground(Color.white); 

        JTextField userField = new JTextField(15);
        JButton calcBtn = new JButton("Calculate");

        calcBtn.setFont(font);
        calcBtn.setBackground(new Color(70, 130, 180));
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setFocusPainted(false);

        
        calcBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                calcBtn.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                calcBtn.setBackground(new Color(70, 130, 180));
            }
        });

        topPanel.add(userLabel);
        topPanel.add(userField);
        topPanel.add(calcBtn);

        
        String[] columns = { "Book ID", "Title", "Issue Date", "Return Date", "Fine (₹)" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        calcBtn.addActionListener(e -> {
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
                """;

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String bookId = rs.getString("book_id");
                    String title = rs.getString("title");
                    LocalDate issueDate = rs.getDate("issue_date").toLocalDate();
                    Date returnDateSQL = rs.getDate("return_date");
                    LocalDate currentDate = LocalDate.now();

                    LocalDate dueDate = issueDate.plusDays(1); 
                    long fine = 0;

                    if (returnDateSQL == null) {
                        long daysLate = ChronoUnit.DAYS.between(dueDate, currentDate);
                        fine = daysLate > 0 ? daysLate * 5 : 0;
                    }

                    model.addRow(new Object[] {
                        bookId,
                        title,
                        issueDate,
                        returnDateSQL != null ? returnDateSQL.toString() : "Not Returned",
                        "₹" + fine
                    });
                }

                conn.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}
