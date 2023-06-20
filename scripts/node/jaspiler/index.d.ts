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
 *   and add the following line to your JS/TS files.
 *   You will get the intellisense in VS Code.
 *
 *   /// <reference types="${yourLocalPath}/jaspiler/index.d.ts"/>
 */

/// <reference no-default-lib="true"/>

enum JTCaseKind {
  'STATEMENT',
  'RULE',
}

enum JTKind {
  'AND', // BinaryTree
  'AND_ASSIGNMENT', // CompoundAssignmentTree
  'ANNOTATED_TYPE', // AnnotatedTypeTree
  'ANNOTATION', // AnnotationTree
  'ANNOTATION_TYPE', // ClassTree
  'ARRAY_ACCESS', // ArrayAccessTree
  'ARRAY_TYPE', // ArrayTypeTree
  'ASSERT', // AssertTree
  'ASSIGNMENT', // AssignmentTree
  'BINDING_PATTERN', // BindingPatternTree
  'BITWISE_COMPLEMENT', // UnaryTree
  'BLOCK', // BlockTree
  'BOOLEAN_LITERAL', // LiteralTree
  'BREAK', // BreakTree
  'CASE', // CaseTree
  'CATCH', // CatchTree
  'CHAR_LITERAL', // LiteralTree
  'CLASS', // ClassTree
  'COMPILATION_UNIT', // CompilationUnitTree
  'CONDITIONAL_AND', // BinaryTree
  'CONDITIONAL_EXPRESSION', // ConditionalExpressionTree
  'CONDITIONAL_OR', // BinaryTree
  'CONTINUE', // ContinueTree
  'DEFAULT_CASE_LABEL', // DefaultCaseLabelTree
  'DIVIDE', // BinaryTree
  'DIVIDE_ASSIGNMENT', // CompoundAssignmentTree
  'DO_WHILE_LOOP', // DoWhileLoopTree
  'DOUBLE_LITERAL', // LiteralTree
  'EMPTY_STATEMENT', // EmptyStatementTree
  'ENHANCED_FOR_LOOP', // EnhancedForLoopTree
  'ENUM', // ClassTree
  'EQUAL_TO', // BinaryTree
  'ERRONEOUS', // ErroneousTree
  'EXPORTS', // ExportsTree
  'EXPRESSION_STATEMENT', // ExpressionStatementTree
  'EXTENDS_WILDCARD', // WildcardTree
  'FLOAT_LITERAL', // LiteralTree
  'FOR_LOOP', // ForLoopTree
  'GREATER_THAN', // BinaryTree
  'GREATER_THAN_EQUAL', // BinaryTree
  'GUARDED_PATTERN', // GuardedPatternTree
  'IDENTIFIER', // IdentifierTree
  'IF', // IfTree
  'IMPORT', // ImportTree
  'INSTANCE_OF', // InstanceOfTree
  'INT_LITERAL', // LiteralTree
  'INTERFACE', // ClassTree
  'INTERSECTION_TYPE', // IntersectionTypeTree
  'LABELED_STATEMENT', // LabeledStatementTree
  'LAMBDA_EXPRESSION', // LambdaExpressionTree
  'LEFT_SHIFT', // BinaryTree
  'LEFT_SHIFT_ASSIGNMENT', // CompoundAssignmentTree
  'LESS_THAN', // BinaryTree
  'LESS_THAN_EQUAL', // BinaryTree
  'LOGICAL_COMPLEMENT', // UnaryTree
  'LONG_LITERAL', // LiteralTree
  'MEMBER_REFERENCE', // MemberReferenceTree
  'MEMBER_SELECT', // MemberSelectTree
  'METHOD', // MethodTree
  'METHOD_INVOCATION', // MethodInvocationTree
  'MINUS', // BinaryTree
  'MINUS_ASSIGNMENT', // CompoundAssignmentTree
  'MODIFIERS', // ModifiersTree
  'MODULE', // ModuleTree
  'MULTIPLY', // BinaryTree
  'MULTIPLY_ASSIGNMENT', // CompoundAssignmentTree
  'NEW_ARRAY', // NewArrayTree
  'NEW_CLASS', // NewClassTree
  'NOT_EQUAL_TO', // BinaryTree
  'NULL_LITERAL', // LiteralTree
  'OPENS', // OpensTree
  'OR', // BinaryTree
  'OR_ASSIGNMENT', // CompoundAssignmentTree
  'OTHER', // null
  'PACKAGE', // PackageTree
  'PARAMETERIZED_TYPE', // ParameterizedTypeTree
  'PARENTHESIZED', // ParenthesizedTree
  'PARENTHESIZED_PATTERN', // ParenthesizedPatternTree
  'PLUS', // BinaryTree
  'PLUS_ASSIGNMENT', // CompoundAssignmentTree
  'POSTFIX_DECREMENT', // UnaryTree
  'POSTFIX_INCREMENT', // UnaryTree
  'PREFIX_DECREMENT', // UnaryTree
  'PREFIX_INCREMENT', // UnaryTree
  'PRIMITIVE_TYPE', // PrimitiveTypeTree
  'PROVIDES', // ProvidesTree
  'RECORD', // ClassTree
  'REMAINDER', // BinaryTree
  'REMAINDER_ASSIGNMENT', // CompoundAssignmentTree
  'REQUIRES', // RequiresTree
  'RETURN', // ReturnTree
  'RIGHT_SHIFT', // BinaryTree
  'RIGHT_SHIFT_ASSIGNMENT', // CompoundAssignmentTree
  'STRING_LITERAL', // LiteralTree
  'SUPER_WILDCARD', // WildcardTree
  'SWITCH', // SwitchTree
  'SWITCH_EXPRESSION', // SwitchExpressionTree
  'SYNCHRONIZED', // SynchronizedTree
  'THROW', // ThrowTree
  'TRY', // TryTree
  'TYPE_ANNOTATION', // AnnotationTree
  'TYPE_CAST', // TypeCastTree
  'TYPE_PARAMETER', // TypeParameterTree
  'UNARY_MINUS', // UnaryTree
  'UNARY_PLUS', // UnaryTree
  'UNBOUNDED_WILDCARD', // WildcardTree
  'UNION_TYPE', // UnionTypeTree
  'UNSIGNED_RIGHT_SHIFT', // BinaryTree
  'UNSIGNED_RIGHT_SHIFT_ASSIGNMENT', // CompoundAssignmentTree
  'USES', // UsesTree
  'VARIABLE', // VariableTree
  'WHILE_LOOP', // WhileLoopTree
  'XOR', // BinaryTree
  'XOR_ASSIGNMENT', // CompoundAssignmentTree
  'YIELD', // YieldTree
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
  kind: JTKind;
  variable: JTExpression<?>;
}

