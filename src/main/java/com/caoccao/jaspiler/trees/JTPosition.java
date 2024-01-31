/*
 * Copyright (c) 2023-2024. caoccao.com Sam Cao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.caoccao.jaspiler.trees;

import com.caoccao.jaspiler.styles.StandardStyleWriter;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.DocSourcePositions;
import com.sun.source.util.SourcePositions;

public record JTPosition(
        long startPosition,
        long endPosition,
        long lineNumber,
        long columnNumber) {
    public static final JTPosition Invalid = new JTPosition(-1, -1, -1, -1);

    public static JTPosition from(
            DocSourcePositions docSourcePositions,
            CompilationUnitTree compilationUnitTree,
            DocCommentTree docCommentTree,
            DocTree docTree) {
        long startPosition = docSourcePositions.getStartPosition(compilationUnitTree, docCommentTree, docTree);
        long endPosition = docSourcePositions.getEndPosition(compilationUnitTree, docCommentTree, docTree);
        long lineNumber = -1;
        long columnNumber = -1;
        if (startPosition >= 0) {
            lineNumber = compilationUnitTree.getLineMap().getLineNumber(startPosition);
            columnNumber = compilationUnitTree.getLineMap().getColumnNumber(startPosition);
        }
        return new JTPosition(startPosition, endPosition, lineNumber, columnNumber);
    }

    public static JTPosition from(
            SourcePositions sourcePositions,
            CompilationUnitTree compilationUnitTree,
            Tree tree) {
        long startPosition = sourcePositions.getStartPosition(compilationUnitTree, tree);
        long endPosition = sourcePositions.getEndPosition(compilationUnitTree, tree);
        long lineNumber = -1;
        long columnNumber = -1;
        if (startPosition >= 0) {
            lineNumber = compilationUnitTree.getLineMap().getLineNumber(startPosition);
            columnNumber = compilationUnitTree.getLineMap().getColumnNumber(startPosition);
        }
        return new JTPosition(startPosition, endPosition, lineNumber, columnNumber);
    }

    public boolean isValid() {
        return !(startPosition < 0 || endPosition < 0 || lineNumber < 0 || columnNumber < 0);
    }

    public long length() {
        return endPosition - startPosition;
    }

    @Override
    public String toString() {
        final var stringBuilder = new StringBuilder();
        stringBuilder.append("S: ").append(startPosition).append(", ");
        stringBuilder.append("E: ").append(endPosition).append(", ");
        stringBuilder.append("L: ").append(lineNumber).append(", ");
        stringBuilder.append("C: ").append(columnNumber);
        return stringBuilder.toString();
    }
}
