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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxc.front.gui.hypervisor;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.out.event.hypervisor.HypervisorConnectedEventOut;
import org.altherian.hbox.comm.out.event.hypervisor.HypervisorDisconnectedEventOut;
import org.altherian.hbox.comm.out.hypervisor.HypervisorOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.ViewEventManager;
import org.altherian.hboxc.front.gui._Refreshable;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HypervisorViewer implements _Refreshable {
   
   private String srvId;
   
   private JLabel stateLabel;
   private JTextField stateData;
   
   private JLabel typeLabel;
   private JTextField typeData;
   
   private JLabel vendorLabel;
   private JTextField vendorData;
   
   private JLabel productLabel;
   private JTextField productData;
   
   private JLabel versionLabel;
   private JTextField versionData;
   
   private JLabel revisionLabel;
   private JTextField revisionData;
   
   private JPanel panel;
   
   public HypervisorViewer() {
      stateLabel = new JLabel("State");
      typeLabel = new JLabel("Type");
      vendorLabel = new JLabel("Vendor");
      productLabel = new JLabel("Product");
      versionLabel = new JLabel("Version");
      revisionLabel = new JLabel("Revision");
      
      stateData = new JTextField();
      stateData.setEditable(false);
      typeData = new JTextField();
      typeData.setEditable(false);
      vendorData = new JTextField();
      vendorData.setEditable(false);
      productData = new JTextField();
      productData.setEditable(false);
      versionData = new JTextField();
      versionData.setEditable(false);
      revisionData = new JTextField();
      revisionData.setEditable(false);
      
      panel = new JPanel(new MigLayout());
      panel.setBorder(BorderFactory.createTitledBorder("Hypervisor"));
      panel.add(stateLabel, "hidemode 3");
      panel.add(stateData, "growx,pushx,wrap,hidemode 3");
      panel.add(typeLabel, "hidemode 3");
      panel.add(typeData, "growx,pushx,wrap,hidemode 3");
      panel.add(vendorLabel, "hidemode 3");
      panel.add(vendorData, "growx,pushx,wrap,hidemode 3");
      panel.add(productLabel, "hidemode 3");
      panel.add(productData, "growx,pushx,wrap,hidemode 3");
      panel.add(versionLabel, "hidemode 3");
      panel.add(versionData, "growx,pushx,wrap,hidemode 3");
      panel.add(revisionLabel, "hidemode 3");
      panel.add(revisionData, "growx,pushx,wrap,hidemode 3");
      
      ViewEventManager.register(this);
   }
   
   private void toogleConnected(final boolean isConnected) {
      stateData.setText(isConnected ? "Connected" : "Disconnected");
      typeLabel.setVisible(isConnected);
      typeData.setVisible(isConnected);
      vendorLabel.setVisible(isConnected);
      vendorData.setVisible(isConnected);
      productLabel.setVisible(isConnected);
      productData.setVisible(isConnected);
      versionLabel.setVisible(isConnected);
      versionData.setVisible(isConnected);
      revisionLabel.setVisible(isConnected);
      revisionData.setVisible(isConnected);
   }
   
   public void show(HypervisorOut srvOut) {
      typeData.setText(srvOut.getType());
      vendorData.setText(srvOut.getVendor());
      productData.setText(srvOut.getProduct());
      versionData.setText(srvOut.getVersion());
      revisionData.setText(srvOut.getRevision());
      toogleConnected(true);
   }
   
   public void setDisconnected() {
      typeData.setText(null);
      vendorData.setText(null);
      productData.setText(null);
      versionData.setText(null);
      revisionData.setText(null);
      toogleConnected(false);
   }
   
   public JComponent getComponent() {
      return panel;
   }

   public void setSrvId(String srvId) {
      this.srvId = srvId;
   }
   
   @Handler
   private void putHypervisorConnectEvent(HypervisorConnectedEventOut ev) {
      if ((srvId != null) && ev.getServerId().equals(srvId)) {
         toogleConnected(true);
      }
      
   }
   
   @Handler
   private void putHypervisorConnectEvent(HypervisorDisconnectedEventOut ev) {
      if ((srvId != null) && ev.getServerId().equals(srvId)) {
         toogleConnected(false);
      }
   }

   @Override
   public void refresh() {
      show(Gui.getServer(srvId).getHypervisor().getInfo());
   }
   
}