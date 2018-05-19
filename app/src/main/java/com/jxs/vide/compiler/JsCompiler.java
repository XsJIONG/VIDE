package com.jxs.vide.compiler;

import com.jxs.v.io.IOUtil;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.optimizer.ClassCompiler;
import org.mozilla.javascript.ErrorReporter;

public class JsCompiler {
	public static byte[] compile(File f) throws IOException {
		return compile(f.getName().substring(0, f.getName().lastIndexOf('.')), IOUtil.bytes2String(IOUtil.read(f)));
	}
	public static byte[] compile(String name, String js) {
		CompilerEnvirons env=new CompilerEnvirons();
		ClassCompiler compiler=new ClassCompiler(env);
		Object[] result=compiler.compileToClassFiles(js, name + ".js", 1, name);
		return (byte[]) result[1];
	}
	private JsCompiler() {}
}
