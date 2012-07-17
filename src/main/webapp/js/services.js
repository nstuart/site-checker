

angular.module('sitecheckerServices', ['ngResource'])
    .factory('LatestResponse', function($resource){
        return $resource('resources/site/:site_id/response/latest')
    })
    .factory('Site', function($resource){
        var service = $resource('resources/site/:id', {})
        return service;
    })
    .factory('Response', function($resource){
        var service = $resource('resources/site/:site_id/response/:id');
        return service
    })
    .factory('Plot', function(){
        var service = {
            site: {id: 'tested'}
        }
        return service;
    })
    