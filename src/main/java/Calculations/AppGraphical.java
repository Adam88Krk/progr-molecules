package Calculations;


import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.*;


import java.io.File;


public class AppGraphical extends JFrame {

    public AppGraphical() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;

        this.setTitle("Calculations");

        initComponents();

        this.pack();

        int widthFrame = this.getSize().width;
        int heightFrame = this.getSize().height;

        this.setLocation(width / 2 - widthFrame / 2, height / 2 - heightFrame / 2);

        this.setResizable(false);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);

    }

    public void initComponents() {
        container = this.getContentPane();

        panelForButtons.add(load);
        panelForButtons.add(showCoordinates);


        panelForButtons.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        showCoordinates.setEnabled(false);


        panelForPath.setLayout(new GridLayout(2, 1));
        panelForPath.add(instruction);
        panelForPath.add(path);

        container.add(panelForButtons, BorderLayout.PAGE_START);
        container.add(panelForPath, BorderLayout.CENTER);


        path.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {


                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    String name;

                    try {

                        name = (String) clipboard.getData(DataFlavor.stringFlavor);


                        if (!isCapitalLetter(name.charAt(0)) || !(name.charAt(1) == 58) || !(name.charAt(2) == 92)) {
                            JOptionPane.showMessageDialog(rootPane, "Beginning of your path is incorrect");

                            e.consume();

                        }


                    } catch (Exception exp) {
                        JOptionPane.showMessageDialog(rootPane, "Error occurs");
                    }

                }

            }


            @Override
            public void keyTyped(KeyEvent e) {

                if (path.getText().length() == 0 && !(isCapitalLetter(e.getKeyChar()))) {
                    e.consume();
                } else if (path.getText().length() == 1 && !(e.getKeyChar() == 58)) {
                    e.consume();
                } else if (path.getText().length() == 2 && !(e.getKeyChar() == 92)) {
                    e.consume();
                }


            }
        });


        load.addActionListener(e -> {

            String providedPath = path.getText();
            StringBuilder pathNew = new StringBuilder();


            if (!providedPath.contains("\\") || providedPath.contains("\\\\") || providedPath.contains(" ")) {
                JOptionPane.showMessageDialog(rootPane, "For sure it is not a path. Incorrect");
                path.setText("");


            } else {
                String[] tablePath = providedPath.split("\\\\");
                pathNew.append(tablePath[0]);
                for (int i = 1; i < tablePath.length; i++) {
                    pathNew.append(File.separator + tablePath[i]);

                }

                pathCorrectFormat = pathNew.toString();

                File fileWithData = new File(pathCorrectFormat);


                if (fileWithData.isFile() && fileWithData.exists()) {
                    JOptionPane.showMessageDialog(rootPane, "Loaded successfully");


                    showCoordinates.setEnabled(true);

                } else {
                    JOptionPane.showMessageDialog(rootPane, "File does not exist");
                    path.setText("");
                }
            }


        });


        showCoordinates.addActionListener((e) -> {

            if (frameFlag) {
                framecoordinates = new FrameCoordinates(pathCorrectFormat);
                frameFlag = false;
                load.setEnabled(false);


                framecoordinates.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        frameFlag = true;
                        load.setEnabled(true);

                    }
                });

            }

        });


    }


    public boolean isCapitalLetter(char check) {
        for (int i = 65; i < 91; i++) {
            if (check == i) {
                return true;
            }
        }

        return false;

    }


    private Container container;
    private String pathCorrectFormat;
    private boolean frameFlag = true;
    private FrameCoordinates framecoordinates;

    private JPanel panelForButtons = new JPanel();
    private JPanel panelForPath = new JPanel();
    private JButton load = new JButton("Load");
    private JButton showCoordinates = new JButton("Show coordinates");

    private JLabel instruction = new JLabel("Provide path to file in xyz format using \\ as the separator:");
    private JTextField path = new JTextField();

}


