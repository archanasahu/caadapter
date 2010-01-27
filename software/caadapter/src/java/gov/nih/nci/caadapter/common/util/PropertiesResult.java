/**
 * <!-- LICENSE_TEXT_START -->
The contents of this file are subject to the caAdapter Software License (the "License"). You may obtain a copy of the License at the following location: 
[caAdapter Home Directory]\docs\caAdapter_license.txt, or at:
http://ncicb.nci.nih.gov/infrastructure/cacore_overview/caadapter/indexContent/docs/caAdapter_License
 * <!-- LICENSE_TEXT_END -->
 */


package gov.nih.nci.caadapter.common.util;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * This class defines a compound class to associate a target object with its PropertyDescriptors.
 * So that caller of this class, such as gov.nih.nci.caadapter.ui.main.properties.DefaultPropertiesTableModel could
 * utilize the properties descriptor directly against the target object.
 *
 * @author OWNER: Scott Jiang
 * @author LAST UPDATE $Author: phadkes $
 * @version Since caAdapter v1.2
 *          revision    $Revision: 1.2 $
 *          date        $Date: 2008-06-09 19:53:50 $
 */
public class PropertiesResult
{
	/**
	 * Logging constant used to identify source of log entry, that could be later used to create
	 * logging mechanism to uniquely identify the logged class.
	 */
	private static final String LOGID = "$RCSfile: PropertiesResult.java,v $";

	/**
	 * String that identifies the class version and solves the serial version UID problem.
	 * This String is for informational purposes only and MUST not be made final.
	 *
	 * @see <a href="http://www.visi.com/~gyles19/cgi-bin/fom.cgi?file=63">JBuilder vice javac serial version UID</a>
	 */
	public static String RCSID = "$Header: /share/content/gforge/caadapter/caadapter/components/common/src/gov/nih/nci/caadapter/common/util/PropertiesResult.java,v 1.2 2008-06-09 19:53:50 phadkes Exp $";

	private Map<Object, List<PropertyDescriptor>> targetObjectToPropertyDescriptorMap;
	private Map<PropertyDescriptor, Object> propertyDescriptorToTargetObjectMap;

	public PropertiesResult()
	{
		//perserve the sequence
		targetObjectToPropertyDescriptorMap = new LinkedHashMap<Object, List<PropertyDescriptor>>();
		propertyDescriptorToTargetObjectMap = new LinkedHashMap<PropertyDescriptor, Object>();
	}

	/**
	 * Add the list of property descriptors the list that already associates with targetObject.
	 * Use the setPropertyDescriptors() instead, if you'd like to complete replace the existing list.
	 * @param targetObject
	 * @param propertyDescriptorList
	 */
	public void addPropertyDescriptors(Object targetObject, List propertyDescriptorList)
	{
		if(targetObject==null || propertyDescriptorList==null)
		{
			return;
		}
		List<PropertyDescriptor> currentList = targetObjectToPropertyDescriptorMap.get(targetObject);
		if(currentList==null)
		{
			currentList = new ArrayList<PropertyDescriptor>(propertyDescriptorList);
			targetObjectToPropertyDescriptorMap.put(targetObject, currentList);
		}
		else
		{
			currentList.addAll(propertyDescriptorList);
		}
		registerPropertyDescriptorToTargetObject(propertyDescriptorList, targetObject);
	}

	private void registerPropertyDescriptorToTargetObject(List<PropertyDescriptor> propList, Object targetObject)
	{
		int size = propList.size();
		for(int i=0; i<size; i++)
		{
			propertyDescriptorToTargetObjectMap.put(propList.get(i), targetObject);
		}
	}

	private void removePropertyDescriptorToTargetObject(List<PropertyDescriptor> propList)
	{
		int size = propList.size();
		for(int i=0; i<size; i++)
		{
			propertyDescriptorToTargetObjectMap.remove(propList.get(i));
		}
	}

	/**
	 * Set all PropertyDescriptors in the given list as the latest registry of the given target Object.
	 * @param targetObject
	 * @param propertyDescriptorList
	 */
	public void setPropertyDescriptors(Object targetObject, List propertyDescriptorList)
	{
		if(targetObject==null || propertyDescriptorList==null)
		{
			return;
		}
		List<PropertyDescriptor> currentList = targetObjectToPropertyDescriptorMap.get(targetObject);
		if(currentList!=null)
		{//found existing one, remove
			removePropertyDescriptorToTargetObject(currentList);
		}
		//construct new from scratch
		currentList = new ArrayList<PropertyDescriptor>(propertyDescriptorList);
		targetObjectToPropertyDescriptorMap.put(targetObject, currentList);
		registerPropertyDescriptorToTargetObject(currentList, targetObject);
	}

