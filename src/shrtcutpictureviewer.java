
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ikirov on 24/10/2016.
 */

public class shrtcutpictureviewer extends JPanel{


    JLabel label = new JLabel();
    JScrollPane jScrollPane = new JScrollPane(label);



    public shrtcutpictureviewer(){
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(150, 200));
        jScrollPane.setBackground(Color.GRAY);

    }


    public void getPositionOnPicture(int x, int y, String img) throws IOException {
        //ImageIcon image = new ImageIcon(img);
        BufferedImage bufmage;
        if (Viewer.rescaledImag!=null){
            bufmage = Viewer.rescaledImag;
        }else {
            bufmage = ImageIO.read(new File(img));
        }

        bufmage = bufmage.getSubimage(x - 60, y - 60, 150, 200);

        label.setPreferredSize(new Dimension(150, 200));
        label.setIcon(new ImageIcon(bufmage));
        jScrollPane.setViewportView(label);
        jScrollPane.getViewport().setViewPosition(new java.awt.Point(x - 75, y - 75));
        add(jScrollPane);
        revalidate();
        repaint();




    }

}
