package classes;

import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FileUploader extends JFrame {
    JLabel JL_browse, JL_host;
    JTextField JTF_file_url;
    JButton browse, upload;
    JTable table = new JTable();
    JScrollPane pane;
    Object[] columns = null;
    JFileChooser jfc;
    JComboBox JCB_host;

    public FileUploader() {

        final File[] fileToSend = new File[1];
        JLabel jlFileName = new JLabel("Choose a file to send.");

        JL_browse = new JLabel("Choose a file: ");
        JL_browse.setBounds(20, 15, 200, 20);

        JL_host = new JLabel("Choose a host: ");
        JL_host.setBounds(20, 40, 200, 20);

        ArrayList<String> receiver_list = new FileManager().ReadData("resources\\host_name.bat");
        String[] hosts = receiver_list.toArray(new String[receiver_list.size()]);
        JCB_host = new JComboBox<String>(hosts);

        JCB_host.setBounds(120, 45, 231, 25);

        JTF_file_url = new JTextField();
        JTF_file_url.setBounds(120, 15, 230, 25);

        browse = new JButton("Browse");
        browse.setBounds(350, 15, 125, 25);

        upload = new JButton("Upload");
        upload.setBounds(350, 45, 125, 25);

        columns = new String[] { "Host Name", "Host IP", "Status" };

        DefaultTableModel log_table = (DefaultTableModel) table.getModel();

        log_table.setColumnIdentifiers(columns);

        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Choose a file to send.");
                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    JTF_file_url.setText(fileToSend[0].getAbsolutePath());
                }
            }
        });

        upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileToSend[0] == null) {
                    jlFileName.setText("Please choose a file to send first!");
                } else {
                    try {

                        // Create an input stream into the file you want to send.
                        String temp_host = (String) JCB_host.getSelectedItem();
                        ArrayList<String> host_ip = new ArrayList<String>();
                        if (temp_host.equals("Everyone all_pc")) {
                            // System.out.println("Hello All_pc");
                            for (int i = 129; i < 171; i++) {
                                host_ip.add("10.13.221." + i);
                            }
                        } else {
                            StringTokenizer st = new StringTokenizer(temp_host, " ");
                            st.nextToken();
                            host_ip.add(st.nextToken());
                        }

                        // System.out.println(toString(JCB_host.getSelectedItem()) = "Everyone all_pc");
                        for (int i = 0; i < host_ip.size(); i++) {
                            try {

                                Socket socket = new Socket();

                                socket.connect(new InetSocketAddress(host_ip.get(i), 1234), 500);
                                socket.setSoTimeout(500);
                                System.out.println("Connection Established With: " + host_ip.get(i));

                                FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                String fileName = fileToSend[0].getName();

                                byte[] fileNameBytes = fileName.getBytes();
                                System.out.println(fileToSend[0].length());
                                byte[] fileBytes = new byte[(int) fileToSend[0].length()];
                                fileInputStream.read(fileBytes);
                                dataOutputStream.writeInt(fileNameBytes.length);
                                dataOutputStream.write(fileNameBytes);
                                dataOutputStream.writeInt(fileBytes.length);
                                dataOutputStream.write(fileBytes);

                                socket.close();
                            } catch (Exception ex) {
                                // System.out.println(ex.getMessage() + "From " + host);
                                System.out.println("Connection Refused From: " + host_ip.get(i));
                                continue;
                            }
                        }

                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                    }
                }
            }
        });

        pane = new JScrollPane(table);
        pane.setBounds(15, 80, 460, 280);
        setLayout(null);
        add(JL_browse);
        add(JL_host);
        add(JCB_host);
        add(JTF_file_url);

        add(pane);
        add(browse);
        add(upload);

        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        new FileUploader();
    }
}