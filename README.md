# [JsnPrsr: A Lean JSON Parser](http://el-tramo.be/jsnprsr)

## About

JsnPrsr is a fast, small, and lightweight 
[JSON](http://json.org) parser (and serializer), written in Java, with
no external dependencies.
 

## Usage

The entry point of the JsnPrsr library is the `JSON` class, which can be used to both
parse and serialize JSON `Value`s:

    Value value = JSON.parse("{\"foo\": \"bar\", \"baz\": [4, 5]}");

    System.out.println(value.getType());
    System.out.println(value.getObject().get("foo").getString());
    System.out.println(value.getObject().get("baz").getArray().get(0).getNumber());

    value.getObject().put("foo", Value.array(Value.string("a"), Value.string("b")));

    System.out.println(JSON.serialize(value));


## Modification tips

- If you want to track line numbers, edit `Parser.java` and comment out the lines 
  referencing `lineNumber`.

- If you want to support trailing commas in arrays, see the comment in `Parser.readArray()`
  and `Parser.readObject()`. This makes it support a more human writable form of JSON, 
  accepting for example:
  
        {
          "foo": 1,
          "bar": 2,
        }

    and

        [
          "foo",
          "bar",
        ]
    


