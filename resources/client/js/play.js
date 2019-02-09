function pageLoad(userInfo) {
    getQuiz();
}
function getQuiz() {
    //Gets the quizID from the url parameter
    const searchParams = new URLSearchParams(window.location.search);
    const quizID = searchParams.get("quizID");
    if(quizID==null){ // if no quizID redirect to search page
        alert("No quiz selected");
        window.location.href = "/client/search.html";
    }
    $.ajax({ // Forms the AJAX request
        url: '/quiz/play/'+quizID,  // sends ID to the play quiz method in quiz controller
        type: 'POST',  // Is a post request as it will be sending data to the server
        success: response => { //Runs once the server responds
            if (response.hasOwnProperty("error")) { //Checks for errors
                alert(response.error); //Displays any errors
            } else {
                console.log("QuizID submitted successfully") // logs the success
                runQuiz(response);
            }
        }
    });
}
function buttonPress(){
    $(".list-group-item").click(function(){ // runs when one of the answer buttons is clicked
        $("#submitBtn").removeAttr("disabled"); // enables the submit button as an answer is selected
        $(".list-group-item").removeClass("active");// resets all the buttons to unselected
        $(this).toggleClass("active");// makes the clicked button highlighted via active class
    })
}
function showQuestion(quizData,counter) {
    console.log(counter);
    console.log("Question id "+quizData.questions[counter].questionID)
    let questionArea = "<h4 class='font-weight-normal text-light text-center pb-3'>"+
        quizData.questions[counter].questionTitle+"</h4><div class=\"row\">\n" + //adds question title
        "                <div class=\"list-group col-6\" id=\"answerDiv\">"
    quizData.questions[counter].answers.forEach(answer =>{ //iterates through answers and addthem to html
        questionArea += "<button type=\"button\" class=\"list-group-item list-group-item-action\" \n" +
            "                            value="+answer.correctAns+">"+answer.content+"</button>";
    });
    questionArea += "</div>\n" +
        "                <div id=\"imgDiv\" class=\"col-6\">"
    if(quizData.questions[counter].image!==""){ //adds the image if it exists
        questionArea += `<img class="img-fluid pt-1 w-75" src=`+quizData.questions[counter].image+`>`
    }
    questionArea += `</div>
            </div>
        <div class="form-group col-12 pt-4 text-center justify-content-center">
            <button class="btn btn-lg btn-primary justify-content-center" disabled="true" id="submitBtn"type="submit">Submit</button>
        </div>`;
    $("#questionDiv").html(questionArea);
    buttonPress()
    $("#submitBtn").click(function(){ // runs when submit pressed
        // below checked if selected answer correct & converts to boolean
        let correct = $("#answerDiv").find(".active").val() === "true";
        if (correct){alert("Well done correct answer!")}
        else{alert("Answer was incorrect")}
        $.ajax({ // Forms the AJAX request
            url: '/questionAttempt/add', // sends ID to the add question attempt method in QuestionAttemptController
            type: 'POST',  // Is a post request as it will be sending data to the server
            // below converts the key values pairs to the URL encoded format
            data: $.param({"attemptID":quizData.attemptID,"questionID":quizData.questions[counter].questionID,"correct":correct}),
            success: response => { //Runs once the server responds
                if (response.startsWith('Error:')) { //Checks for errors
                    alert(response); //Displays any errors
                    return; //breaks the recursive call stack

                } else {
                    if(counter === quizData.questions.length-1){ // base case have all the questions been shown
                        displayFinishedQuiz(quizData); // Displays all questions with answers
                    }
                    else{
                        return showQuestion(quizData,++counter); // recursively calls with the counter incremented
                    }
                }
            }
        });
    });

}
function runQuiz(quizData) {
    console.log(quizData); // For debugging server response
    $("#quizHeader").html(quizData.quizTitle); // Adds the quiz title to heading
    let counter = 0;// Creates a counter
    showQuestion(quizData,counter); // Recursively displays the questions on the page & handles form submit
}

function displayFinishedQuiz(quizData) {
    console.log("Quiz Finished")
    $("#questionDiv").html("<h4 class='font-weight-normal text-light text-center pb-3'> Quiz finished - see explanations below</h4>");
    quizData.questions.forEach(questionData =>{
        let questionBox = "<div class=\"media border p-1 my-1\">\n" + // Creates a  html bootstrap media box for the question
            "                <div class=\"media-body text-light col-9\" style=\"\">\n" +
            "                    <h5 class=\"mt-0 mb-1\" >Question</h5>\n" +
            questionData.questionTitle +
            "                    <ul class=\"list-group col-10 justify-center p-2 mx-auto\">"
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