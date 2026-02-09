# Flamewing Template Engine

## What is Flamewing

Flamewing aims to be a modern template engine that will facilitate development of complex web application by providing an ease to use and easy to understand syntax for developing the HTML pages that will be rendered by the backend. Flamewing uses Server-Side-Rendering (SSR) to speed webpage load times and to provide close integration between the front-end and the back-end of a web application.

Flamewing provides many advantages compared to other template engines that are currently available for Spring/Spring Boot and introduces new features that are not currently available elsewhere.

- Only one special character `@` which separates HTML code from the programmable part of the template
- The ability to use Java in the template. No need to learn a new syntax, language or utilities. You can use the language you already know and love
- Fragments and component-based development. Create reusable components which you can reference between templates
- Reverse routing for Spring. You can change the URL of a web-page without the need to modify your template files. Flamewing will compute the URLs at runtime based on existing controllers
- Lighting fast and lightweight. Flamewing is orders of magnitude faster than Thymeleaf for rendering pages. This is achieved by compiling the template files, instead of interpreting them. See the benchmarks below.
- **More features in development**

For more deatails, examples and documentation, visit the [Flamewing website](https://pazvanti.github.io/Flamewing/index.html)

## Sample template

Here is a simple template that showcases a few key features of Flamewing.

```html
@import java.util.List;

@args(int a, int b)
<head>
    <title>Test page</title>
    <link href="@asset.path("\css\mycss.css")" rel="stylesheet" />
</head>
<body>
    @users.fragment()
    @if(a == 0) {
    <h6>@b</h6>
    }

    <a href="@routes.TestController.renderTestPage()">Test</a>

    @if(b == 0) {<h6>AAAA</h6>}

    @for (int count = 0; count < 10; count++) {
    <span>@count</span>
    }
</body>
```

## Flamewing elements

Flamewing comes with an easy-to-use syntax that allows you to write Java code directly in the HTML template file. The special `@` character indicates non-HTML code that will be transformed into Java code and compiled. Still, there are a few limitations (at least in its current state).

### Arguments (constructor)

Each template must have a constructor. This is done using the `@args()` element. Additionally, you can provide input parameters for your template and you can use these input parameters in the code

Example: `@args(String title, int numDivs)`

### Imports
Using the `@import` keyword you can use any class from Java or from your application. This will allow you to use that class inside your template.

Example: `@import java.util.List;`

### Variables

You can reference variables anywhere in the template using the `@` character followed by the variable name. Once the template is rendered, the String representation will be shown in the HTML code.

Example: `<h6>@title</h6>`

### Eval `@()`

If you want to evaluate an operation, you can use the `@()` operation:

```html
<p>
    Operation: @a + @b = @(a + b)
</p>
```

The eval operator can be used to avoid string concatenation when building complex strings by evalueting only one variable:

```html
<a href="@(pageName).html">URL to the page</a>
```

### For-loops

Just like in Java, you can have for-loops. Again, the `@` character indicates the start of the code segment.

Example: 
```html
@for (int count = 0; count < 10; count++) {
<span>@count</span>
}
```

Flamewing also supports `for-each` loops:
```html
@for (String item:items) {
<span>@item</span>
}
```

As well as `for with iterator`

```html
@for((itt, item):myList) {
    <span>@itt.index() : @item</span>
}
```

### If statements

You can define conditional blocks using `@if` statements. It also support `else` and `else if` branches.

Example:

```html
@if(divCount == 0) {
    <h6>You must show at least one div</h6>
} else {
    <h6>We have some divs</h6>
}
```

### CSRF 
#### CSRF Input

Flamewing requires Spring Security and allows generating of CSRF input fields for forms. This is done using the `@input.csrf()` helper.

Example:

```html
<form action="@routes.TestController.doPost()" method="post">
    @csrf.input()
    <input type="text" placeholder="Your name" name="name">
    <button type="submit">Submit</button>
</form>
```

#### CSRF Meta tags

If you need the CSRF token to be present outside of a form (for use with rest-client) you can generate it as a `<meta>` tag using the `@meta.csrf()` utility.
This will generate two meta tags, one containing the CSRF token and another one containing the header name where Spring Security will read the received token for verification:

Example:

| Flamewing 	      | Generated HTML 	                                                                                 | 
|----------------|--------------------------------------------------------------------------------------------------|
| `@csrf.meta()` | `<meta name="_csrf" content="token">`<br/> `<meta name="_csrf_header" content="X-CSRF-TOKEN">` |


If you only need to include the token and not the header name, you can do so by providing an optional boolean parameter:
`
@csrf.meta(false)
`

### Routes and reverse-routing

Flamewing allows you to build links and URLs to other pages of your application with ease by implementing reverse routing. The template engine determines the controllers that are present in your applciationg and allows you to use them in the template files. In case the path changes, Flamewing will calculate the new path without the need to change the template files, as long as the controller stays the same. And yes, dynamic URLs with `@PathVariable` and `@RequestParam` is possible. All routes start with `@routes.` and are followed by the controller file name and the method name. If the URL has a dynamic part, you can provide the variables that will compute that part as input parameters.

Example simple route: ```<a href="@routes.TestController.renderJson()">Go to JSON page</a>```

Example page with `@PathVariable` or `@RequestParam`: `<a href="@routes.TestController.renderTest(0, 3)">Go to this page</a>`

### Escaping the `@`

You may need to use the `@` character as an actual character (for example, for emails). That is why you will need to escape it. This is done using, you guessed it, the `@` character.

Example: `<a href="mailto:test@@domain.com">Email me</a>`

### Defining variables using `@with`

If you want to use a value with ease, without the need to retrieve it over and over again, you can retrieve it using the `@with` block. 

If the `@with` block has nested content, the variable's lifecycle will be only for the specified nested content.

```html
@* values is a list of strings *@
@with (s = values.get(0)) {
    <div>Value is @s</div>
}
```

### Content blocks

You can define reusable content blocks in the same template. The blocks can hold multiple lines of HTML and can even use variables. Later, you can use content blocks just like any other variable or even pass them to fragments

```html
@myVar => {
    <p>This is my var</p>
    <a href="@a">It holds a URL</a>
}

<hr />
@myVar
```

Passing it to a fragment. More information on using fragments further down.

```html
@fragments.myFragment.template(myVar)
```

### Break

Flamewing provides a `@break` element which can be used to exit a loop

### Null-safety operators

Flamewing supports two null-safety operators:

The null-safety ternary operator can be used to asses if a variable or method call returns `null` and provide an alternative
```html
@myVar?:"Alternative"
@myVar.myMethod()?:"Alternative"
```

If you don't want to provide any alternative, and just to protect from `NullPointerException` and don't write any text, use the `@?` attribute.

```html
@?myVar
```

### Fragments

Flamewing allows the creation of reusable components. This is done by having one file for each component that has the same rules as any other template file. After that, you can reference it inside other templates.

For example, inside my `views` folder I have a directory named `menu` where I have a template for the administrator's menu. From my main template, I can reference it like this:
```html
@menu.admin.template()
```

Now, when my main template is called, it will automatically render the HTML code provided by that fragment. Additionally, I can have input parameters, just like I have for other templates.

Fragments can have arguments which can be passed to the `template()` method call. 

If a fragment has an argument of type `FlamewingContent`, it can be provided as a nested element:

```html
@fragments.withContent.template(a) {
    <p>This is my text</p>
    <a href="@(a).html">It holds another URL</a>
}
```

Since content blocks are also of type `FlamewingContent`, they can be passed as arguments to templates as well. In case a template has multiple arguments of type `FlamewingContent`, the last one can be ommited and provided as a nested element

```html
@myVar => {
    <p>This is my var</p>
    <a href="@a">It holds a URL</a>
}

@fragments.withContent.template(myVar) {
    <p>This is my text</p>
    <a href="@(a).html">It holds another URL</a>
}
```

## The `FlamewingController`, the `Result` and the `View`

Each controller in your application that wants to use Flamewing must extend the `FlamewingController` class. This will allow the engine to properly generate reverse-routes for the controllers. Additionally, it will allow you to easily return either an HTML page or a JSON object. This is because all Flamewing controllers must return a `Result`. Don't worry, `FlamewingController` provides helper methods for this via the `ok()`, `notFound()` and `withHttpStatus()` methods.

To return an HTML page, you must call the `View.of()` method and provide template name (without `.java.html`) and it's input parameters.

Below is an example controller:

```java
@Controller
public class TestController extends FlamewingController {

    @GetMapping("/test.html")
    public Result renderTest(@RequestParam(name = "a", defaultValue = "0") int a,
                             @RequestParam(name = "b", defaultValue = "0") int b) {
        return ok(View.of("test", a, b));
    }

    @GetMapping("/test/{id}/test.html")
    public Result renderTestWithPathParam(@PathVariable(name = "id") String val) {
        return ok(View.of("test", 1, 1));
    }

    @GetMapping("/json")
    public Result renderJson() {
        return ok(Arrays.asList("Test", "test2"));
    }
}
```

## Configuration options

All configuration options are under the `flamewing` tag.

```yaml
flamewing:
    controllersPackage: "com.example.demo.controllers"
    viewsPath: classpath*:views
    errorPages:
      code404: page404 # View name for 404 page, optional
      code500: page500 # View name for 500 page, optional
    fragmentRetrieveEnabled: false
```

The path for the templates can be given either as classpath (like in the example) or as absolute path from disk.

If you want to enable custom error pages (non-whitelabel ones that are active in Spring), the build-in ones need to be disabled:
```yaml
spring:
    mvc:
        throwExceptionIfNoHandlerFound: true
```

## Benchmarks

The following benchmarks were run on my machine for an HTML page that renders 50_000 div elements. Your results may vary, depending on hardware configuration. The templates used are:

Flamewing:
```html
@constructor(String title, int divCount)

<!DOCTYPE html>
<html>
    <head>
        <title>@title</title>
    </head>
    <body>

        <h1>Flamewing Template</h1>
        <p>This is a blank template for a web page.</p>
        @for(int i = 0; i<divCount; i++) {
            <div class="benchmark"> Rendering a div (num <span>@i</span>)</div>
        }
    </body>
</html>
```

Thymeleaf:
```html
<!DOCTYPE html>
<html>
    <head>
        <title th:text="${title}"></title>
    </head>
    <body>

        <h1>Thymeleaf Template</h1>
        <p>This is a blank template for a web page.</p>
        <th:block th:each="i: ${#numbers.sequence(0, divCount - 1)}">
            <div class="benchmark"> Rendering a div (num <span th:text="${i}"></span>)</div>
        </th:block>
    </body>
</html>
```

Results:

|           	| First render 	| Next renders 	 |
|-----------	|--------------	|----------------|
| Flamewing   	| 54ms         	| 9ms          	 |
| Thymeleaf 	| 303ms        	| 51ms         	 |


## Using Flamewing

Flamewing is not ready for prime use yet. Still, if you want to experiment with it, you can do so by downloading the source code and executing the `gradle assemble` task. This will generate a `flamewing-0.0.1-SNAPSHOT-plain.jar` which you can import in your project.

Additionally, the following other dependencies are required:
```gradle
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-security'
```

Finally, create a configuration file for your Spring Boot app and import the `FlamewingConfig` configuration:

```java
@Configuration
@EnableConfigurationProperties()
@Import({FlamewingConfig.class})
public class ApplicationConfiguration {
    
}
```
