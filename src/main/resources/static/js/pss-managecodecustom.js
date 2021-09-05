//Add Button
$("#codeinputBtn").click(function(e){
    e.preventDefault(); 
    window.location.href = "/code/active/";
});

function loadmanagecodedetail(seqID){
    window.location.href = "/code/active/"+seqID;
}



// Delete sample card
$("#delcodeBtn").click(function(e){
    e.preventDefault();
    if (confirm("Are you sure you want to delete this item?")) {
    var codeSeq=$("#codeSeq").val();
	$("#maskloader").show();
    $.ajax({
        url: "/code/delete/"+codeSeq,
        xhrFields: {withCredentials: true},
        complete: function (res) {
            window.location.href = "/code/list";
			$("#maskloader").hide();
        }
	});
	}
});