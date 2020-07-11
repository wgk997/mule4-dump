%dw 2.0
input someElement application/json
import capitalize from dw::core::Strings

fun formatKey(aString: String) = 
capitalize(aString) ++ "Key" 

fun createObjectFromArray( someElement: Array ) =
{
      (formatKey (someElement[0] as String)) : someElement[1]
}

---
createObjectFromArray( someElement )
