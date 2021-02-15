import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * Window to display information
 * about ongoing measurements of chromosome
 */
public class InfoChromMeasureWindow extends JPanel {

    static JTextArea infowindow = new JTextArea();


    public InfoChromMeasureWindow() {
        setSize(150, 200);
        add(infowindow);
        infowindow.setWrapStyleWord(true);
        //infowindow.setBackground(Color.W);
        infowindow.setPreferredSize(new Dimension(150, 200));
        infowindow.setBorder(BorderFactory.createTitledBorder("Measurements"));
        infowindow.setFont(new Font("Serif", Font.PLAIN, 14));
        infowindow.setEditable(false);

    }


    static void chrMeasured(int i){
        showInfoChrom("Chromosomes: " + i);
    }

    static void showInfoChrom(String mes){
        infowindow.setText(mes);
    }
}