// -------------------------------------------------------------------------
/**
 * Implementation of the AES. Has functionality to encrypt and decrypt based on
 * a given text and key. Usage: <plain-text>|<cipher-text> <key>
 *
 * @author Eric Hotinger
 * @version Dec 1, 2012
 */
public class AES
{

    /**
     * Temporary static integer place holders for usage throughout the program.
     */
    private static int         a, b, c;

    /**
     * Array of byte arrays to hold all of the sub keys.
     */
    private static byte[][]    subKeys;

    /**
     * The SBOX.
     */
    private static int[]       SBOX          = { 0x63, 0x7C, 0x77, 0x7B, 0xF2,
        0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76, 0xCA,
        0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C,
        0xA4, 0x72, 0xC0, 0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34,
        0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15, 0x04, 0xC7, 0x23, 0xC3, 0x18,
        0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75, 0x09,
        0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29,
        0xE3, 0x2F, 0x84, 0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A,
        0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF, 0xD0, 0xEF, 0xAA, 0xFB, 0x43,
        0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8, 0x51,
        0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10,
        0xFF, 0xF3, 0xD2, 0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4,
        0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73, 0x60, 0x81, 0x4F, 0xDC, 0x22,
        0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB, 0xE0,
        0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91,
        0x95, 0xE4, 0x79, 0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C,
        0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08, 0xBA, 0x78, 0x25, 0x2E, 0x1C,
        0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A, 0x70,
        0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86,
        0xC1, 0x1D, 0x9E, 0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B,
        0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF, 0x8C, 0xA1, 0x89, 0x0D, 0xBF,
        0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16 };

    /**
     * The inverted SBOX.
     */
    private static int[]       INVERTED_SBOX = { 0x52, 0x09, 0x6A, 0xD5, 0x30,
        0x36, 0xA5, 0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB, 0x7C,
        0xE3, 0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4,
        0xDE, 0xE9, 0xCB, 0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D, 0xEE,
        0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E, 0x08, 0x2E, 0xA1, 0x66, 0x28,
        0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B, 0xD1, 0x25, 0x72,
        0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4, 0xA4, 0x5C, 0xCC, 0x5D,
        0x65, 0xB6, 0x92, 0x6C, 0x70, 0x48, 0x50, 0xFD, 0xED, 0xB9, 0xDA, 0x5E,
        0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D, 0x84, 0x90, 0xD8, 0xAB, 0x00, 0x8C,
        0xBC, 0xD3, 0x0A, 0xF7, 0xE4, 0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06, 0xD0,
        0x2C, 0x1E, 0x8F, 0xCA, 0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01,
        0x13, 0x8A, 0x6B, 0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97,
        0xF2, 0xCF, 0xCE, 0xF0, 0xB4, 0xE6, 0x73, 0x96, 0xAC, 0x74, 0x22, 0xE7,
        0xAD, 0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E, 0x47,
        0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E, 0xAA,
        0x18, 0xBE, 0x1B, 0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79, 0x20, 0x9A,
        0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4, 0x1F, 0xDD, 0xA8, 0x33, 0x88,
        0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xEC, 0x5F, 0x60,
        0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D, 0x2D, 0xE5, 0x7A, 0x9F, 0x93,
        0xC9, 0x9C, 0xEF, 0xA0, 0xE0, 0x3B, 0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8,
        0xEB, 0xBB, 0x3C, 0x83, 0x53, 0x99, 0x61, 0x17, 0x2B, 0x04, 0x7E, 0xBA,
        0x77, 0xD6, 0x26, 0xE1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D };

    /**
     * The RCON.
     */
    private static int         RCON[]        = { 0x8d, 0x01, 0x02, 0x04, 0x08,
        0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f,
        0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef,
        0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a,
        0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01,
        0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab,
        0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3,
        0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2,
        0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8,
        0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36,
        0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35,
        0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3,
        0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d,
        0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40,
        0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63,
        0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39,
        0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66,
        0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08,
        0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f,
        0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef,
        0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a,
        0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d };

    /**
     * Error message that will be printed out if the program has incorrect
     * arguments passed in.
     */
    public static final String ERROR_MESSAGE =
                                                 "ERROR:\tUsage: <plain-text> <key>";


