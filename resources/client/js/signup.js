
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
        signUpForm();
    }
}

function signUpForm() {
    const signUpForm = $('#signUpForm');
    signUpForm.submit(event => {
        event.preventDefault();
        if ($("#inputPassword").val()===$("#inputPassword2").val()){
            $.ajax({
                url: '/user/add',
                type: 'POST',
                data: signUpForm.serialize(),
                success: response => {
                    if (response.startsWith('Error:')) {
                        alert(response);
                    } else {
                        Cookies.set("sessionToken",response);
                        window.location.href = "/client/profile.html";
                    }
                }
            });}
        else{
            alert("Passwords entered do not match");
        }

    });
}