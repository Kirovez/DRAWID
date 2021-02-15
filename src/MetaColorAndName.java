import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * small window to change metacolor and names
 */
public class MetaColorAndName{
    HashMap<String, Color> colorForMetanames;
    ArrayList<String> metaNames;
    Plot pl;

    JPanel choosePanel = new JPanel();
    ArrayList<JButton> colJ = new ArrayList<>();

    public JButton nameButton(final String metaname){
        final JButton nameB = new JButton(metaname);
        nameB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String new_name = JOptionPane.showInputDialog("Type a new name", metaname);
                if (new_name != null && !(new_name.equals(metaname))) {
                    //change color buttons name;


                    // change meta names in color palette
                    Color temp = colorForMetanames.get(metaname);
                    colorForMetanames.remove(metaname);
                    colorForMetanames.put(new_name, temp);

                    for (JButton jb: colJ){
                        if (jb.getText().equals(metaname)){
                            jb.setText(new_name);
                        }
                    }

                    // change meta names in allChromosomes
                    for (Map.Entry<String, ChromF> chr : pl.allChromosomes.entrySet()) {
                        if (chr.getValue().metaTable != null) {
                            for (ArrayList<java.lang.Object> met : chr.getValue().metaTable.metadataTable) {
                                if (metaname.equals(met.get(0))) {
                                    met.set(0, new_name);

                                }

                            }
                        }

                    }
                }
                nameB.setText(new_name);
                pl.repaint();
            }

        });

        return nameB;
    }

    public JButton colorButton(final String metaname){
        final JButton colB = new JButton();
        colB.setText(metaname);
        colB.setBackground(colorForMetanames.get(colB.getText()));
        colB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color butCol = JColorChooser.showDialog(null,
                        "Choose color for " + colB.getText(), colorForMetanames.get(colB.getText()));
                if (butCol != null) {
                    colB.setBackground(butCol);
                    colorForMetanames.put(colB.getText(),butCol);
                    pl.repaint();
                }
            }
        });

        return colB;
    }

    public MetaColorAndName(Plot pl){
        JFrame main = new JFrame();
        this.pl = pl;
        this.metaNames = pl.chromSum.metaNames;
        this.colorForMetanames = pl.chromSum.colorForMetanames;

        choosePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridy = -1;
        gbc3.gridx = -1;
        gbc3.insets = new Insets(2, 2, 2, 2); // add spaces between buttons
        gbc3.gridwidth = 1; // how many cells button occupies;
        gbc3.fill = GridBagConstraints.HORIZONTAL; // stretch the button to adjust grindx;

        for(String metanames: metaNames){
            gbc3.gridy += 1;
            gbc3.gridx = 0;

            JButton tf = nameButton(metanames);
            choosePanel.add(tf,gbc3);

            gbc3.gridx = 1;
            JButton cb = colorButton(metanames);
            colJ.add(cb);
            choosePanel.add(cb, gbc3);

        }

        JButton OK = new JButton("OK");
        OK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JScrollPane jsc = new JScrollPane(choosePanel);

        main.add(jsc);
        main.setSize(new Dimension(300,300));
        main.setTitle("Signal color and names");
        main.setVisible(true);
    }

}
