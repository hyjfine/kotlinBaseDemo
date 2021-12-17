package com.qimiaosiwei.android.plugins

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

/**
 * author : heyongjian
 * time   : 2021/12/16
 * desc   :
 */
class TimeClassVisitors(api: Int, classVisitor: ClassVisitor?) : ClassVisitor(api, classVisitor) {
    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        println("" + System.currentTimeMillis() + " TimeClassVisitor visitAnnotation desc " + descriptor + " visible " + visible)
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        println("" + System.currentTimeMillis() + " TimeClassVisitor method " + name + " desc " + descriptor + " signature " + signature)
        val methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
        return TimeClassAdapters(api, methodVisitor, access, name, descriptor)
    }
}