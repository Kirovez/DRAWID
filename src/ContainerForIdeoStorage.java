import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ContainerForIdeoStorage{
    float multipleY;

    //Means
    public HashMap<String, Float> meanLengthSValue = new HashMap<>();
    public HashMap<String, Float> meanLengthLValue = new HashMap<>();
    public HashMap<String, Float> meanCIValue = new HashMap<>();
    public HashMap<String, Float> meanLengthWhole= new HashMap<>();


    //SDs
    public HashMap<String, Float> SDLengthSValue = new HashMap<>();
    public HashMap<String, Float> SDLengthLValue = new HashMap<>();
    public HashMap<String, Float> SDCIValue = new HashMap<>();
    public HashMap<String, Float> SDLengthWhole = new HashMap<>();

    public void createContainerfromXLreader(HashMap<String, ArrayList<Float>> xlContainer, float multipleY){
        for (Map.Entry<String, ArrayList<Float>> chrom: xlContainer.entrySet()){
            String name = chrom.getKey();
            meanLengthSValue.put(name, chrom.getValue().get(4));
            meanLengthLValue.put(name, chrom.getValue().get(6));
            meanCIValue.put(name, chrom.getValue().get(0));
            meanLengthWhole.put(name, chrom.getValue().get(2));

            SDLengthSValue.put(name, chrom.getValue().get(5)*multipleY);
            SDLengthLValue.put(name, chrom.getValue().get(7)*multipleY);
            SDCIValue.put(name, chrom.getValue().get(1));
            SDLengthWhole.put(name, chrom.getValue().get(3)*multipleY);

        }
    }


    public ContainerForIdeoStorage (){
        this.meanLengthSValue.putAll(IdeoStrorage.meanLengthSValue);
        this.meanLengthLValue.putAll(IdeoStrorage.meanLengthLValue);
        this.meanCIValue.putAll(IdeoStrorage.meanCIValue);
        this.meanLengthWhole.putAll(IdeoStrorage.meanLengthWhole);

        this.SDLengthSValue.putAll(IdeoStrorage.SDLengthSValue);
        this.SDLengthLValue.putAll(IdeoStrorage.SDLengthLValue);
        this.SDCIValue.putAll(IdeoStrorage.SDCIValue);
        this.SDLengthWhole.putAll(IdeoStrorage.SDLengthWhole);

    }

    public ContainerForIdeoStorage(boolean XLreader){

    }
}

