package at.wolfy.observer.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Layout component for pages of application observer.
 */
@Import(stylesheet = {"context:layout/bootstrap.min.css",
                      "context:layout/sticky-footer-navbar.css",
                      "context:layout/layout.css"},
		library    = {"context:layout/bootstrap.min.js",
	                  "context:layout/jquery-2.0.3.min.js"})
public class Layout
{
    @Property
    private String pageName;

    @Inject
    private ComponentResources resources;

    public String getClassForPageName()
    {
        return resources.getPageName().equalsIgnoreCase(pageName)
                ? "active"
                : "";
    }

    public String[] getPageNames()
    {
        return new String[]{"Index", "RecordsList"};
    }
}
