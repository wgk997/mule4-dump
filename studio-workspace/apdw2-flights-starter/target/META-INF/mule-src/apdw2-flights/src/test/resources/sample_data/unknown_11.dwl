do {
  ns fl http://fights.training.mulesoft.com/
  ns ns0 http://default.training.mulesoft.com/
  ns pr http://prices.training.mulesoft.com/
  ---
  {
    findFlightResponse 
    @(xmlns:"http://default.acme.com/",
    "xmlns:fl":"http://flights.acme.com/",
    "xmlns:pr":"http://prices.acme.com/"
    )
    : {
        return: 
          {
            fl#airlineName : "American",
            fl#code @(type:"domestic"): "AA203",
            departureDate: "2018/03/20",
            pr#price: "333.50"
          },
          return: 
          {
            fl#airlineName : "United",
            fl#code @(type:"international"): "UA40",
            departureDate: "2018/03/20",
            pr#price: "620.00"
          }
      }
    }
  }
