/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.calibration;

/**
 *
 * @author Alex
 */
public class ResponseFunctionCalculatorSettings {
    private int noOfImagesP;
    private int noOfPixelsN;
    private int noOfChannels;
    private double[] expTimes;
    private int Zmin;
    private int Zmax;

    public ResponseFunctionCalculatorSettings() {
    }

    /**
     * @return the noOfImagesP
     */
    public int getNoOfImagesP() {
        return noOfImagesP;
    }

    /**
     * @param noOfImagesP the noOfImagesP to set
     */
    public void setNoOfImagesP(int noOfImagesP) {
        this.noOfImagesP = noOfImagesP;
    }

    /**
     * @return the noOfPixelsN
     */
    public int getNoOfPixelsN() {
        return noOfPixelsN;
    }

    /**
     * @param noOfPixelsN the noOfPixelsN to set
     */
    public void setNoOfPixelsN(int noOfPixelsN) {
        this.noOfPixelsN = noOfPixelsN;
    }

    /**
     * @return the noOfChannels
     */
    public int getNoOfChannels() {
        return noOfChannels;
    }

    /**
     * @param noOfChannels the noOfChannels to set
     */
    public void setNoOfChannels(int noOfChannels) {
        this.noOfChannels = noOfChannels;
    }

    /**
     * @return the shutterSpeeds
     */
    public double[] getExpTimes() {
        return expTimes;
    }

    /**
     * @param shutterSpeeds the shutterSpeeds to set
     */
    public void setExpTimes(double[] expTimes) {
        this.expTimes = expTimes;
    }

    /**
     * @return the Zmin
     */
    public int getZmin() {
        return Zmin;
    }

    /**
     * @param Zmin the Zmin to set
     */
    public void setZmin(int Zmin) {
        this.Zmin = Zmin;
    }

    /**
     * @return the Zmax
     */
    public int getZmax() {
        return Zmax;
    }

    /**
     * @param Zmax the Zmax to set
     */
    public void setZmax(int Zmax) {
        this.Zmax = Zmax;
    }
}
