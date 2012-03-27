
Site = Backbone.Model.extend({
    defaults: {
        url: null
    }
})

SiteResponse = Backbone.Model.extend({
    defaults:{
        site: null,
        code: 0,
        responseTime: 0,
        responses: [],
        slow_img: "img/17.png",
        medium_img: "img/26.png",
        normal_img: "img/32.png"
    },    
    initialize: function(){
        
    },
    status_img: function(){
        var img = 'normal_img'
        if(this.get('responseTime') > 1000){
            img = 'slow_img'
        }else if(this.get('responseTime') > 500){
            img = 'medium_img'
        }
        return this.get(img)
    },
    url: function(){
        if(this.get('latest')){
            return '/site-checker/resources/site/' + this.get('site_id') + '/response/latest';
        }else{
            return '/site-checker/resources/site/' + this.get('site_id') + '/response/' + this.get('id');
        }
    }
})

//Needs to be passed a Site to work correctly.
SiteResponses = Backbone.Collection.extend({
    initialize  :   function(){
        
    },
    model       :   SiteResponse,
    url         :   function(){
        
    }
})

Sites = Backbone.Collection.extend({
    model   : Site,
    url     : '/site-checker/resources/site'
})


ResponseView = Backbone.View.extend({
    
    initialize: function(){
        _.bindAll(this, 'render'); //make sure when 'render' is called (no matter the context) that 'this' always refers to this View object
        this.model.bind('change', this.render) //when the model changes, render the view
        this.render()
    },
    render: function(){
        console.log('updating ' + this.model.get('site').url)
        var ops = this.model.toJSON();
        ops['status_img'] = this.model.status_img()
        ops['url'] = this.model.get('site').url
        var temp = _.template($('#site_template').html(), ops)
        $(this.el).html(temp)
        if($(this.el).is(':visible')){
            $(this.el).effect('highlight', {}, 2000)
        }
        return this
    }
})

PlotView = Backbone.View.extend({
    
})