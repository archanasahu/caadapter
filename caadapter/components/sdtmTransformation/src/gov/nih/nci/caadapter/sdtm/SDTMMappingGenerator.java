package gov.nih.nci.caadapter.sdtm;

import gov.nih.nci.caadapter.ui.common.MappableNode;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author OWNER: Harsha Jayanna
 * @author LAST UPDATE $Author: jayannah $
 * @version Since caAdapter v3.2 revision
 *          $Revision: 1.5 $
 */
public class SDTMMappingGenerator
{
    public ArrayList<String> results = null;
    private int counter = 0;
    private String scsSDTMFile = null;
    private String scsDefineXMLFIle = null;
    private static SDTMMappingGenerator _sdtmMappingGeneratorReference;

    // new ArrayList();
    public SDTMMappingGenerator()
    {
        results = new ArrayList<String>();
        counter = 0;
    }

    public void removeObject(String source, String target)
    {
        results.remove(source + "~" + target);
    }

    public boolean put(String source, String target) throws Exception
    {
        //add a filter to remove the <choice> this causes parsing issues
        source = source.replaceAll("<", "[");
        source = source.replaceAll(">", "]");
        try {
            results.add(counter++, source + "~" + target);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            results.add(source + "~" + target);
        }
        return true;
    }

    public String getScsSDTMFile()
    {
        return scsSDTMFile;
    }

    public void setScsSDTMFile(String scsSDTMFile)
    {
        this.scsSDTMFile = scsSDTMFile;
    }

    public String getScsDefineXMLFIle()
    {
        return scsDefineXMLFIle;
    }

    public void setScsDefineXMLFIle(String scsDefineXMLFIle)
    {
        this.scsDefineXMLFIle = scsDefineXMLFIle;
    }

    public static SDTMMappingGenerator get_sdtmMappingGeneratorReference()
    {
        return _sdtmMappingGeneratorReference;
    }

    public void set_sdtmMappingGeneratorReference(SDTMMappingGenerator mappingGeneratorReference)
    {
        _sdtmMappingGeneratorReference = mappingGeneratorReference;
    }
}
