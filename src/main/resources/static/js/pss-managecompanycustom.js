
//Add Button
$("#manageCompanyinputBtn").click(function(e){
    e.preventDefault(); 
    window.location.href = "/company/active/";
});

function loadmanageCompanydetail(CompanyID){
	window.location.href = "/company/active/"+CompanyID;
}

// Delete
$("#deldataBtn").click(function(e){
    e.preventDefault();
    if (confirm("Are you sure you want to delete this item?")) {
    var CompanyID=$("#CompanyID").val();
	$("#maskloader").show();
    $.ajax({
        url: "/company/delete/"+CompanyID,
        xhrFields: {withCredentials: true},
        complete: function (res) {
            window.location.href = "/company/list";
			$("#maskloader").hide();
        }
	});
	}
});
