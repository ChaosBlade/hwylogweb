
$("#managemailinputBtn").click(function(e){
    e.preventDefault();
    window.location.href = "/hwyemaillog/active/";
});


function loadmaillogdetail(logID){
	window.location.href = "/hwyemaillog/active/"+logID;
}


$("#managemailfullprintBtn").click(function(e){
    e.preventDefault();

    var form=$("#searchformhwylog");
    $.ajax({
        url: "/hwyemaillog/allhwyemaillog",
		data: form.serialize(),
        xhrFields: {withCredentials: true},
        dataType: "json",
        success: function (res) {
            seqArr = new Array();
            for(var i=0; i<res.length; i++){
                seqArr.push(res[i]);
             }
            if (seqArr.length>0){
            $("#maskloader").show();
            $("#reportinfoModalTitle").empty();
            $("#reportinfoModalTitle").append("<h4 class='modal-title'><i class='fa fa-print'></i> </span>HWY-E Mail Log Report </h4> ")
            $("#reportinfoModal").modal('show');
            $("#reportinfopdf").css("height",$(window).height()-$(window).height()/4);
            $('#reportinfopdf').html("<iframe name='your_frame_name' src='/hwyemailreport/"+seqArr+"'  height=100% width=100%> </iframe>")
            $("#maskloader").hide();
            }
        }
	});

});







$("#printPreviewBtn").click(function(e){
    e.preventDefault();
    seqArr = new Array();

    var totalcount=document.getElementsByName('printNum[]').length;

    for(var i=0; i<totalcount; i++){
     if (document.getElementsByName('printNum[]')[i].checked==true){
        seqArr.push(document.getElementsByName('printNum[]')[i].value);
     }
    }
    if (seqArr.length>0){
    $("#maskloader").show();
    $("#reportinfoModalTitle").empty();
    $("#reportinfoModalTitle").append("<h4 class='modal-title'><i class='fa fa-print'></i> </span>HWY-E Mail Log Report </h4> ")
    $("#reportinfoModal").modal('show');
	$("#reportinfopdf").css("height",$(window).height()-$(window).height()/4);
	$('#reportinfopdf').html("<iframe name='your_frame_name' src='/hwyemailreport/"+seqArr+"'  height=100% width=100%> </iframe>")
    $("#maskloader").hide();
    }
});





// Delete sample card
$("#deldataBtn").click(function(e){
    e.preventDefault();
    if (confirm("Are you sure you want to delete this item?")) {
    var logID=$("#logID").val();
	$("#maskloader").show();
    $.ajax({
        url: "/hwyemaillog/delete/"+logID,
        xhrFields: {withCredentials: true},
        complete: function (res) {
            window.location.href = "/hwyemaillog/list";
			$("#maskloader").hide();
        }
	});
	}
});



























