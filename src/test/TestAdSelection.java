package test;

import static org.junit.Assert.*;

import java.awt.RenderingHints.Key;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import model.PlacementAdMapping;

import org.junit.Test;

import utils.MyCustomHashMap;
import controller.AdCallProcessor;

@SuppressWarnings("unused")
public class TestAdSelection extends AdCallProcessor {

	// Custom HashMap helps in storing/retrieving ad mapping
	// efficiently.
	MyCustomHashMap testAdMappings;

	public static void main(String[] args) {

	}

	protected Collection<PlacementAdMapping> getAdsForPlacement(int placementId) {
		MyCustomHashMap allAdMappings = this.getAllAds();

		// Get all ad mappings (as an ArrayList) associated with this placementId
		return allAdMappings.get(placementId);
	}

	/*
	 * Seed test data: Ads and their placement mapping
	 */
	private MyCustomHashMap getAllAds() {

		// Create new ad mappings only first time
		if (this.testAdMappings == null) {
			PlacementAdMapping ad1 = new PlacementAdMapping(1, 100, 10);
			PlacementAdMapping ad2 = new PlacementAdMapping(2, 100, 10);
			PlacementAdMapping ad4 = new PlacementAdMapping(2, 101, 20);
			PlacementAdMapping ad3 = new PlacementAdMapping(2, 102, 20);
			PlacementAdMapping ad5 = new PlacementAdMapping(3, 100, 15);
			PlacementAdMapping ad6 = new PlacementAdMapping(3, 103, 5);

			this.testAdMappings = new MyCustomHashMap();
			this.testAdMappings.addAdMapping(ad1);
			this.testAdMappings.addAdMapping(ad2);
			this.testAdMappings.addAdMapping(ad3);
			this.testAdMappings.addAdMapping(ad4);
			this.testAdMappings.addAdMapping(ad5);
			this.testAdMappings.addAdMapping(ad6);
		}

		return this.testAdMappings;
	}

	/*
	 * Use Case: If the ad call is for placement =1, then ad 100 is always
	 * shown. Steps: 1. Get all ads associated with placement id 100 2. Call
	 * chooseAd method 50 times and record rendered ad id 3. Rendered ad id
	 * count should be 50
	 */
	@Test
	public void TestAdSelection1() {

		AdCallProcessor adCallProcessor = new AdCallProcessor();
		PlacementAdMapping selectedAd;

		int placementId = 1;
		int count = 50;

		int expectedAdId = 100;
		int expectedAd100RenderCount = 50;

		// Step 1 Get all eligible ads
		Collection<PlacementAdMapping> eligibleAds = this
				.getAdsForPlacement(placementId);

		// Store Ad Id and Count of number of times it is rendered
		HashMap<Integer, Integer> adCallCount = new HashMap<>();

		// Step 2: Call choose ad 50 times and store the chosen ads' id
		while (count-- > 0) {
			selectedAd = adCallProcessor.chooseAd(eligibleAds);

			// CreativeId ==> Ad Id
			if (!adCallCount.containsKey(selectedAd.getAdId())) {
				// Ad new ad to adCallCount collection and initialize count to 0
				adCallCount.put(selectedAd.getAdId(), 0);
			}

			int oldRenderCount = adCallCount.get(selectedAd.getAdId());

			// Increment count by one
			oldRenderCount++;

			// Update Render Count in collection
			adCallCount.put(selectedAd.getAdId(), oldRenderCount);
		}

		// 3. Verify only add 100 is invoked and it is invoked all 50 times
		// Note: we could have two test case for this use case, one validating
		// which ad id is invoked another validating number of times it is
		// rendered

		assertTrue("Wrong ads rendered", adCallCount.keySet().size() == 1);

		for (Integer adId : adCallCount.keySet()) {
			assertTrue("Wrong ad rendered", adId == expectedAdId);
			
			assertTrue("Ad rendered incorrect times", adCallCount.get(adId) == expectedAd100RenderCount);
		}
	}

	/*
	 * Use Case 2: Validate ads are rendered according to their relative weight
	 * For placement id = 2, ad 100 -> 20%, ad 101 -> 40%, ad 102 -> 40%
	 */
	@Test
	public void TestAdSelection2() {

		AdCallProcessor adCallProcessor = new AdCallProcessor();
		PlacementAdMapping selectedAd;

		int PLACEMENT_ID = 2;
		int TOTAL_RENDERS = 5000;

		int EXPECTED_TOTAL_ADS = 3;

		// Add expected ads and their render percentage for placement id = 2
		HashMap<Integer, Integer> expectedAds = new HashMap<>();
		expectedAds.put(100, 20);
		expectedAds.put(101, 40);
		expectedAds.put(102, 40);

		// Step 1: get all eligible ads
		Collection<PlacementAdMapping> eligibleAds = this
				.getAdsForPlacement(PLACEMENT_ID);

		// Store Ad Id and Count of number of times it is rendered
		HashMap<Integer, Integer> adCallCount = new HashMap<>();

		// Step 2: Run choose ad method
		int counter = TOTAL_RENDERS;
		while (counter-- > 0) {
			selectedAd = adCallProcessor.chooseAd(eligibleAds);

			// CreativeId ==> Ad Id
			if (!adCallCount.containsKey(selectedAd.getAdId())) {
				// Ad new ad to adCallCount collection and initialize count to 0
				adCallCount.put(selectedAd.getAdId(), 0);
			}

			int oldRenderCount = adCallCount.get(selectedAd.getAdId());

			oldRenderCount++;

			adCallCount.put(selectedAd.getAdId(), oldRenderCount);
		}

		// Step 3: Verify
		assertTrue("Wrong ads rendered",
				adCallCount.keySet().size() == EXPECTED_TOTAL_ADS);

		for (Integer adId : adCallCount.keySet()) {
			assertTrue("Wrong ad rendered", expectedAds.containsKey(adId));

			int adRenderCount = adCallCount.get(adId);
			float renderPercent = (float) (adRenderCount * 100) / TOTAL_RENDERS;

			float callPercentageDiff = Math.abs(renderPercent
					- expectedAds.get(adId));

			assertTrue(
					"Ad "+ adId + "rendered wrong number of times . Percentage difference: "
							+ callPercentageDiff, callPercentageDiff < 4);
			// Approximation +-4% error
		}
	}
}
