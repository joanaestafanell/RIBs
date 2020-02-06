package com.badoo.ribs.test.util.ribs.root

import androidx.lifecycle.Lifecycle
import android.os.Bundle
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.test.util.LifecycleObserver
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.child.TestChildView
import com.badoo.ribs.test.util.ribs.child.builder.TestChildBuilder
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import com.badoo.ribs.test.util.ribs.root.builder.TestRootBuilder
import io.reactivex.observers.TestObserver
import java.util.UUID

interface TestRoot : Rib {

    interface Dependency {
        fun viewLifecycleObserver(): TestObserver<Lifecycle.Event>
        fun nodeLifecycleObserver(): TestObserver<Lifecycle.Event>
        fun router(): TestRootRouter
    }

    class Provider(
        private val initialConfiguration: Configuration.Content = Configuration.Content.NoOp,
        private val permanentParts: List<Configuration.Permanent> = emptyList()
    ) {

        val viewLifecycleObserver: LifecycleObserver = LifecycleObserver()
        val nodeLifecycleObserver: LifecycleObserver = LifecycleObserver()

        var permanentNode1: TestNode<*>? = null
            private set
        var permanentNode2: TestNode<*>? = null
            private set
        var childNode1: TestNode<*>? = null
            private set
        var childNode2: TestNode<*>? = null
            private set
        var childNode3: TestNode<*>? = null
            private set
        var rootNode: TestNode<*>? = null
            private set

        private fun builder(block: (TestNode<TestChildView>) -> Unit): (BuildParams.Params) -> TestNode<TestChildView> = {
            TestChildBuilder().build(it).also {
                block.invoke(it)
            }
        }

        fun create(dialogLauncher: DialogLauncher, savedInstanceState: Bundle?): TestNode<TestRootView> {
            val router = TestRootRouter(
                buildParams = BuildParams.Resolved(
                    ancestryInfo = AncestryInfo.Root,
                    viewAttachMode = AttachMode.PARENT,
                    savedInstanceState = savedInstanceState,
                    identifier = Rib.Identifier(
                        rib = object : TestRoot {},
                        uuid = UUID.randomUUID()
                    )
                ),
                builderPermanent1 = builder { permanentNode1 = it },
                builderPermanent2 = builder { permanentNode2 = it },
                builder1 = builder { childNode1 = it },
                builder2 = builder { childNode2 = it },
                builder3 = builder { childNode3 = it },
                initialConfiguration = initialConfiguration,
                permanentParts = permanentParts,
                dialogLauncher = dialogLauncher
            )

            val node = TestRootBuilder(
                object : Dependency {
                    override fun viewLifecycleObserver() = viewLifecycleObserver
                    override fun nodeLifecycleObserver() = nodeLifecycleObserver
                    override fun router() = router

                }
            ).build(
                BuildParams.Params(
                    ancestryInfo = AncestryInfo.Root,
                    savedInstanceState = savedInstanceState
                )
            ) as TestNode<TestRootView>
            rootNode = node
            return node
        }
    }
}
