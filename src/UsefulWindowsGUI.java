import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Some useful windows
 */
public class UsefulWindowsGUI {



    static File FileCh(String Browsename,String format, String filename) {

        File file;
        int rVal = 0;

        JFileChooser chooser = new JFileChooser();
        if (!(ConstStrorage.lastAdress.equals(""))){
            chooser.setCurrentDirectory(new File(ConstStrorage.lastAdress));
        }else {
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        }

        try{
            if (format.equals("jpg")){
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
                chooser.setFileFilter(filter);
                rVal = chooser.showOpenDialog(null);
            } else if(format.equals("txt")){
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text format", "txt");
                chooser.setFileFilter(filter);
                rVal = chooser.showOpenDialog(null);
            } else if(format.equals("xlsx")){
                chooser.setSelectedFile(new File(""));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("XL files format", "xlsx");
                chooser.setFileFilter(filter);
                rVal = chooser.showSaveDialog(null);

            }else if(format.equals("xread")) {
                rVal = chooser.showOpenDialog(null);
            }

        } catch (Exception e){};


        chooser.setDialogTitle(Browsename);


        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (rVal == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            //System.out.println(file);
            ConstStrorage.lastAdress = file.getAbsolutePath();

            return file;
        } else {
            JOptionPane.showMessageDialog(null, "File has not been selected");
            return null;
        }

    }

}
