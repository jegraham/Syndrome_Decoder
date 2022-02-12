package com.company;
    /**
     * @author Jessica L. Graham
     * @version 1.0
     * @since 2022-02-05
     *
     * Description: This code is intended for COSC 5P01 Coding Theory Assignmnet 1
     *              The goal is to decode y using the Syndrome Decoder  from the
     *              Parity Check Matrix (H).
     *
     *              Slide 7-12 Week 3 NOTES:
     *              - Generator --> Parity Matrix
     *              - Coset Leaders Full List (q^(n-k) x q^k))
     *              - Cosets Generated weight 0 till syndromes completely full q(n-k)
     *              - -- Check for unique syndromes, IF unique add to the syndrome list
     *              - DECODE
     *              - Look up y in syndrome table to get the coset leader (e)
     *              - x = y-e which
     *              - Print in lexicographical order.
     *
     *              Variables
     *              - n = length
     *              - k =
     *              - q =
     *
     */
    public class Syndrome_Decoder {

        // ------------------------------------- Initial Static Parameters -------------------------------------

        /* SLIDE EXAMPLE
        public static final int[][] H ={    {1, 1, 1, 0 },
                                            {0, 1, 0, 1}};

        public static final int q = 2;
        public static final int n = 4;
        public static final int k = 2;

        public static final int[] yValue = {0, 0, 1, 1};
        */


        /* Question 2 PART D
        public static final int[][] H ={    {1, 0, 0 ,1 ,   0, 0, 0, 0,   0, 0, 0, 0},
                                            {1, 0, 0 ,0 ,   1, 0, 0, 0,   0, 0, 0, 0},
                                            {0, 1, 0 ,0 ,   0, 1, 0, 0,   0, 0, 0, 0},
                                            {0, 1, 0 ,0 ,   0, 0, 1, 0,   0, 0, 0, 0},
                                            {0, 1, 0 ,0 ,   0, 0, 0, 1,   0, 0, 0, 0},
                                            {1, 0, 0 ,0 ,   0, 0, 0, 0,   1, 0, 0, 0},
                                            {0, 0, 1 ,0 ,   0, 0, 0, 0,   0, 1, 0, 0},
                                            {0, 0, 1 ,0 ,   0, 0, 0, 0,   0, 0, 1, 0},
                                            {0, 0, 1 ,0 ,   0, 0, 0, 0,   0, 0, 0, 1}};

        public static final int q = 2;
        public static final int n = 12;
        public static final int k = 3;

        public static final int[] yValue = {1, 0, 0, 0,  1, 0, 0, 0,  1, 0, 0, 0};
        */

         ///* Question 6 Part B & C
        public static final int[][] H ={    {0, 0, 1, 1, 1,  1, 0, 0, 0, 0},
                                            {0, 1, 0, 1, 0,  0, 1, 0, 0, 0},
                                            {0, 1, 1, 0, 1,  0, 0, 1, 0, 0},
                                            {1, 0, 0, 0, 0,  0, 0, 0, 1, 0},
                                            {1, 0, 0, 0, 0,  0, 0, 0, 0, 1}};

        public static final int q = 2;
        public static final int n = 10;
        public static final int k = 5;

        /*
        //PART I
        public static final int[] yValue = {0, 0, 1, 1, 1,  0, 0, 1, 0, 0};

        //PART II
        public static final int[] yValue = {0, 1, 1, 1, 1,  1, 0, 0, 1, 0};

        //PART III
        public static final int[] yValue = {0, 1, 1, 1, 1,  0, 0, 0, 0, 0};
         */


        public static final int[] yValue = {0, 1, 1, 1, 1,  0, 0, 0, 0, 0};



        //*/

        // ------------------------------------- Initial Static Parameters -------------------------------------

        public int power = (int) Math.pow(q, n-k); //This will hold the main power formula q to the power of n
        public int [][] syndromeTable = new int [n-k][power]; //This will be the list of all syndromes
        public int [][] cosetLeader = new int [(int)Math.pow(q,n)][n]; //This will be the list of generated codewords of length n.
        public int [][] cosetList = new int [power][n]; //This will be the list of coset leaders
        public int numberSyndromes = -1; //By default we will start at -1 and this will be the number of syndromes in the SyndromeTable list
        public int weight = 0; //By default this will start at 0 and be the current weight for the syndrome table.


        //This class will decode the syndrome and be the main class for this java program
        public Syndrome_Decoder (){

        CodewordGenerator(); //Generate the codewords for the coset leader table

            //Generate the syndromes
            while (numberSyndromes < (power - 1)){
                //int [] codeword = new int [cosetLeader.length];
                //System.out.println ("LEADER LENGTH " + cosetLeader.length);

                //Iterate through the words
                //for (int x = 0; x < cosetLeader.length; x++){
                for (int [] codeword: cosetLeader){
                    //If the codeword is the same weight and  need to generate more syndromes?
                    if (Weight(codeword, weight) && (numberSyndromes < (power - 1))){

                        // Multiply the Parity Check (H) and the possible error vector
                        int [] syndrome = CreateSyndrome(codeword);
                        // Compare to the syndromes in the table
                        boolean match = CompareSyndromes(syndrome);

                        //If there is no match in the syndrome table to the syndrome go to the next syndrome
                        //Add the syndrome to the table
                        if (match == false){
                            numberSyndromes++;

                            //Iterate through adding the syndromes to the table.
                            for (int d = 0; d < syndrome.length; d++){
                                syndromeTable[d][numberSyndromes] = syndrome[d];
                            }//End for loop

                            //Iterate through adding the codeword errors to the coset leader list
                            for (int e = 0; e < codeword.length; e++){
                                //System.out.println ("Codeword " + cosetLeadersList.length + " " + cosetLeadersList[numberSyndromes].length + " " + codeword.length);
                               cosetList[numberSyndromes][e] = codeword[e];
                            }//End For Loop
                        }//End Inner If
                    }//End If
                }//End For Loop

                weight++; // Increase to go to next level of weights
            }//End While Loop

            //Display Output
            System.out.println("Syndrome Table");      //----------------------------Good
            //PrintTable(syndromeTable);
            PrintTable(cosetList, syndromeTable);

            System.out.println("");
            System.out.println("");


            System.out.println("Syndrome Table of y"); //----------------------------Good
            int [] tempSyndrome = CreateSyndrome(yValue);
            PrintTable(tempSyndrome);//Print the Syndrome Table
            System.out.println("");

            System.out.println("");
            System.out.println("Coset Syndrome Lookup");//----------------------------Good
            int temp = getCoset(tempSyndrome);
            if (temp > -1) {
                PrintTable(cosetList[temp]); //Print the coset leader at the lookup row
            }//End If
            else{
                System.out.print ("No Match was found for the y: ");
                PrintTable(tempSyndrome);
            }
            System.out.println("");
            System.out.println("");

            System.out.print("Correct Codeword (y + e) = ");
            PrintTable(Decode(yValue,cosetList, temp));
            System.out.println("");
            System.out.println("");


        }//End Syndrome_Decoder

        //This method will generate possible codewords of length n into the array coset Leader list.
        private void CodewordGenerator (){
            //System.out.println ("Codeword Generator");
            String str = ""; // Temp String value
            int l = 0; // Temp length value

            //Convert and Iterate through the list of codewords proand add to the coset leader table.
            for (int i = 0; i < cosetLeader.length; i++) {
                str = Integer.toBinaryString(l);
                for (int j = 0; j < str.length(); j++) {
                    cosetLeader[i][cosetLeader[0].length - str.length() + j]= Integer.parseInt(str.charAt(j) + "");
                }//End Inner For Loop
                l++;
            }//End Outer For Loop

        }//End CodewordGenerator

        //Compare the Syndromes that exist and the newly created syndrome for duplicates and correctness
        private boolean CompareSyndromes(int[] syndrome) {
            //System.out.println ("Compare Syndromes");
            boolean compare = true;

            //Looking to see if there is a match in the syndrome table column and the same position in the new syndrome.
            for (int a = 0; a < numberSyndromes; a++){
                for (int b = 0; b < syndromeTable.length; b++){
                    //System.out.println ("SYNDROME TABLE " + syndromeTable.length+ " " + syndromeTable[b].length +  " " + b + " " + a + " " + numberSyndromes + " " + syndrome.length);
                    //If the syndrome table does not have a value in the column equal to the syndrome at position a
                    //then this is not a match.
                    if (syndromeTable[b][a] != syndrome[b]){
                        compare = false;
                    }//End if
                }//End Inner For Loop

                //If there is a match, then return true
                if (compare) {
                    System.out.println ("Compare Match Found ------");
                    return true;
                }//End if
            }//End Outer For Loop

            //If we are successful through the whole iteration then
            return false;
        } //End CompareSyndrome

        //Creates the syndrome by multiplying the Parity Check Matrix (H).
        private int[] CreateSyndrome(int[] codeword) {
            //System.out.println ("Create Syndromes");
            int [] tempSyndrome = new int [n-k];
            int count = 0;

            //Iterate through the Parity Check Matrix and create the syndrome
            for (int x = 0; x < H.length; x++){
                for (int z = 0; z < H[x].length; z++){
                    count = count + H[x][z] * codeword[z];
                }// End Inner Loop

                tempSyndrome[x] = count % q;
                //System.out.println (" _ " + count + " % " + q + " = " +tempSyndrome[x]);

                count = 0; //reset
            }//End Outer Loop

            return tempSyndrome;
        } //End CreateSyndrome

        //This method will calculate the correct codeword output.
        private int[] Decode(int []syn, int[][]coset, int row){
            for (int i = 0; i < syn.length; i++){
                syn [i] = Math.abs((syn[i] - coset[i][row]) % q);//Binary addition and subtraction is the same thing

            }//End For Loop

            return syn;
        }//End of Decode

        //This method will get the coset or error vector in relation to the syndrome y.
        //Looking up the relevant syndrome. This will be similar to the compareSyndromes
        //method with the difference of returning the integer vs boolean.
        private int getCoset (int [] v){
            boolean match = true;

            //iterate through the syndrome table to see if we have a match to the syndrome.
            //Iterate by column then row. If the column does not have a match, next
            for (int x = 0; x <= numberSyndromes; x++){
                for (int z = 0; z < syndromeTable.length; z++){
                    if (syndromeTable[z][x] != v[z]){
                        match = false;
                    }//End if
                }//End inner for loop
                if (match){
                    System.out.print("ROW MATCH " + x + " : " );
                    return x;
                }
                match = true; //resetting for next row.
            }//End outer for loop

            //If we did not find any match then return -1
            return -1; //No match
        }//End getCoset

        //This method will print the syndrome table
        private void PrintTable(){
            System.out.println ("----------------------PRINT TABLE");
            System.out.println("     Coset Leaders List    | Syndrome ");
            System.out.println("---------------------------------");

            for(int i = 0; i < numberSyndromes; i++) {
                System.out.println("");
                System.out.print("Row " + i + " : ");

                //Print the specific coset
                for (int g = 0; g < cosetList[i].length; g++){
                    System.out.print( cosetList[i][g] + " ");
                }

                System.out.print(" | ");

                //Print the specific syndrome
                for (int h = 0; h < syndromeTable.length; h++){
                    System.out.print( syndromeTable[h][i] + " ");
                }//End For loop

                System.out.print(" " + syndromeTable.length);

                System.out.print("");
            }//End Outer For Loop

            System.out.println();
        }//End PrintTable Method

        //This method will print the syndrome table Adding a parameter int []
        private void PrintTable(int [] table){

            for(int i = 0; i < table.length; i++) {
                System.out.print( table[i] + " ");
            }//End Outer For Loop
        }//End PrintTable Method

        //This method will print the syndrome table Adding a parameter int [][]
        private void PrintTable(int [][] table){
            System.out.println ("   PRINT TABLE WITH 2D Table");
            System.out.println("---------------------------------");

            for(int i = 0; i < table[0].length; i++) {
                System.out.print("Row " + i + " : ");

                for (int g = 0; g < table.length; g++){
                    System.out.print( table[g][i] + " ");
                }
                System.out.println ("");
            }//End Outer For Loop
        }//End PrintTable Method

        //This method will print out the complete syndrome decode
        private void PrintTable(int[][] cosetL, int[][] synTable){
            System.out.println ("   Coset Leader   |  Syndrome ");
            System.out.println("---------------------------------");

            for(int i = 0; i < cosetL[0].length; i++) {
                System.out.print("Row " + i + " : ");

                for (int g = 0; g < cosetL.length; g++){
                    System.out.print( cosetL[g][i] + " ");
                }
                System.out.print(" | ");

                for (int g = 0; g < synTable.length; g++){
                    System.out.print( cosetL[g][i] + " ");
                }
                System.out.println ("");
            }//End Outer For Loop
        }//End PrintTable

        //This method will determine if the weight is the same as the current weight.
        //Similar equation Weight (codeword) = current weight
        private boolean Weight(int [] c, int wt) {
            //System.out.println ("Weight");
            int count = 0; //This will count the number of one's

            //Iterate through and count the number of ones in the array.
            for (int i = 0; i < c.length; i++){
                if (c[i] != 0){
                    count++;
                }//End If
            }//End For Loop

            //Return whether this is true or false
            return (count == wt);
        }//End of Weight

        //Main Method
        public static void main(String[] args) {
            Syndrome_Decoder s = new Syndrome_Decoder();
        }

}
