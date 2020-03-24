package Calculations;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FrameCoordinates extends JFrame {

    public FrameCoordinates(String pathCorrectFormat) {

        this.pathCorrectFormat = pathCorrectFormat;
        this.setTitle("Show coordinates");


        dataNonEditable = fillTable(this.pathCorrectFormat);

        dataNonEditable.setPreferredScrollableViewportSize(new Dimension(dataNonEditable.getPreferredSize().width + 30, dataNonEditable.getPreferredSize().height));


        initComponents();
        this.pack();

        minimumSize = new Dimension(this.getWidth(), this.getHeight());

        this.setMinimumSize(minimumSize);

        this.setDefaultCloseOperation(2);
        this.setVisible(true);

    }


    public void initComponents() {

        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constrain = new GridBagConstraints();


        constrain.insets = new Insets(0, 0, 0, 10);
        this.addComponent(new JScrollPane(dataNonEditable), GridBagConstraints.BOTH, constrain, 1, 1, 0, 0, 1, 3, 0, 0);
        this.addComponent(bondLength, GridBagConstraints.HORIZONTAL, constrain, 0, 0.33, 2, 0, 1, 1, 0, 0);
        this.addComponent(angle, GridBagConstraints.HORIZONTAL, constrain, 0, 0.33, 2, 1, 1, 1, 0, 0);
        this.addComponent(torsion, GridBagConstraints.HORIZONTAL, constrain, 0, 0.33, 2, 2, 1, 1, 0, 0);


        constrain.insets = new Insets(0, 10, 60, 30);
        this.addComponent(informationBondLength, GridBagConstraints.HORIZONTAL, constrain, 0, 0.33, 1, 0, 1, 1, 0, 0);
        this.addComponent(informationAngle, GridBagConstraints.HORIZONTAL, constrain, 0, 0.33, 1, 1, 1, 1, 0, 0);
        this.addComponent(informationTorsion, GridBagConstraints.HORIZONTAL, constrain, 0, 0.33, 1, 2, 1, 1, 0, 0);

        constrain.anchor = GridBagConstraints.CENTER;
        constrain.insets = new Insets(0, 10, 0, 20);
        this.addComponent(atomsBondLength, GridBagConstraints.NONE, constrain, 0, 0.33, 1, 0, 1, 1, 50, 0);
        this.addComponent(atomsAngle, GridBagConstraints.NONE, constrain, 0, 0.33, 1, 1, 1, 1, 50, 0);
        this.addComponent(atomsTorsion, GridBagConstraints.NONE, constrain, 0, 0.33, 1, 2, 1, 1, 50, 0);


        atomsBondLength.addKeyListener(textfieldlimitations);
        atomsAngle.addKeyListener(textfieldlimitations);
        atomsTorsion.addKeyListener(textfieldlimitations);


        bondLength.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (!(atomsBondLength.getText().isEmpty())) {


                    int[] indexNumber = getIndex(atomsBondLength);


                    if (indexNumber.length != 2) {
                        JOptionPane.showMessageDialog(rootPane, "Provide two numbers");
                        atomsBondLength.setText("");
                    } else if (indexNumber[0] > dataNonEditable.getRowCount() || indexNumber[1] > dataNonEditable.getRowCount()) {

                        JOptionPane.showMessageDialog(rootPane, "Index out of range");
                        atomsBondLength.setText("");

                    } else {
                        double[] vector = Atom.coordinates(dataNonEditable, indexNumber[0], indexNumber[1]);
                        double calculatedBondLength = Atom.bondLength(vector);


                        JOptionPane.showMessageDialog(rootPane, "Calculated bond length is " + toTwoDecimalFormat(rounding(calculatedBondLength, 2)));


                    }


                } else {

                    JOptionPane.showMessageDialog(rootPane, "Empty text box");

                }
            }
        });


        angle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!(atomsAngle.getText().isEmpty())) {


                    int[] indexNumber = getIndex(atomsAngle);


                    if (indexNumber.length != 3) {
                        JOptionPane.showMessageDialog(rootPane, "Provide three numbers");
                        atomsAngle.setText("");
                    } else if (indexNumber[0] > dataNonEditable.getRowCount() || indexNumber[1] > dataNonEditable.getRowCount() || indexNumber[2] > dataNonEditable.getRowCount()) {

                        JOptionPane.showMessageDialog(rootPane, "Index out of range");
                        atomsAngle.setText("");

                    } else {
                        double[] vector1 = Atom.coordinates(dataNonEditable, indexNumber[0], indexNumber[1]);
                        double[] vector2 = Atom.coordinates(dataNonEditable, indexNumber[2], indexNumber[1]);
                        double calculatedAngle = Atom.angle(vector1, vector2);

                        if (calculatedAngle != -1) {

                            JOptionPane.showMessageDialog(rootPane, "Calculated angle is " + toTwoDecimalFormat(rounding(calculatedAngle, 2)) + " degree");

                        } else {

                            JOptionPane.showMessageDialog(rootPane, "At least one vector is zero vector?");

                        }

                    }


                } else {

                    JOptionPane.showMessageDialog(rootPane, "Empty text box");

                }
            }


        });


        torsion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!(atomsTorsion.getText().isEmpty())) {


                    int[] indexNumber = getIndex(atomsTorsion);


                    if (indexNumber.length != 4) {
                        JOptionPane.showMessageDialog(rootPane, "Provide four numbers");
                        atomsTorsion.setText("");
                    } else if (indexNumber[0] > dataNonEditable.getRowCount() || indexNumber[1] > dataNonEditable.getRowCount() || indexNumber[2] > dataNonEditable.getRowCount() || indexNumber[3] > dataNonEditable.getRowCount()) {

                        JOptionPane.showMessageDialog(rootPane, "Index out of range");
                        atomsTorsion.setText("");

                    } else {
                        double calculatedTorsion = Atom.torsion(dataNonEditable, indexNumber);

                        if (calculatedTorsion != -1) {

                            JOptionPane.showMessageDialog(rootPane, "Calculated torsion is " + toTwoDecimalFormat(rounding(calculatedTorsion, 2)) + " degree");

                        } else {

                            JOptionPane.showMessageDialog(rootPane, "Planes are not specified correctly.");

                        }

                    }


                } else {

                    JOptionPane.showMessageDialog(rootPane, "Empty text box");

                }


            }
        });


    }


    public <C extends JComponent> void addComponent(C component, int fill, GridBagConstraints constrain, double weightx, double weighty, int gridx, int gridy, int gridwidth, int gridheight, int ipadx, int ipady) {
        constrain.fill = fill;
        constrain.weightx = weightx;
        constrain.weighty = weighty;
        constrain.gridx = gridx;
        constrain.gridy = gridy;
        constrain.gridwidth = gridwidth;
        constrain.gridheight = gridheight;
        constrain.ipadx = ipadx;
        constrain.ipady = ipady;

        this.getContentPane().add(component, constrain);
    }


    public double rounding(double toRound, int scale) {
        BigDecimal bd = new BigDecimal(Double.toString(toRound));
        bd = bd.setScale(scale, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public String toTwoDecimalFormat(double toFormat) {


        if (toFormat == 0) {
            return "0";

        } else {

            char[] resultInArray = Double.toString(toFormat).toCharArray();
            int dotPosition = resultInArray.length + 1;

            for (int i = 0; i < resultInArray.length; i++) {

                if (resultInArray[i] == 46) {

                    dotPosition = i + 1;

                }


            }


            if (resultInArray.length - dotPosition == -1) {

                return Double.toString(toFormat);

            } else if (resultInArray.length - dotPosition == 1) {

                return toFormat + "0";
            } else {

                return Double.toString(toFormat);

            }


        }


    }


    public boolean onlyNumbers(String fromClipboard) {

        char[] seperateChars = fromClipboard.toCharArray();

        for (char item : seperateChars) {
            if (!(Character.isDigit(item))) {
                return false;
            }
        }

        return true;

    }


    private class TextFieldLimitations extends KeyAdapter {

        public void keyTyped(KeyEvent e) {
            if ((e.getKeyChar() < 48 || e.getKeyChar() > 57) && e.getKeyChar() != 32) {
                e.consume();
            }

        }


        public void keyPressed(KeyEvent e) {


            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                String name;

                try {
                    name = (String) clipboard.getData(DataFlavor.stringFlavor);

                    if (!(onlyNumbers(name))) {
                        JOptionPane.showMessageDialog(rootPane, "Please copy and paste only numbers. No spaces");
                        e.consume();
                    }
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(rootPane, "Error occurs");
                }

            }

        }


    }

    public JTable fillTable(String pathCorrectFormat) {
        try {
            Object[] columnNames = {"Atom number", "Atom symbol", "Coordinate x", "Coordinate y", "Coordinate z"};

            Atom[] tableOfAtoms = Atom.fileToTable(pathCorrectFormat);


            Object[][] data = new Object[tableOfAtoms.length][5];


            for (int i = 0; i < tableOfAtoms.length; i++) {
                data[i][0] = i + 1;
                data[i][1] = tableOfAtoms[i].symbol;
                data[i][2] = tableOfAtoms[i].x;
                data[i][3] = tableOfAtoms[i].y;
                data[i][4] = tableOfAtoms[i].z;

            }

            if (checkIfError(tableOfAtoms)) {
                JOptionPane.showMessageDialog(rootPane, "Error. Your input file do not have data in acceptable format. Follow file coordinates.txt. Please check also if the number in the first line is not greater than number of atoms");
                bondLength.setEnabled(false);
                angle.setEnabled(false);
                torsion.setEnabled(false);
                return new JTable(data, columnNames);
            } else {

                if (Atom.counter(pathCorrectFormat) != tableOfAtoms.length)
                    JOptionPane.showMessageDialog(rootPane, "Not all data are displayed. Please adjust the number in first row of the file");
                return new JTable(data, columnNames);

            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane, "IOException is thrown in creating the Table. Empty table created");
            bondLength.setEnabled(false);
            angle.setEnabled(false);
            torsion.setEnabled(false);
            return new JTable();
        }


    }


    public boolean checkIfError(Atom[] tableOfAtoms) {
        if (tableOfAtoms[0].symbol.equals("Error") && tableOfAtoms[0].x == 0 && tableOfAtoms[0].y == 0 && tableOfAtoms[0].z == 0)
            return true;

        else return false;
    }


    public int[] getIndex(JTextField jTextField) {

        String[] index = jTextField.getText().trim().split("\\s+");


        int[] indexNumber = new int[index.length];


        for (int i = 0; i < index.length; i++) {
            indexNumber[i] = Integer.parseInt(index[i]);

        }

        return indexNumber;

    }


    private JTable dataNonEditable;
    private JButton bondLength = new JButton("Bond length");
    private JButton angle = new JButton("Angle");
    private JButton torsion = new JButton("Torsion");
    private JTextField atomsBondLength = new JTextField();
    private JTextField atomsAngle = new JTextField();
    private JTextField atomsTorsion = new JTextField();
    private JLabel informationBondLength = new JLabel("1. Select two atoms to calculate the bond length:");
    private JLabel informationAngle = new JLabel("2. Select three atoms to calculate angle (degree) in the order A B C, where B is a vertex:");
    private JLabel informationTorsion = new JLabel("3. Select the four atoms in order of A B C D to calculate the torsion angle between planes A-B-C and B-C-D:");

    private TextFieldLimitations textfieldlimitations = new TextFieldLimitations();
    Dimension minimumSize;
    String pathCorrectFormat;

}
