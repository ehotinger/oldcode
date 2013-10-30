import java.math.BigInteger;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

// -------------------------------------------------------------------------
/**
 * Takes two long values as arguments, converts them into polynomials, and then
 * calculates the polynomial GCD. Outputs the Polynomial GCD and polynomials
 * g(x) and h(x) such that a(x)g(x) + b(x)(h) = d(x) mod2 where d(x) is the GCD;
 * a(x) and b(x) are the given functions converted from long values.
 *
 * @author Eric Hotinger
 * @version Dec 1, 2012
 */
public class PolynomialGCD
{

    /**
     * Keeps track of the degrees in the polynomial.
     */
    private final TreeSet<BigInteger> polynomialDegreeCollection;

    /**
     * An error message to be printed out if the program is ran incorrectly.
     */
    private final static String       ERROR_MESSAGE =
                                                        "Usage: Polynomials <long> <long>";


    // -------------------------------------------------------------------------
    /**
     * The MODE enum is a simple way of accessing XOR, AND, and OR calculations
     * through one function. The enum defines which mode will be used when the
     * calculation is being processed.
     *
     * @author Eric Hotinger
     * @version Dec 1, 2012
     */
    public enum MODE
    {
        /**
         * an "AND" calculation
         */
        AND,

        /**
         * an "OR" calculation
         */
        OR,

        /**
         * an "XOR" calculation
         */
        XOR
    }


    // ----------------------------------------------------------
    /**
     * Execute the program with specified arguments;
     *
     * @param args
     *            an array of string arguments
     */
    public static void main(String[] args)
    {
        // ----------------------------------------------------------------->
        // If there aren't two arguments to the program to define the
        // different polynomials, terminate the program and print
        // usage info.
        // ----------------------------------------------------------------->
        if (args.length != 2)
        {
            System.out.println(ERROR_MESSAGE);
            System.exit(2);
        }

        // ----------------------------------------------------------------->
        // Otherwise, initiate the program with the given arguments.
        // ----------------------------------------------------------------->
        Long firstLong = Long.parseLong(args[0]);
        Long secondLong = Long.parseLong(args[1]);

        /**
         * Create Polynomials from the long values we received in the arguments.
         */
        PolynomialGCD poly1 = PolynomialGCD.createFromLong(firstLong);
        PolynomialGCD poly2 = PolynomialGCD.createFromLong(secondLong);

        /**
         * Print out all information regarding the two polynomials.
         */
        System.out.println(">>> Initializing program with arguments <"
            + firstLong + "> <" + secondLong + ">");

        System.out.println(">>> <" + firstLong + "> translates to ("
            + PolynomialGCD.createFromLong(firstLong) + ")");

        System.out.println(">>> <" + secondLong + "> translates to ("
            + PolynomialGCD.createFromLong(secondLong) + ")");

        System.out.println("\n>>> The GCD is " + poly1.gcd(poly2));

    }


