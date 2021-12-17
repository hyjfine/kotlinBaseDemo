package com.qimiaosiwei.android.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager

import com.android.utils.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarFile

class TimeTransform extends Transform {

    @Override
    String getName() {
        return "TimeTransform"
    }

    /**
     * 需要处理的数据类型，有两种枚举类型
     * CLASS->处理的java的class文件
     * RESOURCES->处理java的资源
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 指 Transform 要操作内容的范围，官方文档 Scope 有 7 种类型：
     * 1. EXTERNAL_LIBRARIES        只有外部库
     * 2. PROJECT                   只有项目内容
     * 3. PROJECT_LOCAL_DEPS        只有项目的本地依赖(本地jar)
     * 4. PROVIDED_ONLY             只提供本地或远程依赖项
     * 5. SUB_PROJECTS              只有子项目。
     * 6. SUB_PROJECTS_LOCAL_DEPS   只有子项目的本地依赖项(本地jar)。
     * 7. TESTED_CODE               由当前变量(包括依赖项)测试的代码
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 是否增量编译
     * @return
     */
    @Override
    boolean isIncremental() {
        return true
    }

    /**
     *
     * @param context
     * @param inputs 有两种类型，一种是目录，一种是 jar 包，要分开遍历
     * @param outputProvider 输出路径
     */
    @Override
    void transform(Context context,
                   Collection<TransformInput> inputs,
                   Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider,
                   boolean isIncremental) throws IOException, TransformException, InterruptedException {
        if (!incremental) {
            //不是增量更新删除所有的outputProvider
            outputProvider.deleteAll()
        }
        inputs.each { TransformInput input ->
            //遍历目录
            input.directoryInputs.each { DirectoryInput directoryInput ->
                println("---transform name " + directoryInput.file.name)
                handleDirectoryInput(directoryInput, outputProvider)
            }
            // 遍历jar 第三方引入的 class （第三方的不是我们的插桩目标暂不遍历）
            input.jarInputs.each { JarInput jarInput ->
                handleJarInput(jarInput, outputProvider, isIncremental)
            }
        }
    }

    /**
     * 处理文件目录下的class文件
     */
    private static void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        //是否是目录
        if (directoryInput.file.isDirectory()) {
            //列出目录所有文件（包含子文件夹，子文件夹内文件）
            directoryInput.file.eachFileRecurse { File file ->
                def name = file.name
                def isDir = file.isDirectory()
                println("-----handleDirectoryInput $name || $isDir")
                if (filterClass(name)) {
                    ClassReader classReader = new ClassReader(file.bytes)
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor classVisitor = new TimeClassVisitor(Opcodes.ASM6, classWriter)
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    FileOutputStream fos = new FileOutputStream(file.parentFile.absolutePath + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
        }
        //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
        // 获取output目录
        def dest = outputProvider.getContentLocation(
                directoryInput.name,
                directoryInput.contentTypes,
                directoryInput.scopes,
                Format.DIRECTORY)
        //这里执行字节码的注入，不操作字节码的话也要将输入路径拷贝到输出路径
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    private static void handleJarInput(JarInput jarInput, TransformOutputProvider outputProvider, boolean isIncremental) {

        File dstFile = outputProvider.getContentLocation(
                jarInput.getName(),
                jarInput.getContentTypes(),
                jarInput.getScopes(),
                Format.JAR)
//        println('-----transformJar start---- isIncremental ' + isIncremental)
//        println('jar input name ==> ' + jarInput.name)
//        println('jar input ==> ' + jarInput.file.absolutePath)
//        println('dstFile ==> ' + dstFile.getAbsolutePath())

        JarFile jarFile = new JarFile(jarInput.file)
//        jarFile.entries().each {
//            if (it.name.contains(TARGET)) {
//                sTargetList.add(it.name)
//                println(it.name)
//            }
//        }

        if (!isIncremental) {
            FileUtils.copyFile(jarInput.file, dstFile)
        }
    }

    /**
     * 检查class文件是否需要处理
     * @param fileName
     * @return
     */
    static boolean filterClass(String name) {
        return (name.endsWith(".class")
                && !name.startsWith("R\$")
                && "R.class" != name
                && "BuildConfig.class" != name)
    }
}