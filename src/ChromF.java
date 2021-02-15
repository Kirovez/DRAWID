import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import static java.awt.Color.*;
import static java.lang.Math.round;

/**
 * Created by ikirov on 20/10/2016.
 * Store infor about chromosome
 */
public class ChromF implements Comparator{
    ContainerForIdeoStorage containerAverage;

    static Color allChromosomeColor = Color.GRAY;
    public Color selColor = Color.RED;
    public int chromDistance = ConstStrorage.CHROMOSOME_DISTANCE; // distance between two chromosomes
    public int chromWidth = ConstStrorage.CHROMOSOME_WITH;
    static int dfX = ConstStrorage.DFX; // distance from X
    static int dfY = ConstStrorage.DFY; // // distance from Y
    static int FontNSize = ConstStrorage.FONTSIZE; //font size of the Chromosome name

    /////////////////////////////////////////////
    public Metadata metaTable;// Metadata table
    /////////////////////////////////////////////

    public int centromerSize;
    public boolean drawBar = false;

    ChromSumData ChromSum;

    private float lowestlevel = 0;

    public String chromName;
    public Float centromereIndex;
    public Float Length;
    public int ChromNumberIndex;

    public float shortArmLength;
    public float longArmLength;

    public Color chromColor = Color.GRAY;
    public Color centromerColor = BLACK;

    public double centromerePosX;
    public double centromerePosY;

    public double centromerePosX2;
    public double centromerePosY2;

    public double centromerePosX3;
    public double centromerePosY3;

    public double[] revtrianglecentromere;

    private int position;

    public double longArmPosX;
    public double longArmPosY;

    public double shortArmPosX;
    public double shortArmPosY;

    public float verL = 0;

    String ChromInfo;

    public void changeChromosomeLength(float verL){ // get percentage
        float factor = verL/100;
        float shortArmVer;
        this.verL = factor - 1;

        if (factor < 1){ // decrease length
            shortArmVer = shortArmLength*(1-factor);
            shortArmPosY += shortArmVer;
        } else if ( factor < 2 && factor > 1){ // no more than 200%
            shortArmVer = shortArmLength*(factor-1);
            shortArmPosY -= shortArmVer;
        }

        shortArmLength *=factor;
        longArmLength*=factor;


    }

    static void resetChromF(){
        Color allChromosomeColor = Color.GRAY;
        int chromDistance = ConstStrorage.CHROMOSOME_DISTANCE; // distance between two chromosomes
        int chromWidth = ConstStrorage.CHROMOSOME_WITH;
        int dfX = ConstStrorage.DFX; // distance from X
        int dfY = ConstStrorage.DFY; // // distance from Y
        int FontNSize = ConstStrorage.FONTSIZE; //font size of the Chromosome name
    }


    // return coordinates for reversed centromere triangle

    public double[] reversecentromere(double[] triangle){
        double[] ret = new double[6];

        ret[0] = triangle[0];
        ret[1] = triangle[1] + centromerSize;
        ret[2] = triangle[2];
        ret[3] = triangle[3] + centromerSize;
        ret[4] = triangle[4];
        ret[5] = triangle[5];


        return ret;
    }

    public void verticalMoveIdeo(int vesY){
        shortArmPosY+=vesY;
        longArmPosY+=vesY;


        RecalculatePositionX();
        this.centromerePosY2 = centromerePosY;

        this.centromerePosY3 = centromerePosY + 0.5*centromerSize;

        double[] rev = {this.centromerePosX,centromerePosY,this.centromerePosX2,this.centromerePosY2,this.centromerePosX3,this.centromerePosY3};

        this.revtrianglecentromere = reversecentromere(rev);

    }

    public int getVelYforString(Font f, Graphics2D g2, int Width, String s){
        FontMetrics fm = g2.getFontMetrics(f);
        Rectangle2D bounds = fm.getStringBounds(s, g2);
        int Ilength = (int)bounds.getWidth();
        int Iheight = (int)bounds.getHeight();

        return (int) ((Width/2) - Ilength/2);
    }


