/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.hbox.comm.in;

import org.altherian.hbox.constant.EntityType;


public class NetServiceIn extends ObjectIn<EntityType> {
   
   private String serviceTypeId;
   private boolean enabled;
   
   protected NetServiceIn() {
      super(EntityType.NetService);
   }
   
   public NetServiceIn(String serviceTypeId) {
      this();
      setServiceTypeId(serviceTypeId);
   }

   public NetServiceIn(String serviceTypeId, String id) {
      this(serviceTypeId);
      setId(id);
   }

   public String getServiceTypeId() {
      return serviceTypeId;
   }

   public void setServiceTypeId(String serviceTypeId) {
      this.serviceTypeId = serviceTypeId;
   }

   public boolean isEnabled() {
      return enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

}