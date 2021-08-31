/**
 * Created by arash on 13/11/2015.
 */

cimgApp.controller('userPromotionController', function(userAccessToken, $scope,$rootScope, UserService, $state, LOGIN_URI, baseDataService) {

    /*
     if ($window.sessionStorage.userInfo !=undefined ){
     $window.sessionStorage.userInfo = undefined;
     UserService.setUser('reader', '')
     }
     */
     $scope.message = '';
    //$scope.userService = UserService;
    $scope.checkUser = function () {
        var rowObject = $scope.loginForm;
        baseDataService.addRow(rowObject, LOGIN_URI).then(function(response) {
            var userInfo = response.data;
            var isUserHaveAccess = checkUserHasAccessToken(userInfo);
            if (userInfo != undefined && userInfo != null && isUserHaveAccess  )
            {
                UserService.assigneUserAccess(userAccessToken);
                $rootScope.$emit('userPromoted', userAccessToken);
                //$scope.confirm($scope.selectedOption);
                $scope.confirm(true);

            } else if (!isUserHaveAccess)
            {
                $scope.message('User does not have requested access');
            } else {
                $scope.message('Login failed. please try again');
            }
        });
    };

    /**
     * check if user has access to token.
     * @param appUser user
     * @returns {boolean}
     */
    function checkUserHasAccessToken(appUser) {
        if (appUser.accessPoints == undefined) {
            return false;
        }
        for (i =0; i< appUser.accessPoints.length; i++) {
            if (appUser.accessPoints[i].acptToken === userAccessToken) {
                return true;
            }
        }
        if (appUser.appRoles === undefined) {
            return false;
        }
        for (i =0; i< appUser.appRoles.length; i++) {
            appRole = appUser.appRoles[i];
            if (appRole.accessPoints === undefined) {
                continue;
            }
            for (j =0; j< appRole.accessPoints.length; j++) {
                if (appRole.accessPoints[j].acptToken === userAccessToken) {
                    return true;
                }
            }
        }
        return false;
    };
    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

})

