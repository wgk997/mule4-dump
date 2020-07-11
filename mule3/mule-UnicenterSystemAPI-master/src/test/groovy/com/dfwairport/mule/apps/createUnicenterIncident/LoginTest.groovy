package com.dfwairport.mule.apps.createUnicenterIncident

import com.avioconsulting.mule.testing.junit.BaseJunitTest
import com.ca.unicenterserviceplus.servicedesk.GetHandleForUserid
import com.ca.unicenterserviceplus.servicedesk.GetHandleForUseridResponse
import com.ca.unicenterserviceplus.servicedesk.Login
import com.ca.unicenterserviceplus.servicedesk.LoginResponse
import com.oracle.xmlns.createonlineformschangeorder.CreateOnlineFormsChangeOrderProcessRequest
import com.oracle.xmlns.createonlineformschangeorder.CreateOnlineFormsChangeOrderProcessResponse
import com.oracle.xmlns.createunicenterchangeorder.CreateUnicenterChangeOrderProcessRequest
import com.oracle.xmlns.createunicenterchangeorder.CreateUnicenterChangeOrderProcessResponse
import com.oracle.xmlns.createunicenterincident.CreateUnicenterIncidentProcessRequest
import com.oracle.xmlns.createunicenterincident.CreateUnicenterIncidentProcessResponse
import com.oracle.xmlns.createunicenterrequest.CreateUnicenterRequestProcessRequest
import com.oracle.xmlns.createunicenterrequest.CreateUnicenterRequestProcessResponse
import groovy.util.logging.Log4j2
import groovy.xml.DOMBuilder
import org.junit.Test
import org.mule.api.MuleEvent

import javax.xml.namespace.QName

import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat

