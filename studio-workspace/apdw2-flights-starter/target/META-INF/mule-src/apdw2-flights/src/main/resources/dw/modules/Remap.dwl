%dw 2.0

var samplePayload = 
{
  "flights": {
    "flight": {
      "plane-type": null,
      "available-seats": "+40.00",
      "origination": "mua",
      "airline-name": "delta",
      "flight-code": "a134ds",
      "departure-date": "apr 11, 2018",
      "destination": {
        "airport-code": "lax",
        "airport-name": "los angeles international airport",
        "city": "los angeles",
        "dst": "a",
        "altitude": "125",
        "longitude": "33.94250107",
        "time-zone": "-8",
        "time-zone": "america/los_angeles"
      },
      "plane-type": null,
      "price": "+750.00"
    },
    "flight": {
      "plane-type": "boeing 747",
      "available-seats": "+18.00",
      "origination": "mua",
      "airline-name": "delta",
      "flight-code": "a1qwer",
      "departure-date": "aug 11, 2018",
      "destination": {
        "airport-code": "lax",
        "airport-name": "los angeles international airport",
        "city": "los angeles",
        "dst": "a",
        "altitude": "125",
        "longitude": "33.94250107",
        "time-zone": "-8",
        "time-zone": "america/los_angeles"
      },
      "plane-type": "boeing 747",
      "price": "+496.00"
    },
    "flight": {
      "plane-type": "boeing 737",
      "available-seats": "+10.00",
      "origination": "mua",
      "airline-name": "delta",
      "flight-code": "a1b2c4",
      "departure-date": "feb 11, 2018",
      "destination": {
        "airport-code": "lax",
        "airport-name": "los angeles international airport",
        "city": "los angeles",
        "dst": "a",
        "altitude": "125",
        "longitude": "33.94250107",
        "time-zone": "-8",
        "time-zone": "america/los_angeles"
      },
      "plane-type": "boeing 737",
      "price": "+199.99"
    },
    "flight": {
      "plane-type": "boeing 777",
      "available-seats": "+0.00",
      "origination": "mua",
      "airline-name": "american",
      "flight-code": "ffee0192",
      "departure-date": "2018-01-20t00:00:00",
      "destination": {
        "airport-code": "lax",
        "airport-name": "los angeles international airport",
        "city": "los angeles",
        "dst": "a",
        "altitude": "125",
        "longitude": "33.94250107",
        "time-zone": "-8",
        "time-zone": "america/los_angeles"
      },
      "plane-type": "boeing 777",
      "price": "+300.00"
    },
    "flight": {
      "plane-type": "boeing 787",
      "available-seats": "+0.00",
      "origination": "mua",
      "airline-name": "american",
      "flight-code": "rree0001",
      "departure-date": "2018-01-20t00:00:00",
      "destination": {
        "airport-code": "lax",
        "airport-name": "los angeles international airport",
        "city": "los angeles",
        "dst": "a",
        "altitude": "125",
        "longitude": "33.94250107",
        "time-zone": "-8",
        "time-zone": "america/los_angeles"
      },
      "plane-type": "boeing 787",
      "price": "+541.00"
    },
    "flight": {
      "plane-type": "boeing 777",
      "available-seats": "+100.00",
      "origination": "mua",
      "airline-name": "american",
      "flight-code": "eefd4511",
      "departure-date": "2018-01-15t00:00:00",
      "destination": {
        "airport-code": "lax",
        "airport-name": "los angeles international airport",
        "city": "los angeles",
        "dst": "a",
        "altitude": "125",
        "longitude": "33.94250107",
        "time-zone": "-8",
        "time-zone": "america/los_angeles"
      },
      "plane-type": "boeing 777",
      "price": "+900.00"
    },
    "flight": {
      "plane-type": "boeing 737",
      "available-seats": "+52.00",
      "origination": "mua",
      "airline-name": "united",
      "flight-code": "er45if",
      "departure-date": "2018/02/11",
      "destination": {
        "airport-code": "lax",
        "airport-name": "los angeles international airport",
        "city": "los angeles",
        "dst": "a",
        "altitude": "125",
        "longitude": "33.94250107",
        "time-zone": "-8",
        "time-zone": "america/los_angeles"
      },
      "plane-type": "boeing 737",
      "price": "+345.99"
    },
    "flight": {
      "plane-type": "boeing 777",
      "available-seats": "+12.00",
      "origination": "mua",
      "airline-name": "united",
      "flight-code": "er45jd",
      "departure-date": "2018/04/11",
      "destination": {
        "airport-code": "lax",
        "airport-name": "los angeles international airport",
        "city": "los angeles",
        "dst": "a",
        "altitude": "125",
        "longitude": "33.94250107",
        "time-zone": "-8",
        "time-zone": "america/los_angeles"
      },
      "plane-type": "boeing 777",
      "price": "+346.00"
    },
    "flight": {
      "plane-type": "boeing 707",
      "available-seats": "+0.00",
      "origination": "mua",
      "airline-name": "united",
      "flight-code": "er0945",
      "departure-date": "2018/06/11",
      "destination": {
        "airport-code": "lax",
        "airport-name": "los angeles international airport",
        "city": "los angeles",
        "dst": "a",
        "altitude": "125",
        "longitude": "33.94250107",
        "time-zone": "-8",
        "time-zone": "america/los_angeles"
      },
      "plane-type": "boeing 707",
      "price": "+423.00"
    }
  }
}

