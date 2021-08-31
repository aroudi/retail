/**
 * Created by arash on 13/11/2015.
 */

cimgApp.controller('userIdleController', function($http, $scope, $rootScope, userIdleService) {
    userIdleService.loggOutUser();
})

