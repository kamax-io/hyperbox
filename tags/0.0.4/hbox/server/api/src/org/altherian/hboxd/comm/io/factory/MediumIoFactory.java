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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hboxd.core.model._Medium;
import org.altherian.hboxd.hypervisor.storage._RawMedium;

public class MediumIoFactory {
   
   private MediumIoFactory() {
      // static class
   }
   
   public static MediumOutput get(_RawMedium m) {
      MediumOutput mIo = new MediumOutput(m.getUuid(), SettingIoFactory.getList(m.listSettings()));
      return mIo;
   }
   
   public static MediumOutput get(_Medium m) {
      MediumOutput mIo = new MediumOutput(m.getUuid(), SettingIoFactory.getList(m.getSettings()));
      return mIo;
   }
   
}