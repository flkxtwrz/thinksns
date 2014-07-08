package com.hudson.thinksns.netuti;

import org.apache.http.NameValuePair;
/**
 * 
 * @author Íõ´úÒø
 * 
 */
public class MyNameValuePair implements NameValuePair {
  private String name;
  private String value;
  
	public MyNameValuePair(String name, String value) {
	super();
	this.name = name;
	this.value = value;
}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return value;
	}

}
