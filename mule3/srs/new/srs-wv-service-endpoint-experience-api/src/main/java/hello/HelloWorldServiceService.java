package hello;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.18
 * 2019-11-14T15:09:30.568-06:00
 * Generated source version: 2.7.18
 * 
 */
@WebServiceClient(name = "HelloWorldServiceService", 
                  wsdlLocation = "file:/Users/asapkota/AnypointStudio6/srs/new/srs-wv-service-endpoint-experience-api/src/main/wsdl/Service.wsdl",
                  targetNamespace = "http://muleesbcxfsoapexpose.huongdanjava.com/") 
public class HelloWorldServiceService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://muleesbcxfsoapexpose.huongdanjava.com/", "HelloWorldServiceService");
    public final static QName HelloWorldServicePort = new QName("http://muleesbcxfsoapexpose.huongdanjava.com/", "HelloWorldServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/Users/asapkota/AnypointStudio6/srs/new/srs-wv-service-endpoint-experience-api/src/main/wsdl/Service.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(HelloWorldServiceService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/Users/asapkota/AnypointStudio6/srs/new/srs-wv-service-endpoint-experience-api/src/main/wsdl/Service.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public HelloWorldServiceService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public HelloWorldServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public HelloWorldServiceService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public HelloWorldServiceService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public HelloWorldServiceService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public HelloWorldServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns HelloWorldService
     */
    @WebEndpoint(name = "HelloWorldServicePort")
    public HelloWorldService getHelloWorldServicePort() {
        return super.getPort(HelloWorldServicePort, HelloWorldService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns HelloWorldService
     */
    @WebEndpoint(name = "HelloWorldServicePort")
    public HelloWorldService getHelloWorldServicePort(WebServiceFeature... features) {
        return super.getPort(HelloWorldServicePort, HelloWorldService.class, features);
    }

}
