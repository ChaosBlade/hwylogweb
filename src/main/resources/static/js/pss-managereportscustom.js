$("#managereportinputBtn").click(function(e){
    e.preventDefault();
    window.location.href = "/report/active/";
});

function loadmanagereportdetail(seqID){
    window.location.href = "/report/active/"+seqID;
}

// Delete
$("#delcodeBtn").click(function(e){
    e.preventDefault();
    if (confirm("Are you sure you want to delete this item?")) {
    var reportId=$("#reportId").val();
	$("#maskloader").show();
    $.ajax({
        url: "/report/delete/"+reportId,
        xhrFields: {withCredentials: true},
        complete: function (res) {
            window.location.href = "/report/list";
			$("#maskloader").hide();
        }
	});
	}
});
