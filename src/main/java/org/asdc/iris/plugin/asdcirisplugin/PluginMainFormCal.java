/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asdc.iris.plugin.asdcirisplugin;

import org.asdc.iris.plugin.CatalogInfo;
import cfa.vo.iris.gui.NarrowOptionPane;
import cfa.vo.iris.sed.ExtSed;
import cfa.vo.iris.sed.SedlibSedManager;

import cfa.vo.iris.utils.HarvardNameResolver;
import cfa.vo.iris.utils.NameResolver.Position;
import cfa.vo.sedlib.Sed;
import cfa.vo.sedlib.Segment;
import cfa.vo.sedlib.common.SedException;
import cfa.vo.sedlib.io.SedFormat;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import jsky.catalog.Catalog;
import org.jdesktop.application.Action;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Task;

/**
 *
 * @author Roberto
 */
public class PluginMainFormCal extends javax.swing.JInternalFrame {

    private HarvardNameResolver resolver = HarvardNameResolver .getInstance();
    private String ra;
    private String dec;
    private String tstart;
    private String tstop;
    private String startH;
    private String startM;
    private String startS;
    private String stopH;
    private String stopM;
    private String stopS;
    private CatalogInfo catInfo;
    public static final String PROP_CINFO = "catInfo";

    /**
     * Get the value of cInfo
     *
     * @return the value of cInfo
     */
    public CatalogInfo getCatInfo() {
        return catInfo;
    }

    /**
     * Set the value of cInfo
     *
     * @param cInfo new value of cInfo
     */
    public void setCatInfo(CatalogInfo cInfo) {
        CatalogInfo oldCInfo = this.catInfo;
        this.catInfo = cInfo;
        firePropertyChange(PROP_CINFO, oldCInfo, cInfo);
        Logger.getLogger(PluginMainFormCal.class.getName()).log(Level.FINE, 
                    "Set catInfo radius = " + cInfo.getRadius());
    }

    private CheckTreeManager checkTreeManager;
    private JTree jTree;
    /**
     * Get the value of jTreeCatalog
     *
     * @return the value of jTreeCatalog
     */
    public static final String PROP_DEC = "dec";
    public static final String PROP_TSTART = "tstart";
    public static final String PROP_TSTOP = "tstop";
    private SedlibSedManager sedManager;
    private CheckTreeManager treeManager;
    private static final String ASDC_SERVLET_URL = "http://tools.asdc.asi.it/SED/";
    private static final String ASDC_SERVLET_DATA = ASDC_SERVLET_URL + "getdatacatalogcfa";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DateFormat formatterPick = new SimpleDateFormat("yyyy-MM-dd");
    final JDialog dlg = new waitFrame(null, "Getting Data...", true);

    public String getStartH() {
        return startH;
    }

    public void setStartH(String startH) {
        this.startH = startH;
    }

    public String getStartM() {
        return startM;
    }

    public void setStartM(String startM) {
        this.startM = startM;
    }

    public String getStartS() {
        return startS;
    }

    public void setStartS(String startS) {
        this.startS = startS;
    }

    public String getStopH() {
        return stopH;
    }

    public void setStopH(String stopH) {
        this.stopH = stopH;
    }

    public String getStopM() {
        return stopM;
    }

    public void setStopM(String stopM) {
        this.stopM = stopM;
    }

    public String getStopS() {
        return stopS;
    }

    public void setStopS(String stopS) {
        this.stopS = stopS;
    }

    

    
    public String getTstart() {
        return tstart;
    }

    public void setTstart(String tstart) {
        String oldTstart = this.tstart;
        this.tstart = tstart;
        firePropertyChange(PROP_TSTART, oldTstart, tstart);
    }

    public String getTstop() {
        return tstop;
    }

    public void setTstop(String tstop) {
        String oldTstop = this.tstop;
        this.tstop = tstop;
        firePropertyChange(PROP_TSTOP, oldTstop, tstop);
    }

    /**
     * Get the value of dec
     *
     * @return the value of dec
     */
    public String getDec() {
        return dec;
    }

    /**
     * Set the value of dec
     *
     * @param dec new value of dec
     */
    public void setDec(String dec) {
        String oldDec = this.dec;
        this.dec = dec;
        firePropertyChange(PROP_DEC, oldDec, dec);
    }

    /**
     * Set the value of dec
     *
     * @param dec new value of dec
     */
    
    
    public static final String PROP_RA = "ra";

    /**
     * Get the value of ra
     *
     * @return the value of ra
     */
    public String getRa() {
        return ra;
    }

