
angular.module('sitechecker', ['sitecheckerServices']).
    config(['$routeProvider', function($routeProvider){
        $routeProvider.when('/sites', {
            templateUrl: 'partials/sites.html', 
            controller: SitesController
        }).
        otherwise({
            redirectTo: '/sites'
        })
    }
    ])