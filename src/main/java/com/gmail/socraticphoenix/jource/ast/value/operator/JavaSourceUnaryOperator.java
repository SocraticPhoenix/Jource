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
package com.gmail.socraticphoenix.jource.ast.value.operator;

import com.gmail.socraticphoenix.jource.ast.JavaSourceContext;
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import com.gmail.socraticphoenix.jource.ast.value.JavaSourceValue;

import java.util.List;

public class JavaSourceUnaryOperator implements JavaSourceValue {
    private JavaSourceValue target;
    private JavaSourceOperator operator;

    public JavaSourceUnaryOperator(JavaSourceOperator operator, JavaSourceValue target) {
        if(operator != JavaSourceOperator.EMPTY && operator.getOperands() != 1) {
            throw new IllegalArgumentException("Operator does not accept one operand");
        }
        this.operator = operator;
        this.target = target;
    }

    public static JavaSourceUnaryOperator of(JavaSourceOperator operator, JavaSourceValue target) {
        return new JavaSourceUnaryOperator(operator, target);
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        if(this.operator.isPostfix()) {
            return this.target.write(indent + 1, context) + this.operator.getRep();
        } else {
            return this.operator.getRep() + this.target.write(indent + 1, context);
        }
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        return this.target.associatedTypes();
    }

    @Override
    public JavaSourceNamespace type() {
        return JavaSourceNamespace.UNKNOWN;
    }
}