    /**
     * Set the value of ra
     *
     * @param ra new value of ra
     */
    public void setRa(String ra) {
        String oldRa = this.ra;
        this.ra = ra;
        firePropertyChange(PROP_RA, oldRa, ra);
    }
    private String targetName;
    public static final String PROP_TARGETNAME = "targetName";

    /**
     * Get the value of targetName
     *
     * @return the value of targetName
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * Set the value of targetName
     *
     * @param targetName new value of targetName
     */
    public void setTargetName(String targetName) {
        String oldTargetName = this.targetName;
        this.targetName = targetName;
        firePropertyChange(PROP_TARGETNAME, oldTargetName, targetName);
    }

    /**
     * Creates new form PluginMainForm
     */
    public PluginMainFormCal(SedlibSedManager sedManager) {
        this.sedManager = sedManager;
        initComponents();
        
        dateStart.setKeepTime(true);
        dateStop.setKeepTime(true);
        
        dateStart.setDateFormat(formatterPick);
        dateStart.setLocale(Locale.getDefault());
        
        dateStop.setDateFormat(formatterPick);
        dateStop.setLocale(Locale.getDefault());

        
        MJDStart.setVisible(false);
        MJDStop.setVisible(false);

        jTree = CatalogTree.getCatalogTree(ASDC_SERVLET_URL);
        
        checkTreeManager = new CheckTreeManager(jTree);
        
    
        jTree.setSelectionRow(0);
    

        jScrollPane2.setViewportView(jTree);
        jScrollPane2.updateUI();
        
        jTree.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                Logger.getLogger(PluginMainFormCal.class.getName()).log(Level.FINE, 
                    "mouseEvent");
                TreePath selPath = jTree.getPathForLocation(e.getX(), e.getY());
                if(selPath!=null) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                    if (node.isLeaf()) {
                       
                       final CatalogInfo cat = (CatalogInfo) node.getUserObject();
                        
                        EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                               setCatInfo(cat);
                            }
                        });
                        
                        //jtextCatInfo.setText(cat.getName());
                    }
                }
            }
        });
        
        //setSize(500, 550);
        jLabel11.setBounds(0, 0, 128, 128);
        labelVersion.setText("Version: " + new AsdcPlugin().getVersion() );
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        MJDStop = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        dateStart = new com.michaelbaranov.microba.calendar.DatePicker();
        MJDStart = new javax.swing.JTextField();
        dateStop = new com.michaelbaranov.microba.calendar.DatePicker();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        dateFormatCombo = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        boxStartH = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        boxStartS = new javax.swing.JTextField();
        boxStopS = new javax.swing.JTextField();
        boxStopH = new javax.swing.JTextField();
        boxStopM = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        boxStartM = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        labelVersion = new javax.swing.JLabel();
        busy = new org.jdesktop.swingx.JXBusyLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabel4 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jtextCatInfo = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextRadius = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        sedCreationType = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("ASDC Catalog Query");
        setMaximumSize(new java.awt.Dimension(500, 800));

        jButton2.setText("Resolve");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Target Name");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${targetName}"), jTextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel3.setText("Dec");

        jLabel2.setText("Ra");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${ra}"), jTextField3, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${dec}"), jTextField2, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jComboBox1.setModel(new DefaultComboBoxModel(new String[]{ "NED", "SIMBAD"}));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel11.setIcon(new ImageIcon(getClass().getResource("/logo_asdc_90.png")));

        MJDStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MJDStopActionPerformed(evt);
            }
        });

        jLabel7.setText("Time");

        jLabel8.setText("Time");

        try {
            dateStart.setDate(null);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        dateStart.setName(getTstart());
        dateStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateStartActionPerformed(evt);
            }
        });

        try {
            dateStop.setDate(null);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        dateStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateStopActionPerformed(evt);
            }
        });

        jLabel6.setText("TStop Date");
        jLabel6.setToolTipText("Date Format: yyyy-MM-dd hh:mm:ss");

        jLabel5.setText("TStart Date");
        jLabel5.setToolTipText("Date Format: yyyy-MM-dd hh:mm:ss");

        dateFormatCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "yyyy-MM-dd hh:mm:ss", "MJD" }));
        dateFormatCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateFormatComboActionPerformed(evt);
            }
        });

        jLabel16.setText("Date Format :");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${startH}"), boxStartH, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel10.setText(":");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${startS}"), boxStartS, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${stopS}"), boxStopS, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${stopH}"), boxStopH, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${stopM}"), boxStopM, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel12.setText(":");

        jLabel13.setText("  HH");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${startM}"), boxStartM, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel9.setText(":");

        jLabel14.setText("MM");

        jLabel15.setText("SS");

        jLabel21.setText(":");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(11, 11, 11))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(MJDStart, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MJDStop, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateStop, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel13)
                                .addComponent(boxStartH, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(boxStopH, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel9)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel15)
                            .addComponent(boxStartM, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(boxStopM, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel12))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel14)
                            .addComponent(boxStartS, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(boxStopS, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateFormatCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel16))
                            .addComponent(dateFormatCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(6, 6, 6))
                            .addComponent(dateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MJDStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel13))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(boxStartH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel9))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(boxStartM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(boxStartS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(boxStopM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel21))
                            .addComponent(boxStopS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel12))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(boxStopH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dateStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(MJDStop)
                            .addComponent(jLabel6)))))
        );

        labelVersion.setText("Version:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 26, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelVersion, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                    .addComponent(busy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(busy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelVersion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel4.setText("Catalogs Available:");

        jLabel18.setText("Catalog Name");

        jtextCatInfo.setEditable(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${catInfo.name}"), jtextCatInfo, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel19.setText("Search Radius");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${catInfo.radius}"), jTextRadius, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        sedCreationType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Append", "Create New" }));
        sedCreationType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sedCreationTypeActionPerformed(evt);
            }
        });

        jLabel17.setText("SED Creation Mode:");

        jLabel20.setText("arcmin");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(421, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sedCreationType, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton1)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jtextCatInfo)
                                            .addComponent(jTextRadius, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sedCreationType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(58, 58, 58)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtextCatInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    private String getListCatalogId(Object obj) {

        String list = "";
        // to get the paths that were checked
        //TreePath checkedPaths[] =  jTree.getSelectionPaths();

        // for (int i=0; i< checkedPaths.length;i++) {
        //    Object obj = checkedPaths[i].getLastPathComponent();
        //System.out.println("conteggio interno obj y" + obj);

        //if (no.isLeaf() && no.getUserObject() instanceof CatalogInfo) {
        
        DefaultMutableTreeNode d = ((DefaultMutableTreeNode) obj);
        
        if (obj instanceof DefaultMutableTreeNode) {
            
            for (int c = 0; c < d.getChildCount(); c++) {
                list += getListCatalogId(d.getChildAt(c));
            }
        }
        if (d.getUserObject() instanceof CatalogInfo) {
            
            
            CatalogInfo c = (CatalogInfo) d.getUserObject();
            list+= "&catalogId=" + c.getCatalogId() + 
                    "|" + c.getRadius();
        }

        //   }
        return list;

    }

    
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
       
        
        final Thread dialog = new Thread(new Runnable() {
            
            public void run() {
 
                JFrame parentFrame = new JFrame();

                dlg.setLocationRelativeTo(parentFrame);
                dlg.setVisible(true);

            }
        });
        
     
        Thread process = new Thread(new Runnable() {

            public void run() {
                
                try {
                    JFrame parentFrame = new JFrame();
                    if (ra == null || ra.equals("")) {

                        NarrowOptionPane.showMessageDialog(parentFrame, "Insert a value for RA", "Warning", NarrowOptionPane.WARNING_MESSAGE);
                        return;
                    } else {

                        try {
                            Double.parseDouble(ra);
                        } catch (NumberFormatException e) {

                            NarrowOptionPane.showMessageDialog(parentFrame, "Insert a valid value for RA", "Warning", NarrowOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }

                    if (dec == null || dec.equals("")) {

                        NarrowOptionPane.showMessageDialog(parentFrame, "Insert a value for DEC", "Warning", NarrowOptionPane.WARNING_MESSAGE);
                        return;
                    } else {

                        try {
                            Double.parseDouble(dec);
                        } catch (NumberFormatException e) {

                            NarrowOptionPane.showMessageDialog(parentFrame, "Insert a valid value for DEC", "Warning", NarrowOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }


                    String listCatalogId = "";
                    TreePath checkedPaths[] = checkTreeManager.getSelectionModel().getSelectionPaths();

                    if (checkedPaths == null || checkedPaths.length <= 0) {

                        NarrowOptionPane.showMessageDialog(parentFrame, "No Catalogs selected", "Warning", NarrowOptionPane.WARNING_MESSAGE);
                        return;
                    }


                    for (int ca = 0; ca < checkedPaths.length; ca++) {
                        listCatalogId += getListCatalogId(checkedPaths[ca].getLastPathComponent());
                    }
                    Logger.getLogger(PluginMainFormCal.class.getName()).log(Level.FINE,
                            "Lista catalog id " + listCatalogId);

                    Logger.getLogger(PluginMainFormCal.class.getName()).log(Level.FINE,
                            "datestart= " + dateStart.getDate());

                    if (dateFormatCombo.getSelectedIndex() == 0) {//ISO 8601

                        if (dateStart.getDate() != null) {

                            try {
                                //Date date = (Date) formatter.parse(tstart);
                                Date date = (Date) dateStart.getDate();

                                String stringDate = formatterPick.format(date);
                                tstart = stringDate + " " + startH + ":" + startM + ":" + startS;

                                Date dateTest = (Date) formatter.parse(tstart); //test per formato

                                Logger.getLogger(PluginMainFormCal.class.getName()).log(Level.FINE,
                                        "#########" + stringDate);

                            } catch (Exception e) {
                                dlg.setVisible(false);
                                NarrowOptionPane.showMessageDialog(parentFrame, "Start date format Invalid", "Warning", NarrowOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            tstart = tstart.replace(" ", "%20");
                        } else {
                            tstart = "";
                        }


                        if (dateStop.getDate() != null) {

                            try {
                                Date date = (Date) dateStop.getDate();

                                String stringDate = formatterPick.format(date);
                                tstop = stringDate + " " + stopH + ":" + stopM + ":" + stopS;

                                Date dateTest = (Date) formatter.parse(tstop); //test per formato

                                Logger.getLogger(PluginMainFormCal.class.getName()).log(Level.FINE,
                                        "#########" + stringDate);
                            } catch (Exception e) {
                                dlg.setVisible(false);
                                NarrowOptionPane.showMessageDialog(parentFrame, "Stop date format Invalid", "Warning", NarrowOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            tstop = tstop.replace(" ", "%20");
                        } else {
                            tstop = "";
                        }
                    }

                    if (dateFormatCombo.getSelectedIndex() == 1) {//MJD

                        if (!MJDStart.getText().equals("")) {

                            try {

                                Double doubleStart = Double.parseDouble(MJDStart.getText());
                                tstart = MJDStart.getText();
                            } catch (NumberFormatException e) {
                                dlg.setVisible(false);
                                NarrowOptionPane.showMessageDialog(parentFrame, "Start date format Invalid", "Warning", NarrowOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        } else {

                            tstart = "";
                        }

                        if (!MJDStop.getText().equals("")) {

                            try {

                                Double doubleStop = Double.parseDouble(MJDStop.getText());
                                tstop = MJDStop.getText();
                            } catch (NumberFormatException e) {
                                dlg.setVisible(false);
                                NarrowOptionPane.showMessageDialog(parentFrame, "Stop date format Invalid", "Warning", NarrowOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        } else {

                            tstop = "";
                        }
                    }

                    //String parameters = "ra=" + this.ra + "&dec=" + dec + "&catalogId=114&catalogId=115&catalogId=116&catalogId=65&catalogId=66&catalogId=4&catalogId=7";
                    String parameters = "ra=" + ra + "&dec=" + dec
                            + "&tstart=" + tstart + "&tstop=" + tstop + listCatalogId;

                    Logger.getLogger(PluginMainFormCal.class.getName()).log(Level.FINE,
                            "parameters: " + parameters);

                    //String parameters = "ra=" + this.ra + "&dec=" + dec;


                    Sed s;
                    URL url = new URL(ASDC_SERVLET_DATA + "?" + parameters);
                    try {


                        dialog.start();
                        s = Sed.read(new GZIPInputStream(url.openStream()), SedFormat.VOT);

                    } catch (SedException e) {

                        dlg.setVisible(false);
                        NarrowOptionPane.showMessageDialog(parentFrame, e.getMessage(), "Error trying to get data from ASDC", NarrowOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    //ExtSed extSed = sedManager.newSed("asdc");
                    ExtSed extSed = sedManager.getSelected();

                    int idx = sedCreationType.getSelectedIndex();

                    if (extSed == null || idx == 1) {
                        extSed = sedManager.newSed("asdc");
                    }

                    List<Segment> listSeg = new ArrayList();

                    for (int i = 0; i < s.getNumberOfSegments(); i++) {
                        listSeg.add(s.getSegment(i));

                    }
                    extSed.addSegment(listSeg);

                } catch (Exception e) {

                    dlg.setVisible(false);
                    
                    Logger.getLogger(PluginMainFormCal.class.getName()).log(Level.SEVERE,
                            e.getMessage());
                }
                
             dlg.setVisible(false);    
            
            }
        });
        
        process.start();
        process = null;
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void dateStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateStartActionPerformed
        
        Logger.getLogger(PluginMainFormCal.class.getName()).log(Level.FINE, 
                    "dateStartActionPerformed");
        
        if (dateStart.getDate() == null) {
        
            boxStartH.setText("");
            boxStartM.setText("");
            boxStartS.setText("");
        }
        else {
            boxStartH.setText("00");
            boxStartM.setText("00");
            boxStartS.setText("00");
        }
        
        
    }//GEN-LAST:event_dateStartActionPerformed

    private void dateStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateStopActionPerformed

        
        if (dateStop.getDate() == null) {
        
            boxStopH.setText("");
            boxStopM.setText("");
            boxStopS.setText("");
        }
        else {
            boxStopH.setText("00");
            boxStopM.setText("00");
            boxStopS.setText("00");
        }
    }//GEN-LAST:event_dateStopActionPerformed

    private void dateFormatComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateFormatComboActionPerformed
        
        int idx = dateFormatCombo.getSelectedIndex();
        
        if (idx == 0) {
        
            boxStartH.setEnabled(true);
            boxStartM.setEnabled(true);
            boxStartS.setEnabled(true);
            
            boxStopH.setEnabled(true);
            boxStopM.setEnabled(true);
            boxStopS.setEnabled(true);
            
            dateStop.setVisible(true);
            dateStart.setVisible(true);
            
            MJDStart.setVisible(false);
            MJDStop.setVisible(false);
            
            jLabel5.setToolTipText("Date Format: yyyy-MM-dd hh:mm:ss");
            jLabel6.setToolTipText("Date Format: yyyy-MM-dd hh:mm:ss");
        }
        
        if (idx == 1) {
        
            boxStartH.setEnabled(false);
            boxStartM.setEnabled(false);
            boxStartS.setEnabled(false);
            
            boxStopH.setEnabled(false);
            boxStopM.setEnabled(false);
            boxStopS.setEnabled(false);
            
            
            MJDStart.setSize(90, 10);
            MJDStop.setSize(90 , 10);
            
            dateStop.setVisible(false);
            dateStart.setVisible(false);
            
            MJDStart.setVisible(true);
            MJDStop.setVisible(true);
            
            jLabel5.setToolTipText("MJD ddddd.ddd");
            jLabel6.setToolTipText("MJD ddddd.ddd");
            
            
        }
    }//GEN-LAST:event_dateFormatComboActionPerformed

    private void MJDStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MJDStopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MJDStopActionPerformed

    private void sedCreationTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sedCreationTypeActionPerformed
        
        
    }//GEN-LAST:event_sedCreationTypeActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    Task t = resolve();
    t.execute();
}//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField MJDStart;
    private javax.swing.JTextField MJDStop;
    private javax.swing.JTextField boxStartH;
    private javax.swing.JTextField boxStartM;
    private javax.swing.JTextField boxStartS;
    private javax.swing.JTextField boxStopH;
    private javax.swing.JTextField boxStopM;
    private javax.swing.JTextField boxStopS;
    private org.jdesktop.swingx.JXBusyLabel busy;
    private javax.swing.JComboBox dateFormatCombo;
    private com.michaelbaranov.microba.calendar.DatePicker dateStart;
    private com.michaelbaranov.microba.calendar.DatePicker dateStop;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextRadius;
    private javax.swing.JTextField jtextCatInfo;
    private javax.swing.JLabel labelVersion;
    private javax.swing.JComboBox sedCreationType;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    public Task resolve() {
        return new ResolveTask(org.jdesktop.application.Application.getInstance());
    }

    private class ResolveTask extends org.jdesktop.application.Task<Object, Void> {

        private String cat;

        ResolveTask(org.jdesktop.application.Application app) {
            super(app);
            cat = (String) jComboBox1.getSelectedItem();
        }

        protected Object doInBackground() {
            busy.setBusy(true);
            Object pos = null;
            try {
                pos = resolver.resolve(cat, targetName);
            } catch (RuntimeException ex) {
                return ex.getMessage();
            } catch (IOException ex) {
                return ex.getMessage();
            }
            return pos;
        }

        protected void succeeded(Object result) {
            if (result instanceof String) {
                NarrowOptionPane.showMessageDialog(null, result, "Error trying to resolve name", NarrowOptionPane.ERROR_MESSAGE);
            } else {
                Position pos = (Position) result;
                setRa(pos.getRa().toString());
                setDec(pos.getDec().toString());
            }
            busy.setBusy(false);
        }
    }
}
