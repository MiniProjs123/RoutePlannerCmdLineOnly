package com.myrev.rp.dm;
import com.myrev.rp.lu.dm.Station;


public class FactoryINode 
{
	public static final int STATION = 1;
	public static final int JUNCTION = 2;
	public static final int CITY = 3;
	
	
	public FactoryINode()
	{
		
	}
	
	
	public INode makeINode(int iNodeType, String iNodeName)
	{
		return iNodeType == STATION ? new Station(iNodeName) : null;
	}
	
}
