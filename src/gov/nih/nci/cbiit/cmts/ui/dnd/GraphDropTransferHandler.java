package gov.nih.nci.cbiit.cmts.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nih.nci.cbiit.cmts.core.FunctionDef;
import gov.nih.nci.cbiit.cmts.ui.jgraph.MiddlePanelJGraphController;

public class GraphDropTransferHandler extends CommonTransferHandler {
private MiddlePanelJGraphController graphController;
private static DataFlavor[] acceptableFlavors =new DataFlavor[]{TreeTransferableNode.mutableTreeNodeFlavor};

private static  final List<DataFlavor> acceptableFlavorsList = Arrays.asList( TreeTransferableNode.flavors );

/**
 * @return the graphController
 */
public MiddlePanelJGraphController getGraphController() {
	return graphController;
}

/**
 * @param graphController the graphController to set
 */
public void setGraphController(MiddlePanelJGraphController graphController) {
	this.graphController = graphController;
}

/* (non-Javadoc)
 * @see javax.swing.TransferHandler#importData(javax.swing.TransferHandler.TransferSupport)
 */
@Override
public boolean importData(TransferSupport info) 
{
	Object transferableObject;
    try {
    	transferableObject = info.getTransferable().getTransferData(TreeTransferableNode.mutableTreeNodeFlavor);
    } catch (UnsupportedFlavorException e) {
    	e.printStackTrace();
        return false;
    } catch (IOException e) {
    	e.printStackTrace();
        return false;
    }
    System.out.println("GraphDropTransferHandler.importData()..transferable object:"+transferableObject);
    if (transferableObject instanceof DefaultMutableTreeNode)
    {
    	Object nodeUserObject=((DefaultMutableTreeNode)transferableObject).getUserObject();
    	if (nodeUserObject instanceof FunctionDef)
    	{
    		FunctionDef cloneData=(FunctionDef)((FunctionDef)nodeUserObject).clone();
    		return getGraphController().addFunction(cloneData, info.getDropLocation().getDropPoint());
    	}
    	System.out.println("GraphDropTransferHandler.importData()...not support:"+nodeUserObject);
    }
    else if (transferableObject instanceof FunctionDef)
    {
		FunctionDef cloneData=(FunctionDef)((FunctionDef)transferableObject).clone();
		return getGraphController().addFunction(cloneData, info.getDropLocation().getDropPoint());
	}
    return false;
}
///* (non-Javadoc)
// * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor)
// */
//@Override
// public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
//
//	System.out.println("GraphDropTransferHandler.canImport()..check super_flavors:"+super.canImport(comp, transferFlavors));
//	for (int i=0; i<transferFlavors.length;i++)
//	{
//		if (acceptableFlavorsList.contains(transferFlavors[i]))
//			return true;
//	}
//	System.out.println("GraphDropTransferHandler.canImport()..not acceptable DataFlavor kinds:"+transferFlavors.length);
//	return super.canImport(comp, transferFlavors);
// }
/* (non-Javadoc)
 * @see javax.swing.TransferHandler#canImport(javax.swing.TransferHandler.TransferSupport)
 */
@Override
public boolean canImport(TransferSupport info) {
//	System.out.println("GraphDropTransferHandler.canImport()..check super_TransferSupport:"+super.canImport(info));
	if (info.getTransferable().isDataFlavorSupported(TreeTransferableNode.mutableTreeNodeFlavor))
		return true;
//	System.out.println("GraphDropTransferHandler.canImport()..check support..:not support");
    return super.canImport(info);
}
}
