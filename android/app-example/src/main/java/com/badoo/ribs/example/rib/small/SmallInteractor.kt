package com.badoo.ribs.example.rib.small

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.portal.Portal
import com.badoo.ribs.example.rib.small.SmallRouter.Configuration
import com.badoo.ribs.example.rib.small.SmallRouter.Configuration.Content
import com.badoo.ribs.example.rib.small.SmallRouter.Configuration.FullScreen
import com.badoo.ribs.example.rib.small.SmallView.Event
import com.badoo.ribs.example.rib.small.SmallView.ViewModel
import io.reactivex.functions.Consumer

class SmallInteractor(
    buildParams: BuildParams<Nothing?>,
    portal: Portal.OtherSide,
    router: Router<Configuration, *, Content, Nothing, SmallView>
) : Interactor<Configuration, Content, Nothing, SmallView>(
    buildParams = buildParams,
    router = router,
    disposables = null
) {
    override fun onViewCreated(view: SmallView, viewLifecycle: Lifecycle) {
        view.accept(ViewModel("My id: " + id.replace("${SmallInteractor::class.java.name}.", "")))
        viewLifecycle.startStop {
            bind(view to viewEventConsumer)
        }
    }

    private val viewEventConsumer: Consumer<Event> = Consumer {
        when (it) {
            Event.OpenBigClicked -> portal.showContent(router, FullScreen.ShowBig)
            Event.OpenOverlayClicked -> portal.showOverlay(router, FullScreen.ShowOverlay)
        }
    }
}
