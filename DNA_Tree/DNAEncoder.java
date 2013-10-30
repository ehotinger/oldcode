import java.lang.StringBuilder;

/**
 *  Responsible for encoding DNA Strings to/from byte arrays.
 *
 * @author Trevor M. Senior
 * @author Eric R. Hotinger
 * @version 1
 * @since 10/15/2012
 */
public class DNAEncoder {

  /**
   * Adds zeros to the front of a binaryString until it reaches the specified
   * size.
   *
   * @param binaryString The string we want to pad.
   * @param The size we want to pad to.
   * @return The padded String.
   */
  private static String padBinary(String binaryString, int size) {
    String b = binaryString;
    while ((b = (b.length() < size) ? ("0" + b) : b).length() < size);
    return b;
  }

  /**
   * Encodes a String into a byte array
   *
   * @param dnaString The DNA String we want to encode.
   * @return The DNA String as an encoded byte array.
   */
  public static byte[] encodeDNA(String dnaString) {

    String dnaStringReplace = dnaString.replaceAll("A", "00").replaceAll("C", "01")
                         .replaceAll("G", "10").replaceAll("T", "11");

    // splits every 8 characters, aka a byte
    String[] bytePeices = dnaStringReplace.split("(?<=\\G.{8})");

    // we add two bytes to hold the actual length of this byte array.
    byte[] bytes = new byte[bytePeices.length + 2];
    int length = 0;

    for (int i = 2; i < bytes.length; i++) {
      String peice = bytePeices[i - 2];
      // dang Java for not having unsigned variables.
      bytes[i] = (byte)(Integer.parseInt(peice, 2) - 128);
      length += peice.length();
    }

    // add "0" to the front of the binary value till we have 16 bits
    String b = padBinary(Integer.toBinaryString(length), 16);

    // split into bytes and stick the values into the byte array
    String[] lengthPeices = b.split("(?<=\\G.{8})");
    bytes[0] = (byte)(Integer.parseInt(lengthPeices[0], 2) - 128);
    bytes[1] = (byte)(Integer.parseInt(lengthPeices[1], 2) - 128);

    return bytes;
  }

  /**
   * Decodes a byte array into a DNA String.
   *
   * @param dnaByteArray The encoded DNA byte array.
   * @return The decoded DNA String.
   */
  public static String decodeDNA(byte[] dnaByteArray) {

    int length = Integer.parseInt(
            Integer.toBinaryString(dnaByteArray[0] + 128) +
            Integer.toBinaryString(dnaByteArray[1] + 128), 2);

    int end = length; // used to determine the end of the binary string
    StringBuilder bytes = new StringBuilder();
    for (int i = 2; i < dnaByteArray.length; i++) {
      int padding = (end > 8) ? 8 : end;
      end -= 8;
      bytes.append(
        padBinary(Integer.toBinaryString(dnaByteArray[i] + 128), padding));
    }

    String[] dnaChunks = bytes.toString().split("(?<=\\G.{2})");

    StringBuilder decodedString = new StringBuilder();

    for (int i = 0; i < (length / 2); i++) {
      String chunk = null;
      if (dnaChunks[i].equals("00")) chunk = "A";
      else if (dnaChunks[i].equals("01")) chunk = "C";
      else if (dnaChunks[i].equals("10")) chunk = "G";
      else if (dnaChunks[i].equals("11")) chunk = "T";
      decodedString.append(chunk);
    }

    return decodedString.toString();
  }

}