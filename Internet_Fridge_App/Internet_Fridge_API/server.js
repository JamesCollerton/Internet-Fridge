var express = require('express');        
var bodyParser = require('body-parser');
var mySQL = require('mysql');
var fs = require('fs');

// -----------------------------------------------------------------------------
// INITIALISING MYSQL CONNECTION

var mySQLConnection

// Reads in the connection details from the untracked .json, then makes a connection
// to MySQL using those details.
function initialiseMySQLConnection(){

    var connectionDetails = JSON.parse(fs.readFileSync('ignore/mySQLConnectionDetails.json', 'utf8'));

    mySQLConnection = mySQL.createConnection({
      host     : connectionDetails["localhost"],
      user     : connectionDetails["username"],
      password : connectionDetails["password"],
      database : connectionDetails["database"]
    });

    mySQLConnection.connect(function(err) {
      
        if(err){
            console.log(err)
            process.exit(1);
        }
        else{
            console.log("Successfully set up MySQL connection.")
        }

    });

}

// -----------------------------------------------------------------------------
// INITIALISING NODE APP

// These are the app, router and port variables used for the Node.JS connection
// the app is an express app, the router is used to reroute API requests and the
// port is used to specify port 8080 for the app at localhost.
var app;
var router;
var port;

// This defines the app using Express, then configures it to use bodyParser().
// bodyParser lets us get data from the POST request. It also sets up the port
// on the local host to 8080 to use for the API.
function initialiseAppPort(){

    app = express();                 

    app.use(bodyParser.urlencoded({ extended: true }));
    app.use(bodyParser.json());

    port = process.env.PORT || 8080;    

}    

// -----------------------------------------------------------------------------
// API ROUTING

// This creates an instance of the express router in order to route the API 
// traffic to the correct place. Middleware is also used for all of the requests. 
// Logs that a request that something has been asked for, then looks for the 
// next request.
function initialiseBasicRouting(){

    router = express.Router();              

    router.use(function(req, res, next) {

        console.log('Request recieved.');
        next(); 

    });

}

// All of the routing for the suffix MyFridge is done here. There are two options
// - General post to the API (accessed at POST http://localhost:8080/api/MyFridge).
// - General get to the API (accessed at GET http://localhost:8080/api/MyFridge).
function initialiseGeneralRouting(){

    router.route('/MyFridge')

        .post(function(req, res) {

            res.json({ message: 'Posted with information: ' + req.body.name})
            
        })

        .get(function(req, res) {
            
            res.json({ message: 'Get request (for all data) posted.'})

        });

}

// This just routes all of the traffic with the url suffix 'fridge/' and a fridgeItemID
// parameter. There are three options:
// - Get request with paramters (accessed at GET http://localhost:8080/api/MyFridge/:fridgeItemID)
// - Put request with paramters (accessed at PUT http://localhost:8080/api/MyFridge/:fridgeItemID) 
// - Delete request with paramters (accessed at DELETE http://localhost:8080/api/MyFridge/:fridgeItemID)
function initialiseParameterRouting(){

    router.route('/MyFridge/:fridgeItemID')

        .get(function(req, res) {
            
            res.json({ message: 'Get request (for id:' + req.params.fridgeItemID + ') posted.'})

        })
  
        .put(function(req, res) {

            res.json({ message: 'Put request (for id:' + req.params.fridgeItemID + ') posted.'})

        })

        .delete(function(req, res) {
            
            res.json({ message: 'Delete request (for id:' + req.params.fridgeItemID + ') posted.'})

        });

}

// -----------------------------------------------------------------------------
// START SERVER AND MYSQL CONNECTION

initialiseMySQLConnection();
initialiseAppPort();
initialiseBasicRouting();
initialiseGeneralRouting();
initialiseParameterRouting();

// This is used to register routes. All of our routes will be prefixed with '/api'
app.use('/api', router);
app.listen(port);
console.log('Server started on port: ' + port);