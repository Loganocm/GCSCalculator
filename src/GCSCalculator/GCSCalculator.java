package GCSCalculator;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GCSCalculator {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String fileName = checkArgs(args);  // <== asks user/takes cmd line arguments for the name of the Pham table file
        File file = getFile(fileName);
        String[][] phamTable = readFile(file);
        System.out.println("Enter '1' to Calculate between two specific phages or enter '2' to compare all phages (Results found in Results.txt)");
        int input = scan.nextInt();
        scan.nextLine();
        if(input == 1) {
            System.out.println("Enter The name of the First Phage (Must be exact!)");
            String phageA = scan.nextLine();
            System.out.println("Enter The name of the Second Phage (Must be exact!)");
            String phageB = scan.nextLine();
            double GCSValue = GCSCalc(phamTable, phageA, phageB);
            System.out.printf("The Gene Content Similarity score between " + phageA + " and " + phageB + " is " + "%.2f%%\n", GCSValue);
        }
        else if(input == 2)
             calcAll(phamTable);
        else
            throw new IllegalArgumentException("Input must be either 1 or 2");

    }

    public static String checkArgs(String[] argv) {
        String fileName;
        if(argv.length > 0){
            fileName = argv[0];
            return fileName;
        }
        else{
            fileName = promptForFileName();
            return fileName;
        }

    }
    public static String promptForFileName() {
        System.out.println("Enter the file name of your Pham table: ");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();
        return fileName;
    }
    public static String promptForPhageName() {
        System.out.println("Enter the name of one of your phages (exactly from the file): ");
        Scanner scan = new Scanner(System.in);
        String phageName = scan.nextLine();
        scan.close();
        return phageName;
    }
    public static File getFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        return file;
    }
    public static String[][] readFile(File file) throws IOException {
        Scanner scan = new Scanner(file);
        ArrayList<String> temp1 = new ArrayList<>();
        while (scan.hasNext()) {
            temp1.add(scan.nextLine());
        }
        scan.close();
        String[] temp2 = temp1.get(0).split(",");
        String[][] phamTable = new String[temp1.size()][temp2.length];
        for(int i = 0; i < temp1.size(); i++){
            phamTable[i] = temp1.get(i).split(",");
        }
        return phamTable;
    }
    // takes in a pham table and two strings of pham names to compare gene content
    public static double GCSCalc(String[][] phamTable, String phamA, String phamB){
        double totalA = 0;
        double totalB = 0;
        double sharedPhams = 0;
        for(int i = 1; i < phamTable.length; i++){
            double temp1 = totalA;
            double temp2 = totalB;
            for(int j = 0; j < phamTable[i].length; j++){
                if(phamTable[i][j].contains(phamA))
                    totalA++;
                if(phamTable[i][j].contains(phamB))
                    totalB++;
            }
            if((totalA == temp1 + 1) && (totalB == temp2 + 1))
                sharedPhams++;
        }
        //System.out.println("TotalA: " + totalA + ", Total B: " + totalB + ", Shared Phams: " + sharedPhams);
        if(sharedPhams == 0.0)
            return 0.00;
        else
            return 100.00 * (((sharedPhams/totalA) + (sharedPhams/totalB))/2.0);
    }
    public static void calcAll(String[][] phamTable) throws IOException {
        File output = new File("Results.txt");
        FileWriter fw = new FileWriter(output);
        PrintWriter pw = new PrintWriter(fw);
        pw.println("Gene Content Similarity Score Calculator");
        for(int i = 3; i < phamTable[0].length; i++){
            for(int j = i+1; j < phamTable[0].length; j++){
                double GCSValue = GCSCalc(phamTable, phamTable[0][i], phamTable[0][j]);
                pw.printf(phamTable[0][i] + " and " + phamTable[0][j] + ": " +  "%.4f%%\n", GCSValue);
            }
        }
        pw.close();
    }
}

