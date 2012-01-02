define ['underscore', 'backbone', 'text!../../templates/fooTemplate.html'], (_, Backbone, fooTemplate) ->
  class FooView extends Backbone.View
    tagName: 'li'
    template:
      _.template fooTemplate
    events:
      'click .foo': 'makeBar'
    initialize: =>
      @model.bind('change', @render)
    render: =>
      $(@el).html(@template(@model.toJSON())); @
    makeBar: =>
      @model.set {name: if @model.get("name") is "Foo" then "Bar" else "Foo"}
      @model.save()
