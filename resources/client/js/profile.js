function pageLoad(currentUser){
    showProfile(currentUser)
}
function showProfile(currentUser) {
    console.log(currentUser);
    $("#user-heading").html(currentUser.firstName + " " + currentUser.lastName);

}