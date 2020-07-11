package com.dfwairport.mule.apps.createUnicenterIncident

import com.avioconsulting.mule.testing.junit.BaseJunitTest
import com.avioconsulting.mule.testing.junit.MuleGroovyParameterizedRunner
import com.ca.unicenterserviceplus.servicedesk.ArrayOfString
import com.ca.unicenterserviceplus.servicedesk.DoSelect
import com.oracle.xmlns.createunicenterrequest.CreateUnicenterRequestProcessRequest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mule.api.MuleEvent

import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat

@RunWith(MuleGroovyParameterizedRunner)
class DoSelectTest extends
        BaseJunitTest implements
        SharedFlowPropertyStuff {
    private final String impact, priority, severity, urgency
    private final int expectedUnicenterCalls
    private final Map<String, String> expectedOutputs

    DoSelectTest(String ignoredDescription,
                 String impact,
                 String priority,
                 String severity,
                 String urgency,
                 int expectedUnicenterCalls,
                 Map<String, String> expectedOutputs) {
        this.impact = impact
        this.priority = priority
        this.severity = severity
        this.urgency = urgency
        this.expectedUnicenterCalls = expectedUnicenterCalls
        this.expectedOutputs = expectedOutputs
    }

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        [
                [
                        'no special flags',
                        null,
                        null,
                        null,
                        null,
                        0,
                        [:]
                ],
                [
                        'special flags = impact',
                        'medium',
                        null,
                        null,
                        null,
                        1,
                        [impact: 'code for medium']
                ],
                [
                        'special flags = priority',
                        null,
                        'high',
                        null,
                        null,
                        1,
                        [priority: 'code for high']
                ],
                [
                        'special flags = severity',
                        null,
                        null,
                        'low',
                        null,
                        1,
                        [severity: 'code for low']
                ],
                [
                        'special flags = urgency',
                        null,
                        null,
                        null,
                        'medium',
                        1,
                        [urgency: 'code for medium']
                ],
                [
                        'special flags = all',
                        'medium',
                        'high',
                        'low',
                        'medium',
                        4,
                        [
                                impact  : 'code for medium',
                                priority: 'code for high',
                                severity: 'code for low',
                                urgency : 'code for medium'
                        ]
                ],
                [
                        'special flags = exception from DoSelect',
                        null,
                        'exception value', // priority
                        null,
                        null,
                        1, // expected # of unicenter calls
                        [:] // priority lookup will fail but that should be OK, we should have an empty map
                ],
                [
                        'special flags = no values returned from DoSelect',
                        null,
                        'random value', // priority
                        null,
                        null,
                        1, // expected # of unicenter calls
                        [:] // priority lookup will fail but that should be OK, we should have an empty map
                ]
        ].collect { list ->
            // JUnit expects object arrays not lists, so we convert each line above here
            list.toArray(new Object[0])
        }
    }

    @Test
    void request_to_resolve_flags_is_correct() {
        // arrange
        def input = null
        List<String> expectedFlags = []
        List<String> expectedKeys = []

        input = new CreateUnicenterRequestProcessRequest()
        if (this.impact) {
            input.impact = impact
            expectedFlags << impact
            expectedKeys << 'imp'
        }
        if (this.priority) {
            input.priority = priority
            expectedFlags << priority
            expectedKeys << 'pri'
        }
        if (this.severity) {
            input.severity = severity
            expectedFlags << severity
            expectedKeys << 'sev'
        }
        if (this.urgency) {
            input.urgency = urgency
            expectedFlags << urgency
            expectedKeys << 'urg'
        }

        List<DoSelect> actualRequest = []
        // we're not interested in the response in this test, just the query
        def successResponse = 'sample_do_select_low_response.xml'
        mockDoSelectWithResponse(successResponse) { DoSelect request ->
            actualRequest << request
        }
        // act

        runFlow('doSelectFlowWithStreamOfXML') {
            soap {
                inputJaxbPayload(input)
            }
        }

        // assert
        assertThat actualRequest.size(),
                   is(equalTo(this.expectedUnicenterCalls))
        def counter = 0 //counter to get the special flag for each iteration
        actualRequest.each { r ->
            assertThat r.SID,
                       is(equalTo(22))
            assertThat r.objectType,
                       is(equalTo(expectedKeys[counter]))
            assertThat r.whereClause,
                       is(equalTo("sym = '" + expectedFlags[counter] + "'"))
            assertThat r.maxRows,
                       is(equalTo(1))

            def expectedAttributes = new ArrayOfString()
            expectedAttributes.string = [
                    'enum'
            ]
            assertThat(r.attributes,
                       samePropertyValuesAs(expectedAttributes))
            counter++
        }
    }

    @Test
    void output_is_correct() {
        // arrange
        def input = null

        input = new CreateUnicenterRequestProcessRequest()
        if (this.impact) {
            input.impact = impact
        }
        if (this.priority) {
            input.priority = priority
        }
        if (this.severity) {
            input.severity = severity
        }
        if (this.urgency) {
            input.urgency = urgency
        }

        def logger = getLogger()

        mockDoSelectWithResponse()

        // act
        MuleEvent saveOutput = null
        logger.info 'running flow doselect flow now'
        runFlow('doSelectFlowWithStreamOfXML') {
            soap {
                inputJaxbPayload(input)
            }
            withOutputEvent { MuleEvent output ->
                saveOutput = output
            }
        }

        // assert
        assertThat saveOutput.getFlowVariable('specialFlags'),
                   is(equalTo(this.expectedOutputs))
    }
}
