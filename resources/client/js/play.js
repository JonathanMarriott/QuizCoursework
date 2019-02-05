function pageLoad(userInfo) {
    getQuiz();
}
function getQuiz() {
    const searchParams = new URLSearchParams(window.location.search)
    const quizID = searchParams.get("quizID");
    console.log(quizID);
}