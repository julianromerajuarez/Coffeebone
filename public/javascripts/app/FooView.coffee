define ['underscore', 'backbone', 'text!../../templates/fooTemplate.html'], (_, Backbone, fooTemplate) ->
  class FooView extends Backbone.View
    tagName: 'li'
    className: 'foo-row'
    template:
      _.template fooTemplate
    events:
      'click .foo': 'makeBar'
      'click span.foo-destroy': 'clear'
    initialize: =>
      @model.bind('change', @render)
      @model.bind('destroy', @remove)
    render: =>
      $(@el).html(@template(@model.toJSON())); @
    makeBar: =>
      @model.set {name: if @model.get("name") is "Foo" then "Bar" else "Foo"}
      @model.save()
    clear: =>
      @model.destroy()
    remove: =>
      $(@el).remove()


