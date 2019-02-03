function pageLoad(userInfo){
    titleForm();
}
function titleForm(){
    const form = $("#titleForm");
    form.submit(event => { //Runs the code enclosed when the form submits
        event.preventDefault(); // Stops the default form GET behaviour
        $.ajax({ // Forms the AJAX request
            url: '/quiz/create',  // sends to the add quiz method in quiz controller
            type: 'POST',  // Is a post request as it will be changing data
            data: form.serialize(), // Converts form data to a URL encoded string
            success: response => { //Runs once the server responds
                if (response.hasOwnProperty("error")) { //Checks for errors
                    alert(response.error); //Displays any errors
                } else {
                    console.log("Quiz title Submitted successfully") // logs the success
                    titleFinish(response);


                }
            }
        });
    });
}
function titleFinish(quizData) {
    $("#titleDiv").html('<h4 class="font-weight-normal text-light">Quiz Title: ' + quizData.quizTitle  +
        '</h4><a class="btn btn-primary btn" href ="/client/profile.html" role="button">Finished Quiz</a>') // Replaces the title form with the submitted title
    //Code below inserts the form to add a question to the quiz
    $("#formDiv").html('<h4 class="font-weight-normal text-light">Add a question</h4>' +
        '<form class="form justify-content-center" id="questionForm" enctype="multipart/form-data">\n' +
        '                <div class="form justify-content-center ">\n' +
        '                    <div class="form-group">\n' +
        '                        <label for="inputQuestion" class="sr-only">Enter the question</label> <!--Hidden label for form field below-->\n' +
        '                        <input type="text" id="inputQuestion" name="question" class="form-control" placeholder="Enter the Question" required>\n' +
        '                    </div>\n' +
        '                    <div class="form-group form-row">\n' +
        '                        <div class="form-group col-7">\n' +
        '                            <label for="inputAnswer1" class="sr-only">Enter Answer 1</label> <!--Hidden label for answer box below-->\n' +
        '                            <input type="text" id="inputAnswer1" name="answer1" class="form-control" placeholder="Enter Answer 1" required>\n' +
        '                        </div>\n' +
        '                        <div class="form-check form-check-inline col-3 mx-3">\n' +
        '                            <input type="checkbox" class="form-check-input" id="ansCheck1" name="checkAns1"><!--Users selects if answer is correct-->\n' +
        '                            <label class="form-check-label font-weight-normal text-light" for="ansCheck1">Correct?</label>\n' +
        '                        </div>\n' +
        '                        <div class="form-group col-7">\n' +
        '                            <label for="inputAnswer2" class="sr-only">Enter Answer 2</label> <!--Hidden label for form field below-->\n' +
        '                            <input type="text" id="inputAnswer2" name="answer2" class="form-control" placeholder="Enter Answer 2" required>\n' +
        '                        </div>\n' +
        '                        <div class="form-check form-check-inline col-3 mx-3">\n' +
        '                            <input type="checkbox" class="form-check-input" id="ansCheck2" name="checkAns2">\n' +
        '                            <label class="form-check-label font-weight-normal text-light" for="ansCheck2">Correct?</label>\n' +
        '                        </div>\n' +
        '                        <div class="form-group col-7">\n' +
        '                            <label for="inputAnswer1" class="sr-only">Enter Answer 3</label> <!--Hidden label for form field below-->\n' +
        '                            <input type="text" id="inputAnswer3" name="answer3" class="form-control" placeholder="Enter Answer 3" required>\n' +
        '                        </div>\n' +
        '                        <div class="form-check form-check-inline col-3 mx-3">\n' +
        '                            <input type="checkbox" class="form-check-input" id="ansCheck3" name="checkAns3">\n' +
        '                            <label class="form-check-label font-weight-normal text-light" for="ansCheck3">Correct?</label>\n' +
        '                        </div>\n' +
        '                        <div class="form-group col-7">\n' +
        '                            <label for="inputAnswer1" class="sr-only">Enter Answer 4</label> <!--Hidden label for form field below-->\n' +
        '                            <input type="text" id="inputAnswer4" name="answer4" class="form-control" placeholder="Enter Answer 4" required>\n' +
        '                        </div>\n' +
        '                        <div class="form-check form-check-inline col-3 mx-3">\n' +
        '                            <input type="checkbox" class="form-check-input" id="ansCheck4" name="checkAns4">\n' +
        '                            <label class="form-check-label font-weight-normal text-light" for="ansCheck4">Correct?</label>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                    <div class="form-group">\n' +
        '                        <label for="explanation" class="sr-only">Enter an explanation for the answer (optional)</label>\n' +
        '                        <textarea class="form-control" id="explanation" rows="3" name="explanation" placeholder="Enter an explanation for the answer (optional)"></textarea>\n' +
        '                    </div>\n' +
        '                    <div class="form-group form-row justify-content-start"><!--file upload-->\n' +
        '                        <label for="picture" class="font-weight-normal text-light justify-content-start">Add a picture(optional)</label>\n' +
        '                        <input type="file" class="form-control-file font-weight-normal text-light" id="picture" name="picture">\n' +
        '                    </div>\n' +
        '                    <div class="form-group col-2"><!--button div, col-2 sets the relative length for the button-->\n' +
        '                        <button class="btn btn-primary" type="submit">Submit</button> <!--button to submit the form-->\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '    </form>');
    questionForm(quizData);
}

function questionForm(quizData){
    console.log(quizData);
    const form = $("#questionForm"); // selects the form element
    form.submit(event =>{
        event.preventDefault(); // Prevents the default form behaviour
        //checks at least one answer is correct
        if(!($("#ansCheck1").is(':checked') || $("#ansCheck2").is(':checked') || $("#ansCheck3").is(':checked') || $("#ansCheck4").is(':checked'))){
            alert("Select at least one correct answer")
        }
        else{
        let formData = new FormData($("#questionForm")[0]); //Constructs form data from the fields
        console.log(quizData.quizID);
        formData.append("quizID",quizData.quizID);//Adds the quizID to the data payload
        $.ajax({ // forms ajax request
            url: '/question/create', //Sends to the question controller class
            type: 'POST',
            data: formData,
            success: response => { //Runs once the server responds
                if (response.hasOwnProperty("error")) { //Checks for errors in the JSON object
                    alert(response.error); //Displays any errors
                } else {
                    console.log("Question submitted successfully") // logs the success
                    addQuestion(response);
                }},
            cache: false, //needed due to sending file
            contentType: false,//needed due to sending file
            processData: false//needed due to sending file
        });
    }});
}
function addQuestion(questionData){
    console.log(questionData); // Logs the JSON returned for debugging
    $("#questionForm").trigger("reset"); // resets the question form
    let questionBox = "<div class=\"media border p-1 my-1\">\n" + // Creates a  html bootstrap media box for the question
        "                <div class=\"media-body text-light col-9\" style=\"\">\n" +
        "                    <h5 class=\"mt-0 mb-1\" >Question</h5>\n" +
        questionData.questionTitle +
        "                    <ul class=\"list-group col-10 jusify-center p-2 mx-auto\">"
    for(let i=0;i<4; i++){ // Iterates through the returned answers
        if(questionData.checkAns[i]==="on"){// checks if the answer is correct -> gets put in green box
            questionBox += "<li class=\"list-group-item list-group-item-success py-1\">"+questionData.answers[i]+"</li>"
        }//if answer is incorrect it is put into red box
        else{questionBox += "<li class=\"list-group-item list-group-item-danger py-1\">"+questionData.answers[i]+"</li>"}
    }
    questionBox += " </ul>\n"; // ends the html list
    // below checks if there is an explanation & adds it if it exists
    if(questionData.explanation!=""){questionBox += "Explanation: "+questionData.explanation}
    questionBox += " </div>\n"; //Ends the media body div
    if(questionData.image != ""){ //Checks if an image was returned -> adds it to the html if present
        questionBox +="<img src="+questionData.image+" class=\"img-fluid pt-1 w-25 \" alt=\"Question Image\">"
    }
    questionBox +="</div>" // closes the media div
    $("#finishedQuestions").append(questionBox); // Adds the assembled HTML to the end of the finished Questions div
}