@Log4j2
class LoginTest extends
        BaseJunitTest implements
        SharedFlowPropertyStuff {
    @Test
    void correct_session_id_request() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest()

        Login actualUnicenterRequest = null
        def logger = log
        mockSoapCall('Unicenter Login') {
            whenCalledWithJaxb(Login) { Login unicenterRequest ->
                logger.info 'Mock of Unicenter Login called'
                actualUnicenterRequest = unicenterRequest
                def loginResponse = new LoginResponse()
                loginResponse.SID = 22
                return loginResponse
            }
        }

        mockConnectorInvocationAndReturnEmptyOutput('GetHandleForUserid')

        // act
        log.info 'running flow now'
        runFlow(LOGIN_ONLY_INCIDENT) {
            soap {
                inputJaxbPayload(input)
            }
        }

        //assert
        assertThat actualUnicenterRequest.username, is(equalTo('sample_username'))
        assertThat actualUnicenterRequest.password, is(equalTo('sample_password'))
    }

    @Test
    void soap_fault_login_proper_email_sent_out_incident() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest().with {
            affectedEndUser = 'sandesh'
            incidentArea = 'pcat:123'
            instanceID = 'instance123'
            it
        }
        soap_fault_login_proper_email_sent_out(input, CreateUnicenterIncidentProcessResponse)
    }

    @Test
    void soap_fault_login_proper_email_sent_out_request() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            category = '1234'
            instanceID = 'instance123'
            it
        }
        soap_fault_login_proper_email_sent_out(input, CreateUnicenterRequestProcessResponse)
    }

    @Test
    void soap_fault_login_proper_email_sent_out_change_order() {
        // arrange
        def input = new CreateUnicenterChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            category = '1234'
            instanceID = 'instance123'
            it
        }
        soap_fault_login_proper_email_sent_out(input, CreateUnicenterChangeOrderProcessResponse)
    }

    @Test
    void soap_fault_login_proper_email_sent_out_online_forms_change_order() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            category = '1234'
            instanceID = 'instance123'
            it
        }
        soap_fault_login_proper_email_sent_out(input, CreateOnlineFormsChangeOrderProcessResponse)
    }

    @Test
    void soap_fault_on_logged_in_handle_proper_email_sent_out_incident() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest().with {
            affectedEndUser = 'sandesh'
            incidentArea = 'pcat:123'
            it
        }
        soap_fault_on_logged_in_handle_proper_email_sent_out(input)
    }

    @Test
    void soap_fault_on_logged_in_handle_proper_email_sent_out_request() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            category = '1234'
            it
        }
        soap_fault_on_logged_in_handle_proper_email_sent_out(input)
    }

    @Test
    void soap_fault_on_logged_in_handle_proper_email_sent_out_change_order() {
        // arrange
        def input = new CreateUnicenterChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            category = '1234'
            it
        }
        soap_fault_on_logged_in_handle_proper_email_sent_out(input)
    }

    @Test
    void soap_fault_on_logged_in_handle_proper_email_sent_out_online_forms_change_order() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            category = '1234'
            it
        }
        soap_fault_on_logged_in_handle_proper_email_sent_out(input)
    }

    void soap_fault_on_affected_user_handle_proper_email_sent_out_helper(input,
                                                                         String flowName) {

        def logger = log
        mockLoginWithResponse()

        def emailPayloads = []
        mockGeneric('Send exception via SMTP') {
            raw {
                whenCalledWith { payloadDuringEmail ->
                    logger.info 'Email mock triggered'
                    emailPayloads << payloadDuringEmail
                }
            }
        }

        def loggedInUserDone = false
        mockSoapCall('GetHandleForUserid') {
            whenCalledWithJaxb(GetHandleForUserid) { createRequest ->
                if (loggedInUserDone) {
                    logger.info "Mock of Unicenter GetHandleForUserid, about to fail"
                    soapFault('some failure message',
                            new QName('',
                                    'soapenv:Client'),
                            null) { DOMBuilder detailBuilder ->
                        detailBuilder.detail {
                            ErrorMessage('some failure message')
                            ErrorCode('1003')
                        }
                    }
                } else {
                    loggedInUserDone = true
                    new GetHandleForUseridResponse().with {
                        getHandleForUseridResult = 'the handle'
                        it
                    }
                }
            }
        }

        // act
        runFlow(flowName) {
            soap {
                inputJaxbPayload(input)
            }
        }

        //request type and change order type have same context string
        def context_string = (flowName == FLOW_NAME_INCIDENT) ?
                '<strong>Exception context:</strong> affected end user: affectedsandesh, incident area: pcat:123' :
                '<strong>Exception context:</strong> requester: requestersandesh, affected end user: affectedsandesh, request category: not simulation'

        // assert
        assertThat 'Expected to send 1 email for the first failure and then quit processing',
                emailPayloads.size(),
                is(equalTo(1))
        def emailPayload = emailPayloads[0]
        assertThat emailPayload,
                is(containsString('<html>'))
        assertThat emailPayload,
                is(containsString('some failure message'))
        assertThat emailPayload,
                is(containsString(context_string))
    }

    @Test
    void soap_fault_on_affected_user_handle_proper_email_sent_out_incident() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            incidentArea = 'pcat:123'
            it
        }
        soap_fault_on_affected_user_handle_proper_email_sent_out_helper(input, FLOW_NAME_INCIDENT)
    }

    @Test
    void soap_fault_on_affected_user_handle_proper_email_sent_out_request() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            category = 'not simulation'
            it
        }
        soap_fault_on_affected_user_handle_proper_email_sent_out_helper(input, FLOW_NAME_REQUEST)

        input = new CreateUnicenterChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            category = 'not simulation'
            it
        }
        soap_fault_on_affected_user_handle_proper_email_sent_out_helper(input, FLOW_NAME_CHANGEORDER)
    }

    @Test
    void soap_fault_on_affected_user_handle_proper_email_sent_out_change_order() {
        // arrange
        def input = new CreateUnicenterChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            category = 'not simulation'
            it
        }
        soap_fault_on_affected_user_handle_proper_email_sent_out_helper(input, FLOW_NAME_CHANGEORDER)
    }

    @Test
    void soap_fault_on_affected_user_handle_proper_email_sent_out_online_forms_change_order() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            category = 'not simulation'
            it
        }
        soap_fault_on_affected_user_handle_proper_email_sent_out_helper(input, FLOW_NAME_ONLINEFORMSCHANGEORDER)
    }

    @Test
    void soap_fault_on_handle_proper_response_type_incident() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest().with {
            affectedEndUser = 'sandesh'
            incidentArea = 'pcat:123'
            summary = 'the summary'
            description = 'the description'
            instanceID = 'instance123'
            it.incidentPropertiesGeneric = new CreateUnicenterIncidentProcessRequest.IncidentPropertiesGeneric().with {
                incidentProp1Key = 'foo'
                incidentProp1Value = 'bar'
                it
            }
            it
        }

        def logger = log
        mockUnicenterFailure(Login,
                             'Unicenter Login',
                             'some failure message')

        def emailPayloads = []
        mockGeneric('Send exception via SMTP') {
            raw {
                whenCalledWith { payloadDuringEmail ->
                    logger.info 'Email mock triggered'
                    emailPayloads << payloadDuringEmail
                }
            }
        }

        // act
        def result = runSoapApikitFlow('process', 'api-main-incident') {
            inputJaxbPayload(input)
        } as MuleEvent

        // assert
        def stringResponse = result.messageAsString
        log.info 'Response {}',
                 stringResponse
        assertThat stringResponse,
                   is(containsString('Error'))
        assertThat stringResponse,
                   is(containsString('This request was not created due to the fault exception: some failure message.; The instance ID is: instance123, please check the console for more details.'))
    }

    @Test
    void soap_fault_on_handle_proper_response_type_request() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            assignee = 'assigneesandesh'
            summary = 'summary'
            description = 'description'
            category = 'category'
            group = 'group'
            impact = 'medium'
            priority = 'high'
            severity = 'low'
            urgency = 'medium'
            instanceID = 'instance123'
            it
        }

        def request_properties = input.requestProperties = new CreateUnicenterRequestProcessRequest.RequestProperties()
        (1..50).each { index ->
            request_properties."setQ${index}"("the value")
            request_properties."setA${index}"("the value")
        }

        def logger = log
        mockUnicenterFailure(Login,
                             'Unicenter Login',
                             'some failure message')

        def emailPayloads = []
        mockGeneric('Send exception via SMTP') {
            raw {
                whenCalledWith { payloadDuringEmail ->
                    logger.info 'Email mock triggered'
                    emailPayloads << payloadDuringEmail
                }
            }
        }

        // act
        def result = runSoapApikitFlow('process', 'api-main-request') {
            inputJaxbPayload(input)
        } as MuleEvent

        // assert
        def stringResponse = result.messageAsString
        log.info 'Response {}',
                 stringResponse
        assertThat stringResponse,
                is(containsString('Error'))
        assertThat stringResponse,
                is(containsString('This request was not created due to the fault exception: some failure message.; The instance ID is: instance123, please check the console for more details.'))
    }

    @Test
    void soap_fault_on_handle_proper_response_type_change_order() {
        // arrange
        def input = new CreateUnicenterChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            affectedEndUser = 'affectedsandesh'
            summary = 'summary_val'
            description = 'sample description'
            category = 'sample category'
            justification = 'sample justification'
            instanceID = 'instance123'
            it
        }

        def generic_properties = input.genericProperties = new CreateUnicenterChangeOrderProcessRequest.GenericProperties()
        (1..50).each { index ->
            generic_properties."setQ${index}"("the value")
            generic_properties."setA${index}"("the value")
        }
        def logger = log
        mockUnicenterFailure(Login,
                             'Unicenter Login',
                             'some failure message')

        def emailPayloads = []
        mockGeneric('Send exception via SMTP') {
            raw {
                whenCalledWith { payloadDuringEmail ->
                    logger.info 'Email mock triggered'
                    emailPayloads << payloadDuringEmail
                }
            }
        }

        // act
        def result = runSoapApikitFlow('process', 'api-main-changeOrder') {
            inputJaxbPayload(input)
        } as MuleEvent

        // assert
        def stringResponse = result.messageAsString
        log.info 'Response {}',
                 stringResponse
        assertThat stringResponse,
                is(containsString('Error'))
        assertThat stringResponse,
                is(containsString('This request was not created due to the fault exception: some failure message.; The instance ID is: instance123, please check the console for more details.'))
    }

    @Test
    void soap_fault_on_handle_proper_response_type_online_forms_change_order() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            affectedEndUser = 'affectedsandesh'
            summary = 'summary_val'
            description = 'sample description'
            category = 'chgcat:123'
            justification = 'sample justification'
            instanceID = 'instance123'
            it
        }

        def online_form_queries = input.onlineFormQueries = new CreateOnlineFormsChangeOrderProcessRequest.OnlineFormQueries()
        (1..50).each { index ->
            online_form_queries."setQ${index}"("the value")
            online_form_queries."setA${index}"("the value")
        }
        def logger = log
        mockUnicenterFailure(Login,
                'Unicenter Login',
                'some failure message')

        def emailPayloads = []
        mockGeneric('Send exception via SMTP') {
            raw {
                whenCalledWith { payloadDuringEmail ->
                    logger.info 'Email mock triggered'
                    emailPayloads << payloadDuringEmail
                }
            }
        }

        // act
        def result = runSoapApikitFlow('process', 'api-main-onlineFormsChangeOrder') {
            inputJaxbPayload(input)
        } as MuleEvent

        // assert
        def stringResponse = result.messageAsString
        log.info 'Response {}',
                stringResponse
        assertThat stringResponse,
                is(containsString('Error'))
        assertThat stringResponse,
                is(containsString('This request was not created due to the fault exception: some failure message.; The instance ID is: instance123, please check the console for more details.'))
    }
}
