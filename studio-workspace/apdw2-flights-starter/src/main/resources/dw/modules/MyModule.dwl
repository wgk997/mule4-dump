%dw 2.0
import dw::core::Strings 

type Currency = String {format: "###.00"}

var exchangeRate = 1.35

fun capitalizeKey(aKey : String) = Strings::capitalize( aKey ) ++ "Key" 
 
fun myFunction (anInput) =
if( anInput is Array ) "Array"
else if ( anInput is Object ) "Object"
else anInput