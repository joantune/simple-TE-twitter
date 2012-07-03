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
var Friends;
var Tweets;
var userResultViews = [];
var tweetViews = [];
var friendViews = [];
$(function () {

	User = Backbone.RelationalModel.extend( {
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
	
	var Tweet = Backbone.RelationalModel.extend( {
		initialize: function() {
			console.log("new tweet");
		},
		
		validate:function(attrs) {
			if (!this.isNew() &&(attrs.id ===undefined ||
					!_.isString(attrs.content) ||
					attrs.creationDate ===undefined ||
					attrs.ownerUser === undefined))
				{
				console.log("Error: tweet must have id, content, cretion_date, and owner_user");
				return "Error: tweet must have id, content, cretion_date, and owner_user";
				}
		},
		
		relations: [{
			type: Backbone.HasOne,
			key: 'ownerUser',
			relatedModel: 'User',
			reverseRelation: {
				key: 'tweets',
				includeInJSON: 'id'
			},
		}],
		
		parse: function(parsable) {
			console.log('it is detecting the parsing, that is good');
			return parsable;
		},
		
		url: function() {
			return 'Tweets';
		}
	
	});
	
	var TweetCollection = Backbone.Collection.extend({
		model:Tweet,
		
		comparator: function(tweetA, tweetB) {
			 if (tweetA.get('creationDate') === tweetB.get('creationDate'))
				 //in the 'weird' case, let's use ID
				 {
				   if (tweetA.get('id') > tweetB.get('id')) return -1;
				   if (tweetA.get('id') < tweetB.get('id')) return 1;
				   if (tweetA.get('id') === tweetB.get('id')) 
					   {
					   console.log("Strange, tweets with same ID added!!");
					   return 0;
					   }
				   
				 }
			 if (tweetA.get('creationDate') > tweetB.get('creationDate')) return -1;
			 if (tweetA.get('creationDate') < tweetB.get('creationDate')) return 1;
		}
		
	});
	
	function stringInsensitiveComparator(stringA, stringB) {
		if (stringA === null && stringB != null) return -1;
		if (stringA != null && stringB === null) return 1;
		if (stringA === null && stringB === null) return 0;
		if (!_.isString(stringA) || !_.isString(stringB))
			throw Error('no strings were parsed');
		return stringA.toLowerCase().localeCompare(stringB.toLowerCase());
	}
	
	function fullNameInsensitiveComparator(userA, userB) {
		return stringInsensitiveComparator(userA.get('fullName'), userB.get('fullName'));
	}
	
	FriendListCollection = Backbone.Collection.extend({
		model: User,
		
		comparator: fullNameInsensitiveComparator,
		
		url: function() {
			return "Friends";
		}
		
	});
	
	UserSearchResultCollection = Backbone.Collection.extend({
		model: User,
		url: function() {
			return "search/" + $('#searchInput').val();
		},
		
		comparator: fullNameInsensitiveComparator
	
	});
	
	Tweets = new TweetCollection;
	
	Friends = new FriendListCollection;
	
	SearchResult = new UserSearchResultCollection;
	
	UserSearchResultView = Backbone.View.extend({
		
		template: _.template($('#user-search-result-template').html()),
		
		events: {
			"click .follow"	: "follow"
		},
	
		follow: function() {
			SearchResult.remove(this.model);
			Friends.create(this.model,{wait:true});
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
		template: _.template($('#tweet-template').html()),
		
		render: function() {
			this.$el.html(this.template(this.model.toJSON()));
			return this;
		}
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
			"submit .navbar-search": "searchOnSubmit",
			"click #newTweetButton": "addOwnTweet",
			"keypress #newTweetTA" : "addOwnTweetOnEnter"
				
		},
		
		initialize: function() {
			
			this.friendListEl = this.$('#friend-list');
			this.searchInput = this.$('#searchInput');
			this.tweetListEl = this.$('#tweetList');
			this.newTweetInput = this.$('#newTweetTA');
			
			Friends.bind('add', this.addFriend, this);
			SearchResult.bind('add', this.addSearchResult,this);
			SearchResult.bind('reset', this.addAllSearchResults,this);
			
			Tweets.bind('reset', this.resetTweets,this);
			Tweets.bind('add', this.addTweet, this);
			
			Friends.add(friendList); 
			Tweets.add(tweetInitialList);
			
		},
		
		addFriend: function(friend) {
			if (!friend.isNew())
				{
				var view = new FriendView({model: friend});
				this.friendListEl.append(view.render().el);
				friendViews.push(view);
				Friends.sort();
				}
		},
		
		addTweet: function(tweet) {
			var view = new TweetView({model: tweet});
			this.tweetListEl.append(view.render().el);
			tweetViews.push(view);
			Tweets.sort();
		},
		
		addTweetSkipSort: function(tweet) {
			var view = new TweetView({model: tweet});
			this.tweetListEl.append(view.render().el);
			tweetViews.push(view);
		},
		
		resetTweets: function() {
			//let's remove all of them
			deleteAllViews(tweetViews);
			//, and add all of them
			Tweets.each(this.addTweetSkipSort,this);
			
		},
		
		
		addOwnTweetOnEnter: function(e)
		{
			if (e.keyCode != 13) return;
			if (!this.newTweetInput.val()) return;
			this.addOwnTweet();
		},
		addSearchResult: function(searchResult) {
			$('#searchResultsModal').modal();
			var searchView = new UserSearchResultView({model: searchResult});
			$('#userSearchResult').append(searchView.render().el);
			userResultViews.push(searchView);
		},
		
		addAllSearchResults: function() {
			deleteAllViews(userResultViews);
			SearchResult.each(this.addSearchResult);
		},
		
		addOwnTweet: function(event) {
			Tweets.create({content: this.newTweetInput.val()},{wait: true});
			this.newTweetInput.val('');
			Tweets.sort();
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
	
	function deleteAllViews(arrayOfViews) {
		arrayOfViews.forEach(function(viewElement){viewElement.remove();});
		arrayOfViews = [];
	}
	
	
	var App = new AppView;
	
});
