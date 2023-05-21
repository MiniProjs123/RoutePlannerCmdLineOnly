package com.myrev.rp.engine;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.myrev.rp.dm.Edge;
import com.myrev.rp.dm.INode;
import com.myrev.rp.dm.IRouteMap;
import com.myrev.rp.dm.Journey;
import com.myrev.rp.dm.Route;
import com.myrev.rp.ex.DuplicateStationException;
import com.myrev.rp.ex.InvalidStationException;
import com.myrev.rp.ex.NoJourneyFoundException;
import com.myrev.rp.output.EnquiryOutput;
import org.apache.log4j.Logger;


public class RoutePlanner implements IRoutePlanner 
{
	final static Logger logger = Logger.getLogger(RoutePlanner.class);
	
	private IRouteEnquiry dijkstra;
	private ReadPredecessors predReader;
	private EnquiryOutput enquiryOutput;
	private LegOrchestrator legMaker;
	private Journey journey;
	private IRouteMap iRouteMap;
	
	public RoutePlanner(IRouteMap iRouteMap) throws IOException 
	{
		this.iRouteMap = iRouteMap;
		dijkstra = new DijkstraRouteEnquiry(iRouteMap);	
	    predReader = new ReadPredecessors(iRouteMap,dijkstra);
	    legMaker = new LegOrchestrator(iRouteMap);
	    enquiryOutput = new EnquiryOutput();
	    logger.info("Route Planner has been set up successfully......");
	}
	
	
	// TODO refactor
	public List<String> getIRouteMap()
	{
		List stationList = new LinkedList();
		Map<String,INode> stations = iRouteMap.getINodes(); 
		
		Set<String> keySet = stations.keySet();
		for (String key : keySet)
		{
		    String value = ((INode)stations.get(key)).getName();
		    stationList.add(value);	
		} 
		Collections.sort(stationList);
		return stationList;
	}
	
	
	
	public String getJourneyString(Journey journey)
	{
		return enquiryOutput.getJourneyOutput(journey, false);
	}
	
	
	public String getJourneyString(Journey journey, boolean htmlPage)
	{
		return enquiryOutput.getJourneyOutput(journey, htmlPage);
	}
	
	
	
	public void checkInput(String start, String destination) throws NoJourneyFoundException, 
	                                                   InvalidStationException, DuplicateStationException
	{
		if (start.equals("")  ||  destination.equals(""))
		{
			 throw new InvalidStationException("Either the start or destination is not an actual station. Please try again.");
		}
		
		if (start.equals(destination))
		{
			throw new DuplicateStationException("The start and destination should be different. Please try again.");
		}
	}
	
	
	
	/** 
     * By entering a valid start station and destination station an intelligent
     * journey object is returned.
     * @throws InvalidStationException - if the station does not exist within the network
     * @throws DuplicateStationException - if the start and destination stations are the same
     * @throws NoJourneyFoundException - if no journey can be found for any given stations
     * @return {@link Journey} - the quickest journey
     */
	public Journey lookupJourney(String start, String destination) throws NoJourneyFoundException, 
    												InvalidStationException, DuplicateStationException
    {
		checkInput(start,destination);
		INode startNode = iRouteMap.getINode(start);
		INode destNode = iRouteMap.getINode(destination);
		dijkstra.execute(startNode, destNode);
		predReader.generateRoutes(startNode, destNode);
		List<Route> routeList = predReader.getRoutes();
		Journey journey = new Journey(routeList); 
		legMaker.makeJourneyOutput(journey);
		return journey;
	}
	
	
		
	/**
	 * Clears any previous journeys
	 * 
	 */
	public void clearJourney()
	{
		predReader.clearBuildRouteTools();
	}
	
	
	
	/** 
	 * Each shortest route is determined and the number of line changes for all routes is returned
	 * n.b this method is only here in tribute to Dijkstra - it serves no purpose really
	 * @throws NoJourneyFoundException - if the routePlanner can find no routes between to stations
	 * @return int - the number of line changes for all routes 
	 */
	public int getLineChangesCountAllRoutes() throws NoJourneyFoundException
    {
		// ie. routes * stations
		int totalLineChanges = 0;
		List<Route> routeList = journey.getRouteList();
		for (int i = 0; i < routeList.size(); i++)
		{
			Route route = routeList.get(i);
			int legs = ((LinkedList)route.getSummaryLegs()).size();
			int lineChanges = legs - 1;
			totalLineChanges += lineChanges;
		}
		return totalLineChanges;
	}
     
     
	
    /** 
     * Gets the number of possible routes between two stations
     * @throws NoJourneyFoundException - if the routePlanner can find no routes between to stations
     * @return int - Number of routes found having equal shortest distances between start station and 
     *               destination station 
     */
	public int getPossibleRoutesCount() throws NoJourneyFoundException
    {
		List<Route> routeList = journey.getRouteList();
		return routeList.size();
    }
    
    
    
    /** NOW
     * Gets the least number of stops between the start and destination stations
     * @throws NoJourneyFoundException - if the routePlanner can find no routes between two stations
     * @return int - The least number of stops between the start and destination stations
     */
	public int getStationsCount() throws NoJourneyFoundException
    {
		List<Route> routeList = journey.getRouteList();
		Route route = routeList.get(0);
		List<Edge> edgeList = route.getEdgeList();
		return edgeList.size();
	}
	
    
}
	
	
	
	
	
	
	
	
