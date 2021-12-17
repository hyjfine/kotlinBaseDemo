package com.qimiaosiwei.android.plugins

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

/**
 * author : heyongjian
 * time   : 2021/12/16
 * desc   :
 */
class TimeClassAdapters(
    api: Int,
    methodVisitor: MethodVisitor?,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(api, methodVisitor, access, name, descriptor) {
    private var isModify = false

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        println("" + System.currentTimeMillis() + " TimeClassAdapter descriptor " + descriptor + " visible " + visible)
        isModify = descriptor == "Lcom/qimiaosiwei/android/playasm/StatisticTime;"
        return super.visitAnnotation(descriptor, visible)
    }

    override fun onMethodEnter() {
        println("" + System.currentTimeMillis() + " TimeClassAdapter start : name " + name + " isModify " + isModify)
        if (!isModify) return
        visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
        visitVarInsn(LSTORE, 0)
        super.onMethodEnter()
    }

    override fun onMethodExit(opcode: Int) {
        println(System.currentTimeMillis().toString() + " TimeClassAdapter end : name " + name + " isModify " + isModify)
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