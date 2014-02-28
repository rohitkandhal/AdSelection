package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import model.PlacementAdMapping;

public class AdCallProcessor {

	// Use seed to generate same type of random number
	Random randomGenerator = new Random(31);	
	
	// hypothetical high-level ad call logic...
	public void handleAdCall(int placementId) {
		// which ads are mapped to this placement
		Collection<PlacementAdMapping> eligibleAds = getAdsForPlacement(placementId);
		// choose one of the ads based on relative weight
		PlacementAdMapping chosenAd = chooseAd(eligibleAds);
		// ... render the ad
		System.out.println(chosenAd);
	}

	/**
	 * Choose one of the ads ( based on relative weight )
	 * 
	 * @param ads
	 *            list of ads to choose from
	 * @return the chosen ad
	 */
	public PlacementAdMapping chooseAd(Collection<PlacementAdMapping> ads) {

		// Approach: 1. Calculate Sum of weight of all adds
		// 2. Generate random number between 0 - total sum
		// 3. Pick add which lies within that add range.

		int weightSum = 0;

		for (PlacementAdMapping adMap : ads) {
			weightSum += adMap.getWeight();
		}
		
		// Note random generator generates value between 0 (inclusive) and weightSum (exclusive)
		int randomWeight = randomGenerator.nextInt(weightSum);
		
		weightSum = 0;
		
		// Loop through all ads again and find the correct ad based on randomWeight
		for(PlacementAdMapping adMap : ads)
		{
			weightSum += adMap.getWeight();
			
			if(weightSum >= randomWeight)
			{
				return adMap;
			}
		}
		
		// If no ad exists
		return null;
	}

	/**
	 * returns the ads mapped to a given placement
	 * 
	 * @param placementId
	 * @return collection of PlacementAd objects mapped to placement
	 */
	protected Collection<PlacementAdMapping> getAdsForPlacement(int placementId) {
		//
		// In a "real" implementation, this info would probably be read from a
		// DB...
		//
		// For this exercise, feel free to override this for the purpose of
		// testing
		//
		return new ArrayList<PlacementAdMapping>();
	}

}
