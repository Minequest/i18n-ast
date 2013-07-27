package com.theminequest.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;

// http://www.programcreek.com/2011/11/use-jdt-astparser-to-parse-java-file/
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
	
	public static void recurse(File current, Properties prop) throws IOException {
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
	
	public static void parseCompilationUnit(final File sourceFile, final Properties prop) throws IOException {
		System.out.printf("Processing %s\n", sourceFile.getCanonicalPath());
		
		parse(readFileToString(sourceFile), prop);
		
	}
	
	// read file content into a string
	public static String readFileToString(File file) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		
		reader.close();
		
		return fileData.toString();
	}
	
	// use ASTParse to parse string
	public static void parse(String str, final Properties prop) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		cu.accept(new ASTVisitor() {
			
			@Override
			public boolean visit(MethodInvocation node) {
				if (node.getName().getIdentifier().equals("tr") || node.getName().getIdentifier().equals("_")) {
					List<?> arguments = node.arguments();
					String translate = arguments.get(0).toString();
					if (translate.equals("translate")) // for I18NMessage
						return super.visit(node);
					translate = translate.substring(0, translate.length() - 1).substring(1);
					prop.put(translate, translate);
				}
				return super.visit(node);
			}
			
		});
		
	}
	
}
