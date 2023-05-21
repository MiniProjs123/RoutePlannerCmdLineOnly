package com.myrev.rp.engine;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import com.myrev.rp.dm.Edge;
import com.myrev.rp.dm.INode;
import com.myrev.rp.dm.IPath;
import com.myrev.rp.dm.IRouteMap;
import com.myrev.rp.dm.Neighbour;
import com.myrev.rp.ex.NoJourneyFoundException;


public class DijkstraRouteEnquiry implements IRouteEnquiry
{
	/**
     * Infinity value for distances.
     */
    public static final int INFINITE_DISTANCE = Integer.MAX_VALUE;

    /**
     * Some value to initialize the priority queue with.
     */
    private static final int INITIAL_CAPACITY = 8;
    
    /**
     * This comparator orders cities according to their shortest distances,
     * in ascending fashion. If two cities have the same shortest distance,
     * we compare the cities themselves.
     */
    private final Comparator<INode> shortestPathComparator = new Comparator<INode>()
        {
            public int compare(INode left, INode right)
            {
                // note that this trick doesn't work for huge distances, close to Integer.MAX_VALUE
            	int result = getShortestDistance(left) - getShortestDistance(right);
                
                return (result == 0) ? left.compareTo(right) : result;
            }
        };
    
        /**
         * The graph.
         */
        private final IRouteMap map;
        
        /**
         * The working set of cities, kept ordered by shortest distance.
         */
        private final PriorityQueue<INode> unsettledNodes = 
        					new PriorityQueue<INode>(INITIAL_CAPACITY, shortestPathComparator);
          
          
     /**
     * The set of cities for which the shortest distance to the source
     * has been found.
     */
    private final Set<INode> settledNodes = new HashSet<INode>();
    
    /**
     * The currently known shortest distance for all cities.
     */
    private final Map<String, Integer> shortestDistances = new HashMap<String, Integer>();

    /**
     * Predecessors list: maps a INode to its predecessor in the spanning tree of
     * shortest paths.
     */
    private final Map<String, List<Edge>> predecessors = new HashMap<String, List<Edge>>();
    
    private INode destination;
    
    /**
     * Constructor.
     */
    public DijkstraRouteEnquiry(IRouteMap map)
    {
        this.map = map;
    }

    
    
    
    /**
     * Initialize all data structures used by the algorithm.
     * 
     * @param start the source node
     */
    private void init(INode start,INode destination)
    {
        this.destination = destination;
    	settledNodes.clear();
        unsettledNodes.clear();
        
        shortestDistances.clear();
        predecessors.clear();
        
        // add source
        setShortestDistance(start, 0);
        unsettledNodes.add(start);
    }
    
     
        
    
    /**
     * Run Dijkstra's shortest path algorithm on the map.
     * The results of the algorithm are available through
     * {@link #getPredecessor(City)}
     * and 
     * {@link #getShortestDistance(City)}
     * upon completion of this method.
     * 
     * @param start the starting city
     * @param destination the destination city. If this argument is <code>null</code>, 
     * the algorithm is
     * run on the entire graph, instead of being stopped as soon as the destination 
     * is reached.
     */
    
    public void execute(INode start, INode destination)
    {
        init(start,destination);
        
        // the current node
        INode u;
        
        // extract the node with the shortest distance IE: THE FIRST NODE IN UNSETTLEDNODES
        while ((u = unsettledNodes.poll()) != null)
        {
            // destination reached, stop
            if (u == destination) return;
            
            settledNodes.add(u);
            relaxNeighbors(u);
        }
     }

    
    
    
    
    /**
	 * Compute new shortest distance for neighboring nodes and update if a shorter
	 * distance is found.
	 * 
	 * @param u the node
	 */
    private void relaxNeighbors(INode u)
    {
        List neighboursList = u.getNeighbourList();
    	for (int i = 0; i < neighboursList.size(); i++)
    	{
            Neighbour v = (Neighbour)neighboursList.get(i);
    		INode neighbourNode = v.getINode();
            
    		// skip node already settled
            if (!isSettled(neighbourNode))
            {
            	Edge edgeUToV = buildEdge(u,v);
                int distToV = getShortestDistance(u) + v.getWeighting();
                updateRecords(distToV, neighbourNode, edgeUToV);
            }
        }    
    }
    
    
    