/*
 * These are examples of lookupList objects. Normally you provide these lookupList objects in the
 * DataWeave code that is calling Remap::remapElements. You should not edit these examples here. 
 * A typical 
 */

var lookupRemapArrays = {
	 //flight: ["FLIGHT", null],
	 //city: ["arrivalCity", "Los Angeles"],
	 city: ["arrivalCity", null]
}

var lookupRemapArraysAndDefaults = {
	 //flight: ["FLIGHT", null],
	 //city: ["arrivalCity", "Los Angeles", "ORIG"],
	 //city: ["arrivalCity", null, ""],
	 //  city: ["arrivalCity", null, "ORIGINAL_VALUE"]
	 "plane-type": ["planeType", null, "NO_PLANE_ASSIGNED_YET"]
}

var lookupRemapObjects = {
	 city: {newmatchedKey:"arrivalCity", newValue: null}, //null says don't change the value
	 "plane-type": {newKey:"planeType", newValue: null} //null says don't change the value


}

var lookupRemapObjectsAndDefaults = {
	 city: {newKey:"arrivalCity", newValue: "Los Angeles", defaultValue: "ORIGINAL_VALUE"},
	 "plane-type": {newKey:"planeType", newValue: null, defaultValue: "NO_PLANE_ASSIGNED_YET"},
	 //city: {newKey:"arrivalCity", newValue: null, defaultValue: ""},
	 //city: {newKey:"arrivalCity", newValue: null, defaultValue: "ORIGINAL_VALUE"}
}


var lookupRemapFunctions = { 
	 city: {newKey:(k,v) -> lower(k), newValue: (k,v) -> "Arrival " ++ (k) ++ ": " ++ v, defaultValue: (k,v) -> lower(v)}
}

var lookupRemapFunctionsAndDefaults = { 
	 city: {newKey:(k,v) -> lower(k), newValue: (k,v) -> "Arrival " ++ (k) ++ ": " ++ v, defaultValue: (k,v) -> lower(v)}
}

