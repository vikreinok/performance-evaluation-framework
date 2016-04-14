package com.takipi.samples.servprof;

import com.takipi.samples.servprof.inst.InstrumentationFilter;
import com.takipi.samples.servprof.inst.ProfilerClassVisitor;
import javassist.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer {
	private final InstrumentationFilter instFilter;
	private final Options options;

	public Transformer(Options options, InstrumentationFilter instFilter) {
		this.instFilter = instFilter;
		this.options = options;
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		// if the class being loaded / redefined does not match our target cut
		// points return null
		// this instructs the JVM to continue loading that class as is
		if (!instFilter.shouldInstrumentClass(className)) {
			return null;
		}
//		System.err.println(className);

		if (false) {
			byte[] byteCode = instrumentJavaAssist(className);
			if (byteCode != null) return byteCode;

			return classfileBuffer;
		}


		// initiate a reader which will scan the loaded byte code
		ClassReader cr = new ClassReader(classfileBuffer);

		// create a writer which will receive data from the reader and write
		// that into a new bytecode raw byte[] buffer
		// we let the ASM library take care of doing calculations of frame and
		// variable depths which are a part of the
		// bytecode structure
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

		// and this is where we come in - our visitor will be staged between the
		// class reader and the writer
		// ever instruction read by the class reader will be passed on to us
		// before it goes into the writer
		// this gives us the ability to manipulate, or "instrument" the class's
		// code with our own

//		ClassVisitor tnv = new ThreadNameSetterClassVisitor(cw, instFilter, className);

		ClassVisitor cv = new ProfilerClassVisitor(cw, instFilter, className);

		// initiate a Visitor pattern between the reader and writer, passing our
		// visitor through the chain
		cr.accept(cv, 0);

		// the writer has now completed generating the new class, convert to raw bytecode
		return cw.toByteArray();
	}

	private byte[] instrumentJavaAssist(String className) {
		System.err.println(className);
		String modifiedClassName = className.replaceAll("/", ".");
		CtClass cc = null;
		try {
			ClassPool pool = ClassPool.getDefault();
			cc = pool.get(modifiedClassName);
			cc.stopPruning(true);
			for (CtMethod methodName : cc.getDeclaredMethods()) {
				if(instFilter.shouldInstrumentMethod(methodName.getName())) {
					instrument(methodName);
				}
			}
			cc.defrost();
 			return cc.toBytecode();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (cc != null) {
				cc.detach();
			}
		}
		return null;
	}

	private void instrument(CtMethod method) throws NotFoundException, CannotCompileException, IOException {

		StringBuilder sbs = new StringBuilder();
		sbs.append( "long tid = Thread.currentThread().getId();" );
		sbs.append( "StringBuilder sbArgs = new StringBuilder();" );
//		sbs.append( "sbArgs.append( System.identityHashCode( $0 ) );" );
		CtClass[] pTypes = method.getParameterTypes();
		for( int i=0; i < pTypes.length; ++i ) {
			CtClass pType = pTypes[i];
			if ( pType.isPrimitive() ) {
				sbs.append( "sbArgs.append( \", \" + $args[" + i + "] );" );
			} else {
				sbs.append( "if( $args[" + i + "] instanceof java.lang.Long) {" );
				sbs.append( "sbArgs.append( \", \" + (java.lang.Long) $args[" + i + "] );" );
				sbs.append( "} else {" );
				sbs.append( "sbArgs.append( \", \" + System.identityHashCode( $args[" + i + "] ) );" );
				sbs.append( "}" );
			}
		}
		sbs.append( "StringBuilder sb = new StringBuilder();" );
		sbs.append( "sb.append( tid + \" : " + method.getLongName() + ".<START>(\" );" );
		sbs.append( "sb.append( sbArgs.toString() );" );
		sbs.append( "sb.append( \")\" );" );
		sbs.append( "String fPath = \"/path/to/log.out\";" );
		sbs.append( "System.out.println(sb.toString());" );


		String before = "{" + sbs.toString() + "}";
		System.out.println(before);
		method.insertBefore(before);

		StringBuilder sbe = new StringBuilder();
		sbe.append( "long tid = Thread.currentThread().getId();" );
		sbe.append( "StringBuilder sb = new StringBuilder();" );
		sbe.append( "sb.append( tid + \" : " + method.getLongName() + ".<END>(*)\" );" );
		sbe.append( "String fPath = \"/path/to/log.out\";" );
		sbs.append( "System.out.println(sb.toString());" );


		method.insertAfter("{" + sbe.toString() + "}");


//		method.addLocalVariable("elapsedTime", CtClass.longType);
//		method.insertBefore("elapsedTime = System.currentTimeMillis();");
//		method.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
//                + "System.out.println(\"Method\" +  Executed in ms: \" + elapsedTime);}");
//		byte[] byteCode = cc.toBytecode();
//		cc.detach();
//		return byteCode;
	}

	private void foo() {

	}

	public Options getOptions() {
		return options;
	}

	public InstrumentationFilter getInstFilter() {
		return instFilter;
	}
}
