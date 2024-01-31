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

/*
 * Usage:
 *   Please put this file along with your project
 *   and add the following line to your JS/TS files.
 *   You will get the intellisense in VS Code.
 *
 *   /// <reference types="${yourLocalPath}/jaspiler/index.d.ts"/>
 */

/// <reference no-default-lib="true"/>

enum JTBodyKind {
  EXPRESSION,
  STATEMENT,
}

enum JTCaseKind {
  STATEMENT,
  RULE,
}

enum JTKind {
  AND, // BinaryTree
  AND_ASSIGNMENT, // CompoundAssignmentTree
  ANNOTATED_TYPE, // AnnotatedTypeTree
  ANNOTATION, // AnnotationTree
  ANNOTATION_TYPE, // ClassTree
  ARRAY_ACCESS, // ArrayAccessTree
  ARRAY_TYPE, // ArrayTypeTree
  ASSERT, // AssertTree
  ASSIGNMENT, // AssignmentTree
  BINDING_PATTERN, // BindingPatternTree
  BITWISE_COMPLEMENT, // UnaryTree
  BLOCK, // BlockTree
  BOOLEAN_LITERAL, // LiteralTree
  BREAK, // BreakTree
  CASE, // CaseTree
  CATCH, // CatchTree
  CHAR_LITERAL, // LiteralTree
  CLASS, // ClassTree
  COMPILATION_UNIT, // CompilationUnitTree
  CONDITIONAL_AND, // BinaryTree
  CONDITIONAL_EXPRESSION, // ConditionalExpressionTree
  CONDITIONAL_OR, // BinaryTree
  CONTINUE, // ContinueTree
  DEFAULT_CASE_LABEL, // DefaultCaseLabelTree
  DIVIDE, // BinaryTree
  DIVIDE_ASSIGNMENT, // CompoundAssignmentTree
  DO_WHILE_LOOP, // DoWhileLoopTree
  DOUBLE_LITERAL, // LiteralTree
  EMPTY_STATEMENT, // EmptyStatementTree
  ENHANCED_FOR_LOOP, // EnhancedForLoopTree
  ENUM, // ClassTree
  EQUAL_TO, // BinaryTree
  ERRONEOUS, // ErroneousTree
  EXPORTS, // ExportsTree
  EXPRESSION_STATEMENT, // ExpressionStatementTree
  EXTENDS_WILDCARD, // WildcardTree
  FLOAT_LITERAL, // LiteralTree
  FOR_LOOP, // ForLoopTree
  GREATER_THAN, // BinaryTree
  GREATER_THAN_EQUAL, // BinaryTree
  GUARDED_PATTERN, // GuardedPatternTree
  IDENTIFIER, // IdentifierTree
  IF, // IfTree
  IMPORT, // ImportTree
  INSTANCE_OF, // InstanceOfTree
  INT_LITERAL, // LiteralTree
  INTERFACE, // ClassTree
  INTERSECTION_TYPE, // IntersectionTypeTree
  LABELED_STATEMENT, // LabeledStatementTree
  LAMBDA_EXPRESSION, // LambdaExpressionTree
  LEFT_SHIFT, // BinaryTree
  LEFT_SHIFT_ASSIGNMENT, // CompoundAssignmentTree
  LESS_THAN, // BinaryTree
  LESS_THAN_EQUAL, // BinaryTree
  LOGICAL_COMPLEMENT, // UnaryTree
  LONG_LITERAL, // LiteralTree
  MEMBER_REFERENCE, // MemberReferenceTree
  MEMBER_SELECT, // MemberSelectTree
  METHOD, // MethodTree
  METHOD_INVOCATION, // MethodInvocationTree
  MINUS, // BinaryTree
  MINUS_ASSIGNMENT, // CompoundAssignmentTree
  MODIFIERS, // ModifiersTree
  MODULE, // ModuleTree
  MULTIPLY, // BinaryTree
  MULTIPLY_ASSIGNMENT, // CompoundAssignmentTree
  NEW_ARRAY, // NewArrayTree
  NEW_CLASS, // NewClassTree
  NOT_EQUAL_TO, // BinaryTree
  NULL_LITERAL, // LiteralTree
  OPENS, // OpensTree
  OR, // BinaryTree
  OR_ASSIGNMENT, // CompoundAssignmentTree
  OTHER, // null
  PACKAGE, // PackageTree
  PARAMETERIZED_TYPE, // ParameterizedTypeTree
  PARENTHESIZED, // ParenthesizedTree
  PARENTHESIZED_PATTERN, // ParenthesizedPatternTree
  PLUS, // BinaryTree
  PLUS_ASSIGNMENT, // CompoundAssignmentTree
  POSTFIX_DECREMENT, // UnaryTree
  POSTFIX_INCREMENT, // UnaryTree
  PREFIX_DECREMENT, // UnaryTree
  PREFIX_INCREMENT, // UnaryTree
  PRIMITIVE_TYPE, // PrimitiveTypeTree
  PROVIDES, // ProvidesTree
  RECORD, // ClassTree
  REMAINDER, // BinaryTree
  REMAINDER_ASSIGNMENT, // CompoundAssignmentTree
  REQUIRES, // RequiresTree
  RETURN, // ReturnTree
  RIGHT_SHIFT, // BinaryTree
  RIGHT_SHIFT_ASSIGNMENT, // CompoundAssignmentTree
  STRING_LITERAL, // LiteralTree
  SUPER_WILDCARD, // WildcardTree
  SWITCH, // SwitchTree
  SWITCH_EXPRESSION, // SwitchExpressionTree
  SYNCHRONIZED, // SynchronizedTree
  THROW, // ThrowTree
  TRY, // TryTree
  TYPE_ANNOTATION, // AnnotationTree
  TYPE_CAST, // TypeCastTree
  TYPE_PARAMETER, // TypeParameterTree
  UNARY_MINUS, // UnaryTree
  UNARY_PLUS, // UnaryTree
  UNBOUNDED_WILDCARD, // WildcardTree
  UNION_TYPE, // UnionTypeTree
  UNSIGNED_RIGHT_SHIFT, // BinaryTree
  UNSIGNED_RIGHT_SHIFT_ASSIGNMENT, // CompoundAssignmentTree
  USES, // UsesTree
  VARIABLE, // VariableTree
  WHILE_LOOP, // WhileLoopTree
  XOR, // BinaryTree
  XOR_ASSIGNMENT, // CompoundAssignmentTree
  YIELD, // YieldTree
}

