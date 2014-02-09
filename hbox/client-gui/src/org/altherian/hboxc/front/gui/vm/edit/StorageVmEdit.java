/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * 
 * http://hyperbox.altherian.org
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui.vm.edit;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.input.Action;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.MediumInput;
import org.altherian.hbox.comm.input.StorageControllerInput;
import org.altherian.hbox.comm.input.StorageControllerTypeInput;
import org.altherian.hbox.comm.input.StorageDeviceAttachmentInput;
import org.altherian.hbox.comm.io.factory.MediumIoFactory;
import org.altherian.hbox.comm.io.factory.StorageControllerIoFactory;
import org.altherian.hbox.comm.io.factory.StorageDeviceAttachmentIoFactory;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerTypeOutput;
import org.altherian.hbox.comm.output.storage.StorageDeviceAttachmentOutput;
import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hboxc.HyperboxClient;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.storage.HarddiskCreateDialog;
import org.altherian.hboxc.front.gui.storage.MediumBrowser;
import org.altherian.hboxc.front.gui.storage.StorageControllerViewer;
import org.altherian.hboxc.front.gui.storage.StorageDeviceAttachmentViewer;
import org.altherian.tool.logging.Logger;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class StorageVmEdit {
   
   private Map<StorageControllerInput, DefaultMutableTreeNode> scInToNode;
   private Map<StorageDeviceAttachmentInput, DefaultMutableTreeNode> matInToNode;
   
   private String srvId;
   private MachineInput mIn;
   
   private DefaultMutableTreeNode topNode;
   private DefaultTreeModel treeModel;
   private JTree tree;
   private JScrollPane treeView;
   
   private JPanel actionsPanel;
   private JButton addScButton;
   private JButton removeScButton;
   private JButton addAttachmentButton;
   private JButton removeAttachmentButton;
   
   private JMenuItem addIdeMenuItem;
   private JMenuItem addSataMenuItem;
   private JMenuItem addScsiMenuItem;
   private JMenuItem addSasMenuItem;
   private JMenuItem addFloppyMenuItem;
   
   private JMenuItem addDvdDriveMenuItem;
   private JMenuItem addDvdImgMenuItem;
   
   private JMenuItem addFloppyDriveMenuItem;
   private JMenuItem addFloppyImgMenuItem;
   
   private JMenuItem addDiskNewMenuItem;
   private JMenuItem addDiskExistingMenuItem;
   
   private JMenuItem removeMediumMenuItem;
   private JMenuItem removeDeviceMenuItem;
   
   private CardLayout viewerLayout;
   private JPanel viewer;
   
   private JPanel emptyViewer;
   private StorageControllerViewer scViewer;
   private StorageDeviceAttachmentViewer sdaViewer;
   
   private JPanel panel;
   
   public StorageVmEdit() {
      scInToNode = new HashMap<StorageControllerInput, DefaultMutableTreeNode>();
      matInToNode = new HashMap<StorageDeviceAttachmentInput, DefaultMutableTreeNode>();
      
      emptyViewer = new JPanel();
      emptyViewer.add(new JLabel("Select an item on the left"));
      
      scViewer = new StorageControllerViewer();
      sdaViewer = new StorageDeviceAttachmentViewer();
      
      topNode = new DefaultMutableTreeNode("Controllers");
      treeModel = new DefaultTreeModel(topNode);
      tree = new JTree(treeModel);
      tree.setRootVisible(false);
      tree.setShowsRootHandles(true);
      tree.addTreeSelectionListener(new TreeListener());
      tree.setCellRenderer(new StorageTreeCellRenderer());
      
      treeView = new JScrollPane(tree);
      
      viewerLayout = new CardLayout();
      viewer = new JPanel(viewerLayout);
      viewer.add(emptyViewer, "");
      viewer.add(scViewer.getPanel(), EntityTypes.StorageController.getId());
      viewer.add(sdaViewer.getPanel(), EntityTypes.StorageAttachment.getId());
      
      initMedMenu();
      initScMenu();
      initButtons();
      
      panel = new JPanel(new MigLayout());
      panel.add(treeView, "grow, pushy");
      panel.add(new JSeparator(SwingConstants.VERTICAL), "growy, pushy, spany 2");
      panel.add(viewer, "grow, push, spany 2, wrap");
      panel.add(actionsPanel, "growx");
   }
   
   private void initMedMenu() {
      addDvdDriveMenuItem = new JMenuItem("Empty CD/DVD Drive");
      addDvdDriveMenuItem.setIcon(IconBuilder.getDeviceType(EntityTypes.DvdDrive));
      addDvdDriveMenuItem.addActionListener(new AddDeviceListener());
      
      addDvdImgMenuItem = new JMenuItem("Existing CD/DVD Image");
      addDvdImgMenuItem.setIcon(IconBuilder.getDeviceType(EntityTypes.DvdDrive));
      addDvdImgMenuItem.addActionListener(new AddDeviceListener());
      
      addFloppyDriveMenuItem = new JMenuItem("Empty Floppy Drive");
      addFloppyDriveMenuItem.setIcon(IconBuilder.getDeviceType(EntityTypes.FloppyDrive));
      addFloppyDriveMenuItem.addActionListener(new AddDeviceListener());
      
      addFloppyImgMenuItem = new JMenuItem("Existing Floppy Image");
      addFloppyImgMenuItem.setIcon(IconBuilder.getDeviceType(EntityTypes.FloppyDrive));
      addFloppyImgMenuItem.addActionListener(new AddDeviceListener());
      
      addDiskNewMenuItem = new JMenuItem("New Disk Image");
      addDiskNewMenuItem.setIcon(IconBuilder.getTask(HypervisorTasks.MediumCreate));
      addDiskNewMenuItem.addActionListener(new AddDeviceListener());
      
      addDiskExistingMenuItem = new JMenuItem("Existing Disk Image");
      addDiskExistingMenuItem.setIcon(IconBuilder.getTask(HypervisorTasks.MediumRegister));
      addDiskExistingMenuItem.addActionListener(new AddDeviceListener());
      
      removeMediumMenuItem = new JMenuItem("Remove Medium");
      removeMediumMenuItem.addActionListener(new RemoveMediumListener());
      
      removeDeviceMenuItem = new JMenuItem("Remove Device");
      removeDeviceMenuItem.addActionListener(new RemoveDeviceListener());
   }
   
   private void initScMenu() {
      addIdeMenuItem = new JMenuItem(StorageControllerType.IDE.getId());
      addIdeMenuItem.setIcon(IconBuilder.getStorageControllerType(StorageControllerType.IDE.getId()));
      addIdeMenuItem.addActionListener(new AddScListener());
      
      addSataMenuItem = new JMenuItem(StorageControllerType.SATA.getId());
      addSataMenuItem.setIcon(IconBuilder.getStorageControllerType(StorageControllerType.SATA.getId()));
      addSataMenuItem.addActionListener(new AddScListener());
      
      addScsiMenuItem = new JMenuItem(StorageControllerType.SCSI.getId());
      addScsiMenuItem.setIcon(IconBuilder.getStorageControllerType(StorageControllerType.SCSI.getId()));
      addScsiMenuItem.addActionListener(new AddScListener());
      
      addSasMenuItem = new JMenuItem(StorageControllerType.SAS.getId());
      addSasMenuItem.setIcon(IconBuilder.getStorageControllerType(StorageControllerType.SAS.getId()));
      addSasMenuItem.addActionListener(new AddScListener());
      
      addFloppyMenuItem = new JMenuItem(StorageControllerType.Floppy.getId());
      addFloppyMenuItem.setIcon(IconBuilder.getStorageControllerType(StorageControllerType.Floppy.getId()));
      addFloppyMenuItem.addActionListener(new AddScListener());
   }
   
   private void initButtons() {
      addScButton = new JButton(IconBuilder.getTask(HypervisorTasks.StorageControllerAdd));
      addScButton.addActionListener(new AddScListener());
      
      removeScButton = new JButton(IconBuilder.getTask(HypervisorTasks.StorageControllerRemove));
      removeScButton.setEnabled(false);
      removeScButton.addActionListener(new RemoveNodeListener());
      
      addAttachmentButton = new JButton(IconBuilder.getTask(HypervisorTasks.StorageControllerMediumAttachmentAdd));
      addAttachmentButton.setToolTipText("Add/Modify attachment");
      addAttachmentButton.setEnabled(false);
      addAttachmentButton.addActionListener(new AddAttachmentListener());
      
      removeAttachmentButton = new JButton(IconBuilder.getTask(HypervisorTasks.StorageControllerMediumAttachmentRemove));
      removeAttachmentButton.setEnabled(false);
      removeAttachmentButton.addActionListener(new RemoveNodeListener());
      
      actionsPanel = new JPanel(new MigLayout("ins 0, fill"));
      actionsPanel.add(addScButton);
      actionsPanel.add(removeScButton);
      actionsPanel.add(addAttachmentButton);
      actionsPanel.add(removeAttachmentButton);
   }
   
   public Component getComp() {
      return panel;
   }
   
   private void add(StorageControllerInput scIn) {
      mIn.addStorageController(scIn);
      
      DefaultMutableTreeNode node = new DefaultMutableTreeNode(scIn);
      treeModel.insertNodeInto(node, topNode, topNode.getChildCount());
      tree.scrollPathToVisible(new TreePath(node.getPath()));
      scInToNode.put(scIn, node);
   }
   
   private void add(StorageControllerOutput scOut) {
      StorageControllerInput scIn = StorageControllerIoFactory.get(scOut);
      scIn.setMachineUuid(mIn.getUuid());
      add(scIn);
      
      List<StorageDeviceAttachmentOutput> matOutList = Gui.getServer(srvId).listAttachments(scIn);
      for (StorageDeviceAttachmentOutput matOut : matOutList) {
         add(scIn, matOut);
      }
   }
   
   private void add(StorageControllerInput scIn, StorageDeviceAttachmentInput matIn) {
      scIn.addMediumAttachment(matIn);
      
      DefaultMutableTreeNode node = new DefaultMutableTreeNode(matIn);
      treeModel.insertNodeInto(node, scInToNode.get(scIn), scInToNode.get(scIn).getChildCount());
      tree.scrollPathToVisible(new TreePath(node.getPath()));
      matInToNode.put(matIn, node);
   }
   
   private void add(StorageControllerInput scIn, StorageDeviceAttachmentOutput matOut) {
      StorageDeviceAttachmentInput matIn = StorageDeviceAttachmentIoFactory.get(matOut);
      matIn.setAction(Action.Modify);
      add(scIn, matIn);
      
      if (matOut.hasMediumInserted()) {
         MediumOutput medOut = Gui.getServer(srvId).getMedium(new MediumInput(matOut.getMediumUuid()));
         MediumInput medIn = MediumIoFactory.get(medOut);
         matIn.attachMedium(medIn);
      }
   }
   
   public void update(MachineOutput mOut, MachineInput mIn) {
      srvId = mOut.getServerId();
      this.mIn = mIn;
      
      topNode.removeAllChildren();
      treeModel.reload();
      for (StorageControllerOutput scOut : mOut.listStorageController()) {
         add(scOut);
      }
   }
   
   public void save() {
      // TODO
   }
   
   // TODO should throw exception if no slot is found
   // TODO move into StorageControllerInput object
   private StorageDeviceAttachmentInput attachDeviceNextFreeSlot(StorageControllerInput scIn, String deviceType) {
      StorageControllerTypeOutput sctOut = Gui.getServer(srvId).getStorageControllerType(
            new StorageControllerTypeInput(scIn.getType()));
      boolean notFound = true;
      for (long i = sctOut.getMinPort() - 1; (i < sctOut.getMaxPort()) && notFound; i++) {
         for (long j = 0; (j < sctOut.getMaxDevicePerPort()) && notFound; j++) {
            StorageDeviceAttachmentInput sdaIn = new StorageDeviceAttachmentInput(scIn.getName(), i, j, deviceType);
            if (scIn.addMediumAttachment(sdaIn)) {
               notFound = false;
               add(scIn, sdaIn);
               return sdaIn;
            }
         }
      }
      HyperboxClient.getView().postError("No free slot", new Exception());
      return null;
   }
   
   private class TreeListener implements TreeSelectionListener {
      
      @Override
      public void valueChanged(TreeSelectionEvent ev) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
         if (node != null) {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
            if (node.getUserObject() instanceof StorageControllerInput) {
               StorageControllerInput scIn = (StorageControllerInput) node.getUserObject();
               addScButton.setEnabled(true);
               removeScButton.setEnabled(true);
               addAttachmentButton.setEnabled(true);
               removeAttachmentButton.setEnabled(false);
               scViewer.display(srvId, scIn);
               viewerLayout.show(viewer, EntityTypes.StorageController.getId());
            }
            if (node.getUserObject() instanceof StorageDeviceAttachmentInput) {
               StorageDeviceAttachmentInput matIn = (StorageDeviceAttachmentInput) node.getUserObject();
               Logger.debug(matIn);
               StorageControllerInput scIn = (StorageControllerInput) parentNode.getUserObject();
               addScButton.setEnabled(true);
               removeScButton.setEnabled(false);
               addAttachmentButton.setEnabled(true);
               removeAttachmentButton.setEnabled(true);
               sdaViewer.show(srvId, scIn.getType(), matIn);
               viewerLayout.show(viewer, EntityTypes.StorageAttachment.getId());
            }
         } else {
            viewerLayout.show(viewer, "");
         }
      }
      
   }
   
   @SuppressWarnings("serial")
   private class StorageTreeCellRenderer extends DefaultTreeCellRenderer {
      
      @Override
      public Component getTreeCellRendererComponent(JTree rawTree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
         super.getTreeCellRendererComponent(rawTree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
         if (node != topNode) {
            if (node.getUserObject() instanceof StorageControllerInput) {
               StorageControllerInput obj = (StorageControllerInput) node.getUserObject();
               setIcon(IconBuilder.getStorageControllerType(obj.getType()));
            }
            if (node.getUserObject() instanceof StorageDeviceAttachmentInput) {
               StorageDeviceAttachmentInput sdaIn = (StorageDeviceAttachmentInput) node.getUserObject();
               String obj = sdaIn.getDeviceType();
               if (obj.contentEquals(EntityTypes.HardDisk.getId())) {
                  obj = EntityTypes.DiskDrive.getId();
               }
               if (obj.contentEquals(EntityTypes.DVD.getId())) {
                  obj = EntityTypes.DvdDrive.getId();
               }
               if (obj.contentEquals(EntityTypes.Floppy.getId())) {
                  obj = EntityTypes.FloppyDrive.getId();
               }
               setIcon(IconBuilder.getDeviceType(obj));
               
               if (sdaIn.hasMedium()) {
                  setText(sdaIn.getMedium().toString());
               } else {
                  setText("No medium");
               }
            }
         }
         
         return this;
      }
      
   }
   
   private class AddScListener implements ActionListener {
      
      @Override
      public void actionPerformed(ActionEvent e) {
         if (e.getSource().equals(addScButton)) {
            JPopupMenu menu = new JPopupMenu();
            menu.add(addIdeMenuItem);
            menu.add(addSataMenuItem);
            menu.add(addScsiMenuItem);
            menu.add(addSasMenuItem);
            menu.add(addFloppyMenuItem);
            menu.show(addScButton, 0, addScButton.getHeight());
         }
         else if (e.getSource().equals(addIdeMenuItem)) {
            Logger.debug("Adding IDE controller");
            StorageControllerInput scIn = new StorageControllerInput(mIn.getUuid(), StorageControllerType.IDE.getId(), StorageControllerType.IDE.getId());
            add(scIn);
         }
         else if (e.getSource().equals(addSataMenuItem)) {
            Logger.debug("Adding SATA controller");
            StorageControllerInput scIn = new StorageControllerInput(mIn.getUuid(), StorageControllerType.SATA.getId(), StorageControllerType.SATA.getId());
            add(scIn);
         }
         else if (e.getSource().equals(addScsiMenuItem)) {
            Logger.debug("Adding SCSI controller");
            StorageControllerInput scIn = new StorageControllerInput(mIn.getUuid(), StorageControllerType.SCSI.getId(), StorageControllerType.SCSI.getId());
            add(scIn);
         }
         else if (e.getSource().equals(addSasMenuItem)) {
            Logger.debug("Adding SAS controller");
            StorageControllerInput scIn = new StorageControllerInput(mIn.getUuid(), StorageControllerType.SAS.getId(), StorageControllerType.SAS.getId());
            add(scIn);
         }
         else if (e.getSource().equals(addFloppyMenuItem)) {
            Logger.debug("Adding Floppy controller");
            StorageControllerInput scIn = new StorageControllerInput(mIn.getUuid(), StorageControllerType.Floppy.getId(), StorageControllerType.Floppy.getId());
            add(scIn);
         }
         else {
            Logger.error("Unknown source @ AddScListener");
         }
      }
      
   }
   
   private class RemoveNodeListener implements ActionListener {
      
      @Override
      public void actionPerformed(ActionEvent ae) {
         DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
         if (dmtn != null) {
            if ((ae.getSource() == removeScButton) && (dmtn.getUserObject() instanceof StorageControllerInput)) {
               StorageControllerInput scIn = (StorageControllerInput) dmtn.getUserObject();
               mIn.removeStorageController(scIn.getName());
               treeModel.removeNodeFromParent(scInToNode.get(scIn));
               scInToNode.remove(scIn);
            }
            else if (dmtn.getUserObject() instanceof StorageDeviceAttachmentInput) {
               StorageDeviceAttachmentInput matIn = (StorageDeviceAttachmentInput) dmtn.getUserObject();
               
               if (!matIn.getDeviceType().contentEquals(EntityTypes.HardDisk.getId()) && matIn.hasMedium()) {
                  JPopupMenu menu = new JPopupMenu();
                  menu.add(removeMediumMenuItem);
                  menu.add(removeDeviceMenuItem);
                  menu.show(removeAttachmentButton, 0, removeAttachmentButton.getHeight());
               } else {
                  removeDeviceMenuItem.doClick();
               }
            }
            else {
               Logger.error("Unknown user object @ RemoveNodeListener");
            }
         }
      }
      
   }
   
   private class RemoveMediumListener implements ActionListener {
      
      @Override
      public void actionPerformed(ActionEvent ae) {
         DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
         if (dmtn != null) {
            StorageDeviceAttachmentInput matIn = (StorageDeviceAttachmentInput) dmtn.getUserObject();
            matIn.detachMedium();
            treeModel.reload(dmtn);
            sdaViewer.refresh();
         }
      }
      
   }
   
   private class RemoveDeviceListener implements ActionListener {
      
      @Override
      public void actionPerformed(ActionEvent ae) {
         DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
         if (dmtn != null) {
            StorageDeviceAttachmentInput matIn = (StorageDeviceAttachmentInput) dmtn.getUserObject();
            StorageControllerInput scIn = (StorageControllerInput) ((DefaultMutableTreeNode) dmtn.getParent()).getUserObject();
            scIn.removeAttachment(matIn);
            treeModel.removeNodeFromParent(dmtn);
         }
      }
      
   }
   
   private StorageDeviceAttachmentInput addEmpty(StorageControllerInput scIn, String devType) {
      return attachDeviceNextFreeSlot(scIn, devType);
   }
   
   private StorageDeviceAttachmentInput addMedium(StorageControllerInput scIn, String devType) {
      MediumOutput medOut = MediumBrowser.browse(new ServerOutput(srvId), devType);
      if (medOut == null) {
         return null;
      }
      MediumInput medIn = MediumIoFactory.get(medOut);
      return add(scIn, devType, medIn);
   }
   
   private StorageDeviceAttachmentInput add(StorageControllerInput scIn, String devType, MediumInput medIn) {
      StorageDeviceAttachmentInput sdaIn = attachDeviceNextFreeSlot(scIn, devType);
      if (sdaIn == null) {
         return null;
      }
      add(sdaIn, medIn);
      return sdaIn;
   }
   
   private void add(StorageDeviceAttachmentInput sdaIn, MediumInput medIn) {
      sdaIn.attachMedium(medIn);
      StorageControllerInput scIn = (StorageControllerInput) ((DefaultMutableTreeNode) matInToNode.get(sdaIn).getParent()).getUserObject();
      sdaViewer.show(srvId, scIn.getType(), sdaIn);
   }
   
   private void add(StorageDeviceAttachmentInput sdaIn) {
      MediumOutput medOut = MediumBrowser.browse(new ServerOutput(srvId), sdaIn.getDeviceType());
      if (medOut != null) {
         MediumInput medIn = MediumIoFactory.get(medOut);
         add(sdaIn, medIn);
      }
   }
   
   private class AddAttachmentListener implements ActionListener {
      
      @Override
      public void actionPerformed(ActionEvent ae) {
         Logger.track();
         
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
         if (node != null) {
            if (node.getUserObject() instanceof StorageControllerInput) {
               StorageControllerInput scIn = (StorageControllerInput) node.getUserObject();
               JPopupMenu menu = new JPopupMenu();
               if (scIn.getType().contentEquals(StorageControllerType.Floppy.getId())) {
                  menu.add(addFloppyDriveMenuItem);
                  menu.add(addFloppyImgMenuItem);
               } else {
                  menu.add(addDvdDriveMenuItem);
                  menu.add(addDvdImgMenuItem);
                  menu.add(addDiskNewMenuItem);
                  menu.add(addDiskExistingMenuItem);
               }
               menu.show(addAttachmentButton, 0, addAttachmentButton.getHeight());
            }
            else if (node.getUserObject() instanceof StorageDeviceAttachmentInput) {
               StorageDeviceAttachmentInput sdaIn = (StorageDeviceAttachmentInput) node.getUserObject();
               if (sdaIn.getDeviceType().contentEquals(EntityTypes.HardDisk.getId())) {
                  JPopupMenu menu = new JPopupMenu();
                  menu.add(addDiskNewMenuItem);
                  menu.add(addDiskExistingMenuItem);
                  menu.show(addAttachmentButton, 0, addAttachmentButton.getHeight());
               } else {
                  add((StorageDeviceAttachmentInput) node.getUserObject());
                  treeModel.reload(node);
               }
            }
            else {
               Logger.error("Unknown user object @ AddAttachmentListener");
            }
            treeModel.reload(node);
         }
      }
   }
   
   private class AddDeviceListener implements ActionListener {
      
      @Override
      public void actionPerformed(ActionEvent ae) {
         
         Logger.track();
         
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
         if (node != null) {
            if (node.getUserObject() instanceof StorageControllerInput) {
               StorageControllerInput scIn = (StorageControllerInput) node.getUserObject();
               if (ae.getSource().equals(addDvdDriveMenuItem)) {
                  addEmpty(scIn, EntityTypes.DVD.getId());
               }
               if (ae.getSource().equals(addDvdImgMenuItem)) {
                  addMedium(scIn, EntityTypes.DVD.getId());
               }
               
               if (ae.getSource().equals(addDiskNewMenuItem)) {
                  MediumInput medIn = HarddiskCreateDialog.show(new ServerOutput(srvId));
                  if (medIn != null) {
                     add(scIn, EntityTypes.HardDisk.getId(), medIn);
                  }
               }
               if (ae.getSource().equals(addDiskExistingMenuItem)) {
                  addMedium(scIn, EntityTypes.HardDisk.getId());
               }
               
               if (ae.getSource().equals(addFloppyDriveMenuItem)) {
                  addEmpty(scIn, EntityTypes.Floppy.getId());
               }
               if (ae.getSource().equals(addFloppyImgMenuItem)) {
                  addMedium(scIn, EntityTypes.Floppy.getId());
               }
            }
            else if (node.getUserObject() instanceof StorageDeviceAttachmentInput) {
               StorageDeviceAttachmentInput sdaIn = (StorageDeviceAttachmentInput) node.getUserObject();
               if (ae.getSource().equals(addDiskNewMenuItem)) {
                  MediumInput medIn = HarddiskCreateDialog.show(new ServerOutput(srvId));
                  add(sdaIn, medIn);
               }
               if (ae.getSource().equals(addDiskExistingMenuItem)) {
                  add(sdaIn);
               }
            }
            else {
               Logger.error("Unknown user object @ AddDeviceListener");
            }
            treeModel.reload(node);
         }
      }
      
   }
   
}
