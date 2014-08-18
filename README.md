tinyspring
==========
###TinySpring is designed to be a set of tools for Spring-JPA projects.It began when I wished to write a practical generic DAO class for my spring-mvc project.
###For now TinySpring contains three parts:

* A generic DAO class with helper method to access database more easily.

* Some view models to help organize view objects.

* some utilities missing from Apache commons libraries (or I just don't know of, in this case please inform me ;] ).


# Usage

## Generic DAO

Assuming you have an Entity called `User` and you wish a DAO class to access `User`'s data from database.
Simply creates a `UserDao` which extends `AbstractDao<User>` and implements the abastract method `getEntityClass()` in which returns the entity class we'd like to access:

    @Repository
    @Transactional
    public class UserDao extends AbstractDao<User> {
    
    	@Override
    	protected Class<User> getEntityClass() {
    
    		return User.class;
    	}
    
    }
    
Now we have our first DAO. It has already some basic method inherited from AbstractDao like `persist() merge() remove()
findById() findAll() totalCount()`.

If you wish to make a customized query, you can use beginQuery() to begin a tiny query, a method chain to help you
to make a JPA query more easily. 
Here's some examples:

if you wish to query from User with username 'bob'

    User result = userDao.beginQuery().select()
                       .where().equals("username", "bob")
                       .getFirstResult();

if you wish to authenticate user 'bob' with password '123456'

    boolean authSucess = userDao.beginQuery().select()
                              .where().equals("username","bob")
                              .and().equals("password","123456")
                              .hasResult();
                              
if your query is very complex or you are JPQL export and you wish to write JPQL directly

    List<User> resultList = userDao.begeinQuery().query("SELECT m FROM User m WHERE m.signInTime BETWEEN :a AND :b")
                                 .param("a", new Date("2014-07-01"))
                                 .param("b", new Date("2014-07-31"))
                                 .getResultList();
                                 
                                 
                                 


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