enum JTModifier {
  PUBLIC = 'public',
  PROTECTED = 'protected',
  PRIVATE = 'private',
  ABSTRACT = 'abstract',
  DEFAULT = 'default',
  STATIC = 'static',
  SEALED = 'sealed',
  NON_SEALED = 'non-sealed',
  FINAL = 'final',
  TRANSIENT = 'transient',
  VOLATILE = 'volatile',
  SYNCHRONIZED = 'synchronized',
  NATIVE = 'native',
  STRICTFP = 'strictfp',
}

enum JTReferenceMode {
  INVOKE, // Method references
  NEW, // Constructor references
}

enum JTTypeKind {
  BOOLEAN,
  BYTE,
  SHORT,
  INT,
  LONG,
  CHAR,
  FLOAT,
  DOUBLE,
  VOID,
  NONE,
  NULL,
  ARRAY,
  DECLARED,
  ERROR,
  TYPEVAR,
  WILDCARD,
  PACKAGE,
  EXECUTABLE,
  OTHER,
  UNION,
  INTERSECTION,
  MODULE,
}

interface JTAnnotatedType extends JTExpression<JTAnnotatedType> {
  annotations: JTAnnotation[];
  underlyingType: JTExpression<?>;
}

interface JTAnnotation extends JTExpression<JTAnnotation> {
  arguments: JTExpression<?>[];
  annotationType: JTTree<?>;
}

interface JTArrayAccess extends JTExpression<JTArrayAccess> {
  expression: JTExpression<?>;
  index: JTExpression<?>;
}

