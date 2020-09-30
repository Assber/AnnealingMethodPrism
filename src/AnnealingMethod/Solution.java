package AnnealingMethod;


public class Solution {

    //   private Double r;
    private Double x1;
    private Double x2;
    private Double z1;
    private Double z2;
    private Double p;
    //  private Double h;
    private Double m;
    private Double wСalculated;
    private Double wTheoretical;
    private Double wDiff;

    public Solution() {
    }

    public Double getX1() {
        return x1;
    }

    public void setX1(Double x1) {
        this.x1 = x1;
    }

    public Double getX2() {
        return x2;
    }

    public void setX2(Double x2) {
        this.x2 = x2;
    }

    public Double getZ1() {
        return z1;
    }

    public void setZ1(Double z1) {
        this.z1 = z1;
    }

    public Double getZ2() {
        return z2;
    }

    public void setZ2(Double z2) {
        this.z2 = z2;
    }

//    public Double getR() {
//        return r;
//    }
//
//    public void setR(Double r) {
//        this.r = r;
//    }
    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

//    public Double getH() {
//        return h;
//    }
//
//    public void setH(Double h) {
//        this.h = h;
//    }
    public Double getM() {
        return m;
    }

    public void setM(Double m) {
        this.m = m;
    }

    public Double getwСalculated() {
        return wСalculated;
    }

    public void setwСalculated(Double wСalculated) {
        this.wСalculated = wСalculated;
    }

    public Double getwTheoretical() {
        return wTheoretical;
    }

    public void setwTheoretical(Double wTheoretical) {
        this.wTheoretical = wTheoretical;
    }

    public Double getwDiff() {
        return wDiff;
    }

    public void setwDiff(Double wDiff) {
        this.wDiff = wDiff;
    }

}
