Jaspiler
========

`Jaspiler <https://github.com/caoccao/Jaspiler/>`_ is a Java to Java transpiler. It transpiles the given Java code to the corresponding Java code with some customizations. It is expected to be a Babel for Java. If you like this project, please star it.

Major Features
==============

* Annotation based customizations
* AST based customizations
* JavaScript as the domain specific language (JS as DSL)
* Babel flavored plugins
* JDK 17 Support

Quick Start
===========

* Download the all-in-one jar file ``Jaspiler-${version}.jar`` from the latest `Actions <https://github.com/caoccao/Jaspiler/actions>`_.
* Create a JavaScript file ``test.js`` as follows:

.. code-block:: javascript

    const result = jaspiler.transformSync(
      `package com.test;
      public class A {
      }
      `,
      { 
        plugins: [{
          visitor: {
            Class(node) {
              node.simpleName = jaspiler.createName('B');
            },
          },
        }],
        sourceType: 'string',
      });
    console.info(result.code);

* Execute the command as follows:

.. code-block:: shell

    java -cp Jaspiler-${version}.jar test.js

* The output is as follows:

.. code-block:: java

    package com.test;

    public class B {
    }

Documents
=========

* Supported feature list is in this `typescript file <blob/main/scripts/node/jaspiler/index.d.ts>`_.
* More documents are on the way...

Q & A
=====

Why do I Need a Java to Java Transpiler?
----------------------------------------

* I want to code with JDK 17 but support JDK 8/11. Jaspiler can help transform the code from JDK 17 to JDK 8/11.
* I want to remove some proprietary cloud features at source code level before deploying the binaries to an on-premise environment to prevent the reverse engineering. Jaspiler can help add/update/remove any part of the code at the AST level.
* I want to translate the Java code to JavaScript/Python/Go/.... Jaspiler can provide the complete AST and allow all kinds of translation.
* I want to obfuscate the code. Jaspiler can help perform all kinds of code obfuscation at the AST level.

What's the Status of Jaspiler?
------------------------------

Jaspiler is at its early stage with limited feature set. You are welcome taking a try. Jaspiler is expected to be a community driven open-source project, so if you want a feature, either you raise an issue, wait for the community to prioritize it, or you submit a pull request.

License
=======

`APACHE LICENSE, VERSION 2.0 <LICENSE>`_.
