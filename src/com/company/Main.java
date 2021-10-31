package com.company;

import java.io.*;
import java.util.Scanner;

public class Main {

    // Tiesinis kodas - užkoduojame pradinę informacijos ilgį k daugindami iš generuojančios matricos (Vektoriaus ir matricos daugyba)

    // Pavyzdys RM(3,2)
    // v0 vienetų vektorius
    // vi = v(Hi) -> vektorius atitinkantis aibę Hi
    // RM(m,r) -> RM(3,2) -> 3(m) nurodo kiek bus v1,v2,v3... -> 2(r) nurodo kokias kombinacijas imti. Tai bus visos įmanomos sandaugos
    // Jeigu 2(m) tai trumpieji vektoriukai bus ilgio 2 -> {00 01 10 11}
    // Jeigu r būtų 3 - tai atsirastų dar papildomai v1 * v2 * v3 ir t.t
    // Jeigu r būtų 1 - tai nebūtų jokių sandaugų, tik v1,v2 ir t.t. Jeigu r būtų 0 - būtų tik v0 vektorius.
    // Kad gauti v1 * v2 tai sudauginti reikia eilutes v1 * v2, kur 1 * 1 = 1, o 1 * 0 = 0 bei 0 * 0 = 0
    // Kadangi dar m = 3, tai -> 000 001 010 011 100 101 110 111 imsime visas triženkles įmanomas reikšmes.
    // Kodėl v3 yra toks?         1   0   1   0   1   0   1   0 Nes imame pagal pozicijas - 3 pozicijoje turi būti "0", tada pats vektorius bus 1.
    // Hi tai tokia aibė, kuri sudaryta iš vektorių kurių i-toji koordinatė yra lygi 0, tai pavyzdžiui H1 bus sudaryta iš {000,001,010,011}

    //Užkodavimas pavyzdžiui RM(3,2)
    //Jeigu 1(r) tai užkoduosime žodį ilgio 4, jeigu 2(r) -> 7, jeigu 0(r) -> ilgio 1 vektorius, ilgio 8 vektoriais.
    //Kai dauginsime ilgio pavyzdžiui ateinantį informacijos vektorių ilgio 7, tai tokiu atveju pirmą koordinatę jo dauginsime iš v0, antrą koordinatę iš v1 ir t.t
    //Tada viską susumuosime ir tai ir bus mūsų užkoduotas vektorius.

    // Generuojanti matrica
    //          a1  a2  a3  a4  a5  a6  a7  a8
    //          000 001 010 011 100 101 110 111
    // v0        1   1   1   1   1   1   1   1
    // v1        1   1   1   1   0   0   0   0
    // v2        1   1   0   0   1   1   0   0
    // v3        1   0   1   0   1   0   1   0
    // v1 * v2   1   1   0   0   0   0   0   0
    // v1 * v3   1   0   1   0   0   0   0   0
    // v2 * v3   1   0   0   0   1   0   0   0

    // Pradinis informacijos ilgis
    int k; // = 1 + C^1_m + ... C^2_m

    // Ilgesnio užkoduoto vektoriaus ilgis 2^m
    int n; // = 2^m čia turbūt yra tie poaibiai 000 001 010 ....

    // Nusako koks bus pradinės informacijos ilgis, ir užkoduoto vektoriaus ilgis
    int m;
    int r; // r <= m

    public static String[] inputData = new String[4];
    public static Integer[] splitData = new Integer[4];
    public static String[] H;
    public static Integer[][] matrix;
    public static int[] possibleCombinations;
    public static Integer[][] temporaryMatrix;
    public static int counter = 0;
    public static int columns = 0;
    public static int combinationAmount = 0;

    public static void main(String[] args) throws IOException {

        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        // Reading data using readLine
        System.out.println("Enter which scenario to use");
        //TODO: Uncomment this later
        //String scenario = reader.readLine();
        String scenario = "1";
        // Printing the read line

        switch (scenario) {
            case "1":
                System.out.println("scenario 1");
                readFile();
                System.out.println("m = " + inputData[0] + " r = " + inputData[1]);
                generateMatrix(splitData[0], splitData[1]);
                break;
            case "2":
                System.out.println("scenario 2");
                break;
            case "3":
                System.out.println("scenario 3");
                break;
            default:
                System.out.println("Incorrect scenario");
                break;

        }


    }

