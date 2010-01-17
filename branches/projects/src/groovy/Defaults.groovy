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

class Defaults {
	
	static List<Iteration> getIterations(Integer nr, def project)
	{
		List<Iteration> ret = []
		nr.times {
			Iteration iter = new Iteration(project:project)
			iter.workingTitle = "Iteration-${it}"
			iter.status = IterationStatus.FutureWork
			iter.startTime = new Date() - 10
			iter.endTime   = new Date()
			iter.items = []
			
			ret << iter
		}
		
		return ret
	}
	
	static List<ItemGroup> getGroups(Integer nr, def projects = getProjects(1) )
	{
		List<ItemGroup> ret = []
		nr.times {
			def group = new ItemGroup()			
			group.name = "Group-${it}"
			group.items = []
			group.project = Util.random(projects)
			
			ret << group
		}
		
		return ret
	}
	
	static List<Item> getItems(Integer nr, List<ItemGroup> groups, def project = getProjects(1)[0], def maxUid = null)
	{
		maxUid = maxUid ? maxUid : Item.maxUid()				
		List<Item> ret = []
		def prios = [Priority.Low, Priority.Medium, Priority.High]
		
		List points = []
		9.times{ points << it }
		
		nr.times{ index -> 
			def item = new Item(project:project)
			item.uid = index + (maxUid + 1) 
			item.description = "${project.name} + Item  ${index}"			
			item.points = Util.random(points)
			
			if (groups)
			{
				def group = Util.random(groups)
				group.addItem(item)
			}
			else
			{
				item.group = null
			}
			
			item.status = ItemStatus.Request
			item.priority = Util.random(prios)
			item.subItems = []
			item.comment = "Comment for item ${index}"
			item.criteria = "Criteria for item ${index}"
			
			ret << item
		}
		return ret
	}
	
	static List<SubItem> getSubItems(Integer nr, List<Item> items)
	{
		List<SubItem> ret = []
		
		List points = []
		6.times{ points << it }
		
		nr.times{
			def subItem = new SubItem()
			subItem.id = it
			subItem.description = "SubItem ${it}"
			subItem.points = Util.random(points)
			subItem.status = ItemStatus.Request
			def item = Util.random(items)
			item.addSubItem(subItem)
			ret << subItem
		}
		return ret
	}
	
	static def getPointsOverView()
	{
		def myRandom = { Math.round(Math.random()*100.0) }
		def overView = new PointsOverView()
		Priority.each{ prio -> 
			ItemStatus.each{ status -> 
								overView.setPointsForView(prio, status, myRandom()) 
			}
		}
		return overView	
	}
	
	static def getSnapShots(def groups, def startDate, def endDate, def project = getProjects(1)[0])
	{
		def snapShots = []
							
		(startDate..endDate).eachWithIndex{ date, index ->
			def snapShot = new PointsSnapShot(project, date)
			snapShot.id = index + 1
			snapShot.overView = getPointsOverView()
			
			groups.each{ group ->
				def pointsForGroup = new PointsForGroup(group,snapShot)
				pointsForGroup.overView = getPointsOverView()
				snapShot.pointsForGroups << pointsForGroup
			}
			snapShots << snapShot
		}
		return snapShots
	}
	
	static def getProjects(Integer nr)
	{
		def projects = []
		nr.times{
			def name = "Project-${it}"
			projects << new Project(name:"${name}",email:"${name}@projects.org" ) 
		}
		return projects 
	} 
}