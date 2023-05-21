package com.myrev.rp.dm;
import java.util.List;
import java.util.Map;

import com.myrev.rp.ex.InvalidStationException;


public interface IRouteMap 
{
	public String toString();
	
	public List<IPath> getLineList();
	
	public void setLineList(List<IPath> lineList);
	
	public Map getINodes();
	
	public IPath getLine(IPath line);
	
	public IPath getLine(String lineName);
	
	public void setINodes(Map nodes);
	
	public boolean lineAlreadyExists(String paramPathName);
	
	public boolean routeAlreadyExists(String pathName,String paramRouteInfo);
	
	public void addLine(IPath line);
	
	public INode getINode(String nodeName)  throws InvalidStationException;
	
	public IPath getPath(String pathName,String pathRoute);
	
}