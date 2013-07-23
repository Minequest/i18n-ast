package com.theminequest.i18n;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class Main {
	
	public static void _(String s, Object... args) {
		
	}
	
	public static void main(final String[] args) throws Exception {
		Properties prop = new Properties();
		if (args.length < 1)
			recurse(new File(""), prop);
		else
			recurse(new File(args[0]), prop);
		prop.store(new FileWriter(new File("en_US.properties")), "Generated with MineQuest i18n-ast.");
		
		_("Hello World!", 0);
		_("Wheeee", 1);
		_("Hello World 2!", 10, new Properties());
	}
	
	public static void recurse(File current, Properties prop) throws ParseException, IOException {
		if (!current.exists())
			return;
		if (current.isFile()) {
			if (current.getName().endsWith(".java"))
				parseCompilationUnit(current, prop);
			return;
		}
		for (File f : current.listFiles())
			recurse(f, prop);
	}
	
	public static void parseCompilationUnit(final File sourceFile, final Properties prop) throws ParseException, IOException {
		final CompilationUnit cu = JavaParser.parse(sourceFile);
		cu.accept(new VoidVisitorAdapter<Void>() {
			
			@Override
			public void visit(final MethodCallExpr n, final Void arg) {
				
				if (n.getName().equals("tr") || n.getName().equals("_")) {
					List<Expression> arguments = n.getArgs();
					String translate = arguments.get(0).toString();
					if (!translate.startsWith("\"") || !translate.endsWith("\""))
						throw new RuntimeException("Bad argument at " + sourceFile.getName() + ":" + n.getBeginLine() + "!");
					translate = translate.substring(0, translate.length() - 1).substring(1);
					prop.put(translate, translate);
				}
				
				super.visit(n, arg);
			}
		}, null);
	}
	
}
