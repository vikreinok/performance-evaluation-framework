package com.takipi.samples.servprof.inst;

import com.takipi.samples.servprof.state.MethodKey;
import com.takipi.samples.servprof.state.ThreadNameSetter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ThreadNameSetterMethodVisitor extends StartEndMethodVisitor {

	public ThreadNameSetterMethodVisitor(MethodVisitor mv, int access, MethodKey methodKey, String desc) {
		super(mv, access, methodKey, desc);
	}

	@Override
	protected void insertPrologue() {

		// load the method identifier into the stack
		mv.visitLdcInsn(methodKey.toString());

		// load the method's arguments as an Object[]
		loadArgsIntoObjectArray();

		// invoke the Thread name setter
		super.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(ThreadNameSetter.class), "setThreadName",
				"(Ljava/lang/String;[Ljava/lang/Object;)V", false);
	}

	@Override
	protected void insertEpilogue() {
		
		//restore the thread name
		super.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(ThreadNameSetter.class), "restoreThreadName",
				"()V", false);

	}
}
