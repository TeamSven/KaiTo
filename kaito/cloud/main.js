Parse.Cloud.afterSave("VandiRating", function(request) {
  var vandiRatingObj = request.object;
  var rating = vandiRatingObj.get("rating");
  var vandiId = vandiRatingObj.get("vandiId");
  var count1 =0;
  var vandiRating = Parse.Object.extend("VandiRating");
  var vandi = Parse.Object.extend("Vandi");
  var vandiRatingQuery = new Parse.Query(vandiRating);
  vandiRatingQuery.equalTo("vandiId", vandiId);
  vandiRatingQuery.count({
	  success: function(count) {
	  		count1  = count;
	}
  });

		
	  var vandi = Parse.Object.extend("Vandi");
	  var vandiQuery = new Parse.Query(vandi);
	  vandiQuery.get(vandiId, {
	  success: function(vandiObj) {
			// The object was retrieved successfully.
			
			var currRating = (vandiObj.get("avgRating")||0);
		var newRating = ((currRating*(count1-1))+rating)/count1;
			
			vandiObj.set("avgRating",newRating);
			vandiObj.save();
		  },
		  error: function(object, error) {
			// The object was not retrieved successfully.
			// error is a Parse.Error with an error code and message.
		  }
		});

			
	 
});


Parse.Cloud.beforeSave("VandiRating",function(request)) {
var vandiRatingObj = request.object;
var userId = vandiRatingObj.get("user");
var vandiId = vandiRatingObj.get("vandiId");
var newVandiRating;
var vandiRating = Parse.Object.extend("VandiRating");
var vandi = Parse.Object.extend("Vandi");
var vandiRatingQuery = new Parse.Query(vandiRating);
vandiRatingQuery.equalTo("vandiId", vandiId);
vandiRatingQuery.equalTo("user",userId)({
	success: function(list) {
	if (list.length > 0) 
	{
		newVandiRating = list[0];
		newVandiRating.set("rating",vandiRatingObj.get("rating"));
		request.object = newVandiRating;
	}
  	},
	error: function(object,error){
	}

});

}
