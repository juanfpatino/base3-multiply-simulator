import java.math.BigInteger;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.println("Type two values to add in base2 and base3");

        //a
        BigInteger a = s.nextBigInteger();
        bit[] a2 = (intToBitArray(a));
        String a2s = bitArrayToString(a2);
        System.out.println("a in base 2 = " + a2s);
        trit[] a3 = intToTritArray(a);
        String a3s = tritArrayToString(a3);
        System.out.println("a in base 3 = " + a3s);

        //b
        BigInteger b = s.nextBigInteger();
        bit[] b2 = (intToBitArray(b));
        String b2s = bitArrayToString(b2);
        System.out.println("b in base 2 = " + b2s);
        trit[] b3 = intToTritArray(b);
        String b3s = tritArrayToString(b3);
        System.out.println("b in base 3 = " + b3s);


        long start = System.currentTimeMillis();
        bit[] base2res = addBase2(intToBitArray(a),intToBitArray(b)); //ADD
        long base2FinishTime = System.currentTimeMillis();
        long base2Time = base2FinishTime - start;

        String ab2s = bitArrayToString(base2res);

        System.out.println("\nIn base 2: " + bitArrayToString(a2) + " + " + bitArrayToString(b2) + " = " +  ab2s);
        try{
            System.out.println("In base 10: " + base2ToBigInteger(a2) + " + " + base2ToBigInteger(b2) + " = " + base2ToBigInteger(base2res));
        }
        catch (NumberFormatException n){
            System.out.println("Java builtin cannot parse such big a number");
        }
        System.out.println("Computed in " + base2Time + "ms");

        start = System.currentTimeMillis();
        bit[] base2mult = base2Multiply(a2, b2); //MULT
        base2FinishTime = System.currentTimeMillis();
        base2Time = base2FinishTime - start;

        String base2multS = bitArrayToString(base2mult);

        System.out.println("\nIn base 2: " + bitArrayToString(a2) + "x" + bitArrayToString(b2) + " = " +  base2multS);
        try{
            System.out.println("In base 10: " + base2ToBigInteger(a2) +  "x " + base2ToBigInteger(b2) + " = " + base2ToBigInteger(base2mult));
        }
        catch (NumberFormatException n){
            System.out.println("Java builtin cannot parse such big a number");
        }
        System.out.println("Computed in " + base2Time + "ms");

        start = System.currentTimeMillis();
        trit[] base3res = addBase3(intToTritArray(a), intToTritArray(b));
        long base3FinishTime = System.currentTimeMillis();
        long base3Time = base3FinishTime - start;

    }

    private static bit[] intToBitArray(BigInteger i){

        List<bit> tmp = new LinkedList<>();

        do{

            tmp.add(new bit(i.mod(BigInteger.TWO)));
            i = i.divide(BigInteger.TWO);

        }
        while(!i.equals(BigInteger.ZERO));

        bit[] ret = new bit[tmp.size()];

        for(int j = 0; j < tmp.size(); j++){
            ret[j] = tmp.get(j);
        }

        return ret;

    }
    private static trit[] intToTritArray(BigInteger i){
        List<trit> tmp = new LinkedList<>();

        do{

            tmp.add(new trit(i.mod(BigInteger.valueOf(3))));
            i = i.divide(BigInteger.valueOf(3));

        }
        while(!i.equals(BigInteger.ZERO));

        trit[] ret = new trit[tmp.size()];

        for(int j = 0; j < tmp.size(); j++){
            ret[j] = tmp.get(j);
        }

        return ret;    }

    private static bit[] addBase2(bit[] a, bit[] b){

        int n = Integer.max(a.length, b.length);

        List<bit> tmp = new LinkedList<>();

        bit carry = new bit(0);

        for(int i = 0; i < n; i++){

            if(!(i < a.length)){
                carry = getBase2Carry(b, tmp, carry, i);
            }
            else if (!(i < b.length)){
                carry = getBase2Carry(a, tmp, carry, i);
            }
            else{

                if(a[i].val == Base2Digit.ZERO) {

                    if (b[i].val == Base2Digit.ZERO) {

                        if(carry.val == Base2Digit.ONE){ //0 + 0 + 1
                            tmp.add(new bit(1));
                            carry = new bit(0);
                        }
                        else{
                            tmp.add(new bit(0));//0 + 0 + 0
                        }

                    }
                    else{

                        if(carry.val == Base2Digit.ONE){ //0 + 1 + 1
                            tmp.add(new bit(0));
                            carry = new bit(1);
                        }
                        else{
                            tmp.add(new bit(1));//0 + 1 + 0
                        }
                    }

                }
                else{

                    if (b[i].val == Base2Digit.ZERO) {

                        if(carry.val == Base2Digit.ONE){ //1 + 0 + 1
                            tmp.add(new bit(0));
                            carry = new bit(1);
                        }
                        else{
                            tmp.add(new bit(1));//1 + 0 + 0
                        }

                    }
                    else{

                        if(carry.val == Base2Digit.ONE){ //1 + 1 + 1
                            tmp.add(new bit(1));
                            //carry is still 1
                        }
                        else{
                            tmp.add(new bit(0));//1 + 1 + 0
                            carry = new bit(1);
                        }
                    }

                }


            }

        }

        if(carry.val == Base2Digit.ONE){
            tmp.add(new bit(1));
        }

        bit[] ret = new bit[tmp.size()];

        for(int i = 0; i < tmp.size(); i++){
            ret[i] = tmp.get(i);
        }

        return ret;

    }

    private static bit[] base2Multiply(bit[] a, bit[] b){

        if(a.length == 1){
            if(a[0].val == Base2Digit.ZERO){
                return new bit[]{new bit(0)};
            }
            else{
                return b;
            }
        }
        if(b.length == 1){
            if(b[0].val == Base2Digit.ZERO){
                return new bit[]{new bit(0)};
            }
            else{
                return a;
            }
        }

        bit[] ret = Arrays.copyOf(a, a.length);

        BigInteger i = base2ToBigInteger(b);

        for(BigInteger j = BigInteger.ONE; j.compareTo(i) < 0; j = j.add(BigInteger.ONE)){

            ret = addBase2(ret, a);

        }

        return ret;

    }

    private static BigInteger base2ToBigInteger(bit[] b) {
        BigInteger i = BigInteger.ZERO;

        for(int j = 0; j < b.length; j++){
            if(b[j].val == Base2Digit.ONE)
                i = i.add(BigInteger.TWO.pow(j));
        }
        return i;
    }

    private static bit getBase2Carry(bit[] a, List<bit> tmp, bit carry, int i) {
        if(carry.val == Base2Digit.ONE){
            if(a[i].val == Base2Digit.ONE){ //1 + 1
                tmp.add(new bit(0));
            }
            else{//1 + 0
                tmp.add(new bit(1));
                carry = new bit(0);
            }
        }
        else{
            tmp.add(a[i]);
        }
        return carry;
    }

    private static trit getBase3Carry(trit[] a, List<trit> tmp, trit carry, int i) {
        if(carry.val == Base3Digit.ONE){
            if(a[i].val == Base3Digit.ONE){ //1 + 1
                tmp.add(new trit(0));
            }
            else{//1 + 0
                tmp.add(new trit(1));
                carry = new trit(0);
            }
        }
        else{
            tmp.add(a[i]);
        }
        return carry;
    }

    private static trit[] addBase3(trit[] a, trit[] b){

        int n = Integer.max(a.length, b.length);

        List<trit> tmp = new LinkedList<>();

        trit carry = new trit(0);

        for(int i = 0; i < n; i++){

            if(!(i < a.length)){
                carry = getBase3Carry(b, tmp, carry, i);
            }
            else if (!(i < b.length)){
                carry = getBase3Carry(a, tmp, carry, i);
            }
            else{

                if(a[i].val == Base3Digit.ZERO){
                    if(b[i].val == Base3Digit.ZERO)
                    {
                        if(carry.val == Base3Digit.ZERO){//0 + 0 + 0

                        }
                        else if(carry.val == Base3Digit.ONE){//1 + 0 + 0

                        }
                        else{//2 + 0 + 0

                        }
                    }
                    else if (b[i].val == Base3Digit.ONE){
                        if(carry.val == Base3Digit.ZERO){//0 + 0 + 1

                        }
                        else if(carry.val == Base3Digit.ONE){//1 + 0 + 1

                        }
                        else{//2 + 0 + 1

                        }
                    }
                    else{
                        if(carry.val == Base3Digit.ZERO){//0 + 0 + 2

                        }
                        else if(carry.val == Base3Digit.ONE){//1 + 0 + 2

                        }
                        else{//2 + 0 + 2

                        }
                    }
                }
                else if(a[i].val == Base3Digit.ONE)
                {
                    if(b[i].val == Base3Digit.ZERO)
                    {
                        if(carry.val == Base3Digit.ZERO){//0 + 1 + 0

                        }
                        else if(carry.val == Base3Digit.ONE){//1 + 1 + 0

                        }
                        else{//2 + 1 + 0

                        }
                    }
                    else if (b[i].val == Base3Digit.ONE){
                        if(carry.val == Base3Digit.ZERO){

                        }
                        else if(carry.val == Base3Digit.ONE){

                        }
                        else{

                        }
                    }
                    else{
                        if(carry.val == Base3Digit.ZERO){

                        }
                        else if(carry.val == Base3Digit.ONE){

                        }
                        else{

                        }
                    }
                }
                else{
                    if(b[i].val == Base3Digit.ZERO)
                    {
                        if(carry.val == Base3Digit.ZERO){

                        }
                        else if(carry.val == Base3Digit.ONE){

                        }
                        else{

                        }
                    }
                    else if (b[i].val == Base3Digit.ONE){
                        if(carry.val == Base3Digit.ZERO){

                        }
                        else if(carry.val == Base3Digit.ONE){

                        }
                        else{

                        }
                    }
                    else{
                        if(carry.val == Base3Digit.ZERO){

                        }
                        else if(carry.val == Base3Digit.ONE){

                        }
                        else{

                        }
                    }
                }

            }


        }

        if(carry.val == Base3Digit.ONE){
            tmp.add(new trit(1));
        }
        else if (carry.val == Base3Digit.TWO){
            tmp.add(new trit(2));
        }

        trit[] ret = new trit[tmp.size()];

        for(int i = 0; i < tmp.size(); i++){
            ret[i] = tmp.get(i);
        }

        return ret;
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