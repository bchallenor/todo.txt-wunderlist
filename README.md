todo.txt-wunderlist
===================

Exports [Wunderlist](http://www.wunderlist.com) tasks to [todo.txt](http://todotxt.com).
Current tasks are written to `todo.txt`, and completed tasks written to `done.txt`.

Handles:
* task titles
* list titles (maps to your choice of priority/project/context as per [Config.scala](src/main/scala/Config.scala))
* creation date
* completion date
* starred state (as `+starred`)
* due date (as `due:2013-01-01`)
* reminder date (as `t:2013-01-01`)
* note (as `note:[line 1; line 2]`)
* subtasks (as `sub:[task 1; task 2]`)

Tested on the Wunderlist Windows client (2.3.0.29), but it will probably work for other clients too if you can find `Wunderlist.dat`.


Instructions
------------

0. Install [SBT](http://www.scala-sbt.org)
1. Edit [Config.scala](src/main/scala/Config.scala):
    * `datFilePath`: The location of `Wunderlist.dat`. This should just work if you're on Windows.
    * `todoDirPath`: The location of your `$TODO_DIR`. Note that `todo.txt` and `done.txt` will be *overwritten*.
    * `getListTags(String)`: Add a case for each of your Wunderlist lists, specifying how to tag it.
2. `sbt run`


License
-------

    Copyright (C) 2013 Ben Challenor <ben@challenor.org>

    Permission is hereby granted, free of charge, to any person obtaining a copy of
    this software and associated documentation files (the "Software"), to deal in
    the Software without restriction, including without limitation the rights to
    use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
    the Software, and to permit persons to whom the Software is furnished to do so,
    subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
    FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
    COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
    IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
    CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
