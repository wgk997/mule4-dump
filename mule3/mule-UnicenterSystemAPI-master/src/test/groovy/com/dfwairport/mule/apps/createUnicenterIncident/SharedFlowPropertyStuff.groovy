package com.dfwairport.mule.apps.createUnicenterIncident

import com.ca.unicenterserviceplus.servicedesk.*
import com.oracle.xmlns.createonlineformschangeorder.CreateOnlineFormsChangeOrderProcessRequest
import com.oracle.xmlns.createunicenterchangeorder.CreateUnicenterChangeOrderProcessRequest
import com.oracle.xmlns.createunicenterincident.CreateUnicenterIncidentProcessRequest
import com.oracle.xmlns.createunicenterrequest.CreateUnicenterRequestProcessRequest
import groovy.xml.DOMBuilder

import javax.xml.namespace.QName

import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertThat

trait SharedFlowPropertyStuff {
    CreateChangeOrder executeCreateChangeOrder(input) {
        def logger = log
        def flowName = null
        switch (input) {
            case CreateUnicenterChangeOrderProcessRequest:
                flowName = FLOW_NAME_CHANGEORDER
                break
            case CreateOnlineFormsChangeOrderProcessRequest:
                flowName = FLOW_NAME_ONLINEFORMSCHANGEORDER
                break
            default:
                throw new Exception("Invalid input value")
        }
        mockLoginWithResponse()
        mockGetHandleForUserIdWithResponse()

        CreateChangeOrder actualCreateChangeOrder = null
        mockSoapCall('Create Change Order') {
            whenCalledWithJaxb(CreateChangeOrder) { CreateChangeOrder createChangeOrder ->
                logger.info 'Mock of Unicenter Create Change Order called'
                actualCreateChangeOrder = createChangeOrder
                new CreateChangeOrderResponse()
            }
        }

        mockLogout()
        // act
        logger.info 'running flow now'
        runFlow(flowName) {
            soap {
                inputJaxbPayload(input)
            }
        }

        return actualCreateChangeOrder
    }

    CreateRequest executeCreateRequest(input) {
        def logger = log
        def flowName = null
        switch (input) {
            case CreateUnicenterIncidentProcessRequest:
                flowName = FLOW_NAME_INCIDENT
                break
            case CreateUnicenterRequestProcessRequest:
                flowName = FLOW_NAME_REQUEST
                break
            default:
                throw new Exception("Invalid input value")
        }

        mockLoginWithResponse()
        mockGetHandleForUserIdWithResponse()
        mockDoSelectWithResponse()

        CreateRequest actualCreateRequest = null
        mockSoapCall('Create Request') {
            whenCalledWithJaxb(CreateRequest) { CreateRequest createRequest ->
                logger.info 'Mock of Unicenter Create Request called'
                actualCreateRequest = createRequest
                new CreateRequestResponse()
            }
        }

        mockLogout()
        // act
        logger.info 'running flow now'
        runFlow(flowName) {
            soap {
                inputJaxbPayload(input)
            }
        }

        return actualCreateRequest
    }

    Map<String, String> getConfigResourceSubstitutes() {
        [
                // using txt extension because Studio will think bean names collide with implementation
                'global.xml': 'test_version_of_global.txt'
        ]
    }

    final String FLOW_NAME_INCIDENT = 'process:/CreateUnicenterIncident_OSB_Client/CreateUnicenterIncident_OSB_ClientDirectBindingPort11/api-config'
    final String FLOW_NAME_REQUEST = 'process:/CreateUnicenterRequest_OSB_Client/CreateUnicenterRequest_OSB_ClientDirectBindingPort11/api-config'
    final String FLOW_NAME_CHANGEORDER = 'process:/CreateUnicenterChangeOrder_OSB_Client/CreateUnicenterChangeOrder_OSB_ClientDirectBindingPort11/api-config'
    final String FLOW_NAME_ONLINEFORMSCHANGEORDER = 'process:/CreateOnlineFormsChangeOrder_OSB_Client/CreateOnlineFormsChangeOrder_OSB_ClientDirectBindingPort11/api-config'
    final String LOGIN_ONLY_INCIDENT = 'runLoginFlowWithStringOfXmlTypeIncident'
    final String LOGIN_ONLY_REQUEST = 'runLoginFlowWithStringOfXmlTypeRequest'
    final String LOGIN_ONLY_CHANGEORDER = 'runLoginFlowWithStringOfXmlTypeChangeOrder'
    final String LOGIN_ONLY_ONLINEFORMSCHANGEORDER = 'runLoginFlowWithStringOfXmlTypeOnlineFormsChangeOrder'

