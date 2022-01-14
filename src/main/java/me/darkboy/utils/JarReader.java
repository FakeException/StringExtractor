package me.darkboy.utils;

import lombok.var;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarReader {

    private final HashSet<ClassNode> classes = new HashSet<>();

    public void loadJar(String path) {
        System.out.println("Loading input \"" + path + "\"");
        var file = new File(path);

        try {
            var zipFile = new ZipFile(file);
            var entries = zipFile.entries();

            ZipEntry currentEntry;
            while (entries.hasMoreElements()) {
                currentEntry = entries.nextElement();

                if (!currentEntry.isDirectory()) {
                    try (var stream = zipFile.getInputStream(currentEntry)) {
                        if (currentEntry.getName().endsWith(".class")) {
                            try {

                                var classNode = new ClassNode();
                                var reader = new ClassReader(stream);
                                reader.accept(classNode, ClassReader.SKIP_FRAMES);

                                classes.add(classNode);
                            } catch (Throwable ignored) {}
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashSet<ClassNode> getClasses() {
        return classes;
    }
}