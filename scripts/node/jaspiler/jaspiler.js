/*
 * Copyright (c) 2023. caoccao.com Sam Cao
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

/*
 * Usage:
 *   Please put this file along with your project
 *   and add the following lines selectively to your JS files.
 *   You will get the intellisense in VS Code.
 *
 *   const { JTBodyKind, JTCaseKind, JTKind, JTModifier, JTReferenceMode, JTTypeKind } = require('./jaspiler/jaspiler');
 *   const { PluginContractIgnore, PluginContractChangeMethod } = require('./jaspiler/jaspiler');
 */

const vm = require('vm');

const JTBodyKind = Object.freeze({
  EXPRESSION: 'EXPRESSION',
  STATEMENT: 'STATEMENT',
});

const JTCaseKind = Object.freeze({
  STATEMENT: 'STATEMENT',
  RULE: 'RULE',
});

const JTKind = Object.freeze({
  AND: 'AND', // BinaryTree
  AND_ASSIGNMENT: 'AND_ASSIGNMENT', // CompoundAssignmentTree
  ANNOTATED_TYPE: 'ANNOTATED_TYPE', // AnnotatedTypeTree
  ANNOTATION: 'ANNOTATION', // AnnotationTree
  ANNOTATION_TYPE: 'ANNOTATION_TYPE', // ClassTree
  ARRAY_ACCESS: 'ARRAY_ACCESS', // ArrayAccessTree
  ARRAY_TYPE: 'ARRAY_TYPE', // ArrayTypeTree
  ASSERT: 'ASSERT', // AssertTree
  ASSIGNMENT: 'ASSIGNMENT', // AssignmentTree
  BINDING_PATTERN: 'BINDING_PATTERN', // BindingPatternTree
  BITWISE_COMPLEMENT: 'BITWISE_COMPLEMENT', // UnaryTree
  BLOCK: 'BLOCK', // BlockTree
  BOOLEAN_LITERAL: 'BOOLEAN_LITERAL', // LiteralTree
  BREAK: 'BREAK', // BreakTree
  CASE: 'CASE', // CaseTree
  CATCH: 'CATCH', // CatchTree
  CHAR_LITERAL: 'CHAR_LITERAL', // LiteralTree
  CLASS: 'CLASS', // ClassTree
  COMPILATION_UNIT: 'COMPILATION_UNIT', // CompilationUnitTree
  CONDITIONAL_AND: 'CONDITIONAL_AND', // BinaryTree
  CONDITIONAL_EXPRESSION: 'CONDITIONAL_EXPRESSION', // ConditionalExpressionTree
  CONDITIONAL_OR: 'CONDITIONAL_OR', // BinaryTree
  CONTINUE: 'CONTINUE', // ContinueTree
  DEFAULT_CASE_LABEL: 'DEFAULT_CASE_LABEL', // DefaultCaseLabelTree
  DIVIDE: 'DIVIDE', // BinaryTree
  DIVIDE_ASSIGNMENT: 'DIVIDE_ASSIGNMENT', // CompoundAssignmentTree
  DO_WHILE_LOOP: 'DO_WHILE_LOOP', // DoWhileLoopTree
  DOUBLE_LITERAL: 'DOUBLE_LITERAL', // LiteralTree
  EMPTY_STATEMENT: 'EMPTY_STATEMENT', // EmptyStatementTree
  ENHANCED_FOR_LOOP: 'ENHANCED_FOR_LOOP', // EnhancedForLoopTree
  ENUM: 'ENUM', // ClassTree
  EQUAL_TO: 'EQUAL_TO', // BinaryTree
  ERRONEOUS: 'ERRONEOUS', // ErroneousTree
  EXPORTS: 'EXPORTS', // ExportsTree
  EXPRESSION_STATEMENT: 'EXPRESSION_STATEMENT', // ExpressionStatementTree
  EXTENDS_WILDCARD: 'EXTENDS_WILDCARD', // WildcardTree
  FLOAT_LITERAL: 'FLOAT_LITERAL', // LiteralTree
  FOR_LOOP: 'FOR_LOOP', // ForLoopTree
  GREATER_THAN: 'GREATER_THAN', // BinaryTree
  GREATER_THAN_EQUAL: 'GREATER_THAN_EQUAL', // BinaryTree
  GUARDED_PATTERN: 'GUARDED_PATTERN', // GuardedPatternTree
  IDENTIFIER: 'IDENTIFIER', // IdentifierTree
  IF: 'IF', // IfTree
  IMPORT: 'IMPORT', // ImportTree
  INSTANCE_OF: 'INSTANCE_OF', // InstanceOfTree
  INT_LITERAL: 'INT_LITERAL', // LiteralTree
  INTERFACE: 'INTERFACE', // ClassTree
  INTERSECTION_TYPE: 'INTERSECTION_TYPE', // IntersectionTypeTree
  LABELED_STATEMENT: 'LABELED_STATEMENT', // LabeledStatementTree
  LAMBDA_EXPRESSION: 'LAMBDA_EXPRESSION', // LambdaExpressionTree
  LEFT_SHIFT: 'LEFT_SHIFT', // BinaryTree
  LEFT_SHIFT_ASSIGNMENT: 'LEFT_SHIFT_ASSIGNMENT', // CompoundAssignmentTree
  LESS_THAN: 'LESS_THAN', // BinaryTree
  LESS_THAN_EQUAL: 'LESS_THAN_EQUAL', // BinaryTree
  LOGICAL_COMPLEMENT: 'LOGICAL_COMPLEMENT', // UnaryTree
  LONG_LITERAL: 'LONG_LITERAL', // LiteralTree
  MEMBER_REFERENCE: 'MEMBER_REFERENCE', // MemberReferenceTree
  MEMBER_SELECT: 'MEMBER_SELECT', // MemberSelectTree
  METHOD: 'METHOD', // MethodTree
  METHOD_INVOCATION: 'METHOD_INVOCATION', // MethodInvocationTree
  MINUS: 'MINUS', // BinaryTree
  MINUS_ASSIGNMENT: 'MINUS_ASSIGNMENT', // CompoundAssignmentTree
  MODIFIERS: 'MODIFIERS', // ModifiersTree
  MODULE: 'MODULE', // ModuleTree
  MULTIPLY: 'MULTIPLY', // BinaryTree
  MULTIPLY_ASSIGNMENT: 'MULTIPLY_ASSIGNMENT', // CompoundAssignmentTree
  NEW_ARRAY: 'NEW_ARRAY', // NewArrayTree
  NEW_CLASS: 'NEW_CLASS', // NewClassTree
  NOT_EQUAL_TO: 'NOT_EQUAL_TO', // BinaryTree
  NULL_LITERAL: 'NULL_LITERAL', // LiteralTree
  OPENS: 'OPENS', // OpensTree
  OR: 'OR', // BinaryTree
  OR_ASSIGNMENT: 'OR_ASSIGNMENT', // CompoundAssignmentTree
  OTHER: 'OTHER', // null
  PACKAGE: 'PACKAGE', // PackageTree
  PARAMETERIZED_TYPE: 'PARAMETERIZED_TYPE', // ParameterizedTypeTree
  PARENTHESIZED: 'PARENTHESIZED', // ParenthesizedTree
  PARENTHESIZED_PATTERN: 'PARENTHESIZED_PATTERN', // ParenthesizedPatternTree
  PLUS: 'PLUS', // BinaryTree
  PLUS_ASSIGNMENT: 'PLUS_ASSIGNMENT', // CompoundAssignmentTree
  POSTFIX_DECREMENT: 'POSTFIX_DECREMENT', // UnaryTree
  POSTFIX_INCREMENT: 'POSTFIX_INCREMENT', // UnaryTree
  PREFIX_DECREMENT: 'PREFIX_DECREMENT', // UnaryTree
  PREFIX_INCREMENT: 'PREFIX_INCREMENT', // UnaryTree
  PRIMITIVE_TYPE: 'PRIMITIVE_TYPE', // PrimitiveTypeTree
  PROVIDES: 'PROVIDES', // ProvidesTree
  RECORD: 'RECORD', // ClassTree
  REMAINDER: 'REMAINDER', // BinaryTree
  REMAINDER_ASSIGNMENT: 'REMAINDER_ASSIGNMENT', // CompoundAssignmentTree
  REQUIRES: 'REQUIRES', // RequiresTree
  RETURN: 'RETURN', // ReturnTree
  RIGHT_SHIFT: 'RIGHT_SHIFT', // BinaryTree
  RIGHT_SHIFT_ASSIGNMENT: 'RIGHT_SHIFT_ASSIGNMENT', // CompoundAssignmentTree
  STRING_LITERAL: 'STRING_LITERAL', // LiteralTree
  SUPER_WILDCARD: 'SUPER_WILDCARD', // WildcardTree
  SWITCH: 'SWITCH', // SwitchTree
  SWITCH_EXPRESSION: 'SWITCH_EXPRESSION', // SwitchExpressionTree
  SYNCHRONIZED: 'SYNCHRONIZED', // SynchronizedTree
  THROW: 'THROW', // ThrowTree
  TRY: 'TRY', // TryTree
  TYPE_ANNOTATION: 'TYPE_ANNOTATION', // AnnotationTree
  TYPE_CAST: 'TYPE_CAST', // TypeCastTree
  TYPE_PARAMETER: 'TYPE_PARAMETER', // TypeParameterTree
  UNARY_MINUS: 'UNARY_MINUS', // UnaryTree
  UNARY_PLUS: 'UNARY_PLUS', // UnaryTree
  UNBOUNDED_WILDCARD: 'UNBOUNDED_WILDCARD', // WildcardTree
  UNION_TYPE: 'UNION_TYPE', // UnionTypeTree
  UNSIGNED_RIGHT_SHIFT: 'UNSIGNED_RIGHT_SHIFT', // BinaryTree
  UNSIGNED_RIGHT_SHIFT_ASSIGNMENT: 'UNSIGNED_RIGHT_SHIFT_ASSIGNMENT', // CompoundAssignmentTree
  USES: 'USES', // UsesTree
  VARIABLE: 'VARIABLE', // VariableTree
  WHILE_LOOP: 'WHILE_LOOP', // WhileLoopTree
  XOR: 'XOR', // BinaryTree
  XOR_ASSIGNMENT: 'XOR_ASSIGNMENT', // CompoundAssignmentTree
  YIELD: 'YIELD', // YieldTree
});

