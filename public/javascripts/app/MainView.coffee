define ['backbone', 'cs!FooView'], (Backbone, FooView) ->
	class MainView extends Backbone.View
		constructor: (@fooCollection) -> super
		el: $('#foo-app')
		events:
			'click #new-foo': 'makeFoo'
		initialize: =>
			@fooCollection.bind('add', @addFoo)
			@fooCollection.bind('reset', @addAll)
			@fooCollection.bind('all', @render)
			@fooCollection.fetch()
		render: (event) =>
      fooCount = @fooCollection.foos().length
      barCount = @fooCollection.bars().length
      $('#foo-count').text("#{fooCount} foos and #{barCount} bars")
		makeFoo: =>
			@fooCollection.create()
		addFoo: (foo) =>
			view = new FooView {model: foo}
			$('#foo-list').append(view.render().el)
		addAll: =>
			@fooCollection.each @addFoo