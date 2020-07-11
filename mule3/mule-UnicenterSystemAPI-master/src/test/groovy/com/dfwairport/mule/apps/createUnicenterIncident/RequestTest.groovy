package com.dfwairport.mule.apps.createUnicenterIncident

import com.avioconsulting.mule.testing.XmlDateHelp
import com.avioconsulting.mule.testing.junit.BaseJunitTest
import com.ca.unicenterserviceplus.servicedesk.ArrayOfString
import com.ca.unicenterserviceplus.servicedesk.CreateRequest
import com.ca.unicenterserviceplus.servicedesk.CreateRequestResponse
import com.oracle.xmlns.createunicenterrequest.CreateUnicenterRequestProcessRequest
import com.oracle.xmlns.createunicenterrequest.CreateUnicenterRequestProcessResponse
import groovy.util.logging.Log4j2
import org.junit.Test

import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat

@Log4j2
class RequestTest extends
        BaseJunitTest implements
        SharedFlowPropertyStuff,
        XmlDateHelp {

    @Test
    void create_request_overall() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            assignee = 'assigneesandesh'
            summary = 'summary'
            description = 'the description'
            category = '123'
            group = 'group'
            impact = 'medium'
            priority = 'high'
            severity = 'low'
            urgency = 'medium'
            appendfirst5Proptodesc = 'N'
            it
        }

        def request_properties = input.requestProperties = new CreateUnicenterRequestProcessRequest.RequestProperties()
        (1..50).each { index ->
            request_properties."setQ${index}"("the value")
            request_properties."setA${index}"("the value")
        }

        // act
        def actualCreateRequest = executeCreateRequest(input)

        // assert
        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'summary',
                'summary',
                'description',
                'the description',
                'requested_by',
                'the handle for requestersandesh',
                'customer',
                'the handle for affectedsandesh',
                'assignee',
                'the handle for assigneesandesh',
                'category',
                'pcat:123',
                'status',
                'crs:5200',
                'group',
                'group',
                'type',
                'R',
                'impact',
                'code for medium',
                'priority',
                'code for high',
                'severity',
                'code for low',
                'urgency',
                'code for medium'
        ]

        assertThat actualCreateRequest.SID,
                   is(equalTo(22))
        assertThat actualCreateRequest.creatorHandle,
                   is(equalTo('the handle for sample_username'))
        assertThat actualCreateRequest.template,
                   is(equalTo(''))
        assertThat(actualCreateRequest.attrVals,
                   samePropertyValuesAs(expectedAttrVals))

        def expectedPropertyValues = new ArrayOfString()
        (1..100).each { index ->
            expectedPropertyValues.string.add("the value")
        }

        assertThat(actualCreateRequest.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues));

        def expectedAttributes = new ArrayOfString()
        expectedAttributes.string = [
                'persistent_id'
        ]
        assertThat(actualCreateRequest.attributes,
                   samePropertyValuesAs(expectedAttributes))
    }

    @Test
    void description_summary_has_url() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            assignee = 'assigneesandesh'
            summary = 'http://some/other/url'
            description = 'http://some/url'
            category = '123'
            group = 'group'
            impact = 'medium'
            priority = 'high'
            severity = 'low'
            urgency = 'medium'
            appendfirst5Proptodesc = 'N'
            it
        }

        // act
        def actualCreateRequest = executeCreateRequest(input)

        // assert
        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'summary',
                '<a href="http://some/other/url" target="_blank">http://some/other/url</a>',
                'description',
                '<a href="http://some/url" target="_blank">http://some/url</a>',
                'requested_by',
                'the handle for requestersandesh',
                'customer',
                'the handle for affectedsandesh',
                'assignee',
                'the handle for assigneesandesh',
                'category',
                'pcat:123',
                'status',
                'crs:5200',
                'group',
                'group',
                'type',
                'R',
                'impact',
                'code for medium',
                'priority',
                'code for high',
                'severity',
                'code for low',
                'urgency',
                'code for medium'
        ]
        assertThat(actualCreateRequest.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
    }

    @Test
    void description_summary_has_url_whitespace() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            assignee = 'assigneesandesh'
            summary = ' http://some/other/url'
            description = ' http://some/url'
            category = '123'
            group = 'group'
            impact = 'medium'
            priority = 'high'
            severity = 'low'
            urgency = 'medium'
            appendfirst5Proptodesc = 'N'
            it
        }

        // act
        def actualCreateRequest = executeCreateRequest(input)

        // assert
        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'summary',
                '<a href="http://some/other/url" target="_blank">http://some/other/url</a>',
                'description',
                '<a href="http://some/url" target="_blank">http://some/url</a>',
                'requested_by',
                'the handle for requestersandesh',
                'customer',
                'the handle for affectedsandesh',
                'assignee',
                'the handle for assigneesandesh',
                'category',
                'pcat:123',
                'status',
                'crs:5200',
                'group',
                'group',
                'type',
                'R',
                'impact',
                'code for medium',
                'priority',
                'code for high',
                'severity',
                'code for low',
                'urgency',
                'code for medium'
        ]
        assertThat(actualCreateRequest.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
    }

    @Test
    void create_request_overall_appendfirst5_empty() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            assignee = 'assigneesandesh'
            summary = 'summary'
            description = 'description'
            category = '123'
            group = 'group'
            impact = 'medium'
            priority = 'high'
            severity = 'low'
            urgency = 'medium'
            appendfirst5Proptodesc = ''
            it
        }

        def request_properties = input.requestProperties = new CreateUnicenterRequestProcessRequest.RequestProperties()
        (1..50).each { index ->
            request_properties."setQ${index}"("the value")
            request_properties."setA${index}"("the value")
        }

        // act
        def actualCreateRequest = executeCreateRequest(input)

        // assert
        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'summary',
                'summary',
                'description',
                'description',
                'requested_by',
                'the handle for requestersandesh',
                'customer',
                'the handle for affectedsandesh',
                'assignee',
                'the handle for assigneesandesh',
                'category',
                'pcat:123',
                'status',
                'crs:5200',
                'group',
                'group',
                'type',
                'R',
                'impact',
                'code for medium',
                'priority',
                'code for high',
                'severity',
                'code for low',
                'urgency',
                'code for medium'
        ]

        assertThat actualCreateRequest.SID,
                   is(equalTo(22))
        assertThat actualCreateRequest.creatorHandle,
                   is(equalTo('the handle for sample_username'))
        assertThat actualCreateRequest.template,
                   is(equalTo(''))
        assertThat(actualCreateRequest.attrVals,
                   samePropertyValuesAs(expectedAttrVals))

        def expectedPropertyValues = new ArrayOfString()
        (1..100).each { index ->
            expectedPropertyValues.string.add("the value")
        }

        assertThat(actualCreateRequest.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues));

        def expectedAttributes = new ArrayOfString()
        expectedAttributes.string = [
                'persistent_id'
        ]
        assertThat(actualCreateRequest.attributes,
                   samePropertyValuesAs(expectedAttributes))
    }

    @Test
    void create_request_no_impact_urgency_priority_severity() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            assignee = 'assigneesandesh'
            summary = 'summary'
            description = 'description'
            category = '123'
            group = 'group'
            appendfirst5Proptodesc = 'N'
            it
        }

        // act
        def actualCreateRequest = executeCreateRequest(input)

        // assert
        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'summary',
                'summary',
                'description',
                'description',
                'requested_by',
                'the handle for requestersandesh',
                'customer',
                'the handle for affectedsandesh',
                'assignee',
                'the handle for assigneesandesh',
                'category',
                'pcat:123',
                'status',
                'crs:5200',
                'group',
                'group',
                'type',
                'R'
        ]
        assertThat(actualCreateRequest.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
    }

    @Test
    void create_request_empty_impact_urgency_priority_severity() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            assignee = 'assigneesandesh'
            summary = 'summary'
            description = 'description'
            category = '123'
            group = 'group'
            impact = ''
            priority = ''
            severity = ''
            urgency = ''
            appendfirst5Proptodesc = 'N'
            it
        }

        // act
        def actualCreateRequest = executeCreateRequest(input)

        // assert
        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'summary',
                'summary',
                'description',
                'description',
                'requested_by',
                'the handle for requestersandesh',
                'customer',
                'the handle for affectedsandesh',
                'assignee',
                'the handle for assigneesandesh',
                'category',
                'pcat:123',
                'status',
                'crs:5200',
                'group',
                'group',
                'type',
                'R'
        ]
        assertThat(actualCreateRequest.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
    }

    @Test
    void create_request_doselect_returns_no_value_for_impact_urgency_priority_severity() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            assignee = 'assigneesandesh'
            summary = 'summary'
            description = 'description'
            category = '123'
            group = 'group'
            impact = 'unknown_impact'
            priority = 'unknown_priority'
            severity = 'unknown_severity'
            urgency = 'unknown_urgency'
            appendfirst5Proptodesc = 'N'
            it
        }

        // act
        def actualCreateRequest = executeCreateRequest(input)

        // assert
        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'summary',
                'summary',
                'description',
                'description',
                'requested_by',
                'the handle for requestersandesh',
                'customer',
                'the handle for affectedsandesh',
                'assignee',
                'the handle for assigneesandesh',
                'category',
                'pcat:123',
                'status',
                'crs:5200',
                'group',
                'group',
                'type',
                'R'
        ]
        assertThat(actualCreateRequest.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
    }

    @Test
    void create_request_appendfirst_five_all_have_question_and_answer() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            assignee = 'assigneesandesh'
            summary = 'summary'
            description = 'the initial description'
            category = 'category'
            group = 'group'
            impact = 'medium'
            priority = 'high'
            severity = 'low'
            urgency = 'medium'
            appendfirst5Proptodesc = 'Y'
            it
        }

        def request_properties = input.requestProperties = new CreateUnicenterRequestProcessRequest.RequestProperties()
        (1..50).each { index ->
            request_properties."setQ${index}"(" the question ${index}".toString())
            request_properties."setA${index}"(" the answer ${index}".toString())
        }

        // act
        def actualCreateRequest = executeCreateRequest(input)
        def actualDescription = actualCreateRequest.attrVals.string[3]
        assertThat actualDescription,
                   is(equalTo('the initial description\n' +
                                      'the question 1 - the answer 1\n' +
                                      'the question 2 - the answer 2\n' +
                                      'the question 3 - the answer 3\n' +
                                      'the question 4 - the answer 4\n' +
                                      'the question 5 - the answer 5'))
    }

    @Test
    void create_request_appendfirst_five_only_questions() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            summary = 'summary'
            description = 'the initial description'
            category = 'category'
            group = 'group'
            appendfirst5Proptodesc = 'Y'
            it
        }

        def request_properties = input.requestProperties = new CreateUnicenterRequestProcessRequest.RequestProperties()
        (1..5).each { index ->
            request_properties."setQ${index}"(" the question ${index}")
        }

        // act
        def actualCreateRequest = executeCreateRequest(input)

        // assert
        assertThat actualCreateRequest.attrVals.string[3],
                   is(equalTo('the initial description\n' +
                                      'the question 1\n' +
                                      'the question 2\n' +
                                      'the question 3\n' +
                                      'the question 4\n' +
                                      'the question 5'))
    }

    @Test
    void create_request_appendfirst_five_only_answers() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            summary = 'summary'
            description = 'the initial description'
            category = 'category'
            group = 'group'
            appendfirst5Proptodesc = 'Y'
            it
        }

        def request_properties = input.requestProperties = new CreateUnicenterRequestProcessRequest.RequestProperties()
        (1..3).each { index ->
            request_properties."setA${index}"(" the answer ${index}")
        }

        // act

        def actualCreateRequest = executeCreateRequest(input)

        // assert
        assertThat actualCreateRequest.attrVals.string[3],
                   is(equalTo('the initial description\n' +
                                      'the answer 1\n' +
                                      'the answer 2\n' +
                                      'the answer 3'))
    }

    @Test
    void simulation() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest()
        input.category = 'SIMULATION'
        def requestCreated = false

        def logger = log
        mockSoapCall('Create Request') {
            whenCalledWithJaxb(CreateRequest) { CreateRequest createRequest ->
                logger.info 'Mock of Create Request called'
                requestCreated = true
                new CreateRequestResponse()
            }
        }

        //act
        runFlow('createRequestFlowWithStreamOfXML') {
            soap {
                inputJaxbPayload(input)
            }
        }

        // assert
        assertThat requestCreated,
                   is(equalTo(false))
    }

    @Test
    void property_values_only_5() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            it.affectedEndUser = 'affectedsandesh'
            it.description = 'sample description'
            it.summary = 'sample summary'
            it.category = 'pcat:1234'
            it
        }
        def request_properties = input.requestProperties = new CreateUnicenterRequestProcessRequest.RequestProperties()
        (1..5).each { index ->
            request_properties."setQ${index}"("the value")
            request_properties."setA${index}"("the value")
        }

        //act
        def actualCreateRequest = executeCreateRequest(input)

        def expectedPropertyValues = new ArrayOfString()
        (1..10).each { index ->
            expectedPropertyValues.string.add("the value")
        }
        90.times {
            expectedPropertyValues.string.add('-')
        }

        // assert
        assertThat(actualCreateRequest.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues));
    }

    @Test
    void response_success() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            assignee = 'assigneesandesh'
            summary = 'summary'
            description = 'description'
            category = 'category'
            group = 'group'
            impact = 'low'
            priority = 'medium'
            severity = 'low'
            urgency = 'high'
            it
        }

        def request_properties = input.requestProperties = new CreateUnicenterRequestProcessRequest.RequestProperties()
        (1..50).each { index ->
            request_properties."setQ${index}"("the value")
            request_properties."setA${index}"("the value")
        }

        def logger = log
        mockLoginWithResponse()
        mockGetHandleForUserIdWithResponse()
        mockDoSelectWithResponse()
        mockCreateRequestWithRequestResponse()
        mockLogout()

        // act
        log.info 'running flow now'
        def response = runFlow(FLOW_NAME_REQUEST) {
            soap {
                inputJaxbPayload(input)
            }
        } as CreateUnicenterRequestProcessResponse

        // assert
        response.with {
            assertThat sessionId,
                       is(equalTo('22'))
            assertThat result,
                       is(equalTo('Completed'))
            assertThat requestHandle,
                       is(equalTo('the handle'))
            assertThat requestNumber,
                       is(equalTo('request:1234'))
            assertThat desc,
                       is(equalTo('Request was created successfully.'))
        }
    }

    @Test
    void soap_fault_on_create_request_proper_email_sent_out() {
        // arrange
        def input = new CreateUnicenterRequestProcessRequest().with {
            it.affectedEndUser = 'affectedsandesh'
            it.requester = 'requestersandesh'
            it.category = '1234'
            it.description = 'sample description'
            it.summary = 'sample summary'
            it
        }

        def logger = log
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

        mockUnicenterFailure(CreateRequest,
                             'Create Request',
                             'some failure message')

        // act
        runFlow(FLOW_NAME_REQUEST) {
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
                   is(containsString('<strong>Exception context:</strong> requester: requestersandesh, affected end user: affectedsandesh, request category: 1234'))
    }
}