const JTModifier = Object.freeze({
  PUBLIC: 'public',
  PROTECTED: 'protected',
  PRIVATE: 'private',
  ABSTRACT: 'abstract',
  DEFAULT: 'default',
  STATIC: 'static',
  SEALED: 'sealed',
  NON_SEALED: 'non-sealed',
  FINAL: 'final',
  TRANSIENT: 'transient',
  VOLATILE: 'volatile',
  SYNCHRONIZED: 'synchronized',
  NATIVE: 'native',
  STRICTFP: 'strictfp',
});

const JTReferenceMode = Object.freeze({
  INVOKE: 'INVOKE',
  NEW: 'NEW',
});

const JTTypeKind = Object.freeze({
  BOOLEAN: 'BOOLEAN',
  BYTE: 'BYTE',
  SHORT: 'SHORT',
  INT: 'INT',
  LONG: 'LONG',
  CHAR: 'CHAR',
  FLOAT: 'FLOAT',
  DOUBLE: 'DOUBLE',
  VOID: 'VOID',
  NONE: 'NONE',
  NULL: 'NULL',
  ARRAY: 'ARRAY',
  DECLARED: 'DECLARED',
  ERROR: 'ERROR',
  TYPEVAR: 'TYPEVAR',
  WILDCARD: 'WILDCARD',
  PACKAGE: 'PACKAGE',
  EXECUTABLE: 'EXECUTABLE',
  OTHER: 'OTHER',
  UNION: 'UNION',
  INTERSECTION: 'INTERSECTION',
  MODULE: 'MODULE',
});

