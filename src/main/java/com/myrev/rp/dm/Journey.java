package com.myrev.rp.dm;
import java.util.List;

public class Journey 
{
	private List<Route> routeList; 
	
	public Journey(List<Route> routeList)
	{
		this.routeList = routeList;
	}
	
	
	// TODO refactor
	public int getMinChanges()
	{
		int minSoFar = Integer.MAX_VALUE;
		for (int i = 0; i < routeList.size(); i++)
		{
			Route route = routeList.get(i);
			
			if (minSoFar > route.getNumberChanges())
			{
				minSoFar = route.getNumberChanges();
			}
		}
		return minSoFar;
	}
	
	
	
	// TODO refactor
	public String toString()
	{
		String output = "";
		for (int i = 0; i < routeList.size(); i++)
		{
			Route route = (Route)routeList.get(i);
			route.toString();
		}
		
		return output;
	}
	
	public void addRoute(Route route)
	{
		routeList.add(route);
	}


	public List<Route> getRouteList() {
		return routeList;
	}


	public void setRouteList(List<Route> routeList) {
		this.routeList = routeList;
	}
	
}
