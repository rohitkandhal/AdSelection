Ad Selector
===========

#### Given
* We have different types of ad, each having _unique id_ (ad_id)
* Depending on shape and size of an ad placeholder, we have different _placement id_ 
* In this hypothetical ad selector algorithm, we are given a mapping of _placement id, ad id, weight_

```
placement_id | ad_id | weight
  	1        | 100   |  10
  	2        | 100   |  10
  	2        | 101   |  20
  	2        | 102   |  20
  	3        | 100   |  15
  	3        | 103   |  5
```

#### Ad Selection Algorithm
AdCallProcessor implements the high level logic of processing each request:  
1. Look up all of the eligible ads, mapped to the placement_id ( `getAdsForPlacement()` )  
2. Choose an ad based on relative weight ( `chooseAd()` )

#### Use Cases 
1. If the ad call is for p=1, then ad 100 is always shown.
2. If the ad call is for p=2, then ad 100, 101, or 102 is chosen, with frequency ratio `10:20:20` i.e. ad 100 is chosen 20% of the time, ad 102 is chosen 40% of the time.
3. If the ad call is for p=3, then ad 100 or 103 is chosen, with frequency ratio `15:5` ( i.e. ad 103 is choose 25% of the time )
