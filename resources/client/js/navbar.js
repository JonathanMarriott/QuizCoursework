$(function(){
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
});

function logout(){
    Cookies.remove("sessionToken"); // Removes the sessionToken cookie
    window.location.href = "/client/index.html" // Redirect to the homepage
}
function checkUserSession(){
    if(Cookies.get("sessionToken")!== undefined) { // Checks for the sessionToken cookie
        console.log("Session Token found"); // Logs the token was found
        $.ajax({ // Forms the ajax request
            url: '/user/check', // Sends to the checkUser method in user controller
            type: 'GET', // Get request as only receiving data
            success: response => { // Executes once server responds
                console.log("checking for error"); // Logs the error check
                if (!response.hasOwnProperty("error")) { // Checks there are no error messages
                    $("#navbarDropdownMenuLink").html("Logged in as "+response.firstName+" "+response.lastName); // Shows the user as logged in
                    $("#navbarAccountMenu").html('<a class="dropdown-item" onclick="logout()" href="/client/index.html">Sign Out</a>'); // Adds logout to dropdown
                    console.log("Navbar updated"); // Logs the navbar has been updated
                    pageLoad(response); // Parses user object to the pageLoad function
                  }
                else{
                    alert("Please login first"); // User asked to log in if cookie invalid
                    window.location.href = "/client/login.html"; // Redirects to the login page
                }
            }
        })
    }
    else{
        alert("Please login first"); // User asked to log in if no cookie
        window.location.href = "/client/login.html"; //  Redirected to login page
    }
}
