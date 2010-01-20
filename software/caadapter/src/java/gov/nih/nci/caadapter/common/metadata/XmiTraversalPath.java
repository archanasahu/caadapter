/**
 * <!-- LICENSE_TEXT_START -->
The contents of this file are subject to the caAdapter Software License (the "License"). You may obtain a copy of the License at the following location: 
[caAdapter Home Directory]\docs\caAdapter_license.txt, or at:
http://ncicb.nci.nih.gov/infrastructure/cacore_overview/caadapter/indexContent/docs/caAdapter_License
 * <!-- LICENSE_TEXT_END -->
 */
 
package gov.nih.nci.caadapter.common.metadata;
/**
 * @author OWNER: $Author: wangeug $
 * @author LAST UPDATE $Author: wangeug $
 * @since      caAdapter  v4.2    
 * @version    $Revision: 1.4 $
 * @date       $Date: 2009-06-12 15:14:21 $
*/

import java.util.LinkedList;

public class XmiTraversalPath {

	private LinkedList <String> pathElements;
	public XmiTraversalPath()
	{
		pathElements=new LinkedList<String>();
	}
	public XmiTraversalPath(String pathPiece)
	{
		this();
		addOnePathElement(pathPiece);
	}
	
	public void addOnePathElement(String piece)
	{
		if (piece==null)
			return;
		if (piece.equals(""))
			return;
		pathElements.addLast(piece);
	}
	
	public void removeLastPathElement(String piece)
	{
		if (pathElements==null)
			return;
		if (pathElements.isEmpty())
			return;
		if (pathElements.getLast().equals(piece))
			pathElements.removeLast();
		else
			System.out.println("XmiTraversalPath.removeLastPathElement()...not last path element.. failed to remove:"+piece);
	}
	
	public String pathNevigator()
	{
		if (pathElements==null)
			return "";
		
		StringBuffer rtnSb=new StringBuffer();
		for (String piece:pathElements)
			rtnSb.append(piece+".");
		
		String rtnSt=rtnSb.toString();
		if (rtnSt.endsWith("."))
			return rtnSb.substring(0,rtnSb.length()-1);
		return rtnSt;
	}
}

/**
 * HISTORY      : $Log: not supported by cvs2svn $
 * HISTORY      : Revision 1.3  2008/09/25 19:30:39  phadkes
 * HISTORY      : Changes for code standards
 * HISTORY      :
*/