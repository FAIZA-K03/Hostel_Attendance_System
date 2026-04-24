package com.mycompany.Hostel;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class HeadGirlDashboard extends JFrame {

    private final int floorId;
    private final int headGirlId;
    private final String hgName;

    public HeadGirlDashboard(int floorId, int headGirlId, String hgName) {
        this.floorId    = floorId;
        this.headGirlId = headGirlId;
        this.hgName     = hgName;

        setTitle("Head Girl Dashboard");
        setSize(480, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        Color BG      = WardenDashboard.BG;
        Color CARD    = WardenDashboard.CARD;
        Color ACCENT  = WardenDashboard.ACCENT;
        Color SUCCESS = WardenDashboard.SUCCESS;
        Color TEXT    = WardenDashboard.TEXT;
        Color MUTED   = WardenDashboard.MUTED;
        Color BORDER  = WardenDashboard.BORDER;

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(BG);
                g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        setContentPane(root);

        // ── Card ────────────────────────────────────────────
        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2.setColor(BORDER);
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,16,16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(400, 430));

        // Avatar circle
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1E3A5A));
                g2.fillOval(0,0,64,64);
                g2.setColor(ACCENT);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("👑", (64-fm.stringWidth("👑"))/2, 45);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setBounds(168, 24, 64, 64);
        card.add(avatar);

        JLabel nameLabel = new JLabel("Welcome, " + hgName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(TEXT);
        nameLabel.setBounds(0, 98, 400, 28);
        card.add(nameLabel);

        JLabel roleLabel = new JLabel("Head Girl", SwingConstants.CENTER);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(ACCENT);
        roleLabel.setBounds(0, 126, 400, 20);
        card.add(roleLabel);

        String floorName = getFloorName();
        JLabel floorLabel = new JLabel("📍 " + floorName, SwingConstants.CENTER);
        floorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        floorLabel.setForeground(MUTED);
        floorLabel.setBounds(0, 148, 400, 20);
        card.add(floorLabel);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setBounds(30, 178, 340, 1);
        card.add(sep);

        // ── Action buttons ───────────────────────────────────
        JButton markBtn   = buildDashBtn("📋  Mark Attendance",   SUCCESS, 196);
        JButton studBtn   = buildDashBtn("👩‍🎓  My Floor Students", new Color(0x7C3AED), 256);
        JButton reportBtn = buildDashBtn("📊  View Floor Report",  ACCENT, 316);
        JButton logoutBtn = buildDashBtn("🚪  Logout",             new Color(0x475569), 376);

        card.add(markBtn);
        card.add(studBtn);
        card.add(reportBtn);
        card.add(logoutBtn);

        markBtn.addActionListener(e   -> new MarkAttendance(floorId, headGirlId));
        studBtn.addActionListener(e   -> new ManageStudents(floorId));
        reportBtn.addActionListener(e -> ReportService.generateFloorReport(floorId));
        logoutBtn.addActionListener(e -> { new LoginFrame(); dispose(); });

        root.add(card);
        setVisible(true);
    }

    private JButton buildDashBtn(String text, Color color, int y) {
        JButton btn = WardenDashboard.accentBtn(text, color);
        btn.setBounds(30, y, 340, 44);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }

    private String getFloorName() {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return "Floor #" + floorId;
            PreparedStatement ps = con.prepareStatement(
                "SELECT floor_name FROM Floor WHERE floor_id=?");
            ps.setInt(1, floorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("floor_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Floor #" + floorId;
    }
}
