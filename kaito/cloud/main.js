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

Parse.Cloud.beforeSave("VandiRating",function(request) {

var vandiRatingObj = request.object;
var userId = vandiRatingObj.get("userId");
var vandiId = vandiRatingObj.get("vandiId");
var newVandiRating;
var vandiRating = Parse.Object.extend("VandiRating");
var vandi = Parse.Object.extend("Vandi");
var vandiRatingQuery = new Parse.Query(vandiRating);

vandiRatingQuery.equalTo("vandiId", vandiId);
vandiRatingQuery.equalTo("userId", userId)({
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
});


Parse.Cloud.afterSave("FoodRating", function(request) {

 var foodRatingObj = request.object;
 var rating = foodRatingObj.get("rating");
 var foodItemId = foodRatingObj.get("foodItemId");
 var count1 =0;
 var foodRating = Parse.Object.extend("FoodRating");
 var foodItem = Parse.Object.extend("FoodItem");
 var foodRatingQuery = new Parse.Query(foodRating);

 foodRatingQuery.equalTo("foodItemId", foodItemId);
 foodRatingQuery.count({
         success: function(count) {
                       count1  = count;
       }
 });
         var foodItemQuery = new Parse.Query(foodItem);
         foodItemQuery.get(foodItemId, {
         success: function(foodItemObj) {
                       // The object was retrieved successfully.
                       var currRating = (foodItemObj.get("avgRating")||0);
               var newRating = ((currRating*(count1-1))+rating)/count1;
                       foodItemObj.set("avgRating",newRating);
                       foodItemObj.save();
                 },
                 error: function(object, error) {
                       // The object was not retrieved successfully.
                       // error is a Parse.Error with an error code and message.
                 }
               });
});


Parse.Cloud.beforeSave("FoodRating",function(request) {

var foodRatingObj = request.object;
var userId = foodRatingObj.get("userId");
var foodItemId = foodRatingObj.get("foodItemId");
var newFoodRating;
var foodRating = Parse.Object.extend("FoodRating");
var foodRatingQuery = new Parse.Query(foodRating);

foodRatingQuery.equalTo("foodItemId", foodItemId);
foodRatingQuery.equalTo("user",userId)({
       success: function(list) {
       if (list.length > 0)
       {
               newFoodRating = list[0];
               newFoodRating.set("rating",foodRatingObj.get("rating"));
               request.object = newFoodRating;
       }
       },
       error: function(object,error){
       }
});
});
