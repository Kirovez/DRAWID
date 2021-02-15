import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Map;

/**
 * Chromosome settings
 */
public class Settings extends JDialog{

    DataReader dr;
    Frame owner;
    int currentPercentage = 100;


    public void changeChromosomeLength(int percentage) {
        for (Map.Entry<String, ChromF> eachChrom : dr.allChromosomes.entrySet()) {
            eachChrom.getValue().changeChromosomeLength(percentage);
            owner.repaint();
        }
    }

    public Settings(Frame owner, DataReader dr) {
        super(owner);
        this.owner = owner;
        this.dr = dr;
        setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));


        add(new JLabel("Chromosome length, %"));
        SpinnerNumberModel monthModel = new SpinnerNumberModel(currentPercentage,0,200,10);
        final JSpinner spinner = new JSpinner(monthModel);
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) spinner.getValue();
                int toChange;
                if ((currentPercentage - value) > 0){
                    toChange = 100 - (currentPercentage - value);
                }else {
                    toChange = 100 + Math.abs(currentPercentage - value);
                }
                currentPercentage = (int) spinner.getValue();
                changeChromosomeLength(toChange);

            }
        });

        this.add(spinner);
        createGUI();
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
