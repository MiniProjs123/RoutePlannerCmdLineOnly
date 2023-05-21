package com.myrev.rp.engine;
import java.util.LinkedList;

import com.myrev.rp.dm.INode;
import com.myrev.rp.ex.NoJourneyFoundException;


public interface IRouteEnquiry
{
	public void execute(INode start, INode destination);
    
	public LinkedList getPredecessorList(INode iNode) throws NoJourneyFoundException;
    
	public int getShortestDistance(INode INode);
     
}
