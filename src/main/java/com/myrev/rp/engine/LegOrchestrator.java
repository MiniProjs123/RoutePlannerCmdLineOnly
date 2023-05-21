package com.myrev.rp.engine;
import java.util.LinkedList;
import java.util.List;
import com.myrev.rp.dm.Edge;
import com.myrev.rp.dm.INode;
import com.myrev.rp.dm.IPath;
import com.myrev.rp.dm.IRouteMap;
import com.myrev.rp.dm.Journey;
import com.myrev.rp.dm.Route;


// - Organises data in leg format, ie. for each succession of nodes on an IPath, and stores that in a Journey
//    against the detailed edges 
// - Removes routes with too many objects from Journey object
public class LegOrchestrator 
{
	private List<Edge> edgeList; 
	private List<Edge> legData;
	private int endIndex;
	private Edge lastEdge;
	private IPath lastPath;
	private int distance;
	private int journeyDistance;
	private INode startLeg;
	private IRouteMap iRouteMap;
	

	// TODO refactor
	
	
	public LegOrchestrator(IRouteMap iRouteMap)
	{
		legData = new LinkedList<Edge>();
		this.iRouteMap = iRouteMap;
	}
	
	protected void makeJourneyOutput(Journey journey)
	{
		List<Route> routeList = journey.getRouteList();
		for (int i = 0; i < routeList.size(); i++)
		{
			Route route = routeList.get(i);
			makeRouteOutput(route);
			route.setSummaryLegs(legData);
		}
		rejectExtraChanges(journey);
	}
	
	
	
	private void makeRouteOutput(Route route)
	{
		initOneRouteValues(route);
		for (int i = 1; i < edgeList.size(); i++)
		{
			Edge nextEdge = edgeList.get(i);
			if (! nextEdge.onSamePath(lastEdge))
			{
				setLeg(lastEdge.getDest());
				startLeg = nextEdge.getStart();
				distance = nextEdge.getWeighting();
				updateOneRouteValues(nextEdge);
			}
			else 
			{
				distance += nextEdge.getWeighting();
				updateOneRouteValues(nextEdge);
			}
			if (i == endIndex)
			{
				setLeg(nextEdge.getDest());
			}
		}
	}

	
	
	private void setLeg(INode endLeg)
	{
		Edge journeyLeg = new Edge(startLeg, endLeg, distance, lastPath);
		legData.add(journeyLeg);
	}
	
	
	private void initOneRouteValues(Route route)
	{
		INode finalDestNode = route.getDestNode();
		edgeList = route.getEdgeList();
		endIndex = edgeList.size() - 1;
		legData = new LinkedList<Edge>();
		lastEdge = edgeList.get(0);
		if (lastEdge.getDest().equals(finalDestNode))
		{
			legData.add(lastEdge);
		}
		lastPath = lastEdge.getLine();
		distance = lastEdge.getWeighting();
		journeyDistance = distance;
		startLeg = lastEdge.getStart();
	}


	private void updateOneRouteValues(Edge nextEdge)
	{
		journeyDistance += distance; 
		lastEdge = nextEdge;
		lastPath = nextEdge.getLine();
	}
	
	
	
	private void rejectExtraChanges(Journey journey)  
	{
		int minChanges = journey.getMinChanges(); 
		List<Route> routeList = journey.getRouteList();
		List<Route> newRouteList = new LinkedList();
		for (int i = 0; i < routeList.size(); i++)
		{
			Route route = routeList.get(i);
			if (route.getNumberChanges() == minChanges)
			{
				newRouteList.add(route);
				
			}
		}
		journey.setRouteList(newRouteList);
	}
	
	
	
}