interface JTBinary extends JTOperatorExpression<JTBinary> {
  kind: JTKind;
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

interface JTClassDecl extends JTStatement<JTClassDecl> {
  extendsClause: JTExpression<?>;
  implementsClauses: JTExpression<?>[];
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

interface JTForLoop extends JTStatement<JTForLoop> {
  condition: JTExpression<?>;
  initializer: JTStatement<?>[];
  statement: JTStatement<?>;
  update: JTExpressionStatement[];
}

interface JTGuardedPattern extends JTPattern<JTGuardedPattern> {
  expression: JTExpression<?>;
  pattern: JTPattern<?>;
}

interface JTFieldAccess extends JTExpression<JTFieldAccess> {
  expression: JTExpression<?>;
  identifier: JTName;
}

interface JTImport extends JTTree<JTImport> {
  qualifiedIdentifier: JTTree<?>;
  staticImport: boolean;
}

interface JTIdent extends JTExpression<JTIdent> {
  name: JTName;
}

interface JTIf extends JTStatement<JTIf> {
  condition: JTExpression<?>;
  elseStatement: JTStatement<?>;
  thenStatement: JTStatement<?>;
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

interface JTModifiers extends JTTree<JTModifiers> {
  annotations: JTAnnotation[];
  flags: string[];
}

interface JTModuleDecl extends JTTree<JTModuleDecl> {
  annotations: JTAnnotation[];
  name: JTExpression<?>;
  directives: JTDirective;
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

interface JTPattern<Tree extends JTPattern<Tree>> extends JTCaseLabel<JTPattern> {
}

interface JTPolyExpression<Tree extends JTPolyExpression<Tree>> extends JTExpression<JTPolyExpression> {
}

interface JTStatement<Tree extends JTStatement<Tree>> extends JTTree<JTStatement> {
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

interface JTTypeParameter extends JTTree<JTTypeParameter> {
  annotations: JTAnnotation[];
  bound: JTExpression<?>;
  name: JTName;
}

interface JTVariableDecl extends JTStatement<JTVariableDecl> {
  initializer: JTExpression<?>;
  modifiers: JTModifier;
  name: JTName;
  nameExpression: JTExpression<?>;
  type: JTExpression<?>;
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
  AnnotatedType(node: JTAnnotatedType): void;
  Annotation(node: JTAnnotation): void;
  ArrayAccess(node: JTArrayAccess): void;
  ArrayType(node: JTArrayType): void;
  Assert(node: JTAssert): void;
  Assignment(node: JTAssign): void;
  Binary(node: JTBinary): void;
  BindingPattern(node: JTBindingPattern): void;
  Block(node: JTBlock): void;
  Break(node: JTBreak): void;
  Case(node: JTCase): void;
  Catch(node: JTCatch): void;
  Class(node: JTClassDecl): void;
  CompilationUnit(node: JTCompilationUnit): void;
  CompoundAssignment(node: JTAssignOp): void;
  ConditionalExpression(node: JTConditional): void;
  Continue(node: JTContinue): void;
  DefaultCaseLabel(node: JTDefaultCaseLabel): void;
  DoWhileLoop(node: JTDoWhileLoop): void;
  EnhancedForLoop(node: JTEnhancedForLoop): void;
  Erroneous(node: JTErroneous): void;
  Exports(node: JTExports): void;
  ExpressionStatement(node: JTExpressionStatement): void;
  ForLoop(node: JTForLoop): void;
  GuardedPattern(node: JTGuardedPattern): void;
  Identifier(node: JTIdent): void;
  If(node: JTIf): void;
  Import(node: JTImport): void;
  MemberSelect(node: JTFieldAccess): void;
  Method(node: JTMethodDecl): void;
  Package(node: JTPackageDecl): void;
  Variable(node: JTVariableDecl): void;
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
  function createFieldAccess(...strings: string[]): JTFieldAccess;
  function createIdent(name: string): JTIdent;
  function createName(name: string): JTName;

