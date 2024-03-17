## Handling Cookies with Spring Boot and the Servlet API

### What are Cookies?
Cookies are  piece of information that is stored on the client-side (i.e. in the browser). The client sends them to the server with each request and servers can tell the client which cookies to store.

They are commonly used to track the activity of a website, to customize user sessions, and for servers to recognize users between requests. Another scenario is to store a JWT token or the user id in a cookie so that the server can recognize if the user is authenticated with every request.

### How Do Cookies Work?
Cookies are sent to the client by the server in an HTTP response and are stored in the client (user’s browser).

The server sets the cookie in the HTTP response header named Set-Cookie. A cookie is made of a key /value pair, plus other optional attributes, which we’ll look at later.

Let’s imagine a scenario where a user logs in. The client sends a request to the server with the user’s credentials. The server authenticates the user, creates a cookie with a user id encoded, and sets it in the response header. The header Set-Cookie in the HTTP response would look like this:
> Set-Cookie: user-id=c2FtLnNtaXRoQGV4YW1wbGUuY29t

Once the browser gets the cookie, it can send the cookie back to the server. To do this, the browser adds the cookie to an HTTP request by setting the header named Cookie:

> Cookie: user-id=c2FtLnNtaXRoQGV4YW1wbGUuY29t

The server reads the cookie from the request verifies if the user has been authenticated or not, based on the fact if the user-id is valid.

As mentioned, a cookie can have other optional attributes, so let’s explore them.


### Cookie Max-Age and Expiration Date
The attributes Max-Age and/or Expires are used to make a cookie persistent. By default, the browser removes the cookie when the session is closed unless Max-Age and/or Expires are set. These attributes are set like so:


> Set-Cookie: user-id=c2FtLnNtaXRoQGV4YW1wbGUuY29t; Max-Age=86400; Expires=Thu, 21-Jan-2021 20:06:48 GMT

This cookie will expire 86400 seconds after being created or when the date and time specified in the Expires is passed.

When both attributes are present in the cookie, Max-Age has precedence over Expires.

### Cookie Domain
Domain is another important attribute of the Cookie. We use it when we want to specify a domain for our cookie:

> Set-Cookie: user-id=c2FtLnNtaXRoQGV4YW1wbGUuY29t; Domain=example.com; Max-Age=86400; Expires=Thu, 21-Jan-2021 20:06:48 GMT

By doing this we are telling the client to which domain it should send the cookie. A browser will only send a cookie to servers from that domain.
Setting the domain to “example.com” not only will send the cookie to the “example.com” domain but also its subdomains “foo.example.com” and “bar.example.com”.

If we don’t set the domain explicitly, it will be set only to the domain that created the cookie, but not to its subdomains.

### Cookie Path
The Path attribute specifies where a cookie will be delivered inside that domain. The client will add the cookie to all requests to URLs that match the given path. This way we narrow down the URLs where the cookie is valid inside the domain.

Let’s consider that the backend sets a cookie for its client when a request to http://example.com/login is executed:

> Set-Cookie: user-id=c2FtLnNtaXRoQGV4YW1wbGUuY29t; Domain=example.com; Path=/user/; Max-Age=86400; Expires=Thu, 21-Jan-2021 20:06:48 GMT

Notice that the Path attribute is set to /user/. Now let’s visit two different URLs and see what we have in the request cookies.

When we execute a request to http://example.com/user/, the browser will add the following header in the request:

> Cookie: user-id=c2FtLnNtaXRoQGV4YW1wbGUuY29t

As expected, the browser sends the cookie back to the server.

When we try to do another request to http://example.com/contacts/ the browser will not include the Cookie header, because it doesn’t match the Path attribute.


When the path is not set during cookie creation, it defaults to /.

By setting the Path explicitly, the cookie will be delivered to the specified URL and all of its subdirectories.

### Secure Cookie
In cases when we store sensitive information inside the cookie and we want it to be sent only in secure (HTTPS) connections, then the Secure attribute comes to our rescue:

> Set-Cookie: user-id=c2FtLnNtaXRoQGV4YW1wbGUuY29t; Domain=example.com; Max-Age=86400; Expires=Thu, 21-Jan-2021 20:06:48 GMT; Secure

By setting Secure, we make sure our cookie is only transmitted over HTTPS, and it will not be sent over unencrypted connections.

### HttpOnly Cookie
HttpOnly is another important attribute of a cookie. It ensures that the cookie is not accessed by the client scripts. It is another form of securing a cookie from being changed by malicious code or XSS attacks.

> Set-Cookie: user-id=c2FtLnNtaXRoQGV4YW1wbGUuY29t; Domain=example.com; Max-Age=86400; Expires=Thu, 21-Jan-2021 20:06:48 GMT; Secure; HttpOnly

Not all browsers support the HttpOnly flag. The good news is most of them do, but if it doesn’t, it will ignore the HttpOnly flag even if it is set during cookie creation. Cookies should always be HttpOnly unless the browser doesn’t support it or there is a requirement to expose them to clients' scripts.

Now that we know what cookies are and how they work let’s check how we can handle them in spring boot.

### Handling Cookies with the Servlet API
Now, let’s take a look at how to set cookies on the server-side with the Servlet API.

#### Creating a Cookie
For creating a cookie with the Servlet API we use the Cookie class which is defined inside the <code>javax.servlet.http</code> package.

