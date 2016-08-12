package ehc.bo;

import java.util.SortedSet;

import ehc.bo.impl.ResourceType;

public interface ResourceList extends Iterable<ResourceType> {
	void addResource(ResourceType resourceType, Resource resource);
	void addResources(ResourceType resourceType, SortedSet<Resource> resources);
    SortedSet<Resource> getSortedResourcesOfType(ResourceType resourceType);
}
