tinyspring
==========
###TinySpring is designed to be a set of tools for Spring-JPA projects.It began when I wished to write a practical generic DAO class for my spring-mvc project.
###For now TinySpring contains three parts:

* A generic DAO class with helper method to access database more easily.

* Some view models to help organize view objects.

* some utilities missing from Apache commons libraries (or I just don't know of, in this case please inform me ;] ).


# Usage

## Generic DAO

  \*\*\*Under Construction\*\*\*

## Utilities

### StopWatch

    private void yourMethod() {
        StopWatch stopWatch = new StopWatch("a stop watch").start();
        ...
        stopWatch.mark("phase1");
        ...
        stopWatch.mark(2);
        ...
        System.out.println(stopWatch.prettyPrint());   
    }