interface JTArrayType extends JTExpression<JTArrayType> {
  type: JTExpression<?>;
}

interface JTAssert extends JTExpression<JTAssert> {
  condition: JTExpression<?>;
  detail: JTExpression<?>;
}

interface JTAssign extends JTExpression<JTAssign> {
  expression: JTExpression<?>;
  variable: JTExpression<?>;
}

interface JTAssignOp extends JTOperatorExpression<JTAssignOp> {
  expression: JTExpression<?>;
  kind: JTKind.AND_ASSIGNMENT | JTKind.DIVIDE_ASSIGNMENT | JTKind.LEFT_SHIFT_ASSIGNMENT | JTKind.MINUS_ASSIGNMENT | JTKind.MULTIPLY_ASSIGNMENT | JTKind.OR_ASSIGNMENT | JTKind.PLUS_ASSIGNMENT | JTKind.REMAINDER_ASSIGNMENT | JTKind.RIGHT_SHIFT_ASSIGNMENT | JTKind.UNSIGNED_RIGHT_SHIFT_ASSIGNMENT | JTKind.XOR_ASSIGNMENT;
  variable: JTExpression<?>;
}

interface JTBinary extends JTOperatorExpression<JTBinary> {
  kind: JTKind.AND | JTKind.CONDITIONAL_AND | JTKind.CONDITIONAL_OR | JTKind.DIVIDE | JTKind.EQUAL_TO | JTKind.GREATER_THAN | JTKind.GREATER_THAN_EQUAL | JTKind.LEFT_SHIFT | JTKind.LESS_THAN | JTKind.LESS_THAN_EQUAL | JTKind.MINUS | JTKind.MULTIPLY | JTKind.NOT_EQUAL_TO | JTKind.OR | JTKind.PLUS | JTKind.REMAINDER | JTKind.RIGHT_SHIFT | JTKind.UNSIGNED_RIGHT_SHIFT | JTKind.XOR;
  leftOperand: JTExpression<?>;
  rightOperand: JTExpression<?>;
}

interface JTBindingPattern extends JTPattern<JTBindingPattern> {
  variable: JTVariableDecl;
}

interface JTBlock extends JTStatement<JTBlock> {
  statements: JTStatement<?>[];
  static: boolean;
}

interface JTBreak extends JTStatement<JTBreak> {
  label: JTName;
}

interface JTCaseLabel<Tree extends JTCaseLabel<Tree>> extends JTTree<JTCaseLabel> {
}

interface JTCase extends JTStatement<JTCase> {
  body: JTTree<?>[];
  caseKind: JTCaseKind;
  labels: JTCaseLabel<?>;
  statements: JTExpression<?>[];
}

interface JTCatch extends JTTree<JTCatch> {
  block: JTBlock;
  parameter: JTVariableDecl;
}

interface JTCharacter {
  readonly value: string;
}

interface JTClassDecl extends JTStatement<JTClassDecl> {
  extendsClause: JTExpression<?>;
  implementsClauses: JTExpression<?>[];
  kind: JTKind.ANNOTATION_TYPE | JTKind.CLASS | JTKind.ENUM | JTKind.INTERFACE | JTKind.RECORD;
  members: JTTree<?>[];
  modifiers: JTModifiers;
  permitsClauses: JTExpression<?>[];
  simpleName: JTName;
  typeParameters: JTTypeParameter[];
}

interface JTCompilationUnit extends JTTree<JTCompilationUnit> {
  imports: JTImport[];
  module: JTModuleDecl;
  package: JTPackageDecl;
  readonly sourceFile: string;
  typeDecls: JTTree<?>[];
}

interface JTConditional extends JTPolyExpression<JTConditional> {
  condition: JTExpression<?>;
  falseExpression: JTExpression<?>;
  trueExpression: JTExpression<?>;
}

interface JTContinue extends JTStatement<JTContinue> {
  label: JTName;
}

interface JTDefaultCaseLabel extends JTCaseLabel<JTDefaultCaseLabel> {
}

