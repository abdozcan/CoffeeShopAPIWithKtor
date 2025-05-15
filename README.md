# Coffee Shop API

Modern backend REST API for a coffee shop application built with Kotlin and Ktor.

## Tech Stack

- Kotlin
- Ktor
- Exposed ORM
- SQLite database
- JWT Authentication
- OpenAPI & Swagger UI for API documentation

## API Endpoints

Based on the OpenAPI specification, the following endpoints are available:

- **Category**
    - `GET /category` - Get all categories

- **Product**
    - `GET /product/{id}` - Get product by ID
    - `GET /product/popular/limit/{limit}/page/{page}` - Get popular products with pagination
    - `GET /product/newest/limit/{limit}/page/{page}` - Get the newest products with pagination
    - `GET /product/special-offer/limit/{limit}/page/{page}` - Get special offer products with pagination
    - `POST /product/search` - Search products with filters

- **User**
    - `GET /user/info` - Get current user info
    - `GET /user/name` - Get username
    - `GET /user/by-email` - Get user by email
    - `GET /user/{id}` - Get user by ID
    - `DELETE /user/{id}` - Delete user by ID
    - `PUT /user/edit` - Edit user details
    - `PUT /user/change-password` - Change user password
    - `POST /user/reset-password` - Reset user password

- **Authentication**
    - `POST /auth/login` - User login
    - `POST /auth/register` - User registration
    - `POST /auth/refresh-token` - Refresh authentication token
    - `POST /auth/send-otp/{email}` - Send OTP to email
    - `POST /auth/verify-otp` - Verify OTP
    - `POST /auth/resend-otp/{email}` - Resend OTP

- **Cart**
    - `GET /cart/all` - Get all items in cart
    - `GET /cart/items-size` - Get cart size
    - `POST /cart/add` - Add item to cart
    - `POST /cart/delete` - Delete items from cart
    - `POST /cart/apply-promo-code` - Apply promo code to cart

- **Favorite**
    - `POST /favorite/is-favorite/{product_id}` - Check if product is favorite
    - `POST /favorite/add/{product_id}` - Add product to favorites
    - `DELETE /favorite/delete/{product_id}` - Delete product from favorites

- **Review**
    - `GET /review/order/{orderId}` - Get reviews by order ID
    - `POST /review/add` - Add review
    - `PUT /review/edit` - Edit review
    - `DELETE /review/delete/{id}` - Delete review by ID

- **Order**
    - `GET /order/{id}` - Get order by ID
    - `GET /order/{orderId}/products` - Get order products
    - `PUT /order/cancel/{orderId}` - Cancel order
    - `DELETE /order/delete/{id}` - Delete order
    - `POST /order/add` - Create new order

- **Address**
    - `GET /address/all` - Get all addresses
    - `GET /address/default` - Get default address
    - `POST /address/set-default/{id}` - Set default address
    - `POST /address/delete/{id}` - Delete address
    - `POST /address/add` - Add new address
    - `PUT /address/edit` - Edit address

- **3D Secure**
    - `GET /3d-secure/verification` - 3D Secure verification
    - `POST /3d-secure/callback` - 3D Secure callback
    - `GET /3d-secure/success` - 3D Secure success
    - `GET /3d-secure/failure` - 3D Secure failure

- **FAQ**
    - `GET /faq/all` - Get all FAQs
    - `GET /faq/{id}` - Get FAQ by ID
    - `GET /faq/language-code/{code}` - Get FAQs by language code
    - `POST /faq/add-faq` - Add new FAQ
    - `POST /faq/add-translation` - Add FAQ translation
    - `PUT /faq/edit` - Edit FAQ
    - `DELETE /faq/delete/{id}` - Delete FAQ