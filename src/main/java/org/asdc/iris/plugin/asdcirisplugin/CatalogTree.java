/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asdc.iris.plugin.asdcirisplugin;

import java.io.*;
import org.asdc.iris.plugin.CatalogInfo;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Roberto
 */
public class CatalogTree implements Serializable{
    
    public CatalogTree() {

    }

    public static JTree getCatalogTree(String asdcUrl) {

        
        String encoded = "";
        try
        {

        URL url = new URL(asdcUrl + "AvailableAnonymousCatalogs");
        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            
          encoded += line;
        
        }
        } catch (MalformedURLException e) {
            Logger.getLogger(CatalogTree.class.getName()).log(Level.SEVERE,
                            e.getMessage());
        
        } catch (IOException e) {
            Logger.getLogger(CatalogTree.class.getName()).log(Level.SEVERE,
                            e.getMessage());
        
        }

       
        //deserailize
        
        ArrayList rootTree = null;
        byte[] out = null;
       
                out = Base64.decodeBase64(encoded);
      
        ByteArrayInputStream bis = new ByteArrayInputStream(out);

        try {
                ObjectInputStream obj_in = new ObjectInputStream(bis);

                rootTree = (ArrayList) obj_in.readObject();
                Logger.getLogger(CatalogTree.class.getName()).log(Level.INFO, 
                    "Got catalog list from servlet");
        } catch (Exception e) {
                
            Logger.getLogger(CatalogTree.class.getName()).log(Level.SEVERE,
                            e.getMessage());
        }

        //DefaultMutableTreeNode root = processHierarchy(hierarchyCatalog);
        
        DefaultMutableTreeNode root = processHierarchy(rootTree);

        JTree tree = new JTree(root);
        
        // makes your tree as CheckTree
        //CheckTreeManager checkTreeManager = new CheckTreeManager(tree);

        //return checkTreeManager;
        return tree;

        // to get the paths that were checked
        //TreePath checkedPaths[] = checkTreeManager.getSelectionModel().getSelectionPaths();


    }

    private static DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
        DefaultMutableTreeNode node =
                new DefaultMutableTreeNode(hierarchy[0]);
        DefaultMutableTreeNode child;
        for (int i = 1; i < hierarchy.length; i++) {
            Object nodeSpecifier = hierarchy[i];
            if (nodeSpecifier instanceof Object[]) // Ie node with children
            {
                child = processHierarchy((Object[]) nodeSpecifier);
            } else {
                child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf

            }

            node.add(child);
        }
        return (node);
    }
    
    private static DefaultMutableTreeNode processHierarchy(ArrayList hierarchy) {
        DefaultMutableTreeNode node =
                new DefaultMutableTreeNode(hierarchy.get(0));
        DefaultMutableTreeNode child;
        for (int i = 1; i < hierarchy.size(); i++) {
            Object nodeSpecifier = hierarchy.get(i);
            if (nodeSpecifier instanceof ArrayList) // Ie node with children
            {
                child = processHierarchy((ArrayList) nodeSpecifier);
            } else {
                child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf

            }

            node.add(child);
        }
        return (node);
    }
    
}
