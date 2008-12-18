/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdr_plugin.radiance;

import hdr_plugin.Exceptions.TypeNotSupportedException;

/**
 *
 * @author Alexander Heidrich <alexander.heidrich@hki-jena.de>
 */
public interface RadianceMapConstructor {
    public double[] calcRadiance(int channel) throws TypeNotSupportedException;
}
