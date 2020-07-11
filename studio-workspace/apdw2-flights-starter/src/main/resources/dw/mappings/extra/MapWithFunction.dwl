%dw 2.0

input someInput application/json
input theFunction appplication/json

import dw::core::Strings
fun capitalizeKey(value: String) = Strings::capitalize(value) ++ "Key"
---
{someInput: theFunction(someInput.user)} mapObject ((value, key) ->
    {
      (capitalizeKey(key as String)) : value
    }
  )