/*----------------------------------------------------------------------------
Project: Agile Tracking Tool

Copyright 2008, 2009   Ben Schreur
------------------------------------------------------------------------------
This file is part of Agile Tracking Tool.

Agile Tracking Tool is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Agile Tracking Tool is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Agile Tracking Tool.  If not, see <http://www.gnu.org/licenses/>.
------------------------------------------------------------------------------*/
package org.agiletracking

class ItemGroup extends ItemContainer {
	
	String name         
	static belongsTo = [project:Project]
	
	String toString() { return name }
	
	static constraints  = {
		project(nullable:false)
	}
	
	Double totalPoints() { return items.sum{ it.points } }
	Double finishedPoints() { return items.sum{ (it.status == ItemStatus.Finished) ? it.points : 0 } }
}
