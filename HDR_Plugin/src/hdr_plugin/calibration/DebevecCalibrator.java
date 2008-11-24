/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.calibration;

// Jama.Matrix;
import flanagan.math.Matrix;
import java.util.ArrayList;
import java.util.HashSet;

//import no.uib.cipr.matrix.DenseMatrix;
//import no.uib.cipr.matrix.DenseVector;
import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

/**
 *
 * @author Alex
 */
public class DebevecCalibrator {

    private int Zmin;
    private int Zmax;
    private Object[] imgPixels;
    private int noOfImagesQ;
    private int noOfPixelsP;
    private double[] shutterSpeeds;

    public DebevecCalibrator(Object[] imgPixels, int noOfImagesQ, int noOfPixelsP, double[] shutterSpeeds, int Zmin, int Zmax) {
        this.imgPixels = imgPixels;
        this.noOfImagesQ = noOfImagesQ;
        this.noOfPixelsP = noOfPixelsP;
        this.shutterSpeeds = shutterSpeeds;
        this.Zmax = Zmax;
        this.Zmax = Zmin;
    }

    private double w(int z) {
        if (z <= 0.5 * (Zmin + Zmax)) {
            return z - Zmin;
        } else {
            return Zmax - z;
        }
    }

    public void doIt() {
        RandomData rnd = new RandomDataImpl();
        HashSet<Integer> pixtemp = new HashSet<Integer>();
        while (pixtemp.size() < (noOfPixelsP)) {
   //         pixtemp.add(new Integer(rnd.nextInt(0, imgPixels[0]));
        }

        ArrayList<Integer> pixels = new ArrayList<Integer>(pixtemp);

        int n = 4096;
        int k = 0;
        double lambda = 10;

        double[][] a = new double[pixels.size() * noOfImagesQ + n - 1][n + pixels.size()];
        double[] b = new double[a.length];

        for (int i = 0; i < pixels.size(); i++) {                // for all pixels
            int pixelIndex = pixels.get(i);
            for (int j = 0; j < noOfImagesQ; j++) {                    // for all images
                // get pixels for current image
                //int[] img = (int[]) imgPixels[j];
                short[] img = (short[]) imgPixels[j];
                // read pixel with current index
                //int value = (int) (img[pixelIndex] & 0xff0000) >> 16;
                short value = (short) (img[pixelIndex] & 0xffff);
                double wij = w(value);
                if (wij == 0.) {
                    continue;
                }
                //System.out.println("a[" + k + "]" + "[" + value + "] = " + wij);
                a[k][value] = wij;
                a[k][n + i] = -wij;
                b[k] = wij * Math.log(shutterSpeeds[j]);
                k++;
            }
        }
        a[k][128] = 1.0;
        k++;

        for (int i = 0; i < n - 2; i++) {
            a[k][i] = lambda * w(i + 1);
            a[k][i + 1] = -2.0 * lambda * w(i + 1);
            a[k][i + 2] = lambda * w(i + 1);
            k++;
        }


        if (k < pixels.size() * noOfImagesQ + n - 1) {
            double[][] at = new double[k][n + pixels.size()];
            double[] bt = new double[k];
            at = subarray2D(a, 0, k - 1, 0, n + pixels.size() - 1);
            bt = subarray1D(b, 0, k - 1);
            a = at;
            b = bt;
        }

//            Jama.Matrix A = new Matrix(a);
//            Jama.Matrix B = new Matrix(b,b.length);
//            B.print(10, 10);
//            
//            Jama.Matrix X = A.solve(B);
//            
//            double[][] x = X.getArrayCopy();
//            System.out.println(x.length);
//            
//            for (int i = 0; i < x.length; i ++) {
//                for (int j = 0; j < x[i].length; j++) {
//                    System.out.println(x[i][j]);
//                }
//            }

        Matrix A = new Matrix(a);
        double[] x = A.solveLinearSet(b);

        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }

    // try {
    //DenseMatrix Am = new DenseMatrix(a);
    //DenseVector bm = new DenseVector(b);

    //SVD As = SVD.factorize(Am);

    //double[] result = solve(As.getVt(), As.getU(), As.getS(), bm, Am.numRows(), Am.numColumns());
    //System.out.println(new DenseVector(result).toString());

    //} catch (NotConvergedException ex) {
    //    Logger.getLogger(DebevecCalibrator.class.getName()).log(Level.SEVERE, null, ex);
    //}

    // System.out.println(xs.toString());
    }

    private double[][] subarray2D(double[][] in, int lb1, int ub1, int lb2, int ub2) {
        double out[][] = new double[ub1 - lb1 + 1][ub2 - lb2 + 1];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[i].length; j++) {
                out[i][j] = in[lb1 + i][lb2 + j];
            }
        }
        return out;
    }

    private double[] subarray1D(double[] in, int lb1, int ub1) {
        double out[] = new double[ub1 - lb1 + 1];
        for (int i = 0; i < out.length; i++) {
            out[i] = in[lb1 + i];
        }
        return out;
    }

    public int rank(double[] in, int m, int n) {
        double eps = Math.pow(2.0, -52.0);
        double tol = Math.max(m, n) * in[0] * eps;
        int r = 0;
        for (int i = 0; i < in.length; i++) {
            if (in[i] > tol) {
                r++;
            }
        }
        return r;
    }

//    public double[] solve(DenseMatrix Vt, DenseMatrix U, double[] s, DenseVector b, int m, int n) {
//        int rnk = rank(s, m, n);
//        DenseVector beta = new DenseVector(U.numRows());
//        // U^t * b = beta
//        U.transMult(b, beta);
//        double[] betaA = beta.getData();
//
//        double[] chi = new double[n];
//        for (int i = 0; i < rnk; i++) {
//            chi[i] = betaA[i] / s[i];
//        }
//
//        for (int i = rnk; i < n; i++) {
//            chi[i] = 0.0;
//        }
//
//        DenseVector chiVec = new DenseVector(chi);
//        System.out.println(Vt.numColumns());
//        DenseMatrix V = new DenseMatrix(Vt.transpose());
//        DenseVector xn = new DenseVector(V.numRows());
//        // V * chiVec = x
//        V.mult(chiVec, xn);
//
//        return xn.getData();
  //  }
}
