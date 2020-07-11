package com.dfwairport.mule.apps.createUnicenterIncident

import com.avioconsulting.mule.testing.junit.BaseJunitTest
import com.avioconsulting.mule.testing.junit.MuleGroovyParameterizedRunner
import com.ca.unicenterserviceplus.servicedesk.*
import com.oracle.xmlns.createonlineformschangeorder.CreateOnlineFormsChangeOrderProcessRequest
import com.oracle.xmlns.createunicenterchangeorder.CreateUnicenterChangeOrderProcessRequest
import com.oracle.xmlns.createunicenterincident.CreateUnicenterIncidentProcessRequest
import com.oracle.xmlns.createunicenterrequest.CreateUnicenterRequestProcessRequest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat

@RunWith(MuleGroovyParameterizedRunner)
class UserHandleTest extends
        BaseJunitTest implements
        SharedFlowPropertyStuff {
    private final List<String> attrValsWeShouldNotSee
    private final List<String> expectedUserHandleQueries
    enum ServiceType {
        REQUEST,
        INCIDENT,
        CHANGEORDER,
        ONLINEFORMSCHANGEORDER
    }
    private final ServiceType serviceType
    private final String affectedEndUser, requester, assignee
    private final int expectedUnicenterCalls
    private final List<String> expectedAttrVals

    UserHandleTest(String ignoredDescription,
                   ServiceType serviceType,
                   String affectedEndUser,
                   String requester,
                   String assignee,
                   int expectedUnicenterCalls,
                   List<String> expectedAttrVals,
                   List<String> attrValsWeShouldNotSee,
                   List<String> expectedUserHandleQueries) {
        this.expectedUserHandleQueries = expectedUserHandleQueries
        this.attrValsWeShouldNotSee = attrValsWeShouldNotSee
        this.serviceType = serviceType
        this.affectedEndUser = affectedEndUser
        this.requester = requester
        this.assignee = assignee
        this.expectedUnicenterCalls = expectedUnicenterCalls
        this.expectedAttrVals = expectedAttrVals
    }

    static List<List<Object>> generateIncidentRow(String description,
                                                  String affectedEndUser,
                                                  int expectedUnicenterCalls,
                                                  List<String> expectedAttrVals,
                                                  List<String> attrValsWeShouldNotSee,
                                                  List<String> expectedUserHandleQueries) {
        def getResult = { String theAffectedEndUser,
                          String descriptionOverride = null ->
            [
                    descriptionOverride ? "${description} - ${descriptionOverride}".toString() : description,
                    ServiceType.INCIDENT,
                    theAffectedEndUser,
                    null,
                    null,
                    expectedUnicenterCalls,
                    expectedAttrVals,
                    attrValsWeShouldNotSee,
                    expectedUserHandleQueries
            ]
        }
        return affectedEndUser ? [getResult(affectedEndUser)] : [
                getResult(null, 'null'),
                getResult('', 'empty string'),
                getResult(' ', 'space')
        ]
    }

    //useful for request type and onlineFormsChangeOrder ans both have all three userTypes.
    static List<List<Object>> generateAllUserTypeRow(String description,
                                                 String affectedEndUser,
                                                 String requester,
                                                 String assignee,
                                                 int expectedUnicenterCalls,
                                                 List<String> expectedAttrVals,
                                                 List<String> attrValsWeShouldNotSee,
                                                 List<String> expectedUserHandleQueries,
                                                 ServiceType serviceType) {
        def getResult = { String theAffectedEndUser, String theRequester, String theAssignee,
                          String descriptionOverride = null ->
            [
                    descriptionOverride ? "${description} - ${descriptionOverride}".toString() : description,
                    serviceType,
                    theAffectedEndUser,
                    theRequester,
                    theAssignee,
                    expectedUnicenterCalls,
                    expectedAttrVals,
                    attrValsWeShouldNotSee,
                    expectedUserHandleQueries
            ]
        }
        if (affectedEndUser == null && requester == null && assignee == null) {
            return [
                    getResult(null, null, null, 'null'),
                    getResult('', '', '', 'empty string'),
                    getResult(' ', ' ', ' ', 'space')
            ]
        } else {
            return [getResult(affectedEndUser, requester, assignee)]
        }
    }

    static List<List<Object>> generateChangeOrderRow(String description,
                                                     String affectedEndUser,
                                                     String requestor,
                                                     int expectedUnicenterCalls,
                                                     List<String> expectedAttrVals,
                                                     List<String> attrValsWeShouldNotSee,
                                                     List<String> expectedUserHandleQueries) {
        def getResult = { String theAffectedEndUser, String theRequestor,
                          String descriptionOverride = null ->
            [
                    descriptionOverride ? "${description} - ${descriptionOverride}".toString() : description,
                    ServiceType.CHANGEORDER,
                    theAffectedEndUser,
                    theRequestor,
                    null,
                    expectedUnicenterCalls,
                    expectedAttrVals,
                    attrValsWeShouldNotSee,
                    expectedUserHandleQueries
            ]
        }
        if (affectedEndUser == null && requestor == null) {
            return [
                    getResult(null, null, 'null'),
                    getResult('', '', 'empty string'),
                    getResult(' ', ' ', 'space')
            ]
        } else {
            return [getResult(affectedEndUser, requestor)]
        }
    }

    @Parameterized.Parameters(name = "{1} - {0}")
    static Collection<Object[]> data() {
        [
                *generateIncidentRow('no affected end user',
                                     null,
                                     1,
                                     ['customer', 'the handle for sample_username'],
                                     [], // because if no affected end user is supplied, we use the logged user as customer, therefore we should omit anything
                                     ['sample_username']),
                *generateIncidentRow('affected end user',
                                     'sandesh',
                                     2,
                                     ['customer', 'the handle for sandesh'],
                                     [],
                                     ['sample_username', 'sandesh']),
                *generateAllUserTypeRow('no user',
                        null,
                        null,
                        null,
                        1,
                        [], // expected attr vals
                        ['customer', 'requested_by', 'assignee'], // should not see these
                        ['sample_username'],
                        ServiceType.REQUEST),
                *generateAllUserTypeRow('affected end user',
                        'sandesh',
                        null,
                        null,
                        2,
                        ['customer', 'the handle for sandesh'],
                        ['requested_by', 'assignee'],
                        ['sample_username', 'sandesh'],
                        ServiceType.REQUEST),
                *generateAllUserTypeRow('requester',
                        null,
                        'sandesh',
                        null,
                        2,
                        ['requested_by', 'the handle for sandesh'],
                        ['customer', 'assignee'],
                        ['sample_username', 'sandesh'],
                        ServiceType.REQUEST),
                *generateAllUserTypeRow('assignee',
                        null,
                        null,
                        'sandesh',
                        2,
                        ['assignee', 'the handle for sandesh'],
                        ['customer', 'requested_by'],
                        ['sample_username', 'sandesh'],
                        ServiceType.REQUEST),
                *generateAllUserTypeRow('all',
                        'affectedsandesh',
                        'requestersandesh',
                        'assigneesandesh',
                        4,
                        [
                                'customer',
                                'the handle for affectedsandesh',
                                'requested_by',
                                'the handle for requestersandesh',
                                'assignee',
                                'the handle for assigneesandesh'
                        ],
                        [],
                        ['sample_username', 'affectedsandesh', 'requestersandesh', 'assigneesandesh'],
                        ServiceType.REQUEST),
                *generateChangeOrderRow('no user',
                                        null,
                                        null,
                                        1,
                                        [],
                                        ['affected_contact', 'requestor'],
                                        ['sample_username']),
                *generateChangeOrderRow('affected end user',
                                        'sandesh',
                                        null,
                                        2,
                                        ['affected_contact', 'the handle for sandesh'],
                                        ['requestor'],
                                        ['sample_username', 'sandesh']),
                *generateChangeOrderRow('requestor',
                                        null,
                                        'sandesh',
                                        2,
                                        ['requestor', 'the handle for sandesh'],
                                        ['affected_contact'],
                                        ['sample_username', 'sandesh']),
                *generateChangeOrderRow('all',
                                        'affectedsandesh',
                                        'requestersandesh',
                                        3,
                                        [
                                                'affected_contact',
                                                'the handle for affectedsandesh',
                                                'requestor',
                                                'the handle for requestersandesh'
                                        ],
                                        [],
                                        ['sample_username', 'affectedsandesh', 'requestersandesh']),
                *generateAllUserTypeRow('no user',
                        null,
                        null,
                        null,
                        1,
                        [], // expected attr vals
                        ['affected_contact', 'requestor', 'assignee'], // should not see these
                        ['sample_username'],
                        ServiceType.ONLINEFORMSCHANGEORDER),
                *generateAllUserTypeRow('affected end user',
                        'sandesh',
                        null,
                        null,
                        2,
                        ['affected_contact', 'the handle for sandesh'],
                        ['requestor', 'assignee'],
                        ['sample_username', 'sandesh'],
                        ServiceType.ONLINEFORMSCHANGEORDER),
                *generateAllUserTypeRow('requester',
                        null,
                        'sandesh',
                        null,
                        2,
                        ['requestor', 'the handle for sandesh'],
                        ['affected_contact', 'assignee'],
                        ['sample_username', 'sandesh'],
                        ServiceType.ONLINEFORMSCHANGEORDER),
                *generateAllUserTypeRow('assignee',
                        null,
                        null,
                        'sandesh',
                        2,
                        ['assignee', 'the handle for sandesh'],
                        ['affected_contact', 'requestor'],
                        ['sample_username', 'sandesh'],
                        ServiceType.ONLINEFORMSCHANGEORDER),
                *generateAllUserTypeRow('all',
                        'affectedsandesh',
                        'requestersandesh',
                        'assigneesandesh',
                        4,
                        [
                                'affected_contact',
                                'the handle for affectedsandesh',
                                'requestor',
                                'the handle for requestersandesh',
                                'assignee',
                                'the handle for assigneesandesh'
                        ],
                        [],
                        ['sample_username', 'affectedsandesh', 'requestersandesh', 'assigneesandesh'],
                        ServiceType.ONLINEFORMSCHANGEORDER)
        ].collect { list ->
            // JUnit expects object arrays not lists, so we convert each line above here
            list.toArray(new Object[0])
        }
    }

    def getInput() {
        switch (this.serviceType) {
            case ServiceType.INCIDENT:
                return new CreateUnicenterIncidentProcessRequest().with {
                    it.affectedEndUser = this.affectedEndUser
                    it.incidentArea = "pcat:123"
                    it
                }
            case ServiceType.REQUEST:
                return new CreateUnicenterRequestProcessRequest().with {
                    it.affectedEndUser = this.affectedEndUser
                    it.requester = this.requester
                    it.assignee = this.assignee
                    it.description = 'sample description'
                    it.summary = 'sample summary'
                    it.category = 'pcat:1234'
                    it
                }
            case ServiceType.CHANGEORDER:
                return new CreateUnicenterChangeOrderProcessRequest().with {
                    it.affectedEndUser = this.affectedEndUser
                    it.requester = this.requester
                    it.category = 'chgcat:1234'
                    it.description = 'the description'
                    it.summary = 'the summary'
                    it
                }
            case ServiceType.ONLINEFORMSCHANGEORDER:
                return new CreateOnlineFormsChangeOrderProcessRequest().with {
                    it.affectedEndUser = this.affectedEndUser
                    it.requester = this.requester
                    it.assignee = this.assignee
                    it.description = 'sample description'
                    it.summary = 'sample summary'
                    it.category = 'chgcat:1234'
                    it
                }
            default:
                throw new Exception("Unknown service type ${this.serviceType}")
        }
    }
//todo test for null case, if requesttype is other than incident, request, changeOrder of onlineFormsChangeOrder
    @Test
    void correct_handles_requested() {
        // arrange
        def input = getInput()
        String flowName
        switch (this.serviceType) {
            case ServiceType.INCIDENT:
                flowName = LOGIN_ONLY_INCIDENT
                break
            case ServiceType.REQUEST:
                flowName = LOGIN_ONLY_REQUEST
                break
            case ServiceType.CHANGEORDER:
                flowName = LOGIN_ONLY_CHANGEORDER
                break
            case ServiceType.ONLINEFORMSCHANGEORDER:
                flowName = LOGIN_ONLY_ONLINEFORMSCHANGEORDER
                break
            default:
                throw new Exception("Unknown service type ${this.serviceType}")
        }

        def logger = getLogger()
        mockLoginWithResponse()

        List<GetHandleForUserid> actualUnicenterRequests = []
        mockSoapCall('GetHandleForUserid') {
            whenCalledWithJaxb(GetHandleForUserid) { GetHandleForUserid request ->
                logger.info "Now getting handle for user ID ${request.userID}"
                actualUnicenterRequests << request
                def handleResponse = new GetHandleForUseridResponse()
                handleResponse.setGetHandleForUseridResult("the handle for ${request.userID}")
                return handleResponse
            }
        }

        // act
        logger.info 'running flow {} now',
                    flowName
        runFlow(flowName) {
            soap {
                inputJaxbPayload(input)
            }
        }

        // assert
        assertThat actualUnicenterRequests.size(),
                   is(equalTo(this.expectedUnicenterCalls))
        // every one of them will have this
        def loggedinUserRequest = actualUnicenterRequests[0]
        assertThat loggedinUserRequest.SID,
                   is(equalTo(22))
        assertThat loggedinUserRequest.userID,
                   is(equalTo('sample_username'))
        actualUnicenterRequests.each { r ->
            assertThat r.SID,
                       is(equalTo(22))
        }
        def actualUsers = actualUnicenterRequests.collect { r ->
            r.userID
        }
        assertThat actualUsers,
                   is(equalTo(this.expectedUserHandleQueries))
    }

    @Test
    void correct_handles_used_properly_in_later_request() {
        // arrange
        def input = getInput()
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

        CreateRequest actualCreateRequest = null
        mockSoapCall('Create Request') {
            whenCalledWithJaxb(CreateRequest) { CreateRequest createRequest ->
                logger.info 'Mock of Unicenter Create Request called'
                actualCreateRequest = createRequest
                new CreateRequestResponse()
            }
        }

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
        logger.info 'running flow {} now',
                    flowName
        runFlow(flowName) {
            soap {
                inputJaxbPayload(input)
            }
        }

        // assert
        def attrVals = getAttrVals(this.serviceType, actualCreateRequest, actualCreateChangeOrder)

        assertThat(this.expectedAttrVals,
                everyItem(isIn(attrVals)))
        assertThat('We should not see these keys in attrVals at all but we did',
                this.attrValsWeShouldNotSee,
                everyItem(not(isIn(attrVals))))
    }

    def getAttrVals(ServiceType serviceType, CreateRequest actualCreateRequest, CreateChangeOrder actualCreateChangeOrder) {
        switch (serviceType) {
            case ServiceType.INCIDENT:
                return actualCreateRequest.attrVals.string
            case ServiceType.REQUEST:
                return actualCreateRequest.attrVals.string
            case ServiceType.CHANGEORDER:
                return actualCreateChangeOrder.attrVals.string
            case ServiceType.ONLINEFORMSCHANGEORDER:
                return actualCreateChangeOrder.attrVals.string
            default:
                throw new Exception("Unknown service type ${serviceType}")
        }
    }
}
