/**
 * <!-- LICENSE_TEXT_START -->
 * $Header: /share/content/gforge/caadapter/caadapter/components/userInterface/src/gov/nih/nci/caadapter/ui/specification/hsm/actions/SaveAsHsmAction.java,v 1.3 2007-08-10 16:57:39 wangeug Exp $
 *
 * ******************************************************************
 * COPYRIGHT NOTICE
 * ******************************************************************
 *
 * The caAdapter Software License, Version 1.3
 * Copyright Notice.
 * 
 * Copyright 2006 SAIC. This software was developed in conjunction with the National Cancer Institute. To the extent government employees are co-authors, any rights in such works are subject to Title 17 of the United States Code, section 105. 
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the Copyright Notice above, this list of conditions, and the disclaimer of Article 3, below. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 * 
 * 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
 * 
 * 
 * "This product includes software developed by the SAIC and the National Cancer Institute."
 * 
 * 
 * If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself, wherever such third-party acknowledgments normally appear. 
 * 
 * 3. The names "The National Cancer Institute", "NCI" and "SAIC" must not be used to endorse or promote products derived from this software. 
 * 
 * 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize the recipient to use any trademarks owned by either NCI or SAIC-Frederick. 
 * 
 * 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT, THE NATIONAL CANCER INSTITUTE, SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */


package gov.nih.nci.caadapter.ui.specification.hsm.actions;

import gov.nih.nci.caadapter.common.util.Config;
import gov.nih.nci.caadapter.common.util.GeneralUtilities;
import gov.nih.nci.caadapter.hl7.mif.MIFClass;
import gov.nih.nci.caadapter.hl7.mif.MIFToXmlExporter;
import gov.nih.nci.caadapter.ui.common.DefaultSettings;
import gov.nih.nci.caadapter.ui.common.actions.DefaultSaveAsAction;
import gov.nih.nci.caadapter.ui.specification.hsm.HSMPanel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;

import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * This class defines a concrete "Save As" action.
 *
 * @author OWNER: Scott Jiang
 * @author LAST UPDATE $Author: wangeug $
 * @version Since caAdapter v1.2
 *          revision    $Revision: 1.3 $
 *          date        $Date: 2007-08-10 16:57:39 $
 */
public class SaveAsHsmAction extends DefaultSaveAsAction
{
	/**
	 * Logging constant used to identify source of log entry, that could be later used to create
	 * logging mechanism to uniquely identify the logged class.
	 */
	private static final String LOGID = "$RCSfile: SaveAsHsmAction.java,v $";

	/**
	 * String that identifies the class version and solves the serial version UID problem.
	 * This String is for informational purposes only and MUST not be made final.
	 *
	 * @see <a href="http://www.visi.com/~gyles19/cgi-bin/fom.cgi?file=63">JBuilder vice javac serial version UID</a>
	 */
	public static String RCSID = "$Header: /share/content/gforge/caadapter/caadapter/components/userInterface/src/gov/nih/nci/caadapter/ui/specification/hsm/actions/SaveAsHsmAction.java,v 1.3 2007-08-10 16:57:39 wangeug Exp $";

	protected transient HSMPanel hsmPanel;

	//define this variable but does not provide access methods. Leave descendant classes to do it, since this class does not need to memerize it.
	protected transient File defaultFile = null;

	/**
	 * Defines an <code>Action</code> object with a default
	 * description string and default icon.
	 */
	public SaveAsHsmAction(HSMPanel hsmPanel)
	{
		this(COMMAND_NAME, hsmPanel);
	}

	/**
	 * Defines an <code>Action</code> object with the specified
	 * description string and a default icon.
	 */
	public SaveAsHsmAction(String name, HSMPanel hsmPanel)
	{
		this(name, null, hsmPanel);
	}

	/**
	 * Defines an <code>Action</code> object with the specified
	 * description string and a the specified icon.
	 */
	public SaveAsHsmAction(String name, Icon icon, HSMPanel hsmPanel)
	{
		super(name, icon, null);
		this.hsmPanel = hsmPanel;
	}

	/**
	 * Invoked when an action occurs.
	 */
	protected boolean doAction(ActionEvent e) throws Exception
	{
//		File file = DefaultSettings.getUserInputOfFileFromGUI(this.hsmPanel, getUIWorkingDirectoryPath(), Config.HSM_META_DEFINITION_FILE_DEFAULT_EXTENSION, "Save As...", true, true);
		File file = DefaultSettings.getUserInputOfFileFromGUI(this.hsmPanel, Config.HSM_META_DEFINITION_FILE_DEFAULT_EXTENSION, "Save As...", true, true);
		if (file != null)
		{
			setSuccessfullyPerformed(processSaveFile(file, true));
		}
//		else
//		{
//			Log.logInfo(this, COMMAND_NAME + " command cancelled by user.");
//		}
		return isSuccessfullyPerformed();
	}

	protected boolean processSaveFile(File file, boolean resetUUID) throws Exception
	{
		preActionPerformed(hsmPanel);
		boolean oldChangeValue = hsmPanel.isChanged();
		try
		{
			Object treeRoot=hsmPanel.getTree().getModel().getRoot();
			Object specObject =((DefaultMutableTreeNode)treeRoot).getUserObject();
			OutputStream os = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(os); 
			oos.writeObject(specObject);
			oos.close();
			os.close();
			//save as xml
			MIFToXmlExporter xmlExporter;
			try {
				String h3sFileName=file.getPath();
				System.out.println("SaveAsHsmAction.processSaveFile()..filePath"+file.getPath());
				String xmlFileName=h3sFileName.replace(".h3s", "_spec.xml");				
				System.out.println("SaveAsHsmAction.processSaveFile()...xmlFileName:"+xmlFileName);
				xmlExporter = new MIFToXmlExporter((MIFClass)specObject);
				xmlExporter.exportToFile(xmlFileName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (!GeneralUtilities.areEqual(defaultFile, file))
			{//not equal, change it.
				removeFileUsageListener(defaultFile, hsmPanel);
				defaultFile = file;
			}
			hsmPanel.setSaveFile(file);
			//clear the change flag.
			hsmPanel.setChanged(false);
			//try to notify affected panels
			postActionPerformed(hsmPanel);
			JOptionPane.showMessageDialog(hsmPanel.getParent(), "HL7 v3 Specification has been saved successfully.", "Save Complete", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
		catch (Exception e)
		{
			//restore the change value since something occurred and believe the save process is aborted.
			hsmPanel.setChanged(oldChangeValue);
			//rethrow the exception
			throw e;
		}
		finally
		{
//			try
//			{
//				//close buffered writer will automatically close enclosed file writer.
//				if (bw != null) bw.close();
//			}
//			catch (Exception e)
//			{//intentionally ignored.
//			}
		}
	}
}
	
	