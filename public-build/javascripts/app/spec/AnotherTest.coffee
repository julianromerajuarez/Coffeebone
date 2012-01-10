define ['cs!FooCollection'], (FooCollection) ->
  describe 'FooCollection:', ->
    it 'should have no items', ->
      expect((new FooCollection).size()).toEqual(0);

