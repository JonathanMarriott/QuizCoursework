function pageLoad(userInfo){
    searchForm();
}
function searchForm() {
    const form = $("#searchForm");
    form.submit(event => { //Runs the code enclosed when the form submits
        event.preventDefault(); // Stops the default form GET behaviour
        $.ajax({ // Forms the AJAX request
            url: '/quiz/search',  // sends to the search quiz method in quiz controller
            type: 'POST',  // Is a post request as it will be sending data
            data: form.serialize(), // Converts form data to a URL encoded string
            success: response => { //Runs once the server responds
                if (response.hasOwnProperty("error")) { //Checks for errors
                    alert(response.error); //Displays any errors
                } else {
                    console.log("Search submitted successfully") // logs the success
                    showResponse(response);


                }
            }
        });
    });
}

function showResponse(response) {
    $("#searchResults").html(""); // clears any previous search results
    console.log(response); // Logs the returned quizzes
    response.quizzes.forEach(quiz =>{ // iterates through the quizzes in the array
        // Adds the following html to the end of the div
        $("#searchResults").append(`<div class="border rounded border-primary bg-secondary p-2 m-2">` +
            `<span class="badge badge-primary mr-2">`+quiz.first+" "+quiz.last+`</span>` + //adds the name
            `<div class="float-right">` + //adds a button which calls the playButton fuction with the quizID
            `<button class="playButton btn btn-sm btn-primary ml-2" onclick="playButton(`+quiz.quizID+`)">` +
            `Play Quiz` +
            `</button>` +
            `</div>` + // below contains the quiz name & the number of times it was played
            `<span class="messageText lead  text-light py-2 mx-2">`+quiz.quizTitle+` |`+
            `<span class="text-light small py-2 mx-2">Number of plays: `+quiz.noOfPlays+`</span></span>` +
            `</div>`)

    });
}
function playButton(id) { //Loads the play page with the quizID as a url parameter
    window.location.href = "/client/play.html?quizID="+id;
}