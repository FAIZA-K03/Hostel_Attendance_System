package com.mycompany.Hostel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.time.*;


public class ReportService {

    
    public static void generateReport() {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;

            String query =
                "SELECT s.name, s.enrollment_no, f.floor_name, " +
                "COUNT(CASE WHEN a.status='Present' THEN 1 END) AS present_days, " +
                "COUNT(*) AS total_days, " +
                "ROUND(COUNT(CASE WHEN a.status='Present' THEN 1 END)*100.0/NULLIF(COUNT(*),0), 2) AS percentage " +
                "FROM Attendance a " +
                "JOIN Student s ON a.student_id=s.student_id " +
                "JOIN Room r ON s.room_id=r.room_id " +
                "JOIN Floor f ON r.floor_id=f.floor_id " +
                "WHERE MONTH(a.date)=MONTH(CURDATE()) AND YEAR(a.date)=YEAR(CURDATE()) " +
                "GROUP BY s.student_id, s.name, s.enrollment_no, f.floor_name " +
                "ORDER BY percentage ASC";

            ResultSet rs = con.createStatement().executeQuery(query);

            Month month = LocalDate.now().getMonth();
            int year = LocalDate.now().getYear();

            StringBuilder sb = new StringBuilder();
            sb.append("========================================\n");
            sb.append("   MONTHLY ATTENDANCE REPORT\n");
            sb.append("   Begum Azeezun Nisa Hall, AMU\n");
            sb.append("   Month: ").append(month.name()).append(" ").append(year).append("\n");
            sb.append("========================================\n\n");
            sb.append(String.format("%-28s %-14s %-22s %10s %7s\n",
                "Name","Enrollment","Floor","Present","  %"));
            sb.append("-".repeat(85)).append("\n");

            int defaulterCount = 0, totalStudents = 0;
            StringBuilder defaulters = new StringBuilder();

            while (rs.next()) {
                double pct     = rs.getDouble("percentage");
                int present    = rs.getInt("present_days");
                int total      = rs.getInt("total_days");
                totalStudents++;

                String flag = pct < 75 ? "  *** DEFAULTER" : "";
                if (pct < 75) {
                    defaulterCount++;
                    defaulters.append("  • ").append(rs.getString("name"))
                        .append(" (").append(rs.getString("enrollment_no"))
                        .append(") — ").append(String.format("%.1f", pct)).append("%\n");
                }

                sb.append(String.format("%-28s %-14s %-22s %3d/%-5d %5.1f%%%s\n",
                    rs.getString("name"), rs.getString("enrollment_no"),
                    rs.getString("floor_name"), present, total, pct, flag));
            }

            sb.append("\n========================================\n");
            sb.append("SUMMARY\n");
            sb.append("Total Students : ").append(totalStudents).append("\n");
            sb.append("Defaulters (<75%): ").append(defaulterCount).append("\n");
            sb.append("========================================\n");
            if (defaulterCount > 0)
                sb.append("\nDEFAULTERS:\n").append(defaulters);

            showReport(sb.toString(), "Monthly Attendance Report — " + month + " " + year);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error generating report: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── FLOOR REPORT (HeadGirl / Warden per floor) ────────────
    public static void generateFloorReport(int floorId) {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;

            PreparedStatement fpq = con.prepareStatement(
                "SELECT floor_name FROM Floor WHERE floor_id=?");
            fpq.setInt(1, floorId);
            ResultSet frs = fpq.executeQuery();
            String floorName = frs.next() ? frs.getString("floor_name") : "Floor #" + floorId;

            String query =
                "SELECT s.name, s.enrollment_no, " +
                "COUNT(CASE WHEN a.status='Present' THEN 1 END) AS present_days, " +
                "COUNT(*) AS total_days, " +
                "ROUND(COUNT(CASE WHEN a.status='Present' THEN 1 END)*100.0/NULLIF(COUNT(*),0), 2) AS percentage " +
                "FROM Attendance a " +
                "JOIN Student s ON a.student_id=s.student_id " +
                "JOIN Room r ON s.room_id=r.room_id " +
                "WHERE r.floor_id=? " +
                "AND MONTH(a.date)=MONTH(CURDATE()) AND YEAR(a.date)=YEAR(CURDATE()) " +
                "GROUP BY s.student_id, s.name, s.enrollment_no " +
                "ORDER BY percentage ASC";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, floorId);
            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append("========================================\n");
            sb.append("  FLOOR REPORT: ").append(floorName).append("\n");
            sb.append("  Month: ").append(LocalDate.now().getMonth()).append(" ").append(LocalDate.now().getYear()).append("\n");
            sb.append("========================================\n\n");
            sb.append(String.format("%-28s %-14s %7s %8s\n","Name","Enrollment","Present","%"));
            sb.append("-".repeat(62)).append("\n");

            while (rs.next()) {
                double pct  = rs.getDouble("percentage");
                int present = rs.getInt("present_days");
                int total   = rs.getInt("total_days");
                String flag = pct < 75 ? "  *** DEFAULTER" : "";
                sb.append(String.format("%-28s %-14s %3d/%-3d  %5.1f%%%s\n",
                    rs.getString("name"), rs.getString("enrollment_no"),
                    present, total, pct, flag));
            }

            showReport(sb.toString(), "Floor Report — " + floorName);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error generating floor report: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── DISPLAY REPORT 
    private static void showReport(String content, String title) {
        // Dark-themed report dialog
        JDialog dlg = new JDialog((Frame) null, title, true);
        dlg.setSize(720, 500);
        dlg.setLocationRelativeTo(null);
        dlg.getContentPane().setBackground(WardenDashboard.BG);
        dlg.setLayout(new BorderLayout(8, 8));

        JTextArea area = new JTextArea(content);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setBackground(WardenDashboard.CARD);
        area.setForeground(WardenDashboard.TEXT);
        area.setCaretColor(WardenDashboard.TEXT);
        area.setBorder(BorderFactory.createEmptyBorder(12,14,12,14));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(WardenDashboard.BORDER));
        scroll.getViewport().setBackground(WardenDashboard.CARD);
        dlg.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnPanel.setBackground(WardenDashboard.CARD);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1,0,0,0, WardenDashboard.BORDER));

        JButton exportBtn = WardenDashboard.accentBtn("📥 Export to CSV", WardenDashboard.ACCENT);
        JButton closeBtn  = WardenDashboard.accentBtn("✖ Close",           WardenDashboard.MUTED);
        exportBtn.setPreferredSize(new Dimension(160, 34));
        closeBtn.setPreferredSize(new Dimension(100, 34));

        btnPanel.add(exportBtn);
        btnPanel.add(closeBtn);
        dlg.add(btnPanel, BorderLayout.SOUTH);

        exportBtn.addActionListener(e -> exportToCSV(content, title));
        closeBtn.addActionListener(e  -> dlg.dispose());

        dlg.setVisible(true);
    }

    // CSV EXPORT 
    public static void exportToCSV(String data, String suggestedTitle) {
        JFileChooser chooser = new JFileChooser();
        String fname = suggestedTitle.replaceAll("[^a-zA-Z0-9_]", "_") + ".csv";
        chooser.setSelectedFile(new File(fname));

        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

        try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
            String[] lines = data.split("\n");

            // First pass: write header line from the formatted table header
            boolean headerWritten = false;
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("=") || trimmed.startsWith("-")) continue;

                // Convert to CSV by splitting on 2+ spaces
                String csv = trimmed.replaceAll(" {2,}", ",").replaceAll(",+", ",");

                // Mark header (first data line after separators)
                if (!headerWritten) {
                    writer.write("sep=,\n");
                    headerWritten = true;
                }
                writer.write(csv + "\n");
            }

            JOptionPane.showMessageDialog(null,
                "Report exported successfully to:\n" + chooser.getSelectedFile().getAbsolutePath());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage());
        }
    }

    // Legacy overload (called from old code)
    public static void exportToCSV(String data) {
        exportToCSV(data, "attendance_report");
    }
}

