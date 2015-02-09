Ad Selector
===========

#### Given
* Different types of ad with a _unique id_ (ad_id)
* Different types of ad placeholders depending on its shape and size, each having unique _placement id_ 
* A mapping of _placement id, ad id, weight_ where weight represents the priority of that particular ad for corresponding placeholder.

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
1. Look up all of the eligible ads, mapped to the placement_id ( [getAdsForPlacement()](https://github.com/rohitkandhal/AdSelection/blob/master/src/controller/AdCallProcessor.java#L67) )  
2. Choose an ad based on relative weight ( [chooseAd()](https://github.com/rohitkandhal/AdSelection/blob/master/src/controller/AdCallProcessor.java#L29) )

#### Use Cases 
1. If the ad call is for p=1, then ad 100 is always shown.
2. If the ad call is for p=2, then ad 100, 101, or 102 is chosen, with frequency ratio `10:20:20` i.e. ad 100 is chosen 20% of the time, ad 102 is chosen 40% of the time.
3. If the ad call is for p=3, then ad 100 or 103 is chosen, with frequency ratio `15:5` ( i.e. ad 103 is choose 25% of the time )
