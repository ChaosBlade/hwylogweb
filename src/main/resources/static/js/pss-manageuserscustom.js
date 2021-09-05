//Add Button
$("#manageUsersinputBtn").click(function(e){
    e.preventDefault(); 
    window.location.href = "/members/active/";
});

function loadmanageUsersdetail(seqID){
	window.location.href = "/members/active/"+seqID;
}


// Delete
$("#deldataBtn").click(function(e){
    e.preventDefault();
    if (confirm("Are you sure you want to delete this item?")) {
        var userID=$("#userID").val();
        $("#maskloader").show();
        $.ajax({
            url: "/members/delete/"+userID,
            xhrFields: {withCredentials: true},
            complete: function (res) {
                window.location.href = "/members/list";
                $("#maskloader").hide();
            }
        });
	}
});


//CHeck Duplicate ID
$("#UserID").change(function(e){
    e.preventDefault();
    var userID=$("#userID").val();
	$("#maskloader").show();
    $.ajax({
        url: "/members/idcheck/"+userID,
        xhrFields: {withCredentials: true},
        complete: function (res) {
            if (res.responseText=="true"){
            alert("User ID already exists");
            $("#adddataBtn").prop( "disabled", true );
            $("#UserID").addClass( "border-danger");
            $("#UserID").addClass( "text-danger");
            $('#basicmanageUsersForm *').prop('disabled', true);
            $("#UserID").prop( "disabled", false );
            } else {
            $("#adddataBtn").prop( "disabled", false );
            $("#UserID").removeClass( "border-danger");
            $("#UserID").removeClass( "text-danger");
            $('#basicmanageUsersForm *').prop('disabled', false);
            }
        }
	});
});
