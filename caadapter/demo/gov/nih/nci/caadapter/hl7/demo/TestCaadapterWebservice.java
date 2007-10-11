package gov.nih.nci.caadapter.hl7.demo;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import javax.xml.rpc.ParameterMode;
import javax.xml.namespace.QName;
import org.apache.axis.utils.Options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class TestCaadapterWebservice {

	public static String buildCsvString(String fileName)
	{
		StringBuffer rtnBuf=new StringBuffer();
		try {
			File srcFile=new  File(fileName);
			InputStream fi;
			if (srcFile.exists())
        	{
        		fi =new FileInputStream(srcFile);
        	}
			else
				fi=TestCaadapterWebservice.class.getClassLoader().getResource(fileName).openStream();
			InputStreamReader reader=new InputStreamReader(fi);
			BufferedReader bfReader=new BufferedReader(reader);
			String lineSt;
			lineSt = bfReader.readLine();
			while (lineSt!=null)
			{
				rtnBuf.append(lineSt+"\n");
				lineSt = bfReader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return rtnBuf.toString();
		}
		return rtnBuf.toString();
	}
	
  public static void main(String[] args) {
    try {
//      String endpointURL = " http://cbioqa101.nci.nih.gov:49080/caAdapterWS/ws/caAdapterTransformationService";
      String endpointURL = " http://cbiovdev5054.nci.nih.gov/caAdapterWS/ws/caAdapterTransformationService";
//        String endpointURL = " http://caadapter-stage.nci.nih.gov/caAdapterWS/ws/caAdapterTransformationService";
//        String endpointURL = " http://caadapter.nci.nih.gov/caAdapterWS/ws/caAdapterTransformationService";
      if (args.length<2)
      {
    	  System.out.println("TestCaadapterWebservice Usage:[scenarioName][cvsString]");
    	  return;
      }
      System.out.println("TestCaadapterWebservice.main()..scenarioName:"+args[0]);
      System.out.println("TestCaadapterWebservice.main()..cvsString:"+args[1]);
      String methodName = "transformationService";
      String cvsFileName=args[1];
      String csvString =TestCaadapterWebservice.buildCsvString(cvsFileName);
      String scenarioName= "test2";

      Service service = new Service();
      Call call = (Call)service.createCall();
      call.setTargetEndpointAddress(new java.net.URL(endpointURL));
      call.setOperationName(methodName);
      call.addParameter("parameter_name", XMLType.XSD_STRING,ParameterMode.IN );
      call.addParameter("csvstringname",  XMLType.XSD_STRING, ParameterMode.IN );
      call.setReturnClass(java.util.ArrayList.class);
      ArrayList res = (ArrayList)call.invoke(new Object[]{scenarioName,csvString});
      System.out.println(res);
    }catch(Exception e) {
     	 e.printStackTrace();
    }
 }
}
