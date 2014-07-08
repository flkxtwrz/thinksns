package com.hudson.thinksns.model;

import java.io.Serializable;

public class Notify implements Serializable{
	/**
	 * 
	 * @author º÷ÏÕ≥¨
	 * 
	 */
	private static final long serialVersionUID = -4581668662628994670L;
  private String type;
  private String name;
  private int count;
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Notify other = (Notify) obj;
	if (count != other.count)
		return false;
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	if (type == null) {
		if (other.type != null)
			return false;
	} else if (!type.equals(other.type))
		return false;
	return true;
}
@Override
public String toString() {
	return "Nofity [type=" + type + ", name=" + name + ", count=" + count + "]";
}
  
}
