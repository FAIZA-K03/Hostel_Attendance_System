package com.mycompany.Hostel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class WardenDashboard extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField dateField;

    // Palette 
    static final Color BG        = new Color(0x0F1B2D);
    static final Color SIDEBAR   = new Color(0x142035);
    static final Color CARD      = new Color(0x1A2A40);
    static final Color ACCENT    = new Color(0x3B82F6);
    static final Color SUCCESS   = new Color(0x22C55E);
    static final Color DANGER    = new Color(0xEF4444);
    static final Color WARNING   = new Color(0xF59E0B);
    static final Color TEXT      = new Color(0xE2E8F0);
    static final Color MUTED     = new Color(0x64748B);
    static final Color BORDER    = new Color(0x2D3E55);
    static final Color ROW_EVEN  = new Color(0x1A2A40);
    static final Color ROW_ODD   = new Color(0x162237);

    public WardenDashboard() {
        setTitle("Warden Dashboard — Hostel Attendance System");
        setSize(1050, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        // Sidebar 
        JPanel sidebar = buildSidebar();
        add(sidebar, BorderLayout.WEST);

        // Main content
        JPanel main = new JPanel(new BorderLayout(0, 10));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Top bar
        JPanel topBar = buildTopBar();
        main.add(topBar, BorderLayout.NORTH);

        // Table area
        JPanel tableCard = buildTableCard();
        main.add(tableCard, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);

        loadData(java.time.LocalDate.now().toString());
        setVisible(true);
    }

    //  SIDEBAR
    private JPanel buildSidebar() {
        JPanel p = new JPanel();
        p.setBackground(SIDEBAR);
        p.setPreferredSize(new Dimension(210, 0));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));

        // Logo area
        JPanel logo = new JPanel(new BorderLayout());
        logo.setOpaque(false);
        logo.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));
        JLabel logoLbl = new JLabel("<html><span style='font-size:22px'>🏫</span><br/><b>Warden Panel</b></html>");
        logoLbl.setForeground(TEXT);
        logoLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        logo.add(logoLbl);
        p.add(logo);

        p.add(sidebarDivider());

        p.add(sidebarBtn("📋  Today's Attendance",  () -> loadData(java.time.LocalDate.now().toString())));
        p.add(sidebarBtn("📊  All Attendance",       () -> loadData(null)));
        p.add(sidebarBtn("📈  Monthly Report",       () -> ReportService.generateReport()));
        p.add(sidebarBtn("👩‍🎓  Manage Students",     () -> new ManageStudents(-1)));
        p.add(sidebarBtn("👑  Manage Head Girls",    () -> new ManageHeadGirls()));
        p.add(sidebarBtn("📝  Mark Attendance",      () -> new MarkAttendance(-1, 0)));

        p.add(Box.createVerticalGlue());
        p.add(sidebarDivider());
        p.add(sidebarBtn("🚪  Logout", () -> { new LoginFrame(); dispose(); }));
        p.add(Box.createRigidArea(new Dimension(0, 10)));

        return p;
    }

    private JButton sidebarBtn(String text, Runnable action) {
        JButton btn = new JButton(text) {
            boolean hov = false;
            { setOpaque(false); setContentAreaFilled(false); setBorderPainted(false);
              setFocusPainted(false); setHorizontalAlignment(SwingConstants.LEFT);
              setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
              addMouseListener(new MouseAdapter() {
                  public void mouseEntered(MouseEvent e) { hov=true; repaint(); }
                  public void mouseExited (MouseEvent e) { hov=false; repaint(); }
              });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (hov) { g2.setColor(new Color(0x1E3A5A)); g2.fillRect(0,0,getWidth(),getHeight()); }
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT);
        btn.setMaximumSize(new Dimension(210, 42));
        btn.setPreferredSize(new Dimension(210, 42));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JSeparator sidebarDivider() {
        JSeparator s = new JSeparator();
        s.setForeground(BORDER);
        s.setMaximumSize(new Dimension(210, 1));
        return s;
    }

    // TOP BAR 
    private JPanel buildTopBar() {
        JPanel p = new JPanel(null);
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 52));

        JLabel heading = new JLabel("Attendance Records");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(TEXT);
        heading.setBounds(0, 10, 300, 30);
        p.add(heading);

        JLabel dateLbl = new JLabel("Date:");
        dateLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLbl.setForeground(MUTED);
        dateLbl.setBounds(310, 16, 40, 22);
        p.add(dateLbl);

        dateField = new JTextField(java.time.LocalDate.now().toString());
        styleField(dateField);
        dateField.setBounds(350, 12, 140, 30);
        p.add(dateField);

        JButton filterBtn = accentBtn("Filter",   ACCENT);
        JButton allBtn    = accentBtn("All",       MUTED);
        JButton overrideBtn = accentBtn("Override", WARNING);
        JButton exportBtn   = accentBtn("Export CSV", new Color(0x0EA5E9));

        filterBtn.setBounds(500, 12, 80, 30);
        allBtn.setBounds(590, 12, 70, 30);
        overrideBtn.setBounds(680, 12, 100, 30);
        exportBtn.setBounds(790, 12, 110, 30);

        p.add(filterBtn);
        p.add(allBtn);
        p.add(overrideBtn);
        p.add(exportBtn);

        filterBtn.addActionListener(e -> loadData(dateField.getText().trim()));
        allBtn.addActionListener(e -> loadData(null));
        overrideBtn.addActionListener(e -> overrideAttendance());
        exportBtn.addActionListener(e -> exportTable());

        return p;
    }

    // TABLE CARD 
    private JPanel buildTableCard() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));

        model = new DefaultTableModel(
            new String[]{"ID","Student Name","Enrollment","Room","Floor","Status","Marked By","Date"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    String status = (String) getModel().getValueAt(row, 5);
                    if ("Present".equals(status))       c.setBackground(new Color(0x14291A));
                    else if ("Absent".equals(status))   c.setBackground(new Color(0x2A1414));
                    else if ("Leave".equals(status))    c.setBackground(new Color(0x2A2010));
                    else c.setBackground(row%2==0 ? ROW_EVEN : ROW_ODD);
                    c.setForeground(TEXT);
                } else {
                    c.setBackground(new Color(0x2D4A6B));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };

        table.setBackground(ROW_EVEN);
        table.setForeground(TEXT);
        table.setGridColor(BORDER);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(0x2D4A6B));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0,1));

        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0x0D1926));
        header.setForeground(MUTED);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(BorderFactory.createMatteBorder(0,0,1,0,BORDER));
        header.setReorderingAllowed(false);

        // Column widths
        int[] widths = {0, 180, 120, 80, 130, 80, 120, 100};
        for (int i=0; i<widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(CARD);
        scroll.getViewport().setBackground(ROW_EVEN);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    // LOAD DATA 
    void loadData(String dateStr) {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;

            String sql =
                "SELECT a.attendance_id, s.name, s.enrollment_no, r.room_number, " +
                "f.floor_name, a.status, " +
                "COALESCE(hg.name, 'Warden') AS marked_by, a.date " +
                "FROM Attendance a " +
                "JOIN Student s ON a.student_id = s.student_id " +
                "JOIN Room r ON s.room_id = r.room_id " +
                "JOIN Floor f ON r.floor_id = f.floor_id " +
                "LEFT JOIN HeadGirl hg ON a.marked_by = hg.headgirl_id ";

            if (dateStr != null && !dateStr.isEmpty())
                sql += "WHERE a.date = ? ";

            sql += "ORDER BY a.date DESC, f.floor_name, s.name";

            PreparedStatement ps = con.prepareStatement(sql);
            if (dateStr != null && !dateStr.isEmpty())
                ps.setString(1, dateStr);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("attendance_id"),
                    rs.getString("name"),
                    rs.getString("enrollment_no"),
                    rs.getString("room_number"),
                    rs.getString("floor_name"),
                    rs.getString("status"),
                    rs.getString("marked_by"),
                    rs.getDate("date")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    //  OVERRIDE 
    private void overrideAttendance() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row first.");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        String cur = (String) model.getValueAt(row, 5);

        String[] opts = {"Present","Absent","Leave"};
        String newStatus = (String) JOptionPane.showInputDialog(
            this, "Change status for selected record:", "Override Attendance",
            JOptionPane.QUESTION_MESSAGE, null, opts, cur);

        if (newStatus == null || newStatus.equals(cur)) return;

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE Attendance SET status=? WHERE attendance_id=?");
            ps.setString(1, newStatus);
            ps.setInt(2, id);
            ps.executeUpdate();
            model.setValueAt(newStatus, row, 5);
            JOptionPane.showMessageDialog(this, "Attendance updated successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // EXPORT
    private void exportTable() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("attendance_export.csv"));
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

            java.io.FileWriter fw = new java.io.FileWriter(chooser.getSelectedFile());
            // Header
            fw.write("Student Name,Enrollment,Room,Floor,Status,Marked By,Date\n");
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 1; j < model.getColumnCount(); j++) {
                    if (j > 1) sb.append(",");
                    Object v = model.getValueAt(i,j);
                    sb.append(v != null ? v.toString() : "");
                }
                fw.write(sb.toString() + "\n");
            }
            fw.close();
            JOptionPane.showMessageDialog(this, "Exported successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage());
        }
    }

    // Button factory 
    static JButton accentBtn(String text, Color color) {
        JButton btn = new JButton(text) {
            boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hov=true; repaint(); }
                public void mouseExited (MouseEvent e) { hov=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = color;
                g2.setColor(hov ? base.brighter() : base);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setForeground(TEXT);
        f.setBackground(new Color(0x0F1B2D));
        f.setCaretColor(TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LoginFrame.RoundBorder(8, BORDER),
            BorderFactory.createEmptyBorder(0,8,0,8)
        ));
    }
}
