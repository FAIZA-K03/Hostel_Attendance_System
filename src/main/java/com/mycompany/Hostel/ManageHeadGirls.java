package com.mycompany.Hostel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class ManageHeadGirls extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField, userField, passField, floorField;

    public ManageHeadGirls() {
        setTitle("Manage Head Girls");
        setSize(720, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(WardenDashboard.BG);
        setLayout(new BorderLayout(0, 0));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadHG();
        setVisible(true);
    }

    private JPanel buildFormPanel() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(WardenDashboard.CARD);
        wrap.setBorder(BorderFactory.createMatteBorder(0,0,1,0, WardenDashboard.BORDER));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        form.setBackground(WardenDashboard.CARD);

        nameField  = styledField(14);
        userField  = styledField(14);
        passField  = styledField(12);
        floorField = styledField(6);

        form.add(lbl("Name:"));     form.add(nameField);
        form.add(lbl("Username:")); form.add(userField);
        form.add(lbl("Password:")); form.add(passField);
        form.add(lbl("Floor ID:")); form.add(floorField);

        JButton addBtn    = WardenDashboard.accentBtn("➕ Add Head Girl", WardenDashboard.SUCCESS);
        JButton deleteBtn = WardenDashboard.accentBtn("🗑 Delete",         WardenDashboard.DANGER);

        addBtn.setPreferredSize(new Dimension(140, 32));
        deleteBtn.setPreferredSize(new Dimension(100, 32));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btnRow.setBackground(WardenDashboard.CARD);
        btnRow.add(addBtn);
        btnRow.add(deleteBtn);

        addBtn.addActionListener(e    -> addHG());
        deleteBtn.addActionListener(e -> deleteHG());

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(WardenDashboard.CARD);
        inner.setBorder(BorderFactory.createEmptyBorder(4,12,4,12));
        inner.add(form, BorderLayout.CENTER);
        inner.add(btnRow, BorderLayout.SOUTH);
        wrap.add(inner);
        return wrap;
    }

    private JPanel buildTablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(WardenDashboard.BG);
        p.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        model = new DefaultTableModel(
            new String[]{"ID","Name","Username","Floor ID"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setBackground(WardenDashboard.ROW_EVEN);
        table.setForeground(WardenDashboard.TEXT);
        table.setGridColor(WardenDashboard.BORDER);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(0x2D4A6B));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0x0D1926));
        header.setForeground(WardenDashboard.MUTED);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(WardenDashboard.ROW_EVEN);
        scroll.setBorder(BorderFactory.createLineBorder(WardenDashboard.BORDER));

        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private void loadHG() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT headgirl_id, name, username, floor_id FROM HeadGirl ORDER BY name");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void addHG() {
        if (nameField.getText().isEmpty() || userField.getText().isEmpty() ||
            passField.getText().isEmpty() || floorField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }
        try {
            Integer.parseInt(floorField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Floor ID must be a number.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO HeadGirl(name,username,password,floor_id) VALUES(?,?,?,?)");
            ps.setString(1, nameField.getText().trim());
            ps.setString(2, userField.getText().trim());
            ps.setString(3, passField.getText().trim());
            ps.setInt(4, Integer.parseInt(floorField.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Head Girl added successfully.");
            loadHG();
            nameField.setText(""); userField.setText(""); passField.setText(""); floorField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteHG() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a Head Girl to delete."); return; }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete '" + model.getValueAt(row,1) + "'?", "Confirm",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;
            con.createStatement().executeUpdate("DELETE FROM HeadGirl WHERE headgirl_id=" + id);
            loadHG();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private JTextField styledField(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setForeground(WardenDashboard.TEXT);
        f.setBackground(new Color(0x0F1B2D));
        f.setCaretColor(WardenDashboard.TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LoginFrame.RoundBorder(6, WardenDashboard.BORDER),
            BorderFactory.createEmptyBorder(2,6,2,6)
        ));
        f.setPreferredSize(new Dimension(f.getPreferredSize().width, 28));
        return f;
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(WardenDashboard.MUTED);
        return l;
    }
}
