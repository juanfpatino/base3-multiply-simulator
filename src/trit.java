public class trit {

    public Base3Digit val = Base3Digit.ZERO;

    public trit(Base3Digit val){
        this.val = val;
    }
    public trit(int val){
        switch (val) {
            case 1 -> this.val = Base3Digit.ONE;
            case 2 -> this.val = Base3Digit.TWO;
        }
    }

    @Override
    public String toString() {
        switch (val){
            case ZERO -> {
                return "0";
            }
            case ONE -> {
                return "1";
            }
            default -> {
                return "2";
            }
        }
    }
}
