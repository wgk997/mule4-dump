%dw 2.0
/*dynamicMappingJSON.dwl
 * This is a reusable DataWeave template. 
 * Read this as an encoded string from a Dynamic Evaluate component
 */
import capitalize from dw::core::Strings
output application/json

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
	"user:user-name" : uname, 
	"sales:oid-$(vars.uname)" : orderid,
	"org:dept": dept,
	namespaces: {
		user: "http://acme.com/crm",
		org: "http://acme.com/ldap/groups",
		sales: "http://acme.com/dept/sales"
	}
}



