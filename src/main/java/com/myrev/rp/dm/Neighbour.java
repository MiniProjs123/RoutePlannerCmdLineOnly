package com.myrev.rp.dm;

public class Neighbour 
{
	private INode iNode;
	private int weighting;
	private IPath line;
	
	
	public Neighbour(INode iNode, int weighting, IPath line)
	{
		this.iNode = iNode;
		this.weighting = weighting;
		this.line = line;
	}
	
	
	// iNode equality only, not path
	public boolean shallowEquals(INode thatNode, IPath line)
	{
		if (this.iNode.equals(thatNode) &&
				!this.line.equals(line))
		{
			return true;
		}
		
		return false;
	}
		
	
	// iNode and lineName equality
	public boolean deepEquals(INode thatNode, IPath line)
	{
		String lineName = line.getPathName();
		if (this.iNode.equals(thatNode) &&
			this.line.getPathName().equals(lineName))
		{
			return true;
		}
		return false;
	}
	
	
	private boolean contains(String lineName)
	{
		if (this.line.getPathName().equals(lineName))
		{
			return true;
		}
		return false;
	}
	
	
	
	public String getNeighbourName()
	{
		return iNode.getName();
	}
	
	
	public INode getINode() {
		return iNode;
	}


	public void setINode(INode node) {
		iNode = node;
	}


	public int getWeighting() {
		return weighting;
	}


	public void setWeighting(int weighting) {
		this.weighting = weighting;
	}


	public IPath getLine() {
		return line;
	}


	public void setLineList(IPath line) {
		this.line = line;
	}

	
}
