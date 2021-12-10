package com.qimiaosiwei.android.plugin

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class TimeClassVisitor extends ClassVisitor {

    TimeClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor)
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        println(System.currentTimeMillis() + " TimeClassVisitor visitAnnotation desc " + descriptor + " visible " + visible)
        return super.visitAnnotation(descriptor, visible)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        println(System.currentTimeMillis() + " TimeClassVisitor method " + name + " desc " + descriptor + " signature " + signature)
            MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
            return new TimeClassAdapter(api, methodVisitor, access, name, descriptor)
    }
}