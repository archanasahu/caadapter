/**
 * The content of this file is subject to the caAdapter Software License (the "License").  
 * A copy of the License is available at:
 * [caAdapter CVS home directory]\etc\license\caAdapter_license.txt. or at:
 * http://ncicb.nci.nih.gov/infrastructure/cacore_overview/caadapter/indexContent
 * /docs/caAdapter_License
 */
package gov.nih.nci.cbiit.cmps.ui.mapping;

import gov.nih.nci.cbiit.cmps.common.ApplicationException;
import gov.nih.nci.cbiit.cmps.ui.common.ActionConstants;
import gov.nih.nci.cbiit.cmps.ui.common.DefaultSettings;
import gov.nih.nci.cbiit.cmps.ui.common.MenuConstants;
import gov.nih.nci.cbiit.cmps.ui.jgraph.MiddlePanelJGraphController;
import gov.nih.nci.cbiit.cmps.ui.tree.MappingSourceTree;
import gov.nih.nci.cbiit.cmps.ui.tree.MappingTargetTree;
import gov.nih.nci.cbiit.cmps.ui.util.FileUtil;
import gov.nih.nci.cbiit.cmps.ui.util.GeneralUtilities;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

/**
 * This class 
 *
 * @author Chunqing Lin
 * @author LAST UPDATE $Author: linc $
 * @since     CMPS v1.0
 * @version    $Revision: 1.3 $
 * @date       $Date: 2008-11-04 15:58:57 $
 *
 */
public class CmpsMappingPanel extends JPanel implements ActionListener{

	protected File saveFile = null;
	
//	protected FunctionLibraryPane functionPane;
//	protected DefaultPropertiesPage propertiesPane;
	protected MappingTreeScrollPane sourceScrollPane = new MappingTreeScrollPane(MappingTreeScrollPane.DRAW_NODE_TO_RIGHT);
	protected MappingTreeScrollPane targetScrollPane = new MappingTreeScrollPane(MappingTreeScrollPane.DRAW_NODE_TO_LEFT);
	
	protected JTextField sourceLocationArea = new JTextField();
	protected JTextField targetLocationArea = new JTextField();
	protected MappingMiddlePanel middlePanel = null;
	protected MappingSourceTree sTree = null;
	protected MappingTargetTree tTree = null;
	
//	protected TreeCollapseAllAction sourceTreeCollapseAllAction;
//	protected TreeExpandAllAction sourceTreeExpandAllAction;
//	
//	protected TreeCollapseAllAction targetTreeCollapseAllAction;
//	protected TreeExpandAllAction targetTreeExpandAllAction;
//
//	protected MappingFileSynchronizer fileSynchronizer;
//	
//	protected TreeDefaultDragTransferHandler sourceTreeDragTransferHandler = null;
//	protected abstract TreeDefaultDropTransferHandler getTargetTreeDropTransferHandler();
	
	protected JPanel sourceButtonPanel = null;
	protected JPanel sourceLocationPanel = null;
	protected JPanel targetButtonPanel = null;
	protected JPanel targetLocationPanel = null;
	
	protected File mappingSourceFile = null;
	protected File mappingTargetFile = null;


	
	private static final String SELECT_SOURCE = "Open Source...";
	private static final String SELECT_TARGET = "Open Target...";

	private static final int FRAME_DEFAULT_WIDTH = 0;

	private static final String SELECT_CSV_TIP = null;

	private static final double FRAME_DEFAULT_HEIGHT = 0;

	private static final String SELECT_HMD_TIP = null;

	private static final String SOURCE_TREE_FILE_DEFAULT_EXTENTION = null;

	private static final String OPEN_DIALOG_TITLE_FOR_DEFAULT_SOURCE_FILE = null;

	private static final String Cmps_V3_MESSAGE_FILE_DEFAULT_EXTENSION = null;

	private static final String TARGET_TREE_FILE_DEFAULT_EXTENTION = null;

	private static final String OPEN_DIALOG_TITLE_FOR_DEFAULT_TARGET_FILE = null;

	private static final Object CSV_METADATA_FILE_DEFAULT_EXTENTION = null;

	private static final Object HSM_META_DEFINITION_FILE_DEFAULT_EXTENSION = null;

//	private TargetTreeDropTransferHandler targetTreeDropTransferHandler = null;

	private JButton openSourceButton = new JButton(SELECT_SOURCE);
	private JButton openTargetButton = new JButton(SELECT_TARGET);

	public CmpsMappingPanel() throws Exception
	{
		this("","calledFromConstructor","");
	}

    public CmpsMappingPanel(String sourceFile, String _flag) throws Exception
	{
    	this(sourceFile, "calledFromConstructor", _flag);
	}
    public CmpsMappingPanel(String sourceFile, String targetFile, String _flag) throws Exception
	{
    	this.setBorder(BorderFactory.createEmptyBorder());
		this.setLayout(new BorderLayout());
		this.add(getCenterPanel(true), BorderLayout.CENTER);
//		fileSynchronizer = new MappingFileSynchronizer(this);

		if (!sourceFile.equals(""))
			processOpenSourceTree(new File(sourceFile), false, false);

        if ((targetFile == null)||(targetFile.equals(""))) throw new Exception("Empty Target File");
        if (!targetFile.equals("calledFromConstructor"))
        {
	        File file = new File(targetFile);
	        if (!file.exists()) throw new Exception("Target File is Not exist. : " + targetFile);
	        if (!file.isFile()) throw new Exception("Target File is Not a file. : " + targetFile);
	        boolean success = processOpenTargetTree(file, true, true);
	        if (!success) throw new Exception("GEN3");
	    }
	}

