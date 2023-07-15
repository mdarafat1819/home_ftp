
package classes;

import java.awt.Component;

import java.awt.event.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;

public class Server extends JFrame {
    private ServerSocket serverSocket;
    JLabel JL_choose_receiver;
    JScrollPane pane;
    JComboBox JCB_host;
     JPanel jPanel;
      int fileId = 0;
     ArrayList<MyFile> myFiles = new ArrayList<>();

    public Server() {

        JL_choose_receiver = new JLabel("Choose a Receiver: ");
        JL_choose_receiver.setBounds(20, 15, 200, 20);

        ArrayList<String> receiver_list = new FileManager().ReadData("resources\\host_name.bat");
        String[] hosts = receiver_list.toArray(new String[receiver_list.size()]);
        JCB_host = new JComboBox<String>(hosts);
        
        JCB_host.setBounds(130, 15, 345, 25);

        JCB_host.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(JCB_host.getSelectedItem() + " Clicked");
            }
        });
         
       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        add(JL_choose_receiver);
        add(JCB_host);

        jPanel = new JPanel();
        pane = new JScrollPane(jPanel);
        pane.setBounds(15, 45, 460, 300);
        add(pane);

        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
         setVisible(true);
        setLocationRelativeTo(null);
        setSize(500, 400);
        setResizable(false);

    }

    public void start() {
        try {
            serverSocket = new ServerSocket(1234);

            // Create a new thread to handle the client connections
            Thread connectionThread = new Thread(() -> {
                while (true) {
                    try {
                        // Accept a client connection
                        Socket clientSocket = serverSocket.accept();
                        
                        // Handle the client connection in a separate thread
                        Thread clientThread = new Thread(() -> handleClient(clientSocket));
                        clientThread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            connectionThread.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
    }
    private void handleClient(Socket clientSocket) {
        
        try{

             DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                int fileNameLength = dataInputStream.readInt();
                // If the file exists
                if (fileNameLength > 0) {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                    String fileName = new String(fileNameBytes);
                    int fileContentLength = dataInputStream.readInt();
                    if (fileContentLength > 0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);
                        JPanel jpFileRow = new JPanel();
                        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.X_AXIS));
                        JLabel jlFileName = new JLabel(fileName);
                        if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
                            jpFileRow.setName((String.valueOf(fileId)));
                            jpFileRow.addMouseListener(getMyMouseListener());
                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);
                            validate();
                        } else {
                            jpFileRow.setName((String.valueOf(fileId)));
                            jpFileRow.addMouseListener(getMyMouseListener());
                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);
                            validate();
                        }
                        myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName)));
                        fileId++;
                    }
                
        } 
    }catch(Exception ex) {
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        new Server().start();
    }
    public boolean fileDownload(String fileName, byte[] fileData, String fileExtension) {
        JLabel jlFileContent = new JLabel();
        jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (fileExtension.equalsIgnoreCase("txt")) {
            jlFileContent.setText("<html>" + new String(fileData) + "</html>");
        } else {
            jlFileContent.setIcon(new ImageIcon(fileData));
        }
        File fileToDownload = new File(fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
            fileOutputStream.write(fileData);
            fileOutputStream.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i + 1);
        } else {
            return "No extension found.";
        }
    }
    public MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel jPanel = (JPanel) e.getSource();
                int fileId = Integer.parseInt(jPanel.getName());
                for (MyFile myFile : myFiles) {
                    if (myFile.getId() == fileId) {
                        fileDownload(myFile.getName(), myFile.getData(), myFile.getFileExtension());
                        System.out.println("Download Success");
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }
}
