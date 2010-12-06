package gov.nih.nci.cbiit.cdms.formula.core;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "statusType")
@XmlEnum

public enum FormulaStatus {
    @XmlEnumValue("draft")
    DRAFT("draft"),
    @XmlEnumValue("complete")
    COMPLETE("complete"),
    @XmlEnumValue("final")
    FINAL("final");
    private String value;
    
    FormulaStatus(String v)
    {
    	value=v;
    }
    
    public String value() {
        return value;
    }
}