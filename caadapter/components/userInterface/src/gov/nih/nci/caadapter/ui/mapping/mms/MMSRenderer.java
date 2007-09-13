/**
 * 
 */
package gov.nih.nci.caadapter.ui.mapping.mms;

import gov.nih.nci.caadapter.mms.metadata.ModelMetadata;
import gov.nih.nci.caadapter.ui.common.DefaultSettings;
import gov.nih.nci.caadapter.ui.common.tree.DefaultTargetTreeNode;

import java.awt.Component;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author wuye
 *
 */
public class MMSRenderer extends DefaultTreeCellRenderer
{
  // this control comes here	
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        ImageIcon tutorialIcon;
        String table = "";

        ModelMetadata modelMetadata = ModelMetadata.getInstance();
        List<String> lazyKeys = modelMetadata.getLazyKeys();
//        System.out.println( "*** Current Lazy Keys = " + lazyKeys );

        try
        {
            String _tmpStr = (String) ((DefaultMutableTreeNode) value).getUserObject();
            if (_tmpStr.equalsIgnoreCase("Data Model"))
            {
                tutorialIcon = createImageIcon("database.png");
                setIcon(tutorialIcon);
                setToolTipText("Data model");
            }
            else
            {
                tutorialIcon = createImageIcon("schema.png");
                setIcon(tutorialIcon);
                setToolTipText("Schema");
            }
            return this;
        } catch (Exception e) 
        { 
        	//continue 
        }

        try
        {
            gov.nih.nci.caadapter.mms.metadata.TableMetadata qbTableMetaData = (gov.nih.nci.caadapter.mms.metadata.TableMetadata) ((DefaultTargetTreeNode) value).getUserObject();
            table = qbTableMetaData.getName();
            //System.out.println("Tables " + table );

            if (qbTableMetaData.getType().equalsIgnoreCase("normal"))
            {
                tutorialIcon = createImageIcon("table.png");
                setIcon(tutorialIcon);
                setToolTipText("Table");
            } else if (qbTableMetaData.getType().equalsIgnoreCase("VIEW"))
            {
                tutorialIcon = createImageIcon("view.png");
                setIcon(tutorialIcon);
                setToolTipText("View");
            }
        } catch (ClassCastException e)
        {
            try
            {
                gov.nih.nci.caadapter.mms.metadata.ColumnMetadata queryBuilderMeta = (gov.nih.nci.caadapter.mms.metadata.ColumnMetadata) ((DefaultTargetTreeNode) value).getUserObject();
                //System.out.println("Column " + queryBuilderMeta.getXPath() );
                boolean lazyKeyFound = false;

                for( String key : lazyKeys )
                {
                   if ( queryBuilderMeta.getXPath().contains( key ))
                   {
                       System.out.println("Found a Lazy Key " + key );
                       lazyKeyFound = true;
                   }

                }

                if ( lazyKeyFound ) {
                    tutorialIcon = createImageIcon("columnlazy.png");
                    setIcon(tutorialIcon);
                    setToolTipText("Lazy");
                }
                else {
                       tutorialIcon = createImageIcon("columneager.png");
                       setIcon(tutorialIcon);
                       setToolTipText("Eager");
                }

               // }
            } catch (Exception ee)
            {
                try
                {
                    //String queryBuilderMeta = (String) ((DefaultSourceTreeNode) value).getUserObject();
                    tutorialIcon = createImageIcon("load.gif");
                    setIcon(tutorialIcon);
                } catch (Exception e1)
                {
                    setToolTipText(null);
                }
            }
        }
        
        return this;
    }
	protected static ImageIcon createImageIcon(String path)
	{
	    //java.net.URL imgURL = Database2SDTMMappingPanel.class.getResource(path);
	    java.net.URL imgURL = DefaultSettings.class.getClassLoader().getResource("images/" + path);
	    if (imgURL != null)
	    {
	        //System.out.println("class.getResource is "+imgURL.toString());
	        return new ImageIcon(imgURL);
	    } else
	    {
	        System.err.println("Couldn't find file: " + imgURL.toString() + " & " + path);
	        return null;
	    }
	}
}

