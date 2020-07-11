package com.dfwairport.mule.apps.createUnicenterIncident

import com.avioconsulting.mule.testing.XmlDateHelp
import com.avioconsulting.mule.testing.junit.BaseJunitTest
import com.ca.unicenterserviceplus.servicedesk.ArrayOfString
import com.ca.unicenterserviceplus.servicedesk.CreateChangeOrder
import com.oracle.xmlns.createunicenterchangeorder.CreateUnicenterChangeOrderProcessRequest
import com.oracle.xmlns.createunicenterchangeorder.CreateUnicenterChangeOrderProcessResponse
import groovy.util.logging.Log4j2
import org.junit.Test

import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat

@Log4j2
class ChangeOrderTest extends
        BaseJunitTest implements
        SharedFlowPropertyStuff,
        XmlDateHelp {

    @Test
    void create_change_order_request_overall() {
        // arrange
        def input = new CreateUnicenterChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            affectedEndUser = 'affectedsandesh'
            summary = 'summary_val'
            description = 'sample description'
            category = 'chgcat:1234'
            justification = 'sample justification'
            it
        }

        def generic_properties = input.genericProperties = new CreateUnicenterChangeOrderProcessRequest.GenericProperties()
        (1..50).each { index ->
            generic_properties."setQ${index}"("the value")
            generic_properties."setA${index}"("the value")
        }

        def actualCreateChangeOrder = executeCreateChangeOrder(input)

        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'requestor',
                'the handle for requestersandesh',
                'affected_contact',
                'the handle for affectedsandesh',
                'summary',
                'summary_val',
                'description',
                'sample description',
                'category',
                'chgcat:1234',
                'status',
                'chgstat:6000',
                'justification',
                'sample justification'
        ]

        // assert
        assertThat actualCreateChangeOrder.SID,
                   is(equalTo(22))
        assertThat actualCreateChangeOrder.creatorHandle,
                   is(equalTo('the handle for sample_username'))
        assertThat actualCreateChangeOrder.template,
                   is(equalTo(''))
        assertThat(actualCreateChangeOrder.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
        def expectedPropertyValues = new ArrayOfString()
        (1..100).each { index ->
            expectedPropertyValues.string.add("the value")
        }
        assertThat(actualCreateChangeOrder.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues));
        def expectedAttributes = new ArrayOfString()
        expectedAttributes.string = [
                'persistent_id'
        ]
        assertThat(actualCreateChangeOrder.attributes,
                   samePropertyValuesAs(expectedAttributes))
    }

    @Test
    void description_has_url() {
        // arrange
        def input = new CreateUnicenterChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            affectedEndUser = 'affectedsandesh'
            summary = 'http://some/summaryurl'
            description = 'http://some/url'
            category = 'chgcat:1234'
            justification = 'sample justification'
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
                'summary',
                '<a href="http://some/summaryurl" target="_blank">http://some/summaryurl</a>',
                'description',
                '<a href="http://some/url" target="_blank">http://some/url</a>',
                'category',
                'chgcat:1234',
                'status',
                'chgstat:6000',
                'justification',
                'sample justification'
        ]
        assertThat(actualCreateChangeOrder.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
    }

    @Test
    void description_has_whitespace_url() {
        // arrange
        def input = new CreateUnicenterChangeOrderProcessRequest().with {
            requester = 'requestersandesh'
            affectedEndUser = 'affectedsandesh'
            summary = ' http://some/summaryurl'
            description = ' http://some/url'
            category = 'chgcat:1234'
            justification = 'sample justification'
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
                'summary',
                '<a href="http://some/summaryurl" target="_blank">http://some/summaryurl</a>',
                'description',
                '<a href="http://some/url" target="_blank">http://some/url</a>',
                'category',
                'chgcat:1234',
                'status',
                'chgstat:6000',
                'justification',
                'sample justification'
        ]
        assertThat(actualCreateChangeOrder.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
    }

    @Test
    void property_values_only_5() {
        // arrange
        def input = new CreateUnicenterChangeOrderProcessRequest().with {
            affectedEndUser = 'sandesh'
            category = 'chgcat:1234'
            description = 'the description'
            summary = 'the summary'
            it
        }

        def generic_properties = input.genericProperties = new CreateUnicenterChangeOrderProcessRequest.GenericProperties()
        (1..5).each { index ->
            generic_properties."setQ${index}"("the value")
            generic_properties."setA${index}"("the value")
        }

        //act
        def actualCreateChangeOrder = executeCreateChangeOrder(input)

        def expectedPropertyValues = new ArrayOfString()
        (1..10).each { index ->
            expectedPropertyValues.string.add("the value")
        }
        90.times {
            expectedPropertyValues.string.add('')
        }

        // assert
        assertThat(actualCreateChangeOrder.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues))
    }

    @Test
    void response_success() {
        // arrange
        def input = new CreateUnicenterChangeOrderProcessRequest().with {
            affectedEndUser = 'sandesh'
            category = 'chgcat:1234'
            description = 'the description'
            summary = 'the summary'
            it
        }

        def logger = log
        mockLoginWithResponse()
        mockGetHandleForUserIdWithResponse()
        mockCreateChangeOrderWithResponse()
        mockLogout()
        // act
        log.info 'running flow now'
        def response = runFlow(FLOW_NAME_CHANGEORDER) {
            soap {
                inputJaxbPayload(input)
            }
        } as CreateUnicenterChangeOrderProcessResponse

        // assert
        response.with {
            assertThat sessionId,
                       is(equalTo('22'))
            assertThat changeOrderNumber,
                       is(equalTo("chgcat:1234"))
            assertThat changeOrderHandle,
                       is(equalTo('the handle'))
            assertThat result,
                       is(equalTo('Success'))
            assertThat desc,
                       is(equalTo('Change Order number chgcat:1234 with Change Order handle the handle was created successfully.'))
        }
    }

    @Test
    void soap_fault_on_create_request_proper_email_sent_out() {
        // arrange
        def input = new CreateUnicenterChangeOrderProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            requester = 'requestersandesh'
            category = 'chgcat:1234'
            instanceID = 'instance123'
            description = 'the description'
            summary = 'the summary'
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
        def response = runFlow(FLOW_NAME_CHANGEORDER) {
            soap {
                inputJaxbPayload(input)
            }
        } as CreateUnicenterChangeOrderProcessResponse

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
}
