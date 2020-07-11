%dw 2.0
output application/java
import dw::modules::FlightsLib

var payload = readUrl("classpath://examples/scatterGatherResult.json")


//flatten (payload..payload) orderBy $.price

//Note: This is equivalent to the flatten operation
/*
(payload..payload ) reduce ( (airlineFlights, acc=[]) ->
	// acc ++ airlineFlights
	acc ++ airlineFlights as Array
)
*/
---
/* 
flights: ( 
	flatten(payload..flights) //orderBy $.price
)*/
FlightsLib::combineScatterGatherResults( payload )