function canBeIgnoredByAnnotations(annotations, context) {
  const annotation = findAnnotation(annotations, 'JaspilerContract.Ignore');
  return evaluateAnnotationAttribute(annotation, context, 'condition', true, false);
}

function evaluateAnnotationAttribute(annotation, context, attributeName, defaultValueForAttributeFound, defaultValueForAttributeNotFound) {
  if (annotation) {
    const attributeValue = getAnnotationAttributeValueByName(annotation, attributeName);
    if (attributeValue && attributeValue.kind == JTKind.STRING_LITERAL) {
      const script = new vm.Script(attributeValue.value);
      if (!(context instanceof Object)) {
        context = {};
      }
      if (!vm.isContext(context)) {
        vm.createContext(context);
      }
      return script.runInContext(context);
    }
    return defaultValueForAttributeFound;
  }
  return defaultValueForAttributeNotFound;
}

function findAnnotation(annotations, annotationName) {
  if (annotations) {
    return annotations.find(annotation => annotation.annotationType.toString() == annotationName);
  }
  return undefined;
}

function getAnnotationAttributeValueByName(annotation, attributeName) {
  if (annotation) {
    const args = annotation.arguments;
    for (let i = 0; i < args.length; ++i) {
      const arg = args[i];
      if (arg.kind == JTKind.ASSIGNMENT && arg.variable.toString() == attributeName) {
        return arg.expression;
      }
    }
  }
  return undefined;
}

