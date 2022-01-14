package me.darkboy;

import me.darkboy.utils.JarReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;

import static org.objectweb.asm.Opcodes.LDC;

public class Main {

    public static void main(String[] args) {

        JarReader jarReader = new JarReader();

        jarReader.loadJar(args[0]);

        jarReader.getClasses().forEach(Main::printStrings);
    }

    private static void printStrings(ClassNode classNode) {

        classNode.methods.stream()
                .filter(methodNode -> methodNode.instructions.size() > 0)
                .forEach(methodNode -> {
                    InsnList instructions = methodNode.instructions;
                    AbstractInsnNode insn = instructions.getFirst();

                    do {
                        if (insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst instanceof String) {

                            String foundString = (String) ((LdcInsnNode) insn).cst;

                            System.out.println("Class name: " + classNode.name + " Method name: " + methodNode.name + " String found: " + foundString);
                        }
                    } while ((insn = insn.getNext()) != null);
                });
    }
}
