function pageLoad(currentUser){ // Takes user JSON as parameter
    showProfile(currentUser) // parses the Users details to showProfile
}
function showProfile(currentUser) {
    console.log("Updating heading"); // logs the heading update
    $("#user-heading").html(currentUser.firstName + " " + currentUser.lastName);// chnages the blank heaing to the users name

}
