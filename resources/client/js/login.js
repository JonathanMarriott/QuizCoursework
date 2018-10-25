function pageLoad(){
    loginForm();
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