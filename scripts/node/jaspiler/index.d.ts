/// <reference no-default-lib="true"/>

interface JTAnnotation extends JTExpression<JTAnnotation> {
  arguments: JTExpression<?>[];
  annotationType: JTTree<?>;
}

interface JTCaseLabel<Tree extends JTCaseLabel<Tree>> extends JTTree<JTCaseLabel> {
}

interface JTCompilationUnit extends JTTree<JTCompilationUnit> {
  package: JTPackageDecl;
  imports: JTImport[];
  typeDecls: JTTree<?>[];
  module: JTModuleDecl;
  readonly sourceFile: string;
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

interface JTTree<Tree extends JTTree<Tree>> {
  readonly className: string;
  readonly classSimpleName: string;
  getParentTree(): JTTree<?>;
  isActionChange(): boolean;
  isActionIgnore(): boolean;
  isActionNoChange(): boolean;
  setActionChange(): boolean;
  setActionIgnore(): boolean;
  setActionNoChange(): boolean;
  toString(): string;
}

interface TransformOptions {
  plugins: TransformOptionsPlugin[];
}

interface TransformOptionsPlugin {
  visitor: TransformOptionsPluginVisitor;
}

interface TransformOptionsPluginVisitor {
  CompilationUnit(node: JTCompilationUnit): void;
  Package(node: JTPackageDecl): void;
}

interface TransformResult {
  code: string;
  ast: JTCompilationUnit;
}

declare namespace jaspiler {
  function createFieldAccess(...strings: string[]): JTFieldAccess;
  function createName(value: string): JTName;

  function newImport(): JTImport;
  function newPackageDecl(): JTPackageDecl;

  function transformSync(path: string, options: TransformOptions): TransformResult;
}
