$(function(){
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
});
function logout(){
    Cookies.remove("sessionToken");
    window.location.href = "/client/index.html"
}
function checkUserSession(){
    if(Cookies.get("sessionToken")!== undefined) {
        console.log("Session Token found");
        $.ajax({
            url: '/user/check',
            type: 'GET',
            success: response => {
                console.log("checking for error");
                if (!response.hasOwnProperty("error")) {
                    $("#navbarDropdownMenuLink").html("Logged in as "+response.firstName+" "+response.lastName);
                    $("#navbarAccountMenu").html('<a class="dropdown-item" onclick="logout()" href="/client/index.html">Sign Out</a>');
                    console.log(response);
                    pageLoad(response);
                }

            }
        })
    }
    else{
        alert("Please login first");
        window.location.href = "/client/login.html";
    }
}