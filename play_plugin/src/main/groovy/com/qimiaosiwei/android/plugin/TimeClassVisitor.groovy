package com.qimiaosiwei.android.plugin

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class TimeClassVisitor extends ClassVisitor {


    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return super.visitAnnotation(desc, visible)
    }

    TimeClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
        System.out.println("TimeClassVisitor method " + name + " desc " + descriptor + " signature " + signature)
//        return new TimeClassAdapter(api, methodVisitor, access, name, descriptor)
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }
}