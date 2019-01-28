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
    $("#titleDiv").html('<h4 class="font-weight-normal text-light">Quiz Title: ' + quizData.quizTitle + '</h4>') // Replaces the title form with the submitted title
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
        '                        <button class="btn btn-primary btn-block" type="submit">Submit</button> <!--button to submit the form-->\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '    </form>');
    questionForm(quizData);
}

function questionForm(quizData){
    const form = $("#questionForm"); // selects the form element
    form.submit(event =>{
        event.preventDefault(); // Prevents the default form behaviour
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
    });

}