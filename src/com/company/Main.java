package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

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
    public static int[] votingWinner;
    public static Integer[] encodedVectorUpdated;
    public static Integer[] rowSum;

    // Variables for second task
    public static String textInput;
    public static String[] preparedTextInput;
    public static String[] preparedTextInputNoCode;
    public static String textAfterDecoding = "";
    public static String noCodeText = "";

    // Variables for third task
    public static BufferedImage image;
    public static String imageInput;
    public static String[] preparedImageInput;

    public static void main(String[] args) throws IOException {

        final long startTime = System.currentTimeMillis();
        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        // Reading data using readLine
        System.out.println("Enter which scenario to use");
        scenario = reader.readLine();
        // Printing the read line
//        scenario = "2";


        switch (scenario) {
            case "1":
                System.out.println("scenario 1");
                readFile(scenario);
                generateMatrix((splitData[0]), splitData[1]);
                encodedVector = encode(matrix, inputData[3]);
                encodedVectorUpdated = encodedVector;
                System.out.println(sendThroughTunnel(encodedVector, splitData[2]));
                break;
            case "2":
                System.out.println("scenario 2");
                readFile(scenario);
                generateMatrix((splitData[0]), splitData[1]);

                preparedTextInput = prepareVectorForEncoding(textInput);
                preparedTextInputNoCode = preparedTextInput;
                System.out.println("this is the prepared text input " + preparedTextInput[0]);
                System.out.println("first char" +  preparedTextInput[0].charAt(0));
                for (int i = 0; i < preparedTextInput.length; i++) {
                    encodedVector = encode(matrix, preparedTextInput[i]);
                    encodedVectorUpdated = encodedVector;
                    textAfterDecoding += sendThroughTunnel(encodedVector, splitData[2]);
                    noCodeText += sendThroughTunnelNoCode(preparedTextInput[i], splitData[2]);
                }
                System.out.println("Converted back to text without using code: ");
                System.out.println(convertBinaryToString(prettyBinary(noCodeText, 8, " ")));
//                System.out.println("This is the final text");
//                System.out.println(textAfterDecoding);
//                System.out.println(prettyBinary(textAfterDecoding, 8, " "));
                System.out.println("Converted back to text after decoding: ");
                System.out.println(convertBinaryToString(prettyBinary(textAfterDecoding, 8, " ")));
                break;
            case "3":
                System.out.println("scenario 3");
                readFile(scenario);
                generateMatrix((splitData[0]), splitData[1]);

                preparedImageInput = prepareVectorForEncoding(imageInput);
                convertBinaryToImage(imageInput, "Notcoded");
                for (int i = 0; i < preparedImageInput.length; i++) {
                    encodedVector = encode(matrix, preparedImageInput[i]);
                    encodedVectorUpdated = encodedVector;
                    textAfterDecoding += sendThroughTunnel(encodedVector, splitData[2]);
                }
                convertBinaryToImage(textAfterDecoding, "coded");
                break;
            default:
                System.out.println("Incorrect scenario");
                break;
        }

        final long elapsedTimeMillis = System.currentTimeMillis() - startTime;
        long minutes = (elapsedTimeMillis / 1000) / 60;
        long seconds = (elapsedTimeMillis / 1000) % 60;

//        System.out.println("Input text size: " + textInput.length());
//        System.out.println(elapsedTimeMillis + " Milliseconds = "
//                + minutes + " minutes and "
//                + seconds + " seconds.");
//        System.out.println("Elapsed time:" + elapsedTimeMillis);
    }

    private static void convertBinaryToImage(String imageInput, String name) throws IOException {
        byte[] img = new BigInteger(imageInput, 2).toByteArray();

        ByteArrayInputStream bis = new ByteArrayInputStream(img);
        BufferedImage bImage2 = ImageIO.read(bis);
        ImageIO.write(bImage2, "bmp", new File(name + "output.bmp"));
        System.out.println("image created");
    }

    private static String[] prepareVectorForEncoding(String textInput) {

        System.out.println("Text converted to binary:");
        String convertedBinary = convertStringToBinary(textInput);
        System.out.println(convertedBinary);
        System.out.println(prettyBinary(convertedBinary, 8, " "));
//        System.out.println("Converted back to text: " + convertBinaryToString(prettyBinary(convertedBinary, 8, " ")));


        String[] encodedVectorList;
        boolean isDividable = true;
        if (convertedBinary.length() % rows == 0) {
            System.out.println("Text input length: " + convertedBinary.length());
            System.out.println("rows: " + rows);
            System.out.println("lenght/columns: " + convertedBinary.length() / rows);
            encodedVectorList = new String[convertedBinary.length() / rows];
        } else {
            System.out.println("Text input length: " + convertedBinary.length());
            System.out.println("rows: " + rows);
            System.out.println("lenght/columns + 1: " + (convertedBinary.length() / rows + 1));
            encodedVectorList = new String[convertedBinary.length() / rows + 1];
            isDividable = false;
        }

        var tempText = "";
        var counter = 0;
        var index = 0;
        for (int stringIndex = 0; stringIndex < convertedBinary.length(); stringIndex++) {
            tempText += convertedBinary.toCharArray()[stringIndex];
            counter++;
            if (counter == rows) {
                encodedVectorList[index] = tempText;
                index++;
                counter = 0;
                tempText = "";
            }
        }
        if (!isDividable) {
            System.out.println("Was not dividable, filling rest with 0's");
            encodedVectorList[index] = tempText;
            for (int charIndex = counter; charIndex < rows; charIndex++) {
                encodedVectorList[index] += "0";
            }
        }


        // Print the encodedVectorList
        System.out.println("The encoded vector list");
        for (int i = 0; i < encodedVectorList.length; i++) {
            System.out.print(encodedVectorList[i] + " ");
        }
        System.out.println();
        return encodedVectorList;
    }

    public static String convertStringToBinary(String input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0")                         // zero pads
            );
        }
        return result.toString();
    }

    public static String convertBinaryToString(String input) {

        String raw = Arrays.stream(input.split(" "))
                .map(binary -> Integer.parseInt(binary, 2))
                .map(Character::toString)
                .collect(Collectors.joining()); // cut the space

        return raw;
    }

    public static String prettyBinary(String binary, int blockSize, String separator) {

        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < binary.length()) {
            result.add(binary.substring(index, Math.min(index + blockSize, binary.length())));
            index += blockSize;
        }

        return result.stream().collect(Collectors.joining(separator));
    }

    private static String sendThroughTunnelNoCode(String encodedVectorNoCode, int chance) {
        Random r = new Random();
        int[] digits = (encodedVectorNoCode).chars().map(c -> c - '0').toArray();
        System.out.println();
        for (int i = 0; i < digits.length; i++) {
            int chanceToSucceed = r.nextInt(10000) + 1;
            if (chance > chanceToSucceed) {
                switch (digits[i]) {
                    case 0 -> digits[i] = 1;
                    case 1 -> digits[i] = 0;
                    default -> {
                        System.out.println("Error encountered");
                        System.exit(0);
                    }
                }
            }
        }
        StringBuilder newVector = new StringBuilder();
        for(int i = 0; i < digits.length; i++)
        {
            newVector.append(digits[i]);
        }
        return newVector.toString();
    }

    private static String sendThroughTunnel(Integer[] encodedVector, int chance) throws IOException {
        Random r = new Random();
        for (int i = 0; i < encodedVector.length; i++) {
            int chanceToSucceed = r.nextInt(10000) + 1;
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
        if (scenario.equals("1") && changeEncoded()) {
            System.out.println("Enter new encoded vector");
            Scanner in = new Scanner(System.in);
            String s = in.nextLine();
            if (s.length() != encodedVector.length) {
                System.out.println("Your new encoded vector should be of length = " + encodedVector.length);
                System.exit(1);
            } else {
                for (int i = 0; i < encodedVector.length; i++) {
                    encodedVector[i] = Character.getNumericValue(s.charAt(i));
                }
            }
            System.out.println("Your new encoded value is: ");
            for (int i = 0; i < encodedVector.length; i++) {
                System.out.print(encodedVector[i]);
            }
            System.out.println();
        }
        return decode(mGlobal, rGlobal, encodedVector, allCombinations);
    }

    private static boolean changeEncoded() throws IOException {
        System.out.println("Do you wish to change encoded vector? y/n");
        // Enter data using BufferReader
        Scanner in = new Scanner(System.in);
        // Reading data using readLine
        String value = in.nextLine();
        if (value.equals("y")) {
            return true;
        } else if (value.equals("n")) {
            return false;
        } else {
            System.out.println("Incorrect value entered, exiting program");
            System.exit(1);
            return false;
        }
    }

    private static String decode(int m, int r, Integer[] encodedVector, int[][] allCombinations) {
        votingWinner = new int[rows];
        int currRow = rows;
        rowSum = new Integer[columns];


        //Cycle with each combination, example: v1*v2*v3 -> v1*v2 -> v1*v3 and so on.
        for (int i = r; i >= 0; i--) {
            int numberOfCombinations = combinations(m, i);
            currRow -= numberOfCombinations;
            //System.out.println("number of combinations is: " + numberOfCombinations);

            wSize = (int) Math.pow(2, m - i);
            for (int HIndex = 0; HIndex < H.length; HIndex++) {
                System.out.print(H[HIndex] + " ");
            }
            System.out.println();
            ArrayList<String> tValues = new ArrayList<>();

            // Create tValues array depending on iteration of m-i
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

            // Create lValues matrix depending on allCombinations
            // Finish populating allCombinations
            for (int j = 1; j <= mGlobal; j++) {
                allCombinations[j][0] = j;

            }
            // System.out.println("Printed allCombinations");
            // for (int z = 0; z < allCombinations.length; z++) {
            //    for (int j = 0; j < allCombinations[z].length; j++) {
            //        System.out.print(allCombinations[z][j] + " ");
            //    }
            //    System.out.println();
            // }

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

            System.out.println("Printed possibleCombinations");
            for (int z = 0; z < possibleCombinations.length; z++) {
                System.out.print(possibleCombinations[z] + " ");
            }
            System.out.println();
            System.out.println("Printed lValues Matrix");
            for (int z = 0; z < lValues.length; z++) {
                for (int j = 0; j < lValues[z].length; j++) {
                    System.out.print(lValues[z][j] + " ");
                }
                System.out.println();
            }

            // Create wValues list
            // Reset rowSum
            Arrays.fill(rowSum, 0);
            // Print encoded vector
            System.out.println("Currently used encoded vector:");
            for (int rowIndex = 0; rowIndex < encodedVectorUpdated.length; rowIndex++) {
                System.out.print(encodedVectorUpdated[rowIndex]);
            }
            System.out.println();
            for (int j = 0; j < numberOfCombinations; j++) {

                System.out.println("currRow+j: " + (currRow + j));
                for (int z = 0; z < tValues.size(); z++) {
                    System.out.print(tValues.get(z) + " ");
                }
                System.out.println();
                String[] wValues = new String[wSize];
                int[] votingResult = new int[wSize];
                for (int wIndex = 0; wIndex < wValues.length; wIndex++) {
                    String wString = "";
                    for (int HIndex = 0; HIndex < H.length; HIndex++) {
                        String tempText = "";
                        for (int columnIndex = 0; columnIndex < lValues.length && lValues[currRow + j][columnIndex] != 0; columnIndex++) {
                            tempText += H[HIndex].toCharArray()[lValues[currRow + j][columnIndex] - 1];
                        }
                        if (tempText.equals(tValues.get(wIndex))) {
                            wString += "1";
                        } else {
                            wString += "0";
                        }
                    }
                    if (wString.matches("^[0]+$") && rGlobal == mGlobal) {
                        String tempText = "";
                        for (int charIndex = 0; charIndex < wString.length(); charIndex++) {
                            tempText += "1";
                        }
                        wValues[wIndex] = tempText;
                    } else wValues[wIndex] = wString;
                    System.out.println("w" + wIndex + "=" + wValues[wIndex]);
                    // Scaliar product (c, w0) ...
                    // Go through each char
                    for (int charIndex = 0; charIndex < encodedVectorUpdated.length; charIndex++)
                        votingResult[wIndex] += encodedVectorUpdated[charIndex] * wValues[wIndex].toCharArray()[charIndex];
                    if ((votingResult[wIndex] % 2) == 0) {
                        votingResult[wIndex] = 0;
                    } else {
                        votingResult[wIndex] = 1;
                    }

                }

                // Scaliar product (c, w0) ...
                for (int votingResultIndex = 0; votingResultIndex < votingResult.length; votingResultIndex++) {
                    System.out.println("(c,w" + votingResultIndex + ") = " + votingResult[votingResultIndex]);
                }
                // Majority logic decoding
                votingWinner[currRow + j] = mostFrequent(votingResult, votingResult.length);
                System.out.println("Winner is: " + votingWinner[currRow + j]);

                //Sum all necessary lines
                for (int columnIndex = 0; columnIndex < rowSum.length; columnIndex++) {
                    rowSum[columnIndex] += matrix[currRow + j][columnIndex] * votingWinner[currRow + j];
                    if ((rowSum[columnIndex] % 2) == 0) {
                        rowSum[columnIndex] = 0;
                    } else {
                        rowSum[columnIndex] = 1;
                    }
                }
                // Print rowsum
                // System.out.println("This is the partial row sum:");
                // for (int rowIndex = 0; rowIndex < rowSum.length; rowIndex++) {
                //    System.out.print(rowSum[rowIndex]);
                // }
                //System.out.println();

            }
            //Print rowsum
            System.out.println("This is the total row sum:");
            for (int rowIndex = 0; rowIndex < rowSum.length; rowIndex++) {
                System.out.print(rowSum[rowIndex]);
            }
            System.out.println();
            // Update the encoded vector
            for (int columnIndex = 0; columnIndex < rowSum.length; columnIndex++) {
                encodedVectorUpdated[columnIndex] += rowSum[columnIndex];
                if ((encodedVectorUpdated[columnIndex] % 2) == 0) {
                    encodedVectorUpdated[columnIndex] = 0;
                } else {
                    encodedVectorUpdated[columnIndex] = 1;
                }
            }

        }

        System.out.println("This is matrix containing lValues");
        for (int z = 0; z < Main.lValues.length; z++) {
            for (int j = 0; j < Main.lValues[z].length; j++) {
                System.out.print(Main.lValues[z][j] + " ");
            }
            System.out.println();
        }
        if (scenario.equals("1")) {
            System.out.println("Input value");
            System.out.println(inputData[3]);
        }
        System.out.println("Decoded vector");
        return arrayToString(Arrays.stream(votingWinner).boxed().toArray(Integer[]::new));
    }

    private static int mostFrequent(int arr[], int n) {

        // Insert all elements in hash
        Map<Integer, Integer> hp =
                new HashMap<Integer, Integer>();

        for (int i = 0; i < n; i++) {
            int key = arr[i];
            if (hp.containsKey(key)) {
                int freq = hp.get(key);
                freq++;
                hp.put(key, freq);
            } else {
                hp.put(key, 1);
            }
        }

        // find max frequency.
        int max_count = 0, res = -1;

        for (Map.Entry<Integer, Integer> val : hp.entrySet()) {
            if (max_count < val.getValue()) {
                res = val.getKey();
                max_count = val.getValue();
            }
        }

        return res;
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

        System.out.println("m = " + m + " r = " + r);
        rows = calculateRows(m, r);

        //For scenario 1
        if (scenario.equals("1") && rows != String.valueOf(inputData[3]).length()) {
            System.out.println("Incorrect value, input should be the size of: " + rows);
            System.out.println("Value provided: " + inputData[3] + " which is size of: " + String.valueOf(inputData[3]).length());
            System.exit(0);
        } else if (scenario.equals("1")) {
            System.out.println("Chance to fail: " + splitData[2] + " out of 10000 or " + Double.valueOf(splitData[2])/10000);
            System.out.println("Input to send: " + inputData[3]);
        }

        columns = (int) Math.pow(2, m);
        System.out.println("Total rows: " + rows + " Total columns: " + columns);

        populateH(m, columns);

        int rowCounter = 0;
        //Determine matrix size
        matrix = new Integer[rows][columns];

        //Add v0 vector
        for (int i = 0; i < columns; i++) {
            matrix[0][i] = 1;
        }
        rowCounter++;

        //Create temporary Matrix
        allCombinations = new int[rows][columns];
        lValues = new int[rows][columns];
        temporaryMatrix = new Integer[combinationAmount][columns];
        possibleCombinations = new int[m];

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
//            if (r >= 2) {

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
//            }
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

    private static void readFile(String scenario) throws IOException {
        switch (scenario) {
            case "1":
                try {
                    File myObj = new File("src/com/company/part1.txt");
                    Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        inputData = data.split(" ");
                    }
                    convertToInt(inputData);
                    if (splitData[2] > 10000 || splitData[2] < 0) {
                        System.out.println("Chance to fail should be between 0-10000");
                        System.exit(0);
                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("File not found.");
                    e.printStackTrace();
                }
                break;
            case "2":
                File file = new File("src/com/company/part2.txt");
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    var counter = 0;
                    while ((line = br.readLine()) != null) {
                        if (counter == 0) {
                            System.out.println(line);
                            inputData = line.split(" ");
                            for (int i = 0; i < inputData.length; i++) {
                                splitData[i] = parseInt(inputData[i]);
                            }
                            if (splitData[2] > 10000 || splitData[2] < 0) {
                                System.out.println("Chance to fail should be between 0-10000");
                                System.exit(0);
                            }
                            counter++;
                        } else {
                            System.out.println(line);
                            textInput = line;
                        }

                    }
                }
                break;
            case "3":
                try {
                    File myObj = new File("src/com/company/part3.txt");
                    Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        inputData = data.split(" ");
                    }
                    for (int i = 0; i < inputData.length; i++) {
                        splitData[i] = parseInt(inputData[i]);
                    }
                    if (splitData[2] > 10000 || splitData[2] < 0) {
                        System.out.println("Chance to fail should be between 0-10000");
                        System.exit(0);
                    }
                    myReader.close();
                    File myImage = new File("src/com/company/dog.bmp");
                    image = ImageIO.read(myImage);
                    System.out.println(image);
                    convertImageToBinary(image);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found.");
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Incorrect scenario");
                break;

        }

    }

    private static void convertImageToBinary(BufferedImage image) throws IOException {

        StringBuilder text = new StringBuilder();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "bmp", bos);

        byte[] bytes = bos.toByteArray();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            binary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        text.append(binary);

        imageInput = text.toString();
        System.out.println(imageInput);
    }

    private static void convertToInt(String[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            splitData[i] = parseInt(data[i]);
        }
    }

}