    private static void generateMatrix(int m, int r) {
        int rows = calculateRows(m, r);
        columns = (int) Math.pow(2, m);
        System.out.println("Total rows: " + rows + " Total columns: " + columns);
        //If m < r
        populateH(m, columns);

        int rowCounter = 0;
        //Determine matrix size
        matrix = new Integer[rows][columns];

        //Add v0 vector
        for (int i = 0; i < columns; i++) {
            matrix[0][i] = 1;
        }
        rowCounter++;

        //Add v1,v2,...,vm
        if (r >= 1) {
            for (int i = 1; i <= m; i++) {
                for (int j = 0; j < columns; j++) {
                    //startsWith returns true if the String begins with "0", it starts looking from the specified index
                    if (H[j].startsWith("0", i - 1)) {
                        matrix[i][j] = 1;
                    } else {
                        matrix[i][j] = 0;
                    }
                }
                rowCounter++;
            }
            //Add combinations
            if (r >= 2) {
                //Create temporary Matrix
                temporaryMatrix = new Integer[combinationAmount][columns];
                //Prepare temporary Array
                for (int i = 0; i < combinationAmount; i++) {
                    for (int j = 0; j < columns; j++)
                        temporaryMatrix[i][j] = 1;
                }
                possibleCombinations = new int[m];
                for (int i = 0; i < m; i++) {
                    possibleCombinations[i] = i + 1;
                }
                for (int i = 2; i <= r; i++) {
                    printCombination(possibleCombinations, possibleCombinations.length, i);
                }
                for (int i = 0; i < temporaryMatrix.length; i++) {
                    for (int j = 0; j < columns; j++) {
                        matrix[rowCounter][j] = temporaryMatrix[i][j];
                    }
                    rowCounter++;
                }
            }
        }
        //Print matrix
        printMatrix(matrix);
    }

    private static void printCombination(int[] arr, int n, int r) {
        // A temporary array to store all combination one by one
        int data[] = new int[r];


        // Print all combination using temporary array 'data[]'
        combinationUtil(arr, data, 0, n - 1, 0, r);
    }

    private static void combinationUtil(int arr[], int data[], int start,
                                        int end, int index, int r) {

        // Current combination is ready to be printed, print it
        if (index == r) {
            for (int j = 0; j < r; j++) {
            // System.out.print(data[j] + " ");
                for (int k = 0; k < columns; k++) {
                    temporaryMatrix[counter][k] *= matrix[data[j]][k];
                }
            }
            counter += 1;
            // System.out.println("");
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions

        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data[index] = arr[i];

            combinationUtil(arr, data, i + 1, end, index + 1, r);
        }
    }

    private static void printMatrix(Integer[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void populateH(int m, int columns) {
        //Populate H (with vectors, which are {00, 01, 10, 11} and etc.)
        H = new String[columns];

        String binaryValue;
        for (int i = 0; i < H.length; i++) {
            binaryValue = Integer.toBinaryString(i);
            //Uncomment this to see the binary value itself
            //System.out.println("This is the binary value:" + binaryValue);
            //If length is smaller than m (for example binary value is 10, but m is 3, then add an 0 so it becomes 010.
            while (binaryValue.length() < m) {
                binaryValue = '0' + binaryValue;
            }
            H[i] = binaryValue;

        }
        for (int i = 0; i < H.length; i++) {
            System.out.print(H[i] + " ");
        }
        System.out.println();
    }

    private static Integer calculateRows(int m, int r) {
        Integer amount = 0;
        //Start by adding v0
        amount += 1;
        //Add starting vectors v1,v2,...,vm
        if (r > 0)
            amount += m;
        //Add all the combinations, starting from v1*v2, ...
        for (int i = 2; i <= r; i++) {
            amount += combinations(m, i);
            combinationAmount += combinations(m, i);
        }
        return amount;
    }

    private static Integer combinations(int m, int i) {
        //To calculate combinations, I'll use the formula nCr = n! / i! * (n - r)!, where n represents the number of items, and r represents the number of items being chosen at a time.
        return (calculateFactorial(m) / (calculateFactorial(i) * calculateFactorial(m - i)));
    }

    public static int calculateFactorial(int number) {
        int fact = 1;
        int i = 1;
        while (i <= number) {
            fact *= i;
            i++;
        }
        return fact;
    }

    private static void readFile() {
        //TODO: Logic for other scenarios
        try {
            File myObj = new File("src/com/company/part1.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                inputData = data.split(" ");
            }
            convertToInt(inputData);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    private static void convertToInt(String[] data) {
        for (int i = 0; i < data.length; i++) {
            splitData[i] = Integer.parseInt(data[i]);
        }
    }

}