    // ----------------------------------------------------------
    /**
     * Runs the program with two arguments: a plain-text and a key, and encrypts
     * the plain-text. It also decrypts the encrypted plain-text afterwards.
     *
     * @param args
     *            arguments passed into the AES
     */
    public static void main(String[] args)
    {

        /**
         * If there aren't two arguments, send an error message and end the
         * program.
         */
        if (args.length != 2)
        {
            System.out.println(ERROR_MESSAGE);
            System.exit(2);
        }

        /**
         * Otherwise, tell us the original plain-text, the encrypted plain-text,
         * and then the decrypted encryption.
         */
        try
        {
            String plainText = args[0];

            String key = args[1];

            System.out.println(">>>Plain text (before encryption): "
                + plainText + "\n");

            byte[] encryptedText =
                AES.encrypt(plainText.getBytes(), key.getBytes());

            System.out.println(">>>Cipher text (after encryption): "
                + new String(encryptedText));

            byte[] decryptedText = AES.decrypt(encryptedText, key.getBytes());

            System.out.println(">>>Plain text (after decryption): "
                + new String(decryptedText));
        }

        /**
         * Catches any prints out any potential errors which can occur when byte
         * array sizes don't coincide correctly.
         */
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * Based on the given key, generates the subkeys necessary for encryption.
     *
     * @param key
     *            given byte key
     * @return array of keys
     */
    private static byte[][] generateSubkeys(byte[] key)
    {
        int i = 0;

        byte[][] tempKeys = new byte[a * (c + 1)][4];

        while (i < b)
        {
            tempKeys[i][3] = key[i * 4 + 3];
            tempKeys[i][2] = key[i * 4 + 2];
            tempKeys[i][1] = key[i * 4 + 1];
            tempKeys[i][0] = key[i * 4];

            i++;
        }

        i = b;

        while (i < a * (c + 1))
        {
            byte[] temp = new byte[4];

            for (int k = 0; k < 4; k++)
                temp[k] = tempKeys[i - 1][k];

            if (i % b == 0)
            {
                temp = generateWord(rotate(temp));
                temp[0] = (byte)(temp[0] ^ (RCON[i / b] & 0xff));
            }

            else if (b > 6 && i % b == 4)
                temp = generateWord(temp);

            tempKeys[i] = xor(tempKeys[i - b], temp);

            i++;
        }

        return tempKeys;
    }


    // ----------------------------------------------------------
    /**
     * Rotates the bits in a specified byte array.
     *
     * @param array
     *            a byte array
     * @return the rotated byte array
     */
    private static byte[] rotate(byte[] array)
    {
        byte[] rotatedByteArray = new byte[array.length];

        rotatedByteArray[3] = array[0];
        rotatedByteArray[0] = array[1];
        rotatedByteArray[1] = array[2];
        rotatedByteArray[2] = array[3];

        return rotatedByteArray;
    }


    // ----------------------------------------------------------
    /**
     * Generates the sub-word based on an array of bytes.
     *
     * @param array
     *            an array of bytes
     * @return an array of bytes representing the generated sub-word
     */
    private static byte[] generateWord(byte[] array)
    {
        byte[] ret = new byte[array.length];

        for (int i = 0; i < ret.length; i++)
            ret[i] = (byte)(SBOX[array[i] & 0x000000ff] & 0xff);

        return ret;
    }


    // ----------------------------------------------------------
    /**
     * Performs the substitute bytes operation.
     *
     * @param array
     *            an array of bytes
     * @return the byte array after substitution through the SBOX
     */
    private static byte[][] substituteBytes(byte[][] array)
    {

        byte[][] ret = new byte[array.length][array[0].length];

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < a; j++)
                ret[i][j] = (byte)(SBOX[(array[i][j] & 0x000000ff)] & 0xff);

        return ret;
    }


    // ----------------------------------------------------------
    /**
     * Does the same thing as the normal substitute bytes operation, but uses
     * the inverted SBOX.
     *
     * @param array
     *            an array of bytes
     * @return array the array of substituted bytes
     */
    private static byte[][] substituteInvertedBytes(byte[][] array)
    {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < a; j++)
                array[i][j] =
                    (byte)(INVERTED_SBOX[(array[i][j] & 0x000000ff)] & 0xff);