    // We want to be able to test login.xml flows in isolation from others but we can't do that unless
    // we convert the payload from stream to String first (see CreateUnicenterIncident_v1.xml)
    // we have a test only flow to do that for us
    String getMuleDeployPropertiesResources() {
        super.getMuleDeployPropertiesResources() + ',login_flow_tst.txt, doselect_tst.txt, create_request_tst.txt'
    }

    Object getPropertyMap() {
        // don't want to worry about validation during unit tests
        [
                'validate.soap.requests'          : 'true',
                'unicenter.username'              : 'sample_username',
                'unicenter.password'              : 'sample_password',
                'unicenter.incident.soap.endpoint': 'http://foobar',
                // we assert the email stuff is at least invoked in some of our tests
                'dfw.exception.smtp.server'       : 'foobar',
                'dfw.exception.smtp.port'         : '9999'
        ]
    }

    def mockConnectorInvocationAndReturnEmptyOutput(String connectorName,
                                                    Closure mockInvoked = null) {
        mockSoapCall(connectorName) {
            whenCalledWithGroovyXmlParser { Node noop ->
                if (mockInvoked) {
                    mockInvoked()
                }
                new Node(null, 'root')
            }
        }
    }

    def mockUnicenterFailure(Class requestClass,
                             String connectorName,
                             String message) {
        def logger = log
        mockSoapCall(connectorName) {
            whenCalledWithJaxb(requestClass) { createRequest ->
                logger.info "Mock of Unicenter ${connectorName}, about to fail"
                soapFault(message,
                          new QName('',
                                    'soapenv:Client'),
                          null) { DOMBuilder detailBuilder ->
                    detailBuilder.detail {
                        ErrorMessage(message)
                        ErrorCode('1003')
                    }
                }
            }
        }
    }

    def mockLoginWithResponse() {
        mockSoapCall('Unicenter Login') {
            whenCalledWithJaxb(Login) { Login unicenterRequest ->
                logger.info 'Mock of Unicenter Login called'
                def loginResponse = new LoginResponse()
                loginResponse.SID = 22
                return loginResponse
            }
        }
    }

    def mockGetHandleForUserIdWithResponse() {
        mockSoapCall('GetHandleForUserid') {
            whenCalledWithJaxb(GetHandleForUserid) { GetHandleForUserid request ->
                logger.info "Now getting handle for user ID ${request.userID}"
                def handleResponse = new GetHandleForUseridResponse()
                handleResponse.setGetHandleForUseridResult("the handle for ${request.userID}")
                return handleResponse
            }
        }
    }

    def mockCreateChangeOrderWithResponse() {
        mockSoapCall('Create Change Order') {
            whenCalledWithJaxb(CreateChangeOrder) { CreateChangeOrder createChangeOrder ->
                logger.info 'Mock of Unicenter Create Change Order called'
                new CreateChangeOrderResponse().with {
                    setNewChangeNumber("chgcat:1234")
                    setNewChangeHandle('the handle')
                    it
                }
            }
        }
    }

    def mockCreateRequestWithIncidentTypeResponse() {
        mockSoapCall('Create Request') {
            whenCalledWithJaxb(CreateRequest) { CreateRequest createRequest ->
                logger.info 'Mock of Unicenter Create Request called'
                new CreateRequestResponse().with {
                    setNewRequestNumber("incident:1234")
                    setNewRequestHandle('the handle')
                    it
                }
            }
        }
    }

    def mockCreateRequestWithRequestResponse() {
        mockSoapCall('Create Request') {
            whenCalledWithJaxb(CreateRequest) { CreateRequest createRequest ->
                logger.info 'Mock of Unicenter Create Request called'
                new CreateRequestResponse().with {
                    setNewRequestNumber("request:1234")
                    setNewRequestHandle('the handle')
                    it
                }
            }
        }
    }

