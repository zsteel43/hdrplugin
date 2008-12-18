/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.tonemapping.drago;

import ij.ImagePlus;

/**
 *
 * @author Alexander Heidrich <alexander.heidrich@hki-jena.de>
 */
public class DragoToneMapper {

    private ImagePlus imp;
    private double bias;

    public DragoToneMapper(ImagePlus imp) {
        this.imp = imp;
        this.bias = 0.85;
    }

    public double biasFunc(double b, double x) {
        return Math.pow(x, b);		// pow(x, log(bias)/log(0.5)
    }

    private float[] calculateLuminance(int size, float[] Y) {
        float avLum = 0.0f;
        float maxLum = 0.0f;

        for (int i = 0; i < size; i++) {
            avLum += Math.log(Y[i] + 1e-4);
            maxLum = (Y[i] > maxLum) ? Y[i] : maxLum;
        }
        avLum = (float) Math.exp(avLum / size);
        return new float[]{avLum, maxLum};
    }
    
    public double[] map() {

        double LOG05 = -0.693147; // log(0.5)

        int length = imp.getWidth() * imp.getHeight();	// image size
        double[] out = new double[length];
        float[] pixels = (float[]) imp.getChannelProcessor().getPixels();
        float[] luminance = calculateLuminance(length, pixels);
        double maxLum = luminance[1];
        double avLum = luminance[0];

        maxLum /= avLum; // normalize maximum luminance by average luminance

        double divider = Math.log10(maxLum + 1.0);
        double biasP = Math.log(bias) / LOG05;

        for (int y = 0; y < length; y++) {
            double Yw = pixels[y] / avLum;
            double interpol = Math.log(2.0f + biasFunc(biasP, Yw / maxLum) * 8.0f);
            out[y] = (Math.log(Yw + 1.0) / interpol) / divider;
        }

        return out;
    }
}