The following snippet of code creates a cookie with name <b>user-id</b> and value <b>c2FtLnNtaXRoQGV4YW1wbGUuY29t</b> and sets all the attributes we discussed:


```java
Cookie jwtTokenCookie = new Cookie("user-id", "c2FtLnNtaXRoQGV4YW1wbGUuY29t");

jwtTokenCookie.setMaxAge(86400);
jwtTokenCookie.setSecure(true);
jwtTokenCookie.setHttpOnly(true);
jwtTokenCookie.setPath("/user/");
jwtTokenCookie.setDomain("example.com");
```
Now that we created the cookie, we will need to send it to the client. To do so, we add the cookie to the <code>response(HttpServletResponse)</code> and we are done. Yes, it is as simple as that:

```java
response.addCookie(jwtTokenCookie);
```

#### Reading a Cookie

After adding the cookie to the response header, the server will need to read the cookies sent by the client in every request.

The method <code>HttpServletRequest#getCookies()</code> returns an array of cookies that are sent with the request. We can identify our cookie by the cookie name.

In the following snippet of code, we are iterating through the array, searching by cookie name, and returning the value of the matched cookie:
```java
public Optional<String> readServletCookie(HttpServletRequest request, String name) {
    return Arrays.stream(request.getCookies())
        .filter(cookie->name.equals(cookie.getName()))
        .map(Cookie::getValue)
        .findAny();
}
```

#### Deleting a Cookie
To delete a cookie we will need to create another instance of the Cookie with the same name and maxAge 0 and add it again to the response as below:
```java
Cookie deleteServletCookie = new Cookie("user-id", null);
deleteServletCookie.setMaxAge(0);
response.addCookie(deleteServletCookie);

```
Going back to our use case where we save the JWT token inside the cookie, we would need to delete the cookie when the user logs out. Keeping the cookie alive after the user logs out can seriously compromise the security.

### Handling Cookies with Spring
Now that we know how to handle a cookie using the Servlet API, let’s check how we can do the same using the Spring Framework.

#### Creating a Cookie
In this section, we will create a cookie with the same properties that we did using the Servlet API.
We will use the class <code>ResponseCookie</code> for the cookie and <code>ResponseEntity</code> for setting the cookie in the response. They are both defined inside org.springframework.http package.

<code>ResponseCookie</code> has a static method <code>from(final String name, final String value)</code> which returns a <code>ResponseCookieBuilder</code> initialized with the name and value of the cookie.
We can add all the properties that we need and use the method <code>build()</code> of the builder to create the ResponseCookie:


```java
ResponseCookie springCookie = ResponseCookie.from("user-id", "c2FtLnNtaXRoQGV4YW1wbGUuY29t")
    .httpOnly(true)
    .secure(true)
    .path("/")
    .maxAge(60)
    .domain("example.com")
    .build();
```
Below is a simple example of a controller method which add some cookies into the header that in turn added to <code>ResponseEntity</code>

```java
    public final String SauronRising = """
            Now Sauron's lust and pride increased, until he knew no bounds, and he determined to make himself master of all things in Middle-earth, 
            and to destroy the Elves, and to compass if he might, the downfall of Númenor.   
        """;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ResponseEntity<String> greet() {
        ResponseCookie cookie1 = ResponseCookie.from("user-id", UUID.randomUUID().toString())
                        .maxAge(Duration.ofSeconds(3600)).build();
        ResponseCookie cookie2 = ResponseCookie.from("user-name", "Sauron")
                .maxAge(Duration.ofSeconds(3600)).build();
        ResponseCookie cookie3 = ResponseCookie.from("hunting-for", "MasterRing!") // Cookie value cannot have Space. Strange!
                .maxAge(Duration.ofSeconds(3600)).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie1.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie2.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie3.toString());
       
        return new ResponseEntity<>(SauronRising, headers, HttpStatus.OK);
    }
```

#### Reading a Cookie with @CookieValue

Spring Framework provides the <code>@CookieValue</code> annotation to read any cookie by specifying the name without needing to iterate over all the cookies fetched from the request.
<code>@CookieValue</code> is used in a controller method and maps the value of a cookie to a method parameter:
```java
    @GetMapping("/return")
    public String readCookie(@CookieValue(name="user-id", defaultValue = "") String userId,
                             @CookieValue(name="user-name", defaultValue = "") String userName) {
        if (!userName.isEmpty() && !userId.isEmpty()) {
            return userName +"(" + userId +")"+", I see you!";
        }
        return "You do not exist!";
    }
```
In cases where the cookie with the name <code>“user-id”</code> does not exist, the controller will return the default value defined with <code>defaultValue = "default-user-id"</code>. If we do not set the default value and Spring fails to find the cookie in the request then it will throw <code style="color:red">java.lang.IllegalStateException</code>  exception.

#### Deleting a Cookie

To delete a cookie, we will need to create the cookie with the same name and maxAge to 0 and set it to the response header:
```java
    @GetMapping("/destroy")
    public ResponseEntity<String> logout() {
        ResponseCookie cookie1 = ResponseCookie.from("user-id").maxAge(Duration.ZERO).build();
        ResponseCookie cookie2 = ResponseCookie.from("user-name").maxAge(Duration.ZERO).build();
        ResponseCookie cookie3 = ResponseCookie.from("hunting-for").maxAge(Duration.ZERO).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie1.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie2.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie3.toString());
        return new ResponseEntity<>("Master Ring Destroyed and so is Sauron!", headers, HttpStatus.OK);
    }
```
