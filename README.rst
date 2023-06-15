Jaspiler
========

`Jaspiler <https://github.com/caoccao/Jaspiler/>`_ is a Java to Java transpiler. It transpiles the given Java code to the corresponding Java code with some customizations.

Major Features
==============

* Annotation based customizations
* AST based customizations
* JavaScript as the domain specific language (JS as DSL)
* Babel flavored plugins
* JDK 17 Support

Quick Start
===========

* Download the all-in-one jar file ``Jaspiler-${version}.jar`` from the latest `Action <https://github.com/caoccao/Jaspiler/actions>`_.
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

License
=======

`APACHE LICENSE, VERSION 2.0 <LICENSE>`_.