	protected JPanel getTopLevelLeftPanel()
	{
		JPanel topCenterPanel = new JPanel(new BorderLayout());
		topCenterPanel.setBorder(BorderFactory.createEmptyBorder());
		JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		DefaultSettings.setDefaultFeatureForJSplitPane(centerSplitPane);

		//construct source panel
		sourceButtonPanel = new JPanel(new BorderLayout());
		sourceButtonPanel.setBorder(BorderFactory.createEmptyBorder());
		sourceLocationPanel = new JPanel(new BorderLayout(2, 0));
		sourceLocationPanel.setBorder(BorderFactory.createEmptyBorder());
//		sourceTreeCollapseAllAction = new TreeCollapseAllAction(sTree);
//		sourceTreeExpandAllAction = new TreeExpandAllAction(sTree);

		JToolBar sourceTreeToolBar = new JToolBar("Source Tree Tool Bar");
		sourceTreeToolBar.setFloatable(false);
//		sourceTreeToolBar.add(sourceTreeExpandAllAction);
//		sourceTreeToolBar.add(sourceTreeCollapseAllAction);
		sourceLocationPanel.add(sourceTreeToolBar, BorderLayout.WEST);

		sourceLocationArea.setEditable(false);
		sourceLocationArea.setPreferredSize(new Dimension((int) (FRAME_DEFAULT_WIDTH / 10), 24));
		sourceLocationPanel.add(sourceLocationArea, BorderLayout.CENTER);
		sourceLocationPanel.add(openSourceButton, BorderLayout.EAST);
		openSourceButton.setMnemonic('S');
		openSourceButton.setToolTipText(SELECT_CSV_TIP);
		openSourceButton.addActionListener(this);
		sourceButtonPanel.add(sourceLocationPanel, BorderLayout.NORTH);
//		sourceScrollPane = DefaultSettings.createScrollPaneWithDefaultFeatures();
		sourceScrollPane.setSize(new Dimension((int) (FRAME_DEFAULT_WIDTH / 4), (int) (FRAME_DEFAULT_HEIGHT / 1.5)));
		sourceButtonPanel.add(sourceScrollPane, BorderLayout.CENTER);

		//construct target panel
		targetButtonPanel = new JPanel(new BorderLayout());
		targetButtonPanel.setBorder(BorderFactory.createEmptyBorder());
		targetLocationPanel = new JPanel(new BorderLayout(2, 0));
		targetLocationPanel.setBorder(BorderFactory.createEmptyBorder());
//		targetTreeCollapseAllAction = new TreeCollapseAllAction(tTree);
//		targetTreeExpandAllAction = new TreeExpandAllAction(tTree);
		JToolBar targetTreeToolBar = new JToolBar("Target Tree Tool Bar");
		targetTreeToolBar.setFloatable(false);
//		targetTreeToolBar.add(targetTreeExpandAllAction);
//		targetTreeToolBar.add(targetTreeCollapseAllAction);
		targetLocationPanel.add(targetTreeToolBar, BorderLayout.WEST);
		targetLocationArea.setEditable(false);
		targetLocationArea.setPreferredSize(new Dimension((int) (FRAME_DEFAULT_WIDTH / 10), 24));
		targetLocationPanel.add(targetLocationArea, BorderLayout.CENTER);
		targetLocationPanel.add(openTargetButton, BorderLayout.EAST);
		openTargetButton.setMnemonic('T');
		openTargetButton.setToolTipText(SELECT_HMD_TIP);
		openTargetButton.addActionListener(this);
		targetButtonPanel.add(targetLocationPanel, BorderLayout.NORTH);
//		targetScrollPane = DefaultSettings.createScrollPaneWithDefaultFeatures();
		targetButtonPanel.add(targetScrollPane, BorderLayout.CENTER);
		targetButtonPanel.setPreferredSize(new Dimension((int) (FRAME_DEFAULT_WIDTH / 5), (int) (FRAME_DEFAULT_HEIGHT / 1.5)));

		//construct middle panel
		JPanel middleContainerPanel = new JPanel(new BorderLayout());
		JLabel placeHolderLabel = new JLabel();
		placeHolderLabel.setPreferredSize(new Dimension((int) (FRAME_DEFAULT_WIDTH / 3.5), 24));
		middlePanel = new MappingMiddlePanel(this);
		middlePanel.setSize(new Dimension((int) (FRAME_DEFAULT_WIDTH / 3), (int) (FRAME_DEFAULT_HEIGHT / 1.5)));
		middleContainerPanel.add(placeHolderLabel, BorderLayout.NORTH);
		middleContainerPanel.add(middlePanel, BorderLayout.CENTER);

		JSplitPane rightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		DefaultSettings.setDefaultFeatureForJSplitPane(rightSplitPane);
		rightSplitPane.setDividerLocation(0.5);
		rightSplitPane.setLeftComponent(middleContainerPanel);
		rightSplitPane.setRightComponent(targetButtonPanel);

		centerSplitPane.setLeftComponent(sourceButtonPanel);
		centerSplitPane.setRightComponent(rightSplitPane);

		topCenterPanel.add(centerSplitPane, BorderLayout.CENTER);
		topCenterPanel.setPreferredSize(new Dimension((int) (FRAME_DEFAULT_WIDTH * 0.8), (int) (FRAME_DEFAULT_HEIGHT / 1.5)));
		return topCenterPanel;
	}

	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		try
		{
			boolean everythingGood = true;
			if (SELECT_SOURCE.equals(command))
			{
				File file = DefaultSettings.getUserInputOfFileFromGUI(this, //FileUtil.getUIWorkingDirectoryPath(),
					SOURCE_TREE_FILE_DEFAULT_EXTENTION, OPEN_DIALOG_TITLE_FOR_DEFAULT_SOURCE_FILE, false, false);
				if (file != null)
				{
					everythingGood = processOpenSourceTree(file, true, true);
				}
			}
			else if (SELECT_TARGET.equals(command))
			{
				File file = DefaultSettings.getUserInputOfFileFromGUI(this,
//						TARGET_TREE_FILE_DEFAULT_EXTENTION, OPEN_DIALOG_TITLE_FOR_DEFAULT_TARGET_FILE, false, false);
						//FileUtil.getUIWorkingDirectoryPath(),
//					TARGET_TREE_FILE_DEFAULT_EXTENTION+";"+Cmps_V3_MESSAGE_FILE_DEFAULT_EXTENSION, OPEN_DIALOG_TITLE_FOR_DEFAULT_TARGET_FILE, false, false);
					//last added fileExtension :.h3s will be set as default
					Cmps_V3_MESSAGE_FILE_DEFAULT_EXTENSION+";"+TARGET_TREE_FILE_DEFAULT_EXTENTION, OPEN_DIALOG_TITLE_FOR_DEFAULT_TARGET_FILE, false, false);
				if (file != null)
				{
					everythingGood = processOpenTargetTree(file, true, true);
				}
			}
			if (!everythingGood)
			{
//				Message msg = MessageResources.getMessage("GEN3", new Object[0]);
//				JOptionPane.showMessageDialog(this, msg.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		catch (Exception e1)
		{
			DefaultSettings.reportThrowableToLogAndUI(this, e1, "", this, false, false);
		}
	}


	protected TreeNode loadSourceTreeData( Object metaInfo, File absoluteFile)throws Exception
	{
		// The following is changed by eric for the need of loading dbm file as the source, todo need refactory
		String fileExtension = FileUtil.getFileExtension(absoluteFile, true);

		TreeNode node = null;
		if (CSV_METADATA_FILE_DEFAULT_EXTENTION.equals(fileExtension))
		{
			// generate GUI nodes from object graph.
//			SCMMapSourceNodeLoader scmMapSourceNodeLoader = new SCMMapSourceNodeLoader();
//			node = scmMapSourceNodeLoader.loadData(metaInfo);
		}
//		else if (DATABASE_META_FILE_DEFAULT_EXTENSION.equals(fileExtension))
//		{
//			// generate GUI nodes from object graph.
//			DBMMapSourceNodeLoader dbmTreeNodeLoader = new DBMMapSourceNodeLoader();
//			node = dbmTreeNodeLoader.loadData(metaInfo);
//		}
		else
		{
			throw new ApplicationException("Unknow Source File Extension:" + absoluteFile,
				new IllegalArgumentException());
		}

		return node;
	}

	protected TreeNode loadTargetTreeData( Object metaInfo, File absoluteFile)throws Exception
	{
//		 The following is changed by eric for the need of loading dbm file as the source, todo need refactory
		String fileExtension = FileUtil.getFileExtension(absoluteFile, true);

		TreeNode nodes = null;
		if (HSM_META_DEFINITION_FILE_DEFAULT_EXTENSION.equals(fileExtension)
				||Cmps_V3_MESSAGE_FILE_DEFAULT_EXTENSION.equals(fileExtension))
		{
			// generate GUI nodes from object graph.
	        try
	        {
//	        	HSMMapTargetNodeLoader CmpsMapTargetNodeLoader = new HSMMapTargetNodeLoader();
//				nodes = CmpsMapTargetNodeLoader.loadData(metaInfo);
//	        	NewHSMBasicNodeLoader newHsmNodeLoader=new NewHSMBasicNodeLoader(false);
//	        	if(metaInfo!=null&&metaInfo instanceof MIFClass)
//	        		nodes=newHsmNodeLoader.loadMappingTargetData(metaInfo);
//	        	else
//	        		nodes = newHsmNodeLoader.loadMappingTargetData(absoluteFile);
	        }
	        catch(Throwable e)
	        {
	            //Log.logException(this.getClass(), "Cannot initialize the tree anymore!", e);
	            DefaultSettings.reportThrowableToLogAndUI(this, e, "Error occurred during tree initialitation", this, true, true);
	            return null;
	        }
		}
		else
		{
			throw new ApplicationException("Unknow Source File Extension:" + absoluteFile,
				new IllegalArgumentException());
		}
		return nodes;
	}

	protected void buildTargetTree(Object metaInfo, File absoluteFile, boolean isToResetGraph) throws Exception
	{
		TreeNode nodes=loadTargetTreeData(metaInfo,absoluteFile);
		//Build the target tree
		tTree = new MappingTargetTree(this.getMiddlePanel(), nodes);
//		tTree.getSelectionModel().addTreeSelectionListener((TreeSelectionListener) (getMappingDataManager().getPropertiesSwitchController()));
		targetScrollPane.setViewportView(tTree);
		tTree.expandAll();
		
//		TargetTreeDragTransferHandler targetTreeDragTransferHandler = null;
//		drag source for DnD to middle panel.
//		TargetTreeDragTransferHandler targetTreeDragTransferHandler = new TargetTreeDragTransferHandler(tTree, DnDConstants.ACTION_LINK);
//
//		//register collapse all and expand all actions.
//		targetTreeCollapseAllAction.setTree(tTree);
//		targetTreeExpandAllAction.setTree(tTree);
//		tTree.getInputMap().put(targetTreeCollapseAllAction.getAcceleratorKey(), targetTreeCollapseAllAction.getName());
//		tTree.getActionMap().put(targetTreeCollapseAllAction.getName(), targetTreeCollapseAllAction);
//		tTree.getInputMap().put(targetTreeExpandAllAction.getAcceleratorKey(), targetTreeExpandAllAction.getName());
//		tTree.getActionMap().put(targetTreeExpandAllAction.getName(), targetTreeExpandAllAction);
		
		if (sTree != null && isToResetGraph)
		{
			resetMiddlePanel();
		}
		if (absoluteFile != null)
		{
			String absoluteFilePath = absoluteFile.getAbsolutePath();
			targetLocationArea.setText(absoluteFilePath);
			targetLocationArea.setToolTipText(absoluteFilePath);
			mappingTargetFile = absoluteFile;//new File(absoluteFilePath);
		}
		else
		{
			mappingTargetFile = null;
		}
		if (this.getRootPane() != null)
		{
			this.getRootPane().repaint();
		}
//		getMappingFileSynchronizer().registerFile(MappingFileSynchronizer.FILE_TYPE.Target_File, absoluteFile);
////		drop target for DnD from source tree.
//		targetTreeDropTransferHandler = new TargetTreeDropTransferHandler(tTree, getMappingDataManager(), DnDConstants.ACTION_LINK);
	}

	/**
	 * Called by actionPerformed() and overridable by descendant classes.
	 *
	 * @param file
	 * @throws Exception
	 */
	protected boolean processOpenSourceTree(File file, boolean isToResetGraph, boolean supressReportIssuesToUI) throws Exception
	{
		String fileExtension = FileUtil.getFileExtension(file, true);

//		MetaParser parser = null;
//		MetaObject metaInfo = null;
//		BaseResult returnResult = null;
//
//		// parse the file into a meta object graph.
////		if (DATABASE_META_FILE_DEFAULT_EXTENSION.equals(fileExtension))
////		{
////			parser = new DatabaseMetaParserImpl();
////		}
////		else
////		{//default to CSV_METADATA_FILE_DEFAULT_EXTENTION
//			parser = new CSVMetaParserImpl();
////		}
//		returnResult = parser.parse(new FileReader(file));
//		ValidatorResults validatorResults = returnResult.getValidatorResults();
//		if (validatorResults != null && validatorResults.hasFatal())
//		{
//			Message msg = validatorResults.getMessages(ValidatorResult.Level.FATAL).get(0);
//			DefaultSettings.reportThrowableToLogAndUI(this, null, msg.toString(), this, true, supressReportIssuesToUI);
//			return false;
//		}
////
////		if (DATABASE_META_FILE_DEFAULT_EXTENSION.equals(fileExtension))
////		{
////			metaInfo = ((DatabaseMetaResult) returnResult).getDatabaseMeta();
////		}
////		else
////		{//default to HSM_META_DEFINITION_FILE_DEFAULT_EXTENSION
//			metaInfo = ((CSVMetaResult) returnResult).getCsvMeta();
////		}
//		buildSourceTree(metaInfo, file, isToResetGraph);
//		middlePanel.getMappingDataManager().registerSourceComponent(metaInfo, file);
		return true;
	}

	/**
	 * Called by actionPerformed() and overridable by descendant classes.
	 *
	 * @param file
	 * @throws Exception
	 */
	protected boolean processOpenTargetTree(File file, boolean isToResetGraph, boolean supressReportIssuesToUI) throws Exception
	{
		String fileExtension = FileUtil.getFileExtension(file, true);
		// parse the file into a meta object graph.
//		MetaParser parser = null;
//		MetaObject metaInfo = null;
//		BaseResult returnResult = null;
//
//		// The following is changed by eric for the need of loading dbm file as the source, todo need refactory
//
//		// parse the file into a meta object graph.
////		if (DATABASE_META_FILE_DEFAULT_EXTENSION.equals(fileExtension))
////		{
////			parser = new DatabaseMetaParserImpl();
////			returnResult = parser.parse(new FileReader(file));
////			ValidatorResults validatorResults = returnResult.getValidatorResults();
////			if (validatorResults != null && validatorResults.hasFatal())
////			{
////				Message msg = validatorResults.getMessages(ValidatorResult.Level.FATAL).get(0);
////				DefaultSettings.reportThrowableToLogAndUI(this, null, msg.toString(), this, true, supressReportIssuesToUI);
////				return false;
////			}
////		}
////		else
////		{//default to HSM_META_DEFINITION_FILE_DEFAULT_EXTENSION
////			parser = CmpsV3MetaFileParser.instance();
////		}
////		returnResult = parser.parse(new FileReader(file));
////		ValidatorResults validatorResults = returnResult.getValidatorResults();
////		if (validatorResults != null && validatorResults.hasFatal())
////		{
////			Message msg = validatorResults.getMessages(ValidatorResult.Level.FATAL).get(0);
////			DefaultSettings.reportThrowableToLogAndUI(this, null, msg.toString(), this, true, supressReportIssuesToUI);
////			return false;
////		}
//
////		if (DATABASE_META_FILE_DEFAULT_EXTENSION.equals(fileExtension))
////		{
////			metaInfo = ((DatabaseMetaResult) returnResult).getDatabaseMeta();
////		}
////		else
////		{//default to HSM_META_DEFINITION_FILE_DEFAULT_EXTENSION
////			metaInfo = ((CmpsV3MetaResult)returnResult).getCmpsV3Meta();
////		}
//		buildTargetTree(metaInfo, file, isToResetGraph);
//		middlePanel.getMappingDataManager().registerTargetComponent(metaInfo, file);
		return true;
	}

	/**
	 * Called by actionPerformed() and overridable by descendant classes.
	 *
	 * @param file
	 * @throws Exception changed from protected to pulic by sean
	 */
//	public ValidatorResults processOpenMapFile(File file) throws Exception
//	{
//		System.out.println("CmpsMappingPanel.processOpenMapFile()...:"+file.getAbsolutePath());
//		long stTime=System.currentTimeMillis();
//		// parse the file.
//		MapParserImpl parser = new MapParserImpl();
//		ValidatorResults validatorResults = parser.parse(file.getParent(), new FileReader(file));
//		if (validatorResults != null && validatorResults.hasFatal())
//		{//immediately return if it has fatal errors.
//			return validatorResults;
//		}
//		Mapping mapping = parser.getMapping();//returnResult.getMapping();
//
//		//build source tree
//		BaseComponent sourceComp = mapping.getSourceComponent();
//		Object sourceMetaInfo = sourceComp.getMeta();
//		File sourceFile = sourceComp.getFile();
//		buildSourceTree(sourceMetaInfo, sourceFile, false);
//		//build target tree
//		BaseComponent targetComp = mapping.getTargetComponent();
//		Object targetMetaInfo = targetComp.getMeta();
//		File targetFile = targetComp.getFile();
//		buildTargetTree(targetMetaInfo, targetFile, false);
//
//		middlePanel.getMappingDataManager().setMappingData(mapping);
//
//		//set both invisible since no use to allow user to change while mapping exists.
//		if (mapping.getFunctionComponent().size() > 0 || mapping.getMaps().size() > 0)
//		{
//			openSourceButton.setEnabled(false);
//			openTargetButton.setEnabled(false);
//		}
//		setSaveFile(file);
//		System.out.println("CmpsMappingPanel.processOpenMapFile()..timespending:"+(System.currentTimeMillis()-stTime));
//		return validatorResults;
//	}


	public Map getMenuItems(String menu_name)
	{
		Action action = null;
		Map <String, Action>actionMap = null;
//		ContextManager contextManager = ContextManager.getContextManager();
//		Map <String, Action>actionMap = contextManager.getClientMenuActions(MenuConstants.CSV_TO_CmpsV3, menu_name);
		if (MenuConstants.FILE_MENU_NAME.equals(menu_name))
		{
			JRootPane rootPane = this.getRootPane();
			if (rootPane != null)
			{//rootpane is not null implies this panel is fully displayed;
				//on the flip side, if it is null, it implies it is under certain construction.
//				contextManager.enableAction(ActionConstants.NEW_MAP_FILE, false);
//				contextManager.enableAction(ActionConstants.OPEN_MAP_FILE, true);
			}
		}
		//since the action depends on the panel instance,
		//the old action instance should be removed
//		if (actionMap!=null)
//			contextManager.removeClientMenuAction(MenuConstants.CSV_SPEC, menu_name, "");
//
////		if (actionMap==null)
////		{
//				action = new gov.nih.nci.caadapter.ui.mapping.Cmps.actions.SaveMapAction(this);
//				contextManager.addClientMenuAction(MenuConstants.CSV_TO_CmpsV3, MenuConstants.FILE_MENU_NAME,ActionConstants.SAVE, action);
//				contextManager.addClientMenuAction(MenuConstants.CSV_TO_CmpsV3, MenuConstants.TOOLBAR_MENU_NAME,ActionConstants.SAVE, action);
//				action.setEnabled(true);
//
//				action = new gov.nih.nci.caadapter.ui.mapping.Cmps.actions.SaveAsMapAction(this);
//				contextManager.addClientMenuAction(MenuConstants.CSV_TO_CmpsV3, MenuConstants.FILE_MENU_NAME,ActionConstants.SAVE_AS, action);
//				contextManager.addClientMenuAction(MenuConstants.CSV_TO_CmpsV3, MenuConstants.TOOLBAR_MENU_NAME,ActionConstants.SAVE_AS, action);
//				action.setEnabled(true);
//
//				action = new gov.nih.nci.caadapter.ui.mapping.mms.actions.AnotateAction(this);
//				contextManager.addClientMenuAction(MenuConstants.CSV_TO_CmpsV3, MenuConstants.FILE_MENU_NAME,ActionConstants.ANOTATE, action);
//				action.setEnabled(true);
//
//				action = new gov.nih.nci.caadapter.ui.mapping.Cmps.actions.ValidateMapAction(this);
//				contextManager.addClientMenuAction(MenuConstants.CSV_TO_CmpsV3, MenuConstants.FILE_MENU_NAME,ActionConstants.VALIDATE, action);
//				contextManager.addClientMenuAction(MenuConstants.CSV_TO_CmpsV3, MenuConstants.TOOLBAR_MENU_NAME,ActionConstants.VALIDATE, action);
//				action.setEnabled(true);
//
//				action = new gov.nih.nci.caadapter.ui.mapping.Cmps.actions.CloseMapAction(this);
//				contextManager.addClientMenuAction(MenuConstants.CSV_TO_CmpsV3, MenuConstants.FILE_MENU_NAME,ActionConstants.CLOSE, action);
//				action.setEnabled(true);
//
//				action = new gov.nih.nci.caadapter.ui.mapping.Cmps.actions.GenerateReportAction(this);
//				contextManager.addClientMenuAction(MenuConstants.CSV_TO_CmpsV3, MenuConstants.REPORT_MENU_NAME,ActionConstants.GENERATE_REPORT, action);
//				contextManager.addClientMenuAction(MenuConstants.CSV_TO_CmpsV3, MenuConstants.TOOLBAR_MENU_NAME,ActionConstants.GENERATE_REPORT, action);
//				action.setEnabled(true);
//
//				action = new RefreshMapAction(this);
//				contextManager.addClientMenuAction(MenuConstants.CSV_TO_CmpsV3, MenuConstants.TOOLBAR_MENU_NAME,ActionConstants.REFRESH, action);
//				action.setEnabled(true);
//
//				actionMap = contextManager.getClientMenuActions(MenuConstants.CSV_TO_CmpsV3, menu_name);
////		}
		return actionMap;
	}

	/**
	 * return the open action inherited with this client.
	 */
	public Action getDefaultOpenAction()
	{
//		ContextManager contextManager = ContextManager.getContextManager();
//		Action openAction=contextManager.getDefinedAction(ActionConstants.OPEN_MAP_FILE);
//		return openAction;
		return null;
	}

	/**
	 * Explicitly reload information from the internal given file.
	 *
	 * @throws Exception
	 */
	public void reload() throws Exception
	{
//		processOpenMapFile(getSaveFile());
	}

//	protected TreeDefaultDropTransferHandler getTargetTreeDropTransferHandler()
//	{
//		return this.targetTreeDropTransferHandler;
//	}

	/**
	 * Reload the file specified in the parameter.
	 * @param changedFileMap
	 */
//	public void reload(Map<MappingFileSynchronizer.FILE_TYPE, File> changedFileMap)
//	{
//		/**
//		 * Design rationale:
//		 * 1) if the changedFileMap is null, simply return;
//		 * 2) if the getSaveFile() method does not return null, it implies current panel associates with a mapping file,
//		 * just reload the whole mapping file so as to refresh those mapping relationship;
//		 * 3) if the getSaveFile() returns null, just reload source and/or target file within the changedFileMap,
//		 * and ignore the checking of MappingFileSynchronizer.FILE_TYPE.Mapping_File item in the map;
//		 */
//		if(changedFileMap==null)
//		{
//			return;
//		}
//		File existMapFile = getSaveFile();
//		try
//		{
//			if(existMapFile!=null)
//			{
//				if(existMapFile.exists())
//				{
//					processOpenMapFile(existMapFile);
//				}
//				else
//				{//exist map file does not exist anymore
//					JOptionPane.showMessageDialog(this, existMapFile.getAbsolutePath() + " does not exist or is not accessible anymore", "File Error", JOptionPane.ERROR_MESSAGE);
//					return;
//				}
//			}
//			else
//			{//exist map file does not exist, simply reload source and/or target file
//				Iterator it = changedFileMap.keySet().iterator();
//				while(it.hasNext())
//				{
//					MappingFileSynchronizer.FILE_TYPE key = (MappingFileSynchronizer.FILE_TYPE) it.next();
//					File file = changedFileMap.get(key);
//					if(GeneralUtilities.areEqual(MappingFileSynchronizer.FILE_TYPE.Source_File, key))
//					{
//						processOpenSourceTree(file, true, true);
//					}
//					else if(GeneralUtilities.areEqual(MappingFileSynchronizer.FILE_TYPE.Target_File, key))
//					{
//						processOpenTargetTree(file, true, true);
//					}
//				}//end of while
//			}//end of else
//		}
//		catch (Exception e)
//		{
//			DefaultSettings.reportThrowableToLogAndUI(this, e, "", this, false, false);
//		}
//	}
	
	
	protected void buildSourceTree(Object metaInfo, File absoluteFile, boolean isToResetGraph) throws Exception
	{
		TreeNode nodes=loadSourceTreeData(metaInfo,absoluteFile);

		//Build the source tree
		sTree = new MappingSourceTree(middlePanel, nodes);
//		sTree.getSelectionModel().addTreeSelectionListener((TreeSelectionListener) (getMappingDataManager().getPropertiesSwitchController()));
//		sourceTreeDragTransferHandler = new TreeDefaultDragTransferHandler(sTree, DnDConstants.ACTION_LINK);
		sourceScrollPane.setViewportView(sTree);
		sTree.expandAll();
 
		//register collapse all and expand all actions.
//		sourceTreeCollapseAllAction.setTree(sTree);
//		sourceTreeExpandAllAction.setTree(sTree);
//		sTree.getInputMap().put(sourceTreeCollapseAllAction.getAcceleratorKey(), sourceTreeCollapseAllAction.getName());
//		sTree.getActionMap().put(sourceTreeCollapseAllAction.getName(), sourceTreeCollapseAllAction);
//		sTree.getInputMap().put(sourceTreeExpandAllAction.getAcceleratorKey(), sourceTreeExpandAllAction.getName());
//		sTree.getActionMap().put(sourceTreeExpandAllAction.getName(), sourceTreeExpandAllAction);

		if (tTree != null && isToResetGraph)
		{
			resetMiddlePanel();
		}
		if (absoluteFile != null)
		{
			String absoluteFilePath = absoluteFile.getAbsolutePath();
			sourceLocationArea.setText(absoluteFilePath);
			sourceLocationArea.setToolTipText(absoluteFilePath);
			mappingSourceFile = absoluteFile;//new File(absoluteFilePath);
		}
		else
		{
			mappingSourceFile = null;
		}
		if (this.getRootPane() != null)
		{
			this.getRootPane().repaint();
		}
//		getMappingFileSynchronizer().registerFile(MappingFileSynchronizer.FILE_TYPE.Source_File, absoluteFile);
    }
	
	protected void resetMiddlePanel()
	{
		if (middlePanel != null)
		{
			middlePanel.resetGraph();
			middlePanel.repaint();
		}
	}
	
	protected JComponent getCenterPanel(boolean functionPaneRequired)
		{//construct the top level layout of mapping panel
			/**
			 * GUI Layout:
			 * JSplitPane - Horizontal:
			 *      left: JSplitPane - Horizontal:
			 *				left: source panel;
			 *				right: JSplitPane - Horizontal:
			 *							left: middle panel for graph;
			 *							right: target panel;
			 * 		right: JSplitPane - Vertical:
			 * 				top: functional pane;
			 *				bottom: properties panel;
			 */

			JSplitPane leftRightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			leftRightSplitPane.setOneTouchExpandable(false);
			DefaultSettings.setDefaultFeatureForJSplitPane(leftRightSplitPane);
			leftRightSplitPane.setDividerLocation(0.85);
			leftRightSplitPane.setLeftComponent(getTopLevelLeftPanel());
			leftRightSplitPane.setRightComponent(getTopLevelRightPanel(functionPaneRequired));
			return leftRightSplitPane;
		}

		/**
		 * This constructs function and properties panels.
		 *
		 * @return the top level right pane.
		 */
		private JComponent getTopLevelRightPanel(boolean functionPaneRequired)
		{
			JSplitPane topBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			DefaultSettings.setDefaultFeatureForJSplitPane(topBottomSplitPane);
			topBottomSplitPane.setDividerLocation(0.5);
			
//			functionPane = new FunctionLibraryPane();
//			functionPane.setBorder(BorderFactory.createTitledBorder("Functions"));
//			if(functionPaneRequired)
//			{
//				topBottomSplitPane.setTopComponent(functionPane);
//			}
//			propertiesPane = new DefaultPropertiesPage(this.getMappingDataManager().getPropertiesSwitchController());
//			topBottomSplitPane.setBottomComponent(propertiesPane);

			double topCenterFactor = 0.3;
			Dimension rightMostDim = new Dimension((int) (FRAME_DEFAULT_WIDTH / 11), (int) (FRAME_DEFAULT_HEIGHT * topCenterFactor));
//			propertiesPane.setPreferredSize(rightMostDim);
//			functionPane.setPreferredSize(rightMostDim);
//			functionPane.getFunctionTree().getSelectionModel().addTreeSelectionListener((TreeSelectionListener) (getMappingDataManager().getPropertiesSwitchController()));

			topCenterFactor = 1.5;
			rightMostDim = new Dimension((int) (FRAME_DEFAULT_WIDTH / 10), (int) (FRAME_DEFAULT_HEIGHT / topCenterFactor));
			topBottomSplitPane.setSize(rightMostDim);

			return topBottomSplitPane;
		}
		
		/**
	 * Return whether the mapping module is in drag-and-drop mode.
	 * @return whether the mapping module is in drag-and-drop mode.
	 */
	public boolean isInDragDropMode()
	{
        boolean checkSourceTreeDragTransferHandler = false;
        boolean checkTargetTreeDropTransferHandler = false;
        boolean checkMiddlePanel = false;
//        checkSourceTreeDragTransferHandler = sourceTreeDragTransferHandler.isInDragDropMode();
//        checkTargetTreeDropTransferHandler = getTargetTreeDropTransferHandler().isInDragDropMode();
        checkMiddlePanel = middlePanel.getMiddlePanelDropTransferHandler().isInDragDropMode();       
        return (checkSourceTreeDragTransferHandler ||
				checkTargetTreeDropTransferHandler ||
				checkMiddlePanel);
	}

	/**
	 * Set a new value for the mode.
	 * @param newValue
	 */
	public void setInDragDropMode(boolean newValue)
	{
//        if (sourceTreeDragTransferHandler == null)
//        {
//            JOptionPane.showMessageDialog(this, "You should input the source file name first.", "No Source file", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        else 
//        	sourceTreeDragTransferHandler.setInDragDropMode(newValue);
//        
//        if (getTargetTreeDropTransferHandler() == null)
//        {
//            JOptionPane.showMessageDialog(this, "You should input the target file name first.", "No Target file", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        else 
//        	getTargetTreeDropTransferHandler().setInDragDropMode(newValue);
		middlePanel.getMiddlePanelDropTransferHandler().setInDragDropMode(newValue);
	}
	
	/**
	 * Set a new save file.
	 *
	 * @param saveFile
	 * @return true if the value is changed, false otherwise.
	 */
	public boolean setSaveFile(File saveFile)
	{
		//removed the equal check so as to support explicit refresh or reload call.
//		ContextManager contextManager = ContextManager.getContextManager();
		boolean sameFile = GeneralUtilities.areEqual(this.saveFile, saveFile);
		if(!sameFile)
		{//remove interest in the context file manager, first for old file

//				contextManager.getContextFileManager().removeFileUsageListener(this);
		}
		this.saveFile = saveFile;
//		if(!sameFile)
//		{//register interest in the context file manager for new file
//				contextManager.getContextFileManager().registerFileUsageListener(this);
//		}
		updateTitle(this.saveFile.getName());
//		getMappingFileSynchronizer().registerFile(MappingFileSynchronizer.FILE_TYPE.Mapping_File, saveFile);
		return true;
	}
	
	public JScrollPane getSourceScrollPane() {
		return sourceScrollPane;
	}

	public JScrollPane getTargetScrollPane() {
		return targetScrollPane;
	}

	public MappingMiddlePanel getMiddlePanel() {
		return middlePanel;
	}
	    
	/**
     * Return the mapping data manager.
     * 
     * @return the mapping data manager.
     */
	public MiddlePanelJGraphController getMappingDataManager() {
		return middlePanel.getMiddlePanelJGraphController();
	}

	public JTree getSourceTree()
	{
		return sTree;
	}

	public JTree getTargetTree()
	{
		return tTree;
	}
	/**
	 * Return if the source tree has been populated.
	 * @return if the source tree has been populated.
	 */
	public boolean isSourceTreePopulated()
	{
		return sTree!=null;
	}

	/**
	 * Return if the target tree has been populated.
	 * @return if the target tree has been populated.
	 */
	public boolean isTargetTreePopulated()
	{
		return tTree!=null;
	}


	/**
	 * Provide the extended implementation of this method by adding additional files of source and target;
	 *
	 * @return a list of file objects that this context is associated with.
	 */
	public java.util.List<File> getAssociatedFileList()
	{
		List<File> resultList = new ArrayList<java.io.File>();
		if(saveFile!=null)
		{
			resultList.add(saveFile);
		}
		if (mappingSourceFile != null)
		{
			resultList.add(mappingSourceFile);
		}
		if (mappingTargetFile != null)
		{
			resultList.add(mappingTargetFile);
		}
		return resultList;
	}
	
	public void setSize(Dimension newDimension)
	{
		setSize((int) newDimension.getWidth(), (int) newDimension.getHeight());
	}

	public void setSize(int width, int height)
	{
		this.resize(width, height);
	}

	public void resize(int width, int height)
	{
		double topCenterFactor = 1;
		sourceLocationArea.setSize(new Dimension((int) (width / 6), 25));
		sourceScrollPane.setSize(new Dimension((int) (width / 4.5), (int) (height * topCenterFactor)));
		targetLocationArea.setSize(new Dimension(width / 6, 25));
		targetScrollPane.setSize(new Dimension((int) (width / 4.5), (int) (height * topCenterFactor)));
		middlePanel.setSize(new Dimension((int) (width / 4), (int) (height * topCenterFactor)));

		topCenterFactor = 0.5;
		Dimension rightMostDim = new Dimension((int) (width / 5), (int) (height * topCenterFactor));
//		propertiesPane.setSize(rightMostDim);
//		functionPane.setSize(rightMostDim);
	}
	

	/**
	 * Explicitly set the value.
	 *
	 * @param newValue
	 */
	public void setChanged(boolean newValue)
	{
		middlePanel.getGraphController().setGraphChanged(newValue);
	}


	/**
	 * Return the save file.
	 * @return the save file.
	 */
	public File getSaveFile()
	{
		return saveFile;
	}

	public void synchronizeRegisteredFile(boolean notigyOberver)
	{
		//do nothing, only the "MappingFilePanel" will implement it
	}
	/**
	 * Overridable function to update Title in the tabbed pane.
	 * @param newTitle
	 */
	private void updateTitle(String newTitle)
	{
		JRootPane rootPane = getRootPane();
		if (rootPane != null)
		{
			Container container = rootPane.getParent();
//			if (container instanceof AbstractMainFrame)
//			{
//				((AbstractMainFrame)container).setCurrentPanelTitle(newTitle);
//			}
		}
	}

	/**
	 * return the close action inherited with this client.
	 * @return the close action inherited with this client.
	 */
	public Action getDefaultCloseAction()
	{//by doing this way, the menu and the panel will use the same close action.
		Map actionMap = getMenuItems(MenuConstants.FILE_MENU_NAME);
		Action closeAction = (Action) actionMap.get(ActionConstants.CLOSE);
		return closeAction;
	}

	/**
	 * return the save action inherited with this client.
	 * @return the save action inherited with this client.
	 */
	public Action getDefaultSaveAction()
	{
		Map actionMap = getMenuItems(MenuConstants.FILE_MENU_NAME);
		Action saveAction = (Action) actionMap.get(ActionConstants.SAVE);
		return saveAction;
	}

	/**
	 * Return the top root container (frame or dialog or window) this panel is associated with.
	 * @return the top root container (frame or dialog or window) this panel is associated with.
	 */
	public Container getRootContainer()
	{
		return DefaultSettings.findRootContainer(this);
	}


    /**
	 * Return a list of Action objects that is included in this Context manager.
	 * @return a list of Action objects that is included in this Context manager.
	 */
    public java.util.List<Action> getToolbarActionList()
    {
        java.util.List<Action> actions = new ArrayList<Action>();
        actions.add(getDefaultOpenAction());
        //the menu bar display its buttons inorder
        Map <String, Action>actionMap = getMenuItems(MenuConstants.TOOLBAR_MENU_NAME);
		actions.add((Action) actionMap.get(ActionConstants.SAVE));
		actions.add((Action) actionMap.get(ActionConstants.VALIDATE));
		//add the "Refresh" menu if exist
		actions.add((Action) actionMap.get(ActionConstants.REFRESH));
		return actions;
    }

	public MiddlePanelJGraphController getMiddlePanelJGraphController() {
		return middlePanel.getGraphController();
	}
    

}

/**
 * HISTORY: $Log: not supported by cvs2svn $
 * HISTORY: Revision 1.2  2008/10/30 16:02:14  linc
 * HISTORY: updated.
 * HISTORY:
 * HISTORY: Revision 1.1  2008/10/27 20:06:30  linc
 * HISTORY: GUI first add.
 * HISTORY:
 */

