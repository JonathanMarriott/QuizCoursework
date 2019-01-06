function pageLoad(){ // Run when the page loads
    if(Cookies.get("sessionToken")!== undefined) { // Checks if the user has a session token
        console.log("Session Token found"); //Logs the client has a session token
        $.ajax({ // forms the AJAX request
            url: '/user/check', // sends the request to the right address on the server
            type: 'GET', //Sets the request as a GET request as retrieving data
            success: response => {  //executes following code when the response is received
                console.log("Checking for error");
                if (!response.hasOwnProperty("error")) {  //Checks if the token was found in the database
                    alert("You're already logged in as " + response.firstName + " " + response.lastName); //Tells the user they're logged in
                    window.location.href = "/client/profile.html"; // Redirects the user to their profile
                }
                else{
                    loginForm();// runs the form function if the user is not logged in
                }
            }
        })
    }
    else{
        loginForm();// runs the form function if the user is not logged in
    }
}
function loginForm() {
    const loginForm = $('#loginForm'); // finds the form on the page via its ID
    loginForm.submit(event => { //Runs the code enclosed when the form submits
        event.preventDefault(); // Stops the default form GET behaviour
        $.ajax({ // Forms the AJAX request
            url: '/user/login',  // sends to the attemptLogin method in user controller
            type: 'POST',  // Is a post request as it will be changing sessionToken
            data: loginForm.serialize(), // Converts form data to a URL encoded string
            success: response => { //Runs once the server responds
                if (response.startsWith('Error:')) { //Checks for errors
                    alert(response); //Displays any errors
                } else {
                    Cookies.set("sessionToken", response); //Sets the browser cookie to the received token
                    window.location.href = "/client/profile.html"; // Redirects to the profile page
                }
            }
        });

    });
}