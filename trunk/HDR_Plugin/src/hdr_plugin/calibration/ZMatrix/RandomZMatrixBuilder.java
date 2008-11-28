/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.calibration.ZMatrix;

import hdr_plugin.Exceptions.TypeNotSupportedException;
import hdr_plugin.helper.ImageJTools;
import hdr_plugin.response.ResponseFunctionCalculatorSettings;
import ij.ImagePlus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author Alexander Heidrich
 */
public class RandomZMatrixBuilder implements ZMatrixBuilder {

    private ImagePlus imp;
    private int noOfImagesP;
    private int noOfPixelsN;
    private int noOfChannels;
    private int imgWidth;
    private int imgHeight;
    private Random rnd = new Random();

    public RandomZMatrixBuilder(ImagePlus imp, ResponseFunctionCalculatorSettings settings) {
        this.imp = imp;
        this.noOfPixelsN = settings.getNoOfPixelsN();
        this.noOfChannels = settings.getNoOfChannels();
        this.noOfImagesP = settings.getNoOfImagesP();
        this.imgHeight = settings.getHeight();
        this.imgWidth = settings.getWidth();
    }

    public int[][][] getZ() throws TypeNotSupportedException {
        // collect random image positions
        HashSet<Integer> pixtemp = new HashSet<Integer>();
        System.out.println("Start random");
        while (pixtemp.size() < (noOfPixelsN)) {
            pixtemp.add(getNextRandom());
        }
        System.out.println("End random");
        // convert to ArrayList for easier access to the elements
        ArrayList<Integer> pixels = new ArrayList<Integer>(pixtemp);
        // create new Z matrix
        int[][][] Z = new int[noOfChannels][noOfPixelsN][noOfImagesP];
        // fill matrix
        System.out.println("start matrix");
        for (int i = 0; i < Z.length; i++) { // for all channels
            for (int j = 0; j < Z[i].length; j++) { // for all pixels
                for (int k = 0; k < Z[i][j].length; k++) { // for all images
                    Z[i][j][k] = ImageJTools.getPixelValue(imp.getImageStack().getPixels(k), i, k, noOfChannels);
                }
            }
        }
        System.out.println("end matrix");
        return Z;
    }

    private int getNextRandom() {
        return rnd.nextInt(imgWidth * imgHeight);
    }
}
