## Implementing the `charAt` method
Implement `charAt` which returns the character at a specific index. This method requires new instance variables *to meet performance characteristics.*

### Details on Implementing the charAt method
This method returns the character at the specified index if that's a valid index, and throws an `IndexOutOfBoundsException` otherwise. A naive implementation of this method would start at the beginning of the linked list, the node referenced by `myFirst` and count characters until the index-th character is found. 

For full credit (and to pass the timing tests in `TestIterator`) you'll need to maintain state so that after a call of `charAt(k)` the call of `charAt(k+1)` is an $O(1)$ operation. This will make the loop below $O(N)$ for an `N` character strand.

### Basic, Correct but Inefficient Implementation of charAt
First we'll show an inefficient implementation of the charAt method --- a method to find a character at a specific index in a linked list of strings. Your code will need to traverse the linked list counting characters. The code below illustrates how to do this. It doesn't check to see if parameter index is valid, but it passes the JUnit tests for correctness.

```java
public int charAt(int index) {
     int count = 0;
	int dex = 0;
	Node list = myFirst;
	while (count != index) {
		count++;
		dex++;
		if (dex >= list.info.length()) {
			dex = 0;
			list = list.next;
		}
	}
     return list.info.charAt(dex);
}
```

This code will pass correctness tests, e.g., in `TestIterator`. However, it's not efficient since it starts at the beginning of the linked list for each call. You should be sure you understand how local variables `count` and `dex` are used in the code above before trying to make the code more efficient for a sequence of calls as explained in the next section.

### Efficient Implementation of charAt
You should create instance variables in the class `LinkStrand` so that after a call of `charAt(k)`, calling `charAt(k+1)` is an `O(1)` operation. 

To do this, you should appropriately update the following  instance variables: one for the current node in a sequence of calls of charAt, one for the current index into that node, and one for the overall count; these are explained below. 

- `myIndex` is the value of the parameter in the last call to `charAt`. This means that if a call to `s.charAt(100)` is followed by `s.charAt(101)` the value of `myIndex` will be 100 after `s.charAt(100)` executes and 101 after `s.charAt(101)` executes.
- `myLocalIndex` is the value of the index within the string stored in the node last-referenced by `charAt` when the method finishes. For example, suppose a strand consists of three nodes: the first has 60 characters; followed by a node of 30 characters; followed by a node of 40 characters. The call `s.charAt(40)` will mean that `myIndex` is 40 and `myLocalIndex` is also 40 since that's the index within the first node of the list, where the character whose index is 40 is found.  Suppose this is followed by `s.charAt(70).` The character at index 60 of the entire strand will be the character with index zero of the second node -- since the first node holds characters with indexes 0-59 since its info field is a string of 60 characters. The character at index 70 of the entire strand will be the character with index 10 of the second node. This means that after the call `charAt(70)` the value of `myIndex` will be 70, the value of `myLocalIndex` will be 10, and the value of `myCurrent` (see just below) is a pointer to the second node of a three-node list.

<div align="center">
  <img src="figures/charAt.png">
</div>

- `myCurrent` is the node of the internal list referenced in the last call to `charAt`. In the example above the value of `myCurrent` would be the first node after the call `s.charAt(40)`, would be the second node after the call `s.charAt(70)` or `s.charAt(89)`, and would be the third node after the call `s.charAt(90)` since the first two nodes only contain a total of 90 characters, with indexes 0 to 89.

In the `TestIterator` code you get with this assignment, there are correctness tests and performance tests for going forward in `O(N)` time as described here.

##### Why do we need charAt to be efficient?
If the `charAt` method is not efficient, the loop below will be `O(N^2)` since the `charAt` method will be `O(k)` to access the kth character.

```java
LinkStrand dna = new LinkStrand(".....");
StringBuilder s = new StringBuilder("");
for(int k=0; k < dna.size(); k++) {
    s.append(dna.charAt(k));
}
```

This `charAt` method is called by the code in the `CharDnaIterator` class. So iterating over an `IDnaStrand` object will ultimately use the `charAt` method as shown in the code below. 

```java
LinkStrand dna = new LinkStrand(".....");
Iterator<Character> iter= dna.iterator();
for(char ch : iter) {
    System.out.print(ch);
}
System.out.println();
```

The `Iterator` object in the code above is constructed as a result of calling the default `IDnaStrand.iterator` method, the body is shown here: 

```java
return new CharDnaIterator(this);
```

Note that the `IDnaStrand` object referenced by this is then stored in the `CharDnaIterator` object being created. 

You only need to implement `charAt`, then all the code described and shown above will work correctly! You will need to initialize the instance variables too.

##### Order of Calls Matters
However, *you'll need to write code to deal with calls that aren't "in order".* If the call `.charAt(100)` is followed by the call `.charAt(30)` you'll need to start at the beginning of the internal linked list to find the character with index 30. If `.charAt(100)` is followed by `.charAt(350)` you won't start at the first node, but continue with the values stored in the instance variables.