	/**
	 * Remove all PropertyDescriptors in the given list from the registry of the given target Object.
	 * @param targetObject
	 * @param propertyDescriptorList
	 */
	public void removePropertyDescriptors(Object targetObject, List propertyDescriptorList)
	{
		if(targetObject==null || propertyDescriptorList==null)
		{
			return;
		}
		List<PropertyDescriptor> currentList = targetObjectToPropertyDescriptorMap.get(targetObject);
		if(currentList!=null)
		{//function if and only if it is not null.
			currentList.removeAll(propertyDescriptorList);
			removePropertyDescriptorToTargetObject(propertyDescriptorList);
		}
	}

	/**
	 * Return the list of property descriptors of the given target object; if nothing found, return an empty list rather than a null.
	 * @param targetObject
	 * @return the list of property descriptors of the given target object
	 */
	public List<PropertyDescriptor> getPropertyDescriptors(Object targetObject)
	{
		 List<PropertyDescriptor> currentList = targetObjectToPropertyDescriptorMap.get(targetObject);
		if(currentList==null)
		{//so as never return null
			currentList = new ArrayList<PropertyDescriptor>();
		}
		return currentList;
	}

	/**
	 * Return the list of all property descriptors; if nothing found, return an empty list rather than a null.
	 * @return the list of property descriptors of the given target object
	 */
	public List<PropertyDescriptor> getAllPropertyDescriptors()
	{
		List<PropertyDescriptor> resultList = new ArrayList<PropertyDescriptor>();
		Iterator it = targetObjectToPropertyDescriptorMap.keySet().iterator();
		while(it.hasNext())
		{
			resultList.addAll(getPropertyDescriptors(it.next()));
		}
		return resultList;
	}

	/**
	 * Return the total number of propertyDescriptors available in this list, regardless the registered object.
	 * @return the total number of propertyDescriptors available in this list, regardless the registered object.
	 */
	public int getTotalPropertiesCount()
	{
		int count = 0;
		Iterator it = targetObjectToPropertyDescriptorMap.keySet().iterator();
		while(it.hasNext())
		{
			count += getPropertyDescriptors(it.next()).size();
		}
		return count;
	}

	/**
	 * Return the list of target objects that are registered in this object.
	 * @return the list of target objects that are registered in this object.
	 */
	public List getTargetObjectList()
	{
		List resultList = new ArrayList(targetObjectToPropertyDescriptorMap.keySet());
		return resultList;
	}

	/**
	 * Given a propertyDescriptor, return its associated target Object; if nothing is found, return null.
	 * @param propertyDescriptor
	 * @return the given propertyDescriptor associated target Object; if nothing is found, return null.
	 */
	public Object getTargetObject(PropertyDescriptor propertyDescriptor)
	{
		return propertyDescriptorToTargetObjectMap.get(propertyDescriptor);
	}

	/**
	 * Return string representation of this object.
	 * @return string representation of this object.
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		Iterator it = targetObjectToPropertyDescriptorMap.keySet().iterator();
		while(it.hasNext())
		{
			Object targetObject = it.next();
			List propList = getPropertyDescriptors(targetObject);
			buf.append("obj:'" + targetObject + "', # of Prop descriptors: '" + propList.size() + "'");
		}
		return buf.toString();
	}
}
/**
 * HISTORY      : $Log: not supported by cvs2svn $
 * HISTORY      : Revision 1.1  2007/04/03 16:02:37  wangeug
 * HISTORY      : initial loading of common module
 * HISTORY      :
 * HISTORY      : Revision 1.6  2006/08/02 18:44:25  jiangsc
 * HISTORY      : License Update
 * HISTORY      :
 * HISTORY      : Revision 1.5  2006/01/03 19:16:53  jiangsc
 * HISTORY      : License Update
 * HISTORY      :
 * HISTORY      : Revision 1.4  2006/01/03 18:56:26  jiangsc
 * HISTORY      : License Update
 * HISTORY      :
 * HISTORY      : Revision 1.3  2005/12/29 23:06:16  jiangsc
 * HISTORY      : Changed to latest project name.
 * HISTORY      :
 * HISTORY      : Revision 1.2  2005/12/29 15:39:06  chene
 * HISTORY      : Optimize imports
 * HISTORY      :
 * HISTORY      : Revision 1.1  2005/08/23 18:57:19  jiangsc
 * HISTORY      : Implemented the new Properties structure
 * HISTORY      :
 */