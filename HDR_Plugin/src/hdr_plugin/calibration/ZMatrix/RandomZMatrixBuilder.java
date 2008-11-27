/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.calibration.ZMatrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class RandomZMatrixBuilder implements ZMatrixBuilder {

    private int[][] imgPixels;
    private int noOfImagesQ;
    private int noOfPixelsP;
    private int noOfChannels;
    private int imgWidth;
    private int imgHeight;
    private Random rnd = new Random();

    public RandomZMatrixBuilder(int[][] imgPixels, int noOfImagesQ, int noOfPixelsP, int noOfChannels, int imgWidth, int imgHeight) {
        this.imgPixels = imgPixels;
        this.noOfImagesQ = noOfImagesQ;
        this.noOfPixelsP = noOfPixelsP;
        this.noOfChannels = noOfChannels;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
    }

    public int[][][] getZ() {
        // collect random image positions
        HashSet<Integer> pixtemp = new HashSet<Integer>();
        while (pixtemp.size() < (noOfPixelsP)) {
            pixtemp.add(getNextRandom());
        }
        // convert to ArrayList for easier access to the elements
        ArrayList<Integer> pixels = new ArrayList<Integer>(pixtemp);
        // create new Z matrix
        int[][][] Z = new int[noOfChannels][noOfPixelsP][noOfImagesQ];
        // fill matrix
        for (int i = 0; i < Z.length; i++) { // for all channels
            for (int j = 0; j < Z[i].length; j++) { // for all pixels
                for (int k = 0; j < Z[i][j].length; k++) { // for all images
                    Z[i][j][k] = imgPixels[pixels.get(i)][j];
                }
            }
        }
        return Z;
    }

    private int getNextRandom() {
        return rnd.nextInt(imgWidth * imgHeight);
    }
}
