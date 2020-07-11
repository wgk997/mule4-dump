package com.dfwairport.mule.apps.createUnicenterIncident

import com.avioconsulting.mule.testing.junit.BaseJunitTest
import com.ca.unicenterserviceplus.servicedesk.Logout
import com.ca.unicenterserviceplus.servicedesk.LogoutResponse
import com.oracle.xmlns.createunicenterincident.CreateUnicenterIncidentProcessRequest
import groovy.util.logging.Log4j2
import org.junit.Test

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertThat

@Log4j2
class LogoutTest extends
        BaseJunitTest implements
        SharedFlowPropertyStuff {
    @Test
    void correct_sid_request() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest().with{
            incidentArea = "pcat:123"
            it
        }

        def logger = log
        mockLoginWithResponse()
        mockGetHandleForUserIdWithResponse()
        mockCreateRequestWithRequestResponse()

        Logout actualLogoutRequest = null
        mockSoapCall('Unicenter Logout') {
            whenCalledWithJaxb(Logout) { Logout unicenterLogout ->
                logger.info 'Mock of Unicenter Logout called'
                actualLogoutRequest = unicenterLogout
                new LogoutResponse()
            }
        }

        // act
        log.info 'running flow now'
        runFlow(FLOW_NAME_INCIDENT) {
            soap {
                inputJaxbPayload(input)
            }
        }

        //assert
        assertThat actualLogoutRequest.SID, is(equalTo(22))
    }
}