    def mockDoSelectWithResponse(String responseFileOverride = null,
                                 Closure captureRequest = null) {
        mockSoapCall('Do Select') {
            whenCalledWithJaxb(DoSelect) { DoSelect doselectRequest ->
                logger.info 'Mock of Do Select called'
                if (captureRequest) {
                    captureRequest(doselectRequest)
                }
                def filename = responseFileOverride ?: {
                    def whereClause = doselectRequest.whereClause
                    if (whereClause.contains('exception value')) {
                        soapFault('Some DoSelect problem with '+whereClause,
                                  new QName('',
                                            'soapenv:Client'),
                                  null) { DOMBuilder detailBuilder ->
                            detailBuilder.detail {
                                ErrorMessage('Some DoSelect problem')
                                ErrorCode('1003')
                            }
                        }
                    }
                    switch (whereClause) {
                        case "sym = 'low'":
                            return 'sample_do_select_low_response.xml'
                        case "sym = 'medium'":
                            return 'sample_do_select_medium_response.xml'
                        case "sym = 'high'":
                            return 'sample_do_select_high_response.xml'
                        case "chg_ref_num = 'CO123'":
                            return 'sample_do_select_co_response.xml'
                        case "chg_ref_num = '123'":
                            return 'sample_do_select_co_response.xml'
                        default:
                            return 'sample_do_select_empty_response.xml'
                    }
                }()
                new File('src/test/resources/' + filename)
            }
        }
    }

    def mockLogout() {
        mockSoapCall('Unicenter Logout') {
            whenCalledWithJaxb(Logout) { Logout unicenterLogout ->
                logger.info 'Mock of Unicenter Logout called'
                new LogoutResponse()
            }
        }
    }

    def <T> T soap_fault_login_proper_email_sent_out(input, Class<T> returnType) {
        // arrange
        def flowName = null
        def logger = log

        def emailString = '<strong>Exception context:</strong> requester: requestersandesh, affected end user: affectedsandesh, request category: 1234'
        switch (input) {
            case CreateUnicenterIncidentProcessRequest:
                flowName = FLOW_NAME_INCIDENT
                emailString = '<strong>Exception context:</strong> affected end user: sandesh, incident area: pcat:123'
                break
            case CreateUnicenterRequestProcessRequest:
                flowName = FLOW_NAME_REQUEST
                break
            case CreateUnicenterChangeOrderProcessRequest:
                flowName = FLOW_NAME_CHANGEORDER
                break
            case CreateOnlineFormsChangeOrderProcessRequest:
                flowName = FLOW_NAME_ONLINEFORMSCHANGEORDER
                break
            default:
                throw new Exception("Invalid input value")
        }

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
                is(containsString('some failure message'))
        assertThat emailPayload,
                is(containsString(emailString))
        response.with {
            assertThat it.result, is(equalTo('Error'))
            assertThat it.desc, is(equalTo('This request was not created due to the fault exception: some failure message.; The instance ID is: instance123, please check the console for more details.'))
            it
        }
    }

    def soap_fault_on_logged_in_handle_proper_email_sent_out(input) {
        // arrange
        def flowName = null
        def logger = log
        def emailString = '<strong>Exception context:</strong> requester: requestersandesh, affected end user: affectedsandesh, request category: 1234'
        switch (input) {
            case CreateUnicenterIncidentProcessRequest:
                flowName = FLOW_NAME_INCIDENT
                emailString = '<strong>Exception context:</strong> affected end user: sandesh, incident area: pcat:123'
                break
            case CreateUnicenterRequestProcessRequest:
                flowName = FLOW_NAME_REQUEST
                break
            case CreateUnicenterChangeOrderProcessRequest:
                flowName = FLOW_NAME_CHANGEORDER
                break
            case CreateOnlineFormsChangeOrderProcessRequest:
                flowName = FLOW_NAME_ONLINEFORMSCHANGEORDER
                break
            default:
                throw new Exception("Invalid input value")
        }

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

        mockUnicenterFailure(GetHandleForUserid,
                'GetHandleForUserid',
                'some failure message')

        // act
        runFlow(flowName) {
            soap {
                inputJaxbPayload(input)
            }
        }

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
                is(containsString(emailString))

    }
}
