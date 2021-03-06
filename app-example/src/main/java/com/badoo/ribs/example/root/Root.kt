package com.badoo.ribs.example.root

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.auth.AuthStateStorage
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.root.Root.Input
import com.badoo.ribs.example.root.Root.Output
import com.badoo.ribs.example.root.routing.RootRouter
import com.badoo.ribs.routing.transition.handler.TransitionHandler

interface Root : Rib, Connectable<Input, Output> {

    interface Dependency {
        val api: UnsplashApi
        val authStateStorage: AuthStateStorage
    }

    sealed class Input

    sealed class Output

    class Customisation(
        val transitionHandler: TransitionHandler<RootRouter.Configuration>? = null
    ) : RibCustomisation
}
