package com.takipi.samples.servprof.inst;

import com.takipi.samples.servprof.state.MethodKey;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class ProfilerClassVisitor extends FilterClassVisitor {
	private final String className;

	public ProfilerClassVisitor(ClassVisitor cv, InstrumentationFilter instFilter, String className) {
		super(cv, instFilter);

		this.className = className;
	}

	@Override
	protected MethodVisitor createMethodVisitor(MethodVisitor mv, int access, String name, String desc) {
		return new ProfilerMethodVisitor(mv, access, new MethodKey(className, name), desc);
	}
}
