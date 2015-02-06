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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hbox.hypervisor.net;

public interface _NetServiceIP4 extends _NetService {
   
   public String getAddress();
   
   public String getMask();
   
   public String getGateway();
   
   public String getNetwork();
   
   public String getBroadcast();
   
   public void setIP(String ip);
   
   public void setMask(String mask);

   public void setGateway(String gw);

   public void setNetwork(String net);

   public void setBroadcast(String bc);
   
}