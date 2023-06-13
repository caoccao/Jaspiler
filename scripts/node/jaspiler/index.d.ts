/// <reference no-default-lib="true"/>

interface JTTree<Tree extends JTTree<Tree>> {
  isActionChange(): boolean;
  isActionIgnore(): boolean;
  isActionNoChange(): boolean;
  setActionChange(): boolean;
  setActionIgnore(): boolean;
  setActionNoChange(): boolean;
  toString(): string;
}

interface JTCaseLabel<Tree extends JTCaseLabel<Tree>> extends JTTree<JTCaseLabel> {
}

interface JTExpression<Tree extends JTExpression<Tree>> extends JTCaseLabel<JTExpression> {
}

interface JTAnnotation extends JTExpression<JTAnnotation> {
  arguments: JTExpression<?>[];
  annotationType: JTTree<?>;
}

interface JTPackageDecl extends JTTree<JTPackageDecl> {
  annotations: JTAnnotation[];
  packageName: JTExpression<?>;
}

interface TransformOptionsPluginVisitor {
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
  ast: JTTree<?>;
}

declare const jaspiler = {
  createFieldAccess(...strings: string[]): object { },
  transformSync(path: string, options: TransformOptions): TransformResult { },
};

module.exports = {
  jaspiler: jaspiler,
}
