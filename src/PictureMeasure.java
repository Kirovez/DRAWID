import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Takes all measurement events
 */
public class PictureMeasure extends JLabel{

    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    private boolean Bpressed;
    private boolean Spressed;

    private int X;
    private int Y;

    private int remchromcnt = 0;

    // index of a dot of centromere in arrayLists
    private int centromeredotindex = 0;

    private static Color centromereringcolor= Color.WHITE;
    private Color unitNameColor = Color.YELLOW;
    static Color ddcolor = Color.GREEN;

    static Color nor = new Color(104,156,144);
    static Color nor_c = new Color(104,156,145);

    static Color bcolor = new Color(234,239,33);
    static Color bcolor_c = new Color(239, 22, 178);
    Color lineC = Color.CYAN;

    HashMap<Integer, String> unitMeta = new HashMap<>();

    Color currentringcolor;
    Color currentLinecolor;
    String pressedkey;

    static Color ringcolor = Color.RED;

    ArrayList<MeasuredUnit> allMeasureUnits= new ArrayList<>(); // the most important collection. Contains all measured units;

    public ArrayList<Integer> Xcrd = new ArrayList<>();
    public ArrayList<Integer> Ycrd= new ArrayList<>();
    public ArrayList<Color> dotColor = new ArrayList<>();
    public ArrayList<Color> lineColor = new ArrayList<>();

    HashMap<String, int[]> positionOnPicture = new HashMap<>();

    public void rescaleImage(double factor){
        if(Xcrd.size()!=0){
            for (int i = 0; i < Xcrd.size();i++){
                int newValue = Xcrd.get(i);
                Xcrd.set(i, (int) (newValue * factor));

                newValue = Ycrd.get(i);
                Ycrd.set(i, (int) (newValue * factor));
            }
        }

        repaintPanel();
    }


    public void repaintUnits(Graphics g){
        int s = Xcrd.size();
        if (Xcrd.size() != 0){
            for (int i=0; i < s; i++){
                g.setColor(dotColor.get(i));
                g.fillOval(Xcrd.get(i)-5,Ycrd.get(i)-5,10,10);

                if (i > 0){
                    g.setColor(lineColor.get(i));
                    g.drawLine(Xcrd.get(i),Ycrd.get(i),Xcrd.get(i-1),Ycrd.get(i-1));
                }
            }
        }

        // check if any measure units were added before and paint them
        if (allMeasureUnits.size() > 0){
            for (MeasuredUnit each: allMeasureUnits){
                s = each.dotColorUnit.size();
                int mring = -1;
                String name = "";
                // Go through metadata //
                for (int i=0; i < s; i++){
                    mring++;

                    // Draw rings
                    g.setColor(each.dotColorUnit.get(i));
                    g.drawOval(each.Xcoordinates.get(i)-ConstStrorage.DDSIZE/2,each.Ycoordinates.get(i)-ConstStrorage.DDSIZE/2,ConstStrorage.DDSIZE,ConstStrorage.DDSIZE);

                    Color dotcolor = each.dotColorUnit.get(i);
                    // Draw text
                    if (each.metUnit.size() != 0 && dotcolor != ringcolor){
                        if (each.metUnit.containsKey(i)) {
                            g.drawString(each.metUnit.get(i), each.Xcoordinates.get(i), each.Ycoordinates.get(i));
                            name = each.metUnit.get(i);
                        } else {
                            if(dotcolor != centromereringcolor){
                                g.drawString(name, each.Xcoordinates.get(i), each.Ycoordinates.get(i));
                            } else {
                                g.drawString("CEN", each.Xcoordinates.get(i), each.Ycoordinates.get(i));
                            }
                        }
                    }


                    // Drawline
                    if (i > 0){
                        g.setColor(each.lineColorUnit.get(i));
                        g.drawLine(each.Xcoordinates.get(i),each.Ycoordinates.get(i),each.Xcoordinates.get(i-1),each.Ycoordinates.get(i-1));
                    }
                }


                // Draw chromosome name //
                g.setColor(unitNameColor);
                g.drawString(each.nameUnit, each.Xcoordinates.get(s-1), each.Ycoordinates.get(s-1));
            }
        }
    }

    private void repaintPanel(){
        repaint();
    }

