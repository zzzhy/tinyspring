tinyspring
==========
###TinySpring is designed to be a set of tools for Spring-mvc-JPA projects. It began when I wished to write a practical generic DAO class for my spring-mvc project.

###For now TinySpring contains three parts:

* A generic DAO class with helper method to access database more easily.

* Some view models to help organize view objects.

* Some utilities missing from classic Commons or Guava libraries (or I just don't know of, in this case please inform me ;] ).


# Usage

## Generic DAO

Assuming you have an Entity called `User` and you wish a DAO class to access `User`'s data from database.
You can simply create a `UserDao` class which extends `AbstractDao<User>` and implements the abastract method `getEntityClass()` in which returns the entity class we'd like to access:

    @Repository
    @Transactional
    public class UserDao extends AbstractDao<User> {
    
      	@Override
      	protected Class<User> getEntityClass() {
    
    		return User.class;
    	}
    }
    
Now we have our first DAO. It has already some out of box methods inherited from AbstractDao like `persist() merge() remove()
findById() findAll() totalCount()` etc.

If you wish to write customized queries, you can use `beginQuery()` to begin a method chain to help you make a JPA query more easily. 
Here's some examples:

If you wish to query from User with username 'bob':

    import static org.trii.tinyspring.dao.TinyPredicate.equal;
    
    User result = userDao.beginQuery().select()
                       .where(equal("username", "bob"))
                       .getFirstResult();

If you wish to authenticate user 'bob' with password '123456':

    boolean authSucess = userDao.beginQuery().select()
                              .where(equal("username","bob"))
                              .and(equal("password","123456"))
                              .hasResult();
                              
      boolean authSucess = userDao.beginQuery().select()
                              .where(and(equal("username", "bob"), equal("password", "123456")))
                              .hasResult();
                              
        boolean authSucess = userDao.beginQuery().select()
                              .where(equal("username", "bob").and(equal("password", "123456")))
                              .hasResult();
                              
All three syntax are valid.


If your query is very complex or you are JPQL export and you wish to write your JPQL directly:

    List<User> resultList = userDao.begeinQuery()
                                   .query("SELECT m FROM User m WHERE m.signInTime BETWEEN :a AND :b")
                                   .param("a", new Date("2014-07-01"))
                                   .param("b", new Date("2014-07-31"))
                                   .getResultList();
                                   

More demonstrations are coming.

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

