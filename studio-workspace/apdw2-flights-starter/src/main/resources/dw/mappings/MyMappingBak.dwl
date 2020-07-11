%dw 2.0

input someObject application/json

import capitalize from dw::core::Strings

fun formatKey(aString: String) = 
capitalize(aString) ++ "Key" 

---
{
      (formatKey (someElement[0] as String)) : someElement[1]
}
