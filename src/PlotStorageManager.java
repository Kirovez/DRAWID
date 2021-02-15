import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by ikirov on 14/11/2016.
 */
public class PlotStorageManager extends JDialog{
    Frame owner;
    ArrayList<String> plotNames;
    int cnt = 0;
    GridBagConstraints gbc = new GridBagConstraints();
    JPanel panel;
    Main main;


    ArrayList<JLabel> buttons= new ArrayList<>();
    public void showSelectedPlot(String name){
        main.dr = PlotStorage.getPlot(name);
        main.showIdeo();
    }

    public PlotStorageManager(Frame owner, ArrayList<String> plotNames, Main main) {
        super(owner);
        this.main = main;
        this.plotNames = plotNames;
        this.owner = owner;
        buildButtonPanel();
        createGUI();
    }

    public void removePlot(String name){
        PlotStorage.removePlot(name);
        plotNames.remove(name);
        main.mainJpanel.revalidate();
        Main.currentPlotCounter = 0;
        main.viewStorage.revalidate();
    }



    public void buildButtonPanel(){
        panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setLayout(new GridBagLayout());
        for (String names : plotNames){
           final JLabel but = new JLabel(names);
            but.setSize(new Dimension(50,100));
            but.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getButton() == 1){
                        showSelectedPlot(but.getText());
                    }else if (e.getButton() == 3){
                        removePlot(but.getText());
                        but.setVisible(false);
                    }
                }
            });

            but.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    super.mouseMoved(e);
                    but.setBackground(Color.blue);
                    but.revalidate();
                }
            });


            but.setBackground(Color.GRAY);
            gbc.gridy = cnt;
            gbc.fill = GridBagConstraints.HORIZONTAL; // stretch the button to adjust grindx;
            gbc.insets = new Insets(2, 2, 2, 2); // add spaces between buttons
            gbc.gridwidth = 1; // how many cells button occupies;
            gbc.gridx = 0; // position
            buttons.add(but);
            panel.add(but, gbc);
            cnt++;
        }

        add(panel);
    }

    private void createGUI() {
        setPreferredSize(new Dimension(600, 400));
        setTitle(getClass().getSimpleName());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
