
package org.mule.modules.sqsextendedavio.generated.adapters;

import javax.annotation.Generated;
import org.mule.api.MetadataAware;
import org.mule.modules.sqsextendedavio.SqsExtendedAvioConnector;


/**
 * A <code>SqsExtendedAvioConnectorMetadataAdapter</code> is a wrapper around {@link SqsExtendedAvioConnector } that adds support for querying metadata about the extension.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.9.4", date = "2019-12-12T02:29:27-06:00", comments = "Build UNNAMED.2810.4347dd1")
public class SqsExtendedAvioConnectorMetadataAdapter
    extends SqsExtendedAvioConnectorCapabilitiesAdapter
    implements MetadataAware
{

    private final static String MODULE_NAME = "SqsExtendedAvio";
    private final static String MODULE_VERSION = "1.0.0";
    private final static String DEVKIT_VERSION = "3.9.4";
    private final static String DEVKIT_BUILD = "UNNAMED.2810.4347dd1";
    private final static String MIN_MULE_VERSION = "3.5.0";

    public String getModuleName() {
        return MODULE_NAME;
    }

    public String getModuleVersion() {
        return MODULE_VERSION;
    }

    public String getDevkitVersion() {
        return DEVKIT_VERSION;
    }

    public String getDevkitBuild() {
        return DEVKIT_BUILD;
    }

    public String getMinMuleVersion() {
        return MIN_MULE_VERSION;
    }

}
