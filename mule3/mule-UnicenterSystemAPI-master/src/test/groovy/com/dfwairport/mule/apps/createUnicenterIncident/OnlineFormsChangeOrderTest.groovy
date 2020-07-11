package com.dfwairport.mule.apps.createUnicenterIncident

import com.avioconsulting.mule.testing.XmlDateHelp
import com.avioconsulting.mule.testing.junit.BaseJunitTest
import com.ca.unicenterserviceplus.servicedesk.ArrayOfString
import com.ca.unicenterserviceplus.servicedesk.CreateChangeOrder
import com.ca.unicenterserviceplus.servicedesk.DoSelect
import com.oracle.xmlns.createonlineformschangeorder.CabType
import com.oracle.xmlns.createonlineformschangeorder.CreateOnlineFormsChangeOrderProcessRequest
import com.oracle.xmlns.createonlineformschangeorder.CreateOnlineFormsChangeOrderProcessResponse
import groovy.util.logging.Log4j2
import org.junit.Test
import org.mule.api.MuleEvent
import org.mule.api.transport.PropertyScope

import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat

@Log4j2
class OnlineFormsChangeOrderTest extends
        BaseJunitTest implements
        SharedFlowPropertyStuff,
        XmlDateHelp {

    @Test
    void create_change_order_request_overall() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            affectedEndUser = 'affectedsandesh'
            assignee = 'assigneesandesh'
            cabApproval = CabType.YES
            coGroup = 'sample group and length is obviously greater than thirty'
            category = 'chgcat:1234'
            coType = 'co type'
            justification = 'sample justification'
            description = 'sample description'
            summary = 'sample summary'
            it
        }

        def online_form_queries = input.onlineFormQueries = new CreateOnlineFormsChangeOrderProcessRequest.OnlineFormQueries()
        (1..40).each { index ->
            online_form_queries."setQ${index}"("the question number: ${index}".toString())
            online_form_queries."setA${index}"("the answer for Q${index}".toString())
        }
        (41..45).each { index ->
            online_form_queries."setQ${index}"("the question number: ${index}".toString())
        }
        (46..50).each { index ->
            online_form_queries."setA${index}"("the answer for Q${index}".toString())
        }

        def actualCreateChangeOrder = executeCreateChangeOrder(input)

        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'requestor',
                'the handle for requestersandesh',
                'affected_contact',
                'the handle for affectedsandesh',
                'assignee',
                'the handle for assigneesandesh',
                'category',
                'chgcat:1234',
                'cab_approval',
                '1',
                'group',
                'cnt:sample group and length is obviously greater than thirty',
                'chgtype',
                'cnt:co type',
                'justification',
                'sample justification',
                'summary',
                'sample summary',
                'description',
                'sample description',
                'status',
                'chgstat:6000'
        ]

        // assert
        assertThat actualCreateChangeOrder.SID,
                   is(equalTo(22))
        assertThat actualCreateChangeOrder.creatorHandle,
                   is(equalTo('the handle for sample_username'))
        assertThat(actualCreateChangeOrder.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
        assertThat actualCreateChangeOrder.template,
                   is(equalTo(''))


        def expected = ((1..40).collect { num ->
            [
                    "the question number: ${num}",
                    "the answer for Q${num}"
            ]
        } + (41..45).collect { num ->
            [
                    "the question number: ${num}",
                    '-'
            ]
        } + (46..50).collect { num ->
            [
                    '-',
                    "the answer for Q${num}"
            ]
        }).flatten().collect { entry -> entry.toString() }
        // tostring gets rid of GStrings which cause assertion problems
        assertThat actualCreateChangeOrder.propertyValues.string,
                   is(equalTo(expected))
        def expectedAttributes = new ArrayOfString()
        expectedAttributes.string = [
                'persistent_id'
        ]
        assertThat(actualCreateChangeOrder.attributes,
                   samePropertyValuesAs(expectedAttributes))
    }

    @Test
    void description_summary_have_url() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            affectedEndUser = 'affectedsandesh'
            assignee = 'assigneesandesh'
            cabApproval = CabType.YES
            coGroup = 'sample group and length is obviously greater than thirty'
            category = 'chgcat:1234'
            coType = 'co type'
            justification = 'sample justification'
            summary = 'http://some/other/url'
            description = 'http://some/url'
            it
        }

        // act
        def actualCreateChangeOrder = executeCreateChangeOrder(input)

        // assert
        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'requestor',
                'the handle for requestersandesh',
                'affected_contact',
                'the handle for affectedsandesh',
                'assignee',
                'the handle for assigneesandesh',
                'category',
                'chgcat:1234',
                'cab_approval',
                '1',
                'group',
                'cnt:sample group and length is obviously greater than thirty',
                'chgtype',
                'cnt:co type',
                'justification',
                'sample justification',
                'summary',
                '<a href="http://some/other/url" target="_blank">http://some/other/url</a>',
                'description',
                '<a href="http://some/url" target="_blank">http://some/url</a>',
                'status',
                'chgstat:6000'
        ]

        // assert
        assertThat(actualCreateChangeOrder.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
    }

    @Test
    void description_summary_have_url_whitespace() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            affectedEndUser = 'affectedsandesh'
            assignee = 'assigneesandesh'
            cabApproval = CabType.YES
            coGroup = 'sample group and length is obviously greater than thirty'
            category = 'chgcat:1234'
            coType = 'co type'
            justification = 'sample justification'
            summary = ' http://some/other/url'
            description = ' http://some/url'
            it
        }

        // act
        def actualCreateChangeOrder = executeCreateChangeOrder(input)

        // assert
        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'requestor',
                'the handle for requestersandesh',
                'affected_contact',
                'the handle for affectedsandesh',
                'assignee',
                'the handle for assigneesandesh',
                'category',
                'chgcat:1234',
                'cab_approval',
                '1',
                'group',
                'cnt:sample group and length is obviously greater than thirty',
                'chgtype',
                'cnt:co type',
                'justification',
                'sample justification',
                'summary',
                '<a href="http://some/other/url" target="_blank">http://some/other/url</a>',
                'description',
                '<a href="http://some/url" target="_blank">http://some/url</a>',
                'status',
                'chgstat:6000'
        ]

        // assert
        assertThat(actualCreateChangeOrder.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
    }

    @Test
    // DFW-169
    void create_change_order_blank_values() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            affectedEndUser = 'affectedsandesh'
            assignee = 'assigneesandesh'
            cabApproval = CabType.YES
            coGroup = 'sample group and length is obviously greater than thirty'
            category = 'chgcat:1234'
            coType = 'co type'
            justification = 'sample justification'
            description = 'sample description'
            summary = 'sample summary'
            onlineFormQueries = new CreateOnlineFormsChangeOrderProcessRequest.OnlineFormQueries().with {
                q1 = 'This is Q1'
                a1 = 'This is A1'
                q2 = ''
                a2 = ''
                q3 = 'This is Q3'
                a3 = 'This is A3'
                it
            }
            it
        }

        // act
        def actualCreateChangeOrder = executeCreateChangeOrder(input)

        // assert
        def strings = actualCreateChangeOrder.propertyValues.string
        assertThat strings.size(),
                   is(equalTo(100))
        def mainPiece = [
                'This is Q1',
                'This is A1',
                '-',
                '-',
                'This is Q3',
                'This is A3'
        ]
        def filler = (1..(100 - mainPiece.size())).collect { '-' }
        assertThat strings,
                   is(equalTo(mainPiece + filler))
    }

    @Test
    void create_change_order_request_without_form_queries() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            affectedEndUser = 'affectedsandesh'
            assignee = 'assigneesandesh'
            coNumberStatus = "" //empty CO, so it doesn't affect the flow at all
            cabApproval = CabType.YES
            coGroup = 'sample group and length is obviously greater than thirty'
            category = 'chgcat:1234'
            coType = 'co type'
            justification = 'sample justification'
            description = 'sample description'
            summary = 'sample summary'
            it
        }

        def actualCreateChangeOrder = executeCreateChangeOrder(input)

        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'requestor',
                'the handle for requestersandesh',
                'affected_contact',
                'the handle for affectedsandesh',
                'assignee',
                'the handle for assigneesandesh',
                'category',
                'chgcat:1234',
                'cab_approval',
                '1',
                'group',
                'cnt:sample group and length is obviously greater than thirty',
                'chgtype',
                'cnt:co type',
                'justification',
                'sample justification',
                'summary',
                'sample summary',
                'description',
                'sample description',
                'status',
                'chgstat:6000'
        ]

        // assert
        assertThat(actualCreateChangeOrder.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
    }

    @Test
    void create_change_order_request_overall_co_group_less_than_30() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            description = 'sample description'
            summary = 'sample summary'
            coGroup = 'sample group'
            category = 'chgcat:123'
            it
        }

        def actualCreateChangeOrder = executeCreateChangeOrder(input)

        // assert
        assertThat(actualCreateChangeOrder.attrVals.string[7],
                   samePropertyValuesAs(''))
    }

    @Test
    void create_change_order_with_co_number_status() {
        // arrange
        def logger = log
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            coNumberStatus = '123' //not empty, so goes skips getHandleForUserId and createChangeOrder
            it
        }

        mockLoginWithResponse()
        mockLogout()

        def actualDoSelectRequest = null
        mockDoSelectWithResponse(null) { DoSelect request ->
            actualDoSelectRequest = request
        }

        // act
        log.info 'running flow now'
        runFlow(FLOW_NAME_ONLINEFORMSCHANGEORDER) {
            soap {
                inputJaxbPayload(input)
            }
        }

        // assert
        assertThat actualDoSelectRequest.SID,
                   is(equalTo(22))
        assertThat actualDoSelectRequest.objectType,
                   is(equalTo('chg'))
        assertThat actualDoSelectRequest.whereClause,
                   is(equalTo("chg_ref_num = '123'"))
        assertThat actualDoSelectRequest.maxRows,
                   is(equalTo(1))

        def expectedAttributes = new ArrayOfString()
        expectedAttributes.string = [
                'chg_ref_num',
                'summary',
                'description',
                'status',
                'category'
        ]
        assertThat(actualDoSelectRequest.attributes,
                   samePropertyValuesAs(expectedAttributes))
    }

    @Test
    void response_failure_on_invalid_category() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            description = 'sample description'
            summary = 'sample summary'
            coGroup = 'sample group'
            category = 'foobar'
            it
        }

        def logger = log
        mockLoginWithResponse()
        mockGetHandleForUserIdWithResponse()
        mockCreateChangeOrderWithResponse()
        mockLogout()

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
        log.info 'running flow now'
        def response = runFlow(FLOW_NAME_ONLINEFORMSCHANGEORDER) {
            soap {
                inputJaxbPayload(input)
            }
        } as CreateOnlineFormsChangeOrderProcessResponse

        // assert
        assertThat 'Expected to send 1 email for the first failure and then quit processing',
                   emailPayloads.size(),
                   is(equalTo(1))
        def emailPayload = emailPayloads[0]
        assertThat emailPayload,
                   is(containsString('<html>'))
        assertThat emailPayload,
                   is(containsString('Required fields are missing: [category]'))
        assertThat emailPayload,
                   is(containsString('<strong>Exception context:</strong> requester: requestersandesh, affected end user: no affected end user, request category: foobar'))
        response.with {
            assertThat result,
                       is(equalTo('Error'))
            assertThat desc,
                       is(equalTo('This change order was not created. The following required fields are missing or invalid: [category]. This process will stop here. Please check the console for more details.'))
        }
    }

    @Test
    void response_success() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            description = 'sample description'
            summary = 'sample summary'
            category = 'chgcat:1234'
            it
        }

        def logger = log
        mockLoginWithResponse()
        mockGetHandleForUserIdWithResponse()
        mockCreateChangeOrderWithResponse()
        mockLogout()
        // act
        log.info 'running flow now'
        def response = runFlow(FLOW_NAME_ONLINEFORMSCHANGEORDER) {
            soap {
                inputJaxbPayload(input)
            }
        } as CreateOnlineFormsChangeOrderProcessResponse

        // assert
        response.with {
            assertThat sessionId,
                       is(equalTo('22'))
            assertThat changeOrderNumber,
                       is(equalTo("chgcat:1234"))
            assertThat changeOrderHandle,
                       is(equalTo('the handle'))
            assertThat result,
                       is(equalTo('Completed'))
            assertThat desc,
                       is(equalTo('Change Order was created successfully.'))
        }
    }

    @Test
    void response_success_for_co_number_status_variable_boolean_exist() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest()
        input.coNumberStatus = 'CO123' //starts with CO, so goes skips getHandleForUserId and createChangeOrder

        def logger = log
        mockLoginWithResponse()
        mockDoSelectWithResponse()
        mockLogout()

        // act
        log.info 'running flow now'
        def response = runFlow(FLOW_NAME_ONLINEFORMSCHANGEORDER) {
            soap {
                inputJaxbPayload(input)
            }
        } as CreateOnlineFormsChangeOrderProcessResponse

        // assert
        response.with {
            assertThat coStatusValue,
                       is(equalTo('CL'))
            assertThat changeOrderNumber,
                       is(equalTo("123"))
            assertThat sessionId,
                       is(equalTo("RqstPDAOFA"))
            assertThat result,
                       is(equalTo('sample summary'))
            assertThat desc,
                       is(equalTo('sample description'))
        }
    }

    @Test
    void response_for_co_number_status_variable_boolean_null() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest()
        input.coNumberStatus = 'CO1234' // not empty, so goes skips getHandleForUserId and createChangeOrder

        def logger = log
        mockLoginWithResponse()
        mockDoSelectWithResponse()
        mockLogout()

        // act
        log.info 'running flow now'
        def response = runFlow(FLOW_NAME_ONLINEFORMSCHANGEORDER) {
            soap {
                inputJaxbPayload(input)
            }
        } as CreateOnlineFormsChangeOrderProcessResponse

        // assert
        response.with {
            assertThat coStatusValue,
                       is(equalTo('Not Found'))
            assertThat changeOrderNumber,
                       is(equalTo("CO1234"))
            assertThat sessionId,
                       is(equalTo(""))
            assertThat result,
                       is(equalTo('Change Order not found.'))
            assertThat desc,
                       is(equalTo('USD database does not have any details for CO# CO1234'))
        }
    }

    @Test
    void soap_fault_on_create_change_order_proper_email_sent_out() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            description = 'sample description'
            summary = 'sample summary'
            category = 'chgcat:1234'
            instanceID = 'instance123'
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

        mockUnicenterFailure(CreateChangeOrder,
                             'Create Change Order',
                             'some failure message')

        // act
        def response = runFlow(FLOW_NAME_ONLINEFORMSCHANGEORDER) {
            soap {
                inputJaxbPayload(input)
            }
        } as CreateOnlineFormsChangeOrderProcessResponse

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
                   is(containsString('<strong>Exception context:</strong> requester: requestersandesh, affected end user: affectedsandesh, request category: chgcat:1234'))

        response.with {
            assertThat result,
                       is(equalTo('Error'))
            assertThat desc,
                       is(equalTo('This request was not created due to the fault exception: some failure message.; The instance ID is: instance123, please check the console for more details.'))
        }
    }

    void soap_fault_on_required_fields_missing(input,
                                               String firstErrorContext,
                                               String secondErrorContext,
                                               String missingFieldsText) {
        // arrange

        def logger = log
        mockLoginWithResponse()
        mockGetHandleForUserIdWithResponse()
        mockLogout()

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
        def response = runFlow(FLOW_NAME_ONLINEFORMSCHANGEORDER) {
            soap {
                inputJaxbPayload(input)
            }
        } as CreateOnlineFormsChangeOrderProcessResponse

        // assert
        assertThat 'Expected to send 1 email for the first failure and then quit processing',
                   emailPayloads.size(),
                   is(equalTo(1))
        def emailPayload = emailPayloads[0]
        assertThat emailPayload,
                   is(containsString('<html>'))
        assertThat emailPayload,
                   is(containsString(firstErrorContext))
        assertThat emailPayload,
                   is(containsString(secondErrorContext))
        response.with {
            assertThat it.result,
                       is(equalTo('Error'))
            assertThat it.desc,
                       is(equalTo('This change order was not created. The following required fields are missing or invalid: ' + missingFieldsText + '. This process will stop here. Please check the console for more details.'))
            it
        }
    }

    @Test
    void soap_fault_on_required_fields_missing_summary() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            description = 'sample description'
            category = 'chgcat:1234'
            it
        }

        soap_fault_on_required_fields_missing(input,
                                              'Required fields are missing: [summary]',
                                              '<strong>Exception context:</strong> requester: requestersandesh, affected end user: affectedsandesh, request category: chgcat:1234',
                                              '[summary]')
    }

    @Test
    void soap_fault_on_required_fields_missing_description() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            summary = 'sample summary'
            category = 'chgcat:1234'
            it
        }

        soap_fault_on_required_fields_missing(input,
                                              'Required fields are missing: [description]',
                                              '<strong>Exception context:</strong> requester: requestersandesh, affected end user: affectedsandesh, request category: chgcat:1234',
                                              '[description]')
    }

    @Test
    void soap_fault_on_required_fields_missing_category() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            summary = 'sample summary'
            description = 'sample description'
            it
        }

        soap_fault_on_required_fields_missing(input,
                                              'Required fields are missing: [category]',
                                              '<strong>Exception context:</strong> requester: requestersandesh, affected end user: affectedsandesh, request category: no request category',
                                              '[category]')
    }

    @Test
    void soap_fault_on_required_fields_missing_all() {
        // arrange
        def input = new CreateOnlineFormsChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            it
        }

        soap_fault_on_required_fields_missing(input,
                                              'Required fields are missing: [description, summary, category]',
                                              '<strong>Exception context:</strong> requester: requestersandesh, affected end user: affectedsandesh, request category: no request category',
                                              '[description,summary,category]')
    }

    @Test
    void status_check_with_empty_fields() {
        // arrange
        def input = '<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">\n' +
                '   <SOAP-ENV:Header/>\n' +
                '   <SOAP-ENV:Body>\n' +
                '      <CreateOnlineFormsChangeOrderProcessRequest xmlns="http://xmlns.oracle.com/CreateOnlineFormsChangeOrder">\n' +
                '         <bpelName/>\n' +
                '         <instanceID/>\n' +
                '         <assignee/>\n' +
                '         <requester/>\n' +
                '         <affected_end_user/>\n' +
                '         <co_group/>\n' +
                '         <co_type/>\n' +
                '         <cab_approval/>\n' +
                '         <category/>\n' +
                '         <summary/>\n' +
                '         <description/>\n' +
                '         <justification/>\n' +
                '         <co_number_status>123</co_number_status>\n' +
                '      </CreateOnlineFormsChangeOrderProcessRequest>\n' +
                '   </SOAP-ENV:Body>\n' +
                '</SOAP-ENV:Envelope>'
        input = new ByteArrayInputStream(input.bytes)
        mockLoginWithResponse()
        mockLogout()

        def actualDoSelectRequest = null
        mockDoSelectWithResponse(null) { DoSelect request ->
            actualDoSelectRequest = request
        }

        // act
        // normally we would use runSoapApikitFlow() here but we need to send an empty cab_approval element
        // and runSoapApikitFlow requires JAXB, which wouldn't allow us to 'mess up' that way
        runFlow('api-main-onlineFormsChangeOrder') {
            java {
                inputPayload(input)
            }
            withInputEvent { MuleEvent event ->
                // these are the things runSoapApikitFlow usually does for us
                // they allow the SOAP apikit router to function
                def message = event.message
                def setInbound = { String prop,
                                   value ->
                    message.setProperty(prop,
                                        value,
                                        PropertyScope.INBOUND)
                }
                setInbound('SOAPAction',
                           'process')
                setInbound('Content-Type',
                           'text/xml; charset=utf-8')
                setInbound('http.query.params',
                           [:])
            }
        }

        // assert
        assertThat actualDoSelectRequest.SID,
                   is(equalTo(22))
        assertThat actualDoSelectRequest.objectType,
                   is(equalTo('chg'))
        assertThat actualDoSelectRequest.whereClause,
                   is(equalTo("chg_ref_num = '123'"))
        assertThat actualDoSelectRequest.maxRows,
                   is(equalTo(1))

        def expectedAttributes = new ArrayOfString()
        expectedAttributes.string = [
                'chg_ref_num',
                'summary',
                'description',
                'status',
                'category'
        ]
        assertThat(actualDoSelectRequest.attributes,
                   samePropertyValuesAs(expectedAttributes))
    }
}
