package com.qimiaosiwei.android.plugin

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

class TimeClassAdapter extends AdviceAdapter {
    private isModify = false

    protected TimeClassAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor)
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        println(System.currentTimeMillis() + " TimeClassAdapter descriptor " + descriptor + " visible " + visible)
        isModify = descriptor == "Lcom/qimiaosiwei/android/playasm/StatisticTime;"
        return super.visitAnnotation(descriptor, visible)
    }

    @Override
    protected void onMethodEnter() {
        println(System.currentTimeMillis() + " TimeClassAdapter start : name " + name + " isModify " + isModify)
        if (!isModify) return
        visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
        visitVarInsn(LSTORE, 0)
        super.onMethodEnter()
    }

    @Override
    protected void onMethodExit(int opcode) {
        println(System.currentTimeMillis() + " TimeClassAdapter end : name " + name + " isModify " + isModify)
        if (!isModify) return
        visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
        visitVarInsn(LSTORE, 2)
        visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        visitTypeInsn(NEW, "java/lang/StringBuilder")
        visitInsn(DUP)
        visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false)
        visitLdcInsn(name + " executed time: ")
        visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false)
        visitVarInsn(LLOAD, 2)
        visitVarInsn(LLOAD, 0)
        visitInsn(LSUB)
        visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false)
        visitLdcInsn(" :ms")
        visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false)
        visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false)
        visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false)
        super.onMethodExit(opcode)
    }

}