    // ----------------------------------------------------------
    /**
     * Creates a new PolynomialGCD object based on a set of degrees.
     *
     * @param setOfDegrees
     *            a collection of degrees that the new polynomial will inherit
     */
    public PolynomialGCD(Collection<BigInteger> setOfDegrees)
    {
        this.polynomialDegreeCollection =
            new TreeSet<BigInteger>(new PolynomialComparator());

        this.polynomialDegreeCollection.addAll(setOfDegrees);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new PolynomialGCD object based on another object.
     *
     * @param newPoly
     *            a polynomial object to base a new object off of
     */
    public PolynomialGCD(PolynomialGCD newPoly)
    {
        this(newPoly.polynomialDegreeCollection);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new PolynomialGCD object based on a specific long value.
     *
     * @param value
     *            a long value to base a new PolynomialGCD object on
     * @return a new PolynomialGCD object based on a long value
     */
    public static PolynomialGCD createFromLong(long value)
    {
        Set<BigInteger> degrees =
            new TreeSet<BigInteger>(new PolynomialComparator());

        int i = 0;

        long clonedValue = value; // value must be cloned to avoid re-writing

        while (clonedValue != 0)
        {
            if ((clonedValue & 1) == 1)
                degrees.add(BigInteger.valueOf(i));
            i++;

            clonedValue >>= 1;
        }

        return new PolynomialGCD(degrees);
    }


    // ----------------------------------------------------------
    /**
     * Calculate the GCD between the current PolynomialGCD object and another
     * PolynomialGCD object.
     *
     * @param newPoly
     *            another PolynomialGCD object
     * @return a PolynomialGCD object representing the GCD between the two
     *         objects.
     */
    public PolynomialGCD gcd(PolynomialGCD newPoly)
    {

        PolynomialGCD gcdifiedPolynomial = new PolynomialGCD(this);

        PolynomialGCD clonedPolynomial = newPoly; // value must be cloned

        while (!clonedPolynomial.polynomialDegreeCollection.isEmpty())
        {
            PolynomialGCD temporaryPolynomial =
                new PolynomialGCD(clonedPolynomial);

            clonedPolynomial = gcdifiedPolynomial.mod(clonedPolynomial);

            gcdifiedPolynomial = temporaryPolynomial;
        }

        return gcdifiedPolynomial;
    }


    // ----------------------------------------------------------
    /**
     * Returns the highest degree in the list of degrees, otherwise returns
     * null.
     *
     * @return the highest degree in the list of degrees, or null
     */
    public BigInteger getDegree()
    {
        if (polynomialDegreeCollection.isEmpty())
            return null;

        return polynomialDegreeCollection.first();
    }


    // ----------------------------------------------------------
    /**
     * Determines whether or not a given degree exists within the set of
     * degrees.
     *
     * @param degree
     *            a degree to check
     * @return true or false if the degree exists within the list of degrees
     */
    public boolean degreeExists(BigInteger degree)
    {
        return polynomialDegreeCollection.contains(degree);
    }


    // ----------------------------------------------------------
    /**
     * Shifts the degree to the left in the collection of degrees.
     *
     * @param shiftedDegree
     *            a degree to shift
     * @return the PolynomialGCD after the degree has been shifted
     */
    public PolynomialGCD shiftLeft(BigInteger shiftedDegree)
    {
        Set<BigInteger> degrees =
            new TreeSet<BigInteger>(new PolynomialComparator());

        for (BigInteger degree : polynomialDegreeCollection)
        {
            BigInteger shifted = degree.add(shiftedDegree);
            degrees.add(shifted);
        }

        return new PolynomialGCD(degrees);
    }


    // ----------------------------------------------------------
    /**
     * Performs the modulus operation on a given PolynomialGCD object with the
     * current object.
     *
     * @param polynomial
     *            a given polynomial
     * @return the resulting PolynomialGCD after the modulus operation
     */
    public PolynomialGCD mod(PolynomialGCD polynomial)
    {

        PolynomialGCD newPolynomialGCD =
            new PolynomialGCD(this.polynomialDegreeCollection);
        newPolynomialGCD = this.modulus(newPolynomialGCD, polynomial);

        return newPolynomialGCD;
    }


    // ----------------------------------------------------------
    /**
     * This is a helper function for the mod function. It performs the XOR
     * operation repeatedly and does the brunt work for the modulus operation.
     *
     * @param newPolynomialGCD
     *            a PolynomialGCD object
     * @param polynomial
     *            a PolynomialGCD object
     * @return the new PolynomialGCD object after modulus
     */
    public PolynomialGCD modulus(
        PolynomialGCD newPolynomialGCD,
        PolynomialGCD polynomial)
    {
        PolynomialGCD clonedPoly = newPolynomialGCD;
        BigInteger thisDegree = this.getDegree();
        BigInteger polynomialDegree = polynomial.getDegree();
        for (BigInteger i = thisDegree.subtract(polynomialDegree); i
            .compareTo(BigInteger.ZERO) >= 0; i = i.subtract(BigInteger.ONE))
        {

            if (clonedPoly.degreeExists(i.add(polynomialDegree)))
            {
                PolynomialGCD shiftedPolynomial = polynomial.shiftLeft(i);

                clonedPoly =
                    this.performCalculation(shiftedPolynomial, MODE.XOR);
            }
        }
        return clonedPoly;
    }


    /**
     * Returns an accurate String representation of the PolynomialGCD object.
     *
     * @return string representation of the PolynomialGCD object
     */
    public String toString()
    {
        String representation = "";

        for (BigInteger degree : polynomialDegreeCollection)
        {
            if (representation.length() != 0)
                representation += " + ";

            if (degree.compareTo(BigInteger.ZERO) == 0)
                representation += "1";

            else
                representation += "x^" + degree;

        }

        return representation;
    }


    // ----------------------------------------------------------
    /**
     * Based on a given PolynomialGCD object and a MODE, performs a certain
     * calculation on the PolynomialGCD.
     *
     * @param polynomial
     *            a PolynomialGCD object
     * @param mode
     *            a type of MODE
     * @return the new PolynomialGCD object after the calculation is over
     */
    public PolynomialGCD performCalculation(PolynomialGCD polynomial, MODE mode)
    {
        switch (mode)
        {
            case XOR:
                PolynomialGCD polynomialORed =
                    this.performCalculation(polynomial, MODE.OR);

                PolynomialGCD polynomialANDed =
                    this.performCalculation(polynomial, MODE.AND);

                Set<BigInteger> xorDegrees =
                    new TreeSet<BigInteger>(new PolynomialComparator());

                xorDegrees.addAll(polynomialORed.polynomialDegreeCollection);
                xorDegrees
                    .removeAll(polynomialANDed.polynomialDegreeCollection);

                return new PolynomialGCD(xorDegrees);

            case AND:
                Set<BigInteger> andDegrees =
                    new TreeSet<BigInteger>(new PolynomialComparator());

                andDegrees.addAll(this.polynomialDegreeCollection);
                andDegrees.retainAll(polynomial.polynomialDegreeCollection);

                return new PolynomialGCD(andDegrees);

            case OR:
                Set<BigInteger> orDegrees =
                    new TreeSet<BigInteger>(new PolynomialComparator());

                orDegrees.addAll(this.polynomialDegreeCollection);
                orDegrees.addAll(polynomial.polynomialDegreeCollection);

                return new PolynomialGCD(orDegrees);

            default:
                return null;
        }
    }


    // -------------------------------------------------------------------------
    /**
     * Simple private class that ensures that the PolynomialGCD will be printed
     * out in the correct format and order.
     *
     * @author Eric Hotinger
     * @version Dec 1, 2012
     */
    private static final class PolynomialComparator
        implements Comparator<BigInteger>
    {
        public int compare(BigInteger degree1, BigInteger degree2)
        {
            return -1 * degree1.compareTo(degree2);
        }
    }
}