  function newAnnotatedType(): JTAnnotatedType;
  function newAnnotation(): JTAnnotation;
  function newArrayAccess(): JTArrayAccess;
  function newArrayType(): JTArrayType;
  function newAssert(): JTAssert;
  function newAssign(): JTAssign;
  function newAssignOp(): JTAssignOp;
  function newBinary(): JTBinary;
  function newBindingPattern(): JTBindingPattern;
  function newBlock(): JTBlock;
  function newBreak(): JTBreak;
  function newCase(): JTCase;
  function newCatch(): JTCatch;
  function newClassDecl(): JTClassDecl;
  function newConditional(): JTConditional;
  function newContinue(): JTContinue;
  function newDefaultCaseLabel(): JTDefaultCaseLabel;
  function newDoWhileLoop(): JTDoWhileLoop;
  function newEnhancedForLoop(): JTEnhancedForLoop;
  function newErroneous(): JTErroneous;
  function newExports(): JTExports;
  function newExpressionStatement(): JTExpressionStatement;
  function newFieldAccess(): JTFieldAccess;
  function newForLoop(): JTForLoop;
  function newGuardedPattern(): JTGuardedPattern;
  function newIdent(): JTIdent;
  function newIf(): JTIf;
  function newImport(): JTImport;
  function newInstanceOf(): JTInstanceOf;
  function newLabeledStatement(): JTLabeledStatement;
  function newLambda(): JTLambda;
  function newLiteral(): JTLiteral;
  function newMemberReference(): JTMemberReference;
  function newMethodDecl(): JTMethodDecl;
  function newMethodInvocation(): JTMethodInvocation;
  function newModifiers(): JTModifiers;
  function newModuleDecl(): JTModuleDecl;
  function newNewArray(): JTNewArray;
  function newNewClass(): JTNewClass;
  function newOpens(): JTOpens;
  function newPackageDecl(): JTPackageDecl;
  function newParens(): JTParens;
  function newParenthesizedPattern(): JTParenthesizedPattern;
  function newPrimitiveTypeTree(): JTPrimitiveTypeTree;
  function newProvides(): JTProvides;
  function newRequires(): JTRequires;
  function newReturn(): JTReturn;
  function newSkip(): JTSkip;
  function newSwitch(): JTSwitch;
  function newSwitchExpression(): JTSwitchExpression;
  function newSynchronized(): JTSynchronized;
  function newThrow(): JTThrow;
  function newTry(): JTTry;
  function newTypeApply(): JTTypeApply;
  function newTypeCast(): JTTypeCast;
  function newTypeIntersection(): JTTypeIntersection;
  function newTypeParameter(): JTTypeParameter;
  function newTypeUnion(): JTTypeUnion;
  function newUnary(): JTUnary;
  function newUses(): JTUses;
  function newVariableDecl(): JTVariableDecl;
  function newWhileLoop(): JTWhileLoop;
  function newWildcard(): JTWildcard;
  function newYield(): JTYield;

  function transformSync(source: string, options?: TransformOptions): TransformResult;
}