interface JTDirective<Tree extends JTDirective<Tree>> extends JTTree<JTDirective> {
}

interface JTDoWhileLoop extends JTStatement<JTDoWhileLoop> {
  condition: JTExpression<?>;
  statement: JTStatement<?>;
}

interface JTSkip extends JTStatement<JTSkip> {
}

interface JTEnhancedForLoop extends JTStatement<JTEnhancedForLoop> {
  expression: JTExpression<?>;
  statement: JTStatement<?>;
  variable: JTVariableDecl;
}

interface JTErroneous extends JTExpression<JTErroneous> {
  errorTree: JTTree<?>;
}

interface JTExports extends JTDirective<JTExports> {
  moduleNames: JTExpression<?>;
  packageName: JTExpression;
}

interface JTExpression<Tree extends JTExpression<Tree>> extends JTCaseLabel<JTExpression> {
}

interface JTExpressionStatement extends JTStatement<JTExpressionStatement> {
  expression: JTExpression<?>;
}

interface JTFloat {
  readonly value: number;
}

interface JTForLoop extends JTStatement<JTForLoop> {
  condition: JTExpression<?>;
  initializer: JTStatement<?>[];
  statement: JTStatement<?>;
  update: JTExpressionStatement[];
}

interface JTFunctionalExpression<Tree extends JTFunctionalExpression<Tree>> extends JTPolyExpression<JTFunctionalExpression> {
  body: JTTree<?>;
  bodyKind: JTBodyKind;
  parameters: JTVariableDecl[];
}

interface JTGuardedPattern extends JTPattern<JTGuardedPattern> {
  expression: JTExpression<?>;
  pattern: JTPattern<?>;
}

interface JTFieldAccess extends JTExpression<JTFieldAccess> {
  expression: JTExpression<?>;
  identifier: JTName;
}

interface JTIdent extends JTExpression<JTIdent> {
  name: JTName;
}

interface JTImport extends JTTree<JTImport> {
  qualifiedIdentifier: JTTree<?>;
  staticImport: boolean;
}

interface JTIf extends JTStatement<JTIf> {
  condition: JTExpression<?>;
  elseStatement: JTStatement<?>;
  thenStatement: JTStatement<?>;
}

interface JTInstanceOf extends JTExpression<JTInstanceOf> {
  expression: JTExpression<?>;
  pattern: JTTree<?>;
}

interface JTLabeledStatement extends JTStatement<JTLabeledStatement> {
  label: JTName;
  statement: JTStatement<?>;
}

interface JTLambda extends JTFunctionalExpression<JTLambda> {
  body: JTTree<?>;
  bodyKind: JTBodyKind;
  parameters: JTVariableDecl[];
}

interface JTLiteral extends JTExpression<JTLiteral> {
  kind: JTKind.BOOLEAN_LITERAL | JTKind.CHAR_LITERAL | JTKind.DOUBLE_LITERAL | JTKind.FLOAT_LITERAL | JTKind.INT_LITERAL | JTKind.LONG_LITERAL | JTKind.NULL_LITERAL | JTKind.STRING_LITERAL;
  value: number | string | boolean | JTFloat | JTCharacter | null;
}

interface JTMemberReference extends JTFunctionalExpression<JTMemberReference> {
  mode: JTReferenceMode;
  name: JTName;
  qualifiedExpression: JTExpression<?>;
  typeArguments: JTExpression<?>[];
}

interface JTMethodDecl extends JTTree<JTMethodDecl> {
  body: JTBlock;
  defaultValue: JTExpression<?>;
  modifiers: JTModifiers;
  name: JTName;
  parameters: JTVariableDecl[];
  receiverParameter: JTVariableDecl;
  returnType: JTExpression<?>;
  throwExpressions: JTExpression<?>[];
  typeParameters: JTTypeParameter[];
}

interface JTMethodInvocation extends JTPolyExpression<JTMethodInvocation> {
  arguments: JTExpression<?>[];
  methodSelect: JTExpression<?>;
  typeArguments: JTExpression[];
}

