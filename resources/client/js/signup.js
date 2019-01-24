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
                    signUpForm();// runs the form function if the user is not logged in
                }
            }
        })
    }
    else{
        signUpForm();// runs the form function if the user is not logged in
    }
}

function signUpForm() {
    const signUpForm = $('#signUpForm'); // Finds the form on the page via its ID
    signUpForm.submit(event => {  // Runs the code enclosed when the form submits
        event.preventDefault(); // Stops the forms default behaviour
        if (!($("#inputPassword").val()===$("#inputPassword2").val())) {//Checks the 2 passwords match client side to reduce server load
            alert("Passwords entered do not match"); // User alerted if the passwords do not match
        }
        else if ($("#inputFirst").val().length>20 || $("#inputLast").val().length   >20) { // Checks the names are not over 20 characters
            alert("Names are too long");
        }
        else{
                $.ajax({  // Forms the AJAX request
                    url: '/user/add',  // sends to the addUser method in user controller
                    type: 'POST', // Post request as updating DB
                    data: signUpForm.serialize(),  // serializes the form content to a text string
                    success: response => {  //Runs once the server responds
                        if (response.startsWith('Error:')) { //checks for errors
                            alert(response); // Alerts the user to the error
                        } else { // otherwise the account creation was successful
                            Cookies.set("sessionToken",response); // Sets the clients sessionToken cookie to the value from the server
                            window.location.href = "/client/profile.html"; // Redirects the user to the profile page
                        }
                    }
                });}
        });
}
