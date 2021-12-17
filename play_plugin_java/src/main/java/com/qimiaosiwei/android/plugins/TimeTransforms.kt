package com.qimiaosiwei.android.plugins

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarFile

/**
 * author : heyongjian
 * time   : 2021/12/16
 * desc   :
 */
class TimeTransforms : Transform() {
    override fun getName(): String {
        return "TimeTransforms"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun transform(
        context: Context?,
        inputs: MutableCollection<TransformInput>?,
        referencedInputs: MutableCollection<TransformInput>?,
        outputProvider: TransformOutputProvider?,
        isIncremental: Boolean
    ) {
        if (!isIncremental) {
            //不是增量更新删除所有的outputProvider
            outputProvider?.deleteAll()
        }

        inputs?.forEach {
            it.directoryInputs.forEach { directoryInput ->
                println("---transform name " + directoryInput.file.name)
                handleDirectoryInput(directoryInput, outputProvider)
            }
            it.jarInputs.forEach { jarInput ->
                handleJarInput(jarInput, outputProvider, isIncremental)
            }

        }

    }

    private fun handleDirectoryInput(
        directoryInput: DirectoryInput?,
        outputProvider: TransformOutputProvider?
    ) {
        if (directoryInput == null || outputProvider == null) return

        //是否是目录
        if (directoryInput.file.isDirectory) {
            //列出目录所有文件（包含子文件夹，子文件夹内文件）
            directoryInput.file.walkTopDown().filter { it.isFile }.forEach { file ->
                println("-----handleDirectoryInput ${file.name} || ${file.isDirectory}")
                val name = file.name
                if (filterClass(name)) {
                    val classReader = ClassReader(file.readBytes())
                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    val classVisitor = TimeClassVisitors(Opcodes.ASM6, classWriter)
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    val code = classWriter.toByteArray()
                    val fos = FileOutputStream(file.parentFile.absolutePath + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
        }
        //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
        // 获取output目录
        val dest = outputProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )
        //这里执行字节码的注入，不操作字节码的话也要将输入路径拷贝到输出路径
        FileUtils.copyDirectory(directoryInput.file, dest)

    }

    private fun filterClass(name: String): Boolean {
        return (name.endsWith(".class")
                && !name.startsWith("R\$")
                && "R.class" != name
                && "BuildConfig.class" != name)
    }

    private fun handleJarInput(
        jarInput: JarInput?,
        outputProvider: TransformOutputProvider?,
        incremental: Boolean
    ) {
        if (jarInput == null || outputProvider == null) return


        val dstFile = outputProvider.getContentLocation(
            jarInput.name,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )
//        println('-----transformJar start---- isIncremental ' + isIncremental)
//        println('jar input name ==> ' + jarInput.name)
//        println('jar input ==> ' + jarInput.file.absolutePath)
//        println('dstFile ==> ' + dstFile.getAbsolutePath())

        val jarFile = JarFile(jarInput.file)
//        jarFile.entries().each {
//            if (it.name.contains(TARGET)) {
//                sTargetList.add(it.name)
//                println(it.name)
//            }
//        }

        if (!incremental) {
            FileUtils.copyFile(jarInput.file, dstFile)
        }

    }

}