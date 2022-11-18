## Description

This is a demo Spring Boot aplication which provides the following features:

* REST API support (POST, GET, PUT, DELETE and PATCH operations)
* Spring Data with Postgres SQL
* Maven as a build tool

The application allows to manage **products**. Every **product** has it's unique name, description, price, creation and modification dates. **Name** and **description** can be specified in different **locales**, and product **price** can be specified in different **currencies**.

The application has two REST interfaces: one for administrators and another one for customers.

An **administrator** can perform the following operations:

* Create new products
* Update product properties, such as product information and price
* Delete products by their IDs
* Retrieve information about product by its ID

The following constraints for the product are present:

* A product name is a mandatory field, cannot be empty
* A product price is a mandatory field, must be greater than zero

A **customer** can perform the following operations:

* Request information about product by its ID
* Retrieve the list of all available products (paginated)
* Search products by **name** or **description**

Customer must specify both **locale** and **currency** when requesting a product or product list. In case the product is not found for the specified locale and currency a 404 error is returned.

## Build and run the application

Java 11 and Maven 3.8.x are required to build and run the application. To speed up the application deployment it is recommended to build and run the application on a machine with Docker installed. This will allow to avoid manual database set up.

### With Docker
1. Create a local copy of the project:

   `$ git clone https://github.com/chauzov/spring-crud-app.git`

2. Enter the directory `spring-crud-app` and run the command:

   `$ mvn install -PmakeDockerImage`

3. Execute the below command:

   `$ docker-compose up`

The application must be ready with a Postgres database running in separate container (without persistent storage).

### Without Docker
1. Prepare an empty Postgres database named `products`
2. Create a user `products` for the database with the full set of permissions
3. Create a local copy of the project:

   `$ git clone https://github.com/chauzov/spring-crud-app.git`

4. Enter the directory `spring-crud-app` and edit the file `src/main/resources/application-dev.properties`
5. In the database connection string specify the IP address of the Postgres server
6. Edit the line `spring.datasource.password` and specify the password of the user `products`

7. Save the file and execute the command:

   `$ mvn package`

8. Run the application:

   `$ java -Dspring.profiles.active=dev -jar target/crudapp-0.0.1-SNAPSHOT.jar`


## Administrator API usage

### Resource(s) creation
In order to create a new product, an HTTP **POST** request with the header `Content-Type: application/json` must be sent to the endpoint <http://hostname:8080/api/v1/admin/product> with the payload as below:

```
[{
    "productInfo": [
        {
            "locale": "es",
            "name": "La leche",
            "description": "Leche normal 3%"
        },
        {
            "locale": "fr",
            "name": "Le Lait",
            "description": "Lait ordinaire 3%"
        }
    ],
    "productPrices": [
        {
            "price": 2.2,
            "currency": "EUR"
        },
        {
            "price": 2.5,
            "currency": "USD"
        }
    ]
}]
```

Note that the collection is sent, so it is possible to sent multiple products at once.

### Request for a resource

In order to request information about the product by its ID, a **GET** request must be sent to the following example URL:

<http://hostname:8080/api/v1/admin/product/1>

here 1 - is the ID of a particular product.

An example of a response:

```
{
    "productId": 1,
    "productInfo": [
        {
            "productInfoId": 1,
            "locale": "es",
            "name": "La leche",
            "description": "Leche normal 3%"
        },
        {
            "productInfoId": 2,
            "locale": "fr",
            "name": "Le Lait",
            "description": "Lait ordinaire 3%"
        }
    ],
    "productPrices": [
        {
            "priceId": 2,
            "price": 2.50,
            "currency": "USD"
        },
        {
            "priceId": 1,
            "price": 2.20,
            "currency": "EUR"
        }
    ],
    "created": "2022-11-17T12:38:01.579699",
    "modified": "2022-11-17T12:38:01.579707"
}
```

### Resource full update

A product resource body can be fully replaced. It is done the same way as a new product is created, however, there are two differences:

- An HTTP method **PUT** with the header `Content-Type: application/json`must be used
- New resource body must be sent to the endpoint with the product ID specified, see the example: <http://hostname:8080/api/v1/admin/product/1>

### Resource partial update

A product body can be updated partially with the HTTP method **PATCH** and header `Content-Type: application/json-patch+json`. A patch payload must be sent to the endpoint with the product ID specified, see the example: <http://hostname:8080/api/v1/admin/product/1>

A payload example #1 (adding new price for existing product):

```
[
  { "op": "add", "path": "/productPrices/-", "value": {"price": 150, "currency": "RUR"}}
]
```

A payload example #2 (updating existing price and currency):

```
[
  { "op": "replace", "path": "/productPrices/0/price", "value": 3.00},
  { "op": "replace", "path": "/productPrices/0/currency", "value": "EUR"}
]
```

See the full [JSON PATCH specification](https://www.rfc-editor.org/rfc/rfc6902) for more information.

### Resource deletion

A product resource can be deleted with the HTTP method **DELETE** sent to the endpoint with the product ID specified, see the example: <http://hostname:8080/api/v1/admin/product/1>


## Customer API usage

Customers have two options: request a product by its ID and request all available products. Both options required customer's **locale** and **language** to be specified. If an existing product does not have either corresponding locale or currency, a 404 error will be returned.

Since every product returned for customers can only single description in specified language and single price, the returned resource has a different representation unlike to the administrator's one.

### Request for existing product

An HTTP **GET** request is sent to the customer's endpoint with the product ID, customer's locale and currency specified.

Request example:

<http://hostname:8080/api/v1/customer/product/1?locale=es&currency=eur>

Response example:

```
{
    "productId": 1,
    "name": "La leche",
    "description": "Leche normal 3%",
    "locale": "es",
    "price": 2.20,
    "currency": "EUR"
}
```

### Request for the list of available products

An HTTP **GET** request is sent to the customer's endpoint with the keyword **'all'**, customer's locale and currency specified. The returned response is a structure of two entities: **content** and **pageable**. The first one contains a collection of the product resources matched to the given criteria. The second one contains pagination information that can be used on client side.

A request example:

<http://hostname:8080/api/v1/customer/product/all?locale=es&currency=usd>

A response example:

```
{
    "content": [
        {
            "productId": 1,
            "name": "La leche",
            "description": "Leche normal 3%",
            "locale": "es",
            "price": 2.50,
            "currency": "USD"
        },
        {
            "productId": 5,
            "name": "Huevos",
            "description": "Paquete de 12 huevos",
            "locale": "es",
            "price": 3.20,
            "currency": "USD"
        }
    ],
    "pageable": {
...
    "totalPages": 1,
    "totalElements": 2,
...
}
```

In addition, the following **GET** parameters can be added to the request:

- size - defines how many resources must be returned
- page - allows to request next page of the resources

Example:

<http://hostname:8080/api/v1/customer/product/all?locale=es&currency=usd&page=1&size=3>

### Request for the list of available products filtered by a search string

It is possible to filter products by either their names or descriptions using the **'search'** parameter.

A request example:

<http://hostname:8080/api/v1/customer/product/all?locale=en&currency=usd&search=beef>

A response example:

```
{
    "content": [
        {
            "productId": 4,
            "name": "Beef",
            "description": "Texan beef",
            "locale": "en",
            "price": 4.00,
            "currency": "USD"
        }
    ],
    "pageable": {
...
    "totalPages": 1,
    "totalElements": 1,
...
}
```
