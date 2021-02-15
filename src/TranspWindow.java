/**
 * Created by ikirov on 23/10/2016.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import static java.awt.GraphicsDevice.WindowTranslucency.*;

public class TranspWindow extends JPanel {

    int x;
    int y;
    String picture;
    String chromosomename;
    HashMap<String, int[]> positiononpicture;

    final JTextArea message = new JTextArea();

    shrtcutpictureviewer pic;


    public TranspWindow(){
        setSize(150,200);
        add(message);
        message.setWrapStyleWord(true);
        message.setBackground(Color.GRAY);
        message.setPreferredSize(new Dimension(150,200));
        message.setBorder( BorderFactory.createTitledBorder("Information"));
        message.setFont(new Font("Serif", Font.PLAIN, 15));
        message.setEditable(false);
    }

    public void showWindow(String mes, String picture, String chromosomename,  HashMap<String, int[]> positiononpicture, shrtcutpictureviewer pic) throws IOException {
        message.setText(mes);


        try{
            this.pic = pic;
            this.positiononpicture = positiononpicture;
            this.chromosomename = chromosomename;
            this.picture = picture;
            x = positiononpicture.get(chromosomename)[0];
            y = positiononpicture.get(chromosomename)[1];
            pic.getPositionOnPicture(x,y, picture);
        }catch (Exception e){

        }


    }
}