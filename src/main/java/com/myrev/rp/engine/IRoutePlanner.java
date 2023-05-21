package com.myrev.rp.engine;
import java.util.List;
import com.myrev.rp.dm.Journey;
import com.myrev.rp.ex.DuplicateStationException;
import com.myrev.rp.ex.InvalidStationException;
import com.myrev.rp.ex.NoJourneyFoundException;


public interface IRoutePlanner 
{

		/**
		 * Clears any previous journeys
		 * 
		 */
		void clearJourney();
		 
	    
		/**
		 * Each shortest route is determined and the number of line changes for all routes is returned
		 * n.b this method is only here in tribute to Dijkstra - it serves no purpose really
		 * @throws NoJourneyFoundException - if the routePlanner can find no routes between to stations
		 * @return int - the number of line changes for all routes 
		 */
		public int getLineChangesCountAllRoutes() throws NoJourneyFoundException;
	    
	     
	     
		
	    /**
	     * Gets the number of possible routes between two stations
	     * @throws NoJourneyFoundException - if the routePlanner can find no routes between to stations
	     * @return int - Number of routes found having equal shortest distances between start station and 
	     *               destination station 
	     */
	    public int getPossibleRoutesCount() throws NoJourneyFoundException;
	    

	    
	    
	    
	    /**
	     * Gets the least number of stops between the start and destination stations
	     * @throws NoJourneyFoundException - if the routePlanner can find no routes between two stations
	     * @return int - The least number of stops between the start and destination stations
	     */
	    public int getStationsCount() throws NoJourneyFoundException;
	    

	    
	    
	    /**
	     * By entering a valid start station and destination station an intelligent
	     * journey object is returned.
	     * @throws InvalidStationException - if the station does not exist within the network
	     * @throws DuplicateStationException - if the start and destination stations are the same
	     * @throws NoJourneyFoundException - if no journey can be found for any given stations
	     * @return {@link Journey} - the quickest journey
	     */
	    Journey lookupJourney(String start, String destination) throws NoJourneyFoundException, 
	    												InvalidStationException, DuplicateStationException;
	    
	     
	    
	    public String getJourneyString(Journey journey);
	    
	    
	    public String getJourneyString(Journey journey, boolean htmlPage);
	    
	    
	    public List<String> getIRouteMap();

	    
	    void checkInput(String start, String destination) throws NoJourneyFoundException, 
        		InvalidStationException, DuplicateStationException;
		
}
