/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Viewer extends JFrame {
    Main m;


    PictureMeasure label;
    ImageIcon image = new ImageIcon();
    static BufferedImage rescaledImag;

    int w = 0;
    int h = 0;
    float zoomValue = 1;

    public DataReader drMeasure;
    JFrame frame = new JFrame("Chromosome Measure");

    public Viewer(String img, Main m) throws IOException {
        frame.setTitle(img);
        this.m = m;
        JPanel lab = new Label(img);

        frame.setContentPane(lab);
        frame.setSize(800, 800);
        frame.setVisible(true);


    }

    public void closeMeasureWindow(){
        frame.setVisible(false);
        frame.dispose();
    }

    public void jointMeasuredUnit(){

        joinerMeasureUnit j = new joinerMeasureUnit(label.allMeasureUnits.get(label.allMeasureUnits.size() - 2),
                label.allMeasureUnits.get(label.allMeasureUnits.size() - 1));

        label.allMeasureUnits.remove(label.allMeasureUnits.size() - 1);
        label.allMeasureUnits.remove(label.allMeasureUnits.size() - 1);

        label.allMeasureUnits.add(j.joinedClass);
        label.repaint();
    }

    public void CleanerPicture(){
        label.allMeasureUnits = null;
        label.allMeasureUnits = new ArrayList<>();
        label.Xcrd = new ArrayList<>();
        label.Ycrd= new ArrayList<>();
        label.dotColor = new ArrayList<>();
        label.lineColor = new ArrayList<>();
        label.revalidate();
        label.repaint();

   }


    // takes all X and Y measure before and determine index to multiply
    public float barMeasure() {
        float barLength = 0;
        for (int i = 0; i < label.Xcrd.size(); i++) {

            if (i > 0) {
                barLength += MeasuredUnit.lengthbycoordinates(label.Xcrd.get(i - 1), label.Ycrd.get(i - 1), label.Xcrd.get(i), label.Ycrd.get(i));
            }
        }
        label.Xcrd = new ArrayList<>();
        label.Ycrd = new ArrayList<>();
        label.dotColor = new ArrayList<>();
        label.lineColor = new ArrayList<>();

        return barLength;
    }




        public class Label extends JPanel{
        public Label(final String img) throws IOException {

            image = new ImageIcon(img);
            w = image.getIconWidth();
            h = image.getIconHeight();

            label = new PictureMeasure(image);


            setLayout(new BorderLayout());

            JScrollPane sp = new JScrollPane();
            sp.setViewportView(label);

            JPanel ButtonPanel = new JPanel();
            ButtonPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.insets = new Insets(2, 2, 2, 2); // add spaces between buttons
            gbc.gridx = 0; // position x

            gbc.gridwidth = 1; // how many cells button occupies;
            gbc.fill = GridBagConstraints.HORIZONTAL; // stretch the button to adjust grindx;

            gbc.gridy = 0;
            final InfoChromMeasureWindow inforchromw = new InfoChromMeasureWindow();
            ButtonPanel.add(inforchromw);


            gbc.gridy = 1; // position y
            JButton UndoButton = customButton("Undo1.png","Undo2.png","Undo2.png");
            UndoButton.setToolTipText("Undo segment");
            UndoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    label.Undo();
                }
            });

            ButtonPanel.add(UndoButton, gbc);

            gbc.gridy = 2;
            JButton centromere = customButton("Centromere.png", "Centromere2.png","Centromere2.png");
            centromere.setToolTipText("Centromere");
            centromere.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    label.changepressedkeyC();
                }
            });
            ButtonPanel.add(centromere, gbc);

            JButton finish = customButton("Finish1.png","Finish2.png","Finish2.png");
            finish.setToolTipText("Finish measuring of this chromosome");
            finish.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (label.Xcrd.size()!=0){
                            label.Finish();

                    }else {
                        JOptionPane.showMessageDialog(label, "No chromosomes measured!");
                    }
                }
            });

            gbc.gridy = 3;
            ButtonPanel.add(finish, gbc);



            JButton showIdio = customButton("ShowIdio1.png","ShowIdio.png","ShowIdio.png");
            showIdio.setToolTipText("Draw Ideogram");
            showIdio.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if(label.allMeasureUnits.size()!=0){
                        //System.out.println(ChromSumData.multypleY);
                        drMeasure = new DataReader(label.allMeasureUnits);
                        m.mainWindow.setAlwaysOnTop( true );
                        m.calculaIdeoFromViewer();
                        m.mainWindow.setAlwaysOnTop( false );

                        //closeMeasureWindow();
                    }else {
                        JOptionPane.showMessageDialog(label, "No chromosomes measured!");
                    }


                }
            });

            gbc.gridy = 4;
            ButtonPanel.add(showIdio, gbc);

            JButton rem = customButton("removeChromosome.png","removeChromosome2.png","removeChromosome2.png");
            rem.setToolTipText("Remove measurement for chromosome");

            rem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    label.removeChromosome();
                }
            });

            gbc.gridy = 5;
            ButtonPanel.add(rem, gbc);


            JButton breakchromosome = customButton("Break2.png", "Break1.png","Break3.png");
            breakchromosome.setToolTipText("Join two last chromosome segments");
            breakchromosome.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jointMeasuredUnit();
                }
            });

            gbc.gridy = 6;
            ButtonPanel.add(breakchromosome, gbc);


            JButton clean = customButton("CleanAll1.png", "CleanAll2.png", "CleanAll2.png");
            clean.setToolTipText("Clean all measurements!");
            clean.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (label.allMeasureUnits.size()!=0 | label.Xcrd.size()!=0){
                        int output = JOptionPane.showConfirmDialog(label
                                , "Are you sure that you want to remove all measurements?"
                                ,"DRAWID"
                                ,JOptionPane.YES_NO_OPTION);

                        if(output == JOptionPane.YES_OPTION){
                           CleanerPicture();
                        }
                    }

                }
            });

            gbc.gridy = 7;
            ButtonPanel.add(clean, gbc);


            JButton scaleBar = customButton("Bar1.png","Bar2.png","Bar2.png");
            scaleBar.setToolTipText("Use last measured segment as bar");
            scaleBar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (label.Xcrd.size()!=0){
                        if (label.Xcrd.size() > 1){
                            String bar = JOptionPane.showInputDialog(null, "What is the absolute length of the measured bar","");
                            if(Integer.valueOf(bar) > 0){
                                MeasuredUnit.barIndexMuliply = Integer.valueOf(bar)/barMeasure();
                                JOptionPane.showMessageDialog(label, "Successfully!Chromosome length will be recalculated.");
                            }
                        }else {
                            JOptionPane.showMessageDialog(label, "Not enough data!");
                        }
                    }

                }
            });

            gbc.gridy = 8;
            ButtonPanel.add(scaleBar, gbc);

            JButton Band = customButton("Band1.png","Band2.png","Band2.png");
            Band.setToolTipText("Mark this dot as a band start/finish");
            Band.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    label.changepressedkeyB();
                }
            });

            gbc.gridy = 9;
            ButtonPanel.add(Band, gbc);


            JButton DD = customButton("DD1.png","DD2.png","DD2.png");
            DD.setToolTipText("Mark this dot as dot-like signal");
            DD.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    label.changepressedkeyD();
                }
            });

            gbc.gridy = 10;
            ButtonPanel.add(DD, gbc);

            add(sp, BorderLayout.CENTER);

            JScrollPane js = new JScrollPane(ButtonPanel);
            add(js, BorderLayout.EAST);


            /////// TOPBUTTON///////////

            JPanel TopButtonPAnel = new JPanel();
            TopButtonPAnel.setLayout(new GridBagLayout());
            GridBagConstraints gbcT = new GridBagConstraints();

            gbcT.insets = new Insets(2, 2, 2, 2); // add spaces between buttons
            gbcT.gridwidth = 1; // how many cells button occupies;
            gbcT.fill = GridBagConstraints.VERTICAL; // stretch the button to adjust grindx;
            gbcT.gridy = 0;

            JButton zoomin = customButton("ZoomIn1.png","ZoomIn2.png","ZoomIn2.png");
            zoomin.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (label.allMeasureUnits.size()==0){ // apply zoom only when you have not measured yet

                        w*=1.2;
                        h*=1.2;
                        zoomValue*=1.2;
                        InfoChromMeasureWindow.infowindow.setText("Zoom is :" + zoomValue);
                        label.setIcon(new ImageIcon(rescaledImage(image.getImage(),w,h)));
                        label.rescaleImage(1.2);
                        label.revalidate();

                    } else {
                        JOptionPane.showMessageDialog(label, "Sorry, but zoom can be applied only before all measurements");
                    }


                }
            });
            gbcT.gridx = 0;
            TopButtonPAnel.add(zoomin, gbcT);


            JButton zoomout = customButton("ZoomOut1.png","ZoomOut2.png","ZoomOut2.png");
            zoomout.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(label.allMeasureUnits.size()==0){
                        w*=0.8;
                        h*=0.8;
                        zoomValue*=0.8;
                        InfoChromMeasureWindow.infowindow.setText("Zoom is :" + zoomValue);
                        label.setIcon(new ImageIcon(rescaledImage(image.getImage(),w,h)));
                        label.rescaleImage(0.8);
                        label.revalidate();
                    } else {
                        JOptionPane.showMessageDialog(label, "Sorry, but zoom can be applied only before all measurements");
                    }

                }
            });

            gbcT.gridx = 1;
            TopButtonPAnel.add(zoomout, gbcT);

            JButton cutChromosome = new JButton("Cut chromosome");
            cutChromosome.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CutOutChromosome coc = new CutOutChromosome(label.Xcrd, label.Ycrd, image);
                    try {
                        coc.applyClip();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            gbcT.gridx = 2;
            TopButtonPAnel.add(cutChromosome, gbcT);
            add(TopButtonPAnel, BorderLayout.NORTH);





        }

    }


    public Image rescaledImage(Image img, int w , int h){
        rescaledImag = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = rescaledImag.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img,0,0,w,h,null);
        g2.dispose();
        return rescaledImag;
    }

    // Custom button
    public JButton customButton(String startButton1, String startButtonHover1, String startButtonActive1) throws IOException {
        BufferedImage startButton = ImageIO.read(getClass().getResource("resources/buttons/" + startButton1));
        BufferedImage startButtonHover = ImageIO.read(getClass().getResource("resources/buttons/" + startButtonHover1));
        BufferedImage startButtonActive = ImageIO.read(getClass().getResource("resources/buttons/" + startButtonActive1));

        JButton retB = new JButton(new ImageIcon(startButton));
        retB.setCursor(new Cursor(Cursor.HAND_CURSOR));
        retB.setSize(50, 50);
        retB.setRolloverIcon(new ImageIcon(startButtonHover));
        retB.setPressedIcon(new ImageIcon(startButtonActive));
        retB.setBorder(BorderFactory.createEmptyBorder());
        retB.setContentAreaFilled(false);
        retB.setFocusable(false);

        return retB;


    }
}


