# Instructions

1. Run wiremock by executing the ```start.sh``` script.
2. Clone and run the backend application from the repository https://github.com/gabrielholzdecastro/grocery with the command ```java -jar grocery-0.0.1-SNAPSHOT.jar```.
3. Clone and run the frontend application from the repository https://github.com/gabrielholzdecastro/grocery-front with the command ```npm install``` and ```npm run dev```
4. You can also test the APIs by importing the qikserve.postman_collection file, which can be found in the backend repository.

# Follow up questions

## 1. How long did you spend on the test? What would you add if you had more time?

I spent approximately 8 to 10 hours working on the test. If I had more time, I would make the following improvements:
- Documentation and Swagger: I would include more detailed API documentation using Swagger to make the API easier to interact with.

---

## 2. What was the most useful feature that was added to the latest version of your chosen language? Please include a snippet of code that shows how you've used it.

Sequence Collections

```java
    final Promotion promotion = productDetail.getPromotions().getFirst();
```

## 3. What did you find most difficult?

I didn't encounter any major challenges during the test. However, one part that could be slightly more time-consuming is managing different types of promotions and ensuring all possible scenarios are properly covered in discount calculations.

## 4. What mechanism did you put in place to track down issues in production on this code? If you didn’t put anything, write down what you could do.
   
Currently, there is no specific mechanism in place to monitor production issues, but to track down problems in production, I would implement the following:

Structured logging: I would use SLF4J with Logback to log important events and errors at different severity levels (INFO, DEBUG, ERROR).
- Monitoring: Integration with tools like Prometheus to collect performance metrics and Grafana to visualize them.
- Error alerts: Integration with Sentry or ELK Stack (Elasticsearch, Logstash, Kibana) to track exceptions and errors in production.
- Distributed tracing: I would use Spring Sleuth and Zipkin for distributed tracing of requests in environments with multiple services.

## 5. The Wiremock represents one source of information. We should be prepared to integrate with more sources. List the steps that we would need to take to add more sources of items with different formats and promotions.

To integrate more sources of items with different formats and promotions, the following steps would be necessary:

Define a common contract (interface) for different sources:

- Create a generic interface that all data sources can implement. This would ensure that the data, regardless of origin, is handled consistently.

```java
public interface ProductSource {
    List<Product> getAllProducts();
    ProductDetail getProductById(String id);
}
```

Add new Feign or HTTP clients:

- Implement new Feign Clients or HTTP Clients to consume other APIs that provide products and promotions, adapting them to the common contract.
- If the new data sources don’t use JSON or have another format, we can integrate conversion libraries to handle XML, CSV, etc.

```java
@FeignClient(name = "otherProductClient", url = "http://newapi.com")
public interface OtherProductClient extends ProductSource {
@GetMapping("/products")
List<Product> getAllProducts();

    @GetMapping("/products/{id}")
    ProductDetail getProductById(@PathVariable String id);
}
```

Convert the data into a common format:

- For each new data source, convert the different formats into a unified model. For example, if an API returns promotions in a different format, map them to the application’s promotion model.

```java
public class OtherProductService implements ProductSource {
@Autowired
private OtherProductClient otherProductClient;

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = otherProductClient.getAllProducts();
        return products.stream().map(this::convertToInternalFormat).collect(Collectors.toList());
    }

    private Product convertToInternalFormat(ExternalProduct externalProduct) {
        // Convert the external product to the internal format
        return new Product(externalProduct.getId(), externalProduct.getName(), externalProduct.getPrice());
    }
}
```

Data aggregation logic:

- If multiple data sources are involved, it’s necessary to define how to consolidate or prioritize the product information coming from different places. For example, which API takes precedence if the same product is returned from multiple sources?

Test and validate the integrations:

- Write tests to ensure that the different data sources are being correctly integrated and that promotions are applied properly according to each source.
- Use WireMock or another simulator to mock the responses of new data sources during development.

Source monitoring:

- Monitor the performance and integrity of the different sources. For example, if one API goes down, implement a fallback to another source or a contingency plan.

These steps would allow us to easily add new providers of products and promotions to the system, ensuring that the application remains flexible and scalable.
