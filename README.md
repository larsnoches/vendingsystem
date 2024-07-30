# Vending system
Bus ticket vending system. Stack: Docker, docker-compose, Java, SQL, Spring Framework (boot, web, data jpa, security), ZXing, PostgreSQL, Liquibase, Rabbitmq


## Architecture
![image](https://github.com/user-attachments/assets/bec93c9f-ebb2-4ead-90b2-8c368afb8b62)

## Package diagram
![image](https://github.com/user-attachments/assets/e5483949-ef79-409d-9ba6-00c4b9c57be2)

## Physical data model
![image](https://github.com/user-attachments/assets/b40c0369-0801-4ac4-b021-1babcf7ff594)

## Launching
To start the application use command:
```
docker-compose up
```
Be default at url http://localhost:8282 you should set some data. At first start application was create a user with manager role. For entering into system use 
```
manager@manager.com:what
```
After all for stopping the application write in console:
```
docker-compose down
```

## Settings
- POSTGRES_USER – db username;
- POSTGRES_PASSWORD – db password;
- CASHREG_RABBITMQ_HOST – hostname, where message broker rabbitmq has been started;
- CASHREG_RABBITMQ_USERNAME – access username to rabbitmq;
- CASHREG_RABBITMQ_PASSWORD – access password to rabbitmq;
- REGULARBUS_DB_HOST – db hostname;
- REGULARBUS_SERVER_PORT – regularbus service port number (8080);
- REGULARBUS_APPLICATION_NAME – label for application;
- WEBSERVER_PORT – portnumber for frontend.




