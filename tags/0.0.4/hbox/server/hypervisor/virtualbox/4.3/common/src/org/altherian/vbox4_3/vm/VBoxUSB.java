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

package org.altherian.vbox4_3.vm;

import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hboxd.hypervisor.vm.device._RawUSB;
import org.altherian.hboxd.settings.BooleanSetting;
import org.altherian.hboxd.settings._Setting;
import org.altherian.vbox.settings.usb.UsbEhciSetting;
import org.altherian.vbox.settings.usb.UsbOhciSetting;
import org.altherian.vbox4_3.manager.VbSettingManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VBoxUSB implements _RawUSB {
   
   private VBoxMachine machine;
   
   public VBoxUSB(VBoxMachine machine) {
      this.machine = machine;
   }
   
   @Override
   public boolean isEnabled() {
      return ((BooleanSetting) machine.getSetting(MachineAttributes.UsbOhci)).getValue();
   }
   
   @Override
   public void setEnabled(boolean isEnabled) {
      setSetting(new UsbOhciSetting(isEnabled));
   }
   
   @Override
   public boolean isEhciEnabled() {
      return ((BooleanSetting) machine.getSetting(MachineAttributes.UsbEhci)).getValue();
   }
   
   @Override
   public void setEhciEnabled(boolean isEnabled) {
      setSetting(new UsbEhciSetting(isEnabled));
   }
   
   @Override
   public List<_Setting> listSettings() {
      List<_Setting> settings = new ArrayList<_Setting>();
      for (MachineAttributes setting : MachineAttributes.values()) {
         if (setting.getDeviceType().equals(EntityTypes.USB)) {
            getSetting(setting);
         }
      }
      return settings;
   }
   
   @Override
   public _Setting getSetting(Object getName) {
      return VbSettingManager.get(machine, getName);
   }
   
   @Override
   public void setSetting(_Setting s) {
      machine.setSetting(Arrays.asList(s));
   }
   
   @Override
   public void setSetting(List<_Setting> s) {
      machine.setSetting(s);
   }
   
}