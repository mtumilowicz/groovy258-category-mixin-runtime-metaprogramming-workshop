# groovy258-category-mixin-runtime-metaprogramming-workshop

_Reference_: https://en.wikipedia.org/wiki/Metaclass

# runtime metaprogramming
* ExpandoMetaClass is a MetaClass that behaves like an Expando, allowing the addition or replacement of methods, 
properties and constructors on the fly.
    * Just as an ordinary class defines the behavior of certain objects, a metaclass defines the behavior of certain 
    classes and their instances
    * MetaClassImpl - Allows methods to be dynamically added to existing classes at runtime
    * A MetaClass within Groovy defines the behaviour of any given Groovy or Java class.
        * getMethods()
        * getProperty​(Class sender, Object receiver, String property, boolean isCallToSuper, boolean fromInsideClass)
        * invokeMethod​(Class sender, Object receiver, String methodName, Object[] arguments, boolean isCallToSuper, boolean fromInsideClass)
    ```
    * All method calls from Groovy code go through the meta class
    * The MetaClass interface defines two parts. The client API, which is defined via the extend MetaObjectProtocol 
  interface and the contract with the Groovy runtime system
    // Goal = to be able to simply write "1.m + 20.cm - 8.mm"
    Number.metaClass {
        getMm = { delegate          }
        getCm = { delegate *  10.mm }
        getM  = { delegate * 100.cm }
    }
    
    assert (1.m + 20.cm - 8.mm) == 1.192.m
    ```
* In Groovy we work with three kinds of objects: POJO, POGO and Groovy Interceptors
    * POJO - A regular Java object whose class can be written in Java or any other language for the JVM.
    * POGO - A Groovy object whose class is written in Groovy. It extends java.lang.Object and implements the 
    groovy.lang.GroovyObject interface by default. 
        * Interface GroovyObject
            * MetaClass - metaClass
            * Object - property
        ![alt text](img/GroovyInterceptions.png)
        ```
        Live Demo
        class Example {
           static void main(String[] args) {
              Student mst = new Student();
              mst.Name = "Joe";
              mst.ID = 1;
        		
              println(mst.Name);
              println(mst.ID);
              mst.AddMarks();
           } 
        }
         
        class Student implements GroovyInterceptable {
           protected dynamicProps = [:]  
            
           void setProperty(String pName, val) {
              dynamicProps[pName] = val
           } 
           
           def getProperty(String pName) {
              dynamicProps[pName]
           }
           
           def invokeMethod(String name, Object args) {
              return "called invokeMethod $name $args"
           }
        }
        ```
    * Groovy Interceptor - A Groovy object that implements the groovy.lang.GroovyInterceptable interface and 
    has method-interception capability which is discussed in the GroovyInterceptable section.
    * The groovy.lang.GroovyInterceptable interface is marker interface that extends GroovyObject and is used to notify 
    the Groovy runtime that all methods should be intercepted through the method dispatcher mechanism of the Groovy 
    runtime.

* For every method invocation from groovy code, Groovy will find the MetaClass for the given object and delegate the method resolution to the metaclass via MetaClass#invokeMethod which should not be confused with GroovyObject#invokeMethod which happens to be a method that the metaclass may eventually call
* Groovy comes with a special MetaClass the so-called ExpandoMetaClass. It is special in that it allows for dynamically adding or changing methods, constructors, properties and even static methods by using a neat closure syntax.

# mixins
# category
* There are situations where it is useful if a class not under control had additional methods. 
* In order to enable this capability, Groovy implements a feature called Categories.
```
class Distance {
    def number
    String toString() { "${number}m" }
}

@Category(Number)
class NumberCategory {
    Distance getMeters() {
        new Distance(number: this)
    }
}

use (NumberCategory)  {
    assert 42.meters.toString() == '42m'
}
```





* http://docs.groovy-lang.org/latest/html/api/groovy/lang/ExpandoMetaClass.html
    ```
     class CollegeStudent {
         static { mixin Student, Worker }
     }
    ```
* http://docs.groovy-lang.org/latest/html/api/groovy/lang/Category.html
* https://groovy-lang.org/metaprogramming.html
    * https://groovy-lang.org/metaprogramming.html#categories
    * https://groovy-lang.org/metaprogramming.html#xform-Category
    * http://docs.groovy-lang.org/next/html/documentation/#_differences_with_mixins
