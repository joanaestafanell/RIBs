package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainer
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainerInteractor
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainerRouter
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object GreetingsContainerModule {

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        buildParams: BuildParams<Nothing?>
    ): GreetingsContainerRouter =
        GreetingsContainerRouter(
            buildParams = buildParams
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: GreetingsContainerRouter,
        output: Consumer<GreetingsContainer.Output>
    ): GreetingsContainerInteractor =
        GreetingsContainerInteractor(
            buildParams = buildParams,
            router = router,
            output = output
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        router: GreetingsContainerRouter,
        interactor: GreetingsContainerInteractor
    ) : Node<Nothing> = Node(
        buildParams = buildParams,
        viewFactory = null,
        router = router,
        interactor = interactor
    )
}