    public void paintChromosome(Graphics2D g, boolean selYes){
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // Build average idiogram from this

//        if (ChromF.drawBar){
//           shortArmPosY -= (IdeoStrorage.meanLengthSValue.get(chromName) - shortArmLength);
//           shortArmLength += (IdeoStrorage.meanLengthSValue.get(chromName) - shortArmLength);
//           longArmLength += (IdeoStrorage.meanLengthLValue.get(chromName) - longArmLength);
//        }

        g.setColor(Color.BLACK);
        final Font f = new Font("Comic", Font.CENTER_BASELINE, ChromF.FontNSize);
        g.setFont(f);
        g.drawString(this.chromName, (int) this.centromerePosX + getVelYforString(f, g, chromWidth,this.chromName), 20);

        if (selYes) {
            g.setColor(this.selColor);
        } else {
            g.setColor(this.chromColor);
        }

        g.fillRect((int) this.shortArmPosX,
                (int) this.shortArmPosY,
                chromWidth,
                (int) this.shortArmLength + 1
        );



        g.fillRect((int) this.longArmPosX,
                (int) this.longArmPosY,
                chromWidth,
                (int) this.longArmLength
        );

        if (metaTable != null) {
            metaTable.paintMetadata(g, (int) centromerePosX, chromWidth,this.shortArmPosY,this.longArmPosY, this.verL);
        }

        g.setColor(ConstStrorage.BACKGROUND_PLOTCOLOR);

        // rect for centromere
        g.fillRect((int) this.shortArmPosX, (int) centromerePosY, chromWidth, centromerSize);

        g.setColor(this.centromerColor);
        Polygon centromere = new Polygon();
        centromere.addPoint((int) this.centromerePosX, (int) centromerePosY);
        centromere.addPoint((int) this.centromerePosX2, (int) this.centromerePosY2);
        centromere.addPoint((int) this.centromerePosX3, (int) this.centromerePosY3);
        centromere.addPoint((int) this.revtrianglecentromere[0], (int) this.revtrianglecentromere[1]);
        centromere.addPoint((int) this.revtrianglecentromere[2], (int) this.revtrianglecentromere[3]);
        centromere.addPoint((int) this.revtrianglecentromere[4], (int) this.revtrianglecentromere[5]);
        g.fillPolygon(centromere);

        // Draw SD bars
        if (drawBar){

        int diameterDot = 10;
        int line_positionX = (int) centromerePosX + (int) (chromWidth*1.2); // distance between chromosomes and bar

            //CI SD
        int meanCIPositionY = (int) centromerePosY + centromerSize/2 - 5;
            g.setColor(Color.GREEN);
        g.drawLine(line_positionX + diameterDot/2, (int) (meanCIPositionY - ((containerAverage.SDCIValue.get(chromName)*Length)/100)),
                line_positionX + diameterDot/2, (int) (meanCIPositionY + ((containerAverage.SDCIValue.get(chromName)*Length)/100 + diameterDot)));
            g.setColor(Color.BLUE);
            g.fillOval(line_positionX, meanCIPositionY, diameterDot,  diameterDot);

            // s Arm SD
        int meanSPositionY = (int) shortArmPosY - diameterDot/2;
            g.setColor(Color.BLUE);
        g.drawLine(line_positionX + diameterDot/2, (int) (meanSPositionY - containerAverage.SDLengthSValue.get(chromName)),
                line_positionX + diameterDot/2, (int) (meanSPositionY + containerAverage.SDLengthSValue.get(chromName) + diameterDot));
            g.setColor(Color.GREEN);
            g.fillOval(line_positionX, meanSPositionY, diameterDot,  diameterDot);

            // L arm SD
        int meanLPositionY = (int) (longArmPosY + longArmLength) - diameterDot/2;
            g.setColor(Color.BLUE);
        g.drawLine(line_positionX + diameterDot/2, (int) (meanLPositionY - containerAverage.SDLengthLValue.get(chromName)),
                line_positionX + diameterDot/2, (int) (meanLPositionY + containerAverage.SDLengthLValue.get(chromName) + diameterDot));
            g.setColor(Color.GREEN);
            g.fillOval(line_positionX, meanLPositionY, diameterDot,  diameterDot);


//            shortArmPosY += (IdeoStrorage.meanLengthSValue.get(chromName) - shortArmLength);
//            shortArmLength -= (IdeoStrorage.meanLengthSValue.get(chromName) - shortArmLength);
//            longArmLength -= (IdeoStrorage.meanLengthLValue.get(chromName) - longArmLength);
//

        }

    }
    public float getAsoluteLength(){
        return Math.round(Length/ChromSum.multypleY);
    }
    public String getInfo(){
        if (drawBar){
            ChromInfo = "Mean Total Length: " + "\n" + Math.round((float)containerAverage.meanLengthWhole.get(chromName)) + " \u00B1 " + Math.round((float)containerAverage.SDLengthWhole.get(chromName)/ChromSum.multypleY) + "\n" +
                    "Mean Length S: " + "\n" + Math.round((float)containerAverage.meanLengthSValue.get(chromName)) + " \u00B1 " + Math.round((float)containerAverage.SDLengthSValue.get(chromName)/ChromSum.multypleY) +"\n" +
                    "Mean Length L: " + "\n" + Math.round((float)containerAverage.meanLengthLValue.get(chromName)) + " \u00B1 " + Math.round((float)containerAverage.SDLengthLValue.get(chromName)/ChromSum.multypleY) +"\n" +
                    "Cent. Index: " + "\n" + Math.round((float)containerAverage.meanCIValue.get(chromName)) + " \u00B1 " + Math.round((float)containerAverage.SDCIValue.get(chromName));
        } else {
            ChromInfo = "Name: " + chromName + "\n" + "Index: "  + Math.round(centromereIndex) + "\n" + "Length: " + Math.round(Length/ChromSum.multypleY);

        }

        return ChromInfo;
    }

