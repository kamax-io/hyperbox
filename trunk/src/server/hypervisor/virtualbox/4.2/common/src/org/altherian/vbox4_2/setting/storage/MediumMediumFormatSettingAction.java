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

package org.altherian.vbox4_2.setting.storage;

import org.altherian.hbox.constant.MediumAttribute;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.settings.StringSetting;
import org.altherian.hboxd.settings._Setting;
import org.altherian.vbox4_2.setting._MediumSettingAction;

import org.virtualbox_4_2.IMedium;

public class MediumMediumFormatSettingAction implements _MediumSettingAction {
   
   @Override
   public String getSettingName() {
      return MediumAttribute.MediumFormat.toString();
   }
   
   @Override
   public void set(IMedium medium, _Setting setting) {
      throw new HyperboxRuntimeException("Read-only setting");
   }
   
   @Override
   public _Setting get(IMedium medium) {
      return new StringSetting(MediumAttribute.MediumFormat, medium.getMediumFormat().toString());
   }
   
}