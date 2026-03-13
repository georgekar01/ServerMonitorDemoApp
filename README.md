---------This is a demo project in which i implement a crud notification app using Rest Api. The app is made for practice and consists of:

>RestApi: Created controllers and services to manipulate project database (Users, Notifications)

>Security:  Custom authentication using username and password
            JWT suppport
            JWT refresh support (only works on postman right now)
            Role based authorization

>Validation: Custom password format validation
            Custom notification format validation

>Real-time connection: Websocket implemented to give real-time feedback from client to client

>Paging: Implemented paging for large data fetching (used for optimization)

>Dto: Added DTO logic to hide sensitive data and for validation puproses
