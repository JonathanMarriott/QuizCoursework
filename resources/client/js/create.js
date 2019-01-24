function pageLoad(userInfo){
    titleForm(userInfo);
}
function titleForm(userData){
    const emailForm = $("#titleForm");
    emailForm.submit(event => { //Runs the code enclosed when the form submits
        event.preventDefault(); // Stops the default form GET behaviour
        $.ajax({ // Forms the AJAX request
            url: '/quiz/create',  // sends to the add quiz method in quiz controller
            type: 'POST',  // Is a post request as it will be changing data
            data: emailForm.serialize(), // Converts form data to a URL encoded string
            success: response => { //Runs once the server responds
                if (response.hasOwnProperty("error")) { //Checks for errors
                    alert(response.error); //Displays any errors
                } else {
                    console.log("Quiz title Submitted successfully")
                    titleFinish(response);


                }
            }
        });
    });
}
function titleFinish(quizData){
    $("#titleDiv").html('<h4 class="font-weight-normal text-light">'+quizData.title+'</h4>')
    $("#formDiv").html('')
}