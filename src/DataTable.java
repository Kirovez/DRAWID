import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ikirov on 25/10/2016.
 */
public class DataTable {

    HashMap<String, Object[]> karyo = new HashMap<>();
    HashMap<String, ArrayList<ArrayList<Object>>> meta  = new HashMap<>();
    String filename;
    Map<String, ChromF> allChromosomes;
    float multipleY;
    Object[][] dataKar;
    Object[][] dataMet;

    JTable table; // JTable to show

    JTable tableKaryo;

    JTable tableMeta;

    boolean average = false;



    //Chromosome table

    public DataTable(Map<String, ChromF> allChromosomes, float multipleY){


        int metavalues = 0;

        for (Map.Entry<String, ChromF> entry: allChromosomes.entrySet()) {
            if (entry.getValue().metaTable != null){
                metavalues+=entry.getValue().metaTable.metadataTable.size();
            }
        }

        this.allChromosomes = allChromosomes;
        this.multipleY = multipleY;
        dataKar = new Object[allChromosomes.size()][];

        if (metavalues!=0){
            dataMet = new Object[metavalues][4];
        }


        int cnt=0;

        // Creating dataMet and dataKar matrixes for JTable and
        // hashtable for XLWriters: meta and Karyo
        int cm = 0;


        for (Map.Entry<String, ChromF> entry: allChromosomes.entrySet()) {
            if (entry.getValue().drawBar){
                average = true;
                dataKar[cnt] = new Object[]{entry.getValue().chromName,
                        entry.getValue().centromereIndex, entry.getValue().containerAverage.SDCIValue.get(entry.getKey()),
                        entry.getValue().Length/multipleY, entry.getValue().containerAverage.SDLengthWhole.get(entry.getKey())/multipleY,
                        entry.getValue().shortArmLength/multipleY, entry.getValue().containerAverage.SDLengthSValue.get(entry.getKey())/multipleY,
                        entry.getValue().longArmLength/multipleY, entry.getValue().containerAverage.SDLengthLValue.get(entry.getKey())/multipleY
                };
                karyo.put(entry.getValue().chromName,dataKar[cnt]);
            }else {
                dataKar[cnt] = new Object[]{entry.getValue().chromName, entry.getValue().centromereIndex, entry.getValue().Length/multipleY};
                karyo.put(entry.getValue().chromName,dataKar[cnt]);
            }


            if (entry.getValue().metaTable != null){
                meta.put(entry.getValue().chromName, entry.getValue().metaTable.metadataTable);
                for (ArrayList<Object> obj: entry.getValue().metaTable.metadataTable){
                    dataMet[cm] = new Object[]{entry.getValue().chromName, obj.get(0), (float) obj.get(1), (float) obj.get(2)};
                    cm++;
                }
            }
            cnt++;
        };

        String[] columnNamesAv = {"Chromosome name",
                "Centromere Index","SD",
                "Chromosome length", "SD"};
        String[] columnNamesNormal = {"Chromosome name",
                "Centromere Index", "Chromosome length"};

        String[] columnNames;

        if (average){
            columnNames = columnNamesAv;
        } else {
            columnNames = columnNamesNormal;
        }




        String[] metaheader = {"Chromosome name", "MetaName", "From", "To"};


        tableKaryo = new JTable(dataKar, columnNames);
        tableMeta = new JTable(dataMet, metaheader);

    }


    public void XLcall() throws IOException {
        File f = UsefulWindowsGUI.FileCh("Choose directory for saving", "xlsx", filename);
        XLWriter xl = new XLWriter(karyo,meta,f.getAbsolutePath(), average);
    }

    public void createUI(String tabname) {
        final JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        JPanel btnPnl = new JPanel(new BorderLayout());

        JPanel bottombtnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));


        JButton btnSave = new JButton("Save as xlsx");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    XLcall();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        bottombtnPnl.add(btnSave);
        JButton can = new JButton("Cancel");
        can.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
        bottombtnPnl.add(can);


        btnPnl.add(bottombtnPnl, BorderLayout.CENTER);

        if (tabname.equals("Meta")){
            table = tableMeta;
        }else {
            table = tableKaryo;
        }

        table.getTableHeader().setReorderingAllowed(false);

        frame.add(table.getTableHeader(), BorderLayout.NORTH);
        frame.add(table, BorderLayout.CENTER);
        frame.add(btnPnl, BorderLayout.SOUTH);

        frame.setTitle("Data table");
        frame.pack();
        frame.setVisible(true);
    }

}
