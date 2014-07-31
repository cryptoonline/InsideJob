package edu.illinois.seclab.extDroid.thermohack;

public class Utils {

	public static String convertTemp(int paramInt1, int paramInt2)
	  {
	    double d1 = paramInt2;
	    if (paramInt1 == 0)
	      d1 = 320.0D + 1.8D * paramInt2; //Celcius to Farenheit F*9/5 + 32
	    double d2 = d1 / 10.0D;
	    //return df.format(d2).replace(",", ".");
	    return "" + d2;
	  }
	//
}
