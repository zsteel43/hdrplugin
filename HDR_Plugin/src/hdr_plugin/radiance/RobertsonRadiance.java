/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.radiance;

import hdr_plugin.Exceptions.TypeNotSupportedException;
import hdr_plugin.helper.ImageJTools;
import hdr_plugin.response.ResponseFunction;
import ij.ImagePlus;

/**
 *
 * @author Alexander Heidrich <alexander.heidrich@hki-jena.de>
 */
public class RobertsonRadiance implements RadianceMapConstructor {

    private ImagePlus imp;
    private ResponseFunction r;
    private double[] expTimes;
    private double[] out;

    public RobertsonRadiance(ImagePlus imp, ResponseFunction r, double[] expTimes) {
        this.imp = imp;
        this.r = r;
        this.expTimes = expTimes;
        this.out = new double[imp.getWidth()*imp.getHeight()];
    }

    public double[] calcRadiance(int channel) throws TypeNotSupportedException {

        // image type
        int type = imp.getType();

        // number of levels
        int M = (r.getG())[channel].length;

        // number of exposures
        int N = imp.getStackSize();

        // frame size
        int width = imp.getWidth();
        int height = imp.getHeight();

        // number of saturated pixels
        int saturated_pixels = 0;

        // --- anti saturation: calculate trusted camera output range
        int minM = 0;
        for (int m = 0; m < M; m++) {
            if (r.getW().w(m) > 0.) {                
                minM = m;
                break;
            }
        }

        int maxM = M - 1;
        for (int m = M - 1; m >= 0; m--) {
            if (r.getW().w(m) > 0.) {
                maxM = m;
                break;
            }
        }

        // --- anti ghosting: for each image i, find images with
        // the immediately higher and lower exposure times
        int[] i_lower = new int[N];
        int[] i_upper = new int[N];
        for (int i = 0; i < N; i++) {
            i_lower[i] = -1;
            i_upper[i] = -1;
            double ti = expTimes[i];
            double ti_upper = expTimes[0];
            double ti_lower = expTimes[0];

            for (int j = 0; j < N; j++) {
                if (i != j) {
                    if (expTimes[j] > ti && expTimes[j] < ti_upper) {
                        ti_upper = expTimes[j];
                        i_upper[i] = j;
                    }
                    if (expTimes[j] < ti && expTimes[j] > ti_lower) {
                        ti_lower = expTimes[j];
                        i_lower[i] = j;
                    }
                }
            }

            if (i_lower[i] == -1) {
                i_lower[i] = i;
            }
            if (i_upper[i] == -1) {
                i_upper[i] = i;
            }
        }

        // all pixels
        for (int j = 0; j < width * height; j++) {
            // all exposures for each pixel
            double sum = 0.0;
            double div = 0.0;

            double maxti = -1.0e6;
            double minti = +1.0e6;

            for (int i = 0; i < N; i++) {
                Object pixels = imp.getImageStack().getPixels(i+1);
                // get pixel value at position j and the current channel
                int m = ImageJTools.getPixelValue(pixels, j, type, channel);
                double ti = expTimes[i];

                // --- anti saturation: observe minimum exposure time at which
                // saturated value is present, and maximum exp time at which
                // black value is present
                if (m > maxM) {
                    minti = Math.min(minti, ti);
                }
                if (m < minM) {
                    maxti = Math.max(maxti, ti);
                }

                // --- anti ghosting: monotonous increase in time should result
                // in monotonous increase in intensity; make forward and
                // backward check, ignore value if condition not satisfied
                Object pixels_lower = imp.getImageStack().getPixels(i_lower[i]+1);
                Object pixels_upper = imp.getImageStack().getPixels(i_upper[i]+1);
                int m_lower = ImageJTools.getPixelValue(pixels_lower, j, type, channel);
                int m_upper = ImageJTools.getPixelValue(pixels_upper, j, type, channel);
                if (m_lower > m || m_upper < m) {
                    continue;
                }

                sum += r.getW().w(m) * ti * r.getG()[channel][m];
                div += r.getW().w(m) * ti * ti;
            }

            // --- anti saturation: if a meaningful representation of pixel
            // was not found, replace it with information from observed data
            if (div == 0.0) {
                saturated_pixels++;
            }
            if (div == 0.0 && maxti > -1e6) {
                sum = r.getG()[channel][minM];
                div = maxti;
            }
            if (div == 0.0f && minti < +1e6) {
                sum = r.getG()[channel][maxM];
                div = minti;
            }

            if (div != 0.0) {
                out[j] = sum / div;
            } else {
                out[j] = 0.0;
            }
        }

        return out;
    }

    /**
     * @param r the r to set
     */
    public void setR(ResponseFunction r) {
        this.r = r;
    }
}