/*
 * This is the Controller function that links together a lookupList object with a 
 * corresponding remapLogic function.
 * 
 * Example usage:
 *   Remap::remapElements( inputData, lookupList, Remap::remapWithObjectsAndDefaults)
 * 
 *   This example iterates over inputData and applies the remapWithObjectsAndDefaults function to every key/value pair. 
 *   The lookupList should be local to the script with this code line. 
 * 
 * The remapLogic function must be written to understand the particuilar strategy used by the lookupList.
 * This Remap module provides the following strategies with corresponding pairs of lookupList/remapLogic functions:
 *   - lookupWithArrays / remapWithArrays : 
 *     Logic to remap each key/value is stored by key name in an array. 
 *     -  Index 0 of the array stores a literal or expression to create the new key.
 *     -  Index 1 of the array stores a literal or expression to create the new value.
 *        If newValue is null, the original value is used. 
 *     For example:
 *     { "originiation": ["departureAirport", "Mule Airport"] }
 * 
 *   - lookupWithArraysAndDefaults / remapWithArraysAndDefaults : 
 *     Logic to remap each key/value plus a default value is stored by key name in an array. 
 *     -  Index 0 of the array stores a literal or expression to create the new key.
 *     -  Index 1 of the array stores a literal or expression to create the new value.
 *        If the new value at this index is null, the original value is tested with the default operator. 
 *        If default is true, then defaultValue is used. 
 *     -  (Optional) Index 2 of the array can store a literal or expression to create a default value.
 *  	      The default value is used if array index 1 is null and the old value is also null (or is true for the default operator).
 *        If the 3rd default value element is not included in the array, and both newValue and the old value are null, then the new value for that remapped elemnt is set to null. 
 *     For example:
 *     { 
 *       "originiation": ["departureAirport", "Mule Airport"] ,
 *       "plane-type" : ["aircraftType", null, "NO_PLANE_ASSIGNED_YET"]
 *     }
 * 
 *   - lookupWithObjects / remapWithObjects : 
 *     Logic to remap each key/value is stored by key name in an object. 
 *     -  newKey stores a literal or expression to create the new key.
 *     -  newValue stores a literal or expression to create the new value.
 *        If newValue is null, the original value is passed through, unchanged.  
 *     For example:
 *     { "originiation": {newKey: "departureAirport", newValue: "Mule Airport"} }
 * 
 *   - lookupWithObjectsAndDefaults / remapWithArraysAndDefaults : 
 *    Logic to remap each key/value is stored by key name in an object. 
 *     -  newKey stores a literal or expression to create the new key.
 *     -  newValue stores a literal or expression to create the new value.
 *        If newValue is null, the original value is tested with the default operator. If default is true, then defaultValue is used. 
 *     -  (Optional) defaultValue can store a literal or expression to create a default value.
 *  	      The defaultValue is used if newValue is null and the old value is also null (or is true for the default operator).
 *        If defaultValue is not included in the remapping object, and both newValue and the old value are null, then the new value for that remapped element is set to null. 
 *     For example:
 *     { 
 *       "originiation": {newKey: "departureAirport", newValue: "Mule Airport", defaultValue: null} ,
 *       "plane-type" : {newKey: "aircraftType", newValue: null, defaultValue: "NO_PLANE_ASSIGNED_YET"}
 *     }
 * 
 *   - lookupWithFunctions / remapWithFunctions : 
 *     Logic to remap each key/value is stored by key name in an object, 
 *     where the values for the new key and value can depend on the old key and value.
 *     The remapElements function calls remapWithFunctions with the oldKey and oldValue,
 *     and remapWithFunctions in turn calls each function in lookupWithFunctions with the oldKey and oldValue. 
 *     -  newKey stores a function or lambda expression to create the new key.
 *     -  newValue stores a function or lambda expression to create the new value.
 *        If newValue is null, the original value is passed through, unchanged.  
 *     For example:
 *     {
 *       "city": { newKey: createNewCityKey, newValue: (oldKey, oldValue) -> upper(oldValue) },
 *       "plane-type": { newKey: (oldKey, oldValue) -> dw::core::Strings::capitalize(oldKey), newValue: (oldKey, oldValue) -> null } 
 *     }
 *     This example calls createNewCityKey(oldKey, oldValue) to create the new key, and converts oldValue to uppercase. 
 *     The key "plane-type" is transformed to "Plane Type", and the oldValue is passed through unchanged, because 
 *
 *   - lookupWithFunctionsAndDefaults / remapWithFunctionsAndDefaults : 
 *     Logic to remap each key/value is stored by key name in an object, 
 *     where the values for the new key and value can be constructed from the oldKey and oldValue paramters. 
 *     The remapElements function calls remapWithFunctionsAndDefaults with the oldKey and oldValue,
 *     and remapWithFunctionsAndDefaults in turn calls each function in lookupWithFunctionsAndDefaults with the oldKey and oldValue.  
 *     -  newKey stores a function or lambda expression to create the new key, with the oldKey and oldValue parameters available to the function or expression.
*     -  newValue stores a function or lambda expression to create the new value.
 *        If newValue is null, the original value is tested with the default operator. If default is true, then defaultValue is used. 
 *     -  (Optional) defaultValue can store a literal or expression to create a default value.
 *  	      The defaultValue is used if newValue is null and the old value is also null (or is true for the default operator).
 *        If defaultValue is not included in the remapping object, and both newValue and the old value are null, then the new value for that remapped element is set to null. 
 
 *     For example:
 *     {
 *       "city": { newKey: createNewCityKey, newValue: (oldKey, oldValue) -> upper(oldValue) },
 *       "plane-type": { newKey: (oldKey, oldValue) -> dw::core::Strings::capitalize(oldKey), newValue: (oldKey, oldValue) -> null, defaultValue: (oldKey oldValue) -> "NO_PLANE_ASSIGNED_YET" } 
 *     }
 *     This example calls createNewCityKey(oldKey, oldValue) to create the new key, and converts oldValue to uppercase. 
 *     The key "plane-type" is transformed to "Plane Type", and the oldValue is is tested with the default operator. If the default operator is ture, the defaultValue(oldKey,oldValue) function or lambda expression 
 *     is used to create the default value to used for the new value. 
 */
