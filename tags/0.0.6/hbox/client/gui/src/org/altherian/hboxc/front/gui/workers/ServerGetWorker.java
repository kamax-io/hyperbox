/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import javax.swing.SwingWorker;

public class ServerGetWorker extends SwingWorker<ServerOutput, Void> {
   
   private _ServerReceiver recv;
   private String srvId;
   
   public ServerGetWorker(_ServerReceiver recv, String srvId) {
      this.recv = recv;
      this.srvId = srvId;
   }
   
   @Override
   protected ServerOutput doInBackground() throws Exception {
      recv.loadingStarted();
      ServerOutput newSrvOut = Gui.getServerInfo(srvId);
      return newSrvOut;
   }
   
   @Override
   protected void done() {
      Logger.track();
      
      try {
         ServerOutput srvOut = get();
         recv.put(srvOut);
         recv.loadingFinished(true, null);
      } catch (Throwable e) {
         recv.loadingFinished(false, e.getMessage());
      }
   }
   
   public static void get(_ServerReceiver recv, String srvId) {
      new ServerGetWorker(recv, srvId).execute();
   }
   
}