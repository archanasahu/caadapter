package gov.nih.nci.cbiit.cdms.formula.core;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "operationType")
@XmlEnum

public enum OperationType {
    @XmlEnumValue("addition")
    ADDITION("math"),
    @XmlEnumValue("substraction")
    SUBSTRACTION("substraction"),
    @XmlEnumValue("multiplication")
    MULTIPLICATION("multiplication"),
    @XmlEnumValue("division")
    DIVISION("division"),
    @XmlEnumValue("power")
    POWER("power"),
    @XmlEnumValue("radical")
    RADICAL("radical"),
    @XmlEnumValue("squareRoot")
    SQUAREROOT("squareRoot"),
    @XmlEnumValue("exponential")
    EXPONENTIAL("exponential"),
    @XmlEnumValue("trigonometric")
    TRIGONoMETRIC("trigonometric");
	
    private final String value;
    
    OperationType(String v)
    {
    	value=v;
    }
    
    public String value() {
        return value;
    }
    
    public String toString()
    {
    	if (value().equals(ADDITION.value))
    		return " + ";
    	else if (value().equals(SUBSTRACTION.value))
    		return " - ";
    	else if (value().equals(MULTIPLICATION.value))
    		return " X ";
    	else if (value().equals(DIVISION.value()))
    		return " / ";
    	else if (value().equals(POWER.value))
    		return "^";
    	return value();
    }
}
