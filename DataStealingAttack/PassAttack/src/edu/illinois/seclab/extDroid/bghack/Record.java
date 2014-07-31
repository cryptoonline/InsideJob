package edu.illinois.seclab.extDroid.bghack;

public class Record {

	private int year;
	private int mon;
	private int temp;
	private int result;
	private int event;
	private int hour;
	private int min;
	private int ampm;
	private int day;

	public Record(byte[] paramArrayOfByte, int paramInt)
	  {
	    this.year = (paramArrayOfByte[paramInt] >> 1);
	    this.mon = (((0x1 & paramArrayOfByte[paramInt]) << 3) + ((0xE0 & paramArrayOfByte[(paramInt + 1)]) >> 5));
	    this.day = (0x1F & paramArrayOfByte[(paramInt + 1)]);
	    this.temp = (0x3F & paramArrayOfByte[(paramInt + 2)] >> 2);
	    this.result = (((0x3 & paramArrayOfByte[(paramInt + 2)]) << 8) + (0xFF & paramArrayOfByte[(paramInt + 3)]));
	    this.event = ((0xF8 & paramArrayOfByte[(paramInt + 4)]) >> 3);
	    this.hour = (((0x7 & paramArrayOfByte[(paramInt + 4)]) << 2) + ((0xC0 & paramArrayOfByte[(paramInt + 5)]) >> 6));
	    this.min = (0x3F & paramArrayOfByte[(paramInt + 5)]);
	    this.ampm = 0;
	  }
	
	public int getAmpm()
	  {
	    return this.ampm;
	  }

	  public int getDay()
	  {
	    return this.day;
	  }

	  public int getEvent()
	  {
	    return this.event;
	  }

	  public int getHour()
	  {
	    return this.hour;
	  }

	  public int getMin()
	  {
	    return this.min;
	  }

	  public int getMon()
	  {
	    return this.mon;
	  }

	  public int getResult()
	  {
	    return this.result;
	  }

	  public int getTemp()
	  {
	    return this.temp;
	  }

	  public int getYear()
	  {
	    return this.year;
	  }

	  public int hashCode()
	  {

	    return 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 + this.ampm) + this.day) + this.event) + this.hour) + this.min) + this.mon) + this.result) + this.year;
	  }

	  public void setAmpm(int paramInt)
	  {
	    this.ampm = paramInt;
	  }

	  public void setDay(int paramInt)
	  {
	    this.day = paramInt;
	  }

	  public void setEvent(int paramInt)
	  {
	    this.event = paramInt;
	  }

	  public void setHour(int paramInt)
	  {
	    this.hour = paramInt;
	  }

	  public void setMin(int paramInt)
	  {
	    this.min = paramInt;
	  }

	  public void setMon(int paramInt)
	  {
	    this.mon = paramInt;
	  }

	  public void setResult(int paramInt)
	  {
	    this.result = paramInt;
	  }

	  public void setTemp(int paramInt)
	  {
	    this.temp = paramInt;
	  }

	  public void setYear(int paramInt)
	  {
	    this.year = paramInt;
	  }

	  public byte[] toBytes()
	  {
	    byte[] arrayOfByte = new byte[6];
	    arrayOfByte[0] = ((byte)((0xFE & this.year << 1) + (0x1 & this.mon >> 3)));
	    arrayOfByte[1] = ((byte)((0xE0 & this.mon << 5) + (0x1F & this.day)));
	    arrayOfByte[2] = ((byte)(((0x3F & this.temp) << 2) + (0x3 & this.result >> 8)));
	    arrayOfByte[3] = ((byte)(0xFF & this.result));
	    arrayOfByte[4] = ((byte)((0xF8 & this.event << 3) + (0x7 & this.hour >> 2)));
	    arrayOfByte[5] = ((byte)((0xC0 & this.hour << 6) + (0x3F & this.min)));
	    return arrayOfByte;
	  }

	  public String toString()
	  {
	    return "&yr=" + this.year + "&mo=" + this.mon + "&da=" + this.day + "&val=" + this.result + "&act=" + this.event + "&hr=" + this.hour + "&min=" + this.min;
	  }
}

