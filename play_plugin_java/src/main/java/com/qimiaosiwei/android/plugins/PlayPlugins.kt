package com.qimiaosiwei.android.plugins

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * author : heyongjian
 * time   : 2021/12/16
 * desc   :
 */
class PlayPlugins : Plugin<Project> {
    override fun apply(target: Project) {
        println("this is my kotlin plugin!")
        val extension = target.extensions.getByType(AppExtension::class.java)
        extension.registerTransform(TimeTransforms())
    }
}