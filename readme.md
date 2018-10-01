In order to reproduce sbt classpath issue change `sbtVersion` to `1.1.5` in project/build.properties. 
Using version `1.1.4` and having Postgres installed should give you something along the lines of:

```
Oct 01, 2018 11:20:29 AM org.flywaydb.core.internal.util.VersionPrinter printVersion
INFO: Flyway Community Edition 5.1.4 by Boxfuse
Oct 01, 2018 11:20:29 AM org.flywaydb.core.internal.database.DatabaseFactory createDatabase
INFO: Database: jdbc:postgresql:postgres (PostgreSQL 9.6)
Oct 01, 2018 11:20:30 AM org.flywaydb.core.internal.util.scanner.classpath.ClassPathScanner findResourceNames
WARNING: Unable to resolve location classpath:db/migration
Oct 01, 2018 11:20:30 AM org.flywaydb.core.internal.util.scanner.classpath.ClassPathScanner findResourceNames
WARNING: Unable to resolve location classpath:db/migration
Oct 01, 2018 11:20:30 AM org.flywaydb.core.internal.util.scanner.classpath.ClassPathScanner findResourceNames
WARNING: Unable to resolve location classpath:db/migration
Oct 01, 2018 11:20:30 AM org.flywaydb.core.internal.util.scanner.classpath.ClassPathScanner findResourceNames
WARNING: Unable to resolve location classpath:db/migration
Oct 01, 2018 11:20:30 AM org.flywaydb.core.internal.command.DbValidate validate
INFO: Successfully validated 0 migrations (execution time 00:00.006s)
Oct 01, 2018 11:20:30 AM org.flywaydb.core.internal.schemahistory.JdbcTableSchemaHistory create
INFO: Creating Schema History table: "public"."flyway_schema_history"
Oct 01, 2018 11:20:30 AM org.flywaydb.core.internal.command.DbMigrate migrateGroup
INFO: Current version of schema "public": << Empty Schema >>
Oct 01, 2018 11:20:30 AM org.flywaydb.core.internal.command.DbMigrate logSummary
INFO: Schema "public" is up to date. No migration necessary.
```

Otherwise you will get an connection error, which is fine. However, changing the `sbtVersion` version to `1.1.5+` you will see the following:

```
[info] Running Main
java.lang.ExceptionInInitializerError
	at org.flywaydb.core.internal.util.FeatureDetector.isSlf4jAvailable(FeatureDetector.java:96)
	at org.flywaydb.core.api.logging.LogFactory.getLog(LogFactory.java:75)
	at org.flywaydb.core.internal.util.FeatureDetector.<clinit>(FeatureDetector.java:25)
	at org.flywaydb.core.api.logging.LogFactory.getLog(LogFactory.java:72)
	at org.flywaydb.core.Flyway.<clinit>(Flyway.java:79)
	at Main$.$anonfun$migrate$1(Main.scala:13)
	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:12)
	at cats.effect.internals.IORunLoop$.cats$effect$internals$IORunLoop$$loop(IORunLoop.scala:85)
	at cats.effect.internals.IORunLoop$RestartCallback.signal(IORunLoop.scala:336)
	at cats.effect.internals.IORunLoop$RestartCallback.apply(IORunLoop.scala:357)
	at cats.effect.internals.IORunLoop$RestartCallback.apply(IORunLoop.scala:303)
	at cats.effect.internals.IOShift$Tick.run(IOShift.scala:36)
	at java.util.concurrent.ForkJoinTask$RunnableExecuteAction.exec(ForkJoinTask.java:1402)
	at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
	at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
	at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
	at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
Caused by: org.flywaydb.core.api.FlywayException: Unable to instantiate class org.flywaydb.core.internal.util.logging.slf4j.Slf4jLogCreator : org.flywaydb.core.internal.util.logging.slf4j.Slf4jLogCreator
	at org.flywaydb.core.internal.util.ClassUtils.instantiate(ClassUtils.java:61)
	at org.flywaydb.core.api.logging.LogFactory.getLog(LogFactory.java:76)
	at org.flywaydb.core.internal.util.ClassUtils.<clinit>(ClassUtils.java:37)
	... 17 more
Caused by: java.lang.ClassNotFoundException: org.flywaydb.core.internal.util.logging.slf4j.Slf4jLogCreator
	at java.net.URLClassLoader.findClass(URLClassLoader.java:381)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
	at java.lang.Class.forName0(Native Method)
	at java.lang.Class.forName(Class.java:348)
	at org.flywaydb.core.internal.util.ClassUtils.instantiate(ClassUtils.java:59)
	... 19 more
```

Furthermore, setting `fork := true` in `build.sbt` using `sbtVersion` `1.1.4+` works with respect to classpath, e.g.:

```
[info] Running (fork) Main
[error] Oct 01, 2018 11:32:05 AM org.flywaydb.core.internal.util.VersionPrinter printVersion
[error] INFO: Flyway Community Edition 5.1.4 by Boxfuse
[error] Oct 01, 2018 11:32:05 AM org.flywaydb.core.internal.database.DatabaseFactory createDatabase
[error] INFO: Database: jdbc:postgresql:postgres (PostgreSQL 9.6)
[error] Oct 01, 2018 11:32:05 AM org.flywaydb.core.internal.util.scanner.classpath.ClassPathScanner findResourceNames
[error] WARNING: Unable to resolve location classpath:db/migration
[error] Oct 01, 2018 11:32:05 AM org.flywaydb.core.internal.util.scanner.classpath.ClassPathScanner findResourceNames
[error] WARNING: Unable to resolve location classpath:db/migration
[error] Oct 01, 2018 11:32:05 AM org.flywaydb.core.internal.util.scanner.classpath.ClassPathScanner findResourceNames
[error] WARNING: Unable to resolve location classpath:db/migration
[error] Oct 01, 2018 11:32:05 AM org.flywaydb.core.internal.util.scanner.classpath.ClassPathScanner findResourceNames
[error] WARNING: Unable to resolve location classpath:db/migration
[error] Oct 01, 2018 11:32:05 AM org.flywaydb.core.internal.command.DbValidate validate
[error] INFO: Successfully validated 0 migrations (execution time 00:00.059s)
[error] Oct 01, 2018 11:32:05 AM org.flywaydb.core.internal.command.DbMigrate migrateGroup
[error] INFO: Current version of schema "public": << Empty Schema >>
[error] Oct 01, 2018 11:32:05 AM org.flywaydb.core.internal.command.DbMigrate logSummary
[error] INFO: Schema "public" is up to date. No migration necessary.
[success] Total time: 2 s, completed Oct 1, 2018 11:32:05 AM
```