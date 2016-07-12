/**
 * Created by arash on 13/11/2015.
 */

cimgApp.controller('logoutController', function($http, $scope, UserService, $state, LOGOUT_URI, baseDataService) {
    //$scope.userService = UserService;
    baseDataService.getBaseData(LOGOUT_URI).then(function(response){
    });
    UserService.removeUser();
    $state.go('dashboard.login');
})

