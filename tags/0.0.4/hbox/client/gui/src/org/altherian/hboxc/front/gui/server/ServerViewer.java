/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hboxc.front.gui.server;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.event.hypervisor.HypervisorConnectedEventOutput;
import org.altherian.hbox.comm.output.event.hypervisor.HypervisorDisconnectedEventOutput;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.event.server.ServerEvent;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.hypervisor.HypervisorViewer;
import org.altherian.hboxc.front.gui.workers.ServerGetWorker;
import org.altherian.hboxc.front.gui.workers._ServerReceiver;
import org.altherian.helper.swing.JTextFieldUtils;
import org.altherian.tool.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ServerViewer implements _Refreshable, _ServerReceiver {
   
   private ServerOutput srvOut;
   
   private HypervisorViewer hypViewer;
   
   private JLabel idLabel;
   private JTextField idValue;
   private JLabel nameLabel;
   private JTextField nameValue;
   private JLabel typeLabel;
   private JTextField typeValue;
   private JLabel versionLabel;
   private JTextField versionValue;
   private JLabel netProtocolLabel;
   private JTextField netProtocolValue;
   
   private JPanel srvPanel;
   
   private JPanel panel;
   
   public ServerViewer() {
      idLabel = new JLabel("ID");
      nameLabel = new JLabel("Name");
      typeLabel = new JLabel("Type");
      versionLabel = new JLabel("Version");
      netProtocolLabel = new JLabel("Protocol");
      
      idValue = JTextFieldUtils.createNonEditable();
      nameValue = JTextFieldUtils.createNonEditable();
      typeValue = JTextFieldUtils.createNonEditable();
      versionValue = JTextFieldUtils.createNonEditable();
      netProtocolValue = JTextFieldUtils.createNonEditable();
      
      hypViewer = new HypervisorViewer();
      
      srvPanel = new JPanel(new MigLayout());
      srvPanel.setBorder(BorderFactory.createTitledBorder("Server"));
      srvPanel.add(idLabel);
      srvPanel.add(idValue, "growx,pushx,wrap");
      srvPanel.add(nameLabel);
      srvPanel.add(nameValue, "growx,pushx,wrap");
      srvPanel.add(typeLabel);
      srvPanel.add(typeValue, "growx,pushx,wrap");
      srvPanel.add(versionLabel);
      srvPanel.add(versionValue, "growx,pushx,wrap");
      srvPanel.add(netProtocolLabel);
      srvPanel.add(netProtocolValue, "growx,pushx,wrap");
      
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(srvPanel, "growx, pushx,wrap");
      panel.add(hypViewer.getComponent(), "growx, pushx");
      
      FrontEventManager.register(this);
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   public void show(ServerOutput srvOut) {
      this.srvOut = srvOut;
      update();
   }
   
   private void clear() {
      // TODO implement
   }
   
   @Override
   public void refresh() {
      clear();
      ServerGetWorker.get(this, srvOut.getId());
   }
   
   private void update() {
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               update();
            }
         });
      } else {
         idValue.setText(srvOut.getId());
         nameValue.setText(srvOut.getName());
         typeValue.setText(srvOut.getType());
         versionValue.setText(srvOut.getVersion());
         netProtocolValue.setText(srvOut.getNetworkProtocolVersion() != null ? srvOut.getNetworkProtocolVersion() : "Unknown");
         
         if (srvOut.isHypervisorConnected()) {
            hypViewer.show(Gui.getServer(srvOut).getHypervisor().getInfo());
         } else {
            hypViewer.setDisconnected();
         }
      }
   }
   
   @Handler
   public void putServerEvent(ServerEvent ev) {
      Logger.track();
      
      if (ev.getServer().getId().equals(srvOut.getId())) {
         refresh();
      }
   }
   
   // TODO move into the HypervisorViewer class
   @Handler
   public void putHypervisorConnectEvent(HypervisorConnectedEventOutput ev) {
      if (ev.getServer().getId().equals(srvOut.getId())) {
         hypViewer.show(Gui.getServer(srvOut).getHypervisor().getInfo());
      }
   }
   
   // TODO move into the HypervisorViewer class
   @Handler
   public void putHypervisorDisconnectEvent(HypervisorDisconnectedEventOutput ev) {
      if (ev.getServer().getId().equals(srvOut.getId())) {
         hypViewer.setDisconnected();
      }
   }
   
   @Override
   public void loadingStarted() {
      // nothing to do yet
   }
   
   @Override
   public void loadingFinished(boolean isSuccessful, String message) {
      // TODO implement in case of error
   }
   
   @Override
   public void put(ServerOutput srvOut) {
      Logger.track();
      
      this.srvOut = srvOut;
      update();
   }
   
}