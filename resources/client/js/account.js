function pageLoad(userInfo) {
    updateEmail()
    updateNames()
    updatePassword()
    deleteAccount()
}

function updateEmail(){
    const emailForm = $("#emailForm");
    emailForm.submit(event => { //Runs the code enclosed when the form submits
        event.preventDefault(); // Stops the default form GET behaviour
        $.ajax({ // Forms the AJAX request
            url: '/user/updateEmail',  // sends to the updateEmail method in user controller
            type: 'POST',  // Is a post request as it will be changing data
            data: emailForm.serialize(), // Converts form data to a URL encoded string
            success: response => { //Runs once the server responds
                if (response.startsWith('Error:')) { //Checks for errors
                    alert(response); //Displays any errors
                } else {
                    alert("Email updated successfully"); // Tells the user their email has been updated
                    window.location.href = "/client/profile.html"; // redirect to profile page
                }
            }
        });
    });
}
function updateNames(){
    const form = $("#nameForm");
    form.submit(event => { //Runs the code enclosed when the form submits
        event.preventDefault(); // Stops the default form GET behaviour
        $.ajax({ // Forms the AJAX request
            url: '/user/updateName',  // sends to the updatesNames method in user controller
            type: 'POST',  // Is a post request as it will be changing data
            data: form.serialize(), // Converts form data to a URL encoded string
            success: response => { //Runs once the server responds
                if (response.startsWith('Error:')) { //Checks for errors
                    alert(response); //Displays any errors
                } else {
                    alert("Names updated successfully"); // alerts the user their names have been updated
                    window.location.href = "/client/profile.html"; // redirects to profile page
                }
            }
        });
    });
}
function updatePassword(){
    const form = $("#passwordForm");
    form.submit(event => { //Runs the code enclosed when the form submits
        event.preventDefault(); // Stops the default form GET behaviour
        if ($("#inputPassword").val()===$("#inputPassword2").val()) {
            $.ajax({ // Forms the AJAX request
                url: '/user/updatePassword',  // sends to the updatesNames method in user controller
                type: 'POST',  // Is a post request as it will be changing data
                data: form.serialize(), // Converts form data to a URL encoded string
                success: response => { //Runs once the server responds
                    if (response.startsWith('Error:')) { //Checks for errors
                        alert(response); //Displays any errors
                    } else {
                        alert("password updated successfully"); // Alerts the user their password has been updated
                        window.location.href = "/client/profile.html"; // Redirects to the profile page
                    }
                }
            });
        }else {
            alert("Passwords do not match"); // Tells the user if the entered passwords dont match
        }
    });
}
function deleteAccount(){
    const form = $("#deleteForm");
    form.submit(event => { //Runs the code enclosed when the form submits
        event.preventDefault(); // Stops the default form GET behaviour
        if ($("#inputPassword3").val()===$("#inputPassword4").val()) {
            $.ajax({ // Forms the AJAX request
                url: '/user/delete',  // sends to the updatesNames method in user controller
                type: 'POST',  // Is a post request as it will be changing data
                data: form.serialize(), // Converts form data to a URL encoded string
                success: response => { //Runs once the server responds
                    if (response.startsWith('Error:')) { //Checks for errors
                        alert(response); //Displays any errors
                    } else {
                        alert("Account successfully deleted"); //Informs the user their account has been deleted
                        Cookies.remove("sessionToken"); // Removes the sessionToken cookie to log them out
                        window.location.href = "/client/index.html"; // Redirect to the homepage
                    }
                }
            });
        }else {
            alert("Passwords do not match");// Tells the user if the entered passwords dont match
        }
    });
}

