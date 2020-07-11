%dw 2.0
var exchangeRate = 1.35

fun combineScatterGatherResults( theInput ) =
flights: flatten( theInput..flights )

fun buildXmlObject( objectArray, parentTag ) =
{
	 (dw::core::Strings::pluralize(parentTag)) :
	 {
		 ( objectArray map (element) -> 
		 {	
			 (parentTag): element
		 })
	 }
}
fun buildJsonObject( objectArray: Array<Object>, parentTag: String	) = {(
 objectArray map (element, index) -> {
 ( parentTag ++ index ): element
 }
)}