fun remapElements( anInput, lookupList : Object, remapLogic ) =
anInput match {
	
	// Recurse into each array element
	case anArray is Array -> anArray map( (element) -> remapElements(anInput)) 
	
	// Call remapLogic on each key/value of the object
	case anObject is Object -> anObject mapObject ( 
		(oldValue, oldKey) -> remapLogic(oldValue, oldKey, lookupList, remapElements)
	)
	
	else -> anInput
}

/*
 * This is a remapping logic function that can be passed to remapElements function. 
 * This function must be combined with a lookupList object with the correct structure. 
 * Remap::lookupWithArrays provides an example lookupList with the correct structure. 
 * See the comments for remapElements to see how to write the lookupList for this remappign logic function, and the expected behavior. 
 */
fun remapWithArrays(oldValue: Any,oldKey: Key,lookupList: Object, remapElements) =
oldKey match {
		case matchedKey if lookupList[(matchedKey)]? -> 
		{  
			(lookupList[matchedKey][0]) : 
			lookupList[matchedKey][1] default oldValue //only change the value if the lookup value is not null
		}	
		else -> 	(oldKey): remapElements(oldValue, lookupList, remapWithArrays) //Call back the remapElements function on child values
	}	

/*
 * This is a remapping logic function that can be passed to remapElements function. 
 * This function must be combined with a lookupList object with the correct structure. 
 * Remap::lookupWithArraysWithDefaults provides an example lookupList with the correct structure. 
 * See the comments for remapElements to see how to write the lookupList for this remappign logic function, and the expected behavior. 
 */
fun remapWithArraysAndDefaults(oldValue: Any,oldKey: Key,lookupList: Object, remapElements) =
oldKey match {
		case matchedKey if lookupList[(matchedKey)]? -> 
		{  
			(lookupList[matchedKey][0]) : 
			lookupList[matchedKey][1] match {
				case is Null ->  oldValue default lookupList[matchedKey][2]
				else -> $ //Use the non-null new value
				}
			
		}	
		else -> 	(oldKey): remapElements(oldValue, lookupList, remapWithArraysAndDefaults) //Call back the remapElements function on child values
	}	
	
	
