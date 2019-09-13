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
package io.github.socraticphoenix.jource.ast;

import io.github.socraticphoenix.jource.ast.definition.JavaSourceDefinitionType;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;

public interface JavaSourceContext {

    String nameOf(JavaSourceNamespace namespace);

    JavaSourceDefinitionType parentType();

    default boolean isInterface() {
        return parentType() == JavaSourceDefinitionType.INTERFACE || isAnnotation();
    }

    default boolean isClass() {
        return parentType() == JavaSourceDefinitionType.CLASS || isEnum();
    }

    default boolean isEnum() {
        return parentType() == JavaSourceDefinitionType.ENUM;
    }

    default boolean isAnnotation() {
        return parentType() == JavaSourceDefinitionType.ANNOTATION;
    }

    static JavaSourceContext of(JavaSourceContext context, JavaSourceDefinitionType type) {
        return new JavaSourceContext() {
            @Override
            public String nameOf(JavaSourceNamespace namespace) {
                return context.nameOf(namespace);
            }

            @Override
            public JavaSourceDefinitionType parentType() {
                return type;
            }
        };
    }

}
