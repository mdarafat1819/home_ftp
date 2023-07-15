package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {
   
    public boolean WriteData(String  file_name, String data) {

        try {
            FileWriter file = new FileWriter(file_name, true);
            BufferedWriter buffer = new BufferedWriter(file);
            buffer.write(data);
            buffer.close();
             file.close();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public ArrayList<String> ReadData(String file_path) {
        ArrayList<String> data = new ArrayList<String>();
        try {
            FileReader file = new FileReader(file_path);
            BufferedReader buffer = new BufferedReader(file);
            String line;
            while((line = buffer.readLine()) !=  null) {
                data.add(line);
            }
            buffer.close();
            file.close();
            return data;
        } catch (IOException ex) {
            return data;
        }
    }


    public static void main(String[] args) {
       // WriteData("testout.bat", "hello world\n");
       //ReadData("testout.bat");
    }
}