    public void removeAllValues(){
        Xcrd = new ArrayList<>();
        Ycrd= new ArrayList<>();
        dotColor = new ArrayList<>();
       lineColor = new ArrayList<>();
        unitMeta = new HashMap<>();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        repaintUnits(g);


    }
    public void changepressedkeyC(){

        currentringcolor = this.centromereringcolor;
        centromeredotindex = this.dotColor.size() - 1;
        changeRingColor();
    }
    Action changepressedkeyC = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            changepressedkeyC();
        }
    };


    public void changepressedkeyD(){
        currentringcolor = ddcolor;
        changeRingColor();
        String DDname = (String)JOptionPane.showInputDialog(
                this,
                "Input name for this metadata mark",
                "Metadata mark name",
                JOptionPane.PLAIN_MESSAGE,
                createImageIcon("/resources/buttons/DDmessage.png"),null,"Gene1");
                unitMeta.put(Xcrd.size()-1,DDname);
    }
    Action changepressedkeyD = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            changepressedkeyD();

        }
    };


    public void changepressedkeyB(){
        if (Bpressed){
            Bpressed = false;
            dotColor.set(dotColor.size() - 1, bcolor_c);
            currentringcolor = bcolor_c;
            changeRingColor();
        }else {
            Bpressed = true;
            String Bname = (String)JOptionPane.showInputDialog(
                    this,
                    "Input name for this metadata mark",
                    "Metadata mark name",
                    JOptionPane.PLAIN_MESSAGE,
                    createImageIcon("/resources/buttons/Bmessage.png"),null,"Band1");

            currentringcolor = bcolor;
            unitMeta.put(Xcrd.size()-1,Bname);
            changeRingColor();
        }

    }
    Action changepressedkeyB = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            changepressedkeyB();
        }
    };



    public void changepressedkeyS(){
        if (Spressed){
            Spressed = false;
            dotColor.set(dotColor.size() - 1, nor_c);
            currentringcolor = nor_c;
            changeRingColor();
        }else {
            Spressed = true;
            currentringcolor = nor;
            unitMeta.put(Xcrd.size()-1,"NOR");
            changeRingColor();
        }

    }
    Action changepressedkeyS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            changepressedkeyS();
        }
    };


    //Change color of last ring
    public void changeRingColor(){
        dotColor.set(dotColor.size() - 1, this.currentringcolor);
        repaintPanel();
        this.currentringcolor = this.ringcolor;
    }



    // Undo
    public void Undo(){
        if (Xcrd.size() > 0){
            Xcrd.remove( Xcrd.size() - 1);
            Ycrd.remove( Ycrd.size() - 1);
        } else {
            JOptionPane.showMessageDialog(this, "Nothing to remove");
        }

        if (lineColor.size() > 0){lineColor.remove( lineColor.size() - 1);}
        if (dotColor.size() > 0){

            if (dotColor.get(dotColor.size() - 1).equals(ddcolor)){
                unitMeta.remove(dotColor.size() - 1);
            } else if (dotColor.get(dotColor.size() - 1).equals(bcolor)){
                if (dotColor.size() == 1){//chromosome end
                    Bpressed = false;
                    unitMeta.remove(dotColor.size() - 1);
                    currentringcolor = ringcolor;
                }
                else if(dotColor.get(dotColor.size() - 2).equals(bcolor)){
                    Bpressed = true;
                    currentringcolor = bcolor;
                } else {
                    Bpressed = false;
                    unitMeta.remove(dotColor.size() - 1);
                    currentringcolor = ringcolor;
                }
            }

            dotColor.remove( dotColor.size() - 1);
        }

        repaintPanel();
    }


    Action und = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Undo();
        }
    };

    // Finsh
    public void Finish(){
        if (Xcrd.size()!=0){

            allMeasureUnits.add(new MeasuredUnit(String.valueOf(allMeasureUnits.size() + remchromcnt+1),
                    Xcrd,Ycrd,dotColor,lineColor,centromeredotindex, unitMeta));
            int pos[] = new int[2];
            pos[0] = Collections.min(Xcrd);
            pos[1] = Collections.min(Ycrd);

            positionOnPicture.put(String.valueOf(allMeasureUnits.size() + remchromcnt), pos);
            InfoChromMeasureWindow.chrMeasured(allMeasureUnits.size());
            repaintPanel();
            removeAllValues();
            Bpressed=false;
            Spressed=false;

        } else {
            JOptionPane.showMessageDialog(this, "No chromosomes measured");

        }


    }
    Action Fin = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Finish();

        }
    };


    protected ImageIcon createImageIcon(String path
                                        ) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void removeChromosome(){
        String[] possibilities = new String[allMeasureUnits.size()];

        for(int i = 0; i< allMeasureUnits.size(); i++){
            possibilities[i] = allMeasureUnits.get(i).nameUnit;
        }

        String s = (String)JOptionPane.showInputDialog(
                this, "Which chromosome you do not like?",
                "Choose chromosome to remove",
                JOptionPane.PLAIN_MESSAGE,
                createImageIcon("/resources/buttons/removeChromosome"),
                possibilities,
                "0");

        if (s!=null){
            int ind = 0;
            for(int i = 0; i< allMeasureUnits.size(); i++){
                if (allMeasureUnits.get(i).nameUnit.equals(s)){
                    ind = i;
                }
            }

            allMeasureUnits.remove(ind);
            remchromcnt+=1;
            repaintPanel();
            InfoChromMeasureWindow.chrMeasured(allMeasureUnits.size());

        }
    }



    public PictureMeasure(Icon icon) {

        setIcon(icon);
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

        getInputMap(IFW).put(KeyStroke.getKeyStroke("C"), "keyC");
        getActionMap().put("keyC", changepressedkeyC);

        getInputMap(IFW).put(KeyStroke.getKeyStroke("F"), "Finish");
        getActionMap().put("Finish", Fin);

        getInputMap(IFW).put(KeyStroke.getKeyStroke("keydown"), "Undo");
        getActionMap().put("Undo", und);

        getInputMap(IFW).put(KeyStroke.getKeyStroke("D"), "keyD");
        getActionMap().put("keyD", changepressedkeyD);

        getInputMap(IFW).put(KeyStroke.getKeyStroke("B"), "keyB");
        getActionMap().put("keyB", changepressedkeyB);

        getInputMap(IFW).put(KeyStroke.getKeyStroke("S"), "keyS");
        getActionMap().put("keyS", changepressedkeyS);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if (SwingUtilities.isLeftMouseButton(e)) {
                    X = e.getX();
                    Y = e.getY();
                    Xcrd.add(e.getX());
                    Ycrd.add(e.getY());
                    if (Bpressed){
                        dotColor.add(bcolor);
                        lineColor.add(bcolor);
                    }else if(Spressed){
                        dotColor.add(nor);
                        lineColor.add(nor);
                    }
                    else {
                        dotColor.add(Color.RED);
                        lineColor.add(lineC);
                    }


                    repaintPanel();

                } else if (SwingUtilities.isRightMouseButton(e)) {

                }

            }
        });


    }

}
