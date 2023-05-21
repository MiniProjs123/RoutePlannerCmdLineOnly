package com.myrev.rp.lu.dm;
import com.myrev.rp.dm.IPath;

public class Line implements IPath
{
	private String lineName;
	private String routeInfo;
	
	public Line(String lineName,String routeInfo) 
	{
		this.lineName = lineName;
		this.routeInfo = routeInfo;
	}
	
	public String getPathRoute()
	{
		return this.routeInfo;
	}
	
	
	public void setPathInfo(String routeInfo)
	{
		this.routeInfo = routeInfo;
	}
	
	
	public String getPathName() {
		return lineName;
	}

	public void setPathName(String lineName) {
		this.lineName = lineName;
	}

		
	public static String refineLineName(String fullPathName)
	{
		String processString = fullPathName;
		int position = processString.indexOf("(") - 1;
		if (position > 0)
		{
			processString = processString.substring(0,position).trim();
		}
		return processString;
	}

	
	public static String refineRouteInfo(String fullPathName)
	{
		if (fullPathName.indexOf("(") < 0)
		{
			return "";
		}
		String processString = fullPathName;
		int openIndex = processString.indexOf("(") + 1;
		int closeIndex = processString.indexOf(")");
		if (openIndex > 0 && closeIndex > 0)
		{
			processString = processString.substring(openIndex,closeIndex); 
		}
		return processString;
	}
	
	public boolean equals(String pathName,String pathRoute)
	{
		IPath paramPath = new Line(pathName.trim(),pathRoute.trim());
		if (this.lineName.equals(pathName) && this.routeInfo.equals(pathRoute))
		{
			return true;
		}
		return false;
	}
	
	
}
