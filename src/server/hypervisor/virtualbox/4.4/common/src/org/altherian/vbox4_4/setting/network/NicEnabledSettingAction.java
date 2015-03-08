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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.vbox4_4.setting.network;

import org.altherian.hbox.constant.NetworkInterfaceAttribute;
import org.altherian.setting.BooleanSetting;
import org.altherian.setting._Setting;
import org.altherian.vbox.settings.network.NicEnabledSetting;
import org.altherian.vbox4_4.setting._NetworkInterfaceSettingAction;
import org.virtualbox_4_4.INetworkAdapter;
import org.virtualbox_4_4.LockType;

public class NicEnabledSettingAction implements _NetworkInterfaceSettingAction {
   
   @Override
   public LockType getLockType() {
      return LockType.Write;
   }
   
   @Override
   public String getSettingName() {
      return NetworkInterfaceAttribute.Enabled.toString();
   }
   
   @Override
   public void set(INetworkAdapter nic, _Setting setting) {
      nic.setEnabled(((BooleanSetting) setting).getValue());
   }
   
   @Override
   public _Setting get(INetworkAdapter nic) {
      return new NicEnabledSetting(nic.getEnabled());
   }
   
}