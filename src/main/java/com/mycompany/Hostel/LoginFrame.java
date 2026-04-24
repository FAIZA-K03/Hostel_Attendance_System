package com.mycompany.Hostel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;

    //  Palette 
    private static final Color BG         = new Color(0x0F1B2D);
    private static final Color CARD       = new Color(0x1A2A40);
    private static final Color ACCENT     = new Color(0x3B82F6);
    private static final Color ACCENT_HOV = new Color(0x2563EB);
    private static final Color TEXT       = new Color(0xE2E8F0);
    private static final Color MUTED      = new Color(0x64748B);
    private static final Color FIELD_BG   = new Color(0x0F1B2D);
    private static final Color BORDER_COL = new Color(0x2D3E55);

    public LoginFrame() {
        setTitle("Hostel Attendance System — Login");
        setSize(420, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        //  Root panel 
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0, BG, getWidth(), getHeight(), new Color(0x0A1628));
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        setContentPane(root);

        //  Card 
        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                g2.setColor(BORDER_COL);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(340, 380));

        //  Header 
        JLabel icon = new JLabel("🏠", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        icon.setBounds(0, 28, 340, 44);
        card.add(icon);

        JLabel title = new JLabel("Hostel Attendance", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT);
        title.setBounds(0, 75, 340, 28);
        card.add(title);

        JLabel sub = new JLabel("Sign in to continue", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(MUTED);
        sub.setBounds(0, 103, 340, 20);
        card.add(sub);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COL);
        sep.setBounds(30, 132, 280, 1);
        card.add(sep);

        //  Username
        JLabel userLbl = new JLabel("Username");
        userLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLbl.setForeground(MUTED);
        userLbl.setBounds(30, 150, 280, 18);
        card.add(userLbl);

        userField = styledField();
        userField.setBounds(30, 170, 280, 38);
        card.add(userField);

        // Password 
        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passLbl.setForeground(MUTED);
        passLbl.setBounds(30, 218, 280, 18);
        card.add(passLbl);

        passField = new JPasswordField();
        styleBase(passField);
        passField.setBounds(30, 238, 280, 38);
        card.add(passField);

        // Login button
        JButton loginBtn = new JButton("Sign In") {
            boolean hovered = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered=true; repaint(); }
                public void mouseExited(MouseEvent e)  { hovered=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? ACCENT_HOV : ACCENT);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBounds(30, 295, 280, 44);
        loginBtn.setBorderPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.add(loginBtn);

        JLabel copy = new JLabel("Begum Azeezun Nisa Hall, AMU", SwingConstants.CENTER);
        copy.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        copy.setForeground(MUTED);
        copy.setBounds(0, 350, 340, 18);
        card.add(copy);

        loginBtn.addActionListener(e -> login());
        passField.addActionListener(e -> login());
        userField.addActionListener(e -> passField.requestFocus());

        root.add(card);
        setVisible(true);
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        styleBase(f);
        return f;
    }

    private void styleBase(JComponent f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setForeground(TEXT);
        f.setBackground(FIELD_BG);
//        f.setCaretColor(TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(8, BORDER_COL),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        f.setOpaque(true);
    }

    private void login() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            showError("Please enter username and password.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                showError("Could not connect to database.");
                return;
            }

            // Check Warden
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM Warden WHERE username=? AND password=?");
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dispose();
                new WardenDashboard();
                return;
            }

            // Check HeadGirl
            ps = con.prepareStatement(
                "SELECT * FROM HeadGirl WHERE username=? AND password=?");
            ps.setString(1, user);
            ps.setString(2, pass);
            rs = ps.executeQuery();

            if (rs.next()) {
                int floorId    = rs.getInt("floor_id");
                int hgId       = rs.getInt("headgirl_id");
                String hgName  = rs.getString("name");
                dispose();
                new HeadGirlDashboard(floorId, hgId, hgName);
                return;
            }

            showError("Invalid username or password.");

        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    //  Rounded border helper 
    static class RoundBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        RoundBorder(int radius, Color color) { this.radius=radius; this.color=color; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w-1, h-1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(4,8,4,8); }
        @Override public boolean isBorderOpaque() { return false; }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
