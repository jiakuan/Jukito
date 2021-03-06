/**
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.jukito;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * This parent test class is used by {@link ParentTestClassTest}.
 */
@Ignore("Tests in this base class are not meant to be run independantly.")
public class ParentTestClassBase {

    /**
     * This should be automatically injected in the child class.
     */
    @TestSingleton
    static class SingletonDefinedInParent {
        private String value = "SingletonDefinedInParentValue";

        public String getValue() {
            return value;
        }
    }

    /**
     * This should be automatically injected in the child class.
     */
    @TestMockSingleton
    interface MockSingletonDefinedInParent {
        void mockSingletonMethod();
    }

    interface DummyInterface {
        String getDummyValue();
    }

    interface DummyInterfaceUsedOnlyInParent1 {
        String getDummyValue();
    }

    interface DummyInterfaceUsedOnlyInParent2 {
        String getDummyValue();
    }

    interface DummyInterfaceUsedOnlyInParent3 {
        String getDummyValue();
    }

    static class DummyClassUsedOnlyInParent1 {
    }

    static class DummyClassUsedOnlyInParent2 {
    }

    static class DummyClassUsedOnlyInParent3 {
    }

    @Inject
    protected Provider<DummyInterface> dummyProvider;
    @Inject
    protected MockSingletonDefinedInParent mockSingletonDefinedInParent;

    /**
     * This class keeps track of what happens in all the tests run in this
     * class and its child. It's used to make sure all expected tests are called.
     */
    protected static class Bookkeeper {
        static boolean parentTestShouldRunExecuted;
    }

    @Test
    public void parentTestShouldRun() {
        Bookkeeper.parentTestShouldRunExecuted = true;
    }

    @Test
    public void interfaceBoundInChildIsInjectedInParent() {
        assertEquals("DummyValue", dummyProvider.get().getDummyValue());
    }

    @Test
    public void interfaceBoundInChildIsInjectedInParentTestMethod(
            DummyInterface dummyInterface) {
        assertEquals("DummyValue", dummyInterface.getDummyValue());
    }

    @Test
    public void interfaceUsedInParentTestMethodShouldBeMockedAsTestSingleton(
            Provider<DummyInterfaceUsedOnlyInParent1> provider) {
        // Following should not crash
        verify(provider.get(), never()).getDummyValue();

        assertSame(provider.get(), provider.get());
    }

    @Test
    public void concreteClassUsedInParentTestMethodShouldBeBoundAsTestSingleton(
            Provider<DummyClassUsedOnlyInParent1> provider) {
        assertSame(provider.get(), provider.get());
    }

    @Before
    public void interfaceUsedInParentBeforeMethodShouldBeMockedAsTestSingleton(
            Provider<DummyInterfaceUsedOnlyInParent2> provider) {
        // Following should not crash
        verify(provider.get(), never()).getDummyValue();

        assertSame(provider.get(), provider.get());
    }

    @Before
    public void concreteClassUsedInParentBeforeMethodShouldBeBoundAsTestSingleton(
            Provider<DummyClassUsedOnlyInParent2> provider) {
        assertSame(provider.get(), provider.get());
    }

    @After
    public void interfaceUsedInParentAfterMethodShouldBeMockedAsTestSingleton(
            Provider<DummyInterfaceUsedOnlyInParent3> provider) {
        // Following should not crash
        verify(provider.get(), never()).getDummyValue();

        assertSame(provider.get(), provider.get());
    }

    @After
    public void concreteClassUsedInParentAfterMethodShouldBeBoundAsTestSingleton(
            Provider<DummyClassUsedOnlyInParent3> provider) {
        assertSame(provider.get(), provider.get());
    }
}
