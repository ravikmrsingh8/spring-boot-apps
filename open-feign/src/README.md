# Spring Cloud OpenFeign

Spring Cloud OpenFeign project provides OpenFeign integrations for Spring Boot apps through autoconfiguration and binding to the Spring Environment and other Spring programming model idioms.

## What is Feign?
https://github.com/OpenFeign/feign
Feign is a declarative web service client. It makes writing web service clients easier. To use Feign, create an interface and annotate it.

## How to include Feign?
To include Feign in your project use the starter with group <code>org.springframework.cloud</code> and artifact id <code>spring-cloud-starter-openfeign</code>.

<i>OpenFeignApplication.java</i>
```java
@SpringBootApplication
@EnableFeignClients
public class OpenFeignApplication {
	public static void main(String[] args) {
		SpringApplication.run(OpenFeignApplication.class, args);
	}
}
```
<i>ProductApiClient.java</i>
```java
@FeignClient(value = "ProductApiClient",
        url="http://localhost:9001",
        configuration = ProductApiClientConfig.class)
public interface ProductApiClient {

    @GetMapping("/products")
    List<Product> getProducts(@RequestHeader("Authorization") String accessToken);

    @GetMapping("/products/{id}")
    Product getProductById(@RequestHeader("Authorization") String accessToken,
                              @PathVariable("id") int id);

    @PostMapping(value = "/products", consumes = "application/json")
    void addProduct(@RequestHeader("Authorization") String accessToken,
                    Product product);
}
```

## Overriding Feign defaults
A central concept in Spring Cloud’s Feign support is that of the named client. 
Each feign client is part of an ensemble of components that work together to contact a remote server on demand, and the ensemble has a name that you give it as an application developer using the @FeignClient annotation. 
Spring Cloud creates a new ensemble as an ApplicationContext on demand for each named client using FeignClientsConfiguration. 
This contains (amongst other things) an feign.Decoder, a feign.Encoder, and a feign.Contract. 
It is possible to override the name of that ensemble by using the contextId attribute of the @FeignClient annotation.

Spring Cloud lets you take full control of the feign client by declaring additional configuration (on top of the FeignClientsConfiguration) using @FeignClient. Example:

```java
@Configuration
public class ProductApiClientConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ProductApiErrorDecoder();
    }
}

```


In this case the client is composed from the components already in FeignClientsConfiguration together with any in FooConfiguration (where the latter will override the former).