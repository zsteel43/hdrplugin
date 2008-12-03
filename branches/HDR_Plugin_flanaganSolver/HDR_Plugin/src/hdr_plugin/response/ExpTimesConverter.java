/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.response;

import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author Alex
 */
public class ExpTimesConverter extends Converter<String, Double[]> {

    @Override
    public Double[] convertForward(String value) {
        try {
            String[] values = value.split(",");
            Double[] out = new Double[values.length];
            for (int i = 0; i < out.length; i++) {
                out[i] = new Double(values[i].trim());
            }
            return out;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertReverse(Double[] value) {
        try {
            String out = new String();
            for (int i = 0; i < value.length; i++) {
                out = out + value[i].toString() + ", ";
            }
            return out;
        } catch (Exception e) {
            return "";
        }
    }
}
