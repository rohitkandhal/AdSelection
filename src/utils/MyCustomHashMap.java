package utils;

import java.util.ArrayList;
import java.util.HashMap;

import model.PlacementAdMapping;

/*
 * Custom HashMap to expedite Ads retrieval based on Placement id.
 * Key: Placement_id
 * Values: ArrayList<PlacementAdMapping>
 */
public class MyCustomHashMap extends
		HashMap<Integer, ArrayList<PlacementAdMapping>> {

	/**
	 * SerialVersionUID is a unique identifier for each class, JVM uses it to
	 * compare the versions of the class ensuring that the same class was used
	 * during Serialization and Deserialization
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Adds ad mapping to ArrayList associated with Placement_id key in HashMaps
	 */
	public void addAdMapping(PlacementAdMapping adMapping) {
		if (adMapping != null) {

			ArrayList<PlacementAdMapping> adMappingList;

			// Check if placement id already exists in HashMap
			if (!this.containsKey(adMapping.getPlacementId())) {
				adMappingList = new ArrayList<PlacementAdMapping>();

				this.put(adMapping.getPlacementId(), adMappingList);
			} else {
				adMappingList = this.get(adMapping.getPlacementId());
			}

			adMappingList.add(adMapping);
		}
	}
}
