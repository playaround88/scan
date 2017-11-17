scan 
--- 
A data scan tool lib(一个数据扫描的工具包)

Version
---
1.0.0

Relative
---
Same project with similar aims in my site, will deprecated, and discourage to use.  
[scanner](https://github.com/playaround88/scanner)  
[scanner-redis](https://github.com/playaround88/scanner-redis)  
[etl-lib](https://github.com/playaround88/etl-lib)  

Features
---
1. with queue pattern, more less resource use, and more easy to control.
2. split some module, easy to use with maven.
3. two queue type, JAVA raw BlockingQueue and redis list.
4. some common use implements, e.g. Dir file and FTP file.
5. open for extension. user can easy implement themself's need.

How to use
---
Project is not registe in maven repository. So if you want to use in your project, you need install in you local repository.

```
git clone https://github.com/playaround88/scan  
cd scan  
mvn clean install -Dmaven.test.skip=true -s ${Your maven settings file}  
```

And in your project, just easily copy below dependency.  
IF you just use JAVA raw BlockingQueue
```xml
<dependency>
	<groupId>com.ai</groupId>
	<artifactId>scan-core</artifactId>
	<version>${release.version}</version>
</dependency>
```
_*All other module depend on scan-core module._

IF you want use redis as Queue
```xml
<dependency>
	<groupId>com.ai</groupId>
	<artifactId>scan-redis</artifactId>
	<version>${release.version}</version>
</dependency>
```

If you want use directory file scan feature
```xml
<dependency>
	<groupId>com.ai</groupId>
	<artifactId>scan-file</artifactId>
	<version>${release.version}</version>
</dependency>
```

If you want use directory file scan feature, and use redis as queue.
```xml
<dependency>
	<groupId>com.ai</groupId>
	<artifactId>scan-redis</artifactId>
	<version>${release.version}</version>
</dependency>
<dependency>
	<groupId>com.ai</groupId>
	<artifactId>scan-file</artifactId>
	<version>${release.version}</version>
</dependency>
```

*FTP scan is like directory file scan.
