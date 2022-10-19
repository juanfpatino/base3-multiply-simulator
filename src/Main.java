import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.println("Type two values to add in base2 and base3");
        int a = s.nextInt();
        bit[] a2 = (intToBitArray(a));
        System.out.println("a in base 2 = " + bitArrayToString(a2));
        trit[] a3 = intToTritArray(a);
        System.out.println("a in base 3 = " + tritArrayToString(a3));

        int b = s.nextInt();
        bit[] b2 = (intToBitArray(b));
        System.out.println("a in base 2 = " + bitArrayToString(b2));
        trit[] b3 = intToTritArray(b);
        System.out.println("a in base 3 = " + tritArrayToString(b3));


        long start = System.currentTimeMillis();
        bit[] base2res = addBase2(intToBitArray(a),intToBitArray(b));
        long base2FinishTime = System.currentTimeMillis();
        long base2Time = base2FinishTime - start;

        start = System.currentTimeMillis();
        trit[] base3res = addBase3(intToTritArray(a), intToTritArray(b));
        long base3FinishTime = System.currentTimeMillis();
        long base3Time = base3FinishTime - start;


    }

    private static bit[] intToBitArray(int i){

        List<bit> tmp = new LinkedList<>();

        do{

            tmp.add(new bit(i % 2));
            i = i / 2;

        }
        while(i > 0);

        bit[] ret = new bit[tmp.size()];

        for(int j = 0; j < tmp.size(); j++){
            ret[j] = tmp.get(j);
        }

        return ret;

    }
    private static trit[] intToTritArray(int i){
        List<trit> tmp = new LinkedList<>();

        do{

            tmp.add(new trit(i % 3));
            i = i / 2;

        }
        while(i > 0);

        trit[] ret = new trit[tmp.size()];

        for(int j = 0; j < tmp.size(); j++){
            ret[j] = tmp.get(j);
        }

        return ret;    }

    private static bit[] addBase2(bit[] a, bit[] b){

        return null;

    }

    private static trit[] addBase3(trit[] a, trit[] b){

        return null;

    }

    private static String bitArrayToString(bit[] B){
        StringBuilder s = new StringBuilder();

        for (bit b: B
             ) {

            s.append(b);

        }

        return s.reverse().toString();

    }

    private static String tritArrayToString(trit[] T){
        StringBuilder s = new StringBuilder();

        for (trit t: T
        ) {

            s.append(t);

        }

        return s.reverse().toString();

    }

}