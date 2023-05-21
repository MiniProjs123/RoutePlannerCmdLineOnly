package com.myrev.rp.engine;
import java.util.LinkedList;
import java.util.List;
import com.myrev.rp.dm.Edge;
import com.myrev.rp.dm.INode;
import com.myrev.rp.dm.IPath;
import com.myrev.rp.dm.IRouteMap;
import com.myrev.rp.dm.Route;
import com.myrev.rp.ex.NoJourneyFoundException;


public class ReadPredecessors 
{
	private IRouteMap map;
	private boolean startReached = false;
	private boolean atDestination = true;
	private List<Route> routeBuildList;
	private LinkedList<Route> cloneReserve;
	private IRouteEnquiry dijkstra;
	
	public ReadPredecessors(IRouteMap map, IRouteEnquiry dijkstra)
	{
		this.map = map;
		this.dijkstra = dijkstra;
		cloneReserve = new LinkedList();
		clearBuildRouteTools();
	}	
	

	
	public void clearBuildRouteTools()
	{
	    startReached = false;
		atDestination = true;
		routeBuildList = new LinkedList<Route>();
		cloneReserve.clear();
	}
	
	
	
	public List<Route> getRoutes()
	{
	    if (startReached)
	    {
	        return routeBuildList;
	    }
	    return null;
	}
	
	
	public void generateRoutes(INode start, INode finalDest) throws NoJourneyFoundException
	{
		clearBuildRouteTools();
		while (! startReached)
	    {
	        if (! atDestination)
	        {
	            nextBuildStage(finalDest,start);	
	        }
	        else
	        {
	            firstBuildStage(finalDest,start);
	            atDestination = false;
	        } 
	    }  
	}
	
	
	
	private void firstBuildStage(INode finalDest, INode start) throws NoJourneyFoundException
	{
		List<INode> predList = dijkstra.getPredecessorList(finalDest);
		for (int i = 0; i < predList.size(); i++)
	    {
			LinkedList<Edge> edgeList = new LinkedList<Edge>();
			Edge predEdge = (Edge)predList.get(i);
			checkAgainstStart(predEdge, start);
			edgeList.add(predEdge);
	    	Route route = new Route(edgeList);
	    	routeBuildList.add(route);
	    }
	}

	
	
	

	private void nextBuildStage(INode finalDest, INode start) throws NoJourneyFoundException
	{
		List<Route> tmpRouteBuildList = new LinkedList();
		for (int i = 0; i < routeBuildList.size(); i++)//each route in routeBuildList
	    {
	        Route route = (Route)routeBuildList.get(i);
	        INode earlyNode = route.getEarliestNode();
	        LinkedList<Edge> predList = dijkstra.getPredecessorList(earlyNode);
	        cloneManager(route,predList);
	        for (int j = 0; j < predList.size(); j++)
	        {
	        	Edge predEdge = (Edge)predList.get(j);
	        	checkAgainstStart(predEdge, start);
	        	// clone route if more than one predecessor exists for this node 
	        	if (j > 0)
	        	{
	        		route = cloneReserve.removeFirst();
	        	}
	        	route.addEdgeFirst(predEdge);
	        	tmpRouteBuildList.add(route);
	        }
	    }

	    routeBuildList = tmpRouteBuildList;
	}



	
	private void checkAgainstStart(Edge predEdge, INode start)
	{
		if (predEdge.getStart().equals(start))
    	{
    		startReached = true;
    	}
	}
	
	
	
	
	private void cloneManager(Route route, LinkedList predList)
	{
		int clonesRequired = 0;
		if (predList != null)
		{
			clonesRequired = predList.size() - 1;
		}
		while (clonesRequired > 0)
		{
			Route clone = cloneRoute(route);
			cloneReserve.add(clone);
			clonesRequired--;
		}
	}
	
	
	

	private Route cloneRoute(Route route)
	{
		LinkedList edgeList = route.getEdgeList();
		 
		LinkedList clonedEdgeList = new LinkedList();
		for (int i = 0; i < edgeList.size(); i++)
		{
		    Edge origEdge = (Edge)edgeList.get(i);
		    Edge clonedEdge = cloneEdge(origEdge);
		    clonedEdgeList.addLast(clonedEdge);
		}
		return new Route(clonedEdgeList);
	 }
	

	
	
	
	private Edge cloneEdge(Edge origEdge)
	{
		INode start = origEdge.getStart();
		INode dest = origEdge.getDest();
		int weighting = origEdge.getWeighting();
		IPath line = origEdge.getLine();
		
		return new Edge(start, dest, weighting, line);
		
	}
	
	
	
	
	
}
