import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ikirov on 4/11/2016.
 */
public class Reset {

    public Reset(){
        ChromF.allChromosomeColor = Color.GRAY;
        ChromF.dfX = ConstStrorage.DFX; // distance from X
        ChromF.dfY = ConstStrorage.DFY; // // distance from Y
        ChromF.FontNSize = ConstStrorage.FONTSIZE; //font size of the Chromosome name
        ChromF.resetChromF();

        ChromSumData.chromFigureHeight = 0;



        // Index to multuplys chromosome length (Y) and width (X) and distance (X)
        // to adjust JPanel and Chromosome size
        //ChromSumData.multypleX = Float.parseFloat(null);
        //ChromSumData. multypleY = Float.parseFloat(null);

//        ChromSumData.colorForMetanames = new HashMap<>();


        //Means
        IdeoStrorage.meanLengthSValue = new HashMap<>();
        IdeoStrorage.meanLengthLValue = new HashMap<>();
        IdeoStrorage.meanCIValue = new HashMap<>();

        //SDs
        IdeoStrorage.SDLengthSValue = new HashMap<>();
        IdeoStrorage.SDLengthLValue = new HashMap<>();
        IdeoStrorage.SDCIValue = new HashMap<>();

        Metadata.colorpalette = null;


        MeasuredUnit.bar = new int[2]; //[mkm, pxl]
        MeasuredUnit.linetoexclude = 0;

        MeasuredUnit.metananes = new ArrayList<>();

        MeasuredUnit.barIndexMuliply = 1;

        Plot.jpanelwidth = ConstStrorage.JPANELWIDTH;
        Plot.jpanelheight = ConstStrorage.JPANELHEIGHT;

    }
}
