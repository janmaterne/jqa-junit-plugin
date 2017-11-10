package com.buschmais.jqassistant.plugin.junit.test.rule;

import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher;
import com.buschmais.jqassistant.plugin.junit.test.set.junit5.DisabledTestClass;
import com.buschmais.jqassistant.plugin.junit.test.set.junit5.StandardTest;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.MethodDescriptorMatcher.methodDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public class Junit5IT extends AbstractJavaPluginIT {

    @After
    public void commitTransaction() {
        if (store.hasActiveTransaction()) {
            store.commitTransaction();
        }
    }

    @Test
    public void activeTestMethodFound() throws Exception {
        scanClasses(StandardTest.class);
        assertThat(applyConcept("junit5:TestMethod").getStatus(), equalTo(SUCCESS));

        store.beginTransaction();

        assertThat(query("MATCH (m:Method:Junit5:Test) RETURN m").getColumn("m"),
                   hasItem(methodDescriptor(StandardTest.class, "activeTest")));
    }

    @Test
    public void disabledTestMethodFound() throws Exception {
        scanClasses(StandardTest.class);
        assertThat(applyConcept("junit5:TestMethod").getStatus(), equalTo(SUCCESS));
        assertThat(applyConcept("junit5:DisabledTestClassOrMethod").getStatus(), equalTo(SUCCESS));

        store.beginTransaction();

        assertThat(query("MATCH (m:Method:Junit5:Test) RETURN m").getColumn("m"),
                   hasItem(methodDescriptor(StandardTest.class, "disabledTest")));
        assertThat(query("MATCH (m:Method:Junit5:Test:Ignore) RETURN m").getColumn("m"),
                   hasItem(methodDescriptor(StandardTest.class, "disabledTest")));
        assertThat(query("MATCH (m:Method:Junit5:Test:Disabled) RETURN m").getColumn("m"),
                   hasItem(methodDescriptor(StandardTest.class, "disabledTest")));
    }

    @Test
    public void disabledTestClass() throws Exception {
        scanClasses(DisabledTestClass.class);
        assertThat(applyConcept("junit5:TestMethod").getStatus(), equalTo(SUCCESS));
        assertThat(applyConcept("junit5:DisabledTestClassOrMethod").getStatus(), equalTo(SUCCESS));

        store.beginTransaction();

        assertThat(query("MATCH (c:Class:Junit5:Ignore) RETURN c").getColumn("c"),
                   hasItem(TypeDescriptorMatcher.typeDescriptor(DisabledTestClass.class)));
        assertThat(query("MATCH (c:Class:Junit5:Disabled) RETURN c").getColumn("c"),
                   hasItem(TypeDescriptorMatcher.typeDescriptor(DisabledTestClass.class)));
    }

    @Test
    public void beforeEachMethodFound() throws Exception {
        scanClasses(StandardTest.class);
        assertThat(applyConcept("junit5:BeforeEach").getStatus(), equalTo(SUCCESS));

        store.beginTransaction();

        assertThat(query("MATCH (m:Method:Junit5:BeforeEach) RETURN m").getColumn("m"),
                   hasItem(methodDescriptor(StandardTest.class, "beforeEach")));
        assertThat(query("MATCH (m:Method:Junit5:Before) RETURN m").getColumn("m"),
                   hasItem(methodDescriptor(StandardTest.class, "beforeEach")));
    }

    @Ignore
    @Test
    public void beforeAllMethodFound() throws Exception {
        throw new RuntimeException("Not implemented yet!");
    }

    @Test
    public void afterEachMethodFound() throws Exception {
        scanClasses(StandardTest.class);
        assertThat(applyConcept("junit5:AfterEach").getStatus(), equalTo(SUCCESS));

        store.beginTransaction();

        assertThat(query("MATCH (m:Method:Junit5:AfterEach) RETURN m").getColumn("m"),
                   hasItem(methodDescriptor(StandardTest.class, "afterEach")));
        assertThat(query("MATCH (m:Method:Junit5:After) RETURN m").getColumn("m"),
                   hasItem(methodDescriptor(StandardTest.class, "afterEach")));
    }

    @Ignore
    @Test
    public void afterAllMethodFound() throws Exception {
        throw new RuntimeException("Not implemented yet!");
    }
}