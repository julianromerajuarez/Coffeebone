define ['underscore', 'backbone', 'text!../../templates/imageTemplate.html'], (_, Backbone, imageTemplate) ->
  class ImageView extends Backbone.View
    tagName: 'li'
    template:
      _.template imageTemplate
    render: =>
      $(@el).html(@template(@model.toJSON())); @
