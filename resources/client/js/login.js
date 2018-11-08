function pageLoad(){
    if(Cookies.get("sessionToken")!== undefined) {
        console.log("Session Token found");
        $.ajax({
            url: '/user/check',
            type: 'GET',
            success: response => {
                console.log("checking for error");
                if (!response.hasOwnProperty("error")) {
                    alert("You're already logged in as "+response.firstName +" "+ response.lastName);
                    window.location.href = "/client/profile.html";
                }

            }
        })
    }
    else{
        loginForm();
    }
}
function loginForm() {
    const loginForm = $('#loginForm');
    loginForm.submit(event => {
        event.preventDefault();
        $.ajax({
            url: '/user/login',
            type: 'POST',
            data: loginForm.serialize(),
            success: response => {
                if (response.startsWith('Error:')) {
                    alert(response);
                } else {
                    Cookies.set("sessionToken", response);
                    window.location.href = "/client/profile.html";
                }
            }
        });

    });
}