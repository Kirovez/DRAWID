import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ikirov on 27/10/2016.
 */
public class joinerMeasureUnit {

    MeasuredUnit m1;
    MeasuredUnit m2;

    MeasuredUnit joinedClass;
    String nameUnit;
    ArrayList<Integer> Xcoordinates;
    ArrayList<Integer> Ycoordinates;
    ArrayList<Color> dotColorUnit;
    ArrayList<Color> lineColorUnit;

    int centromeredotindex;

    HashMap<Integer, String> metUnit = new HashMap<>();

    public joinerMeasureUnit(MeasuredUnit m1, MeasuredUnit m2){
        this.m1 = m1;
        this.m2 = m2;
        ArrayList<Integer> Xcoordinates = joiner(m1.Xcoordinates, m2.Xcoordinates);
        ArrayList<Integer> Ycoordinates = joiner(m1.Ycoordinates, m2.Ycoordinates);
        ArrayList<Color> dotColorUnit = joinerColor(m1.dotColorUnit, m2.dotColorUnit, false);
        ArrayList<Color> lineColorUnit = joinerColor(m1.lineColorUnit, m2.lineColorUnit, true);

        this.nameUnit = m1.nameUnit;
        this.centromeredotindex = m1.centromeredotindex;

        joinmet();

        MeasuredUnit.linetoexclude = m1.Xcoordinates.size();

        joinedClass = new MeasuredUnit(nameUnit, Xcoordinates, Ycoordinates,
                dotColorUnit, lineColorUnit, centromeredotindex,
        this.metUnit);

        //MeasuredUnit.linetoexclude = 0;



    }

    public ArrayList<Integer> joiner(ArrayList<Integer> arr1, ArrayList<Integer> arr2){

        ArrayList<Integer> joined = new ArrayList<>();

        for (Integer each: arr1){
            joined.add(each);
        }

        for (Integer each: arr2){
            joined.add(each);
        }

        return joined;
    }


    public ArrayList<Color> joinerColor(ArrayList<Color> arr1, ArrayList<Color> arr2, boolean line){

        ArrayList<Color> joined = new ArrayList<>();

        for (Color each: arr1){
            joined.add(each);
        }
        if (line){
            joined.add(Color.WHITE);
        }

        for (Color each: arr2){
            joined.add(each);
        }

        return joined;
    }

    public void joinmet(){
        for(Map.Entry<Integer,String> each: m1.metUnit.entrySet()){
            metUnit.put(each.getKey(),each.getValue());
        }

        for(Map.Entry<Integer,String> each: m2.metUnit.entrySet()){
            metUnit.put(each.getKey(),each.getValue());
        }
    }






}
