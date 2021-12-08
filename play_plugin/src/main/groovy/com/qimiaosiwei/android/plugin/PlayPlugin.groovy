package com.qimiaosiwei.android.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension


class PlayPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
//        project.task("customPlugin") {
//            doLast {
//                println("this is my plugin ~ !")
//            }
//        }
        def android = project.extensions.findByType(AppExtension)
        android.registerTransform(new TimeTransform())
    }
}
