import javax.swing.*;
import java.util.ArrayList;

/**
 * Store all plots which were saved
 */
public class PlotStorage {

    static ArrayList<String> namePlots = new ArrayList<>();
    static ArrayList<DataReader> drPlots = new ArrayList<>();

    static void addPlotToStorage(DataReader dr, String Plotname){
        if (!(namePlots.contains(Plotname))){
            namePlots.add(Plotname);
            drPlots.add(dr);
            Main.currentPlotCounter = drPlots.size() - 1;
            //JOptionPane.showMessageDialog(Main.mainWindow,"The plot " + Plotname + " has been saved. \nThe number of plots in the database is " + drPlots.size());
        } else {
            JOptionPane.showMessageDialog(Main.mainWindow,"The plot with this name is already exist");
        }

    }


    private static void removePlotInd(int index){
        namePlots.remove(index);
        drPlots.remove(index);
    }

    static void removePlot(String name){
        int ind = namePlots.indexOf(name);
        removePlotInd(ind);
    }

    static DataReader getPreviousPlot(){
        if (Main.currentPlotCounter != 0)
        {
            Main.currentPlotCounter-=1;
            DataReader dr = drPlots.get(Main.currentPlotCounter);
            return dr;
        } else {
            JOptionPane.showMessageDialog(Main.mainWindow,"No plots anymore");
            return null;
        }


    }

    static DataReader getNextPlot(){
        if (Main.currentPlotCounter != drPlots.size() - 1)
        {
            Main.currentPlotCounter+=1;
            DataReader dr = drPlots.get(Main.currentPlotCounter);
            return dr;
        }else {

            JOptionPane.showMessageDialog(Main.mainWindow,"No plots anymore");
            return null;
        }

    }

    static DataReader getPlot(String name){
        int ind = namePlots.indexOf(name);
        return drPlots.get(ind);
    }

}
