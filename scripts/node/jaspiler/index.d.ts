/// <reference no-default-lib="true"/>

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

interface JTAnnotation extends JTExpression<JTAnnotation> {
  arguments: JTExpression<?>[];
  annotationType: JTTree<?>;
}

interface JTCaseLabel<Tree extends JTCaseLabel<Tree>> extends JTTree<JTCaseLabel> {
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

interface JTDirective<Tree extends JTDirective<Tree>> extends JTTree<JTDirective> {
}

interface JTExpression<Tree extends JTExpression<Tree>> extends JTCaseLabel<JTExpression> {
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

interface JTPackageDecl extends JTTree<JTPackageDecl> {
  annotations: JTAnnotation[];
  packageName: JTExpression<?>;
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
}

interface TransformOptionsPlugin {
  visitor: TransformOptionsPluginVisitor;
}

interface TransformOptionsPluginVisitor {
  Class(node: JTClassDecl): void;
  CompilationUnit(node: JTCompilationUnit): void;
  Package(node: JTPackageDecl): void;
}

interface TransformResult {
  ast?: JTCompilationUnit | undefined;
  code?: string | undefined;
}

declare namespace jaspiler {
  function createFieldAccess(...strings: string[]): JTFieldAccess;
  function createIdent(name: string): JTIdent;
  function createName(name: string): JTName;

  function newAnnotation(): JTAnnotation;
  function newImport(): JTImport;
  function newPackageDecl(): JTPackageDecl;

  function transformSync(source: string, options?: TransformOptions): TransformResult;
}
