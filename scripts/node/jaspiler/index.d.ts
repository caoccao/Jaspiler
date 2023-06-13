/// <reference no-default-lib="true"/>

interface JTName {
  value: string;
}

interface JTTree<Tree extends JTTree<Tree>> {
  isActionChange(): boolean;
  isActionIgnore(): boolean;
  isActionNoChange(): boolean;
  setActionChange(): boolean;
  setActionIgnore(): boolean;
  setActionNoChange(): boolean;
  toString(): string;
}

interface JTDirective<Tree extends JTDirective<Tree>> extends JTTree<JTDirective> {
}

interface JTCaseLabel<Tree extends JTCaseLabel<Tree>> extends JTTree<JTCaseLabel> {
}

interface JTExpression<Tree extends JTExpression<Tree>> extends JTCaseLabel<JTExpression> {
}

interface JTFieldAccess extends JTExpression<JTFieldAccess> {
  expression: JTExpression<?>;
  identifier: JTName;
}

interface JTAnnotation extends JTExpression<JTAnnotation> {
  arguments: JTExpression<?>[];
  annotationType: JTTree<?>;
}

interface JTModuleDecl extends JTTree<JTModuleDecl> {
  annotations: JTAnnotation[];
  name: JTExpression<?>;
  directives: JTDirective;
}

interface JTPackageDecl extends JTTree<JTPackageDecl> {
  annotations: JTAnnotation[];
  packageName: JTExpression<?>;
}

interface JTImport extends JTTree<JTImport> {
  qualifiedIdentifier: JTTree<?>;
  staticImport: boolean;
}

interface JTCompilationUnit extends JTTree<JTCompilationUnit> {
  package: JTPackageDecl;
  imports: JTImport[];
  typeDecls: JTTree<?>[];
  module: JTModuleDecl;
  sourceFile: string;
}

interface TransformOptionsPluginVisitor {
  CompilationUnit(node: JTCompilationUnit): void;
  Package(node: JTPackageDecl): void;
}

interface TransformOptionsPlugin {
  visitor: TransformOptionsPluginVisitor;
}

interface TransformOptions {
  plugins: TransformOptionsPlugin[];
}

interface TransformResult {
  code: string;
  ast: JTCompilationUnit;
}

declare namespace jaspiler {
  function createFieldAccess(...strings: string[]): JTFieldAccess;
  function createName(value: string): JTName;

  function newImport(): JTImport;

  function transformSync(path: string, options: TransformOptions): TransformResult;
}
