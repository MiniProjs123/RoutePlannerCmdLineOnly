package com.myrev.rp.dm;
import java.util.LinkedList;
import java.util.List;

public interface INode 
{
	// TODO obvious issues here:  Edge and Neighbour make no sense in this interface - resolve this 
	
	// TODO ultimately move this interface to com.myrev.rp.dm 
	
	public String getName();
	public void setName(String name);
	public int compareTo(INode node);
	public boolean equals(INode iNode);
	public Edge createPredEdge(INode start);
	public List getNeighbourList(); 
	public void setNeighbourList(LinkedList neighbourList); 
	public void addNeighbour(Neighbour neighbour);
}
