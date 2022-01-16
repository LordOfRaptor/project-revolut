POST http://localhost:8080/accounts
body
{
  "name": "Noirot",
  "surname": "Quentin",
  "country": "France",
  "passport": "RZZTUUUUU",
  "phoneNumber": "+333698850988",
  "password": "testtest",
  "solde" : 100000
}
POST http://localhost:8080/accounts/{uuidIntervenant}
body
{
    "password" : "testtest"
}

------------------------------------------------------
Ne pas oublier de placer le bearer token dans le header
------------------------------------------------------

POST http://localhost:8080/accounts/{uuidIntervenant}/cards
body
{
    "blocked": false,
    "virtual": false,
    "contactless": true,
    "limit": 5000,
    "location": false
}

GET http://localhost:8080/accounts/{uuidIntervenant}/cards


POST http://localhost:8080/accounts/{uuidIntervenant}/transactions
body
{
    "amount": 10.50,
    "country" : "United Kingdom",
    "category" : null,
    "label" : null,
    "creditAccount" : "1234",
    "creditAccountName" :null,
    "debtorAccountName" : null
}

POST http://localhost:8084/paymentOnline

body
{
    "cardNumber":"numeroCarte",
    "cvv": "NumeroCvv",
    "country" : "United Kingdom",
    "amount" : 14.50
}


GET http://localhost:8080/accounts/{uuidIntervenant}/transactions