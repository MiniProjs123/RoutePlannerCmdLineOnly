package com.myrev.rp.ex;

public class InvalidNetworkException extends Exception {
	 
	private static final long serialVersionUID = 1L;

		public InvalidNetworkException(String message)
	    {
	        super(message);
	    }
  
	    public InvalidNetworkException(Exception e,String message)
	    {
	        super(message,e);
	    }
	  
	    public InvalidNetworkException(Exception e)
	    {
	        super(e);
	    }
}
