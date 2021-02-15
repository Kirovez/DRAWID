import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

/**
 * Created by ikirov on 14/11/2016.
 */
public class statGraph extends JDialog{

    DataReader dr;
    Frame owner;
    int currentPercentage = 100;


    public void removeBars(boolean val) {
        for (Map.Entry<String, ChromF> eachChrom : dr.allChromosomes.entrySet()) {
            if (eachChrom.getValue().containerAverage!=null){
                eachChrom.getValue().drawBar = val;
                owner.repaint();
            }else {
                JOptionPane.showMessageDialog(owner,"No stat data available fot this plot!", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            }

        }
    }

    public statGraph(Frame owner, DataReader dr) {
        super(owner);
        this.owner = owner;
        this.dr = dr;

        final JCheckBox removeB = new JCheckBox("Show Bars", true);
        removeB.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                removeBars(removeB.isSelected());
            }
        });

        add(removeB);
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
