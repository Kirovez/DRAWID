import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Info about all chromosomes and adjusted indexes for chromosome and JPanel sizes
 */
public class ChromSumData {

    public ArrayList <String> allChromNames = new ArrayList<>();
    public List<Float> ChromLength = new ArrayList<>();

    static float chromFigureHeight = 0;

    public int numberMetaNames = 0; // number of unique metanames

    public ArrayList<String> metaNames;
    public Color[] colorpalette;

    public float longestLarm = 0;
    public float longestSarm = 0;

    public float longestArmLength = 0;

    // Index to multuplys chromosome length (Y) and width (X) and distance (X)
    // to adjust JPanel and Chromosome size
    static float multypleX;
    public float multypleY;

    public HashMap<String, Color> colorForMetanames;


    public void generateColors()
    {
        if (metaNames==null){
            metaNames = MeasuredUnit.metananes;
        }
        numberMetaNames += metaNames.size();


        // generate colors
        int n = numberMetaNames;
        Color[] cols = new Color[n];
        for(int i = 0; i < n; i++)
        {
            cols[i] = Color.getHSBColor((float) i / (float) n, 0.85f, 1.0f);
        }
        colorpalette = cols;

        // generate table with colors for each metaname
        colorForMetanames = new HashMap<>();
        for (int i = 0; i < metaNames.size(); i++){
            colorForMetanames.put(metaNames.get(i), colorpalette[i]);
        }
    }


    public void getMultipleChromosomXY(){

        multypleX = ConstStrorage.JPANELWIDTH / (((ChromLength.size() - 1) * (ConstStrorage.CHROMOSOME_DISTANCE + ConstStrorage.CHROMOSOME_WITH)) + (2 * ChromF.dfX) + (ConstStrorage.CHROMOSOME_WITH*2)); // change chromosome width value
        multypleY = ConstStrorage.JPANELHEIGHT/(longestArmLength + ChromF.dfX); // change chromosome height value

    }


}

