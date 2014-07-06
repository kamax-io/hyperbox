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

package org.altherian.hbox.event;

public enum HyperboxEvents {
   
   ServerState,
   ServerConnectionState,
   ServerShutdown,
   SystemState,
   
   ServiceStatus,
   
   ClientState,
   
   SessionState,
   
   TaskState,
   TaskQueue,
   
   StoreState,
   
   ModuleRegistered,
   ModuleUnregistered,
   ModuleEnabled,
   ModuleDisabled,
   ModuleLoaded,
   ModuleUnloaded,
   
   MachineState,
   MachineRegistration,
   MachineDataChange,
   
   /**
    * Temporary event due to Virtualbox Webservice bug
    * 
    * @see <a href="https://www.virtualbox.org/ticket/12379">Virtualbox Bugtracker Ticket #12379</a>
    */
   // TODO review regularly
   MachineSnapshotDataChange,
   
   SnapshotModified,
   SnapshotTaken,
   SnapshotDeleted,
   SnapshotRestored,
   
   ServerFrontLoaded,
   ServerFrontStarted,
   ServerFrontStopped,
   ServerFrontUnloaded,
   ServerFrontCrashed,
   
   UserAdded,
   UserModified,
   UserRemoved,
   UserEnabled,
   UserDisabled,
   
   HypervisorConnected,
   HypervisorDisconnected,
   HypervisorConfigured,
   
   StorageControllerAdded,
   StorageControllerModified,
   StorageControllerRemoved,
   StorageControllerAttachmentDataModified,
   
   StorageAttachmentAdded,
   StorageAttachmentModified,
   StorageAttachmentRemoved,
   
}