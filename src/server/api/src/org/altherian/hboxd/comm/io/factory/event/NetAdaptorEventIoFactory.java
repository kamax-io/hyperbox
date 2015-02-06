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

package org.altherian.hboxd.comm.io.factory.event;

import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.event.EventOut;
import org.altherian.hbox.comm.out.event.net.NetAdaptorAddedEventOut;
import org.altherian.hbox.comm.out.event.net.NetAdaptorRemovedEventOut;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.event._Event;
import org.altherian.hboxd.comm.io.factory.ServerIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.event.net.NetAdaptorEvent;
import org.altherian.tool.logging.Logger;

public class NetAdaptorEventIoFactory implements _EventIoFactory {
   
   @Override
   public Enum<?>[] getHandles() {
      return new Enum<?>[] {
            HyperboxEvents.NetAdaptorAdded,
            HyperboxEvents.NetAdaptorRemoved
      };
   }
   
   @Override
   public EventOut get(_Hyperbox hbox, _Event ev) {
      if (!(ev instanceof NetAdaptorEvent)) {
         Logger.warning("Was given event of type " + ev.getClass().getName());
         return null;
      }
      
      NetAdaptorEvent adaptEv = (NetAdaptorEvent) ev;
      ServerOut srvOut = ServerIoFactory.get();
      switch ((HyperboxEvents) ev.getEventId()) {
         case NetAdaptorAdded:
            return new NetAdaptorAddedEventOut(ev.getTime(), srvOut, adaptEv.getHypervisor().getId(), adaptEv.getMode(), adaptEv.getAdaptor());
         case NetAdaptorRemoved:
            return new NetAdaptorRemovedEventOut(ev.getTime(), srvOut, adaptEv.getHypervisor().getId(), adaptEv.getMode(), adaptEv.getAdaptor());
         default:
            return null;
      }
   }
   
}