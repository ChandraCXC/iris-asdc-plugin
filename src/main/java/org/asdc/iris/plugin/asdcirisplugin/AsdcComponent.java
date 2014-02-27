/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asdc.iris.plugin.asdcirisplugin;

import cfa.vo.iris.ICommandLineInterface;
import cfa.vo.iris.IMenuItem;
import cfa.vo.iris.IWorkspace;
import cfa.vo.iris.IrisApplication;
import cfa.vo.iris.NullCommandLineInterface;
import cfa.vo.iris.gui.GUIUtils;
import cfa.vo.iris.gui.NarrowOptionPane;
import cfa.vo.iris.sdk.AbstractIrisComponent;
import cfa.vo.iris.sdk.AbstractPluginMenuItem;
import cfa.vo.iris.sdk.PluginJar;
import cfa.vo.iris.sdk.PluginManager;
import cfa.vo.iris.sed.SedlibSedManager;
import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AsdcComponent extends AbstractIrisComponent {

    private List<IMenuItem> menus = new AsdcMenus();
    private PluginMainFormCal form;

    @Override
    public String getName() {
        return "ASDC SED Data";
    }

    @Override
    public String getDescription() {
        return "ASDC SED Data";
    }

    @Override
    public ICommandLineInterface getCli() {
        return new NullCommandLineInterface("builderasdc");
    }

    @Override
    public List<IMenuItem> getMenus() {
        return menus;
    }

    @Override
    public void init(IrisApplication app, IWorkspace ws) {
        super.init(app, ws);
        File pluginDir = new File(app.getConfigurationDir().getAbsolutePath() + "/components");
        String[] list = pluginDir.list(new FilenameFilter() {

            @Override
            public boolean accept(File file, String string) {
                return string.startsWith("AsdcIrisPlugin");
            }
        });
        if (list!=null && list.length != 0) {
            int confirm = NarrowOptionPane.showOptionDialog(null,
                    "<html>It looks like you have installed the ASDC Plugin in the past. The new version of the ASDC Plugin is now shipped with Iris.\n"
                    + "Please click OK to delete the old plugin version and continue with the new version only.\nClick on Cancel if you want to maintain both versions.\n"
                    + "You can delete the plugins using the Plugin Manager interface from the Tools menu.",
                    "Plugin Warning",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, null, null);

            if (confirm == 0) {
                PluginManager manager = new PluginManager();
                for (String name : list) {
                    try {
                        String filename = app.getConfigurationDir().getAbsolutePath() + "/components" + "/" + name;
                        manager.unloadJar(new PluginJar(new URL("file:" + filename)));
                        File jar = new File(filename);
                        jar.delete();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(AsdcComponent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }

    class AsdcMenus extends ArrayList<IMenuItem> {

        public AsdcMenus() {
            add(new AsdcMenuItem());
        }
    }

    class AsdcMenuItem extends AbstractPluginMenuItem {

        public AsdcMenuItem() {
            super();
            setIconPath("/logo_asdc_128.png");
            setThumbnailPath("/logo_asdc_24.png");
        }

        @Override
        public String getTitle() {
            return "ASDC Data";
        }

        @Override
        public String getDescription() {
            return "Get data from ASDC";
        }

        @Override
        public void onClick() {
            if (form == null) {
                form = new PluginMainFormCal((SedlibSedManager) workspace.getSedManager());
                AsdcComponent.this.workspace.addFrame(form);
            }
            GUIUtils.moveToFront(form);

        }
    }
}
