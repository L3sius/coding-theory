package com.company;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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
    public static int mGlobal;
    public static int rGlobal; // r <= m

    public static String[] inputData = new String[4];
    public static Integer[] splitData = new Integer[4];
    public static String[] H;
    public static Integer[][] matrix;
    public static int[] possibleCombinations;
    public static Integer[][] temporaryMatrix;
    public static int counter = 0;
    public static int columns = 0;
    public static int combinationAmount = 0;
    public static String scenario;
    public static Integer[] encodedVector;
    public static int rows = 0;
    public static int[][] allCombinations;
    public static int[][] lValues;
    public static int wSize;

    public static void main(String[] args) throws IOException {

        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        // Reading data using readLine
        System.out.println("Enter which scenario to use");
        //TODO: Uncomment this later
        //String scenario = reader.readLine();
        scenario = "1";
        // Printing the read line

        switch (scenario) {
            case "1":
                System.out.println("scenario 1");
                readFile();
                generateMatrix((splitData[0]), splitData[1]);
                encodedVector = encode(matrix, inputData[3]);
                sendThroughTunnel(encodedVector, splitData[2]);
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

    private static void sendThroughTunnel(Integer[] encodedVector, int chance) {
        Random r = new Random();
        for (int i = 0; i < encodedVector.length; i++) {
            int chanceToSucceed = r.nextInt(100) + 1;
            if (chance > chanceToSucceed) {
                System.out.println("Chance was: " + chance + ", chanceToSucceed rolled: " + chanceToSucceed + ", digit changed: " + (i + 1));
                switch (encodedVector[i]) {
                    case 0:
                        encodedVector[i] = 1;
                        break;
                    case 1:
                        encodedVector[i] = 0;
                        break;
                    default:
                        System.out.println("Error encountered");
                        System.exit(0);
                }
            }
        }
        System.out.println("Encoded vector after sending: ");
        for (int i = 0; i < encodedVector.length; i++) {
            System.out.print(encodedVector[i]);
        }
        System.out.println();
        //TODO: Do you want to change it?
//        System.out.println("Decoded vector");
        System.out.println(decode(mGlobal, rGlobal, encodedVector, allCombinations));
    }

    private static String decode(int m, int r, Integer[] encodedVector, int[][] allCombinations) {
        String encodedInformation = arrayToString(encodedVector);
        wSize = (int)Math.pow(2, mGlobal-rGlobal);
        System.out.println("wSize is: " + wSize);
//        System.out.println("Encoded information: " + encodedInformation);

        int[] decodedV = new int[rows];

        int currRow = rows;

        //prasideda decodavimas
        StringBuilder sb = new StringBuilder(encodedInformation);

        //sukam su kiekvienu r kombinaciju t.y. pirma su pvz v1*v2*v3, tada su v1*v2, tada su v1*v3 ir t.t.
        for (int i = r; i >= 0; i--) {
            int numberOfCombinations = combinations(m, i);
            currRow -= numberOfCombinations;
            System.out.println("number of combinations is: " + numberOfCombinations);
            System.out.println("m is: " + m + " i is: " + i + " m-i is:" + (m - i));


            ArrayList<String> tValues = new ArrayList<>();
            ArrayList<String> wValues = new ArrayList<>();

            // Create tValues array depending on iteration of m-i
            //TODO: Redo populateH function
            String binaryValue;
            for (int t = 0; t < Math.pow(2, m - i); t++) {
                binaryValue = Integer.toBinaryString(t);
                //Uncomment this to see the binary value itself
                //System.out.println("This is the binary value:" + binaryValue);
                //If length is smaller than m (for example binary value is 10, but m is 3, then add an 0 so it becomes 010.
                while (binaryValue.length() < m - i) {
                    binaryValue = '0' + binaryValue;
                }
                tValues.add(binaryValue);

            }
            for (int z = 0; z < tValues.size(); z++) {
                System.out.print(tValues.get(z) + " ");
            }
            System.out.println();

            // Create lValues matrix depending on allCombinations
            // Finish populating allCombinations
            for (int j = 1; j <= mGlobal; j++) {
                allCombinations[j][0] = j;
            }
            // Create lValues matrix
            var counter = 0;
            for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
                HashSet<Integer> set = new HashSet();
                for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
                    set.add(allCombinations[rowIndex][columnIndex]);
                }
                for (int possibleCombination : possibleCombinations) {
                    if (!set.contains(possibleCombination)) {
                        lValues[rowIndex][counter] = possibleCombination;
                        counter++;
                    }
                }
                counter = 0;
            }

            for (int j = 0; j < numberOfCombinations; j++) {

                //String specificColumn = filteredMatrix.get(currRow+j);
                //TODO: ?
                System.out.println("currRow+j: " + (currRow + j));
//                System.out.println("These are l values");
//                for (int z = 0; z < lValues.size(); z++) {
//                    System.out.print(lValues.get(z) + " ");
//                }
//                System.out.println();
            }


            // Create wValues array depending on tValues
            for (int z = 0; z < tValues.size(); z++) {
                // TODO: Kiek {0,1} tiek iteracijų, dabar 2
            }
        }
        // System.out.println("This is matrix containing all combinations");
        // for (int z = 0; z < Main.allCombinations.length; z++) {
        //  for (int j = 0; j < Main.allCombinations[z].length; j++) {
        //      System.out.print(Main.allCombinations[z][j] + " ");
        //  }
        //  System.out.println();
        //}
        System.out.println("This is matrix containing lValues");
        for (int z = 0; z < Main.lValues.length; z++) {
            for (int j = 0; j < Main.lValues[z].length; j++) {
                System.out.print(Main.lValues[z][j] + " ");
            }
            System.out.println();
        }
        return null;
    }

    private static String arrayToString(Integer[] encodedVector) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < encodedVector.length; i++) {
            stringBuilder.append(encodedVector[i]);
        }
        return stringBuilder.toString();
    }

    private static Integer[] encode(Integer[][] matrix, String inputVector) {
        Integer[] encodedDigits = new Integer[columns];
        // Populate array with 0's
        Arrays.fill(encodedDigits, 0);
        // Convert input to an array of elements (digits)
        int[] digits = (inputVector).chars().map(c -> c - '0').toArray();

        for (int i = 0; i < (inputVector).length(); i++) {
            for (int j = 0; j < columns; j++) {
                // Start encoding with the generated matrix
                encodedDigits[j] += (matrix[i][j] * digits[i]);
                // Do mod2 operation
                if ((encodedDigits[j] % 2) == 0) {
                    encodedDigits[j] = 0;
                } else {
                    encodedDigits[j] = 1;
                }
            }
        }
        System.out.println("Input vector after encoding: ");
        for (int i = 0; i < encodedDigits.length; i++) {
            System.out.print(encodedDigits[i]);
        }
        System.out.println();
        return encodedDigits;
    }

    private static void generateMatrix(int m, int r) {
        mGlobal = m;
        rGlobal = r;

        System.out.println("m = " + inputData[0] + " r = " + inputData[1]);
        rows = calculateRows(m, r);

        //For scenario 1
        System.out.println((inputData[3]) + " value");
        if (rows != String.valueOf(inputData[3]).length() && scenario.equals("1")) {
            System.out.println("Incorrect value, input should be the size of: " + rows);
            System.out.println("Value provided: " + inputData[3] + " which is size of: " + String.valueOf(inputData[3]).length());
            System.exit(0);
        } else {
            System.out.println("Chance to fail: " + splitData[2]);
            System.out.println("Input to send: " + inputData[3]);
        }

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
                allCombinations = new int[rows][columns];
                lValues = new int[rows][columns];
                temporaryMatrix = new Integer[combinationAmount][columns];
                //Prepare lValues
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++)
                        lValues[i][j] = 0;
                }
                //Prepare temporary Matrix
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
                // System.out.println();
                for (int k = 0; k < columns; k++) {
                    temporaryMatrix[counter][k] *= matrix[data[j]][k];
                }
            }
            for (int i = 0; i < data.length; i++) {
                allCombinations[counter + mGlobal + 1][i] = data[i];
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
        System.out.println("Total combinations amount is: " + combinationAmount);
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
            if (splitData[2] > 100 || splitData[2] < 0) {
                System.out.println("Chance to fail should be between 0-100");
                System.exit(0);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    private static void convertToInt(String[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            splitData[i] = Integer.parseInt(data[i]);
        }
    }

}