function getChangeInstructionByAnnotations(annotations, context) {
  const annotation = findAnnotation(annotations, 'JaspilerContract.Change');
  if (evaluateAnnotationAttribute(annotation, context, 'condition', true, false)) {
    return evaluateAnnotationAttribute(annotation, context, 'instruction', undefined, undefined);
  }
  return undefined;
}

const PluginContractIgnore = Object.freeze({
  visitor: Object.freeze({
    AnnotatedType(node, context) {
      if (canBeIgnoredByAnnotations(node.annotations, context)) {
        node.setActionIgnore();
      }
    },
    CompilationUnit(node, context) {
      const typeDeclIgnored = node.typeDecls.find(typeDecl => {
        if (typeDecl.classSimpleName == 'JTClassDecl') {
          const modifiers = typeDecl.modifiers;
          if (modifiers && modifiers.flags.includes(JTModifier.PUBLIC)) {
            return canBeIgnoredByAnnotations(modifiers.annotations, context);
          }
        }
        return false;
      });
      if (typeDeclIgnored) {
        node.setActionIgnore();
      }
    },
    Class(node, context) {
      if (canBeIgnoredByAnnotations(node.modifiers?.annotations, context)) {
        node.setActionIgnore();
      }
    },
    Method(node, context) {
      if (canBeIgnoredByAnnotations(node.modifiers?.annotations, context)) {
        node.setActionIgnore();
      }
    },
    Module(node, context) {
      if (canBeIgnoredByAnnotations(node.annotations, context)) {
        node.setActionIgnore();
      }
    },
    NewArray(node, context) {
      if (canBeIgnoredByAnnotations(node.annotations, context)) {
        node.setActionIgnore();
      }
    },
    Package(node, context) {
      if (canBeIgnoredByAnnotations(node.annotations, context)) {
        node.setActionIgnore();
      }
    },
    TypeParameter(node, context) {
      if (canBeIgnoredByAnnotations(node.annotations, context)) {
        node.setActionIgnore();
      }
    },
    Variable(node, context) {
      if (canBeIgnoredByAnnotations(node.modifiers?.annotations, context)) {
        node.setActionIgnore();
      }
    },
  }),
});

const PluginContractChangeMethod = Object.freeze({
  visitor: Object.freeze({
    Method(node, context) {
      const instruction = getChangeInstructionByAnnotations(node.modifiers?.annotations, context);
      if (instruction) {
        if (instruction.type == 'clear') {
          const body = node.body;
          // There is no need to clear the method if the body is absent.
          if (body) {
            const returnType = node.returnType;
            const blockStatement = jaspiler.newBlock();
            const isPrimitiveType = returnType.classSimpleName == 'JTPrimitiveType';
            const primitiveTypeKind = isPrimitiveType ? returnType.primitiveTypeKind : null;
            if (!isPrimitiveType || primitiveTypeKind != JTTypeKind.VOID) {
              const returnStatement = jaspiler.newReturn();
              const literalExpression = jaspiler.createLiteral(null);
              if (isPrimitiveType) {
                let kind = JTKind.NULL_LITERAL;
                switch (primitiveTypeKind) {
                  case JTTypeKind.BOOLEAN: kind = JTKind.BOOLEAN_LITERAL; break;
                  case JTTypeKind.CHAR: kind = JTKind.CHAR_LITERAL; break;
                  case JTTypeKind.DOUBLE: kind = JTKind.DOUBLE_LITERAL; break;
                  case JTTypeKind.FLOAT: kind = JTKind.FLOAT_LITERAL; break;
                  case JTTypeKind.INT: kind = JTKind.INT_LITERAL; break;
                  case JTTypeKind.LONG: kind = JTKind.LONG_LITERAL; break;
                  case JTTypeKind.NULL: kind = JTKind.NULL_LITERAL; break;
                }
                literalExpression.kind = kind;
              }
              returnStatement.expression = literalExpression;
              blockStatement.statements = [returnStatement];
            }
            node.body = blockStatement;
          }
        }
      }
    },
  }),
});

module.exports = Object.freeze({
  helpers: Object.freeze({
    evaluateAnnotationAttribute: evaluateAnnotationAttribute,
    findAnnotation: findAnnotation,
    getAnnotationAttributeValueByName: getAnnotationAttributeValueByName,
  }),
  JTBodyKind: JTBodyKind,
  JTCaseKind: JTCaseKind,
  JTKind: JTKind,
  JTModifier: JTModifier,
  JTReferenceMode: JTReferenceMode,
  JTTypeKind: JTTypeKind,
  PluginContractIgnore: PluginContractIgnore,
  PluginContractChangeMethod: PluginContractChangeMethod,
});
