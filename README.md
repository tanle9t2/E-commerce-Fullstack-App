
<h2>Description</h2>
<p>The e-commerce website enables users to search, explore, and
purchase products, including similar and related items. It integrates VNPay
for fast and secure transactions. Users can register as sellers, easily create and
manage products with variations, and track sales performance. The platform
ensures secure authentication using JWT.</p>

<h2>Technology</h2>
<ul>
    <li><strong>Back-end:</strong> Spring Boot, Spring Security, Spring Data JPA, JWT, Docker, Elasticsearch, Kafka.</li>
    <li><strong>Front-end:</strong> React.js, React Query.</li>
    <li><strong>Database:</strong> MySQL.</li>
</ul>
<h2>Features</h2>
<ul>
    <li>Advanced product search with Elasticsearch.</li>
    <li>Secure authentication and authorization using JWT.</li>
    <li>Real-time notifications and updates via Kafka.</li>
    <li>Utilize Kafka to synchronize Elasticsearch with the primary database, ensuring real-time search updates</li>
    <li>VNPay integration for seamless transactions.</li>
    <li>Seller registration and product management with variations.</li>
    <li>Images are stored on Cloudinary for efficient media management.</li>  
    <li>Order tracking, sales performance monitoring, and order cancellation.</li>
</ul>
<h2>Installation</h2>
<p>Docker installation is required! Please pay attention to environment variables. Execute the command below to run the project: </p>

```bash
  docker compose up
```

<h2>Front-end Installation</h2>
<p>To set up the front-end, follow these steps: </p>
<p>1. Navigate to the front-end directory: front-end-ecommerce</p>

```bash
  cd front-end-ecommerce
```
<p>2. Install dependencies:</p>

```bash
  npm install
```

<p>3. Install required libraries:</p>
<ul>
  <li>
      <strong>ESLint & Linting Plugins:</strong>
    
 ```bash
    npm install --save-dev vite-plugin-eslint eslint-config-react-app eslint
```
     
  </li>

  <li>
      <strong>React Router:</strong>

      
 ```bash
    npm install react-router-dom@6
```
     
  </li>

  <li>
      <strong>React Router:</strong>

      
 ```bash
    npm install react-router-dom@6
```
     
  </li>

  <li>
      <strong>React Icons:</strong>

      
 ```bash
    npm install react-icons
```
     
  </li>

  <li>
      <strong>React Query:</strong>

      
 ```bash
    npm install @tanstack/react-query@4
```
     
  </li>

  <li>
      <strong>React Query DevTools:</strong>

      
 ```bash
    npm install @tanstack/react-query-devtools@4
```
     
  </li>

  <li>
      <strong>React Hot Toast:</strong>

      
 ```bash
    npm install react-hot-toast
```
     
  </li>

  <li>
      <strong>React Hook Form:</strong>

      
 ```bash
    npm install react-hook-form@7
```
     
  </li>
</ul>
<p>4. Start the development server:</p>

```bash
  npm run dev
```

<h2>Connecting Debezium with MySQL</h2>

<p>To connect Debezium with MySQL, use the following configuration:</p>

Connect debezium with MySQL
```bash
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
    "table.include.list": "ecommerce.product,ecommerce.category,ecommerce.options, ecommerce.option_value,ecommerce.sku,ecommerce.sku_attribute",
    "database.history.kafka.bootstrap.servers": "kafka2:29092",
    "database.history.kafka.topic": "test.mysql_server",
    "schema.history.internal.kafka.topic": "schema-changes.mysql",
    "schema.history.internal.kafka.bootstrap.servers": "kafka2:29092",
    "topic.prefix": "dbs_"
```

<h2>Additional Information</h2>
# Mock VNPay Payment Information

```bash
Ngân hàng: NCB  
Số thẻ: 9704198526191432198  
Tên chủ thẻ: NGUYEN VAN A  
Ngày phát hành: 07/15  
Mật khẩu OTP: 123456
```
<h2>Authors</h2>
<a href ="https://github.com/tanle9t2">Tân Lê</a>


