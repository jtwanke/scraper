$(function() {
	$("#submit").off();

	$("#indexform").submit(function(evt) {
		//stop submission
		evt.preventDefault();
		//reset warnings
		$("#phrase").css("background-color", $(this).css("background-color"));
		$("#loc").css("background-color", $(this).css("background-color"));
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
		//change these to for ... in loop
		if($("#ypcheck").is(":checked")) {
			checks.push('yellowpages');
		}
		if($("#yelpcheck").is(":checked")) {
			checks.push('yelp');
		}
		console.log("The checks length: " + checks.length.toString());
		if(checks.length <= 0){
			$("#alrtchx").text("Please select a site to scrape.");
			return;
		}
		$("#linkgrid").empty();
		for (domain in checks) {
			executeSearch(checks[domain], phrase, loc);
		}
	});
});

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

function executeSearch(domain, phrase, loc) {
	$.ajax({
		type : 'GET',
		url : 'http://localhost:8080/scraper/s/search/' + domain + '?phrase='
				+ phrase + '&loc=' + loc,
		success : function(json) {
			var response = JSON.parse(json);
			var tableID = response.tableid;
			var domain = response.domain;
			$("#linkgrid").append(generateResultsTable(tableID, domain));
			var listings = response.result;
			var items = 0;
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
