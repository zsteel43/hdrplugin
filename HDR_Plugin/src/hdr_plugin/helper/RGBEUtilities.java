
package hdr_plugin.helper;

/**
 *
 * @author  bjw
 */
public abstract class RGBEUtilities {

  private static float convertMultiplier[];

  static {
    //prebuild table of conversion multipliers
    convertMultiplier = new float[256];
    convertMultiplier[0] = 0; //exponent of zero means a zero value
    for(int e=1;e<256;e++) {
      //multiplier to convert byte to float for corresponding value of exponent e
      convertMultiplier[e] = (float)Math.pow(2.0, e - (128+8));
    }
  }

  /** Creates a new instance of RGBEUtilties */
  public RGBEUtilities() {
  }

  public static void convertRGBEToRGB(float outRGB[], int rgbOffset, byte inRGBE[], int rgbeOffset, int numPixels) {
    for(int i=0;i<numPixels;i++) {
      int byteOffset = 4*(i + rgbeOffset);
      int floatOffset = 3*(i + rgbOffset);
      int r = inRGBE[byteOffset+0]&0x0ff;
      int g = inRGBE[byteOffset+1]&0x0ff;
      int b = inRGBE[byteOffset+2]&0x0ff;
      int e = inRGBE[byteOffset+3]&0x0ff;
      float m = convertMultiplier[e];
      outRGB[floatOffset+0] = r*m;
      outRGB[floatOffset+1] = g*m;
      outRGB[floatOffset+2] = b*m;
    }
  }

  public static void convertRGBToRGBE(byte outRGBE[], int rgbeOffset, float inRGB[], int rgbOffset, int numPixels) {
    for(int i=0;i<numPixels;i++) {
      int floatOffset = 3*(i+rgbOffset);
      int byteOffset = 4*(i+rgbeOffset);
      float redF = inRGB[floatOffset+0];
      float greenF = inRGB[floatOffset+1];
      float blueF = inRGB[floatOffset+2];
      if (Float.isNaN(redF)||Float.isNaN(greenF)||Float.isNaN(blueF)) {
        System.out.println("Warning NAN input value; will truncate to zero in "+RGBEUtilities.class.toString());
        outRGBE[byteOffset+0] = outRGBE[byteOffset+1] = outRGBE[byteOffset+2] = outRGBE[byteOffset+3] = 0;
        continue;
      }
      //negative values cannot be encoded, so we truncate them to zero
      if (redF < 0) redF = 0;
      if (greenF < 0) greenF = 0;
      if (blueF < 0) blueF = 0;
      //find the largest value of the three color components
      float max = (redF > greenF)?redF:greenF;
      max = (blueF > max)?blueF:max;
      //    float max = Math.max(inRed,Math.max(inGreen,inBlue));
      if (max < 1e-32) { //consider all values less than this to be zero.  This constant comes from Ward's definition in "Real Pixels" Graphics Gems II
        outRGBE[byteOffset+0] = outRGBE[byteOffset+1] = outRGBE[byteOffset+2] = outRGBE[byteOffset+3] = 0;
        continue;
      }
      //extract the exponent from the IEEE single precision floating point number
      int biasedExponent = ((Float.floatToIntBits(max)>>23)&0x0FF);
      if (biasedExponent>253) {
        System.out.println("warning: rgbe overflow in "+RGBEUtilities.class.toString());
        outRGBE[byteOffset+0] = outRGBE[byteOffset+1] = outRGBE[byteOffset+2] = outRGBE[byteOffset+3] = (byte)0xFF;
        continue;
      }
      //construct a additive normalizer which is just 2^(exp+1).
      //Adding this to each float will move the relevant mantissa bits to a known fixed location for easy extraction
      float additiveNormalizer = Float.intBitsToFloat((biasedExponent+1)<<23);
      //initially we keep an extra bit (9-bits) so that we can perform rounding to 8-bits in the next step
      int rawR = (Float.floatToIntBits(redF+additiveNormalizer)>>14)&0x1FF;
      int rawG = (Float.floatToIntBits(greenF+additiveNormalizer)>>14)&0x1FF;
      int rawB = (Float.floatToIntBits(blueF+additiveNormalizer)>>14)&0x1FF;
      //rgbeBiasedExponent = (ieeeBiasedExponent-127) + 129  since IEEE single float and rgbe have different exponent bias values
      int e = biasedExponent + 2;
      //round to nearest representable 8 bit value
      int r = (rawR+1)>>1;
      int g = (rawG+1)>>1;
      int b = (rawB+1)>>1;
      //check to see if rounding causes an overflow condition and fix if necessary
      if ((r>255)||(g>255)||(b>255)) {
        //ooops rounding caused overflow, need to use larger exponent and redo the rounding
        e += 1;
        r = (rawR+2)>>2;
        g = (rawG+2)>>2;
        b = (rawB+2)>>2;
        if (e > 255) {
          System.out.println("warning: rgbe overflow (after rounding) in "+RGBEUtilities.class.toString());
          outRGBE[byteOffset+0] = outRGBE[byteOffset+1] = outRGBE[byteOffset+2] = outRGBE[byteOffset+3] = (byte)0xFF;
          continue;
        }
      }
      outRGBE[byteOffset+0] = (byte)r;
      outRGBE[byteOffset+1] = (byte)g;
      outRGBE[byteOffset+2] = (byte)b;
      outRGBE[byteOffset+3] = (byte)e;
    }
  } 

}