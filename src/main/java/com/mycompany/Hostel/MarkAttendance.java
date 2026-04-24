package com.mycompany.Hostel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import java.sql.*;

public class MarkAttendance extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private final int floorId;
    private final int headGirlId;

    public MarkAttendance(int floorId, int headGirlId) {
        this.floorId    = floorId;
        this.headGirlId = headGirlId;

        setTitle(floorId == -1 ? "Mark Attendance (All Floors)" : "Mark Attendance");
        setSize(620, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(WardenDashboard.BG);
        setLayout(new BorderLayout(0, 8));

        // ── Header ──────────────────────────────────────────
        JPanel header = new JPanel(null);
        header.setBackground(WardenDashboard.CARD);
        header.setPreferredSize(new Dimension(0, 54));
        header.setBorder(BorderFactory.createMatteBorder(0,0,1,0, WardenDashboard.BORDER));

        JLabel title = new JLabel("📋  Mark Attendance");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(WardenDashboard.TEXT);
        title.setBounds(16, 14, 300, 26);
        header.add(title);

        JLabel dateLbl = new JLabel("Date: " + java.time.LocalDate.now());
        dateLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLbl.setForeground(WardenDashboard.MUTED);
        dateLbl.setBounds(400, 18, 200, 20);
        header.add(dateLbl);

        add(header, BorderLayout.NORTH);

        //  Table 
        model = new DefaultTableModel(
            new String[]{"ID","Student Name","Room","Present"}, 0) {
            @Override public Class<?> getColumnClass(int col) {
                return col == 3 ? Boolean.class : String.class;
            }
            @Override public boolean isCellEditable(int row, int col) {
                return col == 3;
            }
        };

        table = new JTable(model);
        table.setBackground(WardenDashboard.ROW_EVEN);
        table.setForeground(WardenDashboard.TEXT);
        table.setGridColor(WardenDashboard.BORDER);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(0x2D4A6B));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0,1));

        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        // Style checkbox column
        table.getColumnModel().getColumn(3).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);

        // Row rendering
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val,
                boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setForeground(WardenDashboard.TEXT);
                if (!sel) {
                    Boolean present = (Boolean) t.getModel().getValueAt(row, 3);
                    c.setBackground(Boolean.TRUE.equals(present)
                        ? new Color(0x14291A)
                        : (row%2==0 ? WardenDashboard.ROW_EVEN : WardenDashboard.ROW_ODD));
                }
                return c;
            }
        });

        JTableHeader th = table.getTableHeader();
        th.setBackground(new Color(0x0D1926));
        th.setForeground(WardenDashboard.MUTED);
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(WardenDashboard.ROW_EVEN);
        scroll.setBorder(BorderFactory.createEmptyBorder(8,8,0,8));
        add(scroll, BorderLayout.CENTER);

        //  Footer 
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 10));
        footer.setBackground(WardenDashboard.CARD);
        footer.setBorder(BorderFactory.createMatteBorder(1,0,0,0, WardenDashboard.BORDER));

        JButton markAllBtn  = WardenDashboard.accentBtn("✅ Mark All Present", WardenDashboard.SUCCESS);
        JButton clearBtn    = WardenDashboard.accentBtn("❌ Clear All",         WardenDashboard.DANGER);
        JButton saveBtn     = WardenDashboard.accentBtn("💾 Save Attendance",   WardenDashboard.ACCENT);

        markAllBtn.setPreferredSize(new Dimension(170, 36));
        clearBtn.setPreferredSize(new Dimension(120, 36));
        saveBtn.setPreferredSize(new Dimension(160, 36));

        footer.add(markAllBtn);
        footer.add(clearBtn);
        footer.add(saveBtn);
        add(footer, BorderLayout.SOUTH);

        markAllBtn.addActionListener(e -> setAllPresent(true));
        clearBtn.addActionListener(e   -> setAllPresent(false));
        saveBtn.addActionListener(e    -> saveAttendance());

        loadStudents();
        setVisible(true);
    }

    
    private void loadStudents() {
    model.setRowCount(0);

    try (Connection con = DBConnection.getConnection()) {
        if (con == null) return;

        String sql;
        PreparedStatement ps;

        if (floorId == -1) {
            // Warden → all students
            sql = "SELECT s.student_id, s.name, r.room_number " +
                  "FROM Student s " +
                  "JOIN Room r ON s.room_id = r.room_id " +
                  "ORDER BY r.room_number, s.name";
            ps = con.prepareStatement(sql);

        } else {
            // HeadGirl → only her floor
            sql = "SELECT s.student_id, s.name, r.room_number " +
                  "FROM Student s " +
                  "JOIN Room r ON s.room_id = r.room_id " +
                  "WHERE r.floor_id = ? " +
                  "ORDER BY r.room_number, s.name";

            ps = con.prepareStatement(sql);
            ps.setInt(1, floorId);
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("student_id"),
                rs.getString("name"),
                rs.getString("room_number"),
                Boolean.FALSE
            });
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No students found.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error loading students: " + e.getMessage());
    }
}

    private void setAllPresent(boolean present) {
        for (int i = 0; i < model.getRowCount(); i++)
            model.setValueAt(present, i, 3);
    }

    private void saveAttendance() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No students to save.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Attendance(student_id, date, status, marked_by) " +
                "VALUES(?, CURDATE(), ?, ?) " +
                "ON DUPLICATE KEY UPDATE status=VALUES(status), marked_by=VALUES(marked_by), marked_at=NOW()");

            for (int i = 0; i < model.getRowCount(); i++) {
                int studentId = (int) model.getValueAt(i, 0);
                boolean present = Boolean.TRUE.equals(model.getValueAt(i, 3));

                ps.setInt(1, studentId);
                ps.setString(2, present ? "Present" : "Absent");
                ps.setInt(3, headGirlId);
                ps.addBatch();
            }

            ps.executeBatch();
            JOptionPane.showMessageDialog(this, "✅ Attendance saved successfully!");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving attendance: " + e.getMessage());
        }
    }
}
