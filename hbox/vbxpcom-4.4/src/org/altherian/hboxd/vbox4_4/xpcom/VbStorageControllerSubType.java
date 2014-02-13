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

package org.altherian.hboxd.vbox4_4.xpcom;

import org.altherian.hboxd.hypervisor.storage._RawStorageControllerSubType;

public enum VbStorageControllerSubType implements _RawStorageControllerSubType {
   
   LsiLogic(VbStorageControllerType.SCSI),
   BusLogic(VbStorageControllerType.SCSI),
   IntelAhci(VbStorageControllerType.SATA),
   PIIX3(VbStorageControllerType.IDE),
   PIIX4(VbStorageControllerType.IDE),
   ICH6(VbStorageControllerType.IDE),
   I82078(VbStorageControllerType.Floppy),
   LsiLogicSas(VbStorageControllerType.SAS);
   
   private VbStorageControllerType type;
   
   private VbStorageControllerSubType(VbStorageControllerType type) {
      this.type = type;
   }
   
   @Override
   public String getParentType() {
      return type.getId();
   }
   
   @Override
   public String getId() {
      return toString();
   }
   
}
