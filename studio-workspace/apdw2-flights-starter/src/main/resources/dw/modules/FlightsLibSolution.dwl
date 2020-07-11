%dw 2.0

var exchangeRate = 1.35

fun combineScatterGatherResults( theInput ) =
flatten ( theInput..payload ) orderBy $.price

fun buildXmlObject( objectArray, parentTag ) =
{ 
  (dw::core::Strings::pluralize(parentTag))  : {( 
    objectArray map (element) -> {
      (parentTag): element
    }
  )}
}


fun shuffleObject( anObject, shuffledIndices ) =
do {
/*
     var objectKeys = 
	 anObject mapObject (value, key, index) -> {
		(index as String): key as String
	} 
	pluck ($$ as String)
 */
	//var pluckedKeys = dw::core::Objects::nameSet(anObject) 
	var pluckedKeys = anObject pluck (value, key) -> key as String
	---
	{(
		shuffledIndices map ( shuffledIndex, origIndex ) -> 
		{ (pluckedKeys[shuffledIndex]) : anObject[shuffledIndex] }
	)}
}


fun shuffleObject1( anObject, shuffledIndices ) =
/* 
using( objectKeys = 
	anObject mapObject (value, key, index) -> {
		(index): key
	}
	pluck $
)
*/

using( pluckedKeys = anObject pluck $$ )
(
	{(
		shuffledIndices map ( shuffledIndex, origIndex ) -> 
		{ (pluckedKeys[shuffledIndex]) : anObject[shuffledIndex] }
	)}
)

 fun formatKeys( anyInput ) =
 anyInput match {
 	case is Array -> anyInput map formatKeys($)
 	case is Object -> anyInput mapObject (value, key ) ->  
 		{ (upper(key) ) : formatKeys(value) }
 	case is String -> anyInput 
 	case is Number -> anyInput
 	else -> anyInput
 }

 fun formatDataStructure( anyInput ) =
 anyInput match {
 	case is Null -> ""
 	case is Array -> anyInput map formatDataStructure($)
 	case is Object -> anyInput mapObject (value, key ) ->  
 		{ ( dw::core::Strings::dasherize(key) ) : formatDataStructure(value) }
 	case is String -> lower( anyInput ) 
 	case is Number -> anyInput as String {format: "+#,##0.00;-#"} 
 	else -> anyInput
 }