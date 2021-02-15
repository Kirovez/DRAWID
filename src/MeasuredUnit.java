import java.awt.*;
import java.util.*;

/**
 * Class takes coordinates
 */
public class MeasuredUnit {
    static int[] bar = new int[2]; //[mkm, pxl]
    ArrayList<Integer> Xcoordinates = new ArrayList<>();
    ArrayList<Integer> Ycoordinates = new ArrayList<>();
    ArrayList<Color> dotColorUnit = new ArrayList<>();
    ArrayList<Color> lineColorUnit = new ArrayList<>();
    public String nameUnit;
    public float unitLength = 0;
    public int currentlength = 0;
    public float shortArmLengthUnit = 0;
    public float longArmLengthUnit = 0;
    public HashMap<Integer, String> metUnit = new HashMap<>();
    static int linetoexclude = 0;

    String mname; // temporary storage for band names

    int centromeredotindex;
    public float unitCentromereIndex;

    static ArrayList<String> metananes = new ArrayList<>();

    static float barIndexMuliply = 1;
    public float localBarIndex = 1;


    //Map<String, float[]> calculatedMeta = new HashMap<String, float[]>(); // contains name of the meta mark and two coordinates: from to

    ArrayList<ArrayList<Object>> calculatedMeta =  new ArrayList<>(); //[metaname, coord1, coord2]

    public MeasuredUnit(String nameUnit, ArrayList<Integer> Xcoordinates, ArrayList<Integer> Ycoordinates,
                        ArrayList<Color> dotColorUnit, ArrayList<Color> lineColorUnit, int centromeredotindex,
                        HashMap<Integer, String> metUnit) {
        this.metUnit = metUnit;
        this.nameUnit = nameUnit;
        this.centromeredotindex = centromeredotindex;
        for (Color each : dotColorUnit) {
            this.dotColorUnit.add(each);
        }
        for (Color each : lineColorUnit) {
            this.lineColorUnit.add(each);
        }

        for (Integer each : Xcoordinates) {
            this.Xcoordinates.add(each);
        }

        for (Integer each : Ycoordinates) {
            this.Ycoordinates.add(each);
        }

        ArrayList<Object> tem = new ArrayList<>();
        ArrayList<Object> temnor = new ArrayList<>();

        String tempname = "";

        // determine centromer index and length
        for (int i = 0; i < this.Xcoordinates.size(); i++){

            if (i > 0 && i!=linetoexclude){
                unitLength += lengthbycoordinates(Xcoordinates.get(i - 1), Ycoordinates.get(i - 1), Xcoordinates.get(i), Ycoordinates.get(i));
            }


            // dot is centromere
            if (i == centromeredotindex) {
                currentlength += unitLength;
            }

            if (dotColorUnit.get(i) == PictureMeasure.bcolor && tem.size() == 0) {
                mname = metUnit.get(i);
                tem.add(mname);
                tem.add((float) unitLength);
                tempname = mname;

            }
            else if (dotColorUnit.get(i) == PictureMeasure.bcolor_c) {
                tem.add((float) unitLength);
                calculatedMeta.add(tem);
                tem = new ArrayList<>();
            }
            //if Band in the end of the chromosome
            else if ((dotColorUnit.get(i) == PictureMeasure.bcolor||dotColorUnit.get(i) == PictureMeasure.nor) && tem.size() != 0 && i == dotColorUnit.size() -1 ){
                tem.add((float) unitLength);
                calculatedMeta.add(tem);
                tem = new ArrayList<>();
            }

            // dot is DD marked
            else if (dotColorUnit.get(i) == PictureMeasure.ddcolor) {
                ArrayList<Object> temd = new ArrayList<>();
                temd.add(metUnit.get(i));
                temd.add((float) unitLength);
                temd.add((float) unitLength);
                calculatedMeta.add(temd);
                tempname = metUnit.get(i);

            } else if(dotColorUnit.get(i) == PictureMeasure.nor && temnor.size() == 0){
                mname = metUnit.get(i);
                temnor.add(mname);
                temnor.add((float) unitLength);
                tempname = mname;
            }
            else if (dotColorUnit.get(i) == PictureMeasure.nor_c) {
                temnor.add((float) unitLength);
                calculatedMeta.add(temnor);
                temnor = new ArrayList<>();
            }

            if (metananes != null){
                if (!(metananes.contains(tempname)) && !(tempname.equals(""))){
                    metananes.add(tempname);
                }
            }
        }

        // Chromosome parametres calculation
            shortArmLengthUnit = Math.min(currentlength, unitLength - currentlength);

        // converting the coordinates if the measurements started from long arm
            if (shortArmLengthUnit != currentlength) {
                if (metUnit != null) {
                    for (ArrayList<Object> each : calculatedMeta) {
                        each.set(1, Math.abs(unitLength - (float) each.get(1)) + 1);
                        each.set(2, Math.abs(unitLength - (float) each.get(2)) + 1);
                    }
                }
            }
        // CI calculation
            longArmLengthUnit = unitLength - shortArmLengthUnit;
            unitCentromereIndex = shortArmLengthUnit * 100 / unitLength;
        }


    static double lengthbycoordinates(int X1,int Y1,int X2, int Y2){
        double sumsqrt = Math.pow(Math.abs(X1 - X2),2) + Math.pow(Math.abs(Y1 - Y2), 2);
        double len = Math.sqrt(sumsqrt);
        if (bar[0] != 0){
            return (len * bar[0])/bar[1];
        }
        return len;
    }

    // recalculate all values using bar
    public void reCalculateAll(){
        float multy = 1;
        if (localBarIndex != barIndexMuliply){

            this.shortArmLengthUnit /=localBarIndex;
            this.shortArmLengthUnit *=barIndexMuliply;

            this.longArmLengthUnit /=localBarIndex;
            this.longArmLengthUnit *=barIndexMuliply;

            this.unitLength /=localBarIndex;
            this.unitLength *=barIndexMuliply;

            if (calculatedMeta.size()!=0){

                for (ArrayList<Object> each:calculatedMeta){

                    float v1 = (float) each.get(1)/localBarIndex;
                    v1 *= barIndexMuliply;
                    each.set(1,v1);

                    float v2 = (float) each.get(2)/localBarIndex;
                    v2 *= barIndexMuliply;
                    each.set(2,v2);
                }
            }

            localBarIndex=barIndexMuliply;
        }

    }


}
