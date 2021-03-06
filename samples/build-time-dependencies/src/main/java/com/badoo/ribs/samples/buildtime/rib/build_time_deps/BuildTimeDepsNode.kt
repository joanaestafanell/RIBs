package com.badoo.ribs.samples.buildtime.rib.build_time_deps

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class BuildTimeDepsNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> BuildTimeDepsView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<BuildTimeDepsView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), BuildTimeDeps