interface JTModifiers extends JTTree<JTModifiers> {
  annotations: JTAnnotation[];
  flags: JTModifier[];
}

interface JTModuleDecl extends JTTree<JTModuleDecl> {
  annotations: JTAnnotation[];
  name: JTExpression<?>;
  directives: JTDirective;
}

interface JTNewArray extends JTExpression<JTNewArray> {
  annotations: JTAnnotation[];
  dimAnnotations: JTAnnotation[][];
  dimensions: JTExpression<?>[];
  initializers: JTExpression<?>[];
  type: JTExpression<?>;
}

interface JTNewClass extends JTPolyExpression<JTNewClass> {
  arguments: JTExpression<?>[];
  classBody: JTClassDecl;
  enclosingExpression: JTExpression<?>;
  identifier: JTExpression<?>;
  typeArguments: JTExpression<?>[];
}

interface JTOpens extends JTDirective<JTOpens> {
  moduleNames: JTExpression<?>[];
  packageName: JTExpression<?>;
}

interface JTName {
  value: string;
}

interface JTOperatorExpression<Tree extends JTOperatorExpression<Tree>> extends JTExpression<JTOperatorExpression> {
}

interface JTPackageDecl extends JTTree<JTPackageDecl> {
  annotations: JTAnnotation[];
  packageName: JTExpression<?>;
}

interface JTParens extends JTExpression<JTParens> {
  expression: JTExpression<?>;
}

interface JTParenthesizedPattern extends JTPattern<JTParenthesizedPattern> {
  pattern: JTPattern<?>;
}

interface JTPattern<Tree extends JTPattern<Tree>> extends JTCaseLabel<JTPattern> {
}

interface JTPolyExpression<Tree extends JTPolyExpression<Tree>> extends JTExpression<JTPolyExpression> {
}

interface JTPrimitiveType extends JTExpression<JTPrimitiveType> {
  primitiveTypeKind: JTTypeKind;
}

interface JTProvides extends JTDirective<JTProvides> {
  implementationNames: JTExpression<?>[];
  serviceName: JTExpression<?>;
}

interface JTRequires extends JTDirective<JTRequires> {
  moduleName: JTExpression<?>;
  static: boolean;
  transitive: boolean;
}

interface JTReturn extends JTStatement<JTReturn> {
  expression: JTExpression<?>;
}

interface JTSwitch extends JTStatement<JTSwitch> {
  cases: JTCase[];
  expression: JTExpression<?>;
}

interface JTSwitchExpression extends JTPolyExpression<JTSwitchExpression> {
  cases: JTCase[];
  expression: JTExpression<?>;
}

interface JTSynchronized extends JTStatement<JTSynchronized> {
  block: JTBlock;
  expression: JTExpression<?>;
}

interface JTStatement<Tree extends JTStatement<Tree>> extends JTTree<JTStatement> {
}

interface JTThrow extends JTStatement<JTThrow> {
  expression: JTExpression<?>;
}

interface JTTry extends JTStatement<JTTry> {
  block: JTBlock;
  catches: JTCatch<?>[];
  finallyBlock: JTBlock;
  resources: JTTree<?>[];
}

interface JTTree<Tree extends JTTree<Tree>> {
  readonly className: string;
  readonly classSimpleName: string;
  readonly kind: JTKind;
  readonly parentTree: JTTree<?>;
  isActionChange(): boolean;
  isActionIgnore(): boolean;
  isActionNoChange(): boolean;
  setActionChange(): boolean;
  setActionIgnore(): boolean;
  setActionNoChange(): boolean;
  toString(): string;
}

interface JTTypeApply extends JTExpression<JTTypeApply> {
  type: JTExpression<?>;
  typeArguments: JTExpression<?>[];
}

interface JTTypeCast extends JTExpression<JTTypeCast> {
  expression: JTExpression<?>;
  type: JTTree<?>;
}

interface JTTypeIntersection extends JTExpression<JTTypeIntersection> {
  bounds: JTExpression<?>;
}

