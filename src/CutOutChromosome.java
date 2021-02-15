import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class takes polygon and picture as ImageIcon object and return picture bordered by polygon
 */
public class CutOutChromosome {

    ArrayList<int[]> polygon;
    ArrayList<Integer> Xcrd = new ArrayList<>();
    ArrayList<Integer> Ycrd = new ArrayList<>();

    ImageIcon im;

    public Path2D getPolygon(){ //ArrayList<int[]> polygon
        Path2D path = new Path2D.Double();
        for (int i = 0; i < Xcrd.size(); i++){
            if (i == 0){
                path.moveTo(Xcrd.get(i), Ycrd.get(i));
            } else {
                path.lineTo(Xcrd.get(i), Ycrd.get(i));
            }
        }

        path.closePath();

        return path;
    }


    public void applyClip() throws IOException {
        BufferedImage image = new BufferedImage(im.getIconWidth(), im.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics g = image.getGraphics();

        g.setClip(getPolygon());

        g.drawImage(im.getImage(), 0, 0, null);

        int pos1 = Collections.min(Xcrd);
        int pos2 = Collections.min(Ycrd);

        BufferedImage croppedImage = image.getSubimage(pos1, pos2, 150, 150);

        ImageIO.write(croppedImage, "PNG", new File("yourImageName.PNG"));

        //return g;
    }

    public CutOutChromosome(ArrayList<Integer> Xcrd, ArrayList<Integer> Ycrd, ImageIcon im){
        this.Xcrd = Xcrd;
        this.Ycrd = Ycrd;
        this.im = im;

    }

}
