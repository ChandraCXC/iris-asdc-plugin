/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asdc.iris.plugin.asdcirisplugin;

import cfa.vo.iris.IrisComponent;
import cfa.vo.iris.sdk.IrisPlugin;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fabri
 */
public class AsdcPlugin implements IrisPlugin{
    
    private List<IrisComponent> components;
    
    public AsdcPlugin () {
    
        components = new ArrayList();
        components.add(new AsdcComponent());
    }

    @Override
    public String getName() {
        return "ASDC SED Plugin";
    }

    @Override
    public String getDescription() {
        return "ASDC SED Plugin";
    }

    @Override
    public String getVersion() {
        return "1.1.7";
    }

    @Override
    public String getAuthor() {
        return "ASDC";
    }

    @Override
    public List<IrisComponent> getComponents() {
        return components;
    }

    @Override
    public String getAcknowledgments() {
        return "";
    }
    
}
