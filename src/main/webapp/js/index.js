$(function() {
	$("#submit").off();
	
//	TODO:
//	$("#savesearch").on('click', function() {
//		var search_form = validateItems();
//		for (domain in search_form.checks) {
//			executeSave(search_form.checks[domain]);
//		}
//    });

	$("#indexform").submit(function(evt) {
		//stop submission
		evt.preventDefault();
		var search_form = validateItems();
		$("#linkgrid").empty();
		for (domain in search_form.checks) {
			executeSearch(search_form.checks[domain], search_form.phrase, search_form.loc);
		}
	});
});

function validateItems(){
	//reset warnings
	$("#phrase").css("background-color", $("#phrase").parent().css("background-color"));
	$("#loc").css("background-color", $("#loc").parent().css("background-color"));
	$("#alrtphrase").text("");
	$("#alrtloc").text("");
	$("#alrtchx").text("");
	//process user input
	var phrase = encodeURIComponent($("#phrase").val());
	if (phrase.length <= 0) {
		$("#phrase").css("background-color", "#ffcccc");
		$("#alrtphrase").text("Please enter a valid search phrase (i.e. 'Mechanic').");
		return;
	}
	var loc = encodeURIComponent($("#loc").val());
	if (loc.length <= 0) {
		$("#loc").css("background-color", "#ffcccc");
		$("#alrtloc").text("Please enter a valid location (i.e. 'San Jose California').");
		return;
	}
	console.log('Phrase: ' + phrase + ' | Loc: ' + loc);
	var checks = [];
	$("input").each(function(chkbox){
		if($(this).prop("checked")){
			checks.push($(this).attr("id"));
		}
	});
	if(checks.length <= 0){
		$("#alrtchx").text("Please select a site to scrape.");
		return;
	}
	return {
		phrase : phrase, 
		loc : loc, 
		checks : checks
	};
}

function generateTableItem(num, url, name) {
	return '<tr>' + '<td>' + num + '</td>' + '<td>' + name + '</td>' + '<td>'
			+ '<a href="' + url + '">' + url + '</a>' + '</td>' + '</tr>';
}

function generateResultsTable(id, domain) {
	return '<table id="' + id + '"' + ' class="table table-bordered">'
			+ '<caption>' + domain + ' Results</caption>' + '<thead><tr>'
			+ '<th>#</th>' + '<th>Name</th>' + '<th>Link</th>'
			+ '</tr></thead>' + '<tbody id="' + id + 'results' + '">'
			+ '</tbody>' + '</table>';
}

function executeSave(domain, phrase, loc){
	$.ajax({
        type: 'POST',
        url: 'http://localhost:8080/scraper/s/save/?phrase=' + phrase + '&loc=' + loc + '&site=' + search_form.checks[domain],
        success: function(json) {

        }
    });
}

function executeSearch(domain, phrase, loc) {
	$.ajax({
		type : 'GET',
		url : 'http://localhost:8080/scraper/s/search/?domain=' + domain + '&phrase='
				+ phrase + '&loc=' + loc,
		success : function(json) {
			var response = JSON.parse(json);
			var tableID = response.tableid;
			var domain = response.domain;
			$("#linkgrid").append(generateResultsTable(tableID, domain));
			var listings = response.result;
			var items = 0;
			//do for each loop in vanillaJS and then append in Jquery.
			$.each(listings, function(index) {
				if (items > 9) {
					return;
				}

				items++;
				var listing = listings[index];
				$("#" + tableID + 'results').append(
						generateTableItem(items, listing.url, listing.name));
			});
		}
	});
}
