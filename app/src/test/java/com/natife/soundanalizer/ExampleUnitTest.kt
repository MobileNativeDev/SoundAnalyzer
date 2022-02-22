package com.natife.soundanalizer

import com.natife.soundanalizer.di.modules
import org.junit.Test

import org.junit.Assert.*
import org.koin.test.KoinTest
import org.koin.test.check.checkKoinModules

class ExampleUnitTest : KoinTest {

    @Test
    fun testDependencies() {
        checkKoinModules(modules)
    }

}