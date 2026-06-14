package com.busreservation.ui;

import com.busreservation.model.Admin;
import com.busreservation.model.Bus;
import com.busreservation.model.Ticket;
import com.busreservation.model.User;
import com.busreservation.service.BusService;
import com.busreservation.service.TicketService;
import com.busreservation.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final User     admin;
    private JPanel         contentPanel;
    private JLabel         pageTitleLabel;

    public AdminDashboard(User admin) {
        this.admin = admin;
        setTitle("Admin Dashboard — Bus Ticket System");
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMain(),   BorderLayout.CENTER);
        add(root);
        showDashboard();
        setVisible(true);
    }

    // ── Sidebar ───────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel p = new JPanel(null);
        p.setBackground(Theme.SIDEBAR);
        p.setPreferredSize(new Dimension(210, 700));

        JLabel logo = new JLabel("🚌  BusTicket Admin", SwingConstants.CENTER);
        logo.setBounds(0, 18, 210, 28);
        logo.setForeground(Theme.PRIMARY);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        p.add(logo);

        JLabel uname = new JLabel(admin.getName(), SwingConstants.CENTER);
        uname.setBounds(0, 55, 210, 18); uname.setForeground(Theme.TEXT); uname.setFont(Theme.FONT_LABEL);
        JLabel urole = new JLabel("Administrator", SwingConstants.CENTER);
        urole.setBounds(0, 73, 210, 15); urole.setForeground(Theme.WARNING); urole.setFont(Theme.FONT_SMALL);
        p.add(uname); p.add(urole);

        JSeparator sep = new JSeparator(); sep.setBounds(15, 100, 180, 1); sep.setForeground(Theme.BORDER);
        p.add(sep);

        String[][] items = {
                {"📊  Dashboard",       "dash"},
                {"🚌  Manage Buses",    "buses"},
                {"🎫  All Tickets",     "tickets"},
                {"👥  Passengers",      "users"},
                {"📈  Reports",         "reports"},
        };

        int y = 112;
        for (String[] item : items) {
            RoundedButton btn = new RoundedButton(item[0], Theme.SIDEBAR);
            btn.setBounds(8, y, 194, 38);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setForeground(Theme.SUBTEXT);
            final String action = item[1];
            btn.addActionListener(e -> navigate(action));
            p.add(btn);
            y += 46;
        }

        RoundedButton logout = new RoundedButton("⬅  Logout", Theme.DANGER);
        logout.setBounds(8, 640, 194, 36);
        logout.addActionListener(e -> { if (UIUtils.confirm(this, "Logout?")) { dispose(); new LoginFrame(); } });
        p.add(logout);
        return p;
    }

    private void navigate(String action) {
        switch (action) {
            case "dash":    showDashboard(); break;
            case "buses":   showBuses();     break;
            case "tickets": showTickets();   break;
            case "users":   showUsers();     break;
            case "reports": showReports();   break;
        }
    }

    // ── Main Content ──────────────────────────────────────────
    private JPanel buildMain() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Theme.CARD);
        topBar.setBorder(new EmptyBorder(12, 20, 12, 20));
        pageTitleLabel = new JLabel("Dashboard");
        pageTitleLabel.setForeground(Theme.TEXT); pageTitleLabel.setFont(Theme.FONT_TITLE);
        JLabel adminBadge = new JLabel("ADMIN PANEL", SwingConstants.RIGHT);
        adminBadge.setForeground(Theme.WARNING); adminBadge.setFont(Theme.FONT_SMALL);
        topBar.add(pageTitleLabel, BorderLayout.WEST);
        topBar.add(adminBadge,     BorderLayout.EAST);

        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Theme.BG);

        p.add(topBar,       BorderLayout.NORTH);
        p.add(contentPanel, BorderLayout.CENTER);
        return p;
    }

    private void show(String card, String title) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        // Remove old, add fresh
        contentPanel.removeAll();
        JPanel panel;
        switch (card) {
            case "dash":    panel = buildDashPanel();    break;
            case "buses":   panel = buildBusPanel();     break;
            case "tickets": panel = buildTicketPanel();  break;
            case "users":   panel = buildUserPanel();    break;
            case "reports": panel = buildReportPanel();  break;
            default: panel = new JPanel();
        }
        contentPanel.add(panel, card);
        cl.show(contentPanel, card);
        pageTitleLabel.setText(title);
        contentPanel.revalidate(); contentPanel.repaint();
    }

    private void showDashboard() { show("dash",    "📊  Dashboard"); }
    private void showBuses()     { show("buses",   "🚌  Manage Buses"); }
    private void showTickets()   { show("tickets", "🎫  All Tickets"); }
    private void showUsers()     { show("users",   "👥  Passengers"); }
    private void showReports()   { show("reports", "📈  Reports"); }

    // ── Dashboard Stats ───────────────────────────────────────
    private JPanel buildDashPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(Theme.BG); p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel stats = new JPanel(new GridLayout(1, 4, 16, 0));
        stats.setBackground(Theme.BG);
        stats.add(UIUtils.statCard(String.valueOf(BusService.getAllBuses().size()),      "Total Buses",      Theme.PRIMARY));
        stats.add(UIUtils.statCard(String.valueOf(UserService.getPassengers().size()),   "Passengers",       Theme.SUCCESS));
        stats.add(UIUtils.statCard(String.valueOf(TicketService.getTotalBooked()),       "Tickets Booked",   Theme.WARNING));
        stats.add(UIUtils.statCard("Rs. " + (int)TicketService.getTotalRevenue(),        "Total Revenue",    Theme.PURPLE));

        // Recent tickets table
        JPanel recentPanel = UIUtils.sectionPanel("Recent Bookings");
        String[] cols = {"Ticket ID","Passenger","Bus","Route","Seat","Fare","Status"};
        DefaultTableModel mdl = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable t = new JTable(mdl); UIUtils.styleTable(t);
        List<Ticket> all = TicketService.getAllTickets();
        int start = Math.max(0, all.size() - 10);
        for (int i = all.size() - 1; i >= start; i--) {
            Ticket tk = all.get(i);
            mdl.addRow(new Object[]{
                    tk.getTicketId(), tk.getPassengerName(), tk.getBusName(),
                    tk.getRoute(), tk.getSeatNumber(), "Rs. "+tk.getFare(), tk.getStatus()
            });
        }
        recentPanel.add(UIUtils.styledScroll(t), BorderLayout.CENTER);

        p.add(stats,       BorderLayout.NORTH);
        p.add(recentPanel, BorderLayout.CENTER);
        return p;
    }

    // ── Bus CRUD Panel ────────────────────────────────────────
    private JPanel buildBusPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(Theme.BG); p.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Form
        JPanel form = new JPanel(new GridLayout(3, 6, 8, 8));
        form.setBackground(Theme.CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(12, 12, 12, 12)));

        JTextField idF   = UIUtils.styledField("Bus ID (e.g. BUS-009)");
        JTextField nameF = UIUtils.styledField("Bus Name");
        JTextField routeF= UIUtils.styledField("Route (City A → City B)");
        JTextField seatsF= UIUtils.styledField("Total Seats");
        JTextField depF  = UIUtils.styledField("Departure (08:00 AM)");
        JTextField arrF  = UIUtils.styledField("Arrival (02:00 PM)");
        JTextField dateF = UIUtils.styledField("Date (YYYY-MM-DD)");
        JTextField fareF = UIUtils.styledField("Fare (e.g. 1500)");
        String[] types   = {"AC","Non-AC","Sleeper"};
        JComboBox<String> typeC = UIUtils.styledCombo(types);

        form.add(label("Bus ID"));    form.add(idF);
        form.add(label("Bus Name"));  form.add(nameF);
        form.add(label("Route"));     form.add(routeF);
        form.add(label("Seats"));     form.add(seatsF);
        form.add(label("Departure")); form.add(depF);
        form.add(label("Arrival"));   form.add(arrF);
        form.add(label("Date"));      form.add(dateF);
        form.add(label("Fare (Rs)")); form.add(fareF);
        form.add(label("Bus Type"));  form.add(typeC);

        // Buttons
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnBar.setBackground(Theme.CARD);

        RoundedButton addBtn  = new RoundedButton("➕ Add Bus",    Theme.SUCCESS);
        RoundedButton updBtn  = new RoundedButton("✏  Update",     Theme.WARNING);
        RoundedButton delBtn  = new RoundedButton("🗑  Delete",    Theme.DANGER);
        RoundedButton clrBtn  = new RoundedButton("Clear",          new Color(30,41,70));
        clrBtn.setForeground(Theme.SUBTEXT);
        btnBar.add(addBtn); btnBar.add(updBtn); btnBar.add(delBtn); btnBar.add(clrBtn);
        form.add(new JLabel()); form.add(btnBar);

        // Table
        String[] cols = {"Bus ID","Name","Route","Date","Dep","Arr","Type","Fare","Seats"};
        DefaultTableModel mdl = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(mdl); UIUtils.styleTable(table);
        refreshBusTable(mdl);

        // Fill form on row click
        table.getSelectionModel().addListSelectionListener(e -> {
            if (table.getSelectedRow() < 0) return;
            int row = table.getSelectedRow();
            idF.setText   ((String) mdl.getValueAt(row, 0));
            nameF.setText ((String) mdl.getValueAt(row, 1));
            routeF.setText((String) mdl.getValueAt(row, 2));
            dateF.setText ((String) mdl.getValueAt(row, 3));
            depF.setText  ((String) mdl.getValueAt(row, 4));
            arrF.setText  ((String) mdl.getValueAt(row, 5));
            typeC.setSelectedItem(mdl.getValueAt(row, 6));
            fareF.setText(String.valueOf(mdl.getValueAt(row, 7)).replace("Rs. ",""));
            seatsF.setText(String.valueOf(mdl.getValueAt(row, 8)));
        });

        // ADD
        addBtn.addActionListener(e -> {
            try {
                validateBusForm(idF,nameF,routeF,seatsF,fareF);
                if (BusService.searchById(idF.getText().trim()) != null)
                    throw new Exception("Bus ID already exists.");
                BusService.addBus(new Bus(
                        idF.getText().trim(), nameF.getText().trim(),
                        routeF.getText().trim(), Integer.parseInt(seatsF.getText().trim()),
                        depF.getText().trim(), arrF.getText().trim(),
                        dateF.getText().trim(), Double.parseDouble(fareF.getText().trim()),
                        (String) typeC.getSelectedItem()
                ));
                UIUtils.showSuccess(this, "Bus added successfully!"); clearBusForm(idF,nameF,routeF,seatsF,depF,arrF,dateF,fareF); refreshBusTable(mdl);
            } catch (Exception ex) { UIUtils.showError(this, ex.getMessage()); }
        });

        // UPDATE
        updBtn.addActionListener(e -> {
            try {
                validateBusForm(idF,nameF,routeF,seatsF,fareF);
                boolean ok = BusService.updateBus(
                        idF.getText().trim(), nameF.getText().trim(), routeF.getText().trim(),
                        Integer.parseInt(seatsF.getText().trim()), depF.getText().trim(),
                        arrF.getText().trim(), dateF.getText().trim(),
                        Double.parseDouble(fareF.getText().trim()), (String) typeC.getSelectedItem()
                );
                if (!ok) throw new Exception("Bus ID not found.");
                UIUtils.showSuccess(this, "Bus updated!"); refreshBusTable(mdl);
            } catch (Exception ex) { UIUtils.showError(this, ex.getMessage()); }
        });

        // DELETE
        delBtn.addActionListener(e -> {
            String id = idF.getText().trim();
            if (id.isEmpty()) { UIUtils.showError(this, "Enter Bus ID to delete."); return; }
            if (!UIUtils.confirm(this, "Delete bus " + id + "?")) return;
            if (BusService.deleteBus(id)) { UIUtils.showSuccess(this, "Bus deleted!"); clearBusForm(idF,nameF,routeF,seatsF,depF,arrF,dateF,fareF); refreshBusTable(mdl); }
            else UIUtils.showError(this, "Bus not found.");
        });

        clrBtn.addActionListener(e -> clearBusForm(idF,nameF,routeF,seatsF,depF,arrF,dateF,fareF));

        JPanel top = new JPanel(new BorderLayout(0,8));
        top.setBackground(Theme.BG);
        top.add(form, BorderLayout.CENTER);

        p.add(top,                         BorderLayout.NORTH);
        p.add(UIUtils.styledScroll(table), BorderLayout.CENTER);
        return p;
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t); l.setForeground(Theme.SUBTEXT); l.setFont(Theme.FONT_SMALL); return l;
    }

    private void validateBusForm(JTextField id, JTextField name, JTextField route,
                                 JTextField seats, JTextField fare) throws Exception {
        if (UIUtils.isEmpty(id.getText(), name.getText(), route.getText(), seats.getText(), fare.getText()))
            throw new Exception("Bus ID, Name, Route, Seats and Fare are required.");
        if (!UIUtils.isNumeric(seats.getText())) throw new Exception("Seats must be a number.");
        if (!UIUtils.isNumeric(fare.getText()))  throw new Exception("Fare must be a number.");
    }

    private void clearBusForm(JTextField... fields) { for (JTextField f : fields) f.setText(""); }

    private void refreshBusTable(DefaultTableModel mdl) {
        mdl.setRowCount(0);
        for (Bus b : BusService.getAllBuses()) {
            mdl.addRow(new Object[]{
                    b.getBusId(), b.getBusName(), b.getRoute(), b.getTravelDate(),
                    b.getDepartureTime(), b.getArrivalTime(), b.getBusType(),
                    "Rs. " + b.getFare(), b.getTotalSeats()
            });
        }
    }

    // ── All Tickets Panel ─────────────────────────────────────
    private JPanel buildTicketPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(Theme.BG); p.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Search bar
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        bar.setBackground(Theme.CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER), new EmptyBorder(8,8,8,8)));
        JTextField searchF = UIUtils.styledField("Search by Ticket ID or Passenger Name");
        searchF.setPreferredSize(new Dimension(280, 32));
        RoundedButton cancelBtn = new RoundedButton("Cancel Selected", Theme.DANGER);
        cancelBtn.setPreferredSize(new Dimension(160, 32));
        RoundedButton deleteBtn = new RoundedButton("Delete Ticket", new Color(100,20,20));
        deleteBtn.setPreferredSize(new Dimension(130, 32));
        bar.add(searchF); bar.add(cancelBtn); bar.add(deleteBtn);

        String[] cols = {"Ticket ID","Passenger","Email","Bus","Route","Seat","Date","Fare","Status"};
        DefaultTableModel mdl = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(mdl); UIUtils.styleTable(table);
        refreshTicketTable(mdl);

        cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            String tid = row >= 0 ? (String) mdl.getValueAt(row, 0) : searchF.getText().trim();
            if (tid.isEmpty()) { UIUtils.showError(this, "Select a row or enter Ticket ID."); return; }
            if (!UIUtils.confirm(this, "Cancel ticket " + tid + "?")) return;
            if (TicketService.cancelTicket(tid)) { UIUtils.showSuccess(this, "Cancelled!"); refreshTicketTable(mdl); }
            else UIUtils.showError(this, "Ticket not found or already cancelled.");
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtils.showError(this, "Select a row to delete."); return; }
            String tid = (String) mdl.getValueAt(row, 0);
            if (!UIUtils.confirm(this, "Permanently delete ticket " + tid + "?")) return;
            if (TicketService.deleteTicket(tid)) { UIUtils.showSuccess(this, "Deleted!"); refreshTicketTable(mdl); }
        });

        // Live search
        searchF.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) {
                String q = searchF.getText().trim().toLowerCase();
                mdl.setRowCount(0);
                for (Ticket t : TicketService.getAllTickets()) {
                    if (q.isEmpty() || t.getTicketId().toLowerCase().contains(q)
                            || t.getPassengerName().toLowerCase().contains(q)) {
                        mdl.addRow(new Object[]{
                                t.getTicketId(), t.getPassengerName(), t.getPassengerEmail(),
                                t.getBusName(), t.getRoute(), t.getSeatNumber(),
                                t.getTravelDate(), "Rs. "+t.getFare(), t.getStatus()
                        });
                    }
                }
            }
        });

        p.add(bar, BorderLayout.NORTH);
        p.add(UIUtils.styledScroll(table), BorderLayout.CENTER);
        return p;
    }

    private void refreshTicketTable(DefaultTableModel mdl) {
        mdl.setRowCount(0);
        for (Ticket t : TicketService.getAllTickets()) {
            mdl.addRow(new Object[]{
                    t.getTicketId(), t.getPassengerName(), t.getPassengerEmail(),
                    t.getBusName(), t.getRoute(), t.getSeatNumber(),
                    t.getTravelDate(), "Rs. "+t.getFare(), t.getStatus()
            });
        }
    }

    // ── Passengers Panel ──────────────────────────────────────
    private JPanel buildUserPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(Theme.BG); p.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        bar.setBackground(Theme.CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER), new EmptyBorder(8,8,8,8)));
        JTextField searchF = UIUtils.styledField("Search by name or email");
        searchF.setPreferredSize(new Dimension(260, 32));
        RoundedButton delBtn = new RoundedButton("Delete Passenger", Theme.DANGER);
        delBtn.setPreferredSize(new Dimension(160, 32));
        bar.add(searchF); bar.add(delBtn);

        String[] cols = {"Name","Email","Phone","Role"};
        DefaultTableModel mdl = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(mdl); UIUtils.styleTable(table);
        refreshUserTable(mdl);

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtils.showError(this, "Select a passenger."); return; }
            String email = (String) mdl.getValueAt(row, 1);
            if (!UIUtils.confirm(this, "Delete passenger " + email + "?")) return;
            if (UserService.deleteUser(email)) { UIUtils.showSuccess(this, "Deleted!"); refreshUserTable(mdl); }
        });

        searchF.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) {
                String q = searchF.getText().trim().toLowerCase();
                mdl.setRowCount(0);
                for (User u : UserService.getAllUsers()) {
                    if (q.isEmpty() || u.getName().toLowerCase().contains(q)
                            || u.getEmail().toLowerCase().contains(q)) {
                        mdl.addRow(new Object[]{u.getName(), u.getEmail(), u.getPhone(), u.getRole()});
                    }
                }
            }
        });

        p.add(bar, BorderLayout.NORTH);
        p.add(UIUtils.styledScroll(table), BorderLayout.CENTER);
        return p;
    }

    private void refreshUserTable(DefaultTableModel mdl) {
        mdl.setRowCount(0);
        for (User u : UserService.getAllUsers())
            mdl.addRow(new Object[]{u.getName(), u.getEmail(), u.getPhone(), u.getRole()});
    }

    // ── Reports Panel ─────────────────────────────────────────
    private JPanel buildReportPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(Theme.BG); p.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Stats row
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setBackground(Theme.BG);
        statsRow.add(UIUtils.statCard(String.valueOf(BusService.getAllBuses().size()),       "Total Buses",       Theme.PRIMARY));
        statsRow.add(UIUtils.statCard(String.valueOf(TicketService.getTotalBooked()),        "Active Bookings",   Theme.SUCCESS));
        statsRow.add(UIUtils.statCard(String.valueOf(TicketService.getTotalCancelled()),     "Cancellations",     Theme.DANGER));
        statsRow.add(UIUtils.statCard("Rs. " + String.format("%.0f", TicketService.getTotalRevenue()), "Revenue", Theme.WARNING));

        // Revenue per bus table
        JPanel tablePanel = UIUtils.sectionPanel("Revenue by Bus");
        String[] cols = {"Bus ID","Bus Name","Route","Tickets Sold","Revenue"};
        DefaultTableModel mdl = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable t = new JTable(mdl); UIUtils.styleTable(t);

        for (Bus b : BusService.getAllBuses()) {
            List<Ticket> bTickets = TicketService.searchByBusId(b.getBusId());
            long sold = bTickets.stream().filter(tk -> tk.getStatus().equals("BOOKED")).count();
            double rev = bTickets.stream().filter(tk -> tk.getStatus().equals("BOOKED"))
                    .mapToDouble(Ticket::getFare).sum();
            mdl.addRow(new Object[]{b.getBusId(), b.getBusName(), b.getRoute(), sold, "Rs. " + (int)rev});
        }
        tablePanel.add(UIUtils.styledScroll(t), BorderLayout.CENTER);

        p.add(statsRow,   BorderLayout.NORTH);
        p.add(tablePanel, BorderLayout.CENTER);
        return p;
    }
}