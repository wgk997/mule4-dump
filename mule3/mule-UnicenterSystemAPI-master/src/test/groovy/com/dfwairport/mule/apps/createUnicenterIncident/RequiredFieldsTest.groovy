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
import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat

@RunWith(MuleGroovyParameterizedRunner)
class RequiredFieldsTest extends
        BaseJunitTest implements
        SharedFlowPropertyStuff {
    private final String description, summary, category, expectedOutput, firstErrorContext, secondErrorContext
    enum ServiceType {
        REQUEST,
        INCIDENT,
        CHANGEORDER,
        ONLINEFORMSCHANGEORDER
    }
    private final ServiceType serviceType

    RequiredFieldsTest(String ignoredDescription,
                     ServiceType serviceType,
                     String description,
                     String summary,
                     String category,
                     String expectedOutput,
                     String firstErrorContext,
                     String secondErrorContext) {
            this.serviceType = serviceType
            this.description = description
            this.summary = summary
            this.category = category
            this.expectedOutput = expectedOutput
            this.firstErrorContext = firstErrorContext
            this.secondErrorContext = secondErrorContext
        }

    @Parameterized.Parameters(name = "{0}")
    // these long texts are not DRY'ed now, because these messages can be completely different. Waiting for Kasey's final words.
    static Collection<Object[]> data() {
        [
                [
                        'INCIDENT - no incidentArea',
                        ServiceType.INCIDENT,
                        null,
                        null,
                        null,
                        "This Incident was not created, the category pcat value is invalid as  this Create Incident process will stop here. Please check the EM console for more details."
                ,
                "Required fields are missing: [incidentArea]",
                "<strong>Exception context:</strong> affected end user: no affected end user, incident area: no incident area"],
                [
                        'INCIDENT - invalid incidentArea',
                        ServiceType.INCIDENT,
                        null,
                        null,
                        'foobar',
                        "This Incident was not created, the category pcat value is invalid as foobar this Create Incident process will stop here. Please check the EM console for more details.",
                        "Required fields are missing: [incidentArea]",
                        "<strong>Exception context:</strong> affected end user: no affected end user, incident area: foobar"
                ],
                [
                        'CHANGEORDER - no category',
                        ServiceType.CHANGEORDER,
                        null,
                        null,
                        null,
                        "This change order was not created. The following required fields are missing or invalid: [category]. This process will stop here. Please check the console for more details.",
                        "Required fields are missing: [category]",
                        "<strong>Exception context:</strong> requester: no requester, affected end user: no affected end user, request category: no request category"
                ],
                [
                        'CHANGEORDER - invalid category',
                        ServiceType.CHANGEORDER,
                        null,
                        null,
                        'foobar',
                        "This change order was not created. The following required fields are missing or invalid: [category]. This process will stop here. Please check the console for more details.",
                        "Required fields are missing: [category]",
                        "<strong>Exception context:</strong> requester: no requester, affected end user: no affected end user, request category: foobar"
                ],
                [
                        'REQUEST - no summary',
                        ServiceType.REQUEST,
                        'sample description',
                        null,
                        'pcat:123',
                        "This request was not created. The following required fields are missing or invalid: [summary]. This process will stop here. Please check the console for more details.",
                        "Required fields are missing: [summary]",
                        "<strong>Exception context:</strong> requester: no requester, affected end user: no affected end user, request category: pcat:123"
                ],
                [
                        'REQUEST - no description',
                        ServiceType.REQUEST,
                        null,
                        'sample summary',
                        'pcat:123',
                        "This request was not created. The following required fields are missing or invalid: [description]. This process will stop here. Please check the console for more details.",
                        "Required fields are missing: [description]",
                        "<strong>Exception context:</strong> requester: no requester, affected end user: no affected end user, request category: pcat:123"
                ],
                [
                        'REQUEST - all missing',
                        ServiceType.REQUEST,
                        null,
                        null,
                        null,
                        "This request was not created. The following required fields are missing or invalid: [description,summary,category]. This process will stop here. Please check the console for more details.",
                        "Required fields are missing: [description, summary, category]",
                        "<strong>Exception context:</strong> requester: no requester, affected end user: no affected end user, request category: no request category"
                ],
                [
                        'ONLINEFORMSCHANGEORDER - invalid category',
                        ServiceType.ONLINEFORMSCHANGEORDER,
                        null,
                        null,
                        'foobar',
                        "This change order was not created. The following required fields are missing or invalid: [description,summary,category]. This process will stop here. Please check the console for more details.",
                        "Required fields are missing: [description, summary, category]",
                        "<strong>Exception context:</strong> requester: no requester, affected end user: no affected end user, request category: foobar"
                ],
                [
                        'ONLINEFORMSCHANGEORDER - no summary',
                        ServiceType.ONLINEFORMSCHANGEORDER,
                        'sample description',
                        null,
                        'chgcat:123',
                        "This change order was not created. The following required fields are missing or invalid: [summary]. This process will stop here. Please check the console for more details.",
                        "Required fields are missing: [summary]",
                        "<strong>Exception context:</strong> requester: no requester, affected end user: no affected end user, request category: chgcat:123"
                ],
                [
                        'ONLINEFORMSCHANGEORDER - no description',
                        ServiceType.ONLINEFORMSCHANGEORDER,
                        null,
                        'sample summary',
                        'chgcat:123',
                        "This change order was not created. The following required fields are missing or invalid: [description]. This process will stop here. Please check the console for more details.",
                        "Required fields are missing: [description]",
                        "<strong>Exception context:</strong> requester: no requester, affected end user: no affected end user, request category: chgcat:123"
                ],
                [
                        'ONLINEFORMSCHANGEORDER - all missing',
                        ServiceType.ONLINEFORMSCHANGEORDER,
                        null,
                        null,
                        null,
                        "This change order was not created. The following required fields are missing or invalid: [description,summary,category]. This process will stop here. Please check the console for more details.",
                        "Required fields are missing: [description, summary, category]",
                        "<strong>Exception context:</strong> requester: no requester, affected end user: no affected end user, request category: no request category"
                ]
        ].collect { list ->
            // JUnit expects object arrays not lists, so we convert each line above here
            list.toArray(new Object[0])
        }
    }

    @Test
    void test() {
        def input = null
        switch (this.serviceType) {
            case ServiceType.INCIDENT:
                input = new CreateUnicenterIncidentProcessRequest().with {
                    it.incidentArea = this.category
                    it
                }
                runTheFlow(input, CreateUnicenterIncidentProcessResponse)
                break
            case ServiceType.REQUEST:
                input = new CreateUnicenterRequestProcessRequest().with {
                    it.category = this.category
                    it.description = this.description
                    it.summary = this.summary
                    it
                }
                runTheFlow(input, CreateUnicenterRequestProcessResponse)
                break
            case ServiceType.CHANGEORDER:
                input = new CreateUnicenterChangeOrderProcessRequest().with {
                    it.category = this.category
                    it
                }
                runTheFlow(input, CreateUnicenterChangeOrderProcessResponse)
                break
            case ServiceType.ONLINEFORMSCHANGEORDER:
                input = new CreateOnlineFormsChangeOrderProcessRequest().with {
                    it.category = this.category
                    it.description = this.description
                    it.summary = this.summary
                    it
                }
                runTheFlow(input, CreateOnlineFormsChangeOrderProcessResponse)
                break
            default:
                throw new Exception("Unknown service type ${this.serviceType}")
        }
    }

    def <T> T runTheFlow(input, Class<T> returnType) {
        String flowName
        switch (this.serviceType) {
            case ServiceType.INCIDENT:
                flowName = FLOW_NAME_INCIDENT
                break
            case ServiceType.REQUEST:
                flowName = FLOW_NAME_REQUEST
                break
            case ServiceType.CHANGEORDER:
                flowName = FLOW_NAME_CHANGEORDER
                break
            case ServiceType.ONLINEFORMSCHANGEORDER:
                flowName = FLOW_NAME_ONLINEFORMSCHANGEORDER
                break
            default:
                throw new Exception("Unknown service type ${this.serviceType}")
        }
        def logger = getLogger()
        mockLoginWithResponse()
        mockGetHandleForUserIdWithResponse()

        def emailPayloads = []
        mockGeneric('Send exception via SMTP') {
            raw {
                whenCalledWith { payloadDuringEmail ->
                    logger.info 'Email mock triggered'
                    emailPayloads << payloadDuringEmail
                }
            }
        }

        mockLogout()
        // act
        logger.info 'running flow {} now',
                flowName
        def response = runFlow(flowName) {
            soap {
                inputJaxbPayload(input)
            }
        } as T

        // assert
        assertThat 'Expected to send 1 email for the first failure and then quit processing',
                emailPayloads.size(),
                is(equalTo(1))
        def emailPayload = emailPayloads[0]
        assertThat emailPayload,
                is(containsString('<html>'))
        assertThat emailPayload,
                is(containsString(this.firstErrorContext))
        assertThat emailPayload,
                is(containsString(this.secondErrorContext))

        response.with {
            assertThat it.result, is(equalTo('Error'))
            assertThat it.desc, is(equalTo(this.expectedOutput))
            it
        }

    }
}
