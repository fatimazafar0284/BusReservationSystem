package com.busreservation.ui;

import com.busreservation.model.Bus;
import com.busreservation.model.Ticket;
import com.busreservation.model.User;
import com.busreservation.service.BusService;
import com.busreservation.service.TicketService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PassengerDashboard extends JFrame {

    private final User user;
    private JPanel     contentPanel;
    private JLabel     pageTitleLabel;


    private JPanel searchPanel, myTicketsPanel, profilePanel;

    public PassengerDashboard(User user) {
        this.user = user;
        setTitle("Passenger Dashboard — " + user.getName());
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMain(),   BorderLayout.CENTER);
        add(root);

        showSearch(); // default page
        setVisible(true);
    }


    private JPanel buildSidebar() {
        JPanel p = new JPanel(null);
        p.setBackground(Theme.SIDEBAR);
        p.setPreferredSize(new Dimension(200, 650));

        // Logo
        JLabel logo = new JLabel("🚌  BusTicket", SwingConstants.CENTER);
        logo.setBounds(0, 20, 200, 30);
        logo.setForeground(Theme.PRIMARY);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        p.add(logo);

        // User info
        JLabel uname = new JLabel(user.getName(), SwingConstants.CENTER);
        uname.setBounds(0, 60, 200, 20);
        uname.setForeground(Theme.TEXT);
        uname.setFont(Theme.FONT_LABEL);
        JLabel urole = new JLabel("Passenger", SwingConstants.CENTER);
        urole.setBounds(0, 80, 200, 16);
        urole.setForeground(Theme.SUBTEXT);
        urole.setFont(Theme.FONT_SMALL);
        p.add(uname); p.add(urole);

        JSeparator sep = new JSeparator();
        sep.setBounds(15, 108, 170, 1);
        sep.setForeground(Theme.BORDER);
        p.add(sep);


        String[][] navItems = {
                {"🔍  Search Buses",   "search"},
                {"🎫  My Tickets",     "tickets"},
                {"👤  My Profile",     "profile"},
        };

        int y = 120;
        for (String[] item : navItems) {
            RoundedButton btn = new RoundedButton(item[0], Theme.SIDEBAR);
            btn.setBounds(10, y, 180, 38);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setForeground(Theme.SUBTEXT);
            final String action = item[1];
            btn.addActionListener(e -> {
                switch (action) {
                    case "search":  showSearch();    break;
                    case "tickets": showMyTickets(); break;
                    case "profile": showProfile();   break;
                }
            });
            p.add(btn);
            y += 46;
        }


        RoundedButton logout = new RoundedButton("⬅  Logout", Theme.DANGER);
        logout.setBounds(10, 580, 180, 36);
        logout.addActionListener(e -> {
            if (UIUtils.confirm(this, "Logout from your account?")) {
                dispose(); new LoginFrame();
            }
        });
        p.add(logout);
        return p;
    }


    private JPanel buildMain() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG);


        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Theme.CARD);
        topBar.setBorder(new EmptyBorder(12, 20, 12, 20));
        pageTitleLabel = new JLabel("Search Buses");
        pageTitleLabel.setForeground(Theme.TEXT);
        pageTitleLabel.setFont(Theme.FONT_TITLE);
        JLabel dateLabel = new JLabel("Welcome, " + user.getName());
        dateLabel.setForeground(Theme.SUBTEXT);
        dateLabel.setFont(Theme.FONT_SMALL);
        topBar.add(pageTitleLabel, BorderLayout.WEST);
        topBar.add(dateLabel,      BorderLayout.EAST);

        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Theme.BG);


        searchPanel    = buildSearchPanel();
        myTicketsPanel = buildMyTicketsPanel();
        profilePanel   = buildProfilePanel();

        contentPanel.add(searchPanel,    "search");
        contentPanel.add(myTicketsPanel, "tickets");
        contentPanel.add(profilePanel,   "profile");

        p.add(topBar,        BorderLayout.NORTH);
        p.add(contentPanel,  BorderLayout.CENTER);
        return p;
    }

    private void show(String card, String title) {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, card);
        pageTitleLabel.setText(title);
    }
    private void showSearch()    { show("search",  "🔍  Search Buses"); }
    private void showMyTickets() {
        myTicketsPanel.removeAll();
        myTicketsPanel.add(buildMyTicketContent());
        myTicketsPanel.revalidate();
        show("tickets", "🎫  My Tickets");
    }
    private void showProfile()   { show("profile", "👤  My Profile"); }


    private JPanel buildSearchPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(Theme.BG);
        p.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Search bar
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bar.setBackground(Theme.CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(8, 8, 8, 8)));

        JTextField from = UIUtils.styledField("From (e.g. Lahore)");
        from.setPreferredSize(new Dimension(170, 32));
        JTextField to   = UIUtils.styledField("To (e.g. Islamabad)");
        to.setPreferredSize(new Dimension(170, 32));
        JTextField date = UIUtils.styledField("Date (YYYY-MM-DD)");
        date.setPreferredSize(new Dimension(150, 32));
        RoundedButton searchBtn = new RoundedButton("Search 🔍");
        searchBtn.setPreferredSize(new Dimension(100, 32));

        bar.add(new JLabel("From:"  ) {{ setForeground(Theme.SUBTEXT); setFont(Theme.FONT_SMALL); }});
        bar.add(from);
        bar.add(new JLabel("To:"    ) {{ setForeground(Theme.SUBTEXT); setFont(Theme.FONT_SMALL); }});
        bar.add(to);
        bar.add(new JLabel("Date:"  ) {{ setForeground(Theme.SUBTEXT); setFont(Theme.FONT_SMALL); }});
        bar.add(date);
        bar.add(searchBtn);


        String[] cols = {"Bus ID","Bus Name","Route","Date","Departure","Arrival","Type","Fare","Seats","Action"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 9; }
        };
        JTable table = new JTable(model);
        UIUtils.styleTable(table);


        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), user, this));

        searchBtn.addActionListener(e -> {
            model.setRowCount(0);
            List<Bus> results;
            String f = from.getText().trim(), t2 = to.getText().trim(), d = date.getText().trim();
            if (f.isEmpty() && t2.isEmpty() && d.isEmpty()) {
                results = BusService.getAllBuses();
            } else if (!f.isEmpty() && !t2.isEmpty() && !d.isEmpty()) {
                results = BusService.searchByRouteAndDate(f, t2, d);
            } else if (!f.isEmpty() && !t2.isEmpty()) {
                results = BusService.searchByRoute(f, t2);
            } else {
                results = BusService.getAllBuses();
            }
            for (Bus b : results) {
                int booked = TicketService.searchByBusId(b.getBusId()).stream()
                        .filter(tk -> tk.getStatus().equals("BOOKED")).mapToInt(x -> 1).sum();
                model.addRow(new Object[]{
                        b.getBusId(), b.getBusName(), b.getRoute(), b.getTravelDate(),
                        b.getDepartureTime(), b.getArrivalTime(), b.getBusType(),
                        "Rs. " + b.getFare(), (b.getTotalSeats() - booked) + " / " + b.getTotalSeats(),
                        "Book"
                });
            }
            if (model.getRowCount() == 0)
                UIUtils.showError(this, "No buses found for the given search criteria.");
        });


        SwingUtilities.invokeLater(() -> {
            for (Bus b : BusService.getAllBuses()) {
                int booked = TicketService.searchByBusId(b.getBusId()).stream()
                        .filter(tk -> tk.getStatus().equals("BOOKED")).mapToInt(x -> 1).sum();
                model.addRow(new Object[]{
                        b.getBusId(), b.getBusName(), b.getRoute(), b.getTravelDate(),
                        b.getDepartureTime(), b.getArrivalTime(), b.getBusType(),
                        "Rs. " + b.getFare(), (b.getTotalSeats() - booked) + " / " + b.getTotalSeats(),
                        "Book"
                });
            }
        });

        p.add(bar,                              BorderLayout.NORTH);
        p.add(UIUtils.styledScroll(table),      BorderLayout.CENTER);
        return p;
    }


    private JPanel buildMyTicketsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG);
        p.add(buildMyTicketContent());
        return p;
    }

    private JPanel buildMyTicketContent() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(Theme.BG);
        p.setBorder(new EmptyBorder(16, 20, 16, 20));


        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        bar.setBackground(Theme.CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(8, 8, 8, 8)));
        JTextField tidField = UIUtils.styledField("Ticket ID (e.g. TKT-1234)");
        tidField.setPreferredSize(new Dimension(220, 32));
        RoundedButton cancelBtn = new RoundedButton("Cancel Ticket", Theme.DANGER);
        cancelBtn.setPreferredSize(new Dimension(140, 32));
        bar.add(new JLabel("Search: ") {{ setForeground(Theme.SUBTEXT); setFont(Theme.FONT_SMALL); }});
        bar.add(tidField); bar.add(cancelBtn);


        String[] cols = {"Ticket ID","Bus","Route","Seat","Date","Fare","Booked On","Status"};
        DefaultTableModel mdl = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(mdl);
        UIUtils.styleTable(table);


        for (Ticket t : TicketService.searchByEmail(user.getEmail())) {
            mdl.addRow(new Object[]{
                    t.getTicketId(), t.getBusName(), t.getRoute(),
                    t.getSeatNumber(), t.getTravelDate(),
                    "Rs. " + t.getFare(), t.getBookingDate(), t.getStatus()
            });
        }

        cancelBtn.addActionListener(e -> {
            String tid = tidField.getText().trim();
            if (tid.isEmpty()) {
                // Cancel selected row
                int row = table.getSelectedRow();
                if (row < 0) { UIUtils.showError(this, "Select a ticket or enter a Ticket ID."); return; }
                tid = (String) mdl.getValueAt(row, 0);
            }
            if (!UIUtils.confirm(this, "Cancel ticket " + tid + "? This cannot be undone.")) return;
            if (TicketService.cancelTicket(tid)) {
                UIUtils.showSuccess(this, "Ticket " + tid + " cancelled successfully.");
                showMyTickets();
            } else {
                UIUtils.showError(this, "Ticket not found or already cancelled.");
            }
        });

        p.add(bar, BorderLayout.NORTH);
        p.add(UIUtils.styledScroll(table), BorderLayout.CENTER);
        return p;
    }


    private JPanel buildProfilePanel() {
        JPanel p = new JPanel(null);
        p.setBackground(Theme.BG);

        JPanel card = new JPanel(null);
        card.setBackground(Theme.CARD);
        card.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        card.setBounds(60, 30, 400, 360);

        JLabel title = new JLabel("Edit Profile");
        title.setBounds(20, 16, 360, 26);
        title.setForeground(Theme.TEXT); title.setFont(Theme.FONT_H2);

        JLabel nl = UIUtils.styledLabel("Full Name");     nl.setBounds(20, 56, 360, 16);
        JTextField nf = UIUtils.styledField("");          nf.setBounds(20, 74, 360, 34);
        nf.setText(user.getName());

        JLabel phl = UIUtils.styledLabel("Phone");        phl.setBounds(20, 118, 360, 16);
        JTextField phf = UIUtils.styledField("");         phf.setBounds(20, 136, 360, 34);
        phf.setText(user.getPhone());

        JLabel pwl = UIUtils.styledLabel("New Password (leave blank to keep)");
        pwl.setBounds(20, 180, 360, 16);
        JPasswordField pwf = UIUtils.styledPassField();   pwf.setBounds(20, 198, 360, 34);

        RoundedButton saveBtn = new RoundedButton("Save Changes", Theme.SUCCESS);
        saveBtn.setBounds(20, 250, 360, 40);
        saveBtn.addActionListener(e -> {
            String name  = nf.getText().trim();
            String phone = phf.getText().trim();
            String pass  = new String(pwf.getPassword());
            try {
                if (name.isEmpty()) throw new Exception("Name cannot be empty.");
                if (!phone.isEmpty() && !UIUtils.isValidPhone(phone))
                    throw new Exception("Invalid phone number.");
                com.busreservation.service.UserService.updateUser(user.getEmail(), name, phone, pass);
                UIUtils.showSuccess(this, "Profile updated successfully.");
            } catch (Exception ex) {
                UIUtils.showError(this, ex.getMessage());
            }
        });

        // Email (read only)
        JLabel el = UIUtils.styledLabel("Email (cannot change)"); el.setBounds(20, 296, 360, 16);
        JLabel ef = new JLabel(user.getEmail()); ef.setBounds(20, 312, 360, 20);
        ef.setForeground(Theme.SUBTEXT); ef.setFont(Theme.FONT_BODY);

        card.add(title); card.add(nl); card.add(nf);
        card.add(phl); card.add(phf);
        card.add(pwl); card.add(pwf);
        card.add(saveBtn); card.add(el); card.add(ef);
        p.add(card);
        return p;
    }

    // ── Button renderer / editor for table "Book" column ──────
    static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true); setBackground(Theme.PRIMARY);
            setForeground(Color.WHITE); setFont(Theme.FONT_BTN);
        }
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                       boolean foc, int row, int col) {
            setText("Book"); return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        private final User user;
        private final PassengerDashboard parent;

        public ButtonEditor(JCheckBox cb, User user, PassengerDashboard parent) {
            super(cb); this.user = user; this.parent = parent;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            String busId = (String) table.getValueAt(row, 0);
            Bus bus = BusService.searchById(busId);
            if (bus != null) {
                fireEditingStopped();
                parent.dispose();
                new SeatSelectionFrame(user, bus);
            }
            JButton btn = new JButton("Book");
            btn.setBackground(Theme.PRIMARY);
            btn.setForeground(Color.WHITE);
            return btn;
        }
    }
}