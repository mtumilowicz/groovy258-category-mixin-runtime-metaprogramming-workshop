# groovy258-category-mixin-runtime-metaprogramming-workshop

_Reference_: https://en.wikipedia.org/wiki/Metaclass  
_Reference_: http://docs.groovy-lang.org/latest/html/api/groovy/lang/Category.html  
_Reference_: https://groovy-lang.org/metaprogramming.html  
_Reference_: https://groovy-lang.org/metaprogramming.html#categories  
_Reference_: https://groovy-lang.org/metaprogramming.html#xform-Category  
_Reference_: http://docs.groovy-lang.org/next/html/documentation/#_differences_with_mixins  
_Reference_: http://docs.groovy-lang.org/latest/html/api/groovy/lang/ExpandoMetaClass.html

# preface
* goals of this workshop:
    * introducing the concept of runtime metaprogramming
    * introducing the concept of category (plus simple example)
    * introducing the concept of mixin (plus simple example)
* exercises are in `workshop` package, answers in `answers`
# runtime metaprogramming
* allows altering the class model and the behavior of a program at runtime
## internals
* for every method invocation from groovy code, Groovy will find the `MetaClass` for the given object 
and delegate the method resolution to the metaclass via `MetaClass#invokeMethod` which should not be confused 
with `GroovyObject#invokeMethod` which happens to be a method that the metaclass may eventually call
* `MetaClass`, `MetaClassImpl` defines the behaviour of any given Groovy or Java class
    * simple example
        ```
        // Goal = to be able to simply write "1.m + 20.cm - 8.mm"
        Number.metaClass {
            getMm = { delegate          }
            getCm = { delegate *  10.mm }
            getM  = { delegate * 100.cm }
        }
        
        assert (1.m + 20.cm - 8.mm) == 1.192.m
        ```
    * simple analogy: just as an ordinary class defines the behavior of certain objects, a metaclass defines 
    the behavior of certain classes and their instances
    * all method calls from Groovy code go through the meta class
        * `getMethods()`
        * `getProperty​(Class sender, Object receiver, String property, boolean isCallToSuper, boolean fromInsideClass)`
        * `invokeMethod​(Class sender, Object receiver, String methodName, Object[] arguments, boolean isCallToSuper, boolean fromInsideClass)`
    * The `MetaClass` interface defines two parts
        * client API, which is defined via the extend `MetaObjectProtocol` interface 
        * and the contract with the Groovy runtime system
* `ExpandoMetaClass` is a `MetaClass` that behaves like an `Expando` - it allows for dynamically adding or changing 
methods, constructors, properties and even static methods by using a neat closure syntax
* In Groovy we work with three kinds of objects
    * **POJO** - a regular Java object
    * **POGO** - a Groovy object whose class is written in Groovy
        * extends `java.lang.Object`
        * implements `groovy.lang.GroovyObject`
            * through `GroovyObject` we have access to `MetaClass`
            * field metaClass: `MetaClass`
            * field property: `Object`
    * **Groovy Interceptor** - a Groovy object
        * implements `groovy.lang.GroovyInterceptable`
            * is marker interface that extends `GroovyObject` and is used to notify the Groovy runtime that 
            all methods should be intercepted through the method dispatcher mechanism of the Groovy runtime
        * has method-interception capability
            ```
            Live Demo
            class Example {
               static void main(String[] args) {
                  X x = new X();
                  x.id = 1;
                  println x.id // 1
                  x.go(); // called invokeMethod go()
               } 
            }
             
            class X implements GroovyInterceptable {
               protected dynamicProps = [:]  
                
               void setProperty(String name, val) {
                  dynamicProps[name] = val
               } 
               
               def getProperty(String name) {
                  dynamicProps[name]
               }
               
               def invokeMethod(String name, Object args) {
                  return "called invokeMethod $name($args)"
               }
            }
        ```
## order of invocations
![alt text](img/GroovyInterceptions.png)
    
# mixins
* simple example
    ```
     class CollegeStudent {
         static { mixin Student, Worker } // note that it's better to use `Traits` than to hack multiple inheritance in that way
     }
    ```
* it is useful if a class not under control had additional methods
* in order to enable this capability, Groovy implements a feature called `mixin`
* let you add a `mixin` on any type at runtime
* the instances are not modified
    * if you `mixin` some class into another, there isn’t a third class generated
    * methods which respond to `A` will continue responding to `A` even if `mixed` in
# category
* simple example
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
* it is useful if a class not under control had additional methods 
* in order to enable this capability, Groovy implements a feature called `Categories`
* internals: 
    * during compilation, all methods are transformed to static ones with an additional self parameter 
    * properties invoked using `this` references are transformed so that they are instead invoked on the 
    additional self parameter and not on the Category instance
* `@Category` AST transformation simplifies the creation of Groovy categories
    * Historically, a Groovy category was written like this:
        ```
        class TripleCategory {
            public static Integer triple(Integer self) {
                3*self
            }
        }
        use (TripleCategory) {
            assert 9 == 3.triple()
        }
        ```
    * `@Category` transformation lets you write the same using an instance-style class, rather than a static class style
        ```
        @Category(Integer)
        class TripleCategory {
            public Integer triple() { 3*this }
        }
        use (TripleCategory) {
            assert 9 == 3.triple()
        }
        ```