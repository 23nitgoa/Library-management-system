package library;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard(String username) {
        setTitle("Dashboard - Welcome " + username);
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        setContentPane(new JLabel(new ImageIcon("src/image/image1.jpg")));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        
        Font font = new Font("Segoe UI", Font.BOLD, 14);

        
        JLabel label = new JLabel("Welcome, " + username, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 10, 20, 10);
        add(label, gbc);

        
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new GridLayout(9, 1, 10, 10));
        buttonHolder.setOpaque(false); 

        String[] actions = {
            "Issue Book", "Return Book", "View Issued Books", "Add New Book",
            "Search Book", "Reading History", "Calculate Fine", "View All Books", "Logout"
        };

        for (String action : actions) {
            JButton btn = new JButton(action);
            btn.setFont(font);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(70, 130, 180));
            btn.setForeground(Color.WHITE);

            
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(100, 149, 237));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(70, 130, 180));
                }
            });

            
            switch (action) {
                case "Issue Book" -> btn.addActionListener(e -> new IssueBook());
                case "Return Book" -> btn.addActionListener(e -> new ReturnBook());
                case "View Issued Books" -> btn.addActionListener(e -> new IssuedBooks());
                case "Add New Book" -> btn.addActionListener(e -> new AddBook());
                case "Search Book" -> btn.addActionListener(e -> new SearchBook());
                case "Reading History" -> btn.addActionListener(e -> new ReadingHistory());
                case "Calculate Fine" -> btn.addActionListener(e -> new CalculateFine());
                case "View All Books" -> btn.addActionListener(e -> new AllBooks());
                case "Logout" -> btn.addActionListener(e -> {
                    dispose();
                    new L1();
                });
            }

            buttonHolder.add(btn);
        }

        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(buttonHolder, gbc);

        setVisible(true);
    }
}