    public void RecalculatePositionX(){

        this.centromerePosX = dfY + (this.ChromNumberIndex -1)*(chromWidth + chromDistance);

        this.centromerePosX2 = this.centromerePosX + chromWidth;

        this.centromerePosX3 = this.centromerePosX + 0.5*chromWidth;

        double[] rev = {this.centromerePosX,centromerePosY,this.centromerePosX2,this.centromerePosY2,this.centromerePosX3,this.centromerePosY3};

        this.revtrianglecentromere = reversecentromere(rev);

        this.longArmPosX =  this.centromerePosX;

        this.shortArmPosX =  this.centromerePosX;

    }


    public ChromF(String chromName, Float centromereIndex, float Length, int ChromNumberIndex,ChromSumData ChromSum,
                  float centromerePosY, float chromWidth, float chromDistance, int centromerSize, boolean drawBar) {
        resetChromF();
        this.drawBar = drawBar;
        this.chromWidth = (int) chromWidth;
        this.chromDistance = (int) chromDistance;
        this.centromerePosY = centromerePosY;
        this.centromerSize = centromerSize;

        this.ChromSum = ChromSum;

        this.ChromNumberIndex = ChromNumberIndex;
        this.chromName = chromName;
        this.centromereIndex = centromereIndex;

        this.Length = Length;

        this.shortArmLength = ((centromereIndex*Length)/100);
        this.longArmLength = Length - this.shortArmLength;

        RecalculatePositionX();

        this.centromerePosY2 = centromerePosY;

        this.centromerePosY3 = centromerePosY + 0.5*centromerSize;

        double[] rev = {this.centromerePosX,centromerePosY,this.centromerePosX2,this.centromerePosY2,this.centromerePosX3,this.centromerePosY3};

        this.revtrianglecentromere = reversecentromere(rev);

        this.longArmPosY = centromerePosY + centromerSize;

        this.shortArmPosY = centromerePosY - this.shortArmLength;

    }


    @Override
    public int compare(Object o1, Object o2) {
        return 0;
    }
}
