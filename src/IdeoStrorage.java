import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to store all ideograms
 */
public class IdeoStrorage {


    HashMap<String, HashMap<String, float[]>> allPlots = new HashMap<>(); // plot name: Chromosome name: [Length, CI]
    HashMap<String, Float> sumLengthS = new HashMap<>();
    HashMap<String, Float> sumLengthL = new HashMap<>();
    HashMap<String, Float> sumCI = new HashMap<>();
    HashMap<String, Float> sumLengthWhole = new HashMap<>();

    HashMap<String, Integer> chromosomeCount = new HashMap<>();

    //Means
    static HashMap<String, Float> meanLengthSValue = new HashMap<>();
    static HashMap<String, Float> meanLengthLValue = new HashMap<>();
    static HashMap<String, Float> meanCIValue = new HashMap<>();
    static HashMap<String, Float> meanLengthWhole= new HashMap<>();


    //SDs
    static HashMap<String, Float> SDLengthSValue = new HashMap<>();
    static HashMap<String, Float> SDLengthLValue = new HashMap<>();
    static HashMap<String, Float> SDCIValue = new HashMap<>();
    static HashMap<String, Float> SDLengthWhole = new HashMap<>();




    ArrayList<String> listPlots;
    private float multipleY = 1;
    private float zoom = 1;

    // iterate through all chromosomes on the plot and calculate sum of length and CI
    // followed by mean Length and CI calculation
    public void meanLengthAndCI(ArrayList<String> listPlots) {

        //SDs
        SDLengthSValue = new HashMap<>();
        SDLengthLValue = new HashMap<>();
        SDCIValue = new HashMap<>();
        SDLengthWhole = new HashMap<>();


        this.listPlots = listPlots;
        sumLengthS = new HashMap<>();
        sumLengthL = new HashMap<>();
        sumLengthWhole = new HashMap<>();

        sumCI = new HashMap<>();

        chromosomeCount = new HashMap<>();

        meanLengthSValue = new HashMap<>();
        meanLengthLValue = new HashMap<>();
        meanCIValue = new HashMap<>();
        meanLengthWhole = new HashMap<>();

        // iteration through all plots
        for (Map.Entry<String, HashMap<String, float[]>> plot : allPlots.entrySet()) {
            // iteration through all chromosomes on the plot
            if (listPlots.contains(plot.getKey())){
                for (Map.Entry<String, float[]> chrom : plot.getValue().entrySet()) {

                    float temp = 0;
                    float SL = (chrom.getValue()[0]*chrom.getValue()[1])/100; //short Arm Length
                    float LL = (chrom.getValue()[0]*(100-chrom.getValue()[1]))/100; // long Arm Length
                    float Length = chrom.getValue()[0];

                    if (sumLengthL.containsKey(chrom.getKey())) {
                        //length
                        temp = SL + sumLengthS.get(chrom.getKey());
                        sumLengthS.remove(chrom.getKey());
                        sumLengthS.put(chrom.getKey(), temp);

                        temp = LL + sumLengthL.get(chrom.getKey());
                        sumLengthL.remove(chrom.getKey());
                        sumLengthL.put(chrom.getKey(), temp);

                        //CI
                        temp = chrom.getValue()[1] + sumCI.get(chrom.getKey());
                        sumCI.remove(chrom.getKey());
                        sumCI.put(chrom.getKey(), temp);

                        //Total length
                        temp =  Length + sumLengthWhole.get(chrom.getKey());
                        sumLengthWhole.remove(chrom.getKey());
                        sumLengthWhole.put(chrom.getKey(), temp);

                        //chromosome count
                        int c = chromosomeCount.get(chrom.getKey());
                        c += 1;
                        chromosomeCount.remove(chrom.getKey());
                        chromosomeCount.put(chrom.getKey(), c);


                    } else {
                        sumLengthS.put(chrom.getKey(), SL);
                        sumLengthL.put(chrom.getKey(), LL);
                        sumLengthWhole.put(chrom.getKey(), Length);
                        sumCI.put(chrom.getKey(), chrom.getValue()[1]);
                        chromosomeCount.put(chrom.getKey(), 1);
                    }

                }

            }

        }
        meanLength();
    }

    // add plot to the database
    public void addIdeo(float multypleY, String namePlot, Plot plot) {
        if (allPlots.size() == 0){
            multipleY = multypleY;
        }
        HashMap<String, float[]> hm = new HashMap<>();
        for (Map.Entry<String, ChromF> chr : plot.allChromosomes.entrySet()) {
            float[] LCI = {chr.getValue().Length/multypleY, chr.getValue().centromereIndex};
            hm.put(chr.getValue().chromName, LCI);
        }
        allPlots.put(namePlot, hm);
    }

    //add plot from reduce karyo
    public void addIdeo(float multypleY, String namePlot, ArrayList<ChromF> allchromosomes){
        if (allPlots.size() == 0){
            multipleY = multypleY;
        }
        HashMap<String, float[]> hm = new HashMap<>();
        for (int i = 0; i< allchromosomes.size();i++) {
            float[] LCI = {allchromosomes.get(i).Length/multypleY, allchromosomes.get(i).centromereIndex};
            hm.put(allchromosomes.get(i).chromName, LCI);
        }
        allPlots.put(namePlot, hm);

    }




