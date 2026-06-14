package com.busreservation.ui;

import com.busreservation.model.User;
import com.busreservation.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField     nameField, emailField, phoneField;
    private JPasswordField passField, confirmField;

    public RegisterFrame() {
        setTitle("Register — Bus Ticket System");
        setSize(900, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(buildLeftPanel());
        root.add(buildRightPanel());
        add(root);
        setVisible(true);
    }

    private JPanel buildLeftPanel() {
        JPanel p = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(14, 28, 70),
                        getWidth(), getHeight(), new Color(5, 10, 35));
                g2.setPaint(gp); g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        JLabel icon = new JLabel("🎫", SwingConstants.CENTER);
        icon.setBounds(0, 100, 450, 70); icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        JLabel title = new JLabel("Join Us Today!", SwingConstants.CENTER);
        title.setBounds(0, 185, 450, 36); title.setForeground(Theme.PRIMARY);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel sub = new JLabel("Create your passenger account", SwingConstants.CENTER);
        sub.setBounds(0, 228, 450, 22); sub.setForeground(Theme.SUBTEXT); sub.setFont(Theme.FONT_BODY);
        p.add(icon); p.add(title); p.add(sub);
        return p;
    }

    private JPanel buildRightPanel() {
        JPanel p = new JPanel(null); p.setBackground(Theme.BG);

        JPanel card = new JPanel(null);
        card.setBackground(Theme.CARD);
        card.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        card.setBounds(40, 30, 370, 510);

        JLabel title = new JLabel("Create Account");
        title.setBounds(25, 20, 320, 28); title.setForeground(Theme.TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // Name
        JLabel nl = UIUtils.styledLabel("Full Name");       nl.setBounds(25, 60, 320, 16);
        nameField = UIUtils.styledField("Your full name");  nameField.setBounds(25, 78, 320, 34);

        // Email
        JLabel el = UIUtils.styledLabel("Email Address");   el.setBounds(25, 122, 320, 16);
        emailField = UIUtils.styledField("you@email.com");  emailField.setBounds(25, 140, 320, 34);

        // Phone
        JLabel phl = UIUtils.styledLabel("Phone Number");   phl.setBounds(25, 184, 320, 16);
        phoneField = UIUtils.styledField("03XX-XXXXXXX");   phoneField.setBounds(25, 202, 320, 34);

        // Password
        JLabel pl = UIUtils.styledLabel("Password");        pl.setBounds(25, 246, 320, 16);
        passField = UIUtils.styledPassField();               passField.setBounds(25, 264, 320, 34);

        // Confirm
        JLabel cl = UIUtils.styledLabel("Confirm Password"); cl.setBounds(25, 308, 320, 16);
        confirmField = UIUtils.styledPassField();             confirmField.setBounds(25, 326, 320, 34);

        // Register button
        RoundedButton regBtn = new RoundedButton("Create Account", Theme.SUCCESS);
        regBtn.setBounds(25, 378, 320, 40);
        regBtn.addActionListener(e -> doRegister());

        // Back to login
        RoundedButton backBtn = new RoundedButton("← Back to Login", new Color(20, 30, 55));
        backBtn.setForeground(Theme.PRIMARY);
        backBtn.setBounds(25, 428, 320, 34);
        backBtn.addActionListener(e -> { dispose(); new LoginFrame(); });

        card.add(title);
        card.add(nl); card.add(nameField);
        card.add(el); card.add(emailField);
        card.add(phl); card.add(phoneField);
        card.add(pl);  card.add(passField);
        card.add(cl);  card.add(confirmField);
        card.add(regBtn); card.add(backBtn);

        p.add(card);
        return p;
    }

    private void doRegister() {
        String name    = nameField.getText().trim();
        String email   = emailField.getText().trim();
        String phone   = phoneField.getText().trim();
        String pass    = new String(passField.getPassword());
        String confirm = new String(confirmField.getPassword());

        // ── Input Validation ─────────────────────────────────
        try {
            if (UIUtils.isEmpty(name, email, pass, confirm))
                throw new Exception("All fields are required.");
            if (name.length() < 2)
                throw new Exception("Name must be at least 2 characters.");
            if (!UIUtils.isValidEmail(email))
                throw new Exception("Please enter a valid email address.");
            if (phone.length() > 0 && !UIUtils.isValidPhone(phone))
                throw new Exception("Please enter a valid phone number (10-13 digits).");
            if (pass.length() < 4)
                throw new Exception("Password must be at least 4 characters.");
            if (!pass.equals(confirm))
                throw new Exception("Passwords do not match.");

            User u = new User(name, email, pass, phone.isEmpty() ? "N/A" : phone);
            if (UserService.register(u)) {
                UIUtils.showSuccess(this, "✅ Registration successful! Please log in.");
                dispose();
                new LoginFrame();
            } else {
                throw new Exception("An account with this email already exists.");
            }
        } catch (Exception ex) {
            UIUtils.showError(this, ex.getMessage());
        }
    }
}