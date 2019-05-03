/**
 * Created by arash on 13/11/2015.
 */

cimgApp.controller('logoutController', function($http, $scope, $rootScope, userIdleService,multiPageService, $state, LOGOUT_URI, baseDataService) {
    //$scope.userService = UserService;
    baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to logout?').then(function(result){
        if (result) {
            userIdleService.loggOutUser();
            /*
            baseDataService.getBaseData(LOGOUT_URI).then(function(response){
            });
            UserService.removeUser();
            multiPageService.removePageList();
            var userLoggedIn = '';
            $rootScope.$emit('loginChanged', userLoggedIn);
            $state.go('dashboard.login');
            */
        } else {
            return;
        }
    });

})

