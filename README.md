# E-commerce Fullstack App

## Description

The E-commerce Fullstack App is a robust platform enabling users to search, explore, and purchase products with ease. It supports seller registration, real-time notifications, secure transactions via VNPay, and provides advanced search capabilities. The platform emphasizes security (JWT-based authentication), scalability, and modern development best practices.

## Features

- **Advanced Product Search**: Powered by Elasticsearch for fast and relevant results.
- **Secure Authentication & Authorization**: Uses JWT for protecting user data and actions.
- **Real-time Updates**: Integrates Kafka for live notifications and synchronization.
- **Seamless Transactions**: VNPay integration for quick and secure payments.
- **Seller Dashboard**: Sellers can register, manage products (with variations), and monitor sales.
- **Cloud Storage for Media**: Product images are stored on Cloudinary.
- **Order Management**: Track orders, monitor sales, and manage cancellations.
- **One-on-One Chat**: Real-time chat feature with Firebase message storage.

## Technology Stack

- **Back-end**: Spring Boot, Spring Security, Spring Data JPA, Spring WebSocket, Firebase, JWT, Docker, Elasticsearch, Kafka
- **Front-end**: React.js, React Query
- **Database**: MySQL

## Getting Started

### Prerequisites

- Docker installed and running
- Node.js & npm (for front-end development)

### Backend & Services Setup

```bash
docker compose up
```
> Ensure all required environment variables are configured.

### Frontend Setup

1. Navigate to the front-end directory:
    ```bash
    cd front-end-ecommerce
    ```
2. Install dependencies:
    ```bash
    npm install
    ```
3. Start the development server:
    ```bash
    npm run dev
    ```

### Registering the Debezium MySQL Connector

To register the Debezium MySQL connector, send a POST request to the Kafka Connect REST API with the following configuration:

```json
{
  "name": "mysql-connector",
  "config": {
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",
    "database.hostname": "mysql",
    "database.port": "3306",
    "database.user": "debezium",
    "database.password": "090224",
    "database.server.id": "1",
    "database.server.name": "mysql_server",
    "database.include.list": "ecommerce",
    "table.include.list": "ecommerce.product,ecommerce.category,ecommerce.options,ecommerce.option_value,ecommerce.sku,ecommerce.sku_attribute",
    "database.history.kafka.bootstrap.servers": "kafka2:29092",
    "database.history.kafka.topic": "test.mysql_server",
    "schema.history.internal.kafka.topic": "schema-changes.mysql",
    "schema.history.internal.kafka.bootstrap.servers": "kafka2:29092",
    "topic.prefix": "dbs_"
  }
}
```

Example command (adjust host/port as needed):

```bash
curl -X POST -H "Content-Type: application/json" \
  --data @register-mysql-connector.json \
  http://localhost:8083/connectors
```

## Mock VNPay Payment Information

```
Ngân hàng: NCB
Số thẻ: 9704198526191432198
Tên chủ thẻ: NGUYEN VAN A
Ngày phát hành: 07/15
Mật khẩu OTP: 123456
```

## Author

- [Tân Lê](https://github.com/tanle9t2)

---

Feel free to open issues or pull requests for suggestions and improvements!
