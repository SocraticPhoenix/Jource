/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.jource.io;

import com.gmail.socraticphoenix.jource.ast.JavaSourceContext;
import com.gmail.socraticphoenix.jource.ast.definition.JavaSourceDefinition;
import com.gmail.socraticphoenix.jource.ast.modifier.JavaSourceModifier;
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import com.gmail.socraticphoenix.parse.Strings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaSourceFile {
    private JavaSourceDefinition main;
    private List<JavaSourceDefinition> additional;
    private Set<JavaSourceNamespace> imports;
    private Set<JavaSourceNamespace> literals;

    public JavaSourceFile(JavaSourceDefinition main) {
        this.main = main;
        this.additional = new ArrayList<>();
        this.imports = new HashSet<>();
        this.literals = new HashSet<>();

        this.buildImports();
    }

    private void buildImports() {
        this.imports.clear();
        this.literals.clear();

        List<String> imports = new ArrayList<>();
        List<JavaSourceNamespace> associated = main.associatedTypes();
        this.additional.forEach(s -> associated.addAll(s.associatedTypes()));
        for (JavaSourceNamespace sourceNamespace : associated) {
            if(!this.imports.contains(sourceNamespace) && !this.literals.contains(sourceNamespace)) {
                if (!imports.contains(sourceNamespace.getSimpleName())) {
                    this.imports.add(sourceNamespace);
                    imports.add(sourceNamespace.getSimpleName());
                } else {
                    this.literals.add(sourceNamespace);
                }
            }
        }
    }

    public JavaSourceFile addDefinition(JavaSourceDefinition definition) {
        if(definition.modifiers().contains(JavaSourceModifier.PUBLIC) || definition.modifiers().contains(JavaSourceModifier.PRIVATE) || definition.modifiers().contains(JavaSourceModifier.PROTECTED)) {
            throw new IllegalArgumentException("Additional class definitions must not be public, private, or protected. Only package-protected is allowed.");
        }

        this.additional.add(definition);
        return this;
    }

    public String write() {
        this.buildImports();

        JavaSourceContext context = new JavaSourceExclusionContext(this.literals);
        int indent = 0;
        StringBuilder builder = new StringBuilder();
        this.imports.forEach(jcn -> builder.append(jcn.toImportStatement()).append(";").append(System.lineSeparator()));
        builder.append(this.main.write(indent, context));
        builder.append(System.lineSeparator());
        this.additional.forEach(jsd -> {
            builder.append(System.lineSeparator());
            builder.append(jsd.write(indent, context));
            builder.append(System.lineSeparator());
        });

        return builder.toString();
    }

    public void writeTo(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.append(this.write());
        writer.close();
    }

    public void writeAt(File dir) throws IOException {
        File target = new File(dir, Strings.glue(File.separator, (Object[]) this.main.name().getPath()));
        this.writeTo(target);
    }

}
