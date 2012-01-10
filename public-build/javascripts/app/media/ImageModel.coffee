define ['backbone'], (Backbone) ->
  class ImageModel extends Backbone.Model
    defaults: ->
      name: 'no name'