
package org.mule.modules.sqsextendedavio.generated.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.sqsextendedavio.SqsExtendedAvioConnector;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * A <code>SqsExtendedAvioConnectorProcessAdapter</code> is a wrapper around {@link SqsExtendedAvioConnector } that enables custom processing strategies.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.9.4", date = "2019-12-12T02:29:27-06:00", comments = "Build UNNAMED.2810.4347dd1")
public class SqsExtendedAvioConnectorProcessAdapter
    extends SqsExtendedAvioConnectorLifecycleInjectionAdapter
    implements ProcessAdapter<SqsExtendedAvioConnectorCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, SqsExtendedAvioConnectorCapabilitiesAdapter> getProcessTemplate() {
        final SqsExtendedAvioConnectorCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,SqsExtendedAvioConnectorCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, SqsExtendedAvioConnectorCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

            @Override
            public P execute(ProcessCallback<P, SqsExtendedAvioConnectorCapabilitiesAdapter> processCallback, Filter filter, MuleMessage message)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
