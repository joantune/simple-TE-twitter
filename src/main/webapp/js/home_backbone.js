/*
Copyright (C) 2012 João Antunes

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>
*/
var User;
var FriendListCollection;
var SearchResult;
var UserSearchResultView;
$(function () {

	User = Backbone.Model.extend( {
		initialize: function () {
			if (!this.get("id") || !this.get("username") || !this.get("fullName") || !this.get("icon")) {
				//TODO error
				console.log("Error initializing a Friend model. Not all of the arguments were parsed");
			}
		},
		
		defaults: function() {
			return {
				icon: defaultUserLogoUrl
			};
		},
	
		
		validate: function(attrs) {
			if (attrs.id === undefined ||
					attrs.username === undefined ||
					attrs.fullName === undefined ||
					attrs.icon === undefined )
				{
				console.log("Error: must define username, id, fullName, and icon");
				return "must define username, id, fullName, and icon";
				}
			//TODO - maybe - check to see if we don't have duplicates in the friends collection?!
			
		},
		
		unfollow: function() {
			//remote jquery unfollow task
			//TODO
			console.log("unfollow of user " + this.get("username"));
		},
		
		follow: function() {
			//remote jquery follow task
			//TODO
			console.log("follow of user " + this.get("username"));
		}
		
	});
	
	var Tweet = Backbone.Model.extend( {
		initialize: function() {
			console.log("new tweet");
		},
		
		validate: function(attrs) {
			if (attrs.id ===undefined ||
					!_.isString(attrs.content) ||
					attrs.creation_date ===undefined ||
					attrs.owner_user === undefined)
				{
				console.log("Error: tweet must have id, content, cretion_date, and owner_user");
				return "Error: tweet must have id, content, cretion_date, and owner_user";
				}
		}
		
	
	});
	
	var TweetCollection = Backbone.Model.extend({
		model:Tweet,
		
	});
	
	FriendListCollection = Backbone.Collection.extend({
		model: User
	});
	
	UserSearchResultCollection = Backbone.Collection.extend({
		model: User,
		url: function() {
			return "search/" + $('#searchInput').val();
		}
	});
	
	var Tweets = new Tweet;
	
	var Friends = new FriendListCollection;
	
	SearchResult = new UserSearchResultCollection;
	
	UserSearchResultView = Backbone.View.extend({
		
		template: _.template($('#user-search-result-template').html()),
		
		events: {
			"click .follow"	: "follow"
		},
	
		follow: function() {
			this.model.follow();			
		},
		
		initialize: function() {
			this.model.bind('destroy', this.remove, this);
		},
		
		
		render: function() {
			this.$el.html(this.template(this.model.toJSON()));
			return this;
		}
		
		
	});
	
	var TweetView = Backbone.View.extend({
		template: _.template($('#tweet-template').html())
	});
	
	
	var FriendView = Backbone.View.extend({
		
		template: _.template($('#friend-template').html()),
		
		events: {
			"click .unfollow"	: "unfollow"
		},
	
		unfollow: function() {
			this.model.unfollow();			
		},
		
		initialize: function() {
			this.model.bind('destroy', this.remove, this);
		},
		
		render: function() {
			this.$el.html(this.template(this.model.toJSON()));
			return this;
		}
		
		
	});
	$().alert();
	
	var AppView = Backbone.View.extend({
		
		el: $("#main"),
		
		events: {
			"submit .navbar-search": "searchOnSubmit"
		},
		
		initialize: function() {
			
			this.friendListEl = this.$('#friend-list');
			this.searchInput = this.$('#searchInput');
			
			Friends.bind('add', this.addFriend, this);
			SearchResult.bind('add', this.addSearchResult,this);
			SearchResult.bind('reset', this.addAllSearchResults,this);
			
			Friends.add(friendList); 
			
		},
		
		addFriend: function(friend) {
			var view = new FriendView({model: friend});
			this.friendListEl.append(view.render().el);
		},
		
		addSearchResult: function(searchResult) {
			$('#searchResultsModal').modal();
			var searchView = new UserSearchResultView({model: searchResult});
			$('#userSearchResult').append(searchView.render().el);
		},
		
		addAllSearchResults: function() {
			SearchResult.each(this.addSearchResult);
		},
		
		searchOnSubmit: function(e) {
			if (!this.searchInput.val()) return false;
			$.blockUI({message : "<h1>Loading data</h1>"});
			$('#connectionError').hide();
			
			SearchResult.fetch({
				success : function (collection, response) {
					console.log('got a success!');
					console.log('collection: ' + JSON.stringify(collection));
					console.log('response: ' + JSON.stringify(response));
					$.unblockUI();
					
					
				},
				error : function (collection, response) {
				console.log('got a error!');
				console.log('collection: ' + JSON.stringify(collection));
				console.log('response: ' + JSON.stringify(response));
				$.unblockUI();
				$('#connectionError').show();
				
			}});
			
			return false;
			
			
		}
		
	});
	
	
	var App = new AppView;
	
});
