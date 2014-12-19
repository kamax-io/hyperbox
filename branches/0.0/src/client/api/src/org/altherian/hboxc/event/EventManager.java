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

package org.altherian.hboxc.event;

import org.altherian.tool.logging.Logger;

public final class EventManager {
   
   private static _EventManager evMgr;
   
   private EventManager() {
      // static only
   }
   
   static {
      evMgr = new DefaultEventManager("Core-EvMgr");
   }
   
   public static _EventManager get() {
      return evMgr;
   }
   
   public static void register(Object o) {
      Logger.track();
      
      evMgr.register(o);
   }
   
   public static void unregister(Object o) {
      Logger.track();
      
      evMgr.unregister(o);
   }
   
   public static void post(Object o) {
      Logger.track();
      
      evMgr.post(o);
   }
   
}