/*
 * This is a remapping logic function that can be passed to remapElements function. 
 * This function must be combined with a lookupList object with the correct structure. 
 * Remap::lookupWithObjects provides an example lookupList with the correct structure. 
 * See the comments for remapElements to see how to write the lookupList for this remappign logic function, and the expected behavior. 
 */	
fun remapWithObjects(oldValue: Any,oldKey: Key,lookupList: Object, remapElements) =
oldKey match {
		case matchedKey if lookupList[(matchedKey)]? -> 
		{  
			(lookupList[matchedKey].newKey) : //only changes from remapWithArrays is these two lines
			lookupList[matchedKey].newValue default oldValue //Only remap value if the lookup value is not null
		}	
		else -> 	(oldKey): remapElements(oldValue, lookupList, remapWithObjects) //Call back the remapElements function on child values
	}	

/*
 * This is a remapping logic function that can be passed to remapElements function. 
 * This function must be combined with a lookupList object with the correct structure. 
 * Remap::lookupWithObjectsAndDefaults provides an example lookupList with the correct structure. 
 * See the comments for remapElements to see how to write the lookupList for this remappign logic function, and the expected behavior. 
 */	
fun remapWithObjectsAndDefaults(oldValue: Any,oldKey: Key,lookupList: Object, remapElements) =
oldKey match {
		case matchedKey if lookupList[(matchedKey)]? -> 
		{  
			(lookupList[matchedKey].newKey) : 
			lookupList[matchedKey].newValue match {
				case is Null ->  oldValue default lookupList[matchedKey].defaultValue
				else -> $ //Use the non-null new value
			}
		}	
		else -> 	(oldKey): remapElements(oldValue, lookupList, remapWithObjectsAndDefaults) //Call back the remapElements function on child values
	}	
	
/*
 * This is a remapping logic function that can be passed to remapElements function. 
 * This function must be combined with a lookupList object with the correct structure. 
 * Remap::lookupWithFunctions provides an example lookupList with the correct structure. 
 * See the comments for remapElements to see how to write the lookupList for this remappign logic function, and the expected behavior. 
 */	
fun remapWithFunctions(oldValue: Any,oldKey: Key,lookupList: Object, remapElements) =
oldKey match {
		case matchedKey if lookupList[(matchedKey)]? -> 
		{  
			(lookupList[matchedKey].newKey(oldKey,oldValue) ) : 
			lookupList[matchedKey].newValue(oldKey, oldValue) default oldValue
		}	
		else -> 	(oldKey): remapElements(oldValue, lookupList, remapWithFunctions) //Call back the remapElements function on child values
	}	

/*
 * This is a remapping logic function that can be passed to remapElements function. 
 * This function must be combined with a lookupList object with the correct structure. 
 * Remap::lookupWithFunctions provides an example lookupList with the correct structure. 
 * See the comments for remapElements to see how to write the lookupList for this remappign logic function, and the expected behavior. 
 */	
fun remapWithFunctionsAndDefaults(oldValue: Any,oldKey: Key,lookupList: Object, remapElements) =
oldKey match {
		case matchedKey if lookupList[(matchedKey)]? -> 
		{  
			(lookupList[matchedKey].newKey(oldKey,oldValue) ) : 
			lookupList[matchedKey].newValue(oldKey, oldValue) match {
				case is Null ->  oldValue default lookupList[matchedKey].defaultValue(oldKey,oldValue)
				else -> $ //Use the non-null new value
			}
		}	
		else -> 	(oldKey): remapElements(oldValue, lookupList, remapWithFunctionsAndDefaults) //Call back the remapElements function on child values
	}
	
	
	
