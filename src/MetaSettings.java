import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Map;

/**
 * Created by ikirov on 14/11/2016.
 */
public class MetaSettings extends JDialog{

    DataReader dr;
    Frame owner;

    public MetaSettings(Frame owner, DataReader dr) {
        super(owner);
        this.owner = owner;
        this.dr = dr;
        setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));



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
