package com.myrev.rp.dm;


public interface IPath 
{
	public String getPathRoute();

	public void setPathInfo(String routeInfo);
	
	public String getPathName(); 
	
	public void setPathName(String lineName); 
	
	public boolean equals(String pathName,String pathRoute);
	
}

