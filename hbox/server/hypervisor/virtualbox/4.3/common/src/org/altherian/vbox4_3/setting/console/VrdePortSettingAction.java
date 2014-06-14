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

package org.altherian.vbox4_3.setting.console;

import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hboxd.settings.StringSetting;
import org.altherian.hboxd.settings._Setting;
import org.altherian.vbox4_3.manager.VbSessionManager;
import org.altherian.vbox4_3.setting._MachineSettingAction;

import org.virtualbox_4_3.IMachine;
import org.virtualbox_4_3.ISession;
import org.virtualbox_4_3.LockType;

public class VrdePortSettingAction implements _MachineSettingAction {
   
   @Override
   public LockType getLockType() {
      return LockType.Shared;
   }
   
   @Override
   public String getSettingName() {
      return MachineAttributes.VrdePort.getId();
   }
   
   @Override
   public void set(IMachine machine, _Setting setting) {
      machine.getVRDEServer().setVRDEProperty("TCP/Ports", setting.getString());
   }
   
   @Override
   public _Setting get(IMachine machine) {
      // TODO improve by having a getConsole() in SessionManager and throw the appropriate exception
      try {
         ISession sess = VbSessionManager.get().lockAuto(machine.getId());
         try {
            if ((sess.getConsole().getVRDEServerInfo() != null) && (sess.getConsole().getVRDEServerInfo().getPort() > 0)) {
               return new StringSetting(MachineAttributes.VrdePort, Integer.toString(sess.getConsole().getVRDEServerInfo().getPort()));
            } else {
               return new StringSetting(MachineAttributes.VrdePort, machine.getVRDEServer().getVRDEProperty("TCP/Ports"));
            }
         } finally {
            VbSessionManager.get().unlockAuto(machine.getId());
         }
         // FIXME need more precise exception
      } catch (HypervisorException e) {
         return new StringSetting(MachineAttributes.VrdePort, machine.getVRDEServer().getVRDEProperty("TCP/Ports"));
      }
   }
   
}
