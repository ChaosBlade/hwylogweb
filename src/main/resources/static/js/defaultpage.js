
	 function str_pad(input, pad_length, pad_string, pad_type) {

	  var half = '',
		pad_to_go;

	  var str_pad_repeater = function(s, len) {
		var collect = '',
		  i;

		while (collect.length < len) {
		  collect += s;
		}
		collect = collect.substr(0, len);

		return collect;
	  };

	  input += '';
	  pad_string = pad_string !== undefined ? pad_string : ' ';

	  if (pad_type !== 'STR_PAD_LEFT' && pad_type !== 'STR_PAD_RIGHT' && pad_type !== 'STR_PAD_BOTH') {
		pad_type = 'STR_PAD_RIGHT';
	  }
	  if ((pad_to_go = pad_length - input.length) > 0) {
		if (pad_type === 'STR_PAD_LEFT') {
		  input = str_pad_repeater(pad_string, pad_to_go) + input;
		} else if (pad_type === 'STR_PAD_RIGHT') {
		  input = input + str_pad_repeater(pad_string, pad_to_go);
		} else if (pad_type === 'STR_PAD_BOTH') {
		  half = str_pad_repeater(pad_string, Math.ceil(pad_to_go / 2));
		  input = half + input + half;
		  input = input.substr(0, pad_length);
		}
	  }

	  return input;
	}
	 
	 
	$.root_ = $('body');
	$.left_panel = $('#left-panel');
	$.shortcut_dropdown = $('#shortcut');
	$.bread_crumb = $('#ribbon ol.breadcrumb');

    // desktop or mobile
    $.device = null;

	/*
	 * APP CONFIGURATION
	 * Description: Enable / disable certain theme features here
	 */		
	$.navAsAjax = true; // Your left nav in your app will no longer fire ajax calls
	
	// Please make sure you have included "jarvis.widget.js" for this below feature to work
	$.enableJarvisWidgets = true;
	
	// Warning: Enabling mobile widgets could potentially crash your webApp if you have too many 
	// 			widgets running at once (must have $.enableJarvisWidgets = true)
	$.enableMobileWidgets = false;


	/*
	 * DETECT MOBILE DEVICES
	 * Description: Detects mobile device - if any of the listed device is detected
	 * a class is inserted to $.root_ and the variable $.device is decleard. 
	 */	
	
	/* so far this is covering most hand held devices */
	var ismobile = (/iphone|ipad|ipod|android|blackberry|mini|windows\sce|palm/i.test(navigator.userAgent.toLowerCase()));

	if (!ismobile) {
		// Desktop
		$.root_.addClass("desktop-detected");
		$.device = "desktop";
	} else {
		// Mobile
		$.root_.addClass("mobile-detected");
		$.device = "mobile";
		
		// Removes the tap delay in idevices
		// dependency: js/plugin/fastclick/fastclick.js 
		// FastClick.attach(document.body);
	}

$(document).ready(function() {

	nav_page_height()



	// COLLAPSE LEFT NAV
	$('.minifyme').click(function(e) {
		$('body').toggleClass("minified");
		$(this).effect("highlight", {}, 500);
		e.preventDefault();
	});

	// HIDE MENU
	$('#hide-menu >:first-child > a').click(function(e) {
		$('body').toggleClass("hidden-menu");
		e.preventDefault();
	});


});



function nav_page_height() {
	var setHeight = $('#main').height();
	//menuHeight = $.left_panel.height();
	
	var windowHeight = $(window).height() - $.navbar_height;
	//set height

	if (setHeight > windowHeight) {// if content height exceedes actual window height and menuHeight
		$.left_panel.css('min-height', setHeight + 'px');
		$.root_.css('min-height', setHeight + $.navbar_height + 'px');

	} else {
		$.left_panel.css('min-height', windowHeight + 'px');
		$.root_.css('min-height', windowHeight + 'px');
	}
}


function check_if_mobile_width() {
	if ($(window).width() < 979) {
		$.root_.addClass('mobile-view-activated')
	} else if ($.root_.hasClass('mobile-view-activated')) {
		$.root_.removeClass('mobile-view-activated');
	}
}



jQuery.fn.doesExist = function() {
	return jQuery(this).length > 0;
};


function runAllForms() {	
	$(".DateFormat").mask("99/99/9999"); 
	$(".ClassIdFormat").mask("99-99-F1"); 
	$(".AciClassIdFormat").mask("99-99-A1"); 
	$(".IAClassIdFormat").mask("99-99-I1"); 
	$('.input-group.date').datepicker({});
	/*
	 * SELECT2 PLUGIN
	 */
	if ($.fn.select2) {
		$('.js-select2').each(function() {
			var $this = $(this);
			var width = $this.attr('data-select-width') || '100%';
			//, _showSearchInput = $this.attr('data-select-search') === 'true';
			$this.select2({
				//showSearchInput : _showSearchInput,
				allowClear : true,
				width : width
			})
		})
	}
}






/*******************************************************
****Common Js
********************************************************/
function pageSetUp() {
	runAllForms();
}
var jsArray = {};

function loadScript(scriptName, callback) {

	if (!jsArray[scriptName]) {
		jsArray[scriptName] = true;
		var body = document.getElementsByTagName('body')[0];
		var script = document.createElement('script');
		script.type = 'text/javascript';
		script.src = scriptName;
		script.onload = callback;
		body.appendChild(script);
	} else if (callback) {
		callback();
	}
}

// Print Checkbox All
$("#selectall").click(function(){
  var checked_status = this.checked;
  $("input[name='printNum[]']").each(function(){
      this.checked = checked_status;
  });
});

// Page Link
function goUrl(pageName){
  window.location.href = pageName;
}

// Back List Button
$("#listdataBtn").click(function(){

if (document.referrer.indexOf("active")>0){
    window.history.go(-2);
} else {
    window.history.go(-1);
}
});

