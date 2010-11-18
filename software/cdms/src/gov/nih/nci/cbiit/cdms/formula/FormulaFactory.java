package gov.nih.nci.cbiit.cdms.formula;

import gov.nih.nci.cbiit.cdms.formula.core.FormulaMeta;
import gov.nih.nci.cbiit.cdms.formula.core.FormulaStore;
import gov.nih.nci.cbiit.cdms.formula.core.OperationType;
import gov.nih.nci.cbiit.cdms.formula.core.TermMeta;

import java.io.*;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

public class FormulaFactory {
	private static FormulaStore commonStore;
	private static FormulaStore localStore;
	private static HashMap<String, TermMeta> expressionTemplate;

	public static TermMeta createTemplateTerm(OperationType type)
	{
		if (expressionTemplate==null
				||
				expressionTemplate.isEmpty())
		{
			try {
				FormulaStore templateStore=loadFormulaStore(new File("dataStore/expressionTemplate.xml"));
				expressionTemplate=new HashMap<String, TermMeta>();
				for (FormulaMeta formula:templateStore.getFormula())
				{
					String expressionKey=formula.getExpression().getOperation().value();
					expressionTemplate.put(expressionKey, formula.getExpression());
				}
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		TermMeta expressionTerm=expressionTemplate.get(type.value());
		if (expressionTerm!=null)
			return (TermMeta)expressionTerm.clone();
		return null;
	}
	public static void updateLocalStore(FormulaStore store)
	{
		if (store!=null)
			localStore=store;
	}
	public static FormulaStore getLocalStore()
	{
		if (localStore==null)
		{
			localStore=new FormulaStore();
			localStore.setName("No Formula Defined");

		}
		return localStore;
	}

	public static FormulaStore  getCommonStore()
	{
		if (commonStore==null)
			try {
				commonStore=loadFormulaStore(new File("dataStore/commonFormulae.xml"));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return commonStore;
	}

	public static FormulaStore loadFormulaStore(File f) throws JAXBException
	{
		JAXBContext jc=getJAXBContext();
		Unmarshaller u=jc.createUnmarshaller();
		JAXBElement<FormulaStore> jaxbFormula=u.unmarshal(new StreamSource(f), FormulaStore.class);
		return jaxbFormula.getValue();
	}

	/**
	 * Save a local formula store into a file
	 * @param store local formula store 
	 * @param targetFile target XML file to save
	 * @throws IOException 
	 * @throws JAXBException 
	 */
	public static void saveFormulaStore(FormulaStore store, File targetFile) throws IOException, JAXBException
	{
		FileWriter writer =new FileWriter(targetFile);
		JAXBContext jc=getJAXBContext();
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
		m.marshal(new JAXBElement<FormulaStore>(new QName("formulaStore"),FormulaStore.class, store), writer);
		writer.close();
	}
	
	public static FormulaMeta loadFormula(File f) throws JAXBException
	{
		JAXBContext jc=getJAXBContext();
		Unmarshaller u=jc.createUnmarshaller();
		JAXBElement<FormulaMeta> jaxbFormula=u.unmarshal(new StreamSource(f), FormulaMeta.class);
		return jaxbFormula.getValue();
	}
    public static FormulaMeta loadFormula(String str) throws JAXBException
	{
		JAXBContext jc=getJAXBContext();
		Unmarshaller u=jc.createUnmarshaller();
		JAXBElement<FormulaMeta> jaxbFormula=u.unmarshal(new StreamSource(new CharArrayReader(str.toCharArray())), FormulaMeta.class);
		return jaxbFormula.getValue();
	}

    public static String convertFormulaToXml(FormulaMeta formula)
	{
		StringWriter writer=new StringWriter();
		try {
			writerFormula(formula, writer);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return writer.getBuffer().toString();
	}

	private static void writerFormula(FormulaMeta formula, Writer writer) throws JAXBException
	{
		JAXBContext jc=getJAXBContext();
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
		m.marshal(new JAXBElement<FormulaMeta>(new QName("formula"),FormulaMeta.class, formula), writer);
	}
	
	public static void saveFormula(FormulaMeta formula, File f) throws JAXBException, IOException
	{
		FileWriter writer =new FileWriter(f);
		writerFormula(formula, writer);
		writer.close();
//		JAXBContext jc=getJAXBContext();
//		Marshaller m = jc.createMarshaller();
//		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
//		m.marshal(new JAXBElement<FormulaMeta>(new QName("formula"),FormulaMeta.class, formula), f);
	}
	
	private static JAXBContext getJAXBContext() throws JAXBException
	{
		JAXBContext jc=null;		
//		jc = JAXBContext.newInstance( "gov.nih.nci.cbiit.cmts.core" );
//		jc=com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.newInstance("gov.nih.nci.cbiit.cdms.formula.core");
		jc=JAXBContext.newInstance("gov.nih.nci.cbiit.cdms.formula.core");
		return jc;
	}
}
