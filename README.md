A large project could have more than a couple of hundred tasks and debug log could grow more than 40mb.
It is not an easy task for log viewer or even developer himself.
This tool chops huge gradle debug log into smaller pieces by tasks.

### Usage

```
./gradlew build --debug > hugelog.txt
gradle-log-chopper -o firewood/ hugelog.txt
```

or you could chop log via pipe:

```
./gradlew build --debug | gradle-log-chopper
```

Chopped files are prefixed with index and named by task name:
```
...
0361-:app-features:item-ui:generateDebugSources.txt
0362-:application:processGlobalDebugResources.txt
0363-:application:prepareLintJar.txt
0364-:application:generateGlobalDebugSources.txt
0365-:application:compileGlobalDebugNdk.txt
0366-:application:processGlobalDebugJavaRes.txt
0367-:app-model:kaptGenerateStubsDebugKotlin.txt
...
```

### License

```
Copyright 2018 Andrius Semionovas (neworldLT)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
