package Calculations;


import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Atom {
    String symbol;
    Double x;
    Double y;
    Double z;


    public Atom() {

    }

    public Atom(String symbol, double x, double y, double z) {
        this.symbol = symbol;
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public static String getPath()    //convert the path to path with File.separator - more universal
    {
        String pathOld;
        StringBuilder pathNew = new StringBuilder();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Provide path to file in text format using \\ as the separator:");
        System.out.println("\\");
        System.out.println("\\\\");
        pathOld = scanner.nextLine();

        if (!pathOld.contains("\\") || pathOld.contains("\\\\") || pathOld.contains(" ")) {
            System.out.println("For sure it is not a path. Exit");
            return "Incorrect";
        } else {
            String[] tablePath = pathOld.split("\\\\");
            pathNew.append(tablePath[0]);
            for (int i = 1; i < tablePath.length; i++) {
                pathNew.append(File.separator + tablePath[i]);

            }

            return pathNew.toString();
        }
    }


    public static int counter(String path) throws IOException {
        int counter;
        String fileLine = "";

        BufferedReader readerCheck = new BufferedReader(new FileReader(path));

        for (counter = -2; fileLine != null; counter++) {

            fileLine = readerCheck.readLine();

        }

        return counter;

    }


    public static Atom[] fileToTable(String path) throws IOException   //read the file, put the coordinates into the table of atom instances
    {

        Atom[] TableOfAtoms;


        BufferedReader reader = new BufferedReader(new FileReader(path));


        try {

            int numberOfAtoms = Integer.parseInt(reader.readLine());

            System.out.println(numberOfAtoms);

            TableOfAtoms = new Atom[numberOfAtoms];


            for (int i = 0; i < numberOfAtoms; i++) {
                String line = reader.readLine();

                StringTokenizer token = new StringTokenizer(line, " ");

                TableOfAtoms[i] = new Atom(token.nextToken(), Double.parseDouble(token.nextToken()), Double.parseDouble(token.nextToken()), Double.parseDouble(token.nextToken()));
            }

            reader.close();
            return TableOfAtoms;


        } catch (Exception e) {
            TableOfAtoms = new Atom[1];

            TableOfAtoms[0] = new Atom("Error", 0, 0, 0);

            return TableOfAtoms;
        }

    }

    @Override
    public String toString() {
        return symbol + ": " + x + " " + y + " " + z;
    }


    public static double bondLength(Atom[] atoms) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select two atoms to calculate the bond length: ");
        int index1 = scanner.nextInt();
        int index2 = scanner.nextInt();

        if (index1 <= atoms.length && index2 <= atoms.length && index1 != index2) {
            return Math.sqrt(Math.pow(atoms[index1 - 1].x - atoms[index2 - 1].x, 2) + Math.pow(atoms[index1 - 1].y - atoms[index2 - 1].y, 2) + Math.pow(atoms[index1 - 1].z - atoms[index2 - 1].z, 2));
        } else if (index1 == index2) {
            System.out.println("Select two different atoms, not the same");
            return 0;
        } else {
            System.out.println("Index out of bounds");
            return 0;
        }
    }


    public static double bondLength(Atom[] atoms, int index1, int index2) {

        if (index1 <= atoms.length && index2 <= atoms.length && index1 != index2) {
            return Math.sqrt(Math.pow(atoms[index1 - 1].x - atoms[index2 - 1].x, 2) + Math.pow(atoms[index1 - 1].y - atoms[index2 - 1].y, 2) + Math.pow(atoms[index1 - 1].z - atoms[index2 - 1].z, 2));
        } else if (index1 == index2) {
            System.out.println("Select two different atoms, not the same");
            return 0;
        } else {
            System.out.println("Index out of bounds");
            return -1;
        }
    }


    public static double bondLength(double[] vector) {


        BigDecimal result = new BigDecimal(Double.toString(vector[0])).pow(2).add(new BigDecimal(Double.toString(vector[1])).pow(2)).add(new BigDecimal(Double.toString(vector[2])).pow(2));

        return Math.sqrt(result.doubleValue());
    }


    public static double[] coordinates(Atom[] atoms, int index1, int index2) {
        double[] vector = new double[3];

        vector[0] = atoms[index1 - 1].x - atoms[index2 - 1].x;
        vector[1] = atoms[index1 - 1].y - atoms[index2 - 1].y;
        vector[2] = atoms[index1 - 1].z - atoms[index2 - 1].z;

        return vector;
    }


    public static double[] coordinates(JTable jtable, int index1, int index2) {
        double[] vector = new double[3];

        vector[0] = (Double) (jtable.getModel().getValueAt(index1 - 1, 2)) - (Double) (jtable.getModel().getValueAt(index2 - 1, 2));
        vector[1] = (Double) (jtable.getModel().getValueAt(index1 - 1, 3)) - (Double) (jtable.getModel().getValueAt(index2 - 1, 3));
        vector[2] = (Double) (jtable.getModel().getValueAt(index1 - 1, 4)) - (Double) (jtable.getModel().getValueAt(index2 - 1, 4));


        return vector;
    }


    public static double[] createZeroVector(int vectorLength) {

        double[] vectorZero = new double[vectorLength];

        for (int i = 0; i < vectorLength; i++) {

            vectorZero[i] = 0;

        }

        return vectorZero;

    }


    public static double angle(Atom[] atoms) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select three atoms to calculate angle (degree) in the order A B C, where B is a vertex");
        int index1 = scanner.nextInt();
        int index2 = scanner.nextInt();
        int index3 = scanner.nextInt();


        if (index1 <= atoms.length && index2 <= atoms.length && index3 <= atoms.length && index1 != index2 && index2 != index3 && index1 != index3) {
            // ba - vector1         bc - vector2
            double[] vector1;
            double[] vector2;

            vector1 = coordinates(atoms, index1, index2);
            vector2 = coordinates(atoms, index3, index2);

            double ab = bondLength(atoms, index1, index2);
            double bc = bondLength(atoms, index2, index3);
            double scalar = scalarProduct(vector1, vector2);

            return Math.toDegrees(Math.acos(scalar / (ab * bc)));

        } else {
            System.out.println("Not correct. Index out of bounds or at least two atoms the same");
            return -1;
        }
    }


    public static double angle(double[] vector1, double[] vector2) {
        double vector1Length = bondLength(vector1);
        double vector2Length = bondLength(vector2);


        if (vector1Length != 0 && vector2Length != 0) {
            double scalar = scalarProduct(vector1, vector2);


            BigDecimal result = new BigDecimal(Double.toString(scalar)).divide(new BigDecimal(Double.toString(vector1Length)).multiply(new BigDecimal(Double.toString(vector2Length))), 3, RoundingMode.HALF_UP);
            return Math.toDegrees(Math.acos(result.doubleValue()));

        } else {
            System.out.println("Incorrect. At least one vector is zero vector?");
            return -1;
        }
    }


    public static double[] normalization(Atom[] atoms, int index1, int index2) {
        double vectorLength = bondLength(atoms, index1, index2);
        double[] vector = coordinates(atoms, index1, index2);
        double[] vectorNormalized = new double[3];

        for (int i = 0; i < 3; i++) {
            vectorNormalized[i] = vector[i] / vectorLength;
        }

        return vectorNormalized;
    }


    public static double[] normalization(double[] vector) {
        double vectorLength = bondLength(vector);

        double[] vectorNormalized = new double[3];

        if (vectorLength != 0) {

            for (int i = 0; i < 3; i++) {
                vectorNormalized[i] = vector[i] / vectorLength;
            }

            return vectorNormalized;

        } else {

            return createZeroVector(3);

        }

    }


    public static double[] ortogonalizationInPlane(Atom[] atoms, int index1, int index2, int index3) {
        double[] vector21 = coordinates(atoms, index2, index1);
        double[] vector32 = coordinates(atoms, index3, index2);

        double[] vecRef = vectorProduct(vector21, vector32);
        double[] vecRefNormalized = normalization(vecRef);
        double[] vector2Normalized = normalization(vector21);


        if (bondLength(vecRefNormalized) != 0 && bondLength(vector2Normalized) != 0) {

            double[] vectorOrtogonal = new double[3];

            vectorOrtogonal[0] = (vector2Normalized[2] * vecRefNormalized[1] - vector2Normalized[1] * vecRefNormalized[2]) / (Math.pow(vector2Normalized[0], 2) + Math.pow(vector2Normalized[1], 2) + Math.pow(vector2Normalized[2], 2));
            vectorOrtogonal[1] = (vector2Normalized[0] * vecRefNormalized[2] - vector2Normalized[2] * vecRefNormalized[0]) / (Math.pow(vector2Normalized[0], 2) + Math.pow(vector2Normalized[1], 2) + Math.pow(vector2Normalized[2], 2));
            vectorOrtogonal[2] = (vector2Normalized[1] * vecRefNormalized[0] - vector2Normalized[0] * vecRefNormalized[1]) / (Math.pow(vector2Normalized[0], 2) + Math.pow(vector2Normalized[1], 2) + Math.pow(vector2Normalized[2], 2));

            return vectorOrtogonal;
        } else {

            return createZeroVector(3);

        }
    }


    public static double[] ortogonalizationInPlane(JTable jtable, int index1, int index2, int index3) {
        double[] vector21 = coordinates(jtable, index2, index1);
        double[] vector32 = coordinates(jtable, index3, index2);

        double[] vecRef = vectorProduct(vector21, vector32);
        double[] vecRefNormalized = normalization(vecRef);
        double[] vector2Normalized = normalization(vector21);


        if (bondLength(vecRefNormalized) != 0 && bondLength(vector2Normalized) != 0) {


            double[] vectorOrtogonal = new double[3];

            vectorOrtogonal[0] = (vector2Normalized[2] * vecRefNormalized[1] - vector2Normalized[1] * vecRefNormalized[2]) / (Math.pow(vector2Normalized[0], 2) + Math.pow(vector2Normalized[1], 2) + Math.pow(vector2Normalized[2], 2));
            vectorOrtogonal[1] = (vector2Normalized[0] * vecRefNormalized[2] - vector2Normalized[2] * vecRefNormalized[0]) / (Math.pow(vector2Normalized[0], 2) + Math.pow(vector2Normalized[1], 2) + Math.pow(vector2Normalized[2], 2));
            vectorOrtogonal[2] = (vector2Normalized[1] * vecRefNormalized[0] - vector2Normalized[0] * vecRefNormalized[1]) / (Math.pow(vector2Normalized[0], 2) + Math.pow(vector2Normalized[1], 2) + Math.pow(vector2Normalized[2], 2));

            return vectorOrtogonal;

        } else {

            return createZeroVector(3);

        }
    }


    public static double scalarProduct(double[] vector1, double[] vector2) {

        BigDecimal[] bdvector1 = new BigDecimal[3];
        BigDecimal[] bdvector2 = new BigDecimal[3];

        for (int i = 0; i < vector1.length; i++) {

            bdvector1[i] = new BigDecimal(Double.toString(vector1[i]));
            bdvector2[i] = new BigDecimal(Double.toString(vector2[i]));

        }


        BigDecimal scalar = ((bdvector1[0].multiply(bdvector2[0])).add((bdvector1[1].multiply(bdvector2[1])))).add((bdvector1[2].multiply(bdvector2[2])));

        return scalar.setScale(3, RoundingMode.HALF_UP).doubleValue();


    }


    public static double[] vectorProduct(double[] vector1, double[] vector2) {

        double[] vector = new double[3];
        vector[0] = vector1[1] * vector2[2] - vector1[2] * vector2[1];
        vector[1] = vector1[2] * vector2[0] - vector1[0] * vector2[2];
        vector[2] = vector1[0] * vector2[1] - vector1[1] * vector2[0];

        return vector;
    }


    public static double torsion(Atom[] atoms) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the four atoms in order of A B C D to calculate the torsion angle between planes A-B-C and B-C-D");
        int index1 = scanner.nextInt();
        int index2 = scanner.nextInt();
        int index3 = scanner.nextInt();
        int index4 = scanner.nextInt();

        double[] vectorTor1 = ortogonalizationInPlane(atoms, index3, index2, index1);
        double[] vectorTor2 = ortogonalizationInPlane(atoms, index2, index3, index4);

        return angle(vectorTor1, vectorTor2);
    }


    public static double torsion(JTable jtable, int[] index) {
        double[] vectorTor1 = ortogonalizationInPlane(jtable, index[2], index[1], index[0]);
        double[] vectorTor2 = ortogonalizationInPlane(jtable, index[1], index[2], index[3]);

        return angle(vectorTor1, vectorTor2);
    }


}






