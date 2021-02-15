import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Plot
 */
public class Plot extends JPanel{

    int rescaling = 1;

    Map<String, ChromF> allChromosomes;
    String pictureusedformeasure;

    ChromSumData chromSum;


    ArrayList<Integer> highlightedChromosomes = new ArrayList<>();

    static int jpanelwidth = ConstStrorage.JPANELWIDTH;
    static int jpanelheight = ConstStrorage.JPANELHEIGHT;



    String selChromosomes = null;

    PopUpDemo menu = new PopUpDemo();

    TranspWindow tw;

    private void repaintPanel(){
        revalidate();
        repaint();
    }

    // takes x from mouse click and return chromosome name which overlaps with x
    // otherwise return -1
    public String overLappeCoordinates(int X) {

        for (Map.Entry<String, ChromF> entry: allChromosomes.entrySet()) {
            if (X > entry.getValue().centromerePosX && X < entry.getValue().centromerePosX + entry.getValue().chromWidth) {
                return entry.getKey();
            }
        };

        return null;
    }

    public void chromosomeHighlighting(int X, int Y) throws IOException {
        requestFocusInWindow();
        repaintPanel();
        String res = overLappeCoordinates(X);
        if (res != null){
            if (!(res.equals(selChromosomes))) {
                selChromosomes = res; // select chromosome
                String info = allChromosomes.get(res).getInfo();
                repaintPanel();
                transparentWindowInfo(info);


            } else {
                selChromosomes = null;
                if (tw != null){

                }

                repaintPanel();
            }
        } else if (selChromosomes!=null) {
            selChromosomes = null;
            repaintPanel();
        }
    }

    private void doPop(MouseEvent e){
        this.menu.show(e.getComponent(), e.getX(), e.getY());
    }

    public void changechromosomes(int i){

        if (selChromosomes != null & allChromosomes.get(selChromosomes).ChromNumberIndex + i !=0 & allChromosomes.get(selChromosomes).ChromNumberIndex + i <= allChromosomes.size()) {

            for (Map.Entry<String, ChromF> eachChrom : allChromosomes.entrySet()) {
                if (eachChrom.getValue().ChromNumberIndex == allChromosomes.get(selChromosomes).ChromNumberIndex  + i) {
                    eachChrom.getValue().ChromNumberIndex -= i;
                    eachChrom.getValue().RecalculatePositionX();
                }
            }
            allChromosomes.get(selChromosomes).ChromNumberIndex += i;
            allChromosomes.get(selChromosomes).RecalculatePositionX();
            repaintPanel();

        }


    }

    public String picture;
    public HashMap<String, int[]> positionsonpicture;
    shrtcutpictureviewer pic;

    /////////////// CONSTRUCTOR /////////////

    public Plot(final Map<String, ChromF> allChromosomes, ChromSumData chromSum, TranspWindow tw) {
        this.tw = tw;
        this.chromSum = chromSum;
        jpanelwidth = ConstStrorage.JPANELWIDTH;
        jpanelheight = ConstStrorage.JPANELHEIGHT;
        this.pic = pic;

        this.pictureusedformeasure = picture;

        this.allChromosomes = allChromosomes;

        setSize(jpanelheight, jpanelwidth);
        setBackground(ConstStrorage.BACKGROUND_PLOTCOLOR);
        setLayout(new GridLayout());
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {

                    try {
                        chromosomeHighlighting(e.getX(), e.getY());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }


                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (selChromosomes != null){
                            doPop(e);
                            }
                        }
                    }

            @Override
            public void mouseReleased(MouseEvent e) {

            }
         }
      );



        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                changeCursor(e.getX());
            }
        });



    }


    private void changeCursor(int X){
        if (overLappeCoordinates(X) != null){
            Cursor cur = new Cursor(Cursor.HAND_CURSOR);
            setCursor(cur);
        }else {
            Cursor cur = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(cur);
        }
    }

    //////////////////////////////////////////////////////////

    @Override
    protected void paintComponent(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        super.paintComponent(g);

        for (Map.Entry<String, ChromF> entry: allChromosomes.entrySet()) {

            if (entry.getKey().equals(selChromosomes)) {
                entry.getValue().paintChromosome(g, true);
            }else {
                entry.getValue().paintChromosome(g, false);
            }
        };

    }


    public void SaveFig() {
        selChromosomes=null;
        repaintPanel();

        File SaveFile;

        {
            BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = image.createGraphics();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("*.PNG"));
            fileChooser.setCurrentDirectory(fileChooser.getCurrentDirectory());
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                SaveFile = fileChooser.getSelectedFile();
                // save to file


                this.print(graphics2D);

                try {
                    ImageIO.write(image, "png", SaveFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void changecolorAllchromosomes(Color color){
        for (Map.Entry<String, ChromF> entry: allChromosomes.entrySet()) {
            entry.getValue().chromColor = color;

            setFocusable(true);
        };
    }

    public void changecolorAllcentromere(Color color){
        for (Map.Entry<String, ChromF> entry: allChromosomes.entrySet()) {
            entry.getValue().centromerColor = color;
            repaintPanel();
        };
    }


    public void transparentWindowInfo(String mes) throws IOException {
        tw.showWindow(mes,picture, selChromosomes, positionsonpicture, pic);

    }

    // Popup menu
    private class PopUpDemo extends JPopupMenu {
        JMenuItem changeChC;
        JMenuItem changeCenC;
        JMenuItem changeName;
        boolean res = false;
        public PopUpDemo(){
            changeChC = new JMenuItem("Change chromosome color");
            changeChC.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                     Color Chcolor = JColorChooser.showDialog(null,
                            "Choose chromosome color", Color.GRAY);
                     if (Chcolor != null) {
                         System.out.println(Chcolor);
                         allChromosomes.get(selChromosomes).chromColor = Chcolor;
                         res = true;
                         selChromosomes = null;
                         repaintPanel();
                     }else res = false;
                }
            });
            changeCenC = new JMenuItem("Change centromere color");
            changeCenC.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Color Cencolor = JColorChooser.showDialog(null,
                            "Choose centromere color", Color.BLACK);
                    if (Cencolor != null) {
                        System.out.println(Cencolor);
                        allChromosomes.get(selChromosomes).centromerColor = Cencolor;
                        res = true;
                        selChromosomes = null;
                        repaintPanel();
                    } else res = false;
                }
            });

            changeName = new JMenuItem("Change chromosome name");
            changeName.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = JOptionPane.showInputDialog("What name do you like?", "Change chromosome name");
                    if (allChromosomes.containsKey(name)){
                        ChromF toReplace = allChromosomes.get(name);
                        toReplace.chromName = selChromosomes;
                        allChromosomes.put(name, allChromosomes.get(selChromosomes));
                        allChromosomes.get(name).chromName = name;
                        allChromosomes.put(selChromosomes, toReplace);
                    } else {
                        ChromF torep = allChromosomes.get(selChromosomes);
                        torep.chromName = name;
                        allChromosomes.remove(selChromosomes);
                        allChromosomes.put(name, torep);
                    }
                    repaintPanel();
                }
            });
            add(changeChC);
            add(changeCenC);
            add(changeName);
        }
    }

////////////////////CHROMOSOMES POSITION CHANGING ////////////////////////////////////




}