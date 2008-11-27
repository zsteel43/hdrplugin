/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.helper;

import ij.ImagePlus;

/**
 *
 * @author Alex
 */
public class ImageJTools {

    private static int[][][] convert16bitGrayToInt(Object[] imageArray, int noOfImagesQ, int imgWidth, int imgHeight) {
        int[][][] converted = new int[1][imgWidth * imgHeight][noOfImagesQ];
        for (int i = 0; i < noOfImagesQ; i++) {
            for (int j = 0; j < imgWidth * imgHeight; j++) {
                converted[0][j][i] = (((int[]) imageArray[i])[j] & 0xffff);
            }
        }
        return converted;
    }

    private static int[][][] convertRGBToInt(Object[] imageArray, int noOfImagesQ, int imgWidth, int imgHeight) {
        int[][][] converted = new int[3][imgWidth * imgHeight][noOfImagesQ];
        for (int i = 0; i < noOfImagesQ; i++) {
            for (int j = 0; j < imgWidth * imgHeight; j++) {
                converted[0][j][i] = (((int[]) imageArray[i])[j] & 0xff0000) >> 16;
                converted[1][j][i] = (((int[]) imageArray[i])[j] & 0x00ff00) >> 8;
                converted[2][j][i] = (((int[]) imageArray[i])[j] & 0x0000ff);
            }
        }
        return converted;
    }

    public static int[][][] checkAndConvert(ImagePlus imp) {
        int type = imp.getType();
        if (type == ImagePlus.GRAY16) {
            return convert16bitGrayToInt(imp.getStack().getImageArray(), imp.getStackSize(), imp.getWidth(), imp.getHeight());
        }
        if (type == ImagePlus.COLOR_RGB) {
            return convertRGBToInt(imp.getStack().getImageArray(), imp.getStackSize(), imp.getWidth(), imp.getHeight());
        }
        return null;
    }

    public static double[][] subarray2D(double[][] in, int lb1, int ub1, int lb2, int ub2) {
        double out[][] = new double[ub1 - lb1 + 1][ub2 - lb2 + 1];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[i].length; j++) {
                out[i][j] = in[lb1 + i][lb2 + j];
            }
        }
        return out;
    }

    public static double[] subarray1D(double[] in, int lb1, int ub1) {
        double out[] = new double[ub1 - lb1 + 1];
        for (int i = 0; i < out.length; i++) {
            out[i] = in[lb1 + i];
        }
        return out;
    }

    static int rank(double[] in, int m, int n) {
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
}
