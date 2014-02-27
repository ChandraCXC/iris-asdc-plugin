/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asdc.iris.plugin.asdcirisplugin;

import com.sun.rowset.CachedRowSetImpl;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.asdc.iris.plugin.CatalogInfo;

/**
 *
 * @author fabri
 */
public class test {
    
     public static void main(String[] args) {

        String encoded = "";
        try
        {

        URL url = new URL("http://toolsdev.asdc.asi.it/SED/AvailableAnonymousCatalogs");
        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            
          encoded += line;
        
        }
        } catch (MalformedURLException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }

       
        //deserailize
        
        ArrayList rs = null;
        byte[] out = null;
       
                out = Base64.decodeBase64(encoded);
      
        System.out.println("Got parameters");
        ByteArrayInputStream bis = new ByteArrayInputStream(out);

        try {
                ObjectInputStream obj_in = new ObjectInputStream(bis);

                rs = (ArrayList) obj_in.readObject();
        } catch (Exception e) {
                e.printStackTrace();
        }
        
        System.out.println("END" + rs.size());
        /*
        List <String> catOrder = new ArrayList<String>();
        Map catalogType = new Hashtable();
        Map catalogSubType = new Hashtable();
        String tempSubName = "";
        
        try {
            while (rs.next()) { 
                        
                        int catalogId = rs.getInt("catalog_id");
                        String catalogName = rs.getString("catalog_name");
                        double radius = rs.getDouble("errorRadius");
                        
                        CatalogInfo tempCat = new CatalogInfo(catalogId,catalogName,radius);
                        System.out.println("FABRI: creato cataloginfo name:" + catalogName);
                        String catTypeName = rs.getString("name");
                        
                        if (rs.getObject(27) == null) { //non appartiene a un sottogruppo
                            
                            tempSubName = "";
                            
                            if (catalogType.containsKey(catTypeName)) {
                                ArrayList temp = (ArrayList) catalogType.get(catTypeName);
                                temp.add(tempCat);
                                catalogType.put(catTypeName, temp);
                            } else {
                                 ArrayList temp = new ArrayList();
                                 temp.add(catTypeName);
                                 temp.add(tempCat);
                                 catalogType.put(catTypeName, temp);
                                 catOrder.add(catTypeName);
                            }
                            
                        }
                        else { //appartiene ad un sottogrutto
                            
                            
                            catTypeName = rs.getString(28);
                            
                            if (tempSubName.equals("") || !tempSubName.equals(rs.getString("name"))) {
                                
                                
                                if (!catalogType.containsKey(catTypeName)) {
                                    
                                    ArrayList temp = new ArrayList();
                                    temp.add(catTypeName);
                                    catalogType.put(catTypeName, temp);
                                    catOrder.add(catTypeName);
                                }
                                
                                tempSubName = rs.getString("name");
                            
                                if (!catalogSubType.containsKey(catTypeName+"|"+tempSubName)) {
                                    
                                    ArrayList temp = new ArrayList();
                                    temp.add(tempSubName);
                                    temp.add(tempCat);
                                    catalogSubType.put(catTypeName+"|"+tempSubName, temp);
                                }
                                else {
                                
                                    ArrayList temp = (ArrayList) catalogSubType.get(catTypeName+"|"+tempSubName);
                                    temp.add(tempCat);
                                    catalogSubType.put(catTypeName+"|"+tempSubName, temp);
                                }
                            }
                            else {
                           
                              ArrayList temp = (ArrayList) catalogSubType.get(catTypeName+"|"+tempSubName);
                              temp.add(tempCat);
                              catalogSubType.put(catTypeName+"|"+tempSubName, temp);  
                            }
                            
                        }

                    }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
                
                
                ArrayList rootTree = new ArrayList();
                rootTree.add("Catalogs");

                for (Object key : catalogSubType.keySet()) {
                    
                    String name = (String) key;
                    
                    String[] tempname = name.split("\\|");
                   
                    ArrayList tempsub = (ArrayList) catalogSubType.get(name);
                    ArrayList temp = (ArrayList) catalogType.get(tempname[0]);
                    
                    temp.add(tempsub);
                    
                    catalogType.put(tempname[0], temp);
                }
                
                for (Object value : catOrder.toArray()) {
                    
                    String name = (String) value;
                    ArrayList arr = (ArrayList) catalogType.get(name);
                    rootTree.add(arr);
                }
                
                System.out.println("end");*/
    }
    
}
