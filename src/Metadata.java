import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.*;
import java.util.List;

/**
 * Class describes position, color, shape for metadata
 */
public class Metadata {
    public float multypleY;
    float centromereSize;
    HashMap<String, Color> colorForMetanames;

    float Xposition = 0;

    static Color[] colorpalette;

    float currentVerL = 0; // verL to be set


    ArrayList<ArrayList<Object>> metadataTable; // Name,position1, 2

    Map<String, String> metaShapes = new HashMap<>(); // Name: shape type

    Map<String, Color> metaColor = new HashMap<>(); // Name: Color

    public Metadata(float multypleY,ArrayList<ArrayList<Object>> metadataTable, float centromereSize){
        this.centromereSize = centromereSize;
        this.metadataTable = metadataTable;
        this.multypleY = multypleY;
    }

//    // if size has been changed
//    public float[] fitYtoVer(float o1, float o2){
//        float[] toRet = {Math.min(o1,o2),Math.max(o1,o2)};
//        if (currentVerL != 0) {
//            if (o1 != o2) { // band
//                toRet[0] = o1*(1+ currentVerL);
//                toRet[1] = o1 + 2*((o2 - o1)*(1 + currentVerL));
//            }
//        }
//        currentVerL = 0;
//        return toRet;
//    }

    public float[] fitY(Float o1, Float o2,double shortArmPosY, double longArmPosY){

        float ret[] = new float[2];
        float Y1 = o1;
        float Y2 = o2;

        // add start of chromosome
        Y1 = Y1*multypleY + (float) shortArmPosY;
        Y2 = Y2*multypleY + (float) shortArmPosY;


        // if the point on the long arm the centromere size is added;
        if (Y1 > longArmPosY - centromereSize ){
            Y1 = Y1 + centromereSize;
        }else {
            Y1 -= 4;  //kostil
        }

        // if the point on the long arm the centromere size is added;
        if (Y2 > longArmPosY - centromereSize ) { // I do not know why but  - Centromer size works //
            Y2 = Y2 + centromereSize;
        }else {
            Y2 -= 4;
        }

        ret[0] = Y1;
        ret[1] = Y2;
        //ret = fitYtoVer(Y1,Y2);

        return ret;
    }

//    public void NOR_spacer(Graphics g, int X, int Y, int Width){
//        int link_width = (Width/ConstStrorage.NORlinkWIDTH)*2;
//        g.setColor(Color.BLACK);
//        g.fillRect(X + (int) (Width*(ConstStrorage.NORlinkWIDTH - 1)/ConstStrorage.NORlinkWIDTH), Y, link_width, (int) (Width/ConstStrorage.NORlinkHEIGHT));
//    }


    private void drawNOR(Graphics g, int X, int Y1, int Y2, int Width, int cnt){

        // white block above
        g.setColor(ConstStrorage.BACKGROUND_PLOTCOLOR);
        g.fillRect(X,  Math.min(Y1, Y2)  - (int) (Width/ConstStrorage.NORlinkHEIGHT),
                Width, (int) (Width/ConstStrorage.NORlinkHEIGHT));

        // NOR block
        g.setColor(ConstStrorage.NORCOLOR);
        g.fillRect(X, Math.min(Y1, Y2), Width, Math.abs(Y1-Y2));

        // White block below
        g.setColor(ConstStrorage.BACKGROUND_PLOTCOLOR);
        g.fillRect(X, Math.max(Y1, Y2), Width, (int) (Width/ConstStrorage.NORlinkHEIGHT));

    }

    public void paintMetadata(Graphics g, int X, int Width, double shortArmPosY, double longArmPosY, float verL){
        this.currentVerL = verL;

        int cnt = 0;
        for(ArrayList<Object> each: metadataTable){
            cnt++;

            float[] YY = fitY((Float) each.get(1), (Float) each.get(2), shortArmPosY, longArmPosY);

            float Y1 = YY[0];
            float Y2 = YY[1];


            g.setColor(colorForMetanames.get(String.valueOf(each.get(0))));
            // dot
            if ((int) Y1 == (int) Y2) {
                g.fillOval(X, (int) Y1, Width/2, Width/4);
                g.fillOval(X + Width/2, (int) Y2, Width/2, Width/4);

            //band
            }else {
                int height = Math.max((int) Y1, (int) Y2) - Math.min((int) Y1, (int) Y2);

                if (String.valueOf(each.get(0)).equals("NOR")){

                   drawNOR(g,X, (int) Y1, (int) Y2,Width, cnt);

                } else {
                    g.fillRect(X, Math.min((int) Y1, (int) Y2), Width, height);

                }

            }
            g.drawString(String.valueOf(each.get(0)), X + Width, ((int) Y1 + (int) Y2)/2 + Width/8);

        }
    }

    public Metadata(float centromereSize){
        this.centromereSize = centromereSize;

    }


}
