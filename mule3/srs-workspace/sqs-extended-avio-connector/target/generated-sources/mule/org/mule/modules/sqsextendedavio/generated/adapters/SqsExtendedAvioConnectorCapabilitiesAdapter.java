
package org.mule.modules.sqsextendedavio.generated.adapters;

import javax.annotation.Generated;
import org.mule.api.devkit.capability.Capabilities;
import org.mule.api.devkit.capability.ModuleCapability;
import org.mule.modules.sqsextendedavio.SqsExtendedAvioConnector;


/**
 * A <code>SqsExtendedAvioConnectorCapabilitiesAdapter</code> is a wrapper around {@link SqsExtendedAvioConnector } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.9.4", date = "2019-12-12T02:29:27-06:00", comments = "Build UNNAMED.2810.4347dd1")
public class SqsExtendedAvioConnectorCapabilitiesAdapter
    extends SqsExtendedAvioConnector
    implements Capabilities
{


    /**
     * Returns true if this module implements such capability
     * 
     */
    public boolean isCapableOf(ModuleCapability capability) {
        if (capability == ModuleCapability.LIFECYCLE_CAPABLE) {
            return true;
        }
        return false;
    }

}
