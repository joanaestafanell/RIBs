package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerRouter.Configuration
import io.reactivex.functions.Consumer
import com.badoo.ribs.core.builder.BuildParams

class GreetingsContainerInteractor(
    buildParams: BuildParams<Nothing?>,
    router: Router<Configuration, Nothing, Configuration, Nothing, Nothing>,
    output: Consumer<GreetingsContainer.Output>
) : Interactor<Configuration, Configuration, Nothing, Nothing>(
    buildParams = buildParams,
    router = router,
    disposables = null
) {

}
