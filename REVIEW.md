# Code Review for Stuga Case – Januari 2022

## Repository Hygiene
The README greets the visitor with a clean introduction of what is in the repository. The instructions to setup your local development environment are clear and concise. One thing to add here:  **since the project uses Maven, it would make more sense to write instructions how to run the application using Maven.** For example, you can start a Spring Boot application with `mvn spring-boot:run`

## Branches & Pull Requests
It's good to see you are using branches and pull requests to work on features. It is also good to see that there are no un-merged branches hanging around. You want to keep branches around as briefly as possible, or you might forget what they were about.

The number of closed branches is steadily growing and since they no longer serve a purpose, **it is good practice to clean up branches that are no longer being used**. GitHub (and other source management tools) offers a button for this once a PR has been merged. Or you can manually remove them from the `/branches/all` page on your repository on GitHub.

## Decision Records
We didn't spend a lot of time on the decision records, so it's nice to see you've created two new records on your own. They are concise and clear. There could have been a few more, if we had emphasized it more. A decision like using CockroachDB is an example that definitely should be in here.


## Code Readability
The overall look and feel of your project is very clean and organised. There are clear packages, containing at most 6 classes. Your classes are also concise and to the point. The methods I've seen are also kept short and you've managed to keep the responsibilities of each method limited. All this contributes to a clean layout of the project.

The only thing missing is the occasional comment to explain the function of a class, or the reasoning behind its origin. Since you've used descriptive names for classes and functions, they are mostly self-explanatory. However, future-you (or a colleague that comes after you) will be grateful for any kind hints to where they are in the application.

For example, a description for the `MessageLogica` class could be:

```
/**
 * The MessageLogica class is a utility class centralising the validation rules for Messages, used by the
 * web endpoints.
 * Since there is no state, all methods here will be made static.
 */
public class MessageLogica {
  // ...
}
```


## Testing
You can be proud of the fact that your tests cover approximately 96% of all of your production code. That's a number that is generally hard to achieve in a (large) project. I like how your tests include unit tests, integration tests and even tests on the web-layer. 

There is one thing I would like to hilight for you:

```
	@Test
	public void ChannelExistsTest() {
		assertTrue(mainRepository.channelExists("1"));
		assertFalse(mainRepository.channelExists("9"));
	}
```

It is not wrong to have multiple assertions in a test case, in some cases you actually want to assert a lot of things about the result of a certain test. However, this example actually shows two tests. You are testing both the positive and the negative flow through the `channelExists`-method. While also technically not wrong, it can lead to situation where you may be hiding information about a problem.
Suppose `channelExists` is a large and difficult method and the above test would fail both assertions. Any failing `assert*`-method will stop the test case immediately. So you would first only see the `assertTrue` fail. Perhaps you would then fix the path that causes this problem, only to find out that `assertFalse` is also failing. If both of these are in their own test-cases, then you would be able to see immediately that two paths in `channelExists` are failing, instead of just  one.

## Overall

I am very proud of the application you have built in the past two weeks. Starting from absolute zero, you have built a functional chat application with channels and administration. The code looks good, the application works. Great job everyone!