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

package org.altherian.hboxc.front.gui.vm.view;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.MediumInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.output.event.storage.StorageControllerAttachmentDataModifiedEventOutput;
import org.altherian.hbox.comm.output.hypervisor.GuestNetworkInterfaceOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.network.NetworkInterfaceOutput;
import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerOutput;
import org.altherian.hbox.comm.output.storage.StorageDeviceAttachmentOutput;
import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.event.machine.MachineStateChangedEvent;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.action.storage.StorageDeviceAttachmentMediumEditAction;
import org.altherian.hboxc.front.gui.network.NetworkInterfaceSummary;
import org.altherian.helper.swing.BorderUtils;
import org.altherian.helper.swing.JTextFieldUtils;
import org.altherian.tool.StringTools;
import org.altherian.tool.logging.Logger;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public final class VmSummaryView {
   
   private MachineOutput mOut;
   private Map<String, StorageControllerOutput> controllers;
   
   private JPanel panel;
   
   private JPanel generalPanel;
   private JLabel nameLabel;
   private JTextField nameField;
   private JLabel uuidLabel;
   private JTextField uuidField;
   private JLabel stateLabel;
   private JTextField stateField;
   private JLabel osTypeLabel;
   private JTextField osTypeField;
   
   private JPanel systemPanel;
   private JLabel cpuCountLabel;
   private JTextField cpuCountValue;
   private JLabel memoryLabel;
   private JTextField memoryValue;
   private JLabel accelLabel;
   private JTextField accelValue;
   
   private JPanel displayPanel;
   private JLabel vramLabel;
   private JTextField vramValue;
   private JLabel consoleModuleLabel;
   private JTextField consoleModuleValue;
   private JLabel consoleAddressLabel;
   private JTextField consoleAddressValue;
   private JButton consoleConnectButton;
   
   private JPanel storagePanel;
   
   private JPanel audioPanel;
   private JLabel hostDriverLabel;
   private JTextField hostDriverValue;
   private JLabel audioControllerLabel;
   private JTextField audioControllerValue;
   
   private JPanel networkPanel;
   
   private JPanel descPanel;
   private JTextArea descArea;
   
   private boolean isRefreshing = false;
   
   public VmSummaryView() {
      Logger.track();
      init();
   }
   
   private void init() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               init();
            }
         });
      } else {
         initGeneral();
         initSystem();
         initDisplay();
         initStorage();
         initAudio();
         initNetwork();
         initDesc();
         
         panel = new JPanel(new MigLayout("ins 0"));
         panel.add(generalPanel, "growx, pushx, wrap");
         panel.add(systemPanel, "growx, pushx, wrap");
         panel.add(displayPanel, "growx, pushx, wrap");
         panel.add(storagePanel, "growx, pushx, wrap");
         panel.add(audioPanel, "growx, pushx, wrap");
         panel.add(networkPanel, "growx, pushx, wrap");
         panel.add(descPanel, "growx, pushx, wrap");
         
         FrontEventManager.register(this);
      }
   }
   
   private void initGeneral() {
      Logger.track();
      
      nameLabel = new JLabel("Name");
      nameField = JTextFieldUtils.createNonEditable();
      
      uuidLabel = new JLabel("UUID");
      uuidField = JTextFieldUtils.createNonEditable();
      
      stateLabel = new JLabel("Status");
      stateField = JTextFieldUtils.createNonEditable();
      
      osTypeLabel = new JLabel("OS Type");
      osTypeField = JTextFieldUtils.createNonEditable();
      
      generalPanel = new JPanel(new MigLayout());
      generalPanel.setBorder(BorderUtils.createTitledBorder(Color.gray, "General"));
      generalPanel.add(nameLabel);
      generalPanel.add(nameField, "growx, pushx, wrap");
      generalPanel.add(uuidLabel);
      generalPanel.add(uuidField, "growx, pushx, wrap");
      generalPanel.add(stateLabel);
      generalPanel.add(stateField, "growx, pushx, wrap");
      generalPanel.add(osTypeLabel);
      generalPanel.add(osTypeField, "growx, pushx, wrap");
   }
   
   private void initSystem() {
      Logger.track();
      
      cpuCountLabel = new JLabel("vCPU");
      cpuCountValue = JTextFieldUtils.createNonEditable();
      memoryLabel = new JLabel("Memory");
      memoryValue = JTextFieldUtils.createNonEditable();
      accelLabel = new JLabel("Acceleration");
      accelValue = JTextFieldUtils.createNonEditable();
      
      systemPanel = new JPanel(new MigLayout());
      systemPanel.setBorder(BorderUtils.createTitledBorder(Color.gray, "System"));
      systemPanel.add(cpuCountLabel);
      systemPanel.add(cpuCountValue, "growx, pushx, wrap");
      systemPanel.add(memoryLabel);
      systemPanel.add(memoryValue, "growx, pushx, wrap");
      systemPanel.add(accelLabel);
      systemPanel.add(accelValue, "growx, pushx, wrap");
   }
   
   private void initDisplay() {
      Logger.track();
      
      vramLabel = new JLabel("VRAM");
      vramValue = JTextFieldUtils.createNonEditable();
      consoleModuleLabel = new JLabel("Console Module");
      consoleModuleValue = JTextFieldUtils.createNonEditable();
      consoleAddressLabel = new JLabel("Console Address");
      consoleAddressValue = JTextFieldUtils.createNonEditable();
      consoleConnectButton = new JButton("Connect");
      consoleConnectButton.setEnabled(false);
      consoleConnectButton.addActionListener(new ConnectAction());
      
      displayPanel = new JPanel(new MigLayout());
      displayPanel.setBorder(BorderUtils.createTitledBorder(Color.GRAY, "Display"));
      displayPanel.add(vramLabel);
      displayPanel.add(vramValue, "growx, pushx,span 2, wrap");
      displayPanel.add(consoleModuleLabel);
      displayPanel.add(consoleModuleValue, "growx, pushx, span 2, wrap");
      displayPanel.add(consoleAddressLabel);
      displayPanel.add(consoleAddressValue, "growx, pushx");
      displayPanel.add(consoleConnectButton, "wrap");
   }
   
   private void initStorage() {
      Logger.track();
      
      controllers = new HashMap<String, StorageControllerOutput>();
      storagePanel = new JPanel(new MigLayout());
      storagePanel.setBorder(BorderUtils.createTitledBorder(Color.gray, "Storage"));
   }
   
   private void initAudio() {
      Logger.track();
      
      hostDriverLabel = new JLabel("Host Driver");
      hostDriverValue = JTextFieldUtils.createNonEditable();
      audioControllerLabel = new JLabel("Controller");
      audioControllerValue = JTextFieldUtils.createNonEditable();
      
      audioPanel = new JPanel(new MigLayout());
      audioPanel.setBorder(BorderUtils.createTitledBorder(Color.gray, "Audio"));
      audioPanel.add(hostDriverLabel);
      audioPanel.add(hostDriverValue, "growx, pushx, wrap");
      audioPanel.add(audioControllerLabel);
      audioPanel.add(audioControllerValue, "growx, pushx, wrap");
   }
   
   private void initNetwork() {
      Logger.track();
      
      networkPanel = new JPanel(new MigLayout());
      networkPanel.setBorder(BorderUtils.createTitledBorder(Color.gray, "Network"));
   }
   
   private void initDesc() {
      Logger.track();
      
      descArea = new JTextArea();
      descArea.setEditable(false);
      
      descPanel = new JPanel(new MigLayout());
      descPanel.setBorder(BorderUtils.createTitledBorder(Color.gray, "Description"));
      descPanel.add(descArea, "grow, push, wrap");
   }
   
   private void clearGeneral() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               clearGeneral();
            }
         });
      } else {
         nameField.setText(null);
         uuidField.setText(null);
         stateField.setText(null);
         osTypeField.setText(null);
      }
   }
   
   private void clearSystem() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               clearSystem();
            }
         });
      } else {
         cpuCountValue.setText(null);
         memoryValue.setText(null);
         accelValue.setText(null);
      }
   }
   
   private void clearDisplay() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               clearDisplay();
            }
         });
      } else {
         vramValue.setText(null);
         consoleModuleValue.setText(null);
         consoleAddressValue.setText(null);
      }
   }
   
   private void clearStorage() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               clearStorage();
            }
         });
      } else {
         storagePanel.removeAll();
      }
   }
   
   private void clearAudio() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               clearAudio();
            }
         });
      } else {
         hostDriverValue.setText(null);
         audioControllerValue.setText(null);
      }
   }
   
   private void clearNetwork() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               clearNetwork();
            }
         });
      } else {
         networkPanel.removeAll();
      }
   }
   
   private void clearDesc() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               clearDesc();
            }
         });
      } else {
         descArea.setText(null);
      }
   }
   
   public void clear() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               clear();
            }
         });
      } else {
         mOut = null;
         clearGeneral();
         clearSystem();
         clearDisplay();
         clearStorage();
         clearAudio();
         clearNetwork();
         clearDesc();
      }
   }
   
   public void show(final MachineOutput mOut) {
      Logger.track();
      
      if ((this.mOut == null) || !this.mOut.equals(mOut)) {
         this.mOut = mOut;
         refresh();
      }
   }
   
   public void refreshGeneral() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               refreshGeneral();
            }
         });
      } else {
         nameField.setText(mOut.getName());
         uuidField.setText(mOut.getUuid());
         stateField.setText(mOut.getState());
         osTypeField.setText(mOut.getSetting(MachineAttributes.OsType).getString());
      }
   }
   
   public void refreshSystem() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               refreshSystem();
            }
         });
      } else {
         cpuCountValue.setText(mOut.getSetting(MachineAttributes.CpuCount).getString());
         memoryValue.setText(mOut.getSetting(MachineAttributes.Memory).getString() + " MB");
         List<String> extList = new ArrayList<String>();
         if (mOut.getSetting(MachineAttributes.HwVirtEx).getBoolean()) {
            extList.add("VT-x/AMD-V");
         }
         if (mOut.getSetting(MachineAttributes.HwVirtExExcl).getBoolean()) {
            extList.add("Virt Unrestricted");
         }
         if (mOut.getSetting(MachineAttributes.NestedPaging).getBoolean()) {
            extList.add("Nested Paging");
         }
         if (mOut.getSetting(MachineAttributes.PAE).getBoolean()) {
            extList.add("PAE/NX");
         }
         if (mOut.getSetting(MachineAttributes.LargePages).getBoolean()) {
            extList.add("Large Pages");
         }
         if (mOut.getSetting(MachineAttributes.Vtxvpid).getBoolean()) {
            extList.add("VT-x VPID");
         }
         StringBuilder extBuilder = new StringBuilder();
         for (String ext : extList) {
            extBuilder.append(ext + ", ");
         }
         extBuilder.delete(extBuilder.lastIndexOf(", "), extBuilder.length());
         accelValue.setText(extBuilder.toString());
      }
   }
   
   public void refreshDisplay() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               refreshDisplay();
            }
         });
      } else {
         vramValue.setText(mOut.getSetting(MachineAttributes.VRAM).getString());
         
         consoleModuleValue.setText(mOut.getSetting(MachineAttributes.VrdeModule).getString());
         
         if (mOut.getSetting(MachineAttributes.VrdeEnabled).getBoolean()) {
            String addr = Gui.getReader().getConnectorForServer(mOut.getServerId()).getAddress();
            if (!StringTools.isEmpty(mOut.getSetting(MachineAttributes.VrdeAddress).getString())) {
               addr = mOut.getSetting(MachineAttributes.VrdeAddress).getString();
            }
            addr = addr + ":" + mOut.getSetting(MachineAttributes.VrdePort).getString();
            consoleAddressValue.setText(addr);
            consoleConnectButton.setEnabled(mOut.getState().equalsIgnoreCase("running"));
         } else {
            consoleAddressValue.setText("Not available (Disabled or Console Module not installed)");
         }
      }
   }
   
   public void refreshStorage() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               refreshStorage();
            }
         });
      } else {
         clearStorage();
         if (controllers.isEmpty()) {
            for (StorageControllerOutput scOut : mOut.listStorageController()) {
               controllers.put(scOut.getId(), scOut);
            }
         }
         
         for (StorageControllerOutput scOut : controllers.values()) {
            try {
               storagePanel.add(new JLabel(scOut.getType()), "wrap");
               for (StorageDeviceAttachmentOutput sdaOut : scOut.getAttachments()) {
                  storagePanel.add(new JLabel(""));
                  storagePanel.add(new JLabel(sdaOut.getPortId() + ":" + sdaOut.getDeviceId()));
                  
                  storagePanel.add(new JLabel(""));
                  storagePanel.add(new JLabel(""));
                  if (sdaOut.hasMediumInserted()) {
                     MediumOutput medOut = Gui.getServer(mOut.getServerId()).getMedium(new MediumInput(sdaOut.getMediumUuid()));
                     while (medOut.hasParent()) {
                        Logger.debug(medOut.getName() + " has parent : " + medOut.getParentUuid());
                        medOut = Gui.getServer(mOut.getServerId()).getMedium(new MediumInput(medOut.getParentUuid()));
                     }
                     storagePanel.add(new JLabel("[" + sdaOut.getDeviceType() + "] " + medOut.getName()));
                  } else {
                     storagePanel.add(new JLabel("[" + sdaOut.getDeviceType() + "] Empty"));
                  }
                  if (sdaOut.getDeviceType().contentEquals(EntityTypes.DVD.getId())) {
                     storagePanel.add(new JButton(new StorageDeviceAttachmentMediumEditAction(mOut.getServerId(), sdaOut)), "wrap");
                  } else {
                     storagePanel.add(new JLabel(""), "wrap");
                  }
               }
            } catch (Throwable e) {
               storagePanel.removeAll();
               storagePanel.add(new JLabel("Unable to load storage info: " + e.getMessage()));
               storagePanel.revalidate();
            } finally {
               storagePanel.repaint();
               storagePanel.revalidate();
            }
         }
      }
   }
   
   public void refreshAudio() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               refreshAudio();
            }
         });
      } else {
         audioPanel.removeAll();
         if (mOut.getSetting(MachineAttributes.AudioEnable).getBoolean()) {
            hostDriverValue.setText(mOut.getSetting(MachineAttributes.AudioDriver).getString());
            audioControllerValue.setText(mOut.getSetting(MachineAttributes.AudioController).getString());
            
            audioPanel.add(hostDriverLabel);
            audioPanel.add(hostDriverValue, "growx, pushx, wrap");
            audioPanel.add(audioControllerLabel);
            audioPanel.add(audioControllerValue, "growx, pushx, wrap");
         } else {
            audioPanel.add(new JLabel("Disabled"));
         }
         audioPanel.revalidate();
      }
   }
   
   public void refreshNetwork() {
      Logger.track();
      
      clearNetwork();
      new SwingWorker<List<StorageDeviceAttachmentOutput>,Void>() {
         
         @Override
         protected List<StorageDeviceAttachmentOutput> doInBackground() throws Exception {
            for (NetworkInterfaceOutput nicOut : mOut.listNetworkInterface()) {
               if (nicOut.isEnabled()) {
                  GuestNetworkInterfaceOutput gNicOut = null;
                  try  {
                     gNicOut = Gui.getReader().getServerReader(mOut.getServerId()).getGuest(mOut.getUuid())
                           .findNetworkInterface(nicOut.getMacAddress());
                  } catch (Throwable t) {
                     gNicOut = null;
                  }
                  
                  networkPanel.add(NetworkInterfaceSummary.get(nicOut, gNicOut), "growx, pushx, wrap");
               }
            }
            return null;
         }
         
         @Override
         protected void done() {
            networkPanel.revalidate();
         }
         
      }.execute();
      
      
   }
   
   public void refreshDesc() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               refreshDesc();
            }
         });
      } else {
         descArea.setText(mOut.getSetting(MachineAttributes.Description).getString());
      }
   }
   
   public void refresh() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               refresh();
            }
         });
      }
      
      try {
         if (!isRefreshing) {
            isRefreshing = true;
            if (mOut == null) {
               clear();
            } else {
               refreshGeneral();
               refreshSystem();
               refreshDisplay();
               controllers.clear();
               refreshStorage();
               refreshAudio();
               refreshNetwork();
               refreshDesc();
               panel.revalidate();
               panel.repaint();
            }
         } else {
            Logger.warning("Trying to refresh VM info while already refreshing");
         }
      } finally {
         isRefreshing = false;
      }
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   @Handler
   public void putMachineStateChangedEvent(MachineStateChangedEvent ev) {
      if (ev.getUuid().contentEquals(mOut.getUuid())) {
         stateField.setText(ev.getMachine().getState());
         // TODO improve next line
         consoleConnectButton.setEnabled(ev.getMachine().getState().equalsIgnoreCase("running"));
      }
   }
   
   @Handler
   public void putStorageControllerAttachmentDataChanged(StorageControllerAttachmentDataModifiedEventOutput ev) {
      if (ev.getUuid().contentEquals(mOut.getUuid())) {
         controllers.put(ev.getStorageController().getId(), ev.getStorageController());
         refreshStorage();
      }
   }
   
   private class ConnectAction implements ActionListener {
      
      @Override
      public void actionPerformed(ActionEvent ae) {
         Logger.track();
         
         Gui.post(new Request(ClientTasks.ConsoleViewerUse, new ServerInput(mOut.getServerId()), new MachineInput(mOut)));
      }
      
   }
   
}