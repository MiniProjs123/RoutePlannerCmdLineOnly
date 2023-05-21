package com.myrev.rp.client;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

import com.myrev.rp.dm.IRouteMap;
import com.myrev.rp.dm.Journey;
import com.myrev.rp.engine.IRoutePlanner;
import com.myrev.rp.engine.RoutePlanner;
import com.myrev.rp.ex.DuplicateStationException;
import com.myrev.rp.ex.InvalidNetworkException;
import com.myrev.rp.ex.InvalidStationException;
import com.myrev.rp.ex.NoJourneyFoundException;
import com.myrev.rp.load.RouteMapReader;

import java.util.ResourceBundle;


public class RoutePlannerCmdLineMain {

	final static Logger logger = Logger.getLogger(RoutePlannerCmdLineMain.class);
	
	final static ResourceBundle mybundle = ResourceBundle.getBundle("application");
	
	public RoutePlannerCmdLineMain() {
		
		
	}

	public static void main(String[] args) {
		RoutePlannerCmdLineMain mainApp = new RoutePlannerCmdLineMain();		
		IRouteMap mapData;
		try {
			mapData = mainApp.loadSystemData();
			IRoutePlanner planner = new RoutePlanner(mapData); 
			String[] routeDetails = mainApp.getRouteEnquiryDetails(planner);
			Journey journey = planner.lookupJourney(routeDetails[0], routeDetails[1]);
			String journeyDisplay = planner.getJourneyString(journey);
			System.out.println(journeyDisplay);
		} catch(FileNotFoundException e) {
			System.out.println("File not found\n");
			logger.info(e);
		} catch(IOException e) {
			System.out.println("IOException \n");
			logger.info(e);
		} catch(InvalidStationException e) {
			System.out.println("Invalid Station\n");
			logger.info(e);
		} catch(InvalidNetworkException e) {
			System.out.println("Invalid Network\n");
			logger.info(e);
		} catch(NoJourneyFoundException e) { 
			System.out.println("No Journey Found\n");
			logger.info(e);
		} catch (DuplicateStationException e) {
			System.out.println("Duplicate Station entered - presumably start and destination are the same\n");
			logger.info(e);
		}
	}    
	
	
	
	public String[] getRouteEnquiryDetails(IRoutePlanner planner) throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        System.out.println("Welcome to the London Underground Route Planner. \n");
        String start = "";
        String destination = "";
        boolean validDataEntered = false;
		while (!validDataEntered)
		{
			System.out.println("Please enter a starting station: ");
			start = br.readLine();
			System.out.println("Now enter a destination station: ");
			destination = br.readLine();
			try {
				planner.checkInput(start, destination); // TODO note this is only actually checking for blanks and duplicates!
				validDataEntered = true;
				return new String[] {start, destination};
			} catch(InvalidStationException e) {
				System.out.println("Station not recognized\n");
			} catch (DuplicateStationException e) {
				System.out.println("Duplicate Station entered - presumably start and destination are the same\n");
			} catch(NoJourneyFoundException e) {
				System.out.println("Invalid station request - No Journey Found\n");
			}
		}
		
		return new String[] {start, destination};
	}
		
	
	public IRouteMap loadSystemData() throws IOException, FileNotFoundException, InvalidNetworkException {
		RouteMapReader reader = new RouteMapReader();
		return reader.buildIRouteMap(mybundle.getString("route.map.xml"));
	}

}
