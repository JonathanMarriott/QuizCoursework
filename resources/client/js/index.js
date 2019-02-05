function pageLoad(){ // Run when the page loads
    if(Cookies.get("sessionToken")!== undefined) { // Checks if the user has a session token
        console.log("Session Token found"); //Logs the client has a session token
        $.ajax({ // forms the AJAX request
            url: '/user/check', // sends the request to the right address on the server
            type: 'GET', //Sets the request as a GET request as retrieving data
            success: response => {  //executes following code when the response is received
                console.log("Checking for error");
                if (!response.hasOwnProperty("error")) {  //Checks if the token was found in the database
                    $("#navbarDropdownMenuLink").html("Logged in as " + response.firstName + " " + response.lastName); // Shows the user as logged in
                    $("#navbarAccountMenu").html('<a class="dropdown-item" onclick="logout()" href="/client/index.html">Sign Out</a>'); // Adds logout to dropdown
                    console.log("Navbar updated"); // Logs the navbar has been updated
                }}

    });
}}