interface JTTypeParameter extends JTTree<JTTypeParameter> {
  annotations: JTAnnotation[];
  bound: JTExpression<?>;
  name: JTName;
}

interface JTTypeUnion extends JTExpression<JTTypeUnion> {
  typeAlternatives: JTExpression<?>;
}

interface JTUnary extends JTOperatorExpression<JTUnary> {
  expression: JTExpression<?>;
  kind: JTKind.BITWISE_COMPLEMENT | JTKind.LOGICAL_COMPLEMENT | JTKind.POSTFIX_DECREMENT | JTKind.POSTFIX_INCREMENT | JTKind.PREFIX_DECREMENT | JTKind.PREFIX_INCREMENT | JTKind.UNARY_MINUS | JTKind.UNARY_PLUS;
}

interface JTUses extends JTDirective<JTUses> {
  serviceName: JTExpression<?>;
}

interface JTVariableDecl extends JTStatement<JTVariableDecl> {
  initializer: JTExpression<?>;
  modifiers: JTModifiers;
  name: JTName;
  nameExpression: JTExpression<?>;
  type: JTExpression<?>;
}

interface JTWhileLoop extends JTStatement<JTWhileLoop> {
  condition: JTExpression<?>;
  statement: JTStatement<?>;
}

interface JTWildcard extends JTExpression<JTWildcard> {
  bound: JTTree<?>;
  kind: JTKind.EXTENDS_WILDCARD | JTKind.SUPER_WILDCARD | JTKind.UNBOUNDED_WILDCARD;
}

interface JTYield extends JTStatement<JTYield> {
  value: JTExpression<?>;
}

interface TransformOptions {
  /**
   * Include the AST in the returned object
   *
   * Default: `false`
   */
  ast?: boolean | null | undefined;
  /**
   * Enable code generation
   *
   * Default: `true`
   */
  code?: boolean | null | undefined;
  /**
   * Context for evaluation
   *
   * Default: undefined
   */
  context?: object | null | undefined;
  /**
   * Specify the file name in source type 'string' mode
   *
   * Default: `undefined`
   */
  fileName?: string | null | undefined;
  /**
   * List of plugins to load and use
   *
   * Default: `[]`
   */
  plugins?: TransformOptionsPlugin[] | null | undefined;
  /**
   * Type of the source
   *
   * Default: `file`
   */
  sourceType?: 'string' | 'file';
  /**
   * Transform style
   *
   * Default: null
   */
  style?: TransformOptionStyle;
}

interface TransformOptionsPlugin {
  visitor: TransformOptionsPluginVisitor;
}

