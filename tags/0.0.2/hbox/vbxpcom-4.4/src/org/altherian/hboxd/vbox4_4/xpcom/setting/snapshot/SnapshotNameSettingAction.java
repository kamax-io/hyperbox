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

package org.altherian.hboxd.vbox4_4.xpcom.setting.snapshot;

import org.altherian.hbox.constant.SnapshotAttributes;
import org.altherian.hboxd.settings.StringSetting;
import org.altherian.hboxd.settings._Setting;
import org.altherian.hboxd.vbox4_4.xpcom.setting._SnapshotSettingAction;

import org.virtualbox_4_4.ISnapshot;
import org.virtualbox_4_4.LockType;

public class SnapshotNameSettingAction implements _SnapshotSettingAction {
   
   @Override
   public LockType getLockType() {
      return LockType.Shared;
   }
   
   @Override
   public String getSettingName() {
      return SnapshotAttributes.Name.getId();
   }
   
   @Override
   public void set(ISnapshot snap, _Setting setting) {
      String name = setting.getString();
      snap.setName(name);
   }
   
   @Override
   public _Setting get(ISnapshot snap) {
      return new StringSetting(SnapshotAttributes.Name, snap.getName());
   }
   
}