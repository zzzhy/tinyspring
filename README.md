tinyspring
==========
###TinySpring is designed to be a set of tools for Spring-mvc-JPA projects. It began when I wished to write a practical generic DAO class for my spring-mvc project.

###For now TinySpring contains three parts:

* A generic DAO class with helper method to access database more easily.

* Some view models to help organize view objects.

* Some utilities missing from classic Commons or Guava libraries (or I just don't know of, in this case please inform me ;] ).


# Usage

## Generic DAO

  \*\*\*Under Construction\*\*\*

## Utilities

### StopWatch

I know Commons and Guava and Spring they all have StopWatch. I just tried to make this one writing less code to use and be able to mark multiple time in one instance.

    private void yourMethod() {
        StopWatch stopWatch = new StopWatch("a stop watch").start();
        //do something
        
        stopWatch.mark("phase 1");
        
        //do something
        
        stopWatch.mark("phase 2");
        
        //do something
        
        stopWatch.mark("phase 3");
        
        stopWatch.prettyPrint();   
    }

This will print in the console something like:
StopWatch[a stop watch]:    
-----> (30 ms) -----> phase 1   
-----> (42 ms) -----> phase 2  
-----> (20 ms) -----> phase 3  
Total time: 92 ms.  