interface TransformOptionsPluginVisitor {
  AnnotatedType(node: JTAnnotatedType, context?: object | null | undefined): void;
  Annotation(node: JTAnnotation, context?: object | null | undefined): void;
  ArrayAccess(node: JTArrayAccess, context?: object | null | undefined): void;
  ArrayType(node: JTArrayType, context?: object | null | undefined): void;
  Assert(node: JTAssert, context?: object | null | undefined): void;
  Assignment(node: JTAssign, context?: object | null | undefined): void;
  Binary(node: JTBinary, context?: object | null | undefined): void;
  BindingPattern(node: JTBindingPattern, context?: object | null | undefined): void;
  Block(node: JTBlock, context?: object | null | undefined): void;
  Break(node: JTBreak, context?: object | null | undefined): void;
  Case(node: JTCase, context?: object | null | undefined): void;
  Catch(node: JTCatch, context?: object | null | undefined): void;
  Class(node: JTClassDecl, context?: object | null | undefined): void;
  CompilationUnit(node: JTCompilationUnit, context?: object | null | undefined): void;
  CompoundAssignment(node: JTAssignOp, context?: object | null | undefined): void;
  ConditionalExpression(node: JTConditional, context?: object | null | undefined): void;
  Continue(node: JTContinue, context?: object | null | undefined): void;
  DefaultCaseLabel(node: JTDefaultCaseLabel, context?: object | null | undefined): void;
  DoWhileLoop(node: JTDoWhileLoop, context?: object | null | undefined): void;
  EmptyStatement(node: JTSkip, context?: object | null | undefined): void;
  EnhancedForLoop(node: JTEnhancedForLoop, context?: object | null | undefined): void;
  Erroneous(node: JTErroneous, context?: object | null | undefined): void;
  Exports(node: JTExports, context?: object | null | undefined): void;
  ExpressionStatement(node: JTExpressionStatement, context?: object | null | undefined): void;
  ForLoop(node: JTForLoop, context?: object | null | undefined): void;
  GuardedPattern(node: JTGuardedPattern, context?: object | null | undefined): void;
  Identifier(node: JTIdent, context?: object | null | undefined): void;
  If(node: JTIf, context?: object | null | undefined): void;
  Import(node: JTImport, context?: object | null | undefined): void;
  InstanceOf(node: JTInstanceOf, context?: object | null | undefined): void;
  IntersectionType(node: JTTypeIntersection, context?: object | null | undefined): void;
  LabeledStatement(node: JTLabeledStatement, context?: object | null | undefined): void;
  LambdaExpression(node: JTLambda, context?: object | null | undefined): void;
  Literal(node: JTLiteral, context?: object | null | undefined): void;
  MemberReference(node: JTMemberReference, context?: object | null | undefined): void;
  MemberSelect(node: JTFieldAccess, context?: object | null | undefined): void;
  Method(node: JTMethodDecl, context?: object | null | undefined): void;
  MethodInvocation(node: JTMethodInvocation, context?: object | null | undefined): void;
  Modifiers(node: JTModifiers, context?: object | null | undefined): void;
  Module(node: JTModuleDecl, context?: object | null | undefined): void;
  NewArray(node: JTNewArray, context?: object | null | undefined): void;
  NewClass(node: JTNewClass, context?: object | null | undefined): void;
  Opens(node: JTOpens, context?: object | null | undefined): void;
  Other(node: JTTree, context?: object | null | undefined): void;
  Package(node: JTPackageDecl, context?: object | null | undefined): void;
  ParameterizedType(node: JTTypeApply, context?: object | null | undefined): void;
  Parenthesized(node: JTParens, context?: object | null | undefined): void;
  ParenthesizedPattern(node: JTParenthesizedPattern, context?: object | null | undefined): void;
  PrimitiveType(node: JTPrimitiveType, context?: object | null | undefined): void;
  Provides(node: JTProvides, context?: object | null | undefined): void;
  Requires(node: JTRequires, context?: object | null | undefined): void;
  Return(node: JTReturn, context?: object | null | undefined): void;
  Scan(node: JTTree, context?: object | null | undefined): void;
  Switch(node: JTSwitch, context?: object | null | undefined): void;
  SwitchExpression(node: JTSwitchExpression, context?: object | null | undefined): void;
  Synchronized(node: JTSynchronized, context?: object | null | undefined): void;
  Throw(node: JTThrow, context?: object | null | undefined): void;
  Try(node: JTTry, context?: object | null | undefined): void;
  TypeCast(node: JTTypeCast, context?: object | null | undefined): void;
  TypeParameter(node: JTTypeParameter, context?: object | null | undefined): void;
  Unary(node: JTUnary, context?: object | null | undefined): void;
  UnionType(node: JTTypeUnion, context?: object | null | undefined): void;
  Uses(node: JTUses, context?: object | null | undefined): void;
  Variable(node: JTVariableDecl, context?: object | null | undefined): void;
  WhileLoop(node: JTWhileLoop, context?: object | null | undefined): void;
  Wildcard(node: JTWildcard, context?: object | null | undefined): void;
  Yield(node: JTYield, context?: object | null | undefined): void;
}

