Parse.Cloud.afterSave("VandiRating", function(request) {
  var vandiRatingObj = request.object;
  var rating = vandiRatingObj.getInt("rating");
  var vandiId = vandiRatingObj.get("vandiId");
  var count1 = 0;
  var vandiRating = Parse.Object.extend("VandiRating");
  //var vandi = Parse.Object.extend("Vandi");
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
                        var currRating = vandiObj.getDouble("avgRating");
                        var newRating = ((currRating*(count1-1))+rating)/count1;
                        vandiObj.set("avgRating",newRating);
                        vandiObj.save();
                  },
                  error: function(object, error) {
                        // The object was not retrieved successfully.
                        // error is a Parse.Error with an error code and message
                  }
                });
});

