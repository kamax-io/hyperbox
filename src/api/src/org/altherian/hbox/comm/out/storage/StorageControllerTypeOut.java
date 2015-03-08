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

package org.altherian.hbox.comm.out.storage;

import org.altherian.hbox.comm.out.ObjectOut;
import org.altherian.hbox.constant.EntityType;

public class StorageControllerTypeOut extends ObjectOut {
   
   private long minPort;
   private long maxPort;
   private long maxDevicePerPort;
   
   @SuppressWarnings("unused")
   private StorageControllerTypeOut() {
      // Used for serialization
   }
   
   public StorageControllerTypeOut(String typeId, long minPort, long maxPort, long maxDevicePerPort) {
      super(EntityType.StorageControllerType, typeId);
      this.minPort = minPort;
      this.maxPort = maxPort;
      this.maxDevicePerPort = maxDevicePerPort;
   }
   
   public long getMaxPort() {
      return maxPort;
   }
   
   public long getMaxDevicePerPort() {
      return maxDevicePerPort;
   }
   
   public long getMinPort() {
      return minPort;
   }
   
}