package com.badoo.ribs.tutorials.tutorial1.rib.hello_world.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorld.Output
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorldRouter
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorldView
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object HelloWorldModule {

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun router(
        buildParams: BuildParams<Nothing?>
    ): HelloWorldRouter =
        HelloWorldRouter(
            buildParams = buildParams
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: HelloWorldRouter,
        output: Consumer<Output>
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            buildParams = buildParams,
            router = router,
            output = output
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: HelloWorld.Customisation,
        router: HelloWorldRouter,
        interactor: HelloWorldInteractor
    ) : Node<HelloWorldView> = Node(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
