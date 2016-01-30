/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sugan;

import java.math.BigInteger;
import java.util.Arrays;
import static sugan.MainJFrame.NLITERAL;

/**
 *
 * @author SuganShakya
 */
public class Implicant {

    /**
     * The Integer array of minterms, For first table, array size is 1, for
     * next, it will be 2, 4 8 and so on...
     */
    private final int[] minterms;  // The number of '1' in the bit representation.

    private int bitCount;   //Number of One in the Implicant    

    private String binaryString; //The binary expression of bits eg. 00-1, 1-0-
    /**
     * isChecked = false => It is not combined and will appear in the prime
     * implicant
     */
    private boolean isChecked;

    /**
     * The program need to make sure that the minterms input is not empty.
     *
     * @param minterms
     */
    public Implicant(int[] minterms) {
        this.minterms = minterms;
        Arrays.sort(this.minterms);
        this.isChecked = false;
        setBinaryString();
        setBitCount();
    }

    /**
     * The 2nd type of constructor is one in which the binary String is already
     * known, It is generally for the implicants in the higher Tables.
     *
     * @param minterms
     * @param binaryString
     */
    public Implicant(int[] minterms, String binaryString) {
        this.minterms = minterms;
        Arrays.sort(this.minterms);
        this.isChecked = false;
        this.binaryString = binaryString;
        setBitCount();
    }

    /**
     * Assign value to the bitCount, which is equal to number of '1' bits
     *
     * @return
     */
    private int setBitCount() {
        this.bitCount = 0;
        for (char digit : binaryString.toCharArray()) {
            if (digit == '1') {
                this.bitCount++;
            }
        }
        return this.bitCount;
    }

    public int getBitCount() {
        return this.bitCount;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setCheck(boolean state) {
        this.isChecked = state;
    }

    public int[] getMinterms() {
        return minterms;
    }

    public int getMintermSize() {
        return minterms.length;
    }

    // Return true if 'value' is in its minterm list

    public boolean contains(int value) {
        for (int i : minterms) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }

    public void setBinaryString() {
        ///
        String raw = Integer.toBinaryString(minterms[0]);
        String formattedString = String.format("%0" + NLITERAL + "d", new BigInteger(raw));
        char charArray[] = formattedString.toCharArray();
        //System.out.printf("\n%s", new String(charArray));
        int xorValue;
        for (int i = 1; i < minterms.length; i++) {
            xorValue = ~(minterms[i] ^ minterms[i - 1]);
            for (int bitPosition = 0; bitPosition < NLITERAL; ++bitPosition) {
                if (((xorValue >> bitPosition) & 1) == 0) //bit at bitPosition of xorValue
                // The indexing of charArray and bitPositio is reverse
                {
                    charArray[NLITERAL - bitPosition - 1] = '-';
                }
            }
            //System.out.printf("\n%s", new String(charArray));
        }
        this.binaryString = new String(charArray);        
    }
    /**
     * The function returns A'BD' for binary String 01-0 and so on
     * @return 
     */
    public String getExpressionString() {
        char[] expressionWord = binaryString.toCharArray();
        String str = "";
        char c = 'A';

        for (int i = 0; i < NLITERAL; i++) {

            if (expressionWord[i] == '1') {
                str = str + Character.toString(c);
            } else if (expressionWord[i] == '0') {
                str = str + Character.toString(c) + "' ";
            } else {
            }
            c++;
        }
        return str;
    }

    public String getBinaryString() {
        return binaryString;
    }

    public String paddedStringNumber(int num) {
        String binString = Integer.toBinaryString(num);        
        int paddingcount = NLITERAL - binString.length();
        //StringUtils.leftPad(answer1);
        String formattedString = String.format("%0" + paddingcount + "d%s", 0, binString);
        return formattedString;
    }
     
    /** Check if the two prime impicant are same equal   
     * @param implicant2
     * @return 
     */
    public boolean isEqual(Implicant implicant2) {
        return Arrays.equals(this.minterms, implicant2.minterms);
    }

    /** 
     * Return the String equivalent of the class representation
     * @return 
     */
    @Override
    public String toString() {
        return String.format("%s | %s | %3d | %5s",
                Arrays.toString(minterms), binaryString, bitCount, isChecked);
    }
    /**
     * Create the higher order implicant
     * Ex. Combine [0,2] 00-0 with [8,10] 10-1 to give [0,2,8,10] -0-0 for 4 variable case
     * @param implicant2
     * @return 
     */
    public Implicant combine(Implicant implicant2) {
        // Combine two minterms
        int x[] = new int[this.minterms.length + implicant2.minterms.length];
        System.arraycopy(this.minterms, 0, x, 0, this.minterms.length);
        System.arraycopy(implicant2.minterms, 0, x, this.minterms.length, implicant2.minterms.length);
        // Combine the two binaryString        
        char[] newWord = binaryString.toCharArray();
        int charPosition = posSingleCharChange(implicant2);
        if (charPosition != -1) {
            newWord[charPosition] = '-';
        } else {
            System.out.println("\n No Single Char Difference Observed.");
        }
        //this.isChecked = true;
        //implicant2.isChecked = true;
        String newBinaryString = new String(newWord);
        Implicant bigImplicant = new Implicant(x, newBinaryString);
        return bigImplicant;
    }
    /**
     * The character position at which two string differ.
     * Ex1. 00-1 and 10-1 differ at position 0 and returns 0
     * Ex2: 01-0 and 10-0 differs at two position, so returns (-1)
     */

    public int posSingleCharChange(Implicant imp2) {
        //System.out.println("\n Inside posSingleCharChange.");
        int pos = -1; // pos -1 means Char change at more than one bit
        int count = 0;
        char[] c1 = this.binaryString.toCharArray();
        char[] c2 = imp2.binaryString.toCharArray();
        for (int i = 0; i < this.binaryString.length(); ++i) {
            if (c1[i] != c2[i]) {
                count++;
                pos = i;
            }
        }
        if (count == 1) {
            return pos;
        } else {
            return -1;
        }
    }
}
