function pageLoad(userInfo) {
    getQuiz();
}
function getQuiz() {
    const searchParams = new URLSearchParams(window.location.search)
    const quizID = searchParams.get("quizID");
    $.ajax({ // Forms the AJAX request
        url: '/quiz/play/'+quizID,  // sends to the search quiz method in quiz controller
        type: 'POST',  // Is a post request as it will be sending data
        success: response => { //Runs once the server responds
            if (response.hasOwnProperty("error")) { //Checks for errors
                alert(response.error); //Displays any errors
            } else {
                console.log("Search submitted successfully") // logs the success
                runQuiz(response);


            }
        }
    });
}
function runQuiz(response) {
    console.log(response);
    response.questions.forEach(questionData =>{
        let questionBox = "<div class=\"media border p-1 my-1\">\n" + // Creates a  html bootstrap media box for the question
            "                <div class=\"media-body text-light col-9\" style=\"\">\n" +
            "                    <h5 class=\"mt-0 mb-1\" >Question</h5>\n" +
            questionData.questionTitle +
            "                    <ul class=\"list-group col-10 jusify-center p-2 mx-auto\">"
        questionData.answers.forEach(answer => { // Iterates through the returned answers
            if(answer.correctAns===true){// checks if the answer is correct -> gets put in green box
                questionBox += "<li class=\"list-group-item list-group-item-success py-1\">"+answer.content+"</li>"
            }//if answer is incorrect it is put into red box
            else{questionBox += "<li class=\"list-group-item list-group-item-danger py-1\">"+answer.content+"</li>"}
        });
        questionBox += " </ul>\n"; // ends the html list
        // below checks if there is an explanation & adds it if it exists
        if(questionData.explanation!=""){questionBox += "Explanation: "+questionData.explanation}
        questionBox += " </div>\n"; //Ends the media body div
        if(questionData.image != ""){ //Checks if an image was returned -> adds it to the html if present
            questionBox +="<img src="+questionData.image+" class=\"img-fluid pt-1 w-25 \" alt=\"Question Image\">"
        }
        questionBox +="</div>" // closes the media div
        $("#questionDiv").append(questionBox); // Adds the assembled HTML to the end of the finished Questions div
    });

}