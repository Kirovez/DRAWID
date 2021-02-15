import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.jar.Attributes;

/**
 * Class to read chromosome and metadata tables
 */

public class DataReader {
    String line;
    File table;
    String names[];
    // line counter
    byte lcnt;
    float maxArmL;
    float maxArmS;
    float Length;
    boolean drawBar = false;


    //temporary  storage for columns
    String[] columns;

    ContainerForIdeoStorage cont;
    XLReader xl;

    List<Float>     ChromLength = new ArrayList<>();

    // sorted chromosome names by length
    Map<String, Float> sortMap = new HashMap<String, Float>();

    // hashmap of centromere index and chromosome names
    Map<String, Float> CentMap = new TreeMap<String, Float>();

    ChromSumData ChromSum;


    // collect all chromosomes
    Map<String, ChromF> allChromosomes = new HashMap<String, ChromF>();

    HashMap<String,ArrayList<ArrayList<Object>>> metadataPerChromosome = new HashMap<>();


    public DataReader(ArrayList<MeasuredUnit> measUnits) {
        drawBar = false;
        ChromSum = new ChromSumData();
        lcnt = 0;
        maxArmL = 0;
        maxArmS = 0;
        Length = 0;

        for (MeasuredUnit each : measUnits) {
            each.reCalculateAll(); // will recalculate length and coordinates according to bar settings;
            Length = each.unitLength;

            ChromLength.add(Length);
            sortMap.put(each.nameUnit, Length);

            String Name = each.nameUnit;
            ChromSum.allChromNames.add(Name);

            float CI = each.unitCentromereIndex;

            CentMap.put(Name, CI);

            if (maxArmL < (Length * 100 - CI) / 100) {
                maxArmL = (Length * 100 - CI) / 100;
            }
            ;

            if (maxArmS < (Length * CI) / 100) {
                maxArmS = (Length * CI) / 100;
            }
            ;
            lcnt++;

            // add metadata table from metaunit to metadataPerChromosome hash map to then add it to ChromF;
            if (each.calculatedMeta != null){
                metadataPerChromosome.put(each.nameUnit, each.calculatedMeta);
            }
        }

        calculatioIndexes();
    }

    // Constructor for average

    public DataReader(HashMap<String, float[]> averageData){
        cont = new ContainerForIdeoStorage();
        drawBar = true;
        lcnt = 0;
        maxArmL = 0;
        maxArmS = 0;
        Length = 0;

        this.table = table;

        // reading and chromosome class assembling
        ChromSum = new ChromSumData();

       for(Map.Entry<String, float[]> chr : averageData.entrySet()) {

                Length = chr.getValue()[0];

                ChromLength.add(Length);

                String Name = chr.getKey();

                sortMap.put(Name, Length);

                ChromSum.allChromNames.add(Name);

                float CI = chr.getValue()[1];

                CentMap.put(Name, CI);

                if (maxArmL < (Length * 100 - CI) / 100) {
                    maxArmL = (Length * 100 - CI) / 100;
                }
                ;

                if (maxArmS < (Length * CI) / 100) {
                    maxArmS = (Length * CI) / 100;
                }

        }


        calculatioIndexes();

    }


    public DataReader(XLReader xl){
        cont = new ContainerForIdeoStorage();
        drawBar = false;
        ChromSum = new ChromSumData();

        if (xl.allChromosomes.size() != 0){
            drawBar = true;
            this.xl = xl;
        }

        for (Map.Entry<String,float[]> XLchr: xl.XLallChromosomes.entrySet()) {
                Length = XLchr.getValue()[0];

                ChromLength.add(Length);

                String Name = XLchr.getKey();

                sortMap.put(Name, Length);

                ChromSum.allChromNames.add(Name);

                float CI = XLchr.getValue()[1];

                if (CI > 50) {
                    CI = (Math.min((Length - CI), CI) * 100) / Length;
                }

                CentMap.put(Name, CI);

                if (maxArmL < (Length * 100 - CI) / 100) {
                    maxArmL = (Length * 100 - CI) / 100;
                }
                ;

                if (maxArmS < (Length * CI) / 100) {
                    maxArmS = (Length * CI) / 100;
                }
            }


        // add metadata table from metaunit to metadataPerChromosome hash map to then add it to ChromF;
        if (xl.XLmetadataPerChromosome != null){
            metadataPerChromosome = xl.XLmetadataPerChromosome;
        }

        calculatioIndexes();

    }

    public static float getMinValue(List<Float> numbers){
        float minValue = numbers.get(0);
        for(int i=1;i<numbers.size();i++){
            if(numbers.get(i) < minValue){
                minValue = numbers.get(i);
            }
        }
        return minValue;
    }
    //read metadata and build list of unique metanames and load it to ChromosomSum
    public void generateColors(){
        ArrayList<String> metaNames = new ArrayList<>();
        for (Map.Entry<String,ArrayList<ArrayList<Object>>> md : metadataPerChromosome.entrySet()){
            for (ArrayList<Object> met : md.getValue()){
                if (!(metaNames.contains(String.valueOf(met.get(0))))){
                    metaNames.add(String.valueOf(met.get(0)));
                }
            }

        }

        ChromSum.metaNames = metaNames;
        ChromSum.generateColors();
    }

    public void calculatioIndexes() {

        if (metadataPerChromosome.size() != 0){
            generateColors();
        }
        ChromSum.longestArmLength = 0;


        int centromerSize = 10;

        ChromSum.longestArmLength = maxArmL + maxArmS + centromerSize;


        ChromSum.longestSarm = maxArmS;
        ChromSum.longestLarm = maxArmL;

        ChromSum.ChromLength = ChromLength;

        ChromSum.getMultipleChromosomXY();

        // SD
        if (drawBar && xl != null){
            cont.createContainerfromXLreader(xl.allChromosomes, ChromSum.multypleY);
        }


        // indexes
        float chromWidth = ConstStrorage.CHROMOSOME_WITH * ChromSumData.multypleX;
        float chromDistance = ConstStrorage.CHROMOSOME_DISTANCE * ChromSumData.multypleX;


        float centromerePosY = (maxArmS * ChromSum.multypleY) + (2 * ChromF.dfX);


        byte cnt = 0;
        for (Map.Entry<String, Float> entry : entriesSortedByValues(sortMap)) {

            Length = entry.getValue() * ChromSum.multypleY;



            ChromF chrom = new ChromF(entry.getKey(),
                    CentMap.get(entry.getKey()),
                    Length,
                    cnt + 1,
                    ChromSum, centromerePosY, chromWidth, chromDistance, centromerSize, drawBar);


            if (drawBar){
                chrom.containerAverage = cont;
            }

            if (metadataPerChromosome.containsKey(entry.getKey())) {
                chrom.metaTable = new Metadata(ChromSum.multypleY, metadataPerChromosome.get(entry.getKey()), centromerSize);
                chrom.metaTable.colorForMetanames = ChromSum.colorForMetanames;
            }


            allChromosomes.put(entry.getKey(), chrom);
            cnt++;
        }
    }




    // overriding compare method
    static <K,V extends Comparable<? super V>>
    List<Map.Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Map.Entry<K,V>> sortedEntries = new ArrayList<Map.Entry<K,V>>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K,V>>() {
                    @Override
                    public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );

        return sortedEntries;
    }
}

