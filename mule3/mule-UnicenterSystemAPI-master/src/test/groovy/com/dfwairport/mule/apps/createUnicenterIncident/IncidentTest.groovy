package com.dfwairport.mule.apps.createUnicenterIncident

import com.avioconsulting.mule.testing.XmlDateHelp
import com.avioconsulting.mule.testing.junit.BaseJunitTest
import com.ca.unicenterserviceplus.servicedesk.ArrayOfString
import com.ca.unicenterserviceplus.servicedesk.CreateRequest
import com.ca.unicenterserviceplus.servicedesk.CreateRequestResponse
import com.oracle.xmlns.createunicenterincident.CreateUnicenterIncidentProcessRequest
import com.oracle.xmlns.createunicenterincident.CreateUnicenterIncidentProcessResponse
import groovy.util.logging.Log4j2
import org.junit.Test
import org.mule.api.MuleEvent

import java.time.ZonedDateTime

import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat

@Log4j2
class IncidentTest extends
        BaseJunitTest implements
        SharedFlowPropertyStuff,
        XmlDateHelp {
    @Override
    List<String> keepListenersOnForTheseFlows() {
        ['api-main-incident']
    }

    @Test
    void validation_empty_optional_elements_supplied() {
        // arrange
        def logger = getLogger()
        def input = new CreateUnicenterIncidentProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            incidentArea = "pcat:123"
            summary = 'the summary'
            description = 'the description'
            fuelSystemFuel = new CreateUnicenterIncidentProcessRequest.FuelSystemFuel()
            fuelSystemMeterReading = new CreateUnicenterIncidentProcessRequest.FuelSystemMeterReading()
            fuelSystemTrakSync = new CreateUnicenterIncidentProcessRequest.FuelSystemTrakSync()
            it
        }
        mockLoginWithResponse()
        mockLogout()
        mockGetHandleForUserIdWithResponse()
        CreateRequest actualCreateRequest = null
        mockSoapCall('Create Request') {
            whenCalledWithJaxb(CreateRequest) { CreateRequest createRequest ->
                logger.info 'Mock of Unicenter Create Request called'
                actualCreateRequest = createRequest
                new CreateRequestResponse()
            }
        }

        // act
        def result = runSoapApikitFlow('process',
                                       'api-main-incident') {
            inputJaxbPayload(input)
        } as MuleEvent

        // assert
        assertThat result.messageAsString,
                   is(containsString('was created successfully'))
        assertThat actualCreateRequest,
                   is(not(nullValue()))
    }

    @Test
    void validation_does_not_remove_non_empties() {
        // arrange
        def logger = getLogger()
        def input = new CreateUnicenterIncidentProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            incidentArea = 'pcat:2243749'
            summary = 'the summary'
            description = 'the description'
            fuelSystemFuel = new CreateUnicenterIncidentProcessRequest.FuelSystemFuel().with {
                ADDFUELEQUIPMENTCODE = 'the equip code'
                it
            }
            it
        }
        mockLoginWithResponse()
        mockLogout()
        mockGetHandleForUserIdWithResponse()
        CreateRequest actualCreateRequest = null
        mockSoapCall('Create Request') {
            whenCalledWithJaxb(CreateRequest) { CreateRequest createRequest ->
                logger.info 'Mock of Unicenter Create Request called'
                actualCreateRequest = createRequest
                new CreateRequestResponse()
            }
        }

        // act
        def result = runSoapApikitFlow('process',
                                       'api-main-incident') {
            inputJaxbPayload(input)
        } as MuleEvent

        // assert
        assertThat result.messageAsString,
                   is(containsString('was created successfully'))
        assertThat actualCreateRequest,
                   is(not(nullValue()))
        assertThat actualCreateRequest.propertyValues.string.unique(),
                   is(equalTo(['-',
                               'the equip code']))
    }

    @Test
    void validation_does_not_prevent_wsdl_fetch() {
        // arrange

        // act
        logger.info 'WSDL fetch'
        def result = new URL('http://localhost:8081/createUnicenterIncident/v1/CreateUnicenterIncident_OSB_Client/CreateUnicenterIncident_OSB_ClientDirectBindingPort11?wsdl').text

        // assert
        assertThat result,
                   is(containsString('<definitions'))
        logger.info 'XSD fetch'
        result = new URL('http://localhost:8081/createUnicenterIncident/v1/CreateUnicenterIncident_OSB_Client/CreateUnicenterIncident_OSB_ClientDirectBindingPort11?xsd=CreateUnicenterIncident.xsd').text
        assertThat result,
                   is(containsString('<schema'))
    }

    @Test
    void create_request_have_affected_end_user() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest().with {
            affectedEndUser = 'affectedsandesh'
            incidentArea = "pcat:123"
            it
        }

        // act
        def attrVals = executeCreateRequest(input).attrVals

        // assert
        assertThat(attrVals.string,
                   hasItems('customer',
                            'the handle for affectedsandesh'))
    }

    @Test
    void create_request_do_not_have_affected_end_user() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest().with {
            it.incidentArea = "pcat:123"
            it
        }

        // act
        def attrVals = executeCreateRequest(input).attrVals

        // assert
        assertThat(attrVals.string,
                   hasItems('customer',
                            'the handle for sample_username'))
        //sample_username is main user
    }

    @Test
    void create_request_overall() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest().with {
            affectedEndUser = 'sandesh'
            incidentArea = 'pcat:123'
            summary = 'summary_val'
            description = 'sample description'
            it
        }

        def actualCreateRequest = executeCreateRequest(input)

        def expectedAttrVals = new ArrayOfString()
        expectedAttrVals.string = [
                'customer',
                'the handle for sandesh',
                'category',
                'pcat:123',
                'summary',
                'summary_val',
                'description',
                'sample description',
                'status',
                'crs:5200',
                'type',
                'I'
        ]

        // assert
        assertThat actualCreateRequest.SID,
                   is(equalTo(22))
        assertThat actualCreateRequest.creatorHandle,
                   is(equalTo('the handle for sample_username'))
        assertThat actualCreateRequest.template,
                   is(equalTo(''))
        assertThat(actualCreateRequest.attrVals,
                   samePropertyValuesAs(expectedAttrVals))
        def expectedAttributes = new ArrayOfString()
        expectedAttributes.string = [
                'persistent_id'
        ]
        assertThat(actualCreateRequest.attributes,
                   samePropertyValuesAs(expectedAttributes))
    }


    @Test
    void property_value_pcat_2243749() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest()
        input.incidentArea = 'pcat:2243749'
        input.fuelSystemFuel = new CreateUnicenterIncidentProcessRequest.FuelSystemFuel()
        input.fuelSystemFuel.with {
            ADDFUELDEPOTCODE = 'depot code'
            ADDFUELPUMPCODE = 'pump code'
            ADDFUELFUELCODE = 'fuel code'
            ADDFUELEQUIPMENTCODE = 'equipment code'
            ADDFUELPERSONCODE = 'person code'
            ADDFUELQUANTITYVALUE = 10
            ADDFUELTRANSACTIONDATE = getXmlDateTime(ZonedDateTime.parse('2018-02-17T13:00:00-06:00'))
            ADDFUELPRICEVALUE = 100
        }

        def actualCreateRequest = executeCreateRequest(input)

        def expectedPropertyValues = new ArrayOfString()
        expectedPropertyValues.string = [
                'depot code',
                'pump code',
                'fuel code',
                'equipment code',
                'person code',
                '10',
                // to keep formats in unicenter consistent, we'll expect our service to convert this to current DFW time
                '02/17/2018 13:00',
                '100'
        ]

        // assert
        assertThat(actualCreateRequest.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues))
    }

    @Test
    void property_value_pcat_2243749_no_transaction_date() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest()
        input.incidentArea = 'pcat:2243749'
        input.fuelSystemFuel = new CreateUnicenterIncidentProcessRequest.FuelSystemFuel()
        input.fuelSystemFuel.with {
            ADDFUELDEPOTCODE = 'depot code'
            ADDFUELPUMPCODE = 'pump code'
            ADDFUELFUELCODE = 'fuel code'
            ADDFUELEQUIPMENTCODE = 'equipment code'
            ADDFUELPERSONCODE = 'person code'
            ADDFUELQUANTITYVALUE = 10
            ADDFUELPRICEVALUE = 100
        }

        // act
        def actualCreateRequest = executeCreateRequest(input)

        def expectedPropertyValues = new ArrayOfString()
        expectedPropertyValues.string = [
                'depot code',
                'pump code',
                'fuel code',
                'equipment code',
                'person code',
                '10',
                '-', // will use this if no transaction date is supplied
                '100'
        ]

        // assert
        assertThat(actualCreateRequest.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues));
    }

    @Test
    void property_value_optional_value() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest()
        input.incidentArea = 'pcat:2243749'
        input.fuelSystemFuel = new CreateUnicenterIncidentProcessRequest.FuelSystemFuel()
        input.fuelSystemFuel.with {
            ADDFUELDEPOTCODE = 'depot code'
            ADDFUELPUMPCODE = 'pump code'
            ADDFUELFUELCODE = 'fuel code'
            ADDFUELEQUIPMENTCODE = 'equipment code'
            ADDFUELPERSONCODE = 'person code'
            ADDFUELQUANTITYVALUE = 10
            ADDFUELTRANSACTIONDATE = getXmlDateTime(ZonedDateTime.parse('2018-02-17T13:00:00-06:00'))
            // no price value
        }

        def actualCreateRequest = executeCreateRequest(input)

        def expectedPropertyValues = new ArrayOfString()
        expectedPropertyValues.string = [
                'depot code',
                'pump code',
                'fuel code',
                'equipment code',
                'person code',
                '10',
                // to keep formats in unicenter consistent, we'll expect our service to convert this to current DFW time
                '02/17/2018 13:00',
                '-' // price value
        ]

        // assert
        assertThat(actualCreateRequest.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues));
    }

    @Test
    void property_value_pcat_2243751() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest()
        input.incidentArea = 'pcat:2243751'
        input.fuelSystemMeterReading = new CreateUnicenterIncidentProcessRequest.FuelSystemMeterReading()
        input.fuelSystemMeterReading.with {
            ADDREADINGEQUIPMENTCODE = 'equipment code'
            ADDREADINGUOM = 'uom'
            ADDREADINGMETERREADINGDATETIME = getXmlDateTime(ZonedDateTime.parse('2018-02-17T13:00:00-06:00'))
            ADDREADINGHOURSMETERREADING = 'hours meter reading'
            ADDREADINGCURRENTMETERREADING = 'current meter reading'
        }

        def actualCreateRequest = executeCreateRequest(input)

        def expectedPropertyValues = new ArrayOfString()
        expectedPropertyValues.string = [
                'equipment code',
                'uom',
                // to keep formats in unicenter consistent, we'll expect our service to convert this to current DFW time
                '02/17/2018 13:00',
                'hours meter reading',
                'current meter reading'
        ]

        // assert
        assertThat(actualCreateRequest.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues));
    }

    @Test
    void property_value_pcat_2243752() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest()
        input.incidentArea = 'pcat:2243752'
        input.fuelSystemTrakSync = new CreateUnicenterIncidentProcessRequest.FuelSystemTrakSync()
        input.fuelSystemTrakSync.with {
            TRAKTYPE = 'trak type'
            TRAKACTION = 'trak action'
            PERSONCODE = 'person code'
            PERSONDESCRIPTION = 'person description'
            VEHCODE = 'veh code'
            VEHDESCRIPTION = 'veh description'
            VEHMAXFILLUPMILES = 'veh max fillup miles'
            VEHTANKCAPACITY = 'veh tank capacity'
        }

        def actualCreateRequest = executeCreateRequest(input)

        def expectedPropertyValues = new ArrayOfString()
        expectedPropertyValues.string = [
                'trak type',
                'trak action',
                'person code',
                'person description',
                'veh code',
                'veh description',
                'veh max fillup miles',
                'veh tank capacity'
        ]

        // assert
        assertThat(actualCreateRequest.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues));
    }

    def populateGenericProperties(CreateUnicenterIncidentProcessRequest input,
                                  int count) {
        def generic = input.incidentPropertiesGeneric = new CreateUnicenterIncidentProcessRequest.IncidentPropertiesGeneric()
        (1..count).each { index ->
            generic."setIncidentProp${index}Value"("the value for ${index}")
        }
    }

    @Test
    void incident_properties_generic_all_50() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest()
        input.incidentArea = 'pcat:123'
        populateGenericProperties(input,
                                  50)

        def actualCreateRequest = executeCreateRequest(input)

        def expectedPropertyValues = new ArrayOfString()
        (1..50).each { index ->
            expectedPropertyValues.string.add("the value for ${index}".toString())
        }

        // assert
        assertThat(actualCreateRequest.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues));
    }

    @Test
    void incident_properties_generic_only_5() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest()
        input.incidentArea = 'pcat:123'
        populateGenericProperties(input,
                                  5)

        def actualCreateRequest = executeCreateRequest(input)

        def expectedPropertyValues = new ArrayOfString()
        (1..5).each { index ->
            expectedPropertyValues.string.add("the value for ${index}".toString())
        }
        45.times {
            expectedPropertyValues.string.add('-')
        }

        // assert
        assertThat(actualCreateRequest.propertyValues,
                   samePropertyValuesAs(expectedPropertyValues));
    }

    @Test
    void response_success() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest().with {
            affectedEndUser = 'sandesh'
            incidentArea = "pcat:123"
            it
        }

        def logger = log
        mockLoginWithResponse()
        mockGetHandleForUserIdWithResponse()
        mockCreateRequestWithIncidentTypeResponse()
        mockLogout()

        // act
        log.info 'running flow now'
        def response = runFlow(FLOW_NAME_INCIDENT) {
            soap {
                inputJaxbPayload(input)
            }
        } as CreateUnicenterIncidentProcessResponse

        // assert
        response.with {
            assertThat sessionId,
                       is(equalTo('22'))
            assertThat incidentId,
                       is(equalTo("incident:1234"))
            assertThat requestHandle,
                       is(equalTo('the handle'))
            assertThat result,
                       is(equalTo('Success'))
            assertThat desc,
                       is(equalTo('Incident incident:1234 with session-id 22 was created successfully.'))
        }
    }

    @Test
    void soap_fault_on_create_request_proper_email_sent_out() {
        // arrange
        def input = new CreateUnicenterIncidentProcessRequest().with {
            affectedEndUser = 'sandesh'
            incidentArea = 'pcat:123'
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

        mockUnicenterFailure(CreateRequest,
                             'Create Request',
                             'some failure message')

        // act
        def response = runFlow(FLOW_NAME_INCIDENT) {
            soap {
                inputJaxbPayload(input)
            }
        } as CreateUnicenterIncidentProcessResponse

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
                   is(containsString('<strong>Exception context:</strong> affected end user: sandesh, incident area: pcat:123'))

        response.with {
            assertThat result,
                       is(equalTo('Error'))
            assertThat desc,
                       is(equalTo('This request was not created due to the fault exception: some failure message.; The instance ID is: instance123, please check the console for more details.'))
        }
    }
}
