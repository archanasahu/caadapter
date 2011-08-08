package gov.nih.nci.cbiit.cmts.transform.artifact;

import java.util.List;
import java.util.Stack;

import org.jdom.Element;

import gov.nih.nci.cbiit.cmts.core.AttributeMeta;
import gov.nih.nci.cbiit.cmts.core.Component;
import gov.nih.nci.cbiit.cmts.core.ComponentType;
import gov.nih.nci.cbiit.cmts.core.ElementMeta;
import gov.nih.nci.cbiit.cmts.core.FunctionType;
import gov.nih.nci.cbiit.cmts.core.LinkType;
import gov.nih.nci.cbiit.cmts.core.Mapping;
import gov.nih.nci.cbiit.cmts.transform.QueryBuilderUtil;
import gov.nih.nci.cbiit.cmts.transform.XQueryBuilder;

public class StylesheetBuilder extends XQueryBuilder {

	private XSLTStylesheet stylesheet;
	private String varName="_template_";
	private int varCount=0;
	public StylesheetBuilder(Mapping m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	public XSLTStylesheet buildStyleSheet()
	{
		stylesheet =new XSLTStylesheet();
		
		XSLTTemplate rootTemplate=new XSLTTemplate();
		rootTemplate.setMatch("/");
		
		List<Component> l = mapping.getComponents().getComponent();
		Component tgt = null;
		Component src=null;
		for (Component c:l) {
			if (c.getType().equals(ComponentType.TARGET)) 
				tgt = c;
			else if (c.getType().equals(ComponentType.SOURCE))
				src=c;
			
		}
		stylesheet.addTempate(rootTemplate);
		xpathStack = new Stack<String>();
		processTargetElement(tgt.getRootElement(), "", rootTemplate);
		return stylesheet;
	}
	
    /**
	 * Process a target element:
	 * Case I: The element is mapped to a source node
	 * Case II: The element is mapped to function output port
	 * Case II.1: The function does not have input port
	 * Case II.2: The function has one or more input ports
	 * Case III: The element is not mapped, but its attribute is mapped
	 * Case IV: Neither the element nor its attribute is mapped. But its descendant
	 * is mapped
	 * Case V: create an empty element if it is mandatory
	 * Case VI: Others, empty element with/without default inline text
	 * @param elementMeta
	 * @param parentMappedXPath
	 * @param parentTemplate
	 */
	private void processTargetElement(ElementMeta elementMeta,String parentMappedXPath,
			XSLTTemplate parentTemplate)
	{
		xpathStack.push(elementMeta.getName());
		String targetElementXpath=QueryBuilderUtil.buildXPath(xpathStack);
		LinkType link = links.get(targetElementXpath);
		String childElementRef=parentMappedXPath;
		Element tgtDataElement= new Element(elementMeta.getName());
		//case III: The element is not mapped, but its attribute is mapped
		encodeAttribute(tgtDataElement,elementMeta, targetElementXpath);
        if(link!=null)
        {
			String tgtMappingSrc=link.getSource().getId();
			if (tgtMappingSrc.indexOf("@")>-1)
			{
				//case I.1: link source is an attribute 
				XSLTElement valueElment=new XSLTElement("value-of");
				String selectExp=tgtMappingSrc.substring(parentMappedXPath.length()+1);
				valueElment.setAttribute("select", selectExp);
				tgtDataElement.addContent(valueElment);
				parentTemplate.addContent(tgtDataElement);
			}
			else
			{
				childElementRef=tgtMappingSrc;
 				//case I.2: link source is an element
				XSLTElement forEach=new XSLTElement("for-each");
				forEach.setAttribute("select",tgtMappingSrc.substring(parentMappedXPath.length()+1));
				if (!hasMappedDescenant(elementMeta))
				{
					//apply content if this target element is leaf
					XSLTApplyTemplates elementApply=new XSLTApplyTemplates();
					elementApply.setSelect(".");	
					tgtDataElement.addContent(elementApply);
				}
				forEach.addContent(tgtDataElement);
				parentTemplate.addContent(forEach);
			}
        }
        else
        {
    		parentTemplate.addContent(tgtDataElement);
			LinkType fLink = metaToFunctionLinks.get(targetElementXpath);
			if (fLink!=null)
			{
				//Case II: The element is mapped to function output port
				String functionId=fLink.getTarget().getComponentid();
				FunctionType inputFunction=functions.get(functionId);
				if (inputFunction.getData().size()==1)
				{
					//Case II.1: The function does not have input port
					XSLTElement outElement=createXsltForFunctionWithoutInput(inputFunction);
					tgtDataElement.addContent(outElement);
				}
				else
				{
					//Case II.2: The function has one or more input ports
					//a loop will be create to invoke data manipulation function
//					inlineText=createQueryForFunctionWithInput(fLink,  parentMappedXPath);
//					encodeElement(tgt, parentMappedXPath,inlineText,true);
//					elementCreated=true;
				}
			}
        }
		if (hasMappedDescenant(elementMeta))
		{
			//case IV: Neither the element nor its attribute is mapped. But its descendant
			//is mapped
			for(ElementMeta e:elementMeta.getChildElement()) 
			{
				XSLTCallTemplate callTemplate=new XSLTCallTemplate();
//				String tmpName=targetElementXpath.replace("/", "_")+"_"+e.getName();//varName+varCount++;
				String tmpName=varName+varCount++;
				callTemplate.setCalledTemplate(tmpName);
				tgtDataElement.addContent(callTemplate);
				XSLTTemplate calledTemplate =new XSLTTemplate();
				calledTemplate.setAttribute("name", tmpName);
				stylesheet.addTempate(calledTemplate);
				processTargetElement(e,childElementRef, calledTemplate);
			}
		}
		xpathStack.pop();
	}
	/**
	 * Set values for the attributes of an element
	 * Case I: The attribute is mapped from a source item
	 * Case II.1: The attribute is mapped to the out put port of a function without input port
	 * Case II.2: The attribute is mapped to the out put port of a function with one or more input ports
	 * Case III :use fixed value first
	 * Case IV : use default value
	 * Case V: Ignore this attribute
	 * @param elementMeta - Target Element meta
	 * @param mappedSourceNodeId - The previously mapped source node path, which is associated with the variable on varStack
	 */
	private void encodeAttribute(Element targetData, ElementMeta targetMeta,
			String targetElementMetaXpath)
	{
		for (AttributeMeta attrMeta:targetMeta.getAttrData())
		{
			String targetAttributePath=targetElementMetaXpath+"/@"+attrMeta.getName();
			LinkType link = links.get(targetAttributePath);
			XSLTElement xsltAttr=new XSLTElement("attribute");
			xsltAttr.setAttribute("name", attrMeta.getName());
			if (link!=null)
			{
	        	/**
	        	 * Case I: The attribute is mapped from a source item
	        	 * Case I.1: link source is an element  
	        	 * case I.2: link source is an attribute
	        	 */
				String tgtMappingSrc=link.getSource().getId();
				//I.1 is default
				String selectExp=".";
				if (tgtMappingSrc.indexOf("@")>-1)
					//case I.1
					selectExp=tgtMappingSrc.substring(tgtMappingSrc.indexOf("@"));
				
				XSLTElement valueElment=new XSLTElement("value-of");
				valueElment.setAttribute("select", selectExp);
				xsltAttr.addContent(valueElment);
				targetData.addContent(xsltAttr);
			}
			else if(metaToFunctionLinks.get(targetAttributePath)!=null) 
			{
				//case II
				LinkType fLink = metaToFunctionLinks.get(targetAttributePath);
				String functionId=fLink.getTarget().getComponentid();
				FunctionType inputFunction=functions.get(functionId);
				if (inputFunction.getData().size()==1)
				{
					//Case II.1: The linked function dose not have input port
					FunctionType functionType=functions.get(fLink.getTarget().getComponentid());
					XSLTElement xsltAttrValue=createXsltForFunctionWithoutInput(functionType);
					xsltAttr.addContent(xsltAttrValue);
				}
				else
				{
					//Case II.2: The linked function has one or more input ports
//					attrValue=createQueryForFunctionWithInput(fLink,  mappedSourceNodeId);
				}
				targetData.addContent(xsltAttr);
			}
			else
			{
				String targetAttrValue="";
				if (attrMeta.getFixedValue()!=null)
					//case III
					targetAttrValue=attrMeta.getFixedValue();
				else if (attrMeta.getDefaultValue()!=null)
					//case IV
					targetAttrValue=attrMeta.getDefaultValue();
				else
					//case V
					continue;
				
				if (!targetAttrValue.equals(""))
				{
					XSLTElement xlstText=new XSLTElement("text");
					xlstText.setText(targetAttrValue);
					xsltAttr.addContent(xlstText);
					targetData.addContent(xsltAttr);
				}					
			}
		}
	}
	
	private XSLTElement createXsltForFunctionWithoutInput(FunctionType functionType)
	{
		if (functionType.getData().size()!=1)
			return null;// "invalid function:"+functionType.getGroup()+":"+functionType.getName()+":"+functionType.getMethod();
		XSLTElement rtnElement=new XSLTElement("value-of");
		String xqueryString=(String)QueryBuilderUtil.generateXpathExpressionForFunctionWithoutInput(functionType);
		rtnElement.setAttribute("select", xqueryString);
		return rtnElement ;
 
	}
}
