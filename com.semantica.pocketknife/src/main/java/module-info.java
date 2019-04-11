module com.semantica.pocketknife {
    exports com.semantica.pocketknife;
    exports com.semantica.pocketknife.calls;
    exports com.semantica.pocketknife.methodrecorder;
    exports com.semantica.pocketknife.mock;
    exports com.semantica.pocketknife.pojo;
    exports com.semantica.pocketknife.util;

    requires javax.inject;
    requires slf4j.api;
    requires org.apache.commons.lang3;

    requires net.bytebuddy;
    requires org.objenesis;

    requires org.hamcrest;
    requires org.opentest4j;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;

    opens com.semantica.pocketknife.methodrecorder.dynamicproxies to net.bytebuddy;
    opens com.semantica.pocketknife to com.fasterxml.jackson.databind;
}