interface TransformOptionStyle {
  /**
   * Size of the continuation indent
   *
   * Default: 8
   */
  continuationIndentSize?: number | null | undefined;
  /**
   * Size of the indent
   *
   * Default: 4
   */
  indentSize?: number | null | undefined;
  /**
   * Preserve the copyrights or not
   *
   * Default: true
   */
  preserveCopyrights?: boolean | null | undefined;
  /**
   * Style type
   *
   * Default: 'standard'
   */
  type?: 'compact' | 'standard' | null | undefined;
  /**
   * Wrap if the line length is greater than
   *
   * Default: 120
   */
  wordWrapColumn?: number | null | undefined;
}

interface TransformResult {
  ast?: JTCompilationUnit | undefined;
  code?: string | undefined;
}

declare namespace jaspiler {
  export const argv: Array<string>;

  export function createCharacter(value: string): JTCharacter;
  export function createFieldAccess(...values: string[]): JTFieldAccess;
  export function createFloat(value: string): JTFloat;
  export function createIdent(value: string): JTIdent;
  export function createLiteral(value: string): JTLiteral;
  export function createName(value: string): JTName;

  export function newAnnotatedType(): JTAnnotatedType;
  export function newAnnotation(): JTAnnotation;
  export function newArrayAccess(): JTArrayAccess;
  export function newArrayType(): JTArrayType;
  export function newAssert(): JTAssert;
  export function newAssign(): JTAssign;
  export function newAssignOp(): JTAssignOp;
  export function newBinary(): JTBinary;
  export function newBindingPattern(): JTBindingPattern;
  export function newBlock(): JTBlock;
  export function newBreak(): JTBreak;
  export function newCase(): JTCase;
  export function newCatch(): JTCatch;
  export function newClassDecl(): JTClassDecl;
  export function newConditional(): JTConditional;
  export function newContinue(): JTContinue;
  export function newDefaultCaseLabel(): JTDefaultCaseLabel;
  export function newDoWhileLoop(): JTDoWhileLoop;
  export function newEnhancedForLoop(): JTEnhancedForLoop;
  export function newErroneous(): JTErroneous;
  export function newExports(): JTExports;
  export function newExpressionStatement(): JTExpressionStatement;
  export function newFieldAccess(): JTFieldAccess;
  export function newForLoop(): JTForLoop;
  export function newGuardedPattern(): JTGuardedPattern;
  export function newIdent(): JTIdent;
  export function newIf(): JTIf;
  export function newImport(): JTImport;
  export function newInstanceOf(): JTInstanceOf;
  export function newLabeledStatement(): JTLabeledStatement;
  export function newLambda(): JTLambda;
  export function newLiteral(): JTLiteral;
  export function newMemberReference(): JTMemberReference;
  export function newMethodDecl(): JTMethodDecl;
  export function newMethodInvocation(): JTMethodInvocation;
  export function newModifiers(): JTModifiers;
  export function newModuleDecl(): JTModuleDecl;
  export function newNewArray(): JTNewArray;
  export function newNewClass(): JTNewClass;
  export function newOpens(): JTOpens;
  export function newPackageDecl(): JTPackageDecl;
  export function newParens(): JTParens;
  export function newParenthesizedPattern(): JTParenthesizedPattern;
  export function newPrimitiveType(): JTPrimitiveType;
  export function newProvides(): JTProvides;
  export function newRequires(): JTRequires;
  export function newReturn(): JTReturn;
  export function newSkip(): JTSkip;
  export function newSwitch(): JTSwitch;
  export function newSwitchExpression(): JTSwitchExpression;
  export function newSynchronized(): JTSynchronized;
  export function newThrow(): JTThrow;
  export function newTry(): JTTry;
  export function newTypeApply(): JTTypeApply;
  export function newTypeCast(): JTTypeCast;
  export function newTypeIntersection(): JTTypeIntersection;
  export function newTypeParameter(): JTTypeParameter;
  export function newTypeUnion(): JTTypeUnion;
  export function newUnary(): JTUnary;
  export function newUses(): JTUses;
  export function newVariableDecl(): JTVariableDecl;
  export function newWhileLoop(): JTWhileLoop;
  export function newWildcard(): JTWildcard;
  export function newYield(): JTYield;

  export function transformSync(source: string, options?: TransformOptions): TransformResult;
}
