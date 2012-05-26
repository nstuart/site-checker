
Site = Backbone.Model.extend({
    urlRoot: "/site-checker/resources/site/",
    initialize: function (){
        this.responses = new SiteResponses()
        this.responses.url = this.get('responses_url')
        this.latest = new SiteResponse()
        this.latest.url = this.get('latest_url')
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
        if(this.get('code') > 400){
            img = 'slow_img'
        }else if(this.get('responseTime') > 1000){
            img = 'slow_img'
        }else if(this.get('responseTime') > 500){
            img = 'medium_img'
        }
        return this.get(img)
    }
})

//Needs to be passed a Site to work correctly.
SiteResponses = Backbone.Collection.extend({
    site_id     :   null,
    model       :   SiteResponse,
    initialize  :   function(options){
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
    start       :   new Date().getTime() - ((1000 * 60) * 60),
    options     :   {
        xaxis: {
            mode: "time"
        },
        series: {
            lines: {
                show: true
            }
        },
        grid: {
            hoverable: true, 
            clickable: true
        }
    },
    plot_obj    :   null,
    el          :   '#plot_holder',
    initialize  :   function(options){
        if(options.options){
            this.options = options.options
        }
        if(options.start){
            this.start = options.start
        }
        _.bindAll(this, 'render')
        this.plot_obj = $.plot($(this.el), [this.getData()], this.options)
        $(this.el).bind("plotclick", function (event, pos, item) {
            if (item) {
                this.plot_obj.highlight(item.series, item.datapoint);
            }
        });
    },
    render      :   function(){
        this.plot_obj.setData([this.getData()])
        this.plot_obj.setupGrid()
        this.plot_obj.draw()
        return this
    },
    getData     :   function(){
        var data = []
        this.model.fetch({
            async   : false,
            data    :   {
                start: this.start
            }
        })
        this.model.each(function(resp){
            data.push([resp.get('createdAt'), (resp.get('responseTime')) / 1000.0])
        })
        return {
            data: data, 
            label: this.model.at(0).get('site').url
        }
    }
})