        return array;
    }


    // ----------------------------------------------------------
    /**
     * Adds a round key and returns the result.
     *
     * @param keys
     *            byte array of keys
     * @param keys2
     *            byte array of keys
     * @param roundNumber
     *            the round number
     * @return the result after the keys are added
     */
    private static byte[][] addKey(
        byte[][] keys,
        byte[][] keys2,
        int roundNumber)
    {

        byte[][] ret = new byte[keys.length][keys[0].length];

        for (int i = 0; i < a; i++)
            for (int j = 0; j < 4; j++)
                ret[j][i] = (byte)(keys[j][i] ^ keys2[roundNumber * a + i][j]);

        return ret;
    }


    // ----------------------------------------------------------
    /**
     * Performs the mix columns operation on a specified byte array.
     *
     * @param array
     *            the byte array
     * @return the byte array after the mix columns operation
     */
    private static byte[][] mixColumns(byte[][] array)
    {
        int[] tempInts = new int[4];

        byte hexa2 = (byte)0x02;
        byte hexa3 = (byte)0x03;

        for (int i = 0; i < 4; i++)
        {
            tempInts[0] =
                fieldMultiply(hexa2, array[0][i])
                    ^ fieldMultiply(hexa3, array[1][i]) ^ array[2][i]
                    ^ array[3][i];

            tempInts[1] =
                array[0][i] ^ fieldMultiply(hexa2, array[1][i])
                    ^ fieldMultiply(hexa3, array[2][i]) ^ array[3][i];

            tempInts[2] =
                array[0][i] ^ array[1][i] ^ fieldMultiply(hexa2, array[2][i])
                    ^ fieldMultiply(hexa3, array[3][i]);

            tempInts[3] =
                fieldMultiply(hexa3, array[0][i]) ^ array[1][i] ^ array[2][i]
                    ^ fieldMultiply(hexa2, array[3][i]);

            for (int j = 0; j < 4; j++)
                array[j][i] = (byte)(tempInts[j]);
        }

        return array;
    }


    // ----------------------------------------------------------
    /**
     * Performs the inverted mix columns operation on a specified byte array.
     *
     * @param array
     *            a byte array
     * @return array the byte array after the inverted mix columns operation
     */
    private static byte[][] mixInvertedColumns(byte[][] array)
    {
        int[] tempInts = new int[4];

        byte byte9 = (byte)0x09;
        byte byte11 = (byte)0x0b;
        byte byte13 = (byte)0x0d;
        byte byte14 = (byte)0x0e;

        for (int i = 0; i < 4; i++)
        {
            tempInts[0] =
                fieldMultiply(byte14, array[0][i])
                    ^ fieldMultiply(byte11, array[1][i])
                    ^ fieldMultiply(byte13, array[2][i])
                    ^ fieldMultiply(byte9, array[3][i]);

            tempInts[1] =
                fieldMultiply(byte9, array[0][i])
                    ^ fieldMultiply(byte14, array[1][i])
                    ^ fieldMultiply(byte11, array[2][i])
                    ^ fieldMultiply(byte13, array[3][i]);

            tempInts[2] =
                fieldMultiply(byte13, array[0][i])
                    ^ fieldMultiply(byte9, array[1][i])
                    ^ fieldMultiply(byte14, array[2][i])
                    ^ fieldMultiply(byte11, array[3][i]);

            tempInts[3] =
                fieldMultiply(byte11, array[0][i])
                    ^ fieldMultiply(byte13, array[1][i])
                    ^ fieldMultiply(byte9, array[2][i])
                    ^ fieldMultiply(byte14, array[3][i]);

            for (int j = 0; j < 4; j++)
                array[j][i] = (byte)(tempInts[j]);
        }

        return array;
    }


    // ----------------------------------------------------------
    /**
     * Performs a multiplication on the two specified bytes.
     *
     * @param byte1
     *            a byte
     * @param byte2
     *            another byte
     * @return the multiplied version of the bytes
     */
    public static byte fieldMultiply(byte byte1, byte byte2)
    {
        byte clonedByte1 = byte1;
        byte clonedByte2 = byte2;
        byte tempByte = 0;
        byte tempByte2;

        while (clonedByte1 != 0)
        {
            if ((clonedByte1 & 1) != 0)
                tempByte = (byte)(tempByte ^ clonedByte2);

            tempByte2 = (byte)(clonedByte2 & 0x80);

            clonedByte2 = (byte)(clonedByte2 << 1);

            if (tempByte2 != 0)
                clonedByte2 = (byte)(clonedByte2 ^ 0x1b);

            clonedByte1 = (byte)((clonedByte1 & 0xff) >> 1);
        }

        return tempByte;
    }


    // ----------------------------------------------------------
    /**
     * Performs the encryption of a byte array.
     *
     * @param array
     *            a byte array to be encrypted
     * @return the encrypted byte array
     */
    public static byte[] encryptBloc(byte[] array)
    {
        byte[] tempBytes = new byte[array.length];

        byte[][] modifiedBytes = new byte[4][a];

        for (int i = 0; i < array.length; i++)
            modifiedBytes[i / 4][i % 4] = array[(i % 4 * 4) + (i / 4)];

        modifiedBytes = addKey(modifiedBytes, subKeys, 0);

        for (int i = 1; i < c; i++)
        {
            modifiedBytes = substituteBytes(modifiedBytes);
            modifiedBytes = shiftRows(modifiedBytes);
            modifiedBytes = mixColumns(modifiedBytes);
            modifiedBytes = addKey(modifiedBytes, subKeys, i);
        }

        modifiedBytes = substituteBytes(modifiedBytes);
        modifiedBytes = shiftRows(modifiedBytes);
        modifiedBytes = addKey(modifiedBytes, subKeys, c);

        for (int i = 0; i < tempBytes.length; i++)
            tempBytes[i % 4 * 4 + i / 4] = modifiedBytes[i / 4][i % 4];

        return tempBytes;
    }


    // ----------------------------------------------------------
    /**
     * Performs the decryption of a byte array.
     *
     * @param array
     *            the byte array to be decrypted
     * @return the decrypted byte array
     */
    public static byte[] decryptBloc(byte[] array)
    {
        byte[] tempBytes = new byte[array.length];

        byte[][] modifiedBytes = new byte[4][a];

        for (int i = 0; i < array.length; i++)
            modifiedBytes[i / 4][i % 4] = array[(i % 4 * 4) + (i / 4)];

        modifiedBytes = addKey(modifiedBytes, subKeys, c);

        for (int i = c - 1; i >= 1; i--)
        {
            modifiedBytes = substituteInvertedBytes(modifiedBytes);
            modifiedBytes = shiftInvertedRows(modifiedBytes);
            modifiedBytes = addKey(modifiedBytes, subKeys, i);
            modifiedBytes = mixInvertedColumns(modifiedBytes);
        }

        modifiedBytes = substituteInvertedBytes(modifiedBytes);
        modifiedBytes = shiftInvertedRows(modifiedBytes);
        modifiedBytes = addKey(modifiedBytes, subKeys, 0);

        for (int i = 0; i < tempBytes.length; i++)
            tempBytes[(i % 4 * 4) + (i / 4)] = modifiedBytes[i / 4][i % 4];

        return tempBytes;
    }


    // ----------------------------------------------------------
    /**
     * Given a byte plain-text and byte key, runs the encryption on the
     * plain-text and returns cipher-text.
     *
     * @param plainText
     *            the plain text
     * @param byteKey
     *            the key
     * @return cipher text in byte format
     */
    public static byte[] encrypt(byte[] plainText, byte[] byteKey)
    {
        int i;
        int ptLength = 0;
        byte[] bitPadding = new byte[1];

        a = 4;
        b = byteKey.length / 4;
        c = b + 6;

        ptLength = 16 - plainText.length % 16;

        bitPadding = new byte[ptLength];
        bitPadding[0] = (byte)0x80;

        for (i = 1; i < ptLength; i++)
            bitPadding[i] = 0;

        byte[] tempBytes = new byte[plainText.length + ptLength];
        byte[] bytes = new byte[16];

        subKeys = generateSubkeys(byteKey);

        //System.out.println(">>>Subkeys: ");
        //AES.printArrayOfByteArrays(subKeys);
        //System.out.println();

        int count = 0;

        for (i = 0; i < plainText.length + ptLength; i++)
        {
            if (i > 0 && i % 16 == 0)
            {
                bytes = encryptBloc(bytes);
                System.arraycopy(bytes, 0, tempBytes, i - 16, bytes.length);
            }

            if (i < plainText.length)
                bytes[i % 16] = plainText[i];

            else
            {
                bytes[i % 16] = bitPadding[count % 16];
                count++;
            }
        }

        if (bytes.length == 16)
        {
            bytes = encryptBloc(bytes);
            System.arraycopy(bytes, 0, tempBytes, i - 16, bytes.length);
        }

        return tempBytes;
    }


    // ----------------------------------------------------------
    /**
     * Given an array of byte arrays, prints them out in a specified format so
     * that they all align nicely and have commas interchanged.
     *
     * @param array
     *            an array of bytes
     */
    public static void printArrayOfByteArrays(byte[][] array)
    {
        for (int i = 0; i < 4; i++)
        {
            System.out.print("|");
            for (int j = 0; j < 16; j++)
            {
                if (j < 15)
                    System.out.printf("%4d, ", array[j][i]);
                else
                    System.out.printf("%4d  |", +array[j][i]);
            }
            System.out.println();
        }
    }


    // ----------------------------------------------------------
    /**
     * Given a byte cipher-text and byte key, runs the decryption on the
     * cipher-text and returns plain-text.
     *
     * @param cipherText
     *            the cipher text
     * @param byteKey
     *            the key
     * @return plain text in byte format
     */
    public static byte[] decrypt(byte[] cipherText, byte[] byteKey)
    {
        int i;
        byte[] tempBytes = new byte[cipherText.length];
        byte[] bytes = new byte[16];

        a = 4;
        b = byteKey.length / 4;
        c = b + 6;

        subKeys = generateSubkeys(byteKey);

        for (i = 0; i < cipherText.length; i++)
        {
            if (i > 0 && i % 16 == 0)
            {
                bytes = decryptBloc(bytes);
                System.arraycopy(bytes, 0, tempBytes, i - 16, bytes.length);
            }

            if (i < cipherText.length)
                bytes[i % 16] = cipherText[i];
        }

        bytes = decryptBloc(bytes);

        System.arraycopy(bytes, 0, tempBytes, i - 16, bytes.length);

        /**
         * Something weird going on; have to fix formatting for null characters
         * at the end of the byte array potentially.
         */
        tempBytes = AES.fixFormatting(tempBytes);

        return tempBytes;
    }


    // ----------------------------------------------------------
    /**
     * Fixes the formatting of a given byte array. Not sure why this is
     * happening.
     *
     * @param array
     *            a byte array
     * @return the byte array with its formatting fixed
     */
    private static byte[] fixFormatting(byte[] array)
    {
        int mistakes = 0;

        int i = array.length - 1;

        while (array[i] == 0)
        {
            mistakes++;
            i--;
        }

        byte[] tempBytes = new byte[array.length - mistakes - 1];

        System.arraycopy(array, 0, tempBytes, 0, tempBytes.length);

        return tempBytes;
    }


    // ----------------------------------------------------------
    /**
     * Performs the XOR operation on two different byte arrays.
     *
     * @param byte1
     *            a byte array
     * @param byte2
     *            a byte array
     * @return byte array -- the XOR of the two byte arrays
     */
    private static byte[] xor(byte[] byte1, byte[] byte2)
    {
        byte[] xoredByteArray = new byte[byte1.length];

        for (int i = 0; i < byte1.length; i++)
            xoredByteArray[i] = (byte)(byte1[i] ^ byte2[i]);

        return xoredByteArray;
    }


    // ----------------------------------------------------------
    /**
     * Performs the shift rows operation on a byte array.
     *
     * @param array
     *            the byte array
     * @return the byte array after shifting
     */
    private static byte[][] shiftRows(byte[][] array)
    {
        byte[] tempBytes = new byte[4];

        for (int i = 1; i < 4; i++)
        {
            for (int j = 0; j < a; j++)
                tempBytes[j] = array[i][(j + i) % a];

            for (int k = 0; k < a; k++)
                array[i][k] = tempBytes[k];
        }

        return array;
    }


    // ----------------------------------------------------------
    /**
     * Performs the shift rows operation on a byte array but inverted.
     *
     * @param array
     *            the byte array
     * @return the byte array after inverted shifting
     */
    private static byte[][] shiftInvertedRows(byte[][] array)
    {
        byte[] tempBytes = new byte[4];

        for (int i = 1; i < 4; i++)
        {
            for (int j = 0; j < a; j++)
                tempBytes[(j + i) % a] = array[i][j];

            for (int k = 0; k < a; k++)
                array[i][k] = tempBytes[k];
        }

        return array;
    }

}
