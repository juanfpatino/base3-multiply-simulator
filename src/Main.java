import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.println("The purpose of this simulation is to compare the speed of arithmetic" +
                " in base 2 versus base 3");

        if (args.length == 0)
            System.out.println("Give two values to multiply");

        //a
        BigInteger a;
        if (args.length == 0)
            a = s.nextBigInteger();
        else
            a = new BigInteger(args[0]);
        bit[] a2 = (intToBitArray(a));
        String a2s = bitArrayToString(a2);
        System.out.println("a in base 2 = " + a2s);
        trit[] a3 = intToTritArray(a);
        String a3s = tritArrayToString(a3);
        System.out.println("  in base 3 = " + a3s);

        //b
        BigInteger b;
        if (args.length == 0)
            b = s.nextBigInteger();
        else
            b = new BigInteger(args[1]);
        bit[] b2 = (intToBitArray(b));
        String b2s = bitArrayToString(b2);
        System.out.println("b in base 2 = " + b2s);
        trit[] b3 = intToTritArray(b);
        String b3s = tritArrayToString(b3);
        System.out.println("  in base 3 = " + b3s);

        long start = System.currentTimeMillis();
        bit[] base2mult = base2Multiply(a2, b2); //MULT
        long base2FinishTime = System.currentTimeMillis();
        long base2Time = base2FinishTime - start;

        String base2multS = bitArrayToString(base2mult);

        System.out.println("\nIn base 2: " + bitArrayToString(a2) + " x " + bitArrayToString(b2) + " = " + base2multS);
        try {
            System.out.println("In base 10: " + base2ToBigInteger(a2) + " x " + base2ToBigInteger(b2) + " = " + base2ToBigInteger(base2mult));
        } catch (NumberFormatException n) {
            System.out.println("Java builtin cannot parse such big a number");
        }
        System.out.println("Computed in " + base2Time + "ms");

        start = System.currentTimeMillis();
        trit[] base3mult = base3Multiply(a3, b3); //MULT
        long base3FinishTime = System.currentTimeMillis();
        long base3Time = base3FinishTime - start;

        String base3multS = tritArrayToString(base3mult);

        System.out.println("\nIn base 3: " + tritArrayToString(a3) + " x " + tritArrayToString(b3) + " = " + base3multS);
        try {
            System.out.println("In base 10: " + base3ToBigInteger(a3) + " x " + base3ToBigInteger(b3) + " = " + base3ToBigInteger(base3mult));
        } catch (NumberFormatException n) {
            System.out.println("Java builtin cannot parse such big a number");
        }
        System.out.println("Computed in " + base3Time + "ms\n");

        if (base3ToBigInteger(base3mult).equals(base2ToBigInteger(base2mult)) && base3ToBigInteger(base3mult).equals(a.multiply(b)))
            System.out.println("Arithmetic is all correct!\n");
        else
            System.out.println("Arithmetic does not equal " + a.multiply(b));


        if (base3Time > base2Time)
            System.out.println("Base 2 is faster!\n");//never
        else if (base2Time > base3Time)
            System.out.println("Base 3 is faster! " + String.format("%,.0f", (double) base3Time) + "ms < " + String.format("%,.0f", (double) base2Time) + "ms! \n" +
                    (double) (base2Time - base3Time) / base3Time * 100.0 + "% faster!");
        else
            System.out.println("Same, maybe try higher numbers");
    }

    private static bit[] intToBitArray(BigInteger i) {

        List<bit> tmp = new LinkedList<>();

        do {

            tmp.add(new bit(i.mod(BigInteger.TWO)));
            i = i.divide(BigInteger.TWO);

        }
        while (!i.equals(BigInteger.ZERO));

        bit[] ret = new bit[tmp.size()];

        for (int j = 0; j < tmp.size(); j++) {
            ret[j] = tmp.get(j);
        }

        return ret;

    }

    private static trit[] intToTritArray(BigInteger i) {
        List<trit> tmp = new LinkedList<>();

        do {

            tmp.add(new trit(i.mod(BigInteger.valueOf(3))));
            i = i.divide(BigInteger.valueOf(3));

        }
        while (!i.equals(BigInteger.ZERO));

        trit[] ret = new trit[tmp.size()];

        for (int j = 0; j < tmp.size(); j++) {
            ret[j] = tmp.get(j);
        }

        return ret;
    }

    private static bit[] addBase2(bit[] a, bit[] b) {

        int n = Integer.max(a.length, b.length);

        List<bit> tmp = new LinkedList<>();

        bit carry = new bit(0);

        for (int i = 0; i < n; i++) {

            if (!(i < a.length)) {
                carry = getBase2Carry(b, tmp, carry, i);
            } else if (!(i < b.length)) {
                carry = getBase2Carry(a, tmp, carry, i);
            } else {

                if (a[i].val == Base2Digit.ZERO) {

                    if (b[i].val == Base2Digit.ZERO) {

                        if (carry.val == Base2Digit.ONE) { //0 + 0 + 1
                            tmp.add(new bit(1));
                            carry = new bit(0);
                        } else {
                            tmp.add(new bit(0));//0 + 0 + 0
                        }

                    } else {

                        if (carry.val == Base2Digit.ONE) { //0 + 1 + 1
                            tmp.add(new bit(0));
                            carry = new bit(1);
                        } else {
                            tmp.add(new bit(1));//0 + 1 + 0
                        }
                    }

                } else {

                    if (b[i].val == Base2Digit.ZERO) {

                        if (carry.val == Base2Digit.ONE) { //1 + 0 + 1
                            tmp.add(new bit(0));
                            carry = new bit(1);
                        } else {
                            tmp.add(new bit(1));//1 + 0 + 0
                        }

                    } else {

                        if (carry.val == Base2Digit.ONE) { //1 + 1 + 1
                            tmp.add(new bit(1));
                            //carry is still 1
                        } else {
                            tmp.add(new bit(0));//1 + 1 + 0
                            carry = new bit(1);
                        }
                    }

                }


            }

        }

        if (carry.val == Base2Digit.ONE) {
            tmp.add(new bit(1));
        }

        bit[] ret = new bit[tmp.size()];

        for (int i = 0; i < tmp.size(); i++) {
            ret[i] = tmp.get(i);
        }

        return ret;

    }

    private static bit[] base2Multiply(bit[] a, bit[] b) {

        if (a.length == 1) {
            if (a[0].val == Base2Digit.ZERO) {
                return new bit[]{new bit(0)};
            } else {
                return b;
            }
        }
        if (b.length == 1) {
            if (b[0].val == Base2Digit.ZERO) {
                return new bit[]{new bit(0)};
            } else {
                return a;
            }
        }

        bit[] ret = Arrays.copyOf(a, a.length);

        BigInteger i = base2ToBigInteger(b);

        for (BigInteger j = BigInteger.ONE; j.compareTo(i) < 0; j = j.add(BigInteger.ONE)) {

            ret = addBase2(ret, a);

        }

        return ret;

    }

    private static trit[] base3Multiply(trit[] a, trit[] b) {

        if (a.length == 1) {
            if (a[0].val == Base3Digit.ZERO) {
                return new trit[]{new trit(0)};
            } else if (a[0].val == Base3Digit.ONE) {
                return b;
            } else
                return addBase3(b, b);
        }
        if (b.length == 1) {
            if (b[0].val == Base3Digit.ZERO) {
                return new trit[]{new trit(0)};
            } else if (b[0].val == Base3Digit.ONE) {
                return a;
            } else
                return addBase3(a, a);
        }

        trit[] ret = Arrays.copyOf(a, a.length);

        BigInteger i = base3ToBigInteger(b);

        for (BigInteger j = BigInteger.ONE; j.compareTo(i) < 0; j = j.add(BigInteger.ONE)) {

            ret = addBase3(ret, a);

        }

        return ret;

    }


    private static BigInteger base2ToBigInteger(bit[] b) {
        BigInteger i = BigInteger.ZERO;

        for (int j = 0; j < b.length; j++) {
            if (b[j].val == Base2Digit.ONE)
                i = i.add(BigInteger.TWO.pow(j));
        }
        return i;
    }

    private static BigInteger base3ToBigInteger(trit[] b) {
        BigInteger i = BigInteger.ZERO;
        BigInteger three = new BigInteger("3");
        String bLength = String.valueOf(b.length);

        //this is messy
        for (BigInteger j = BigInteger.ZERO; j.compareTo(new BigInteger(bLength)) < 0; j = j.add(BigInteger.ONE)) {
            trit thisTritVal = b[j.intValue()];

            switch (thisTritVal.val) {
                case ONE -> {
                    if (j.equals(BigInteger.ZERO))
                        i = i.add(BigInteger.ONE);
                    else
                        i = i.add(three.pow(j.intValue()).multiply(BigInteger.ONE));
                }
                case TWO -> {
                    if (j.equals(BigInteger.ZERO))
                        i = i.add(BigInteger.TWO);
                    else {
                        i = i.add(three.pow(j.intValue()).multiply(BigInteger.TWO));
                    }
                }
            }
        }
        return i;
    }

    private static bit getBase2Carry(bit[] a, List<bit> tmp, bit carry, int i) {
        if (carry.val == Base2Digit.ONE) {
            if (a[i].val == Base2Digit.ONE) { //1 + 1
                tmp.add(new bit(0));
            } else {//1 + 0
                tmp.add(new bit(1));
                carry = new bit(0);
            }
        } else {
            tmp.add(a[i]);
        }
        return carry;
    }

    private static trit getBase3Carry(trit[] a, List<trit> tmp, trit carry, int i) {
        //c + a
        if (carry.val == Base3Digit.ONE) {//either there IS a carry of 1 or two
            if (a[i].val == Base3Digit.ONE) { //1 + 1
                carry = b3Two(tmp);
            } else if (a[i].val == Base3Digit.TWO) {//2 + 1
                carry = b3Three(tmp);
            } else {//1 + 0
                carry = b3One(tmp);
            }

        } else if (carry.val == Base3Digit.TWO) {
            if (a[i].val == Base3Digit.ONE) { //2 + 1
                carry = b3Three(tmp);
            } else if (a[i].val == Base3Digit.TWO) {//2 + 2
                carry = b3Four(tmp);
            } else {//2 + 0
                carry = b3Two(tmp);
            }
        } else {//0 + whatever //or there isn't
            tmp.add(a[i]);
        }
        return carry;
    }

    private static trit[] addBase3(trit[] a, trit[] b) {

        //all possible values with 2 trits + 1 carry
        //base 10 base 3
        //0        0
        //1        1
        //2        2
        //3        10
        //4        11
        //5        12
        //6        20

        int n = Integer.max(a.length, b.length);

        List<trit> tmp = new LinkedList<>();

        trit carry = new trit(0);

        for (int i = 0; i < n; i++) {

            if (!(i < a.length)) {
                carry = getBase3Carry(b, tmp, carry, i);
            } else if (!(i < b.length)) {
                carry = getBase3Carry(a, tmp, carry, i);
            } else {//c + a + b

                if (a[i].val == Base3Digit.ZERO) {
                    if (b[i].val == Base3Digit.ZERO) {
                        if (carry.val == Base3Digit.ZERO) {//0 + 0 + 0

                            b3Zero(tmp);

                        } else if (carry.val == Base3Digit.ONE) {//1 + 0 + 0

                            carry = b3One(tmp);

                        } else {//2 + 0 + 0

                            carry = b3Two(tmp);

                        }
                    } else if (b[i].val == Base3Digit.ONE) {
                        if (carry.val == Base3Digit.ZERO) {//0 + 0 + 1

                            carry = b3One(tmp);

                        } else if (carry.val == Base3Digit.ONE) {//1 + 0 + 1

                            carry = b3Two(tmp);

                        } else {//2 + 0 + 1

                            carry = b3Three(tmp);

                        }
                    } else {
                        if (carry.val == Base3Digit.ZERO) {//0 + 0 + 2

                            carry = b3Two(tmp);

                        } else if (carry.val == Base3Digit.ONE) {//1 + 0 + 2

                            carry = b3Three(tmp);

                        } else {//2 + 0 + 2
                            carry = b3Four(tmp);
                        }
                    }
                } else if (a[i].val == Base3Digit.ONE) {
                    if (b[i].val == Base3Digit.ZERO) {
                        if (carry.val == Base3Digit.ZERO) {//0 + 1 + 0
                            carry = b3One(tmp);
                        } else if (carry.val == Base3Digit.ONE) {//1 + 1 + 0
                            carry = b3Two(tmp);
                        } else {//2 + 1 + 0
                            carry = b3Three(tmp);
                        }
                    } else if (b[i].val == Base3Digit.ONE) {
                        if (carry.val == Base3Digit.ZERO) {//0 + 1 + 1
                            carry = b3Two(tmp);
                        } else if (carry.val == Base3Digit.ONE) {// 1 + 1 + 1
                            carry = b3Three(tmp);
                        } else {//2 + 1 + 1
                            carry = b3Four(tmp);
                        }
                    } else {
                        if (carry.val == Base3Digit.ZERO) {//0 + 1 + 2
                            carry = b3Three(tmp);
                        } else if (carry.val == Base3Digit.ONE) {//1 + 1 + 2
                            carry = b3Four(tmp);
                        } else {//2 + 1 + 2
                            carry = b3Five(tmp);
                        }
                    }
                } else {
                    if (b[i].val == Base3Digit.ZERO) {
                        if (carry.val == Base3Digit.ZERO) {//0 + 2 + 0
                            carry = b3Two(tmp);
                        } else if (carry.val == Base3Digit.ONE) {//1 + 2 + 0
                            carry = b3Three(tmp);
                        } else {//2 + 2 + 0
                            carry = b3Four(tmp);
                        }
                    } else if (b[i].val == Base3Digit.ONE) {
                        if (carry.val == Base3Digit.ZERO) {//0 + 2 + 1
                            carry = b3Three(tmp);
                        } else if (carry.val == Base3Digit.ONE) {//1 + 2 + 1
                            carry = b3Four(tmp);
                        } else {//2 + 2 + 1
                            carry = b3Five(tmp);
                        }
                    } else {
                        if (carry.val == Base3Digit.ZERO) {//0 + 2 + 2
                            carry = b3Four(tmp);
                        } else if (carry.val == Base3Digit.ONE) {//1 + 2 + 2
                            carry = b3Five(tmp);
                        } else {//2 + 2 + 2 = 6
                            carry = new trit(2);
                            tmp.add(new trit(0));
                        }
                    }
                }

            }


        }

        if (carry.val == Base3Digit.ONE) {
            tmp.add(new trit(1));
        } else if (carry.val == Base3Digit.TWO) {
            tmp.add(new trit(2));
        }

        trit[] ret = new trit[tmp.size()];

        for (int i = 0; i < tmp.size(); i++) {
            ret[i] = tmp.get(i);
        }

        return ret;
    }

    private static trit b3Five(List<trit> tmp) {
        trit carry;
        tmp.add(new trit(2));
        carry = new trit(1);
        return carry;
    }

    private static trit b3Four(List<trit> tmp) {
        trit carry;
        tmp.add(new trit(1));
        carry = new trit(1);
        return carry;
    }

    private static trit b3Three(List<trit> tmp) {
        trit carry;
        b3Zero(tmp);
        carry = new trit(1);
        return carry;
    }

    private static trit b3Two(List<trit> tmp) {
        trit carry;
        tmp.add(new trit(2));
        carry = new trit(0);
        return carry;
    }

    private static trit b3One(List<trit> tmp) {
        trit carry;
        tmp.add(new trit(1));
        carry = new trit(0);
        return carry;
    }

    private static void b3Zero(List<trit> tmp) {
        tmp.add(new trit(0));
    }

    private static String bitArrayToString(bit[] B) {
        StringBuilder s = new StringBuilder();

        for (bit b : B
        ) {

            s.append(b);

        }

        return s.reverse().toString();

    }

    private static String tritArrayToString(trit[] T) {
        StringBuilder s = new StringBuilder();

        for (trit t : T
        ) {

            s.append(t);

        }

        return s.reverse().toString();

    }

}