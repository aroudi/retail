/**
 * Created by arash on 13/11/2015.
 */

cimgApp.controller('loginController', function($scope, UserService, $state, LOGIN_URI, baseDataService) {
    $scope.message = "";
    /*
     if ($window.sessionStorage.userInfo !=undefined ){
     $window.sessionStorage.userInfo = undefined;
     UserService.setUser('reader', '')
     }
     */
    if (UserService.getMessage() != null) {
        $scope.message = UserService.getMessage();
    }
    //$scope.userService = UserService;
    $scope.checkUser = function (loginForm) {
        var rowObject = $scope.loginForm;
        baseDataService.addRow(rowObject, LOGIN_URI).then(function(response) {
            var userInfo = response.data;
            if (userInfo != undefined && userInfo != null )
            {
                UserService.setUser(userInfo);
                UserService.setMessage('');
                UserService.setUserAccess(extractUserAccess(userInfo));
                $state.go('dashboard.listSaleTransaction');
            } else
            {
                UserService.setMessage('Login Failed');
                $state.go('dashboard.login');
            }
        });
    };

    function extractUserAccess(appUser) {
        var userAccess = [];
        if (appUser.accessPoints == undefined) {
            return userAccess;
        }
        for (i =0; i< appUser.accessPoints.length; i++) {
            userAccess.push(appUser.accessPoints[i].acptToken);
        }
        if (appUser.appRoles == undefined) {
            return userAccess;
        }
        for (i =0; i< appUser.appRoles.length; i++) {
            appRole = appUser.appRoles[i];
            if (appRole.accessPoints == undefined) {
                continue;
            }
            for (j =0; j< appRole.accessPoints.length; j++) {
                userAccess.push(appRole.accessPoints[j].acptToken);
            }
        }
        return userAccess;
    };
})

