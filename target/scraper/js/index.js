$(function() {
	$("#submit").off();

	$("#submit").on('click', function() {
		var phrase = encodeURIComponent($("#phrase").val());
		var loc = encodeURIComponent($("#loc").val());
		console.log('Phrase: ' + phrase + ' | Loc: ' + loc);
		var checks = [];
		if($("#ypcheck").is(":checked")){
			checks.push('yellowpages');
		}
		if($("#yelpcheck").is(":checked")){
			checks.push('yelp');
		}
		for(domain in checks){
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
			+ '<caption>'+ domain + ' Results</caption>' + '<thead><tr>'
			+ '<th>#</th>' + '<th>Name</th>' + '<th>Link</th>'
			+ '</tr></thead>' + '<tbody id="' + id + 'results'  + '">' + '</tbody>'
			+ '</table>';
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
//			$("#linkgrid").empty();
			$("#linkgrid").append(generateResultsTable(tableID, domain));
			var listings = response.result;
			var items = 0;
			$.each(listings, function(index) {
				if (items > 20)
					return;
				items++;
				var listing = listings[index];
				$("#" + tableID + 'results').append(
						generateTableItem(items, listing.url, listing.name));
			});
		}
	});
} 
