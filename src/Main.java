import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Main class to run DRAWID
 */
public class Main {
    static int currentPlotCounter = 0;
    final JButton viewStorage;

    static JFrame mainWindow;

    Viewer chromMeasureViewer;
    String image;
    TranspWindow tw;

    boolean remBut = false;


    // main panel ;)
    final JPanel mainJpanel = new JPanel() {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 1200);
        };
    };

    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    shrtcutpictureviewer pic;
    JPanel buttonPanel;
    JPanel drawPanel;
    DataReader dr = null;

    DataTable DT;
    int plotCounter = 0;

    File ChromTable = null;
    File MetaTable = null;
    File SaveFile;
    Plot pl = null;
    String picture;

    UsefulWindowsGUI UWG = new UsefulWindowsGUI();

    GridBagConstraints gbc = new GridBagConstraints();
    GridBagConstraints gbc2 = new GridBagConstraints();

    IdeoStrorage ideoStrorage;
    JPanel al; // check box panel for average Ideo construction
    ArrayList<JCheckBox> cbox;


    public void showIdeo(){
        if (pl != null){
            drawPanel.remove(pl);
        }
        pl = new Plot(dr.allChromosomes,dr.ChromSum, tw);
        if (picture!=null){
           pl.picture = picture;
           pl.positionsonpicture = chromMeasureViewer.label.positionOnPicture;
           pl.pic = pic;
        }


        drawPanel.add(pl);
        drawPanel.revalidate();
        drawPanel.repaint();

    }


    public void calculaIdeoFromViewer(){
        if (dr != null){
            if (pl != null){
                drawPanel.remove(pl);
                pl = null;
            }
            ChromTable = null;
            dr.ChromSum = null;
            MetaTable = null;
            dr = null;
            mainJpanel.revalidate();
            mainJpanel.repaint();
        }

        dr = chromMeasureViewer.drMeasure;
        showIdeo();
    }

    public void viewer() throws IOException {
        picture = UsefulWindowsGUI.FileCh("Choose picture for measure","jpg","*.txt").getAbsolutePath();
        this.image = picture;
        chromMeasureViewer = new Viewer(picture, this);
    }


    // Custom button

    public JButton customButton(String startButton1, String startButtonHover1, String startButtonActive1) throws IOException {
        System.out.println(startButton1);
        BufferedImage startButton = ImageIO.read(getClass().getResource("/resources/buttons/" + startButton1));
        BufferedImage startButtonHover = ImageIO.read(getClass().getResource("/resources/buttons/" + startButtonHover1));
        BufferedImage startButtonActive = ImageIO.read(getClass().getResource("/resources/buttons/" + startButtonActive1));

        JButton retB = new JButton(new ImageIcon(startButton));
        retB.setCursor(new Cursor(Cursor.HAND_CURSOR));
        retB.setSize(50,50);
        retB.setRolloverIcon(new ImageIcon(startButtonHover));
        retB.setPressedIcon(new ImageIcon(startButtonActive));
        retB.setBorder(BorderFactory.createEmptyBorder());
        retB.setContentAreaFilled(false);
        retB.setFocusable(false);

        return retB;
    };




    public void LeftMove(){
        pl.changechromosomes(-1);
    }

    public void Move(int i){
        int ves = i*(-10);

        for (Map.Entry<String, ChromF> chr : pl.allChromosomes.entrySet()){
            chr.getValue().centromerePosY+=ves;
            chr.getValue().verticalMoveIdeo(ves);
        }
        pl.revalidate();
        pl.repaint();
    }

    public void RightMove(){
        pl.changechromosomes(1);
    }


    Action LeftMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            LeftMove();
        }
    };

    Action UPMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Move(1);
        }
    };

    Action DownMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Move(-1);
        }
    };

    Action RightMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            RightMove();
        }
    };


    public void addIdeoToStorage(){
        String picture_name = "";
        String plotName;
        if (picture!=null){
            picture_name = Paths.get(picture).getFileName().toString();
        }
        String pN;

        pN = JOptionPane.showInputDialog(pl, "Plot name", picture_name);


        if (pN != null){
            plotName = pN;

            if (ideoStrorage==null){
                ideoStrorage = new IdeoStrorage();
            }

            if (ideoStrorage.allPlots.containsKey(plotName)){
                int ans = JOptionPane.showConfirmDialog(pl, "The database already contains this plot. /n Do you want to overwrite it?","Warning!",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ans == JOptionPane.OK_OPTION){
                    ideoStrorage.addIdeo(dr.ChromSum.multypleY,plotName,pl);
                    PlotStorage.addPlotToStorage(dr, pN);

                }
            } else {
                ideoStrorage.addIdeo(dr.ChromSum.multypleY, plotName,pl);
                PlotStorage.addPlotToStorage(dr, pN);

            }
        }


    }

    // show check bos withh plots added to database
    public void plotCheckBoxDialog(){
        ArrayList<String> toRet = new ArrayList<>();
        al = new JPanel();
        cbox = new ArrayList<>();
        for(Map.Entry<String, HashMap<String, float[]>> Plots: ideoStrorage.allPlots.entrySet()){
            JCheckBox box = new JCheckBox(Plots.getKey());
            box.setSelected(true);
            al.add(box);
            cbox.add(box);
        }
            int Jo = JOptionPane.showConfirmDialog(null, al);
            if (Jo == JOptionPane.YES_OPTION){
                for(JCheckBox cbj: cbox){
                    if (cbj.isSelected()){
                        toRet.add(cbj.getText());
                    }
                }

                if (toRet.size()!=0){
                    // calculate means and SDs values for all ideos in ideoStorage
                    ideoStrorage.meanLengthAndCI(toRet);
                    // create new plot
                    if (dr != null) {
                        drawPanel.remove(pl);
                        MetaTable = null;
                        pl = null;
                        dr = null;
                        mainJpanel.revalidate();
                        mainJpanel.repaint();
                    }
                    dr = new DataReader(ideoStrorage.getDRhashMap());
                    showIdeo();

                }

            }

    }

    // Rename chromosomes according to the order
    public void RenameToOrder(){
        for(Map.Entry<String,ChromF> chr: dr.allChromosomes.entrySet()){
            chr.getValue().chromName = String.valueOf(chr.getValue().ChromNumberIndex);
            pl.repaint();
        }
    }

    public XLReader XLRead(){
        File XLfile = UsefulWindowsGUI.FileCh("Choose xls or xlxs DRAWID files", "xread", null);
        try {
            return new XLReader(XLfile);
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(pl,"No such file or directory!" + e1.getMessage());
        }
        return null;
    }

    // make indexes in dr.allChromosomes equal to the indexess of chromosomes
    public ArrayList<ChromF> adjustDRindexes(){
        ArrayList<ChromF> newAllChromosomes= new ArrayList<>();
        for (int i = 0; i < dr.allChromosomes.size(); i++){
            for (Map.Entry<String, ChromF> chr : dr.allChromosomes.entrySet()){
                if (chr.getValue().ChromNumberIndex == i+1){
                    newAllChromosomes.add(chr.getValue());
                }
            }
        }

        return newAllChromosomes;

    }

    // split current chromosomes set plot into several chromosome sets (number = ploidy) and
    // run ideoStorage calculation
    public void reduceKaryo(){
        ArrayList<String> namePlots = new ArrayList<>();
        IdeoStrorage IS = new IdeoStrorage();
        int ploidy = Integer.valueOf(JOptionPane.showInputDialog(mainWindow, "Input ploidy level, please!"));

        HashMap<String, ArrayList<ChromF>> spliChrStore = new HashMap<>();
        int shift = 0;
        int chromNumber = 1;
        ArrayList<ChromF> newChr = adjustDRindexes();
        for (int i = 0; i < newChr.size(); i++){
            shift+=1;
            if (shift > ploidy){
                shift = 1;
                chromNumber+=1;
            }
            newChr.get(i).chromName = String.valueOf(chromNumber);
            if (spliChrStore.containsKey(String.valueOf(shift))){
                ArrayList<ChromF> temp = spliChrStore.get(String.valueOf(shift));
                temp.add(newChr.get(i));
                spliChrStore.put(String.valueOf(shift), temp);
            }else {
                ArrayList<ChromF> temp = new ArrayList<>();
                temp.add(newChr.get(i));
                spliChrStore.put(String.valueOf(shift), temp);
            }
        }

        for(Map.Entry<String,ArrayList<ChromF>> entry: spliChrStore.entrySet()){
            ideoStrorage.addIdeo(dr.ChromSum.multypleY, entry.getKey(), entry.getValue());
            namePlots.add(entry.getKey());
        }
        if (dr != null) {
            drawPanel.remove(pl);
            MetaTable = null;
            pl = null;
            dr = null;
            mainJpanel.revalidate();
            mainJpanel.repaint();
        }
        ideoStrorage.meanLengthAndCI(namePlots);
        dr = new DataReader(ideoStrorage.getDRhashMap());
        showIdeo();

    }

    public void plotMan(){
        PlotStorageManager plStor= new PlotStorageManager(mainWindow, PlotStorage.namePlots, this);
    }



    public Main() throws IOException {

        // main Window
        JFrame.setDefaultLookAndFeelDecorated(true);
        mainWindow = new JFrame();
        ideoStrorage = new IdeoStrorage();

        mainWindow.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }

        });

        mainWindow.setTitle("DRAWID");
        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainWindow.setLayout(new BorderLayout());



        // main panel ;)
        final JPanel mainJpanel = new JPanel();
        mainJpanel.setLayout(new BorderLayout());

        final GridBagConstraints gbc = new GridBagConstraints();


        // Build Button panel
        buttonPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(500, 80);
            }

            ;
        };
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setLayout(new GridBagLayout());


        //RUN Button
        Cursor hcur = new Cursor(Cursor.HAND_CURSOR);

        gbc.insets = new Insets(2, 2, 2, 2); // add spaces between buttons
        gbc.gridy = 0; // position y
        gbc.gridwidth = 1; // how many cells button occupies;
        gbc.fill = GridBagConstraints.HORIZONTAL; // stretch the button to adjust grindx;
        //**************************************//



        JButton Browse =customButton("Browse1.png", "Browse2.png", "Browse3.png");
        Browse.setToolTipText("Choose table for ideogram drawing");
        Browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //File f = UWG.FileCh("Choose chromosome table", "txt", "*.txt"); //call File browser
                //ChromTable = f;

                if (dr != null) {
                    drawPanel.remove(pl);
                    MetaTable = null;
                    pl = null;
                    dr = null;
                    mainJpanel.revalidate();
                    mainJpanel.repaint();
                }
                dr = new DataReader(XLRead());
                showIdeo();


            }

        });
        gbc.gridx = 0; // position x
        buttonPanel.add(Browse, gbc);



        JButton ChColorB =customButton("ChC1.PNG", "ChC2.PNG", "ChC3.PNG");
        ChColorB.setToolTipText("Change chromosome color");
        ChColorB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (pl != null) {
                    Color ChcolorAll = JColorChooser.showDialog(null,
                            "Choose chromosome color", ChromF.allChromosomeColor);
                    if (ChcolorAll != null && pl != null) {
                        ChromF.allChromosomeColor = ChcolorAll;
                        pl.changecolorAllchromosomes(ChcolorAll);
                    }
                }
            }

        });
        gbc.gridx = 1; // position x
        buttonPanel.add(ChColorB, gbc);


        //**************************************//


        //Centromere color button

        JButton CenColorB =customButton("CenCol1.png", "CenCol2.png", "CenCol3.png");
        CenColorB.setToolTipText("Change centromer color for all chromosomes");
        CenColorB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (pl != null) {
                    Color CencolorAll = JColorChooser.showDialog(null,
                            "Choose centromere color", ChromF.allChromosomeColor);
                    if (CencolorAll != null && pl != null) {
                        ChromF.allChromosomeColor = CencolorAll;
                        pl.changecolorAllcentromere(CencolorAll);
                    }
                }

            }
        });
        gbc.gridx = 2; // position x
        buttonPanel.add(CenColorB, gbc);

        //**************************************//



        // Measure button

        JButton Measure = customButton("Measure1.png", "Measure2.png", "Measure3.png");
        Measure.setToolTipText("Select picture and do measurements");
        Measure.setCursor(hcur);

        Measure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    viewer();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        gbc.gridx = 3; // position x
        buttonPanel.add(Measure, gbc);


        // RENAME ACCORDING TO THE POSITION
        JButton RenamPos = customButton("ReName1.png","ReName2.png","ReName3.png");
        RenamPos.setToolTipText("Rename chromosomes according to their order");
        RenamPos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RenameToOrder();
            }
        });
        gbc.gridx = 4; // position x
        buttonPanel.add(RenamPos,gbc);


        // ADD IDEO button
        gbc.gridx = 5; // position x
        JButton addIdeoButton = customButton("AddIdeo.png", "AddIdeo2.png", "AddIdeo2.png");
        addIdeoButton.setToolTipText("Add ideogram to database");
        addIdeoButton.setCursor(hcur);
        addIdeoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addIdeoToStorage();
                viewStorage.setText(String.valueOf(PlotStorage.drPlots.size()));
            }
        });

        buttonPanel.add(addIdeoButton, gbc);

        // CALCULATE AVERAGE CHROMOSOME
        gbc.gridx = 6;
        JButton averageIdeo = customButton("AverageIdeo1.png", "AverageIdeo2.png","AverageIdeo3.png" );
        averageIdeo.setToolTipText("Build plot of average chromosomes");
        averageIdeo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plotCheckBoxDialog();
            }
        });
        buttonPanel.add(averageIdeo,gbc);


        //ReadXL
        JButton MetaColor = customButton("Meta1.png","Meta2.png","Meta2.png");
        MetaColor.setToolTipText("Change signal color");
        MetaColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              MetaColorAndName mcan = new MetaColorAndName(pl);
            }
        });

        gbc.gridx = 7;
        buttonPanel.add(MetaColor,gbc);

        JButton reduceKaryo = customButton("reduceKaryo1.png","reduceKaryo2.png","reduceKaryo2.png");
        reduceKaryo.setToolTipText("Build 1n ideogram");
        reduceKaryo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reduceKaryo();
            }
        });

        gbc.gridx = 8;
        buttonPanel.add(reduceKaryo, gbc);


        gbc.gridx = 9;
        final JButton previousPlotB = customButton("previousPlot1.png","previousPlot2.png","previousPlot2.png");
        previousPlotB.setToolTipText("Previous plot");
        previousPlotB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataReader drfromPlot = PlotStorage.getPreviousPlot();
                if (drfromPlot != null){
                    dr = drfromPlot;
                    showIdeo();
                }
            }
        });
        buttonPanel.add(previousPlotB, gbc);

        gbc.gridx = 10;
        final JButton nextPlotB = customButton("nextPlot1.png","nextPlot2.png","nextPlot2.png");
        previousPlotB.setToolTipText("Next plot");
        nextPlotB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataReader drfromPlot = PlotStorage.getNextPlot();
                if (drfromPlot != null){
                    dr = drfromPlot;
                    showIdeo();
                }
            }
        });
        buttonPanel.add(nextPlotB, gbc);

        viewStorage = customButton("ViewStorage1.png", "ViewStorage2.png","ViewStorage2.png");
        viewStorage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStorage.setText(String.valueOf(PlotStorage.drPlots.size()));
                plotMan();
            }
        });
        viewStorage.setText(String.valueOf(PlotStorage.drPlots.size()));

        gbc.gridx = 11;
        buttonPanel.add(viewStorage, gbc);

        mainJpanel.add(buttonPanel, BorderLayout.NORTH);


        //**************************************//

        // Build Draw Panel
        drawPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(Plot.jpanelheight, Plot.jpanelwidth);
            }
        };

        drawPanel.setBorder(BorderFactory.createTitledBorder("Ideogram drawing"));
        drawPanel.setLayout(new GridLayout());
        drawPanel.setBackground(Color.GRAY);
        mainJpanel.add(drawPanel, BorderLayout.CENTER);
        mainWindow.add(mainJpanel);


        //*****************************************//


        //Chromosome view panel

        JPanel RightPanel = new JPanel();
        RightPanel.setBackground(Color.GRAY);

        RightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.HORIZONTAL; // stretch the button to adjust grindx;
        gbc2.insets = new Insets(2, 2, 2, 2); // add spaces between buttons
        gbc.gridwidth = 1; // how many cells button occupies;
        gbc2.gridx = 0; // position



        gbc2.gridy = 0; // position y
        this.tw = new TranspWindow();
        this.tw.setBackground(Color.GRAY);
        RightPanel.add(tw, gbc2);

        gbc2.gridy = 1; // position y
        pic = new shrtcutpictureviewer();
        pic.setBorder( BorderFactory.createTitledBorder("Chromosome view"));
        pic.setBackground(Color.GRAY);
        RightPanel.add(pic, gbc2);
        RightPanel.setFocusable(false);
        mainJpanel.add(RightPanel, BorderLayout.EAST);


        //*****************************************//




        // JMenubar
        JMenuBar jMenuBar = new JMenuBar();
        mainWindow.setJMenuBar(jMenuBar);

        JMenu file = new JMenu("File");

        JMenuItem Exit = new JMenuItem("Start new project");
        Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dr != null) {
                    drawPanel.remove(pl);
                    ChromTable = null;
                    MetaTable = null;
                    pl = null;
                    dr = null;
                    mainJpanel.revalidate();
                    mainJpanel.repaint();
                    ideoStrorage=null;
                    Reset res = new Reset();
                }

            }
        });


        JMenuItem SaveFig = new JMenuItem("Save Figure");

        SaveFig.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                pl.SaveFig();
            }
        });

        file.add(SaveFig);

        file.add(Exit);

        jMenuBar.add(file);


        JMenu settings = new JMenu("Settings");
        final JMenuItem ChromosomeSettings = new JMenuItem(new AbstractAction("Chromosome parametres") {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean meta = false;
                for (Map.Entry<String, ChromF> chr : pl.allChromosomes.entrySet()){
                    if (chr.getValue().metaTable!=null){
                        JOptionPane.showMessageDialog(mainWindow, "Sorry!The chromosome settings are not available when metadata exists");
                        meta = true;
                        break;
                    }
                }
                if (!(meta)){
                    Settings settings = new Settings(mainWindow, dr);
                }

            }
        });

        settings.add(ChromosomeSettings);

        final JMenuItem statSettings = new JMenuItem(new AbstractAction("Stat graphs") {
            @Override
            public void actionPerformed(ActionEvent e) {
                statGraph stGr = new statGraph(mainWindow, dr);
            }
        });

        settings.add(statSettings);

        jMenuBar.add(settings);


        JMenu data = new JMenu("Data");

        final JMenuItem ChromTab = new JMenuItem((new AbstractAction("Show Chromosome table") {

            public void actionPerformed(ActionEvent e) {
                if (pl == null){
                    JOptionPane.showMessageDialog(null, "Table file has not been selected");
                }else {
                    DT = new DataTable(dr.allChromosomes, dr.ChromSum.multypleY);
                    DT.createUI("Karyo");
                }
            }
        }));

        data.add(ChromTab);


        JMenuItem MetaTab = new JMenuItem((new AbstractAction("Show metadata table") {
            public void actionPerformed(ActionEvent e) {
                if (pl == null){
                    JOptionPane.showMessageDialog(null, "Table file has not been selected");
                }else {
                    DT = new DataTable(dr.allChromosomes, dr.ChromSum.multypleY);
                    DT.createUI("Meta");

                }
            }
        }));
        data.add(MetaTab);

        JMenuItem PlotManager = new JMenuItem(new AbstractAction("Plot manager") {
            @Override
            public void actionPerformed(ActionEvent e) {
                plotMan();
            }
        });

        data.add(PlotManager);
        jMenuBar.add(data);


        /////////////////////CHROMOSOME MOVE/////////////////////

        mainJpanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"), "Left");
        mainJpanel.getActionMap().put("Left", LeftMove);

        mainJpanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("RIGHT"), "Right");
        mainJpanel.getActionMap().put("Right", RightMove);

        mainJpanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("UP"), "UP");
        mainJpanel.getActionMap().put("UP", UPMove);

        mainJpanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("DOWN"), "DOWN");
        mainJpanel.getActionMap().put("DOWN", DownMove);



    }


    public static void main(String[] args) throws IOException {
        Main MN = new Main();
        Splash SS = new Splash(Main.mainWindow, 2000);
        Main.mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Main.mainWindow.setBounds(100, 100, (int) dim.getWidth(), (int) dim.getHeight());
        Main.mainWindow.setLocationRelativeTo(null);
        Main.mainWindow.setResizable(true);
        Main.mainWindow.setVisible(true);

    }

}

