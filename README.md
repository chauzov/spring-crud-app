# How to start

1. Prepare the database:

```
postgres=# create database products;
postgres=# create user products with encrypted password '1q2w3e';
postgres=# grant all privileges on database products to products;
```

2. Configure connection to the DB in the file `src/main/resources/application.properties`

3. Build and run the application


#How to test admin functionality:

1. Create new product entry:

POST new JSON to the URL `http://localhost:8080/api/v1/admin/product`
The payload example:
```
  {
  "productInfo": [
    {
      "name": "Potato",
      "description": "Sweet potato",
      "locale": "en"
    },
    {
      "name": "Kartoffel",
      "description": "Der gut katoffel",
      "locale": "de"
    }
  ],
  "productPrice": [
    {
      "price": 11,
      "currency": "eur"
    },
    {
      "price": 15,
      "currency": "usd"
    }
  ]
}
```
This will create a product with 2 names/descriptions in 2 differenct locales and also with 2 prices: in euro and USD.

2. How to get a product by ID:

GET `http://localhost:8080/api/v1/admin/product/{ID}`

3. Delete a product by ID:

DELETE `http://localhost:8080/api/v1/admin/product/{ID}`

4. Update product: make a PUT to `http://localhost:8080/api/v1/admin/product/{ID}` with the new content. The new payload will replace the previous properties.



#How to test customer functionality:

1. Run a GET to the URL `http://localhost:8080/api/v1/customer/product/{PRODUCT_ID}?locale={LOCALE_ID}&currency={CURRENCY_ID}`
Examples: 
PRODUCT_ID: 1
LOCALE_ID: en
CURRENCY_ID: usd


2. Get all products:
GET `http://localhost:8080/api/v1/customer/product/all?locale={LOCALE_ID}&currency={CURRENCY_ID}`

Search by product description is not implemented yet.
