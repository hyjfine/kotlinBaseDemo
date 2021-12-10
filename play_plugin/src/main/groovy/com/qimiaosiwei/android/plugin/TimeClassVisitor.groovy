package com.qimiaosiwei.android.plugin

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.TypePath

class TimeClassVisitor extends ClassVisitor {

    TimeClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor)
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        println("TimeClassVisitor visitAnnotation desc " + descriptor + " visible " + visible)
        return super.visitAnnotation(descriptor, visible)
    }

    @Override
    AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        println("TimeClassVisitor visitTypeAnnotation typePath " + typePath + " desc " + descriptor + " visible " + visible)
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        println("TimeClassVisitor method " + name + " desc " + descriptor + " signature " + signature)
        if (name.endsWith("ST")) {
            MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
            return new TimeClassAdapter(api, methodVisitor, access, name, descriptor)
        } else
            return super.visitMethod(access, name, descriptor, signature, exceptions)
    }
}