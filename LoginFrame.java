package com.busreservation.ui;

import com.busreservation.model.User;
import com.busreservation.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {

    private JTextField     emailField;
    private JPasswordField passField;

    public LoginFrame() {
        setTitle("🚌 Bus Ticket Reservation System");
        setSize(900, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(false);
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
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(14, 28, 70),
                        getWidth(), getHeight(), new Color(5, 10, 35));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };


        JLabel busIcon = new JLabel("🚌", SwingConstants.CENTER);
        busIcon.setBounds(0, 80, 450, 80);
        busIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));

        JLabel title = new JLabel("Bus Ticket System", SwingConstants.CENTER);
        title.setBounds(0, 175, 450, 40);
        title.setForeground(Theme.PRIMARY);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel sub = new JLabel("Book your journey with ease", SwingConstants.CENTER);
        sub.setBounds(0, 220, 450, 25);
        sub.setForeground(Theme.SUBTEXT);
        sub.setFont(Theme.FONT_BODY);


        String[] features = {"✔  Search & Book Tickets", "✔  Seat Selection", "✔  Manage Reservations", "✔  Admin Dashboard"};
        int y = 280;
        for (String feat : features) {
            JLabel fl = new JLabel(feat, SwingConstants.CENTER);
            fl.setBounds(0, y, 450, 24);
            fl.setForeground(Theme.SUBTEXT);
            fl.setFont(Theme.FONT_BODY);
            p.add(fl);
            y += 30;
        }

        p.add(busIcon);
        p.add(title);
        p.add(sub);
        return p;
    }


    private JPanel buildRightPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(Theme.BG);


        JPanel card = new JPanel(null);
        card.setBackground(Theme.CARD);
        card.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        card.setBounds(50, 60, 350, 450);


        JLabel titleLbl = new JLabel("Welcome Back");
        titleLbl.setBounds(30, 30, 290, 32);
        titleLbl.setForeground(Theme.TEXT);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel subLbl = new JLabel("Sign in to your account");
        subLbl.setBounds(30, 62, 290, 20);
        subLbl.setForeground(Theme.SUBTEXT);
        subLbl.setFont(Theme.FONT_SMALL);


        JLabel emailLbl = UIUtils.styledLabel("Email Address");
        emailLbl.setBounds(30, 105, 290, 18);
        emailField = UIUtils.styledField("Enter email");
        emailField.setBounds(30, 125, 290, 36);


        JLabel passLbl = UIUtils.styledLabel("Password");
        passLbl.setBounds(30, 175, 290, 18);
        passField = UIUtils.styledPassField();
        passField.setBounds(30, 195, 290, 36);


        RoundedButton loginBtn = new RoundedButton("Sign In", Theme.PRIMARY);
        loginBtn.setBounds(30, 255, 290, 40);
        loginBtn.addActionListener(e -> doLogin());


        passField.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        });

        JLabel regLbl = new JLabel("Don't have an account?", SwingConstants.CENTER);
        regLbl.setBounds(30, 310, 160, 20);
        regLbl.setForeground(Theme.SUBTEXT);
        regLbl.setFont(Theme.FONT_SMALL);

        RoundedButton regBtn = new RoundedButton("Register", new Color(30, 41, 59));
        regBtn.setBounds(30, 335, 290, 36);
        regBtn.setForeground(Theme.PRIMARY);
        regBtn.addActionListener(e -> { dispose(); new RegisterFrame(); });


        JLabel hint = new JLabel("Admin: admin@bus.com / admin123", SwingConstants.CENTER);
        hint.setBounds(20, 390, 310, 16);
        hint.setForeground(new Color(80, 100, 130));
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 10));

        card.add(titleLbl); card.add(subLbl);
        card.add(emailLbl); card.add(emailField);
        card.add(passLbl);  card.add(passField);
        card.add(loginBtn);
        card.add(regLbl);   card.add(regBtn);
        card.add(hint);

        p.add(card);
        return p;
    }

    private void doLogin() {
        String email = emailField.getText().trim();
        String pass  = new String(passField.getPassword());


        if (UIUtils.isEmpty(email, pass)) {
            UIUtils.showError(this, "Please enter both email and password.");
            return;
        }
        if (!UIUtils.isValidEmail(email)) {
            UIUtils.showError(this, "Please enter a valid email address.");
            return;
        }

        try {
            User user = UserService.login(email, pass);
            if (user != null) {
                dispose();
                if (user.getRole().equals("Admin")) {
                    new AdminDashboard(user);
                } else {
                    new PassengerDashboard(user);
                }
            } else {
                UIUtils.showError(this, "Invalid email or password. Please try again.");
                passField.setText("");
            }
        } catch (Exception ex) {
            UIUtils.showError(this, "Login error: " + ex.getMessage());
        }
    }
}