    private Edge buildEdge(INode startU, Neighbour v)
    {
   	 	INode dest = v.getINode();
        int weighting = v.getWeighting();
        IPath path = v.getLine();
        return new Edge(startU, dest, weighting, path);
    }
    
    
    
    private void updateRecords(int distToV, INode neighbourNode, Edge edgeUToV)
    {
    	//int shortestDist = getShortestDistance(neighbourNode); 
    	if (distToV == getShortestDistance(neighbourNode))
        {
    		// if a shorter way exists, do not clear the predecessor list for this node (false)
        	setPredecessor(neighbourNode, edgeUToV,false);
        }
    	else if (distToV < getShortestDistance(neighbourNode))
        {
        	// assign new shortest distance and mark unsettled
            setShortestDistance(neighbourNode, distToV);
                            
            // assign predecessor in shortest path
            setPredecessor(neighbourNode, edgeUToV,true);
        }
        
       	
    }
    
    
    
    
    
    
    
    /**
	 * Test a node.
	 * 
     * @param v the node to consider
     * 
     * @return whether the node is settled, ie. its shortest distance
     * has been found.
     */
    private boolean isSettled(INode v)
    {
        return settledNodes.contains(v);
    }
    
    
    
    
    /**
     * @return the city leading to the given city on the shortest path, or
     * <code>null</code> if there is no route to the destination.
     */
    public LinkedList getPredecessorList(INode destination) throws NoJourneyFoundException
    {
        if (!predecessors.containsKey(destination.getName()))  
        {
        	System.out.println();
    		throw new NoJourneyFoundException("A route could not be found to the destination. " +
        			               "This is rather odd and suggests your network is not valid.");
        }
    	return (LinkedList)predecessors.get(destination.getName());  
    } 
    
    
    
    private void setPredecessor(INode v, Edge uv, boolean shorterWayExists)
    {
        if (! predecessors.containsKey(v.getName()))
    	{
    		List<Edge> edgeList = new LinkedList<Edge>();
         	edgeList.add(uv); 
         	predecessors.put(v.getName(), edgeList);
    	}
    	else //list already exists
    	{
    		List<Edge> edgeList = (LinkedList)predecessors.get(v.getName());
    		
    		// if edge uv already exists in the list, do not add it again
    		if (shorterWayExists)
    		{
    			edgeList.clear();
    		}
    		if (! containsEdge(edgeList,uv))
    		{
    			edgeList.add(uv);
    		}
    	}
    }

    
    private boolean containsEdge(List<Edge> edgeList, Edge uv)
    {
    	for (int i = 0; i < edgeList.size(); i++)
    	{
    		Edge testEdge = edgeList.get(i);
    		if (testEdge.equals(uv))
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
        
    
    
    /**
	 * Set the new shortest distance for the given node,
	 * and re-balance the queue according to new shortest distances.
	 * 
	 * @param city the node to set
	 * @param distance new shortest distance value
	 */        
    private void setShortestDistance(INode iNode, int distance)
    {
    	/*
         * This crucial step ensures no duplicates are created in the queue
         * when an existing unsettled node is updated with a new shortest 
         * distance.
         * 
         * Note: this operation takes linear time. If performance is a concern,
         * consider using a TreeSet instead instead of a PriorityQueue. 
         * TreeSet.remove() performs in logarithmic time, but the PriorityQueue
         * is simpler. (An earlier version of this class used a TreeSet.)
         */
        unsettledNodes.remove(iNode);

        /*
         * Update the shortest distance.
         */
        shortestDistances.put(iNode.getName(), distance);
        
		/*
		 * Re-balance the queue according to the new shortest distance found
		 * (see the comparator the queue was initialized with).
		 */
		
        
        unsettledNodes.add(iNode);      
    }
    
       
        
    /**
     * @return the shortest distance from the source to the given INode, or
     * {@link DijkstraEngine#INFINITE_DISTANCE} if there is no route to the destination.
     */    
    public int getShortestDistance(INode INode)
    {
        Integer d = shortestDistances.get(INode.getName());
        return (d == null) ? INFINITE_DISTANCE : d;
    }
    
  
    
    
}
