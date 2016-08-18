Kalah Game
---
It's an API based implementation of [Kalah Game](https://en.wikipedia.org/wiki/Kalah).

The game
---
Each of the two players has six pits in front of him/her. To the right of the six pits, each player
has a larger pit, his Kalah or house. At the start of the game, six stones are put In each pit.
The player who begins picks up all the stones in any of their own pits, and sows the stones on to
the right, one in each of the following pits, including his own Kalah. No stones are put in the
opponent's' Kalah. If the players last stone lands in his own Kalah, he gets another turn. This can
be repeated any number of times before it's the other player's turn.
when the last stone lands in an own empty pit, the player captures this stone and all stones in
the opposite pit (the other players' pit) and puts them in his own Kalah.
The game is over as soon as one of the sides run out of stones. The player who still has stones
in his/her pits keeps them and puts them in his/hers Kalah. The winner of the game is the player
who has the most stones in his Kalah.

Technologies
---

   - [Java - 8](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)
   - [Maven - 3.3.9](https://maven.apache.org/download.cgi)
   - [Spring Boot - 1.4.0](http://projects.spring.io/spring-boot/)
   - [Project Lombok](https://projectlombok.org/)
   - [Embedded Tomcat](http://tomcat.apache.org/)
   - [JaCoCo](http://www.eclemma.org/jacoco/)
  
Adding Project Lombok Agent
---

This project uses [Project Lombok](http://projectlombok.org/features/index.html)
to generate getters and setters etc. Compiling from the command line this
shouldn't cause any problems, but in an IDE you need to add an agent
to the JVM. Full instructions can be found in the Lombok website. The
sign that you need to do this is a lot of compiler errors to do with
missing methods and fields.    
 
Build
---
This application will generate an executable jar file, to build and run **ensure you have Java 8 and Maven 3 installed** 
and execute the following command on the terminal:

```
$ mvn clean package
```

>**Note**: *Make sure you're on project root dir*.

Code Coverage
---
This project uses [JaCoCo](http://www.eclemma.org/jacoco/) for code coverage. To check it's result execute:

```
$ mvn clean package
```

When the tests finishes the result you'll see a message like this:
```
[INFO] Kalah Game ......................................... SUCCESS [  1.040 s]
[INFO] api ................................................ SUCCESS [ 10.061 s]
[INFO] web ................................................ SUCCESS [  0.612 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

The JaCoCo result will be available on dirs: `$module-name\target\site\jacoco\index.html`.

>**Note**: change the `module-name` to each module name in this project to find out the JaCoCo result.

API Docs
---
The backend service have a built-in documentation that can be accessed by the uri [/docs/api.html](http://localhost:9090/docs/api.html).  

Run
---
The applications are built with `spring-boot` that generates an executable jar with an embedded Tomcat.  
To run the applications just execute the following commands:  

```
$ java -jar api/target/api.jar
```

If everything goes ok you must see a message just like the following:

```
2016-08-17 17:32:07.406  INFO 80518 --- [           main] com.marcosbarbero.kalah.ApiApplication   : Started ApiApplication in 3.879 seconds (JVM running for 4.306)
```

>Note: The API will be available on `9090` port.  

After the API is up and running it is time to run the web layer executing the following command:
```
$ java -jar web/target/web.jar
```

If everything goes ok you must see a message just like the following:
```
2016-08-17 21:34:41.300  INFO 7299 --- [           main] com.marcosbarbero.kalah.WebApplication   : Started WebApplication in 3.846 seconds (JVM running for 4.251)
```

>Note: The web layer will be available on `8080` port.

>**Note**: The commands above must be executed in the root of this project and make sure to execute the commands above after the *build*.

Playing the game
---
To play the game just reach the [http://localhost:8080](http://localhost:8080) choose the number of stones to play
 and enjoy it!