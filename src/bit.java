import java.math.BigInteger;

public class bit {

    public Base2Digit val = Base2Digit.ZERO;

    public bit(Base2Digit val){
        this.val = val;
    }
    public bit(int val){
        if (val == 1)
            this.val = Base2Digit.ONE;
    }

    public bit(BigInteger val){
        if (!val.equals(BigInteger.ZERO))
            this.val = Base2Digit.ONE;
    }

    @Override
    public String toString() {
        if (val == Base2Digit.ONE)
            return "1";
        return "0";
    }

}
