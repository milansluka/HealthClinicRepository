package ehc.bo.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import ehc.bo.Resource;
import ehc.bo.ResourceList;

public class ResourceListImpl implements ResourceList {
	Map<ResourceType, SortedSet<Resource>> resources = new HashMap<>();

	@Override
	public void addResource(ResourceType resourceType, Resource resource) {
		if (resources.containsKey(resourceType)) {
			resources.get(resourceType).add(resource);
		} else {
			SortedSet<Resource> resourcesOfGivenType = new TreeSet<>();
			resourcesOfGivenType.add(resource);
			resources.put(resourceType, resourcesOfGivenType);
		}
		
	}	
	
	@Override
	public Iterator<ResourceType> iterator() {
		return resources.keySet().iterator();
	}

	@Override
	public SortedSet<Resource> getSortedResourcesOfType(ResourceType resourceType) {
		return resources.get(resourceType);
	}

	@Override
	public void addResources(ResourceType resourceType, SortedSet<Resource> sortedResources) {
		if (resources.containsKey(resourceType)) {
			return;
		}
		resources.put(resourceType, sortedResources);	
	}
}
