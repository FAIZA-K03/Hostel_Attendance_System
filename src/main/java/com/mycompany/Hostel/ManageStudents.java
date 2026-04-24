package com.mycompany.Hostel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class ManageStudents extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JTextField nameField, enrollField, contactField;
    private JComboBox<String> categoryCombo, roomCombo;

    private final HashMap<String, Integer> categoryMap = new HashMap<>();
    private final HashMap<String, Integer> roomMap = new HashMap<>();

    private final int floorId;

    public ManageStudents() {
        this(-1);
    }

    public ManageStudents(int floorId) {
        this.floorId = floorId;

        setTitle("Manage Students");
        setSize(980, 620);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(WardenDashboard.BG);
        setLayout(new BorderLayout());

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadDropdowns();
        loadStudents();

        setVisible(true);
    }

    //  FORM 
    private JPanel buildFormPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WardenDashboard.CARD);
        panel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, WardenDashboard.BORDER));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        form.setBackground(WardenDashboard.CARD);

        nameField = styledField(12);
        enrollField = styledField(12);
        contactField = styledField(10);

        categoryCombo = new JComboBox<>();
        roomCombo = new JComboBox<>();

        styleCombo(categoryCombo);
        styleCombo(roomCombo);

        form.add(lbl("Name")); form.add(nameField);
        form.add(lbl("Enroll")); form.add(enrollField);
        form.add(lbl("Contact")); form.add(contactField);
        form.add(lbl("Category")); form.add(categoryCombo);
        form.add(lbl("Room")); form.add(roomCombo);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        btnRow.setBackground(WardenDashboard.CARD);

        JButton addBtn = WardenDashboard.accentBtn("➕ Add", WardenDashboard.SUCCESS);
        JButton editBtn = WardenDashboard.accentBtn("✏️ Edit", WardenDashboard.ACCENT);
        JButton deleteBtn = WardenDashboard.accentBtn("🗑 Delete", WardenDashboard.DANGER);

        btnRow.add(addBtn);
        btnRow.add(editBtn);
        btnRow.add(deleteBtn);

        panel.add(form, BorderLayout.NORTH);
        panel.add(btnRow, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addStudent());
        editBtn.addActionListener(e -> editStudent());
        deleteBtn.addActionListener(e -> deleteStudent());

        return panel;
    }

    //  TABLE 
    private JPanel buildTablePanel() {

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(WardenDashboard.BG);
        p.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        model = new DefaultTableModel(
            new String[]{"ID","Name","Enrollment","Category","Room"},0
        );

        table = new JTable(model);

        table.setRowHeight(28);
        table.setBackground(WardenDashboard.ROW_EVEN);
        table.setForeground(WardenDashboard.TEXT);
        table.setGridColor(WardenDashboard.BORDER);

        // hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        // AUTO FILL
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0){
                nameField.setText(model.getValueAt(row,1).toString());
                enrollField.setText(model.getValueAt(row,2).toString());
                categoryCombo.setSelectedItem(model.getValueAt(row,3));
                roomCombo.setSelectedItem("Room " + model.getValueAt(row,4).toString());
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    //  LOAD 
    private void loadDropdowns() {
        try(Connection con = DBConnection.getConnection()){

            ResultSet cat = con.createStatement().executeQuery("SELECT * FROM Category");
            while(cat.next()){
                categoryMap.put(cat.getString("category_name"), cat.getInt("category_id"));
                categoryCombo.addItem(cat.getString("category_name"));
            }

            String sql = (floorId==-1)
                ? "SELECT * FROM Room"
                : "SELECT * FROM Room WHERE floor_id="+floorId;

            ResultSet room = con.createStatement().executeQuery(sql);
            while(room.next()){
                String r = "Room " + room.getString("room_number");
                roomMap.put(r, room.getInt("room_id"));
                roomCombo.addItem(r);
            }

        }catch(Exception e){ e.printStackTrace(); }
    }

    private void loadStudents(){
        model.setRowCount(0);

        try(Connection con = DBConnection.getConnection()){

            String sql =
                "SELECT s.student_id, s.name, s.enrollment_no, c.category_name, r.room_number " +
                "FROM Student s " +
                "JOIN Category c ON s.category_id=c.category_id " +
                "JOIN Room r ON s.room_id=r.room_id";

            if(floorId!=-1) sql += " WHERE r.floor_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            if(floorId!=-1) ps.setInt(1,floorId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)
                });
            }

        }catch(Exception e){ e.printStackTrace(); }
    }

    //  ADD 
    private void addStudent(){
        try(Connection con = DBConnection.getConnection()){

            String name = nameField.getText().trim();
            String enroll = enrollField.getText().trim();

            if(name.isEmpty() || enroll.isEmpty()){
                JOptionPane.showMessageDialog(this,"Name & Enrollment required!");
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Student(name,enrollment_no,category_id,room_id) VALUES(?,?,?,?)"
            );

            ps.setString(1,name);
            ps.setString(2,enroll);
            ps.setInt(3,categoryMap.get(categoryCombo.getSelectedItem()));
            ps.setInt(4,roomMap.get(roomCombo.getSelectedItem()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Student Added");
            clear();
            loadStudents();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    //  EDIT 
    private void editStudent(){
        int row = table.getSelectedRow();

        if(row < 0){
            JOptionPane.showMessageDialog(this,"Select student");
            return;
        }

        String name = nameField.getText().trim();
        String enroll = enrollField.getText().trim();

        if(name.isEmpty() || enroll.isEmpty()){
            JOptionPane.showMessageDialog(this,"Fields cannot be empty!");
            return;
        }

        try(Connection con = DBConnection.getConnection()){

            PreparedStatement ps = con.prepareStatement(
                "UPDATE Student SET name=?, enrollment_no=?, category_id=?, room_id=? WHERE student_id=?"
            );

            ps.setString(1,name);
            ps.setString(2,enroll);
            ps.setInt(3,categoryMap.get(categoryCombo.getSelectedItem()));
            ps.setInt(4,roomMap.get(roomCombo.getSelectedItem()));
            ps.setInt(5,(int)model.getValueAt(row,0));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Updated");
            loadStudents();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    //  DELETE 
    private void deleteStudent(){
        int row = table.getSelectedRow();

        if(row < 0){
            JOptionPane.showMessageDialog(this,"Select student");
            return;
        }

        int id = (int) model.getValueAt(row,0);

        try(Connection con = DBConnection.getConnection()){

            // remove attendance first
            PreparedStatement ps1 = con.prepareStatement(
                "DELETE FROM Attendance WHERE student_id=?"
            );
            ps1.setInt(1,id);
            ps1.executeUpdate();

            // remove student
            PreparedStatement ps2 = con.prepareStatement(
                "DELETE FROM Student WHERE student_id=?"
            );
            ps2.setInt(1,id);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this,"Deleted");
            loadStudents();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    private void clear(){
        nameField.setText("");
        enrollField.setText("");
        contactField.setText("");
    }

    //  UI 
    private JTextField styledField(int cols){
        JTextField f = new JTextField(cols);
        f.setBackground(new Color(0x0F1B2D));
        f.setForeground(WardenDashboard.TEXT);
        f.setCaretColor(WardenDashboard.TEXT);
        f.setBorder(BorderFactory.createLineBorder(WardenDashboard.BORDER));
        return f;
    }
private void styleCombo(JComboBox<String> cb) {

    Color bg = new Color(0x0F1B2D);
    Color border = WardenDashboard.BORDER;

    
    cb.setBackground(bg);
    cb.setForeground(Color.black);
    cb.setOpaque(true);

    
    if (cb.getEditor() != null && cb.getEditor().getEditorComponent() instanceof JTextField editor) {
        editor.setBackground(bg);
        editor.setForeground(Color.WHITE);
        editor.setCaretColor(Color.WHITE);
        editor.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
    }

   
    cb.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
        @Override
        protected JButton createArrowButton() {
            JButton button = super.createArrowButton();
            button.setBackground(bg);
            button.setBorder(BorderFactory.createEmptyBorder());
            return button;
        }
    });

   
    cb.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            label.setOpaque(true);

            if (isSelected) {
                label.setBackground(new Color(0x2D4A6B));
            } else {
                label.setBackground(bg);
            }

            label.setForeground(Color.WHITE);

            return label;
        }
    });

    // Border
    cb.setBorder(BorderFactory.createLineBorder(border));
}

    private JLabel lbl(String t){
        JLabel l = new JLabel(t);
        l.setForeground(WardenDashboard.MUTED);
        return l;
    }
}
