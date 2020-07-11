%dw 2.0
/* dynamicMappingXML.dwl
 * This is a reusable DataWeave template. 
 * Read this as an encoded string from a Dynamic Evaluate component
 */
import capitalize from dw::core::Strings
output application/xml

ns cust http://amce.com/crm
ns sales http://acme.com/dept/sales
ns org http://acme.com/ldap


fun formatKey(aString: String) = 
capitalize(aString) ++ "Key" 

fun createObjectFromArray( someElement: Array ) =
{
      (formatKey (someElement[0] as String)) : someElement[1]
}
---
result:
createObjectFromArray( payload )
++ 
{
	cust#"user-name" @(dept: vars.dept): uname, 
	sales#oid @(user: vars.uname): orderid,
	org#dept: dept
}



/*
 * This is a reusable DataWeave template. 
 * To use this template, the two variables uname and orderid are supplied.
 * This script will then be dynamically evaluated in-place using a 
 * Dynamic Evaluate component in the flow. 
 * 
 * If this template uses custom DataWeave modules, these modules must be included in the 
 * project so they can be accessed when evaluated by the Dynamic Evaluate component. 
 */