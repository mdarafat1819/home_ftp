package classes;

import java.awt.event.*;

import javax.swing.*;

public class Home extends JFrame {

    JButton file_share, server, host, setting;
    JLabel apps_name;

    public Home() {
        apps_name = new JLabel("<html><font size='10'color=green> Home FTP</font></html>");
        apps_name.setBounds(150, 20, 350, 50);
        file_share = new JButton("File Share");
        file_share.setBounds(15, 100, 220, 90);

        server = new JButton("server");
        server.setBounds(235, 100, 230, 90);

        host = new JButton("Host");
        host.setBounds(15, 190, 220, 90);

        setting = new JButton("Setting");
        setting.setBounds(235, 190, 230, 90);

        file_share.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FileUploader();
                // new Server();
            }
        });

        host.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // new FileUploader();
                // new Server();
                new HostManager();
            }
        });
        server.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Server().start();
            }
        });

        setLayout(null);
        add(apps_name);
        add(file_share);
        add(server);
        add(host);
        add(setting);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setSize(500, 400);
        setResizable(false);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        new Home();
    }
}