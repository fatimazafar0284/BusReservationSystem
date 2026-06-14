package com.busreservation.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

public class UIUtils {


    public static void showError(Component c, String msg) {
        JOptionPane.showMessageDialog(c, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccess(Component c, String msg) {
        JOptionPane.showMessageDialog(c, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(Component c, String msg) {
        return JOptionPane.showConfirmDialog(c, msg, "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }


    public static boolean isEmpty(String... fields) {
        for (String f : fields) if (f == null || f.trim().isEmpty()) return true;
        return false;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[0-9\\-+]{10,13}$");
    }

    public static boolean isNumeric(String s) {
        try { Double.parseDouble(s); return true; }
        catch (NumberFormatException e) { return false; }
    }



    public static JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.setBackground(Theme.INPUT_BG);
        f.setForeground(Theme.TEXT);
        f.setCaretColor(Theme.PRIMARY);
        f.setFont(Theme.FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                new EmptyBorder(6, 10, 6, 10)));
        f.setToolTipText(placeholder);
        return f;
    }

    public static JPasswordField styledPassField() {
        JPasswordField f = new JPasswordField();
        f.setBackground(Theme.INPUT_BG);
        f.setForeground(Theme.TEXT);
        f.setCaretColor(Theme.PRIMARY);
        f.setFont(Theme.FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    public static JLabel styledLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Theme.SUBTEXT);
        l.setFont(Theme.FONT_LABEL);
        return l;
    }

    public static JLabel titleLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Theme.TEXT);
        l.setFont(Theme.FONT_TITLE);
        return l;
    }

    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setBackground(Theme.INPUT_BG);
        c.setForeground(Theme.TEXT);
        c.setFont(Theme.FONT_BODY);
        c.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        return c;
    }


    public static void styleTable(JTable table) {
        table.setBackground(Theme.CARD);
        table.setForeground(Theme.TEXT);
        table.setFont(Theme.FONT_BODY);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(Theme.PRIMARY_D);
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.BORDER);
        header.setForeground(Theme.PRIMARY);
        header.setFont(Theme.FONT_LABEL);
        header.setReorderingAllowed(false);


        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    public static JScrollPane styledScroll(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBackground(Theme.CARD);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        sp.getViewport().setBackground(Theme.CARD);
        return sp;
    }


    public static JPanel statCard(String value, String label, Color accent) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(Theme.CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent, 1),
                new EmptyBorder(16, 20, 16, 20)));

        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setForeground(accent);
        val.setFont(Theme.FONT_BIG);

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setForeground(Theme.SUBTEXT);
        lbl.setFont(Theme.FONT_SMALL);

        p.add(val, BorderLayout.CENTER);
        p.add(lbl, BorderLayout.SOUTH);
        return p;
    }


    public static JPanel sectionPanel(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(16, 16, 16, 16)));

        JLabel lbl = new JLabel(title);
        lbl.setForeground(Theme.PRIMARY);
        lbl.setFont(Theme.FONT_H2);
        lbl.setBorder(new EmptyBorder(0, 0, 12, 0));
        p.add(lbl, BorderLayout.NORTH);
        return p;
    }
}