    // calculate mean length and CI per chromosome
    public void meanLength() {
        for (Map.Entry<String, Float> m : sumLengthL.entrySet()) {

            meanLengthLValue.put(m.getKey(), m.getValue() / chromosomeCount.get(m.getKey()));
            meanLengthSValue.put(m.getKey(), sumLengthS.get(m.getKey()) / chromosomeCount.get(m.getKey()));
            meanLengthWhole.put(m.getKey(), sumLengthWhole.get(m.getKey()) / chromosomeCount.get(m.getKey()));
            meanCIValue.put(m.getKey(), sumCI.get(m.getKey()) / chromosomeCount.get(m.getKey()));
        }

        SD_calculation();

    }

    public HashMap<String, float[]> getDRhashMap(){
        HashMap<String, float[]> forDR = new HashMap<>();
        for (Map.Entry<String, Float> mean : meanLengthWhole.entrySet()){
            float[] fl = {mean.getValue(), meanCIValue.get(mean.getKey())};
            forDR.put(mean.getKey(), fl);
        }

        return forDR;
    }

    public void SD_calculation() {
        HashMap<String, Float> sumSD_S = new HashMap<>();
        HashMap<String, Float> sumSD_L =  new HashMap<>();
        HashMap<String, Float> sumSD_CI =  new HashMap<>();
        HashMap<String, Float> sumSD_total = new HashMap<>();

        // iteration through all plots
        for (Map.Entry<String, HashMap<String, float[]>> chr : allPlots.entrySet()) {
            // iteration through all chromosomes on the plot
            if (listPlots.contains(chr.getKey())) {
                for (Map.Entry<String, float[]> chrom : chr.getValue().entrySet()) {
                    if (sumSD_CI.containsKey(chrom.getKey())){

                        // CI
                        float meandev = (float) Math.pow(chrom.getValue()[1] - meanCIValue.get(chrom.getKey()),2);
                        sumSD_CI.put(chrom.getKey(), sumSD_CI.get(chrom.getKey()) + meandev);
                        //S
                        meandev = (float) Math.pow(((chrom.getValue()[0]*chrom.getValue()[1])/100) - meanLengthSValue.get(chrom.getKey()),2);
                        sumSD_S.put(chrom.getKey(), sumSD_S.get(chrom.getKey()) + meandev);
                        //L
                        meandev = (float) Math.pow(((chrom.getValue()[0]*(100 - chrom.getValue()[1]))/100) - meanLengthLValue.get(chrom.getKey()),2);
                        sumSD_L.put(chrom.getKey(), sumSD_L.get(chrom.getKey()) + meandev);
                        //LengthTotal
                        meandev = (float) Math.pow(chrom.getValue()[0] - meanLengthWhole.get(chrom.getKey()),2);
                        sumSD_total.put(chrom.getKey(), sumSD_total.get(chrom.getKey()) + meandev);


                    }else {

                        float meandev = (float) Math.pow(chrom.getValue()[1] - meanCIValue.get(chrom.getKey()),2);
                        sumSD_CI.put(chrom.getKey(), meandev);
                        //S
                        meandev = (float) Math.pow(((chrom.getValue()[0]*chrom.getValue()[1])/100) - meanLengthSValue.get(chrom.getKey()),2);
                        sumSD_S.put(chrom.getKey(), meandev);
                        //L
                        meandev = (float) Math.pow(((chrom.getValue()[0]*(100 - chrom.getValue()[1]))/100) - meanLengthLValue.get(chrom.getKey()),2);
                        sumSD_L.put(chrom.getKey(), meandev);
                        //Total
                        meandev = (float) Math.pow(chrom.getValue()[0] - meanLengthWhole.get(chrom.getKey()),2);
                        sumSD_total.put(chrom.getKey(), meandev);

                    }
                }
            }
        }

        // SD calculation
        for (Map.Entry<String, Float> chrom: sumSD_CI.entrySet()){

            float SD_CI = (float) Math.sqrt(chrom.getValue()/chromosomeCount.get(chrom.getKey()));
            SDCIValue.put(chrom.getKey(), SD_CI);

            float SD_S = (float) Math.sqrt(sumSD_S.get(chrom.getKey())/chromosomeCount.get(chrom.getKey()));
            SDLengthSValue.put(chrom.getKey(), SD_S*multipleY);

            float SD_L = (float) Math.sqrt(sumSD_L.get(chrom.getKey())/chromosomeCount.get(chrom.getKey()));
            SDLengthLValue.put(chrom.getKey(), SD_L*multipleY);

            float SD_Total = (float) Math.sqrt(sumSD_total.get(chrom.getKey())/chromosomeCount.get(chrom.getKey()));
            SDLengthWhole.put(chrom.getKey(), SD_Total*multipleY);



        }
//
//        //System.out.println(SDCIValue);
//        System.out.println("meanLength" + meanLengthSValue);
//        System.out.println("SDLengthS" + SDLengthSValue);
//        System.out.println("CISD:" + SDCIValue);

    }



}
