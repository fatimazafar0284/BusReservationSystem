package com.busreservation.ui;

import com.busreservation.model.Ticket;
import com.busreservation.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TicketReceiptFrame extends JFrame {

    public TicketReceiptFrame(User user, Ticket ticket) {
        setTitle("Booking Confirmed! — " + ticket.getTicketId());
        setSize(480, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);


        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(Theme.SUCCESS);
        banner.setBorder(new EmptyBorder(18, 20, 18, 20));
        JLabel check = new JLabel("✅  Booking Confirmed!", SwingConstants.CENTER);
        check.setForeground(Color.WHITE);
        check.setFont(new Font("Segoe UI", Font.BOLD, 20));
        banner.add(check, BorderLayout.CENTER);


        JPanel card = new JPanel(null);
        card.setBackground(Theme.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(20, 30, 20, 30)));

        int y = 20;
        // Ticket ID
        addRow(card, "🎟  Ticket ID",   ticket.getTicketId(),    y); y += 44;
        addRow(card, "👤  Passenger",   ticket.getPassengerName(), y); y += 44;
        addRow(card, "📧  Email",       ticket.getPassengerEmail(), y); y += 44;
        addRow(card, "🚌  Bus",         ticket.getBusName(),      y); y += 44;
        addRow(card, "🛣️   Route",       ticket.getRoute(),        y); y += 44;
        addRow(card, "🪑  Seat No.",    String.valueOf(ticket.getSeatNumber()), y); y += 44;
        addRow(card, "📅  Travel Date", ticket.getTravelDate(),   y); y += 44;
        addRow(card, "💰  Fare Paid",   "Rs. " + ticket.getFare(), y);


        JSeparator sep = new JSeparator();
        sep.setBounds(0, y + 36, 420, 1);
        sep.setForeground(Theme.BORDER);
        card.add(sep);

        card.setPreferredSize(new Dimension(420, y + 50));


        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        btns.setBackground(Theme.BG);

        RoundedButton homeBtn = new RoundedButton("Go to Dashboard", Theme.PRIMARY);
        homeBtn.addActionListener(e -> { dispose(); new PassengerDashboard(user); });

        RoundedButton newBtn = new RoundedButton("Book Another", new Color(30, 41, 70));
        newBtn.setForeground(Theme.PRIMARY);
        newBtn.addActionListener(e -> { dispose(); new PassengerDashboard(user); });

        btns.add(homeBtn); btns.add(newBtn);

        root.add(banner,                      BorderLayout.NORTH);
        root.add(new JScrollPane(card),       BorderLayout.CENTER);
        root.add(btns,                        BorderLayout.SOUTH);
        add(root);
        setVisible(true);
    }

    private void addRow(JPanel card, String label, String value, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(0, y, 180, 20);
        lbl.setForeground(Theme.SUBTEXT);
        lbl.setFont(Theme.FONT_SMALL);

        JLabel val = new JLabel(value);
        val.setBounds(180, y, 220, 20);
        val.setForeground(Theme.TEXT);
        val.setFont(Theme.FONT_BODY);


        JLabel dot = new JLabel("· · · · · · · · · ·");
        dot.setBounds(0, y + 20, 400, 14);
        dot.setForeground(Theme.BORDER);
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        card.add(lbl); card.add(val); card.add(dot);
    }
}