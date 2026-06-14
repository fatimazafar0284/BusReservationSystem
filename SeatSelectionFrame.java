package com.busreservation.ui;

import com.busreservation.model.Bus;
import com.busreservation.model.Ticket;
import com.busreservation.model.User;
import com.busreservation.service.BusService;
import com.busreservation.service.TicketService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SeatSelectionFrame extends JFrame {

    private final User   user;
    private final Bus    bus;
    private Integer      selectedSeat = null;
    private JLabel       selectedLabel;
    private RoundedButton confirmBtn;

    public SeatSelectionFrame(User user, Bus bus) {
        this.user = user;
        this.bus  = bus;

        setTitle("Select Seat — " + bus.getBusName());
        setSize(680, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Theme.BG);
        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
        add(root);
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.CARD);
        p.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("🪑  Seat Selection", SwingConstants.LEFT);
        title.setForeground(Theme.TEXT); title.setFont(Theme.FONT_TITLE);

        JPanel info = new JPanel(new GridLayout(1, 4, 10, 0));
        info.setBackground(Theme.CARD);

        String[][] details = {
                {"🚌", bus.getBusName()},
                {"🛣️",  bus.getRoute()},
                {"📅", bus.getTravelDate()},
                {"💰", "Rs. " + bus.getFare()}
        };
        for (String[] d : details) {
            JLabel l = new JLabel("<html><center>" + d[0] + "<br><b>" + d[1] + "</b></center></html>",
                    SwingConstants.CENTER);
            l.setForeground(Theme.SUBTEXT); l.setFont(Theme.FONT_SMALL);
            info.add(l);
        }

        p.add(title, BorderLayout.WEST);
        p.add(info,  BorderLayout.EAST);
        return p;
    }

    private JPanel buildCenter() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG);
        p.setBorder(new EmptyBorder(12, 20, 12, 20));


        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 4));
        legend.setBackground(Theme.BG);
        legend.add(legendItem(Theme.FREE_SEAT,   "Available"));
        legend.add(legendItem(Theme.BOOKED_SEAT, "Booked"));
        legend.add(legendItem(Theme.SELECTED,    "Selected"));
        p.add(legend, BorderLayout.NORTH);


        int total = bus.getTotalSeats();
        int cols  = 4;
        int rows  = (int) Math.ceil(total / (double) cols);

        JPanel grid = new JPanel(new GridLayout(rows, cols + 1, 10, 10));
        grid.setBackground(Theme.BG);
        grid.setBorder(new EmptyBorder(14, 30, 14, 30));

        for (int i = 1; i <= total; i++) {
            final int seatNum = i;
            boolean booked = TicketService.isSeatBooked(bus.getBusId(), seatNum);

            JButton btn = new JButton(String.valueOf(i));
            btn.setFont(Theme.FONT_BTN);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(booked ? Cursor.DEFAULT_CURSOR : Cursor.HAND_CURSOR));
            btn.setBorderPainted(false);
            btn.setForeground(Color.WHITE);

            if (booked) {
                btn.setBackground(Theme.BOOKED_SEAT);
                btn.setEnabled(false);
            } else {
                btn.setBackground(Theme.FREE_SEAT);
                btn.addActionListener(e -> selectSeat(seatNum, btn));
            }

            grid.add(btn);


            if (i % cols == 2) grid.add(new JLabel());
        }

        p.add(new JScrollPane(grid), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setBackground(Theme.CARD);
        p.setBorder(new EmptyBorder(14, 20, 14, 20));

        selectedLabel = new JLabel("No seat selected", SwingConstants.LEFT);
        selectedLabel.setForeground(Theme.SUBTEXT);
        selectedLabel.setFont(Theme.FONT_BODY);

        confirmBtn = new RoundedButton("Confirm Booking", Theme.SUCCESS);
        confirmBtn.setEnabled(false);
        confirmBtn.setPreferredSize(new Dimension(180, 38));
        confirmBtn.addActionListener(e -> confirmBooking());

        RoundedButton backBtn = new RoundedButton("← Back", new Color(30, 41, 70));
        backBtn.setForeground(Theme.PRIMARY);
        backBtn.setPreferredSize(new Dimension(110, 38));
        backBtn.addActionListener(e -> { dispose(); new PassengerDashboard(user); });

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btns.setBackground(Theme.CARD);
        btns.add(backBtn); btns.add(confirmBtn);

        p.add(selectedLabel, BorderLayout.WEST);
        p.add(btns,          BorderLayout.EAST);
        return p;
    }

    private JPanel legendItem(Color color, String label) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setBackground(Theme.BG);
        JLabel box = new JLabel("  ");
        box.setOpaque(true); box.setBackground(color);
        box.setPreferredSize(new Dimension(20, 14));
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Theme.SUBTEXT); lbl.setFont(Theme.FONT_SMALL);
        p.add(box); p.add(lbl);
        return p;
    }

    private void selectSeat(int num, JButton btn) {

        Component[] comps = ((JPanel) btn.getParent()).getComponents();
        for (Component c : comps) {
            if (c instanceof JButton && c.isEnabled()) {
                boolean stillBooked = TicketService.isSeatBooked(bus.getBusId(),
                        Integer.parseInt(((JButton)c).getText()));
                if (!stillBooked) ((JButton) c).setBackground(Theme.FREE_SEAT);
            }
        }
        btn.setBackground(Theme.SELECTED);
        selectedSeat = num;
        selectedLabel.setText("Selected: Seat " + num + " | Fare: Rs. " + bus.getFare());
        selectedLabel.setForeground(Theme.PRIMARY);
        confirmBtn.setEnabled(true);
    }

    private void confirmBooking() {
        if (selectedSeat == null) return;
        try {
            if (TicketService.isSeatBooked(bus.getBusId(), selectedSeat)) {
                UIUtils.showError(this, "This seat was just booked. Please select another.");
                return;
            }
            Ticket t = new Ticket(
                    user.getName(), user.getEmail(),
                    bus.getBusId(), bus.getBusName(), bus.getRoute(),
                    selectedSeat, bus.getFare(), bus.getTravelDate()
            );
            TicketService.addTicket(t);
            dispose();
            new TicketReceiptFrame(user, t);
        } catch (Exception ex) {
            UIUtils.showError(this, "Booking failed: " + ex.getMessage());
        }
    }
}