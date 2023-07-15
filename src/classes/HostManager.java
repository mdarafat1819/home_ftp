package classes;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HostManager extends JFrame {
    JLabel JL_host_name, JL_host_ip;
    JTextField JTF_host_name, JTF_host_ip;
    JButton btn_add;
    JTable table = new JTable() {
         private static final long serialVersionUID = 1L;

        public boolean isCellEditable(int row, int column) {                
                return false;               
        };
    };
    JScrollPane pane;
    Object[] columns = null;

    public HostManager() {

        JL_host_name = new JLabel("Host Nick Name: ");
        JL_host_name.setBounds(20, 15, 200, 20);

        JL_host_ip = new JLabel("Host IP: ");
        JL_host_ip.setBounds(20, 40, 200, 20);

        JTF_host_name = new JTextField();
        JTF_host_name.setBounds(120, 15, 230, 25);

        JTF_host_ip = new JTextField();
        JTF_host_ip.setBounds(120, 45, 230, 25);

        btn_add = new JButton("ADD");
        btn_add.setBounds(350, 15, 125, 55);

        columns = new String[] { "Host Name", "Host IP" };

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setColumnIdentifiers(columns);

        ArrayList<String> host_info = new FileManager().ReadData("resources\\host_name.bat");

       for(int i = 0; i < host_info.size(); i++) {
         StringTokenizer st = new StringTokenizer(host_info.get(i)," ");  
     while (st.hasMoreTokens()) {  
        model.addRow(new Object[]{st.nextToken(), st.nextToken()});
         //System.out.println(st.nextToken());  

     }  
       }
        // Add A Row To JTable From JTextfields
        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String fileData = JTF_host_name.getText() + " "+ JTF_host_ip.getText() + "\n";
                FileManager file = new FileManager();
                file.WriteData("resources\\host_name.bat",fileData);
                model.addRow(new Object[] {
                        JTF_host_name.getText(),
                        JTF_host_ip.getText()
                });
            }
        });

        pane = new JScrollPane(table);
        pane.setBounds(15, 80, 460, 280);
        setLayout(null);
        add(JL_host_name);
        add(JL_host_ip);
        add(JTF_host_name);
        add(JTF_host_ip);

        add(pane);
        add(btn_add);

       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setSize(500, 400);
        setResizable(false);

    }

    public static void main(String[] args) {
        new HostManager();
    }
}