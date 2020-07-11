package com.dfwairport.mule.apps.createUnicenterIncident

import com.avioconsulting.mule.testing.junit.BaseJunitTest
import com.avioconsulting.mule.testing.junit.MuleGroovyParameterizedRunner
import com.oracle.xmlns.createonlineformschangeorder.CreateOnlineFormsChangeOrderProcessRequest
import com.oracle.xmlns.createonlineformschangeorder.CreateOnlineFormsChangeOrderProcessResponse
import com.oracle.xmlns.createunicenterchangeorder.CreateUnicenterChangeOrderProcessRequest
import com.oracle.xmlns.createunicenterchangeorder.CreateUnicenterChangeOrderProcessResponse
import com.oracle.xmlns.createunicenterincident.CreateUnicenterIncidentProcessRequest
import com.oracle.xmlns.createunicenterincident.CreateUnicenterIncidentProcessResponse
import com.oracle.xmlns.createunicenterrequest.CreateUnicenterRequestProcessRequest
import com.oracle.xmlns.createunicenterrequest.CreateUnicenterRequestProcessResponse
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mule.api.MuleEvent

import static groovy.test.GroovyAssert.shouldFail
import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertThat

@RunWith(MuleGroovyParameterizedRunner)
class ValidationTest extends
        BaseJunitTest implements
        SharedFlowPropertyStuff {
    private final String affectedEndUser, category, summary, description, expectedFailureMessage, missingVariable
    enum ServiceType {
        REQUEST,
        INCIDENT,
        CHANGEORDER
    }
    private final ServiceType serviceType

    ValidationTest(String ignoredDescription,
                   ServiceType serviceType,
                   String affectedEndUser,
                   String category,
                   String summary,
                   String description,
                   String expectedFailureMessage,
                   String missingVariable) {
        this.serviceType = serviceType
        this.affectedEndUser = affectedEndUser
        this.category = category
        this.summary = summary
        this.description = description
        this.expectedFailureMessage = expectedFailureMessage
        this.missingVariable = missingVariable
    }

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        [
                ['INCIDENT: affected end user - empty', ServiceType.INCIDENT, '', 'pcat:123', 'the summary', 'the description', null, null],
                ['INCIDENT: affected end user - space', ServiceType.INCIDENT, ' ', 'pcat:123', 'the summary', 'the description', null, null],
                // should pass
                ['INCIDENT: affected end user - none', ServiceType.INCIDENT, null, 'pcat:123', 'the summary', 'the description', null, null],
                // should pass
                ['INCIDENT: affected end user - supplied', ServiceType.INCIDENT, 'someuser', 'pcat:123', 'the summary', 'the description', null, null],
                // should pass
                ['INCIDENT: category starts with pcat', ServiceType.INCIDENT, null, 'pcat:123', 'the summary', 'the description', null, null],
                // should reply with error response
                ['INCIDENT: incident area null', ServiceType.INCIDENT, null, null, 'the summary', 'the description', 'Could not parse the XML stream caused by: javax.xml.stream.XMLStreamException:', 'incidentArea'],
                ['REQUEST: category null', ServiceType.REQUEST, null, null, 'the summary', 'the description', 'Could not parse the XML stream caused by: javax.xml.stream.XMLStreamException:', 'category'],
                ['REQUEST: summary null', ServiceType.REQUEST, null, 'pcat:123', null, 'the description', 'Could not parse the XML stream caused by: javax.xml.stream.XMLStreamException:', 'summary'],
                ['REQUEST: description null', ServiceType.REQUEST, null, 'pcat:123', 'the summary', null, 'Could not parse the XML stream caused by: javax.xml.stream.XMLStreamException:', 'description'],
                ['CHANGEORDER: category null', ServiceType.CHANGEORDER, null, null, 'the summary', 'the description', 'Could not parse the XML stream caused by: javax.xml.stream.XMLStreamException:', 'category']
        ].collect { list ->
            // JUnit expects object arrays not lists, so we convert each line above here
            list.toArray(new Object[0])
        }
    }

    @Test
    void test() {
        // arrange
        def input = null
        def apiKitFlowName = null
        switch (this.serviceType) {
            case ServiceType.INCIDENT:
                input = new CreateUnicenterIncidentProcessRequest().with {
                    it.affectedEndUser = this.affectedEndUser
                    it.incidentArea = this.category
                    it.summary = this.summary
                    it.description = this.description
                    it.instanceID = 'the instance ID'
                    it.incidentPropertiesGeneric = new CreateUnicenterIncidentProcessRequest.IncidentPropertiesGeneric().with {
                        incidentProp1Key = 'foo'
                        incidentProp1Value = 'bar'
                        it
                    }
                    it
                }
                apiKitFlowName = 'api-main-incident'
                break
            case ServiceType.REQUEST:
                input = new CreateUnicenterRequestProcessRequest().with {
                    it.affectedEndUser = this.affectedEndUser
                    it.category = this.category
                    it.summary = this.summary
                    it.description = this.description
                    it
                }
                apiKitFlowName = 'api-main-request'
                break
            case ServiceType.CHANGEORDER:
                input = new CreateUnicenterChangeOrderProcessRequest().with {
                    it.affectedEndUser = this.affectedEndUser
                    it.category = this.category
                    it.summary = this.summary
                    it.description = this.description
                    it
                }
                apiKitFlowName = 'api-main-changeOrder'
                break
            default:
                throw new Exception("Unknown service type ${this.serviceType}")
        }

        mockLoginWithResponse()
        mockGetHandleForUserIdWithResponse()
        mockCreateRequestWithIncidentTypeResponse()
        mockLogout()

        // act
        def result = runSoapApikitFlow('process', apiKitFlowName) {
            inputJaxbPayload(input)
        } as MuleEvent

        if (this.expectedFailureMessage){
            assertThat result.messageAsString,
                    is(containsString(this.expectedFailureMessage))
            assertThat result.messageAsString,
                    is(containsString(this.missingVariable))
        } else {
            assertThat result.messageAsString,
                   is(containsString('<ns0:incidentId>incident:1234</ns0:incidentId>'))
        }
    }
}
